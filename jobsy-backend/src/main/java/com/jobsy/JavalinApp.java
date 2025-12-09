package com.jobsy;

import com.jobsy.config.DataSeeder;
import com.jobsy.dao.*;
import com.jobsy.models.*;
import com.jobsy.services.*;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;

import java.util.Date;

/**
 * Main Javalin application for Jobsy Backend.
 */
public class JavalinApp {

        private static UserDAO userDAO;
        private static JobDAO jobDAO;
        private static ApplicationDAO applicationDAO;

        private static UserService userService;
        private static JobService jobService;
        private static ApplicationService applicationService;

        public static void main(String[] args) {

                userDAO = new UserDAO();
                jobDAO = new JobDAO();
                applicationDAO = new ApplicationDAO();

                userService = new UserService(userDAO);
                jobService = new JobService(jobDAO);
                applicationService = new ApplicationService(applicationDAO);

                DataSeeder.seedData(userService, jobService);

                Javalin app = Javalin.create(config -> {
                        config.plugins.enableCors(cors -> cors.add(it -> it.anyHost()));
                        config.plugins.enableDevLogging();
                        config.jsonMapper(new io.javalin.json.JavalinJackson(
                                        new com.fasterxml.jackson.databind.ObjectMapper()
                                                        .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule())
                                                        .configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                                                                        false)));
                }).start(8080);

                System.out.println("🚀 Jobsy Backend started on http://localhost:8080");
                System.out.println("📊 H2 Console: http://localhost:8080/h2-console");

                // POST /api/users/signup
                app.post("/api/users/signup", ctx -> {
                        try {
                                SignupRequest request = ctx.bodyAsClass(SignupRequest.class);
                                User user;

                                if ("student".equalsIgnoreCase(request.getUserType())) {
                                        Student student = new Student(request.getUsername(), request.getEmail(),
                                                        request.getAge(), request.getPassword());
                                        student.setSkills(request.getSkills());
                                        student.setEducation(request.getEducation());
                                        student.setMajor(request.getMajor());
                                        student.setGpa(request.getGpa());
                                        user = student;
                                } else {
                                        Employer employer = new Employer(request.getUsername(), request.getEmail(),
                                                        request.getAge(), request.getPassword());
                                        employer.setCompanyName(request.getCompanyName());
                                        employer.setCompanyDescription(request.getCompanyDescription());
                                        employer.setIndustry(request.getIndustry());
                                        employer.setLocation(request.getLocation());
                                        employer.setWebsite(request.getWebsite());
                                        user = employer;
                                }

                                User savedUser = userService.save(user);
                                ctx.status(HttpStatus.CREATED).json(savedUser);
                        } catch (Exception e) {
                                System.err.println("Signup error: " + e.getMessage());
                                e.printStackTrace();
                                ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                                .json(new ErrorResponse("Signup failed: " + e.getMessage()));
                        }
                });

                // POST /api/users/login
                app.post("/api/users/login", ctx -> {
                        LoginRequest request = ctx.bodyAsClass(LoginRequest.class);
                        User user = userService.login(request.getEmail(), request.getPassword());
                        if (user != null) {
                                ctx.json(user);
                        } else {
                                ctx.status(HttpStatus.UNAUTHORIZED).result("Invalid credentials");
                        }
                });

                // GET /api/users/:id
                // Authorization: allow if requester is the same user OR if requester is an employer
                // who has a job that this student applied to. The frontend should send `X-User-Id` header.
                app.get("/api/users/{id}", ctx -> {
                        Long requestedId = ctx.pathParamAsClass("id", Long.class).get();
                        String header = ctx.header("X-User-Id");
                        Long callerId = null;
                        try {
                            if (header != null) callerId = Long.valueOf(header);
                        } catch (NumberFormatException ignored) {
                        }

                        // If caller is the same user, allow
                        if (callerId != null && callerId.equals(requestedId)) {
                                userService.findById(requestedId).ifPresentOrElse(
                                                ctx::json,
                                                () -> ctx.status(HttpStatus.NOT_FOUND).result("User not found"));
                                return;
                        }

                        // If caller not provided, deny
                        if (callerId == null) {
                                ctx.status(HttpStatus.UNAUTHORIZED).result("Unauthorized");
                                return;
                        }

                        // Otherwise, check if caller is an employer who has received applications from this student
                        boolean allowed = false;
                        try {
                                java.util.List<Application> apps = applicationService.getApplicationsByStudent(requestedId);
                                for (Application a : apps) {
                                        Job j = a.getJob();
                                        if (j != null) {
                                                Long ownerId = null;
                                                if (j.getEmployer() != null && j.getEmployer().getId() != null) ownerId = j.getEmployer().getId();
                                                else if (j.getPostedBy() != null && j.getPostedBy().getId() != null) ownerId = j.getPostedBy().getId();
                                                if (ownerId != null && ownerId.equals(callerId)) {
                                                        allowed = true;
                                                        break;
                                                }
                                        }
                                }
                        } catch (Exception ignored) {
                        }

                        if (allowed) {
                                userService.findById(requestedId).ifPresentOrElse(
                                                ctx::json,
                                                () -> ctx.status(HttpStatus.NOT_FOUND).result("User not found"));
                        } else {
                                ctx.status(HttpStatus.FORBIDDEN).result("Forbidden");
                        }
                });

                // GET /jobs
                app.get("/jobs", ctx -> ctx.json(jobService.getAllJobs()));

                // GET /jobs/:id
                app.get("/jobs/{id}", ctx -> {
                        Long id = ctx.pathParamAsClass("id", Long.class).get();
                        jobService.getJobById(id).ifPresentOrElse(
                                        ctx::json,
                                        () -> ctx.status(HttpStatus.NOT_FOUND).result("Job not found"));
                });

                // POST /jobs
                app.post("/jobs", ctx -> {
                        try {
                                Job job = ctx.bodyAsClass(Job.class);
                                if (job.getDatePosted() == null)
                                        job.setDatePosted(new Date());
                                ctx.status(HttpStatus.CREATED).json(jobService.saveJob(job));
                        } catch (Exception e) {
                                System.err.println("Error creating job: " + e.getMessage());
                                e.printStackTrace();
                                ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                                .json(new ErrorResponse("Failed to create job: " + e.getMessage()));
                        }
                });

                // PUT /jobs/:id
                app.put("/jobs/{id}", ctx -> {
                        Long id = ctx.pathParamAsClass("id", Long.class).get();
                        Job job = ctx.bodyAsClass(Job.class);
                        job.setId(id);
                        ctx.json(jobService.updateJob(job));
                });

                // DELETE /jobs/:id
                app.delete("/jobs/{id}", ctx -> {
                        Long id = ctx.pathParamAsClass("id", Long.class).get();
                        if (jobService.getJobById(id).isPresent()) {
                                jobService.deleteJob(id);
                                ctx.status(HttpStatus.NO_CONTENT);
                        } else {
                                ctx.status(HttpStatus.NOT_FOUND).result("Job not found");
                        }
                });

                // GET /jobs/search
                app.get("/jobs/search", ctx -> {
                        String keyword = ctx.queryParam("keyword");
                        ctx.json(keyword != null && !keyword.isEmpty()
                                        ? jobService.searchJobsByTitle(keyword)
                                        : jobService.getAllJobs());
                });

                // GET /jobs/employer/:employerId
                app.get("/jobs/employer/{employerId}", ctx -> {
                        Long employerId = ctx.pathParamAsClass("employerId", Long.class).get();
                        ctx.json(jobService.getJobsByEmployer(employerId));
                });

                // GET /applications
                app.get("/applications", ctx -> ctx.json(applicationService.getAllApplications()));

                // GET /applications/:id
                app.get("/applications/{id}", ctx -> {
                        Long id = ctx.pathParamAsClass("id", Long.class).get();
                        applicationService.getApplicationById(id).ifPresentOrElse(
                                        ctx::json,
                                        () -> ctx.status(HttpStatus.NOT_FOUND).result("Application not found"));
                });

                // POST /applications
                app.post("/applications", ctx -> {
                        Application application = ctx.bodyAsClass(Application.class);
                        if (application.getDateApplied() == null)
                                application.setDateApplied(new Date());
                        try {
                                ctx.status(HttpStatus.CREATED).json(applicationService.submitApplication(application));
                        } catch (RuntimeException e) {
                                ctx.status(HttpStatus.BAD_REQUEST).result(e.getMessage());
                        }
                });

                // GET /applications/student/:studentId
                app.get("/applications/student/{studentId}", ctx -> {
                        Long studentId = ctx.pathParamAsClass("studentId", Long.class).get();
                        ctx.json(applicationService.getApplicationsByStudent(studentId));
                });

                // GET /applications/job/:jobId
                // Only the employer who posted the job may fetch applicants. Frontend must send `X-User-Id`.
                app.get("/applications/job/{jobId}", ctx -> {
                        Long jobId = ctx.pathParamAsClass("jobId", Long.class).get();
                        String header = ctx.header("X-User-Id");
                        Long callerId = null;
                        try {
                                if (header != null) callerId = Long.valueOf(header);
                        } catch (NumberFormatException ignored) {
                        }

                        if (callerId == null) {
                                ctx.status(HttpStatus.UNAUTHORIZED).result("Unauthorized");
                                return;
                        }

                        java.util.Optional<Job> jobOpt = jobService.getJobById(jobId);
                        if (!jobOpt.isPresent()) {
                                ctx.status(HttpStatus.NOT_FOUND).result("Job not found");
                                return;
                        }
                        Job job = jobOpt.get();
                        Long ownerId = null;
                        if (job.getEmployer() != null && job.getEmployer().getId() != null) ownerId = job.getEmployer().getId();
                        else if (job.getPostedBy() != null && job.getPostedBy().getId() != null) ownerId = job.getPostedBy().getId();

                        if (ownerId == null || !ownerId.equals(callerId)) {
                                ctx.status(HttpStatus.FORBIDDEN).result("Forbidden");
                                return;
                        }

                        ctx.json(applicationService.getApplicationsByJob(jobId));
                });

                // PUT /applications/:id/accept
                app.put("/applications/{id}/accept", ctx -> {
                        Long id = ctx.pathParamAsClass("id", Long.class).get();
                        applicationService.getApplicationById(id).ifPresentOrElse(
                                        application -> ctx.json(applicationService.acceptApplication(application)),
                                        () -> ctx.status(HttpStatus.NOT_FOUND).result("Application not found"));
                });

                // PUT /applications/:id/reject
                app.put("/applications/{id}/reject", ctx -> {
                        Long id = ctx.pathParamAsClass("id", Long.class).get();
                        applicationService.getApplicationById(id).ifPresentOrElse(
                                        application -> ctx.json(applicationService.rejectApplication(application)),
                                        () -> ctx.status(HttpStatus.NOT_FOUND).result("Application not found"));
                });

                // DELETE /applications/:id
                app.delete("/applications/{id}", ctx -> {
                        Long id = ctx.pathParamAsClass("id", Long.class).get();
                        if (applicationService.getApplicationById(id).isPresent()) {
                                applicationService.deleteApplication(id);
                                ctx.status(HttpStatus.NO_CONTENT);
                        } else {
                                ctx.status(HttpStatus.NOT_FOUND).result("Application not found");
                        }
                });

                // Health check
                app.get("/health", ctx -> ctx.result("OK"));

                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                        System.out.println("Shutting down...");
                        app.stop();

                }));
        }
}
