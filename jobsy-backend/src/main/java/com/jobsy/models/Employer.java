package com.jobsy.models;

/**
 * Employer user type with company information.
 */
/**
 * Employer user type with company information.
 */
@com.fasterxml.jackson.annotation.JsonTypeInfo(use = com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NONE)
public class Employer extends User {

    private String companyName;
    private String companyDescription;
    private String industry;
    private String location;
    private String website;

    public Employer() {
    }

    public Employer(String username, String email, int age, String password) {
        super(username, email, age, password);
    }

    public Employer(String username, String email, int age, String password,
            String companyName, String companyDescription, String industry,
            String location, String website) {
        super(username, email, age, password);
        this.companyName = companyName;
        this.companyDescription = companyDescription;
        this.industry = industry;
        this.location = location;
        this.website = website;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyDescription() {
        return companyDescription;
    }

    public void setCompanyDescription(String companyDescription) {
        this.companyDescription = companyDescription;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    @Override
    public String getUserType() {
        return "employer";
    }
}
