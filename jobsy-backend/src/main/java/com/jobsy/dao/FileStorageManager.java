package com.jobsy.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Utility class to handle reading and writing objects to JSON files.
 */
public class FileStorageManager {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static <T> List<T> loadData(String fileName, Class<T[]> clazz) {
        File file = new File(fileName);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try {
            T[] array = objectMapper.readValue(file, clazz);
            return new ArrayList<>(Arrays.asList(array));
        } catch (IOException e) {
            System.err.println("Error reading file " + fileName + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static <T> void saveData(String fileName, List<T> data) {
        try {
            objectMapper.writeValue(new File(fileName), data);
        } catch (IOException e) {
            System.err.println("Error writing file " + fileName + ": " + e.getMessage());
            throw new RuntimeException("Error writing data to file", e);
        }
    }
}
