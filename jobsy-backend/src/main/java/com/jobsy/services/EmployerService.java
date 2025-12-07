package com.jobsy.services;

import com.jobsy.dao.UserDAO;
import com.jobsy.models.Employer;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service layer for managing employers.
 */
public class EmployerService {

    private final UserDAO userDAO;

    public EmployerService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public Employer save(Employer employer) {
        return (Employer) userDAO.save(employer);
    }

    public List<Employer> getAll() {
        return userDAO.findAll().stream()
                .filter(user -> user instanceof Employer)
                .map(user -> (Employer) user)
                .collect(Collectors.toList());
    }

    public Optional<Employer> findById(Long id) {
        return userDAO.findById(id)
                .filter(user -> user instanceof Employer)
                .map(user -> (Employer) user);
    }

    public Optional<Employer> findByEmail(String email) {
        return userDAO.findByEmail(email)
                .filter(user -> user instanceof Employer)
                .map(user -> (Employer) user);
    }

    public void delete(Long id) {
        userDAO.delete(id);
    }
}
