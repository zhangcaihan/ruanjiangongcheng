"""测试 app.py 导入"""
import sys
print("Python version:", sys.version)
print("Testing app.py import...")

try:
    # 测试导入关键模块
    from src.agent_core import Agent
    print("✓ Agent imported")
    
    from src.mcp_client import MCPClient
    print("✓ MCPClient imported")
    
    from src.chitchat import respond
    print("✓ chitchat imported")
    
    # 测试 Agent 初始化（传统模式）
    agent = Agent(use_mcp=False)
    print("✓ Agent (traditional mode) initialized")
    
    # 测试简单对话
    response = agent.handle("你好")
    print(f"✓ Agent response: {response[:50]}...")
    
    print("\n✅ All imports and basic functionality working!")
    
except Exception as e:
    print(f"\n❌ Error: {e}")
    import traceback
    traceback.print_exc()
