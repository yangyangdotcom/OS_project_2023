import os
import asyncio
from asyncio import Queue

os.environ["PYTHONASYNCIODEBUG"] = "1"

producers = 1000
consumers = 1000
capacity = 10

async def producer(queue, id):
    await queue.put(id)
    print(f"Producer {id} produced")

async def consumer(queue, id):
    value = await queue.get()
    print(f"Consumer {id} consumed value {value}")

async def main():
    queue = Queue(capacity)
    tasks = []

    # Launch producer tasks
    for i in range(producers):
        tasks.append(asyncio.create_task(producer(queue, i)))

    # Launch consumer tasks
    for i in range(consumers):
        tasks.append(asyncio.create_task(consumer(queue, i)))

    await asyncio.gather(*tasks)

asyncio.run(main())
