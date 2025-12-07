package com.jobsy.dao;

import com.jobsy.models.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Data Access Object for User entities (Students and Employers) using file
 * persistence.
 */
public class UserDAO {

    private static final String FILE_NAME = "users.json";

    public User save(User user) {
        List<User> users = findAll();

        if (user.getId() == null) {
            // Insert new user
            long maxId = users.stream()
                    .mapToLong(User::getId)
                    .max()
                    .orElse(0);
            user.setId(maxId + 1);
            users.add(user);
        } else {
            // Update existing user
            boolean found = false;
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getId().equals(user.getId())) {
                    users.set(i, user);
                    found = true;
                    break;
                }
            }
            if (!found) {
                users.add(user);
            }
        }

        FileStorageManager.saveData(FILE_NAME, users);
        return user;
    }

    public Optional<User> findById(Long id) {
        List<User> users = findAll();
        return users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();
    }

    public Optional<User> findByEmail(String email) {
        List<User> users = findAll();
        return users.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    public List<User> findAll() {
        // We need to manually handle polymorphism since we removed the annotations from
        // User class
        // This is a bit of a hack for file persistence without proper Jackson
        // polymorphism
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            mapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            com.fasterxml.jackson.databind.JsonNode rootNode = mapper.readTree(new java.io.File(FILE_NAME));
            List<User> users = new java.util.ArrayList<>();

            if (rootNode.isArray()) {
                for (com.fasterxml.jackson.databind.JsonNode node : rootNode) {
                    if (node.has("userType")) {
                        String type = node.get("userType").asText();
                        if ("student".equalsIgnoreCase(type)) {
                            users.add(mapper.treeToValue(node, com.jobsy.models.Student.class));
                        } else if ("employer".equalsIgnoreCase(type)) {
                            users.add(mapper.treeToValue(node, com.jobsy.models.Employer.class));
                        }
                    }
                }
            }
            return users;
        } catch (Exception e) {
            // If file doesn't exist or error, return empty list (or handle properly)
            return new java.util.ArrayList<>();
        }
    }

    public void delete(Long id) {
        List<User> users = FileStorageManager.loadData(FILE_NAME, User[].class);
        List<User> updatedUsers = users.stream()
                .filter(u -> !u.getId().equals(id))
                .collect(Collectors.toList());
        FileStorageManager.saveData(FILE_NAME, updatedUsers);
    }
}
