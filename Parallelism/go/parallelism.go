package main

import (
	"encoding/json"
	"fmt"
	"io/ioutil"
	"math/rand"
	"net/http"
	"time"
	//"sync"
	"runtime"
)

func httpRequest() {
	idNum := rand.Intn(100) + 1

	resp, err := http.Get(fmt.Sprintf("https://dummyjson.com/todos/%d", idNum))
	if err != nil {
		fmt.Println("Error:", err)
		return
	}
	defer resp.Body.Close()

	body, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		fmt.Println("Error:", err)
		return
	}

	var result map[string]interface{}
	err = json.Unmarshal(body, &result)
	if err != nil {
		fmt.Println("Error:", err)
		return
	}

	// Uncomment the following line to print the response
	 fmt.Println(result)
}

func main() {
	var m runtime.MemStats

	runtime.ReadMemStats(&m) // read current memory statistics
	numRequests := 75

	// Parallel execution using goroutines and a WaitGroup
	// startTime := time.Now()
	// var wg sync.WaitGroup

	// for i := 0; i < numRequests; i++ {
	// 	wg.Add(1)
	// 	go func() {
	// 		defer wg.Done()
	// 		httpRequest()
	// 	}()
	// }

	// wg.Wait()
	// elapsedTime := time.Since(startTime)
	// fmt.Printf("Total execution time: %.6f seconds\n", elapsedTime.Seconds())
	// fmt.Println("---")

	// Sequential execution
	startTime := time.Now()
	for i := 0; i < numRequests; i++ {
		httpRequest()
	}
	elapsedTime := time.Since(startTime)
	fmt.Printf("Total execution time: %.6f seconds\n", elapsedTime.Seconds())
	fmt.Println("---")

	var memStats runtime.MemStats
	runtime.ReadMemStats(&memStats)

	peakMemoryUsage := float64(memStats.HeapSys) / (1024 * 1024)
	fmt.Printf("Peak memory usage: %.2f MB\n", peakMemoryUsage)
}
