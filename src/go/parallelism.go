package main

import (
	"encoding/json"
	"fmt"
	"io/ioutil"
	"math/rand"
	"net/http"
	"sync"
	"time"
)

func httpRequest(requestCount int) {
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
	// fmt.Println(result)
}

func main() {
	numRequests := 1000

	// Parallel execution using goroutines and a WaitGroup
	startTime := time.Now()
	var wg sync.WaitGroup

	for i := 0; i < numRequests; i++ {
		wg.Add(1)
		go func() {
			defer wg.Done()
			httpRequest(1000)
		}()
	}

	wg.Wait()
	elapsedTime := time.Since(startTime)
	fmt.Printf("Program finished in %s - using goroutines\n", elapsedTime)
	fmt.Println("---")

	// Sequential execution
	startTime = time.Now()
	for i := 0; i < numRequests; i++ {
		httpRequest(1000)
	}
	elapsedTime = time.Since(startTime)
	fmt.Printf("Program finished in %s\n", elapsedTime)
	fmt.Println("---")
}
