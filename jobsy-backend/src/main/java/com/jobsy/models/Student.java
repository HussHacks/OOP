package com.jobsy.models;

/**
 * Student user type with academic and skill information.
 */
public class Student extends User {

    private String skills; // Comma-separated skills
    private String education; // Education background
    private String major; // Major/field of study
    private Double gpa; // GPA

    public Student() {
    }

    public Student(String username, String email, int age, String password) {
        super(username, email, age, password);
    }

    public Student(String username, String email, int age, String password,
            String skills, String education, String major, Double gpa) {
        super(username, email, age, password);
        this.skills = skills;
        this.education = education;
        this.major = major;
        this.gpa = gpa;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public Double getGpa() {
        return gpa;
    }

    public void setGpa(Double gpa) {
        this.gpa = gpa;
    }

    @Override
    public String getUserType() {
        return "student";
    }
}
