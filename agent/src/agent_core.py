"""
智能任务管理助手核心模块
专注于任务的增删改查、待办清单生成、任务统计
"""
import json
import os
import re
from datetime import datetime, timedelta
from typing import Dict, Any, List, Optional

import yaml
from pydantic import BaseModel

from .tools.todo_tool import TodoTool
from .chitchat import respond as chitchat_respond
from .mcp_client import MCPClient


class Memory(BaseModel):
    """对话记忆"""
    conversation: List[Dict[str, str]] = []
    actions: List[Dict[str, Any]] = []


class Agent:
    """智能任务管理助手 - 帮助用户整理任务和生成待办清单"""
    
    def __init__(self, config_path: str = "config/settings.yaml", use_mcp: bool = False, mcp_client: Optional[MCPClient] = None):
        # 加载配置
        if os.path.exists(config_path):
            with open(config_path, "r", encoding="utf-8") as f:
                self.cfg = yaml.safe_load(f)
        else:
            # 默认配置
            self.cfg = {
                "app": {"name": "智能任务助手", "data_dir": "./data"},
                "todo": {"store": "./data/todos.json"}
            }
        
        os.makedirs(self.cfg["app"].get("data_dir", "./data"), exist_ok=True)
        
        # 选择使用 MCP 客户端 或 直接调用 TodoTool
        self.use_mcp = use_mcp
        if use_mcp:
            self.mcp_client = mcp_client
            if not self.mcp_client:
                from .mcp_client import get_mcp_client
                self.mcp_client = get_mcp_client()
            self.todo = None  # 不使用直接调用
        else:
            self.mcp_client = None
            self.todo = TodoTool(self.cfg)  # 传统方式
        
        # 对话记忆
        self.memory = Memory()
    
    def handle(self, text: str) -> str:
        """处理用户输入"""
        self.memory.conversation.append({"role": "user", "content": text})
        intent = self._parse_intent(text)
        
        # 执行对应操作
        response = self._execute_intent(intent, text)
        
        self.memory.conversation.append({"role": "assistant", "content": response})
        return response
    
    def _execute_intent(self, intent: Dict[str, Any], text: str) -> str:
        """执行意图对应的操作"""
        
        # 添加任务
        if intent["name"] == "add_task":
            title = intent.get("title")
            if not title:
                return "❌ 请提供任务标题，例如：添加任务 完成项目报告"
            priority = intent.get("priority", "中")
            deadline = intent.get("deadline")
            category = intent.get("category", "工作")
            
            if self.use_mcp:
                result = self.mcp_client.add_task(title, priority, deadline, category)
            else:
                result = self.todo.add_task(title, deadline, priority, category)
            return result.get("message", str(result))
        
        # 完成任务
        if intent["name"] == "complete_task":
            task_id = intent.get("task_id")
            if task_id is None:
                return "❌ 请提供任务ID，例如：完成任务 1"
            
            if self.use_mcp:
                result = self.mcp_client.complete_task(int(task_id))
            else:
                result = self.todo.complete_task(int(task_id))
            return result.get("message", str(result))
        
        # 删除任务
        if intent["name"] == "delete_task":
            task_id = intent.get("task_id")
            if task_id is None:
                return "❌ 请提供任务ID，例如：删除任务 1"
            
            if self.use_mcp:
                result = self.mcp_client.delete_task(int(task_id))
            else:
                result = self.todo.delete_task(int(task_id))
            return result.get("message", str(result))
        
        # 更新任务状态
        if intent["name"] == "update_task_status":
            task_id = intent.get("task_id")
            status = intent.get("status", "进行中")
            if task_id is None:
                return "❌ 请提供任务ID"
            
            if self.use_mcp:
                result = self.mcp_client.update_task_status(int(task_id), status)
            else:
                result = self.todo.update_task_status(int(task_id), status)
            return result.get("message", str(result))
        
        # 查看任务列表
        if intent["name"] == "list_tasks":
            status = intent.get("status")
            
            if self.use_mcp:
                tasks = self.mcp_client.list_tasks(status)
            else:
                tasks = self.todo.list_tasks(status)
                
            if not tasks:
                status_text = f"「{status}」" if status else ""
                return f"暂无{status_text}任务"
            
            lines = [f"📝 任务列表（共 {len(tasks)} 个）：\n"]
            for task in tasks:
                priority_emoji = {"高": "🔴", "中": "🟡", "低": "🟢"}.get(task["priority"], "⚪")
                status_tag = f"[{task['status']}]"
                deadline_str = f" | 📅 {task['deadline']}" if task.get('deadline') else ""
                category_str = f" | 🏷️ {task['category']}" if task.get('category') else ""
                lines.append(f"{task['id']}. {priority_emoji} {status_tag} {task['title']}{deadline_str}{category_str}")
            return "\n".join(lines)
        
        # 生成待办清单
        if intent["name"] == "show_todo_list":
            if self.use_mcp:
                return self.mcp_client.generate_todo_list()
            else:
                return self.todo.generate_todo_list()
        
        # 任务统计
        if intent["name"] == "task_stats":
            if self.use_mcp:
                return self.mcp_client.get_statistics()
            else:
                return self.todo.get_statistics()
        
        # 搜索任务
        if intent["name"] == "search_tasks":
            keyword = intent.get("keyword", "")
            if not keyword:
                return "❌ 请提供搜索关键词，例如：搜索任务 报告"
            
            if self.use_mcp:
                tasks = self.mcp_client.search_tasks(keyword)
            else:
                tasks = self.todo.search_tasks(keyword)
                
            if not tasks:
                return f"未找到包含「{keyword}」的任务"
            lines = [f"🔍 搜索结果（共 {len(tasks)} 个）：\n"]
            for task in tasks:
                priority_emoji = {"高": "🔴", "中": "🟡", "低": "🟢"}.get(task["priority"], "⚪")
                lines.append(f"{task['id']}. {priority_emoji} [{task['status']}] {task['title']}")
            return "\n".join(lines)
        
        # 默认：尝试闲聊；若未命中则给出功能性帮助
        cc = chitchat_respond(text, self.memory.dict())
        if cc:
            return cc
        return (
            "🤖 我可以帮你管理任务，也可以和你简单聊天。\n\n"
            "📋 任务管理：添加/完成/删除/查看\n"
            "🧾 待办清单：说『待办清单』\n"
            "📊 任务统计：说『任务统计』\n\n"
            "比如：『添加任务 完成项目报告 优先级高 截止2025-10-25』"
        )
    
    def _parse_intent(self, text: str) -> Dict[str, Any]:
        """意图识别 - 只处理任务相关"""
        t = text.strip().lower()
        
        # 添加任务
        if any(k in t for k in ["添加任务", "新建任务", "创建任务", "加个任务", "新增任务"]):
            title = _extract_task_title(text)
            priority = _extract_priority(text)
            deadline = _extract_deadline(text)
            category = _extract_category(text)
            return {
                "name": "add_task",
                "title": title,
                "priority": priority,
                "deadline": deadline,
                "category": category
            }
        
        # 完成任务
        if any(k in t for k in ["完成任务", "任务完成", "做完了", "标记完成"]):
            task_id = _extract_task_id(text)
            return {"name": "complete_task", "task_id": task_id}
        
        # 删除任务
        if any(k in t for k in ["删除任务", "移除任务", "取消任务"]):
            task_id = _extract_task_id(text)
            return {"name": "delete_task", "task_id": task_id}
        
        # 开始任务（更新为进行中）
        if any(k in t for k in ["开始任务", "开始做", "进行中"]):
            task_id = _extract_task_id(text)
            return {"name": "update_task_status", "task_id": task_id, "status": "进行中"}
        
        # 查看任务
        if any(k in t for k in ["查看任务", "任务列表", "所有任务", "我的任务", "显示任务"]):
            status = None
            if "待办" in t:
                status = "待办"
            elif "已完成" in t or "完成的" in t:
                status = "已完成"
            elif "进行中" in t:
                status = "进行中"
            return {"name": "list_tasks", "status": status}
        
        # 待办清单
        if any(k in t for k in ["待办清单", "今日待办", "今天要做", "今天的任务", "清单"]):
            return {"name": "show_todo_list"}
        
        # 任务统计
        if any(k in t for k in ["任务统计", "任务情况", "任务进度", "完成情况"]):
            return {"name": "task_stats"}
        
        # 搜索任务
        if any(k in t for k in ["搜索任务", "查找任务", "找任务"]):
            keyword = _extract_search_keyword(text)
            return {"name": "search_tasks", "keyword": keyword}
        
        # 默认
        return {"name": "help"}


# ========== 辅助函数 ==========

def _extract_task_title(text: str) -> Optional[str]:
    """提取任务标题"""
    # 移除常见的指令词
    for keyword in ["添加任务", "新建任务", "创建任务", "加个任务", "新增任务"]:
        text = text.replace(keyword, "")
    # 移除其他属性词
    text = re.sub(r"(截止|deadline|到期)[:：]?\s*\d{4}-\d{2}-\d{2}", "", text)
    text = re.sub(r"(优先级|priority)[:：]?\s*(高|中|低)", "", text)
    text = re.sub(r"(类别|分类|category)[:：]?\s*\S+", "", text)
    return text.strip() or None


def _extract_task_id(text: str) -> Optional[int]:
    """提取任务ID"""
    m = re.search(r"(\d+)", text)
    return int(m.group(1)) if m else None


def _extract_priority(text: str) -> str:
    """提取优先级"""
    if any(k in text for k in ["高", "紧急", "重要", "高优先级"]):
        return "高"
    elif any(k in text for k in ["低", "不急", "低优先级"]):
        return "低"
    return "中"


def _extract_deadline(text: str) -> Optional[str]:
    """提取截止日期"""
    # YYYY-MM-DD 格式
    m = re.search(r"(\d{4}-\d{2}-\d{2})", text)
    if m:
        return m.group(1)
    
    # 相对日期
    if "今天" in text or "今日" in text:
        return datetime.now().strftime("%Y-%m-%d")
    if "明天" in text:
        return (datetime.now() + timedelta(days=1)).strftime("%Y-%m-%d")
    if "后天" in text:
        return (datetime.now() + timedelta(days=2)).strftime("%Y-%m-%d")
    
    # 本周、下周
    if "本周" in text:
        days_until_sunday = (6 - datetime.now().weekday()) % 7
        return (datetime.now() + timedelta(days=days_until_sunday)).strftime("%Y-%m-%d")
    if "下周" in text:
        days_until_next_monday = (7 - datetime.now().weekday()) % 7 + 7
        return (datetime.now() + timedelta(days=days_until_next_monday)).strftime("%Y-%m-%d")
    
    return None


def _extract_category(text: str) -> str:
    """提取任务类别"""
    categories = ["工作", "学习", "生活", "娱乐", "健康", "社交", "购物", "其他"]
    for cat in categories:
        if cat in text:
            return cat
    return "工作"


def _extract_search_keyword(text: str) -> str:
    """提取搜索关键词"""
    for keyword in ["搜索任务", "查找任务", "找任务"]:
        text = text.replace(keyword, "")
    return text.strip()
