package com.jobsy.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Profile entity for user profiles.
 */
public class Profile {

    private Long id;
    private String bio;
    private String education;
    private String experience;
    private List<String> skills;

    public Profile() {
        this.bio = "";
        this.education = "";
        this.experience = "";
        this.skills = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public void addSkill(String skill) {
        skills.add(skill);
    }

    public void removeSkill(String skill) {
        skills.remove(skill);
    }

    public void displayProfile() {
        System.out.println("Bio: " + bio);
        System.out.println("Education: " + education);
        System.out.println("Experience: " + experience);
        System.out.println("Skills: " + (skills.isEmpty() ? "None" : String.join(", ", skills)));
    }
}