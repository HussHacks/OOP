package com.jobsy.models;

/**
 * Abstract base class for all users in the system.
 * Subclasses: Student, Employer
 */
public abstract class User {

    private Long id;
    private String username;
    private String email;
    private int age;
    private String password;

    public User() {
    }

    public User(String username, String email, int age, String password) {
        this.username = username;
        this.email = email;
        this.age = age;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("userType")
    public abstract String getUserType();
}
