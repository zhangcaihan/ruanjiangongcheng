import asyncio
from datetime import date, timedelta, datetime

from mcp import ClientSession
from mcp.client.stdio import stdio_client, StdioServerParameters

# Windows Conda Python and server entry
PYTHON = r"C:\Users\19434\.conda\envs\dog\python.exe"
SERVER = r"E:\agent\mcp_server\server.py"


async def main():
    params = StdioServerParameters(command=PYTHON, args=[SERVER])
    async with stdio_client(params) as (read, write):
        async with ClientSession(read, write) as session:
            await session.initialize()

            # Helper to unwrap structured results
            def unwrap(result):
                sc = getattr(result, "structuredContent", None)
                return sc.get("result") if isinstance(sc, dict) else sc

            # Timestamped title to avoid collisions
            stamp = datetime.now().strftime("%H%M%S")
            title1 = f"验证：MCP任务A-{stamp}"
            title2 = f"验证：MCP任务B-{stamp}"
            dl = (date.today() + timedelta(days=1)).strftime("%Y-%m-%d")

            # 1) add_task x2
            add_res1 = await session.call_tool(
                "add_task",
                arguments={
                    "title": title1,
                    "priority": "高",
                    "deadline": dl,
                    "category": "测试",
                },
            )
            print("[add_task-1]", unwrap(add_res1))

            add_res2 = await session.call_tool(
                "add_task",
                arguments={
                    "title": title2,
                    "priority": "中",
                    "deadline": dl,
                    "category": "测试",
                },
            )
            print("[add_task-2]", unwrap(add_res2))

            # 2) list_tasks (all)
            list_all = await session.call_tool("list_tasks", arguments={})
            tasks_all = unwrap(list_all) or []
            print("[list_tasks/all] count:", len(tasks_all))
            if tasks_all:
                print("[list_tasks/all] last:", tasks_all[-1])

            # Find ids for the two new tasks
            def find_id(title: str):
                for t in tasks_all[::-1]:
                    if t.get("title") == title:
                        return t.get("id")
                return None

            id1 = find_id(title1)
            id2 = find_id(title2)
            print("[ids]", id1, id2)

            # 3) search_tasks
            search = await session.call_tool("search_tasks", arguments={"keyword": "MCP任务"})
            search_tasks = unwrap(search) or []
            print("[search_tasks] count:", len(search_tasks))

            # 4) update_task_status for id1 -> 进行中
            if id1 is not None:
                up_res = await session.call_tool(
                    "update_task_status",
                    arguments={"task_id": id1, "status": "进行中"},
                )
                print("[update_task_status]", unwrap(up_res))

            # 5) list_tasks(status='进行中')
            inprog = await session.call_tool("list_tasks", arguments={"status": "进行中"})
            inprog_list = unwrap(inprog) or []
            print("[list_tasks/进行中] count:", len(inprog_list))

            # 6) complete_task for id1
            if id1 is not None:
                comp = await session.call_tool("complete_task", arguments={"task_id": id1})
                print("[complete_task]", unwrap(comp))

            # 7) list_tasks(status='已完成')
            done = await session.call_tool("list_tasks", arguments={"status": "已完成"})
            done_list = unwrap(done) or []
            print("[list_tasks/已完成] count:", len(done_list))

            # 8) generate_todo_list
            todo_text = unwrap(await session.call_tool("generate_todo_list", arguments={}))
            if isinstance(todo_text, str):
                print("[todo_list] preview:\n", "\n".join(todo_text.splitlines()[:5]))
            else:
                print("[todo_list] structured:", todo_text)

            # 9) stats
            stats_text = unwrap(await session.call_tool("stats", arguments={}))
            print("[stats]", stats_text)

            # 10) delete_task id2 (cleanup)
            if id2 is not None:
                del_res = await session.call_tool("delete_task", arguments={"task_id": id2})
                print("[delete_task]", unwrap(del_res))


if __name__ == "__main__":
    asyncio.run(main())
