package org.example.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.enums.FileNameExtension;
import java.io.File;
import java.io.IOException;

public class AnimalSerializer {
    private final ObjectMapper mapper;
    private final FileNameExtension extension;
    private final String path = "src/main/resources/animal";
    private final PetShelter petShelter;

    public AnimalSerializer(ObjectMapper mapper, FileNameExtension extension, PetShelter petShelter) {
        this.mapper = mapper;
        this.extension = extension;
        this.petShelter = petShelter;
    }

    public void serialize() {
        try {
            mapper.writeValue(new File(path + extension), petShelter.getAnimals());
        } catch (IOException e) {
            System.out.println("Error during data saving.");
        }
    }

    public void deSerialize() {
        try {
            File file = new File(path + extension);
            if (file.exists()) {
                petShelter.setAnimals(mapper.readValue(file, new TypeReference<>() {}));
            }
        } catch (IOException e) {
            System.out.println("Error during loading data.");
        }
    }
}
