package com.jobsy.services;

import com.jobsy.dao.UserDAO;
import com.jobsy.models.User;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for managing users (Students and Employers).
 * Handles authentication and user management.
 */
public class UserService {

    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Save a user (Student or Employer)
     */
    public User save(User user) {
        return userDAO.save(user);
    }

    /**
     * Authenticate user with email and password
     */
    public User login(String email, String password) {
        Optional<User> userOpt = userDAO.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // WARNING: In production, use BCrypt for password hashing!
            if (user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Find user by email
     */
    public Optional<User> findByEmail(String email) {
        return userDAO.findByEmail(email);
    }

    /**
     * Find user by ID
     */
    public Optional<User> findById(Long id) {
        return userDAO.findById(id);
    }

    /**
     * Get all users
     */
    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    /**
     * Delete user by ID
     */
    public void deleteUser(Long id) {
        userDAO.delete(id);
    }
}
