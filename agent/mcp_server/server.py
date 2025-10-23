"""
MCP Server for the Task Management Assistant
Exposes task tools over the Model Context Protocol (FastMCP/stdio).
"""
from __future__ import annotations

import os
import sys
from pathlib import Path
from typing import List, Dict, Any

from pydantic import BaseModel, Field
from mcp.server.fastmcp import FastMCP

# Ensure project root is on sys.path to import src.*
ROOT_DIR = Path(__file__).resolve().parents[1]
if str(ROOT_DIR) not in sys.path:
    sys.path.insert(0, str(ROOT_DIR))

# Now we can import our existing Todo tool
from src.tools.todo_tool import TodoTool  # type: ignore
import yaml


class TaskModel(BaseModel):
    id: int
    title: str
    status: str
    priority: str
    deadline: str | None = None
    category: str | None = None
    created_at: str | None = None
    completed_at: str | None = None


# 为避免 Pydantic 在 FastMCP 参数模型生成阶段的前向引用问题，
# 工具函数参数直接使用原生类型（并采用 PEP 604 的 | None 语法，而非 Optional）。


# Load config (reuse same config as app/agent)
CFG_PATH = ROOT_DIR / "config" / "settings.yaml"
if CFG_PATH.exists():
    with open(CFG_PATH, "r", encoding="utf-8") as f:
        CFG = yaml.safe_load(f)
else:
    CFG = {"app": {"name": "智能任务助手", "data_dir": str(ROOT_DIR / "data")}, "todo": {"store": str(ROOT_DIR / "data" / "todos.json")}}

# Ensure data dir exists
os.makedirs(CFG["app"].get("data_dir", str(ROOT_DIR / "data")), exist_ok=True)

# Initialize domain tool
TODO = TodoTool(CFG)

# Create MCP server
mcp = FastMCP(
    name="Task Assistant (MCP)",
    instructions=(
        "一个任务管理 MCP 服务器：提供添加/删除/完成/列表/搜索/统计/待办清单等工具。"
        "与任意支持 MCP 的客户端配合，即可进行自然语言对话和调用这些工具。"
    ),
)


@mcp.tool(title="添加任务")
def add_task(
    title: str = Field(description="任务标题"),
    priority: str = Field(default="中", description="优先级：高/中/低"),
    deadline: str | None = Field(default=None, description="截止日期 YYYY-MM-DD，可选"),
    category: str = Field(default="工作", description="任务类别，可选"),
) -> Dict[str, Any]:
    """添加新任务，返回操作结果。"""
    result = TODO.add_task(title=title, deadline=deadline, priority=priority, category=category)
    return {"message": result.get("message", ""), "ok": True}


@mcp.tool(title="完成任务")
def complete_task(task_id: int = Field(description="任务ID")) -> Dict[str, Any]:
    """按ID标记任务为已完成。"""
    result = TODO.complete_task(int(task_id))
    return {"message": result.get("message", ""), "ok": True}


@mcp.tool(title="删除任务")
def delete_task(task_id: int = Field(description="任务ID")) -> Dict[str, Any]:
    """按ID删除任务。"""
    result = TODO.delete_task(int(task_id))
    return {"message": result.get("message", ""), "ok": True}


@mcp.tool(title="更新任务状态")
def update_task_status(
    task_id: int = Field(description="任务ID"),
    status: str = Field(default="进行中", description="状态：待办/进行中/已完成"),
) -> Dict[str, Any]:
    """按ID更新任务状态（待办/进行中/已完成）。"""
    result = TODO.update_task_status(int(task_id), status)
    return {"message": result.get("message", ""), "ok": True}


@mcp.tool(title="任务列表")
def list_tasks(status: str | None = None) -> List[TaskModel]:
    """列出任务，可选过滤状态（待办/进行中/已完成）。"""
    tasks = TODO.list_tasks(status)
    return [TaskModel(**t) for t in tasks]


@mcp.tool(title="搜索任务")
def search_tasks(keyword: str = Field(description="搜索关键词")) -> List[TaskModel]:
    """按关键词搜索任务，匹配标题/类别等。"""
    tasks = TODO.search_tasks(keyword)
    return [TaskModel(**t) for t in tasks]


@mcp.tool(title="待办清单")
def generate_todo_list() -> str:
    """生成今日待办清单（文本）。"""
    return TODO.generate_todo_list()


@mcp.tool(title="任务统计")
def stats() -> str:
    """输出任务统计（文本）。"""
    return TODO.get_statistics()


if __name__ == "__main__":
    # Direct execution: stdio transport
    mcp.run()
