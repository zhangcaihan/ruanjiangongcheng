@echo off
setlocal
cd /d %~dp0

REM Start MCP server over stdio. Use the active Python on PATH.
REM If you use Conda/venv, activate it before running this script.

python -u mcp_server\server.py

endlocal
