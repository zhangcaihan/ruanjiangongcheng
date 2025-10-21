"""
任务管理工具 - 支持任务增删改查、待办清单生成
"""
from typing import List, Dict, Optional
from datetime import datetime
import json
import os
from pathlib import Path


class TodoTool:
    """任务管理工具类"""
    
    def __init__(self, cfg: Dict = None):
        storage_path = "data/todos.json"
        if cfg:
            storage_path = cfg.get("todo", {}).get("store", storage_path)
        self.storage = Path(storage_path)
        self.storage.parent.mkdir(parents=True, exist_ok=True)
        self.tasks = self._load()
    
    def add_task(self, title: str, deadline: Optional[str] = None, 
                 priority: str = "中", category: str = "其他") -> Dict:
        """添加新任务"""
        task = {
            "id": self._get_next_id(),
            "title": title,
            "deadline": deadline,
            "priority": priority,  # 高/中/低
            "category": category,
            "status": "待办",  # 待办/进行中/已完成
            "created_at": datetime.now().strftime("%Y-%m-%d %H:%M:%S"),
            "completed_at": None
        }
        self.tasks.append(task)
        self._save()
        return {"message": f"✅ 已添加任务：{title} (优先级: {priority})", "task": task}
    
    def complete_task(self, task_id: int) -> Dict:
        """完成任务"""
        for task in self.tasks:
            if task["id"] == task_id:
                task["status"] = "已完成"
                task["completed_at"] = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
                self._save()
                return {"message": f"✅ 已完成任务：{task['title']}", "task": task}
        return {"message": "❌ 未找到该任务", "task": None}
    
    def delete_task(self, task_id: int) -> Dict:
        """删除任务"""
        for i, task in enumerate(self.tasks):
            if task["id"] == task_id:
                deleted_title = task["title"]
                self.tasks.pop(i)
                self._save()
                return {"message": f"🗑️ 已删除任务：{deleted_title}"}
        return {"message": "❌ 未找到该任务"}
    
    def update_task_status(self, task_id: int, status: str) -> Dict:
        """更新任务状态"""
        for task in self.tasks:
            if task["id"] == task_id:
                task["status"] = status
                if status == "已完成" and not task["completed_at"]:
                    task["completed_at"] = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
                self._save()
                return {"message": f"✅ 已更新任务状态为：{status}", "task": task}
        return {"message": "❌ 未找到该任务", "task": None}
    
    def list_tasks(self, status: Optional[str] = None) -> List[Dict]:
        """列出任务"""
        if status:
            return [t for t in self.tasks if t["status"] == status]
        return self.tasks
    
    def generate_todo_list(self) -> str:
        """生成今日待办清单"""
        pending = self.list_tasks("待办")
        in_progress = self.list_tasks("进行中")
        
        all_active = pending + in_progress
        
        if not all_active:
            return "🎉 暂无待办任务！"
        
        # 按优先级和截止日期排序
        priority_order = {"高": 1, "中": 2, "低": 3}
        all_active.sort(key=lambda x: (
            priority_order.get(x["priority"], 4),
            x["deadline"] or "9999-12-31"
        ))
        
        lines = ["📋 今日待办清单：\n"]
        for i, task in enumerate(all_active, 1):
            priority_emoji = {"高": "🔴", "中": "🟡", "低": "🟢"}.get(task["priority"], "⚪")
            status_prefix = "[进行中] " if task["status"] == "进行中" else ""
            deadline_str = f" | 截止: {task['deadline']}" if task["deadline"] else ""
            lines.append(f"{i}. [{task['id']}] {priority_emoji} {status_prefix}{task['title']}{deadline_str}")
        
        return "\n".join(lines)
    
    def search_tasks(self, keyword: str) -> List[Dict]:
        """搜索任务"""
        keyword_lower = keyword.lower()
        return [
            t for t in self.tasks 
            if keyword_lower in t["title"].lower() or keyword_lower in t.get("category", "").lower()
        ]
    
    def get_statistics(self) -> str:
        """获取任务统计"""
        total = len(self.tasks)
        if total == 0:
            return "暂无任务数据"
            
        pending = len([t for t in self.tasks if t["status"] == "待办"])
        in_progress = len([t for t in self.tasks if t["status"] == "进行中"])
        completed = len([t for t in self.tasks if t["status"] == "已完成"])
        
        completion_rate = completed / total * 100 if total > 0 else 0
        
        return f"""📊 任务统计：
总任务数: {total}
待办: {pending}
进行中: {in_progress}
已完成: {completed}
完成率: {completion_rate:.1f}%"""
    
    def _get_next_id(self) -> int:
        """获取下一个任务ID"""
        if not self.tasks:
            return 1
        return max(t["id"] for t in self.tasks) + 1
    
    def _load(self) -> List[Dict]:
        """加载任务数据"""
        try:
            if self.storage.exists():
                with open(self.storage, 'r', encoding='utf-8') as f:
                    return json.load(f)
        except Exception as e:
            print(f"加载任务数据失败: {e}")
        return []
    
    def _save(self):
        """保存任务数据"""
        try:
            with open(self.storage, 'w', encoding='utf-8') as f:
                json.dump(self.tasks, f, ensure_ascii=False, indent=2)
        except Exception as e:
            print(f"保存任务数据失败: {e}")
