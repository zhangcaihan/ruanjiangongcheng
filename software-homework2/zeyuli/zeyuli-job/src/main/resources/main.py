#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
SSE 流式返回 DeepSeek 旅行计划 JSON
java 调用方式示例：
  java 通过 ProcessBuilder 启动本脚本，把参数 JSON 写入 stdin，
  然后逐行读取 stdout 即可获得 SSE 片段，与原有 Flux<String> 兼容。
"""
import json
import sys
import os
from datetime import datetime
from openai import OpenAI
# 在 main.py 中添加，放在文件开头
import sys
import io

# main.py 开头添加
import sys
import codecs

# 设置标准流的编码为UTF-8
if sys.version_info[0] < 3:
    # Python 2.x
    sys.stdin = codecs.getreader('utf-8')(sys.stdin)
    sys.stdout = codecs.getwriter('utf-8')(sys.stdout)
    sys.stderr = codecs.getwriter('utf-8')(sys.stderr)
else:
    # Python 3.x
    sys.stdin.reconfigure(encoding='utf-8')
    sys.stdout.reconfigure(encoding='utf-8')

# 设置无缓冲输出
sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8', write_through=True)


# ---------- 配置 ----------
client = OpenAI(
    api_key=os.getenv("DEEPSeek_API_KEY", "sk-84dbc668f2d048ddb6c39ace5ae78f44"),
    base_url="https://api.deepseek.com"
)
MODEL = "deepseek-chat"


# ---------- 工具 ----------
def build_prompt(data: dict) -> str:
    start = data["startCity"]
    end = data["endCity"]
    start_date = datetime.fromisoformat(data["startDate"])
    end_date = datetime.fromisoformat(data["endDate"])
    user_input = data.get("userInput", "").strip()

    days = (end_date - start_date).days + 1
    fmt_date = lambda d: d.strftime("%Y-%m-%d")

    if not user_input:
        # 无用户输入版本
        return (
            "你是严格的 JSON 生成器，只返回合法 JSON，禁止任何注释、解释、markdown 代码块。\n"
            "若格式错误，用户将无法解析，视为严重事故。\n\n"
            "下面是一段已验证的范例，你必须保持完全相同的键名、嵌套深度、数据类型（String/Number/List），"
            "仅把内容替换成本次行程的真实信息。\n\n"
            '=== 范例开始 ===\n'
            '{\n'
            '  "days": [\n'
            '    {\n'
            '      "dayIndex": 1,\n'
            '      "date": "2025-12-20",\n'
            '      "label": "第1天",\n'
            '      "items": [\n'
            '        {\n'
            '          "time": "09:00",\n'
            '          "title": "抵达厦门",\n'
            '          "description": "- 抵达 **厦门高崎机场**\\n- 乘坐 **机场快线** 前往市区\\n> 建议提前购买 **厦门公交卡**",\n'
            '          "attractions": "厦门高崎机场",\n'
            '          "cost": "约30元",\n'
            '          "durationHours": 1\n'
            '        }\n'
            '      ]\n'
            '    }\n'
            '  ]\n'
            '}\n'
            '=== 范例结束 ===\n\n'
            f'本次行程信息：\n'
            f'- 出发地：{start}\n'
            f'- 目的地：{end}\n'
            f'- 出发日期：{fmt_date(start_date)}\n'
            f'- 返程日期：{fmt_date(end_date)}\n'
            f'- 总天数：{days}\n\n'
            "严格按照范例的键名、结构、类型生成 JSON，description 字段允许使用 Markdown 排版。\n"
            "禁止输出范例之外的多余文字、禁止包裹 ```json、禁止注释。"
        )
    else:
        # 有用户输入版本
        return (
            "输出格式与字段要求与刚才完全相同，description 仍支持 Markdown。\n\n"
            f'用户修改要求：\n{user_input}\n\n'
            f'行程天数：{days} 天\n'
            f'出发：{start} → {end}，{fmt_date(start_date)} 至 {fmt_date(end_date)}\n\n'
            "请直接返回 JSON，不要任何额外文字。"
        )


def stream_json_travel(data: dict):
    """流式返回【固定格式】旅行计划 JSON（兼容 DeepSeek 当前接口）"""
    prompt = build_prompt(data)          # 你原来的 prompt 逻辑照旧

    # 1. 把目标 schema 纯文本写死到 system 里
    system_text = (
        "你是严格的 JSON 生成器，必须全程保持沉默，只输出合法 JSON，"
        "禁止任何注释、解释、markdown 代码块。输出必须严格符合下面 Schema：\n"
        '{"days":[{"dayIndex":number,"date":"string","label":"string",'
        '"items":[{"time":"string","title":"string","description":"string",'
        '"attractions":"string","cost":"string","durationHours":number}]}]}\n'
        "字段名、类型、层级均不得变动；没有的内容用空字符串或 0 填充。"
    )

    # 2. 普通 json_object 模式 + 温度 0
    response = client.chat.completions.create(
        model=MODEL,
        messages=[
            {"role": "system", "content": system_text},
            {"role": "user",   "content": prompt}
        ],
        response_format={"type": "json_object"},  # DeepSeek 仅支持这一种
        stream=True,
        temperature=0.0,
        # 如 DeepSeek 后续开放 seed，可再加 seed=42 保证完全复现
    )

    for chunk in response:
        delta = chunk.choices[0].delta.content
        if delta:
            yield delta


# ---------- main ----------
if __name__ == "__main__":
    # 1. 读参数
    raw = sys.stdin.read().strip()
    if not raw:
        print("error: empty stdin", flush=True)
        sys.exit(1)
    try:
        args = json.loads(raw)
    except Exception as e:
        print(f"error: invalid json - {e}", flush=True)
        sys.exit(1)

    # 2. 逐片输出，模拟 SSE（每行一段）
    try:
        for piece in stream_json_travel(args):
            print(piece, flush=True)
    except Exception as e:
        print(f"error: {e}", flush=True)
        sys.exit(1)
