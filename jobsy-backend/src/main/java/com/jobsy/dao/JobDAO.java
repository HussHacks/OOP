package com.jobsy.dao;

import com.jobsy.config.DatabaseConfig;
import com.jobsy.models.Job;
import com.jobsy.models.Employer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for Job entities.
 */
public class JobDAO {

    private final UserDAO userDAO = new UserDAO();

    public Job save(Job job) {
        if (job.getId() == null) {
            // Insert new job
            String sql = """
                        INSERT INTO jobs (title, description, salary, company_name, job_type, is_open, employer_id, date_posted)
                        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                    """;
            try (Connection conn = DatabaseConfig.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                stmt.setString(1, job.getTitle());
                stmt.setString(2, job.getDescription());
                stmt.setString(3, job.getSalary());
                stmt.setString(4, job.getCompanyName());
                stmt.setString(5, job.getJobType());
                stmt.setBoolean(6, job.isOpen());

                if (job.getEmployer() != null && job.getEmployer().getId() != null) {
                    stmt.setLong(7, job.getEmployer().getId());
                } else {
                    stmt.setNull(7, Types.BIGINT);
                }

                if (job.getDatePosted() != null) {
                    stmt.setTimestamp(8, new Timestamp(job.getDatePosted().getTime()));
                } else {
                    stmt.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
                }

                stmt.executeUpdate();

                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        job.setId(keys.getLong(1));
                    }
                }
            } catch (SQLException e) {
                System.err.println("SQL Error saving job: " + e.getMessage());
                System.err.println("SQL State: " + e.getSQLState());
                System.err.println("Error Code: " + e.getErrorCode());
                e.printStackTrace();
                throw new RuntimeException("Error saving job: " + e.getMessage(), e);
            }
        } else {
            // Update existing job
            String sql = """
                        UPDATE jobs SET title = ?, description = ?, salary = ?, company_name = ?,
                        job_type = ?, is_open = ?, employer_id = ? WHERE id = ?
                    """;
            try (Connection conn = DatabaseConfig.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, job.getTitle());
                stmt.setString(2, job.getDescription());
                stmt.setString(3, job.getSalary());
                stmt.setString(4, job.getCompanyName());
                stmt.setString(5, job.getJobType());
                stmt.setBoolean(6, job.isOpen());

                if (job.getEmployer() != null && job.getEmployer().getId() != null) {
                    stmt.setLong(7, job.getEmployer().getId());
                } else {
                    stmt.setNull(7, Types.BIGINT);
                }

                stmt.setLong(8, job.getId());

                stmt.executeUpdate();
            } catch (SQLException e) {
                System.err.println("SQL Error updating job: " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Error updating job: " + e.getMessage(), e);
            }
        }

        return job;
    }

    public Optional<Job> findById(Long id) {
        String sql = "SELECT * FROM jobs WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapRowToJob(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding job by id", e);
        }
        return Optional.empty();
    }

    public List<Job> findAll() {
        List<Job> jobs = new ArrayList<>();
        String sql = "SELECT * FROM jobs ORDER BY date_posted DESC";

        try (Connection conn = DatabaseConfig.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                jobs.add(mapRowToJob(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all jobs", e);
        }

        return jobs;
    }

    public List<Job> findByEmployerId(Long employerId) {
        List<Job> jobs = new ArrayList<>();
        String sql = "SELECT * FROM jobs WHERE employer_id = ? ORDER BY date_posted DESC";

        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, employerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                jobs.add(mapRowToJob(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding jobs by employer", e);
        }

        return jobs;
    }

    public List<Job> searchByTitle(String keyword) {
        List<Job> jobs = new ArrayList<>();
        String sql = "SELECT * FROM jobs WHERE LOWER(title) LIKE ? ORDER BY date_posted DESC";

        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + keyword.toLowerCase() + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                jobs.add(mapRowToJob(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error searching jobs", e);
        }

        return jobs;
    }

    public List<Job> findOpenJobs() {
        List<Job> jobs = new ArrayList<>();
        String sql = "SELECT * FROM jobs WHERE is_open = 1 ORDER BY date_posted DESC";

        try (Connection conn = DatabaseConfig.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                jobs.add(mapRowToJob(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding open jobs", e);
        }

        return jobs;
    }

    public void delete(Long id) {
        String sql = "DELETE FROM jobs WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting job", e);
        }
    }

    private Job mapRowToJob(ResultSet rs) throws SQLException {
        Job job = new Job();
        job.setId(rs.getLong("id"));
        job.setTitle(rs.getString("title"));
        job.setDescription(rs.getString("description"));
        job.setSalary(rs.getString("salary"));
        job.setCompanyName(rs.getString("company_name"));
        job.setJobType(rs.getString("job_type"));
        job.setOpen(rs.getBoolean("is_open"));
        job.setDatePosted(rs.getTimestamp("date_posted"));

        // Load employer if exists
        Long employerId = rs.getLong("employer_id");
        if (employerId != null && employerId > 0) {
            userDAO.findById(employerId).ifPresent(user -> {
                if (user instanceof Employer) {
                    job.setEmployer((Employer) user);
                }
            });
        }

        return job;
    }
}
