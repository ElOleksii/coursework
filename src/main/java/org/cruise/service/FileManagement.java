package org.cruise.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.cruise.model.Cashier;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileManagement {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> void saveToFile(List<T> objects, String filePath) {
        try {
            objectMapper.writeValue(new File(filePath), objects);
            System.out.println("Data saved to " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error saving data to file.");
        }
    }

    public static <T> List<T> loadFromFile(String filePath, TypeReference<List<T>> typeRef) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                System.out.println("File not found: " + filePath);
                return new ArrayList<>(); // Return empty list if file does not exist
            }

            return objectMapper.readValue(file, typeRef);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading objects from file: " + filePath);
            return new ArrayList<>(); // Return empty list on error
        }
    }

}
