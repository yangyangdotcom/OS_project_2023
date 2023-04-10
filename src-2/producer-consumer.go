package main

import (
	"fmt"
	"runtime"
	"sync"
)

const (
	producers = 1000
	consumers = 1000
	capacity  = 10
)

func main() {
	// Set the number of CPU cores used by the program to 1
	runtime.GOMAXPROCS(1)

	var wg sync.WaitGroup
	wg.Add(producers + consumers)

	buffer := make(chan int, capacity)

	// Launch producer Goroutines
	for i := 0; i < producers; i++ {
		go func(id int) {
			defer wg.Done()
			buffer <- id
			fmt.Printf("Producer %d produced\n", id)
		}(i)
	}

	// Launch consumer Goroutines
	for i := 0; i < consumers; i++ {
		go func(id int) {
			defer wg.Done()
			value := <-buffer
			fmt.Printf("Consumer %d consumed value %d\n", id, value)
		}(i)
	}

	wg.Wait()
	fmt.Println("Done")
}
