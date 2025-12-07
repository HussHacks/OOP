package com.jobsy.services;

import com.jobsy.dao.ApplicationDAO;
import com.jobsy.models.Application;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for managing job applications.
 * Handles business logic for creating, retrieving, updating, and deleting
 * applications.
 */
public class ApplicationService {

    private final ApplicationDAO applicationDAO;

    public ApplicationService(ApplicationDAO applicationDAO) {
        this.applicationDAO = applicationDAO;
    }

    /**
     * Submit a new job application
     */
    public Application submitApplication(Application application) {
        // Set default status if not provided
        if (application.getStatus() == null || application.getStatus().isEmpty()) {
            application.setStatus("PENDING");
        }

        return applicationDAO.save(application);
    }

    /**
     * Get all applications
     */
    public List<Application> getAllApplications() {
        return applicationDAO.findAll();
    }

    /**
     * Get application by ID
     */
    public Optional<Application> getApplicationById(Long id) {
        return applicationDAO.findById(id);
    }

    /**
     * Get all applications by a specific student
     */
    public List<Application> getApplicationsByStudent(Long studentId) {
        return applicationDAO.findByStudentId(studentId);
    }

    /**
     * Get all applications for a specific job
     */
    public List<Application> getApplicationsByJob(Long jobId) {
        return applicationDAO.findByJobId(jobId);
    }

    /**
     * Accept an application
     */
    public Application acceptApplication(Application application) {
        application.setStatus("ACCEPTED");
        return applicationDAO.save(application);
    }

    /**
     * Reject an application
     */
    public Application rejectApplication(Application application) {
        application.setStatus("REJECTED");
        return applicationDAO.save(application);
    }

    /**
     * Delete an application
     */
    public void deleteApplication(Long id) {
        applicationDAO.delete(id);
    }

    /**
     * Get applications by status
     */
    public List<Application> getApplicationsByStatus(String status) {
        return applicationDAO.findByStatus(status);
    }
}
