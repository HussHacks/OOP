package com.jobsy.services;

import com.jobsy.dao.JobDAO;
import com.jobsy.models.Job;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for managing jobs.
 */
public class JobService {

    private final JobDAO jobDAO;

    public JobService(JobDAO jobDAO) {
        this.jobDAO = jobDAO;
    }

    // CREATE / SAVE
    public Job saveJob(Job job) {
        if (job.getTitle() == null || job.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Job title cannot be empty");
        }
        if (job.getDescription() == null || job.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Job description cannot be empty");
        }
        return jobDAO.save(job);
    }

    // READ ALL
    public List<Job> getAllJobs() {
        return jobDAO.findAll();
    }

    // READ BY ID
    public Optional<Job> getJobById(Long id) {
        return jobDAO.findById(id);
    }

    // UPDATE
    public Job updateJob(Job job) {
        return jobDAO.save(job);
    }

    // DELETE
    public void deleteJob(Long id) {
        jobDAO.delete(id);
    }

    // Custom queries
    public List<Job> getJobsByEmployer(Long employerId) {
        return jobDAO.findByEmployerId(employerId);
    }

    public List<Job> getOpenJobs() {
        return jobDAO.findOpenJobs();
    }

    public List<Job> searchJobsByTitle(String keyword) {
        return jobDAO.searchByTitle(keyword);
    }
}
