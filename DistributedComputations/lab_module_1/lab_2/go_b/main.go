package main

import "fmt"

var amountOfItems = 10

func take(takenChan chan int) {
	for i := 0; i < amountOfItems; i++ {
		fmt.Println("Ivanov took from storage.")
		takenChan <- 1
	}
}

func load(takenChan chan int, loadedChan chan int) {
	for i := 0; i < amountOfItems; i++ {
		<-takenChan
		fmt.Println("Petrov loaded to lorry.")
		loadedChan <- 1
	}
}

func count(loadedChan chan int) {
	for i := 0; i < amountOfItems; i++ {
		<-loadedChan
		fmt.Println("Nechiporchuk counted total price.")
	}
}

func main() {
	var takenChan = make(chan int, 1)
	var loadedChan = make(chan int, 1)

	go take(takenChan)
	go load(takenChan, loadedChan)
	go count(loadedChan)

	fmt.Scanln()
}
