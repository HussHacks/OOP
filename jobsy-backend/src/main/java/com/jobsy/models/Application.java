package com.jobsy.models;

import java.util.Date;

/**
 * Job application entity.
 */
public class Application {

    private Long id;
    private Student student;
    private Job job;
    private String status;
    private Date dateApplied;

    public Application() {
    }

    public Application(Student student, Job job, String status, Date dateApplied) {
        this.student = student;
        this.job = job;
        this.status = status;
        this.dateApplied = dateApplied;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDateApplied() {
        return dateApplied;
    }

    public void setDateApplied(Date dateApplied) {
        this.dateApplied = dateApplied;
    }
}
