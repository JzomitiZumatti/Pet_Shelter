package org.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.example.enums.FileNameExtension;
import org.example.model.Animal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PetShelter {
    private List<Animal> animals;
    private final ObjectMapper objectMapper;
    private final Scanner scanner;
    private final AnimalSerializer animalSerializer;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private boolean isOn = true;

    public PetShelter() {
        animals = new ArrayList<>();
        objectMapper = new ObjectMapper();
        scanner = new Scanner(System.in);
        animalSerializer = new AnimalSerializer(new YAMLMapper(), FileNameExtension.YAML, this);
        loadPetData();
    }

    public List<Animal> getAnimals() {
        return animals;
    }

    public void setAnimals(List<Animal> animals) {
        this.animals = animals;
    }

    public void run() {
        while (isOn) {
            displayMenu();
            String choice = getUserChoice();

            switch (choice) {
                case "1" -> addPet();
                case "2" -> showAllPets();
                case "3" -> takePetFromShelter();
                case "0" -> saveAndExit();
                default -> System.out.println("Wrong choice. Try again.");
            }
        }
    }

    public void displayMenu() {
        System.out.println("\nMenu:");
        System.out.println("1. Add animal;");
        System.out.println("2. Show all animals;");
        System.out.println("3. Take the pet from shelter;");
        System.out.println("0. Exit.");
    }

    public String getUserChoice() {
        System.out.print("Enter the number of action: ");
        return scanner.next();
    }

    public void addPet() {
        System.out.print("Enter species of pet: ");
        String species = scanner.next();
        System.out.print("Enter " + species + "'s name: ");
        String name = scanner.next();
        int age;
        do {
            System.out.print("Enter " + name + "'s age: ");
            while (!scanner.hasNextInt()) {
                System.out.println("That's not a valid age. Please enter a valid number.");
                System.out.print("Enter " + name + "'s age: ");
                scanner.nextLine();
            }
            age = scanner.nextInt();

            if (age <= 0) {
                System.out.println("Age must be a positive number. Please enter a valid age.");
            }

        } while (age <= 0);
        String dateOfArrival = LocalDateTime.now().format(dateFormatter);

        Animal animal = new Animal(species, name, age, dateOfArrival);
        animals.add(animal);

        System.out.println("Animal added to list.");
    }

    public void showAllPets() {
        if (animals.isEmpty()) {
            System.out.println("Shelter is empty.");
        } else {
            System.out.println("Animals in shelter:");
            animals.stream()
                    .map(animal -> animals.indexOf(animal) + 1 + ". " + animal)
                    .forEach(System.out::println);
        }
    }

    public void takePetFromShelter() {
        showAllPets();

        if (!animals.isEmpty()) {
            System.out.print("Choose the number of animal to take it away: ");
            int index = scanner.nextInt() - 1;

            if (index >= 0 && index < animals.size()) {
                Animal removedAnimal = animals.remove(index);
                System.out.println("Animal took from shelter: " + removedAnimal);
            } else {
                System.out.println("Wrong number of animal.");
            }
        }
    }

    public void saveAndExit() {
        animalSerializer.serialize();
        isOn = false;
        System.out.println("Data saved. See you next time!");
    }

    private void loadPetData() {
        animalSerializer.deSerialize();
    }
}
