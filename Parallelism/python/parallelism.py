import requests
import json 
from multiprocessing import Process
import random
import time
from multiprocessing import Pool
import tracemalloc

def http_request(request_count):
    id_num = random.randint(1,100)
    res = requests.get('https://dummyjson.com/todos/' + str(id_num))
    response = json.loads(res.text)
    # print(response)

            
if __name__ == '__main__':
    tracemalloc.start()  # start tracing memory allocations
    start_time = time.perf_counter()
    with Pool() as pool:
        result = pool.map(http_request, range(0,25))
    finish_time = time.perf_counter()
    print("Program finished in {} seconds - using multiprocessing".format(finish_time-start_time))
    print("---")

    # start_time = time.perf_counter()
    # for i in range(100):
    #     http_request(100)
    # finish_time = time.perf_counter()
    # print("Program finished in {} seconds".format(finish_time-start_time))
    # print("---")

    current, peak = tracemalloc.get_traced_memory()
    print(f"Peak memory usage: {peak / 1024 / 1024:.2f} MB")
    tracemalloc.stop()  # stop tracing memory allocations