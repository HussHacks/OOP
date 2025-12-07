package com.jobsy.models;

import java.util.Date;

/**
 * Job posting entity.
 */
public class Job {

    private Long id;
    private String title;
    private String description;
    private Date datePosted;
    private String salary;
    private String companyName;
    private String jobType;
    private boolean isOpen = true;

    @com.fasterxml.jackson.annotation.JsonTypeInfo(use = com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NONE)
    private Employer employer;

    @com.fasterxml.jackson.annotation.JsonTypeInfo(use = com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NONE)
    private Employer postedBy;

    public Job() {
    }

    public Job(String title, String description, Date datePosted, Employer employer) {
        this.title = title;
        this.description = description;
        this.datePosted = datePosted;
        this.employer = employer;
        this.postedBy = employer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(Date datePosted) {
        this.datePosted = datePosted;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("isOpen")
    public boolean isOpen() {
        return isOpen;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("isOpen")
    public void setOpen(boolean open) {
        isOpen = open;
    }

    @com.fasterxml.jackson.annotation.JsonTypeInfo(use = com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NONE)
    public Employer getEmployer() {
        return employer;
    }

    public void setEmployer(Employer employer) {
        this.employer = employer;
        this.postedBy = employer;
    }

    public Employer getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(Employer postedBy) {
        this.postedBy = postedBy;
    }
}
