"""演示智能任务管理助手的所有功能"""
from src.agent_core import Agent

def demo():
    print("=" * 60)
    print("智能任务管理助手 - 功能演示")
    print("=" * 60)
    
    agent = Agent()
    
    scenarios = [
        ("添加任务 完成项目报告 优先级高 截止2025-10-25 类别工作", "添加任务"),
        ("添加任务 学习Python 优先级中", "添加普通任务"),
        ("添加任务 买菜 优先级低 类别生活", "添加生活任务"),
        ("查看任务", "查看所有任务"),
        ("待办清单", "生成待办清单"),
        ("任务统计", "查看统计信息"),
        ("完成任务 1", "完成第一个任务"),
        ("查看任务", "再次查看任务列表"),
        ("搜索任务 项目", "搜索功能"),
        ("任务统计", "最终统计"),
    ]
    
    for i, (command, description) in enumerate(scenarios, 1):
        print(f"\n[{i}/{len(scenarios)}] {description}")
        print(f"输入: {command}")
        response = agent.handle(command)
        print(f"回复:\n{response}")
        print("-" * 60)
    
    print("\n" + "=" * 60)
    print("演示完成！")
    print("=" * 60)

if __name__ == "__main__":
    demo()
