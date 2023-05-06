import os
import asyncio
from asyncio import Queue
import time
import tracemalloc

tracemalloc.start()  # start tracing memory allocations

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

    start_time = time.monotonic()
    await asyncio.gather(*tasks)
    elapsed_time = time.monotonic() - start_time
    print(f"Total execution time: {elapsed_time:.2f} seconds")

    print("---")


asyncio.run(main())

current, peak = tracemalloc.get_traced_memory()
print(f"Peak memory usage: {peak / 1024 / 1024:.2f} MB")
tracemalloc.stop()  # stop tracing memory allocations
