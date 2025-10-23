"""
测试 MCP 集成 - 验证自然语言处理功能
"""
import sys
from pathlib import Path

# 添加项目根目录到路径
root = Path(__file__).resolve().parent
sys.path.insert(0, str(root))

from src.agent_core import Agent
from src.mcp_client import get_mcp_client


def test_nlp_with_mcp():
    """测试通过 MCP 的自然语言处理"""
    print("=" * 60)
    print("🧪 测试 MCP 集成 - 自然语言处理")
    print("=" * 60)
    
    # 初始化 MCP 客户端
    print("\n1️⃣ 初始化 MCP 客户端...")
    try:
        mcp_client = get_mcp_client()
        print("✅ MCP 客户端初始化成功")
    except Exception as e:
        print(f"❌ MCP 初始化失败: {e}")
        return
    
    # 创建使用 MCP 的 Agent
    print("\n2️⃣ 创建 Agent（MCP 模式）...")
    agent = Agent(use_mcp=True, mcp_client=mcp_client)
    print("✅ Agent 创建成功")
    
    # 测试自然语言指令
    test_cases = [
        "添加任务 完成MCP集成测试 优先级高 截止2025-10-25",
        "查看任务",
        "任务统计",
        "待办清单",
        "搜索任务 MCP",
    ]
    
    print("\n3️⃣ 测试自然语言指令...")
    for i, prompt in enumerate(test_cases, 1):
        print(f"\n{'─' * 60}")
        print(f"测试 {i}: {prompt}")
        print(f"{'─' * 60}")
        
        try:
            response = agent.handle(prompt)
            print(response)
            print("✅ 成功")
        except Exception as e:
            print(f"❌ 失败: {e}")
            import traceback
            traceback.print_exc()
    
    # 清理
    print("\n4️⃣ 清理...")
    try:
        mcp_client.close()
        print("✅ MCP 客户端已关闭")
    except Exception as e:
        print(f"⚠️ 清理警告: {e}")
    
    print("\n" + "=" * 60)
    print("🎉 测试完成！")
    print("=" * 60)


if __name__ == "__main__":
    test_nlp_with_mcp()
