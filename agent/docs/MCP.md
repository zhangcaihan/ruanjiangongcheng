# MCP 集成说明（任务管理助手）

本项目提供一个符合 Model Context Protocol (MCP) 的服务器，暴露任务管理工具（添加/删除/完成/列表/搜索/统计/待办清单）。任何支持 MCP 的客户端（如 Claude Desktop、MCP Inspector）都可以通过 stdio 或 HTTP 连接，并在对话中调用这些工具。

## 1. 目录与入口

- 服务器入口：`mcp_server/server.py`
- Windows 启动脚本：`start_mcp.bat`
- 使用的业务逻辑：沿用 `src/tools/todo_tool.py` 与配置 `config/settings.yaml`

## 2. 依赖安装

建议 Python 3.10+。安装依赖：

```bash
# WSL/PowerShell 均可，确保激活你的虚拟环境/Conda 环境
pip install "mcp[cli]" pyyaml pydantic
```

如果你的项目已有依赖文件，请将 `mcp[cli]` 加入其中再统一安装。

## 3. 本地快速启动（直接执行）

```bash
# 在项目根目录运行
./start_mcp.bat
```

这会以 stdio 方式启动服务器。通常由 MCP 客户端通过 `command + args` 启动并连接；直接启动时可用于自检或与 MCP Inspector 对接。

## 4. 用 MCP Inspector 进行开发/调试（可选）

安装 uv（可选）：https://docs.astral.sh/uv/

```bash
# 以文件作为服务器入口进行调试
uv run mcp dev mcp_server/server.py
```

## 5. 在 Claude Desktop 中安装（推荐）

Claude Desktop 支持从本地文件安装 MCP 服务器。

```bash
# 将本地服务器安装进 Claude Desktop
uv run mcp install mcp_server/server.py --name "Task Assistant (MCP)"
```

或手动配置 `claude_desktop_config.json`（路径通常在：`%APPDATA%/Claude/claude_desktop_config.json`）中的 `mcpServers`：

```json
{
  "mcpServers": {
    "task-assistant": {
      "command": "python",
      "args": ["e:/agent/mcp_server/server.py"],
      "cwd": "e:/agent"
    }
  }
}
```

注意：Windows 路径分隔符可用 `/` 或 `\\`，确保路径存在且 Python 在 PATH 中。

## 6. 可用工具（Tools）

- add_task(title, priority="中", deadline=None, category="工作") -> { message }
- complete_task(task_id) -> { message }
- delete_task(task_id) -> { message }
- update_task_status(task_id, status) -> { message }
- list_tasks(status=None) -> TaskModel[]
- search_tasks(keyword) -> TaskModel[]
- generate_todo_list() -> str
- stats() -> str

TaskModel 字段：id, title, status, priority, deadline?, category?, created_at?, completed_at?

## 7. 与现有 Web/CLI 共存

MCP 仅作为新增的“对外协议层”，底层仍沿用同一份任务存储（`data/todos.json`）。
- 你可以同时运行 Streamlit Web UI、CLI 与 MCP Server。
- 任何一端修改任务，其他界面刷新后即可看到。

## 8. 常见问题

- 提示找不到 `src` 模块：请确保以项目根目录作为工作目录（`cwd`），或使用本仓库的启动脚本；我们在 `server.py` 中已自动将项目根加入 `sys.path`。
- Claude Desktop 无法连接：检查 `command` 可执行、路径是否正确、以及是否有弹窗权限提示。
- 想用 HTTP 而不是 stdio：可将 `mcp.run(transport="streamable-http")` 用于 HTTP 模式（需要额外依赖和端口配置）。

---

如你需要把“自然语言解析”也做成 MCP Prompt 或 Tool，我们可以继续把 `src/agent_core.py` 的意图识别封装为 MCP Prompt，让客户端一键注入“任务管家”的对话指令集。