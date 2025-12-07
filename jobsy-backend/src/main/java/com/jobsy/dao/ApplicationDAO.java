package com.jobsy.dao;

import com.jobsy.models.Application;
import com.jobsy.models.Job;
import com.jobsy.models.Student;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Data Access Object for Application entities using file persistence.
 */
public class ApplicationDAO {

    private static final String FILE_NAME = "applications.json";
    private final UserDAO userDAO = new UserDAO();
    private final JobDAO jobDAO = new JobDAO();

    public Application save(Application application) {
        List<Application> applications = FileStorageManager.loadData(FILE_NAME, Application[].class);

        if (application.getId() == null) {
            // Check for duplicates
            boolean exists = applications.stream()
                    .anyMatch(a -> a.getStudent() != null
                            && a.getStudent().getId().equals(application.getStudent().getId()) &&
                            a.getJob() != null && a.getJob().getId().equals(application.getJob().getId()));
            if (exists) {
                throw new RuntimeException("Student has already applied to this job");
            }

            // Insert new application
            long maxId = applications.stream()
                    .mapToLong(Application::getId)
                    .max()
                    .orElse(0);
            application.setId(maxId + 1);
            if (application.getDateApplied() == null) {
                application.setDateApplied(new Date());
            }
            applications.add(application);
        } else {
            // Update existing application
            boolean found = false;
            for (int i = 0; i < applications.size(); i++) {
                if (applications.get(i).getId().equals(application.getId())) {
                    applications.set(i, application);
                    found = true;
                    break;
                }
            }
            if (!found) {
                applications.add(application);
            }
        }

        FileStorageManager.saveData(FILE_NAME, applications);
        return application;
    }

    public Optional<Application> findById(Long id) {
        return loadAllAndHydrate().stream()
                .filter(a -> a.getId().equals(id))
                .findFirst();
    }

    public List<Application> findAll() {
        return loadAllAndHydrate().stream()
                .sorted(Comparator.comparing(Application::getDateApplied).reversed())
                .collect(Collectors.toList());
    }

    public List<Application> findByStudentId(Long studentId) {
        return loadAllAndHydrate().stream()
                .filter(a -> a.getStudent() != null && a.getStudent().getId().equals(studentId))
                .sorted(Comparator.comparing(Application::getDateApplied).reversed())
                .collect(Collectors.toList());
    }

    public List<Application> findByJobId(Long jobId) {
        return loadAllAndHydrate().stream()
                .filter(a -> a.getJob() != null && a.getJob().getId().equals(jobId))
                .sorted(Comparator.comparing(Application::getDateApplied).reversed())
                .collect(Collectors.toList());
    }

    public List<Application> findByStatus(String status) {
        return loadAllAndHydrate().stream()
                .filter(a -> a.getStatus() != null && a.getStatus().equalsIgnoreCase(status))
                .sorted(Comparator.comparing(Application::getDateApplied).reversed())
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        List<Application> applications = FileStorageManager.loadData(FILE_NAME, Application[].class);
        List<Application> updatedApplications = applications.stream()
                .filter(a -> !a.getId().equals(id))
                .collect(Collectors.toList());
        FileStorageManager.saveData(FILE_NAME, updatedApplications);
    }

    private List<Application> loadAllAndHydrate() {
        List<Application> applications = FileStorageManager.loadData(FILE_NAME, Application[].class);
        // Hydrate student and job data
        for (Application app : applications) {
            if (app.getStudent() != null && app.getStudent().getId() != null) {
                userDAO.findById(app.getStudent().getId()).ifPresent(user -> {
                    if (user instanceof Student) {
                        app.setStudent((Student) user);
                    }
                });
            }
            if (app.getJob() != null && app.getJob().getId() != null) {
                jobDAO.findById(app.getJob().getId()).ifPresent(app::setJob);
            }
        }
        return applications;
    }
}
