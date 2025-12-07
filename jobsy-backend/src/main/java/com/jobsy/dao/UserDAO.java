package com.jobsy.dao;

import com.jobsy.config.DatabaseConfig;
import com.jobsy.models.User;
import com.jobsy.models.Student;
import com.jobsy.models.Employer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for User entities (Students and Employers).
 */
public class UserDAO {

    public User save(User user) {
        String userType = user instanceof Student ? "STUDENT" : "EMPLOYER";

        if (user.getId() == null) {
            // Insert new user
            String sql = """
                        INSERT INTO USERS (USER_TYPE, USERNAME, EMAIL, AGE, PASSWORD,
                                           SKILLS, EDUCATION, MAJOR, GPA,
                                           COMPANY_NAME, COMPANY_DESCRIPTION, INDUSTRY, LOCATION, WEBSITE)
                        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                    """;
            try (Connection conn = DatabaseConfig.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                stmt.setString(1, userType);
                stmt.setString(2, user.getUsername());
                stmt.setString(3, user.getEmail());
                stmt.setInt(4, user.getAge());
                stmt.setString(5, user.getPassword());

                // Student fields
                if (user instanceof Student) {
                    Student student = (Student) user;
                    stmt.setString(6, student.getSkills());
                    stmt.setString(7, student.getEducation());
                    stmt.setString(8, student.getMajor());
                    stmt.setObject(9, student.getGpa());
                    stmt.setNull(10, Types.VARCHAR);
                    stmt.setNull(11, Types.VARCHAR);
                    stmt.setNull(12, Types.VARCHAR);
                    stmt.setNull(13, Types.VARCHAR);
                    stmt.setNull(14, Types.VARCHAR);
                } else {
                    // Employer fields
                    Employer employer = (Employer) user;
                    stmt.setNull(6, Types.VARCHAR);
                    stmt.setNull(7, Types.VARCHAR);
                    stmt.setNull(8, Types.VARCHAR);
                    stmt.setNull(9, Types.DOUBLE);
                    stmt.setString(10, employer.getCompanyName());
                    stmt.setString(11, employer.getCompanyDescription());
                    stmt.setString(12, employer.getIndustry());
                    stmt.setString(13, employer.getLocation());
                    stmt.setString(14, employer.getWebsite());
                }

                System.out.println("Executing insert for user: " + user.getUsername() + " (type: " + userType + ")");
                stmt.executeUpdate();

                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        user.setId(keys.getLong(1));
                    }
                }
            } catch (SQLException e) {
                System.err.println("SQL Error saving user: " + e.getMessage());
                System.err.println("SQL State: " + e.getSQLState());
                System.err.println("Error Code: " + e.getErrorCode());
                e.printStackTrace();
                throw new RuntimeException("Error saving user: " + e.getMessage(), e);
            }
        } else {
            // Update existing user
            String sql = """
                        UPDATE USERS SET USERNAME = ?, EMAIL = ?, AGE = ?, PASSWORD = ?,
                                         SKILLS = ?, EDUCATION = ?, MAJOR = ?, GPA = ?,
                                         COMPANY_NAME = ?, COMPANY_DESCRIPTION = ?, INDUSTRY = ?, LOCATION = ?, WEBSITE = ?
                        WHERE ID = ?
                    """;
            try (Connection conn = DatabaseConfig.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, user.getUsername());
                stmt.setString(2, user.getEmail());
                stmt.setInt(3, user.getAge());
                stmt.setString(4, user.getPassword());

                // Student fields
                if (user instanceof Student) {
                    Student student = (Student) user;
                    stmt.setString(5, student.getSkills());
                    stmt.setString(6, student.getEducation());
                    stmt.setString(7, student.getMajor());
                    stmt.setObject(8, student.getGpa());
                    stmt.setNull(9, Types.VARCHAR);
                    stmt.setNull(10, Types.VARCHAR);
                    stmt.setNull(11, Types.VARCHAR);
                    stmt.setNull(12, Types.VARCHAR);
                    stmt.setNull(13, Types.VARCHAR);
                } else {
                    // Employer fields
                    Employer employer = (Employer) user;
                    stmt.setNull(5, Types.VARCHAR);
                    stmt.setNull(6, Types.VARCHAR);
                    stmt.setNull(7, Types.VARCHAR);
                    stmt.setNull(8, Types.DOUBLE);
                    stmt.setString(9, employer.getCompanyName());
                    stmt.setString(10, employer.getCompanyDescription());
                    stmt.setString(11, employer.getIndustry());
                    stmt.setString(12, employer.getLocation());
                    stmt.setString(13, employer.getWebsite());
                }

                stmt.setLong(14, user.getId());
                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Error updating user", e);
            }
        }

        return user;
    }

    public Optional<User> findById(Long id) {
        String sql = "SELECT * FROM USERS WHERE ID = ?";
        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapRowToUser(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding user by id", e);
        }
        return Optional.empty();
    }

    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM USERS WHERE EMAIL = ?";
        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapRowToUser(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding user by email", e);
        }
        return Optional.empty();
    }

    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM USERS";

        try (Connection conn = DatabaseConfig.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                users.add(mapRowToUser(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all users", e);
        }

        return users;
    }

    public void delete(Long id) {
        String sql = "DELETE FROM USERS WHERE ID = ?";
        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting user", e);
        }
    }

    private User mapRowToUser(ResultSet rs) throws SQLException {
        String userType = rs.getString("USER_TYPE");
        User user;

        if ("STUDENT".equals(userType)) {
            Student student = new Student();
            student.setSkills(rs.getString("SKILLS"));
            student.setEducation(rs.getString("EDUCATION"));
            student.setMajor(rs.getString("MAJOR"));
            Double gpa = rs.getDouble("GPA");
            if (!rs.wasNull()) {
                student.setGpa(gpa);
            }
            user = student;
        } else {
            Employer employer = new Employer();
            employer.setCompanyName(rs.getString("COMPANY_NAME"));
            employer.setCompanyDescription(rs.getString("COMPANY_DESCRIPTION"));
            employer.setIndustry(rs.getString("INDUSTRY"));
            employer.setLocation(rs.getString("LOCATION"));
            employer.setWebsite(rs.getString("WEBSITE"));
            user = employer;
        }

        user.setId(rs.getLong("ID"));
        user.setUsername(rs.getString("USERNAME"));
        user.setEmail(rs.getString("EMAIL"));
        user.setAge(rs.getInt("AGE"));
        user.setPassword(rs.getString("PASSWORD"));

        return user;
    }
}
