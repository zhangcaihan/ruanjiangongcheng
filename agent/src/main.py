"""CLI 交互主程序 - 智能任务管理助手"""
from rich.console import Console
from rich.prompt import Prompt
from rich.panel import Panel
from rich.table import Table
from src.agent_core import Agent

console = Console()

def show_welcome():
    """显示欢迎信息"""
    console.print(Panel.fit(
        "智能任务管理助手 (Smart Todo Assistant)",
        title="欢迎",
        subtitle="输入 'exit' 退出",
        border_style="cyan"
    ))
    
    console.print("\n✨ 我可以帮你：")
    console.print("  📋 添加任务 - 创建新的待办事项")
    console.print("  ✅ 完成任务 - 标记任务为已完成")
    console.print("  🗑️  删除任务 - 移除不需要的任务")
    console.print("  📝 查看任务 - 浏览所有任务列表")
    console.print("  📊 任务统计 - 查看完成情况")
    console.print("  🔍 搜索任务 - 查找特定任务\n")

def main():
    show_welcome()
    agent = Agent()
    
    while True:
        try:
            text = Prompt.ask("[bold green]你想让我做什么[/bold green]")
        except (EOFError, KeyboardInterrupt):
            console.print("\n[yellow]再见！[/yellow]")
            break
        
        if text.strip().lower() in {"exit", "quit", "q", "退出", "再见"}:
            console.print("[yellow]再见！[/yellow]")
            break
        
        if not text.strip():
            continue
        
        reply = agent.handle(text)
        console.print(f"\n[cyan]{reply}[/cyan]\n")

if __name__ == "__main__":
    main()
