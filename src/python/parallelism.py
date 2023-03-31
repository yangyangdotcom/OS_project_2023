import requests
import json 
from multiprocessing import Process
import random
import time
from tqdm import tqdm
from multiprocessing import Pool

def http_request(request_count):
    # for i in tqdm(range(request_count)):
        id_num = random.randint(1,100)
        res = requests.get('https://dummyjson.com/todos/' + str(id_num))
        response = json.loads(res.text)
        # print(response)

            
if __name__ == '__main__':
    start_time = time.perf_counter()
    with Pool() as pool:
        result = pool.map(http_request, range(0,1000))
    finish_time = time.perf_counter()
    print("Program finished in {} seconds - using multiprocessing".format(finish_time-start_time))
    print("---")

    start_time = time.perf_counter()
    for i in range(1000):
        http_request(1000)
    finish_time = time.perf_counter()
    print("Program finished in {} seconds".format(finish_time-start_time))
    print("---")