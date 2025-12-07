package com.jobsy.services;

import com.jobsy.dao.UserDAO;
import com.jobsy.models.Student;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service layer for managing students.
 */
public class StudentService {

    private final UserDAO userDAO;

    public StudentService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public Student save(Student student) {
        return (Student) userDAO.save(student);
    }

    public List<Student> getAll() {
        return userDAO.findAll().stream()
                .filter(user -> user instanceof Student)
                .map(user -> (Student) user)
                .collect(Collectors.toList());
    }

    public Optional<Student> findById(Long id) {
        return userDAO.findById(id)
                .filter(user -> user instanceof Student)
                .map(user -> (Student) user);
    }

    public Optional<Student> findByEmail(String email) {
        return userDAO.findByEmail(email)
                .filter(user -> user instanceof Student)
                .map(user -> (Student) user);
    }

    public void delete(Long id) {
        userDAO.delete(id);
    }
}
