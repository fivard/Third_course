package main

import (
	"bufio"
	"fmt"
	"os"
	"strings"
)

type Animal interface {
	Eat()
	Move()
	Speak()
}

type Cow struct {
	food       string
	locomotion string
	noise      string
}

func (cow Cow) Eat() {
	fmt.Println(cow.food)
}
func (cow Cow) Move() {
	fmt.Println(cow.locomotion)
}
func (cow Cow) Speak() {
	fmt.Println(cow.noise)
}

type Bird struct {
	food       string
	locomotion string
	noise      string
}

func (bird Bird) Eat() {
	fmt.Println(bird.food)
}
func (bird Bird) Move() {
	fmt.Println(bird.locomotion)
}
func (bird Bird) Speak() {
	fmt.Println(bird.noise)
}

type Snake struct {
	food       string
	locomotion string
	noise      string
}

func (snake Snake) Eat() {
	fmt.Println(snake.food)
}
func (snake Snake) Move() {
	fmt.Println(snake.locomotion)
}
func (snake Snake) Speak() {
	fmt.Println(snake.noise)
}

func createAnimal(animalType string) Animal {
	var newAnimal Animal
	switch animalType {
	case "cow":
		newAnimal = Cow{food: "grass", locomotion: "walk", noise: "moo"}
	case "bird":
		newAnimal = Bird{food: "worms", locomotion: "fly", noise: "peep"}
	case "snake":
		newAnimal = Snake{food: "mice", locomotion: "slither", noise: "hsss"}
	}
	return newAnimal
}

func main() {
	animals := map[string]Animal{}
	var line string
	for {
		fmt.Print(">")
		scanner := bufio.NewScanner(os.Stdin)
		for scanner.Scan() {
			line = scanner.Text()
			break
		}
		if line == "X" {
			break
		}
		info := strings.Split(line, " ")
		command, animalName := info[0], info[1]
		switch command {
		case "newanimal":
			animalType := info[2]
			newAnimal := createAnimal(animalType)
			animals[animalName] = newAnimal
		case "query":
			animalAction := info[2]
			switch animalAction {
			case "eat":
				animals[animalName].Eat()
			case "move":
				animals[animalName].Move()
			case "speak":
				animals[animalName].Speak()
			}
		}
	}
}