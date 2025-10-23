@echo off
chcp 65001 >nul
echo ========================================
echo 智能任务管理助手 - Streamlit Web UI
echo ========================================
echo.

cd /d E:\agent
echo 正在启动 Web UI...
echo 浏览器将自动打开 http://localhost:8501 （如被占用可改为 --server.port 8503）
echo 按 Ctrl+C 停止服务器
echo.

C:\Users\19434\.conda\envs\dog\python.exe -m streamlit run app.py

pause
