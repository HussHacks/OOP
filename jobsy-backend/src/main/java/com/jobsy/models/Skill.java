package com.jobsy.models;

/**
 * Skill entity - simplified without JPA.
 */
public class Skill {

    private Long id;
    private String name;
    private int level;

    public Skill() {
    }

    public Skill(String name, int level) {
        this.name = name;
        this.level = level;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean matches(Skill required) {
        return name.equalsIgnoreCase(required.name) && level >= required.level;
    }

    @Override
    public String toString() {
        return name + " (level " + level + ")";
    }
}
