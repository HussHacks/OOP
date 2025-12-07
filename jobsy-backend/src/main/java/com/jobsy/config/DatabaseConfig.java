package com.jobsy.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Database configuration for H2 database with HikariCP connection pooling.
 */
public class DatabaseConfig {

    private static HikariDataSource dataSource;

    public static void initialize() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(
                "jdbc:sqlserver://localhost:1433;databaseName=jobsydb;encrypt=true;trustServerCertificate=true;");
        config.setUsername("sa");
        config.setPassword("Password123!");
        config.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

        // Connection pool settings
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);

        dataSource = new HikariDataSource(config);

        // Create tables if they don't exist
        createTables();
    }

    public static DataSource getDataSource() {
        if (dataSource == null) {
            initialize();
        }
        return dataSource;
    }

    public static Connection getConnection() throws SQLException {
        return getDataSource().getConnection();
    }

    private static void createTables() {
        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement()) {

            // Tables will be created if they don't exist

            // Users table (parent table for students and employers)
            stmt.execute("""
                        IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='USERS' AND xtype='U')
                        CREATE TABLE USERS (
                            ID BIGINT IDENTITY(1,1) PRIMARY KEY,
                            USER_TYPE VARCHAR(20) NOT NULL,
                            USERNAME VARCHAR(255) NOT NULL,
                            EMAIL VARCHAR(255) NOT NULL UNIQUE,
                            AGE INT,
                            PASSWORD VARCHAR(255) NOT NULL,
                            SKILLS VARCHAR(MAX),
                            EDUCATION VARCHAR(500),
                            MAJOR VARCHAR(255),
                            GPA DOUBLE PRECISION,
                            COMPANY_NAME VARCHAR(255),
                            COMPANY_DESCRIPTION VARCHAR(MAX),
                            INDUSTRY VARCHAR(255),
                            LOCATION VARCHAR(255),
                            WEBSITE VARCHAR(255),
                            CREATED_AT DATETIME2 DEFAULT GETDATE()
                        )
                    """);

            // Jobs table
            stmt.execute("""
                        IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='JOBS' AND xtype='U')
                        CREATE TABLE JOBS (
                            ID BIGINT IDENTITY(1,1) PRIMARY KEY,
                            TITLE VARCHAR(255) NOT NULL,
                            DESCRIPTION VARCHAR(MAX),
                            SALARY VARCHAR(100),
                            COMPANY_NAME VARCHAR(255),
                            JOB_TYPE VARCHAR(50),
                            IS_OPEN BIT DEFAULT 1,
                            DATE_POSTED DATETIME2 DEFAULT GETDATE(),
                            EMPLOYER_ID BIGINT,
                            FOREIGN KEY (EMPLOYER_ID) REFERENCES USERS(ID) ON DELETE CASCADE
                        )
                    """);

            // Applications table
            stmt.execute("""
                        IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='APPLICATIONS' AND xtype='U')
                        CREATE TABLE APPLICATIONS (
                            ID BIGINT IDENTITY(1,1) PRIMARY KEY,
                            STUDENT_ID BIGINT NOT NULL,
                            JOB_ID BIGINT NOT NULL,
                            STATUS VARCHAR(50) DEFAULT 'PENDING',
                            DATE_APPLIED DATETIME2 DEFAULT GETDATE(),
                            FOREIGN KEY (STUDENT_ID) REFERENCES USERS(ID) ON DELETE CASCADE,
                            FOREIGN KEY (JOB_ID) REFERENCES JOBS(ID) ON DELETE CASCADE,
                            UNIQUE(STUDENT_ID, JOB_ID)
                        )
                    """);

            System.out.println("✅ Database tables created successfully");

        } catch (SQLException e) {
            System.err.println("❌ Error creating tables: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}
