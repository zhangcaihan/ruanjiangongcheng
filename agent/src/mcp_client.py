"""
MCP Client 封装 - 提供同步接口供 Streamlit 使用
"""
import asyncio
import subprocess
import sys
import time
import threading
from pathlib import Path
from typing import Dict, Any, List, Optional
import atexit

from mcp import ClientSession
from mcp.client.stdio import stdio_client, StdioServerParameters


class MCPClient:
    """MCP 客户端封装类，提供同步接口"""
    
    def __init__(self, server_script: str, python_exe: Optional[str] = None):
        """
        初始化 MCP 客户端
        
        Args:
            server_script: MCP 服务器脚本路径
            python_exe: Python 可执行文件路径，默认使用当前 Python
        """
        self.server_script = Path(server_script).resolve()
        self.python_exe = python_exe or sys.executable
        self.session: Optional[ClientSession] = None
        self._loop: Optional[asyncio.AbstractEventLoop] = None
        self._thread: Optional[threading.Thread] = None
        self._server_process = None
        self._initialized = False
        self._context_manager = None
        self._read = None
        self._write = None

    def _ensure_loop_thread(self):
        """确保后台事件循环线程已启动"""
        if self._loop and self._thread and self._thread.is_alive():
            return
        self._loop = asyncio.new_event_loop()
        
        def _runner():
            asyncio.set_event_loop(self._loop)
            self._loop.run_forever()
        
        self._thread = threading.Thread(target=_runner, name="MCPClientLoop", daemon=True)
        self._thread.start()
        
    def _run_async(self, coro):
        """在后台事件循环中运行异步协程并同步等待结果"""
        self._ensure_loop_thread()
        fut = asyncio.run_coroutine_threadsafe(coro, self._loop)
        return fut.result()
    
    async def _init_session(self):
        """异步初始化会话"""
        try:
            params = StdioServerParameters(
                command=self.python_exe,
                args=[str(self.server_script)],
            )
            
            # 启动 stdio 客户端
            self._context_manager = stdio_client(params)
            self._read, self._write = await self._context_manager.__aenter__()
            
            # 创建会话
            self.session = ClientSession(self._read, self._write)
            await self.session.__aenter__()
            
            # 初始化连接
            await self.session.initialize()
            self._initialized = True
        except Exception as e:
            self._initialized = False
            raise RuntimeError(f"MCP 客户端初始化失败: {e}") from e
    
    def initialize(self):
        """同步初始化客户端"""
        if not self._initialized:
            self._run_async(self._init_session())
        return self
    
    def call_tool(self, tool_name: str, arguments: Dict[str, Any]) -> Dict[str, Any]:
        """
        调用 MCP 工具（同步接口）
        
        Args:
            tool_name: 工具名称
            arguments: 工具参数
            
        Returns:
            工具返回结果（已解包 structuredContent）
        """
        if not self._initialized:
            self.initialize()
        
        async def _call():
            result = await self.session.call_tool(tool_name, arguments=arguments)
            sc = getattr(result, "structuredContent", None)
            if isinstance(sc, dict) and "result" in sc:
                return sc["result"]
            return sc or {}
        
        try:
            return self._run_async(_call())
        except Exception:
            # 连接可能断开，尝试重连一次
            self._initialized = False
            self._run_async(self._close_async())
            self._run_async(self._init_session())
            return self._run_async(_call())

    async def _close_async(self):
        try:
            if self.session:
                await self.session.__aexit__(None, None, None)
            if self._context_manager:
                await self._context_manager.__aexit__(None, None, None)
        finally:
            self.session = None
            self._context_manager = None
    
    def list_tasks(self, status: Optional[str] = None) -> List[Dict[str, Any]]:
        """列出任务"""
        result = self.call_tool("list_tasks", {"status": status})
        return result if isinstance(result, list) else []
    
    def add_task(
        self,
        title: str,
        priority: str = "中",
        deadline: Optional[str] = None,
        category: str = "工作"
    ) -> Dict[str, Any]:
        """添加任务"""
        return self.call_tool("add_task", {
            "title": title,
            "priority": priority,
            "deadline": deadline,
            "category": category,
        })
    
    def complete_task(self, task_id: int) -> Dict[str, Any]:
        """完成任务"""
        return self.call_tool("complete_task", {"task_id": task_id})
    
    def delete_task(self, task_id: int) -> Dict[str, Any]:
        """删除任务"""
        return self.call_tool("delete_task", {"task_id": task_id})
    
    def update_task_status(self, task_id: int, status: str) -> Dict[str, Any]:
        """更新任务状态"""
        return self.call_tool("update_task_status", {
            "task_id": task_id,
            "status": status,
        })
    
    def search_tasks(self, keyword: str) -> List[Dict[str, Any]]:
        """搜索任务"""
        result = self.call_tool("search_tasks", {"keyword": keyword})
        return result if isinstance(result, list) else []
    
    def generate_todo_list(self) -> str:
        """生成待办清单"""
        result = self.call_tool("generate_todo_list", {})
        return result if isinstance(result, str) else str(result)
    
    def get_statistics(self) -> str:
        """获取统计信息"""
        result = self.call_tool("stats", {})
        return result if isinstance(result, str) else str(result)
    
    def close(self):
        """关闭客户端连接并停止事件循环线程"""
        try:
            self._run_async(self._close_async())
        except Exception:
            pass
        finally:
            self._initialized = False
            if self._loop:
                try:
                    self._loop.call_soon_threadsafe(self._loop.stop)
                except Exception:
                    pass
            if self._thread and self._thread.is_alive():
                try:
                    self._thread.join(timeout=1.0)
                except Exception:
                    pass
            self._loop = None
            self._thread = None
    
    def __del__(self):
        """析构时清理资源"""
        try:
            self.close()
        except:
            pass


# 全局单例客户端
_global_client: Optional[MCPClient] = None


def get_mcp_client() -> MCPClient:
    """获取全局 MCP 客户端单例"""
    global _global_client
    if _global_client is None:
        # 获取项目根目录
        root = Path(__file__).resolve().parents[1]
        server_path = root / "mcp_server" / "server.py"
        _global_client = MCPClient(str(server_path))
        _global_client.initialize()
        # 注册退出时清理
        atexit.register(_global_client.close)
    return _global_client
