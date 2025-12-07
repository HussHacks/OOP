package com.jobsy.dao;

import com.jobsy.models.Employer;
import com.jobsy.models.Job;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Data Access Object for Job entities using file persistence.
 */
public class JobDAO {

    private static final String FILE_NAME = "jobs.json";
    private final UserDAO userDAO = new UserDAO();

    public Job save(Job job) {
        List<Job> jobs = FileStorageManager.loadData(FILE_NAME, Job[].class);

        if (job.getId() == null) {
            // Insert new job
            long maxId = jobs.stream()
                    .mapToLong(Job::getId)
                    .max()
                    .orElse(0);
            job.setId(maxId + 1);
            if (job.getDatePosted() == null) {
                job.setDatePosted(new Date());
            }
            jobs.add(job);
        } else {
            // Update existing job
            boolean found = false;
            for (int i = 0; i < jobs.size(); i++) {
                if (jobs.get(i).getId().equals(job.getId())) {
                    jobs.set(i, job);
                    found = true;
                    break;
                }
            }
            if (!found) {
                jobs.add(job);
            }
        }

        FileStorageManager.saveData(FILE_NAME, jobs);
        return job;
    }

    public Optional<Job> findById(Long id) {
        List<Job> jobs = loadAllAndHydrate();
        return jobs.stream()
                .filter(j -> j.getId().equals(id))
                .findFirst();
    }

    public List<Job> findAll() {
        return loadAllAndHydrate().stream()
                .sorted(Comparator.comparing(Job::getDatePosted).reversed())
                .collect(Collectors.toList());
    }

    public List<Job> findByEmployerId(Long employerId) {
        return loadAllAndHydrate().stream()
                .filter(j -> j.getEmployer() != null && j.getEmployer().getId().equals(employerId))
                .sorted(Comparator.comparing(Job::getDatePosted).reversed())
                .collect(Collectors.toList());
    }

    public List<Job> searchByTitle(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return loadAllAndHydrate().stream()
                .filter(j -> j.getTitle() != null && j.getTitle().toLowerCase().contains(lowerKeyword))
                .sorted(Comparator.comparing(Job::getDatePosted).reversed())
                .collect(Collectors.toList());
    }

    public List<Job> findOpenJobs() {
        return loadAllAndHydrate().stream()
                .filter(Job::isOpen)
                .sorted(Comparator.comparing(Job::getDatePosted).reversed())
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        List<Job> jobs = FileStorageManager.loadData(FILE_NAME, Job[].class);
        List<Job> updatedJobs = jobs.stream()
                .filter(j -> !j.getId().equals(id))
                .collect(Collectors.toList());
        FileStorageManager.saveData(FILE_NAME, updatedJobs);
    }

    private List<Job> loadAllAndHydrate() {
        List<Job> jobs = FileStorageManager.loadData(FILE_NAME, Job[].class);
        // Hydrate employer data to ensure it's up to date
        for (Job job : jobs) {
            if (job.getEmployer() != null && job.getEmployer().getId() != null) {
                userDAO.findById(job.getEmployer().getId()).ifPresent(user -> {
                    if (user instanceof Employer) {
                        job.setEmployer((Employer) user);
                    }
                });
            }
        }
        return jobs;
    }
}
