"""
Streamlit Web UI - 智能任务管理助手
专注于任务管理和待办清单
"""
import streamlit as st
from datetime import datetime
from src.agent_core import Agent

st.set_page_config(
    page_title="智能任务助手",
    page_icon="✅",
    layout="wide",
    initial_sidebar_state="expanded"
)

# 初始化
if "messages" not in st.session_state:
    st.session_state.messages = []

if "agent" not in st.session_state:
    # 启用 MCP 模式：创建并持久化 MCP 客户端（跨 rerun 复用连接）
    from pathlib import Path
    from src.mcp_client import MCPClient

    root = Path(__file__).resolve().parent
    server_path = root / "mcp_server" / "server.py"

    mcp_client = MCPClient(str(server_path))
    try:
        mcp_client.initialize()
        st.session_state.mcp_client = mcp_client
        st.session_state.agent = Agent(use_mcp=True, mcp_client=mcp_client)
        st.session_state.use_mcp = True
    except Exception as e:
        # 兜底：如果 MCP 初始化失败，回退传统模式，但提示原因
        st.warning(f"MCP 初始化失败，已回退到传统模式：{e}")
        st.session_state.agent = Agent(use_mcp=False)
        st.session_state.use_mcp = False

# 页面标题
st.title("✅ 智能任务管理助手")
st.caption("能说会做的AI助手 - 帮你整理任务和生成待办清单")

# 主界面布局
col1, col2 = st.columns([2, 1])

with col1:
    st.header("💬 对话界面")
    
    # 聊天历史
    chat_container = st.container()
    with chat_container:
        for msg in st.session_state.messages:
            with st.chat_message(msg["role"]):
                st.write(msg["content"])
    
    # 输入框
    if prompt := st.chat_input("你想让我做什么？"):
        # 添加用户消息
        st.session_state.messages.append({"role": "user", "content": prompt})
        with st.chat_message("user"):
            st.write(prompt)
        
        # 获取AI回复
        with st.spinner("思考中..."):
            response = st.session_state.agent.handle(prompt)
        
        st.session_state.messages.append({"role": "assistant", "content": response})
        with st.chat_message("assistant"):
            st.write(response)
        st.rerun()

with col2:
    st.header("📋 任务看板")
    
    # 任务统计（MCP）
    if st.session_state.use_mcp:
        stats = st.session_state.mcp_client.get_statistics()
    else:
        stats = st.session_state.agent.todo.get_statistics()
    st.info(stats)
    
    # 待办任务列表（MCP）
    st.subheader("⏳ 待办任务")
    if st.session_state.use_mcp:
        pending_tasks = st.session_state.mcp_client.list_tasks("待办")
    else:
        pending_tasks = st.session_state.agent.todo.list_tasks("待办")
    
    if pending_tasks:
        for task in pending_tasks:
            priority_emoji = {"高": "🔴", "中": "🟡", "低": "🟢"}.get(task["priority"], "⚪")
            
            # 使用三列：任务信息、完成按钮、删除按钮
            col_title, col_done, col_del = st.columns([6, 1, 1])
            
            with col_title:
                st.write(f"{priority_emoji} **{task['title']}**")
                info_parts = []
                if task.get("deadline"):
                    info_parts.append(f"📅 {task['deadline']}")
                if task.get("category"):
                    info_parts.append(f"🏷️ {task['category']}")
                info_parts.append(f"优先级: {task['priority']}")
                st.caption(" | ".join(info_parts))
            
            with col_done:
                if st.button("✅", key=f"done_{task['id']}", help="完成任务"):
                    if st.session_state.use_mcp:
                        st.session_state.mcp_client.complete_task(task['id'])
                    else:
                        st.session_state.agent.todo.complete_task(task['id'])
                    st.success(f"✅ 已完成：{task['title']}")
                    st.rerun()
            
            with col_del:
                if st.button("🗑️", key=f"del_{task['id']}", help="删除任务"):
                    if st.session_state.use_mcp:
                        st.session_state.mcp_client.delete_task(task['id'])
                    else:
                        st.session_state.agent.todo.delete_task(task['id'])
                    st.warning(f"🗑️ 已删除：{task['title']}")
                    st.rerun()
            
            st.divider()
    else:
        st.success("🎉 暂无待办任务！")
    
    # 进行中的任务（MCP）
    st.subheader("🔄 进行中")
    if st.session_state.use_mcp:
        in_progress_tasks = st.session_state.mcp_client.list_tasks("进行中")
    else:
        in_progress_tasks = st.session_state.agent.todo.list_tasks("进行中")
    
    if in_progress_tasks:
        for task in in_progress_tasks:
            priority_emoji = {"高": "🔴", "中": "🟡", "低": "🟢"}.get(task["priority"], "⚪")
            
            col_title, col_done, col_del = st.columns([6, 1, 1])
            
            with col_title:
                st.write(f"{priority_emoji} **{task['title']}** 🔄")
                info_parts = []
                if task.get("deadline"):
                    info_parts.append(f"📅 {task['deadline']}")
                if task.get("category"):
                    info_parts.append(f"🏷️ {task['category']}")
                st.caption(" | ".join(info_parts))
            
            with col_done:
                if st.button("✅", key=f"done_prog_{task['id']}", help="完成任务"):
                    if st.session_state.use_mcp:
                        st.session_state.mcp_client.complete_task(task['id'])
                    else:
                        st.session_state.agent.todo.complete_task(task['id'])
                    st.success(f"✅ 已完成：{task['title']}")
                    st.rerun()
            
            with col_del:
                if st.button("🗑️", key=f"del_prog_{task['id']}", help="删除任务"):
                    if st.session_state.use_mcp:
                        st.session_state.mcp_client.delete_task(task['id'])
                    else:
                        st.session_state.agent.todo.delete_task(task['id'])
                    st.warning(f"🗑️ 已删除：{task['title']}")
                    st.rerun()
            
            st.divider()
    else:
        st.caption("暂无进行中的任务")
    
    # 已完成的任务（MCP）
    with st.expander("✅ 已完成任务"):
        if st.session_state.use_mcp:
            completed_tasks = st.session_state.mcp_client.list_tasks("已完成")
        else:
            completed_tasks = st.session_state.agent.todo.list_tasks("已完成")
        
        if completed_tasks:
            for task in completed_tasks[-5:]:  # 只显示最近5个已完成任务
                priority_emoji = {"高": "🔴", "中": "🟡", "低": "🟢"}.get(task["priority"], "⚪")
                
                col_title, col_del = st.columns([7, 1])
                
                with col_title:
                    st.write(f"~~{priority_emoji} {task['title']}~~ ✅")
                    info_parts = []
                    if task.get("completed_at"):
                        info_parts.append(f"完成于: {task['completed_at']}")
                    if task.get("category"):
                        info_parts.append(f"🏷️ {task['category']}")
                    st.caption(" | ".join(info_parts))
                
                with col_del:
                    if st.button("🗑️", key=f"del_comp_{task['id']}", help="删除任务"):
                        if st.session_state.use_mcp:
                            st.session_state.mcp_client.delete_task(task['id'])
                        else:
                            st.session_state.agent.todo.delete_task(task['id'])
                        st.warning(f"🗑️ 已删除：{task['title']}")
                        st.rerun()
                
                st.divider()
        else:
            st.caption("暂无已完成任务")
    
    # 快捷操作
    st.subheader("🚀 快捷操作")
    
    with st.expander("➕ 添加任务"):
        with st.form("add_task_form", clear_on_submit=True):
            task_title = st.text_input("任务标题", placeholder="例如：完成项目报告")
            col_form1, col_form2 = st.columns(2)
            with col_form1:
                task_priority = st.selectbox("优先级", ["高", "中", "低"], index=1)
                task_category = st.selectbox("类别", ["工作", "学习", "生活", "娱乐", "健康", "社交", "购物", "其他"])
            with col_form2:
                task_deadline = st.date_input("截止日期（可选）", value=None)
            
            submitted = st.form_submit_button("添加任务", use_container_width=True)
            if submitted and task_title:
                deadline_str = task_deadline.strftime("%Y-%m-%d") if task_deadline else None
                if st.session_state.use_mcp:
                    result = st.session_state.mcp_client.add_task(
                        task_title, task_priority, deadline_str, task_category
                    )
                else:
                    result = st.session_state.agent.todo.add_task(
                        task_title, deadline_str, task_priority, task_category
                    )
                st.success(result.get("message", "任务已添加"))
                st.rerun()

# 侧边栏
with st.sidebar:
    st.header("⚙️ 功能面板")
    
    # 模式开关：使用 MCP 或 本地直连
    desired_use_mcp = st.checkbox("使用 MCP 模式", value=st.session_state.get("use_mcp", True))
    if desired_use_mcp != st.session_state.get("use_mcp", False):
        # 模式发生变化：重新初始化 Agent / MCP 客户端
        from pathlib import Path
        if desired_use_mcp:
            try:
                from src.mcp_client import MCPClient
                root = Path(__file__).resolve().parent
                server_path = root / "mcp_server" / "server.py"
                mcp_client = MCPClient(str(server_path))
                mcp_client.initialize()
                st.session_state.mcp_client = mcp_client
                st.session_state.agent = Agent(use_mcp=True, mcp_client=mcp_client)
                st.session_state.use_mcp = True
                st.success("已切换到 MCP 模式")
            except Exception as e:
                st.session_state.agent = Agent(use_mcp=False)
                st.session_state.use_mcp = False
                st.error(f"切换 MCP 失败，已回退本地模式：{e}")
        else:
            # 关闭 MCP 客户端并切换到本地模式
            try:
                if st.session_state.get("mcp_client"):
                    st.session_state.mcp_client.close()
            except Exception:
                pass
            st.session_state.mcp_client = None
            st.session_state.agent = Agent(use_mcp=False)
            st.session_state.use_mcp = False
            st.info("已切换到本地模式")
        st.rerun()
    
    # 快速指令
    st.subheader("📌 快速操作")
    
    if st.button("📝 查看所有任务", use_container_width=True):
        st.session_state.messages.append({"role": "user", "content": "查看任务"})
        response = st.session_state.agent.handle("查看任务")
        st.session_state.messages.append({"role": "assistant", "content": response})
        st.rerun()
    
    if st.button("📋 生成待办清单", use_container_width=True):
        st.session_state.messages.append({"role": "user", "content": "待办清单"})
        response = st.session_state.agent.handle("待办清单")
        st.session_state.messages.append({"role": "assistant", "content": response})
        st.rerun()
    
    if st.button("📊 任务统计", use_container_width=True):
        st.session_state.messages.append({"role": "user", "content": "任务统计"})
        response = st.session_state.agent.handle("任务统计")
        st.session_state.messages.append({"role": "assistant", "content": response})
        st.rerun()
    
    st.divider()
    
    # 搜索任务
    st.subheader("🔍 搜索任务")
    search_keyword = st.text_input("输入关键词", placeholder="例如：报告")
    if st.button("搜索", use_container_width=True) and search_keyword:
        query_text = f"搜索任务 {search_keyword}"
        st.session_state.messages.append({"role": "user", "content": query_text})
        response = st.session_state.agent.handle(query_text)
        st.session_state.messages.append({"role": "assistant", "content": response})
        st.rerun()
    
    st.divider()
    
    # 清空对话
    if st.button("🗑️ 清空对话", use_container_width=True):
        st.session_state.messages = []
        st.session_state.agent.memory.conversation = []
        st.rerun()
    
    # 关于
    with st.expander("ℹ️ 关于"):
        st.write("""
        **智能任务管理助手 v2.0**
        
        一个"能说会做"的AI智能体
        
        **主要功能：**
        - ✅ 任务增删改查
        - 📋 待办清单生成
        - 📊 任务统计分析
        - 🔍 智能搜索
        - 💬 自然语言交互
        - 🚀 MCP 协议集成
        
        **技术栈：**
        - Streamlit + Python
        - Model Context Protocol (MCP)
        - 自然语言处理
        """)

# 页脚
st.divider()
st.caption("💡 提示：可以说「添加任务 完成项目报告 优先级高 截止2025-10-25」")
