package main

import "fmt"

type Customer struct {
	name         string
	currentQueue int
}

func manipulateQueue(queue1 chan Customer, queue2 chan Customer, currentQueue int, exit chan bool) {
	for len(queue1) > 0 || len(queue2) > 0 {
		if len(queue1) == 0 {
			customer := <-queue2
			queue1 <- customer
			fmt.Println("Customer", customer.name, "came to", currentQueue, "queue")
		}
		customer := <-queue1
		fmt.Println("Serviced customer", customer.name, "in", currentQueue, "queue")
		fmt.Println(currentQueue, "queue has", len(queue1), "customers")
		len1 := len(queue1)
		len2 := len(queue2)
		if len1 > len2+1 {
			customer := <-queue1
			queue2 <- customer
			fmt.Println("Customer", customer.name, "left", currentQueue, "queue")
		}
	}
	exit <- true
}

func main() {
	customer1 := Customer{name: "Alex", currentQueue: 1}
	customer2 := Customer{name: "Dasha", currentQueue: 1}
	customer3 := Customer{name: "Andrew", currentQueue: 2}
	customer4 := Customer{name: "Dima", currentQueue: 2}
	customer5 := Customer{name: "Sasha", currentQueue: 1}
	customer6 := Customer{name: "Petya", currentQueue: 1}

	queue1 := make(chan Customer, 6)
	queue2 := make(chan Customer, 6)
	exit := make(chan bool, 1)

	queue1 <- customer1
	queue1 <- customer2
	queue1 <- customer5
	queue1 <- customer6

	queue2 <- customer3
	queue2 <- customer4

	go manipulateQueue(queue1, queue2, 1, exit)
	go manipulateQueue(queue2, queue1, 2, exit)
	<-exit
}
