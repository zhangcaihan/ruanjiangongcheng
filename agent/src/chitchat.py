"""
简易闲聊模块：为助手提供基础的自由对话能力（无需外部大模型）。
"""
from __future__ import annotations

from datetime import datetime
from typing import Dict, Any, List


def _contains_any(text: str, keywords: List[str]) -> bool:
    return any(k in text for k in keywords)


def respond(text: str, memory: Dict[str, Any] | None = None) -> str | None:
    """
    基于规则的闲聊应答器。
    返回 None 表示未命中闲聊，将交给帮助/其他逻辑处理。
    """
    if not text:
        return None

    t = text.strip().lower()

    # 问候
    if _contains_any(text, ["你好", "您好", "嗨", "在吗", "哈喽", "hello", "hi", "hey", "早上好", "中午好", "下午好", "晚上好"]):
        return "你好呀，我在这。要不要我帮你整理一下任务，或者直接说一声你现在想做什么？"

    # 致谢
    if _contains_any(text, ["谢谢", "多谢", "感激", "感谢"]):
        return "不客气～有需要随时叫我。要继续添加任务还是看看待办清单？"

    # 自我介绍 / 能力
    if _contains_any(text, ["你是谁", "你是", "介绍一下", "能做什么", "会什么", "功能", "帮助", "怎么用"]):
        return (
            "我是你的任务管理助手，能帮你：\n"
            "• 添加/完成/删除/搜索任务\n"
            "• 生成今日待办清单\n"
            "• 查看任务统计\n\n"
            "直接和我说话就行，比如：‘添加任务 本周整理报销 优先级高 截止周五’"
        )

    # 时间 / 日期
    if _contains_any(text, ["几点", "时间", "现在几时", "what time"]):
        return f"现在时间是 {datetime.now().strftime('%H:%M')}"
    if _contains_any(text, ["日期", "今天几号", "今天什么日子", "what date"]):
        return f"今天是 {datetime.now().strftime('%Y-%m-%d')}"

    # 心情 / 情绪简单回复
    if _contains_any(text, ["无聊", "累", "困", "焦虑", "压力", "难过", "不开心", "郁闷"]):
        return "先缓一缓吧～要不要把今天的任务拆小一点？我可以帮你列个轻量清单。"
    if _contains_any(text, ["开心", "高兴", "兴奋", "不错", "顺利"]):
        return "太好了！要不要顺势推进一个小任务？我可以帮你挑一个优先级合适的。"

    # 玩笑 / 简单调剂
    if _contains_any(text, ["讲个笑话", "来个笑话", "无趣", "打个气"]):
        return "程序员的浪漫：给你提交了一个 PR，标题是『把你放进我的人生』。😉"

    # 未命中
    return None
