# 智能任务管理助手

一个“能说会做”的本地任务管理智能体，支持自然语言对话与任务增删改查、待办清单、统计分析。提供 Web UI、CLI，以及标准 MCP（Model Context Protocol）服务器，便于外部客户端调用。

## 🎯 特性总览

- 能说
  - 中文自然语言指令与轻量闲聊
  - 规则式意图识别（离线、无需网络）
- 会做
  - 任务增删改查、搜索、状态更新（待办/进行中/已完成）
  - 今日待办清单生成、统计分析
  - 数据持久化到本地 JSON
- MCP 标准
  - 内置 MCP Server（stdio 传输）
  - Web UI 默认通过 MCP 调用工具（失败自动回退本地）
  - 附带冒烟测试脚本验证端到端调用
- UI
  - Streamlit Web UI
  - Rich CLI 命令行

数据存储路径：./data/todos.json（UI/CLI/MCP 共享同一数据）

## 🧭 架构

- Web UI（app.py）：自然语言 → Agent → MCP Client → MCP Server → TodoTool
- MCP Server（mcp_server/server.py）：暴露任务工具（add/list/complete/delete/...）
- MCP Client（src/mcp_client.py）：提供同步调用封装（后台事件循环 + 自动重连）
- Agent（src/agent_core.py）：自然语言解析 + 调用执行

说明：Web UI 默认通过 MCP 调用；若 MCP 初始化失败，会自动回退到本地工具并提示。侧边栏提供“使用 MCP 模式”开关，可随时切换模式。

## 📁 目录结构

```
agent/
├── app.py                 # Streamlit Web UI（默认通过 MCP）
├── scripts/
│   └── mcp_smoke_test.py  # MCP 冒烟测试脚本
├── mcp_server/
│   └── server.py          # MCP Server（stdio）
├── src/
│   ├── agent_core.py      # 自然语言解析 + 执行
│   ├── mcp_client.py      # MCP 客户端封装
│   ├── chitchat.py        # 轻量闲聊
│   └── tools/
│       └── todo_tool.py   # 任务工具（本地实现）
├── data/
│   └── todos.json         # 任务数据（自动创建）
├── config/
│   └── settings.yaml
├── docs/
│   └── MCP.md             # MCP 使用说明（可选）
├── requirements.txt
├── start_web.bat          # Windows 启动 Web UI（可选）
├── start_cli.bat          # Windows 启动 CLI（可选）
└── start_cli.sh           # Linux/WSL 启动 CLI（可选）
```

## 🚀 快速开始（Windows 推荐）

前置：已安装 Conda 环境 dog，项目位于 E:\agent

- 安装依赖
```powershell
conda activate dog
cd E:\agent
pip install -r requirements.txt
```

- 启动 Web UI（默认通过 MCP）
```powershell
conda activate dog
cd E:\agent
python -m streamlit run app.py
```
浏览器打开 http://localhost:8501（端口占用可改为 --server.port 8503）

- 启动 CLI
```powershell
conda activate dog
cd E:\agent
python -m src.main
```

## 🧪 MCP 验证（脚本/Inspector）

- 运行冒烟测试（端到端：add_task → list_tasks）
```powershell
conda activate dog
C:\Users\19434\.conda\envs\dog\python.exe -u E:\agent\scripts\mcp_smoke_test.py
```

- 可选：MCP Inspector 可视化调试
```powershell
conda activate dog
C:\Users\19434\.conda\envs\dog\Scripts\mcp.exe dev E:\agent\mcp_server\server.py
```
连接配置：
- Transport: STDIO
- Command: C:\Users\19434\.conda\envs\dog\python.exe
- Arguments: E:\agent\mcp_server\server.py
- Authentication: None

## �️ 自然语言指令示例

- 添加任务
  - 添加任务 完成项目报告 优先级高 截止2025-10-25 类别工作
  - 新建任务 复习考试 明天截止 优先级中
- 查看与搜索
  - 查看任务 / 查看待办任务 / 查看已完成任务
  - 搜索任务 报告
  - 待办清单 / 今日待办
- 状态与删除
  - 完成任务 1
  - 开始任务 2
  - 删除任务 3
- 统计
  - 任务统计 / 任务情况

支持相对日期：今天、明天、后天；支持优先级：高/中/低。

## ⚙️ 配置

文件：config/settings.yaml
```yaml
app:
  name: "智能任务管理助手"
  data_dir: "./data"

todo:
  store: "./data/todos.json"
```

## 🐛 故障排除

- 端口占用（Streamlit）
```powershell
python -m streamlit run app.py --server.port 8503
```

- 依赖安装失败
```powershell
pip install -r requirements.txt --upgrade
```

- MCP 连接报错（Inspector）
检查 Command/Arguments 是否指向正确的 Python 与 server.py，认证应为 None。

## 📄 License

MIT License（教学示例）

——
环境：Windows 10/11 + Conda (dog) + Python 3.12  
更新时间：2025-10-23
