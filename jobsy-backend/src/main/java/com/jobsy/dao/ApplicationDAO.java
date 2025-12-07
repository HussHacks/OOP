package com.jobsy.dao;

import com.jobsy.config.DatabaseConfig;
import com.jobsy.models.Application;
import com.jobsy.models.Student;
import com.jobsy.models.Job;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for Application entities.
 */
public class ApplicationDAO {

    private final UserDAO userDAO = new UserDAO();
    private final JobDAO jobDAO = new JobDAO();

    public Application save(Application application) {
        if (application.getId() == null) {
            // Insert new application
            String sql = "INSERT INTO applications (student_id, job_id, status) VALUES (?, ?, ?)";
            try (Connection conn = DatabaseConfig.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                stmt.setLong(1, application.getStudent().getId());
                stmt.setLong(2, application.getJob().getId());
                stmt.setString(3, application.getStatus());

                stmt.executeUpdate();

                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        application.setId(keys.getLong(1));
                    }
                }
            } catch (SQLException e) {
                if (e.getMessage().contains("Unique")) {
                    throw new RuntimeException("Student has already applied to this job");
                }
                throw new RuntimeException("Error saving application", e);
            }
        } else {
            // Update existing application
            String sql = "UPDATE applications SET status = ? WHERE id = ?";
            try (Connection conn = DatabaseConfig.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, application.getStatus());
                stmt.setLong(2, application.getId());

                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Error updating application", e);
            }
        }

        return application;
    }

    public Optional<Application> findById(Long id) {
        String sql = "SELECT * FROM applications WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapRowToApplication(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding application by id", e);
        }
        return Optional.empty();
    }

    public List<Application> findAll() {
        List<Application> applications = new ArrayList<>();
        String sql = "SELECT * FROM applications ORDER BY date_applied DESC";

        try (Connection conn = DatabaseConfig.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                applications.add(mapRowToApplication(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all applications", e);
        }

        return applications;
    }

    public List<Application> findByStudentId(Long studentId) {
        List<Application> applications = new ArrayList<>();
        String sql = "SELECT * FROM applications WHERE student_id = ? ORDER BY date_applied DESC";

        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, studentId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                applications.add(mapRowToApplication(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding applications by student", e);
        }

        return applications;
    }

    public List<Application> findByJobId(Long jobId) {
        List<Application> applications = new ArrayList<>();
        String sql = "SELECT * FROM applications WHERE job_id = ? ORDER BY date_applied DESC";

        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, jobId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                applications.add(mapRowToApplication(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding applications by job", e);
        }

        return applications;
    }

    public List<Application> findByStatus(String status) {
        List<Application> applications = new ArrayList<>();
        String sql = "SELECT * FROM applications WHERE status = ? ORDER BY date_applied DESC";

        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                applications.add(mapRowToApplication(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding applications by status", e);
        }

        return applications;
    }

    public void delete(Long id) {
        String sql = "DELETE FROM applications WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting application", e);
        }
    }

    private Application mapRowToApplication(ResultSet rs) throws SQLException {
        Application application = new Application();
        application.setId(rs.getLong("id"));
        application.setStatus(rs.getString("status"));
        application.setDateApplied(rs.getTimestamp("date_applied"));

        // Load student
        Long studentId = rs.getLong("student_id");
        userDAO.findById(studentId).ifPresent(user -> {
            if (user instanceof Student) {
                application.setStudent((Student) user);
            }
        });

        // Load job
        Long jobId = rs.getLong("job_id");
        jobDAO.findById(jobId).ifPresent(application::setJob);

        return application;
    }
}
