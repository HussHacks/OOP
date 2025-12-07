package com.jobsy.config;

import com.jobsy.dao.*;
import com.jobsy.models.*;
import com.jobsy.services.*;

import java.util.Date;

/**
 * Seed data for the application - creates sample users and jobs
 */
public class DataSeeder {

    public static void seedData(UserService userService, JobService jobService) {
        try {
            // Check if data already exists
            if (!userService.getAllUsers().isEmpty()) {
                System.out.println("📊 Data already seeded, skipping...");
                return;
            }

            System.out.println("🌱 Seeding sample data...");

            // Create sample employers
            Employer employer1 = new Employer("Google Inc", "hr@google.com", 0, "password123");
            employer1 = (Employer) userService.save(employer1);

            Employer employer2 = new Employer("Microsoft Corp", "jobs@microsoft.com", 0, "password123");
            employer2 = (Employer) userService.save(employer2);

            Employer employer3 = new Employer("Amazon", "careers@amazon.com", 0, "password123");
            employer3 = (Employer) userService.save(employer3);

            Employer employer4 = new Employer("Meta", "recruiting@meta.com", 0, "password123");
            employer4 = (Employer) userService.save(employer4);

            Employer employer5 = new Employer("Apple Inc", "talent@apple.com", 0, "password123");
            employer5 = (Employer) userService.save(employer5);

            // Create sample jobs
            Job job1 = new Job();
            job1.setTitle("Software Engineer");
            job1.setDescription(
                    "Join our team to build innovative products that impact billions of users worldwide. We're looking for talented engineers with strong problem-solving skills.");
            job1.setCompanyName("Google Inc");
            job1.setSalary("$120,000 - $180,000");
            job1.setJobType("Full-time");
            job1.setEmployer(employer1);
            job1.setDatePosted(new Date());
            job1.setOpen(true);
            jobService.saveJob(job1);

            Job job2 = new Job();
            job2.setTitle("Frontend Developer");
            job2.setDescription(
                    "Build beautiful and responsive user interfaces using React, TypeScript, and modern web technologies. Experience with Azure is a plus.");
            job2.setCompanyName("Microsoft Corp");
            job2.setSalary("$100,000 - $150,000");
            job2.setJobType("Full-time");
            job2.setEmployer(employer2);
            job2.setDatePosted(new Date());
            job2.setOpen(true);
            jobService.saveJob(job2);

            Job job3 = new Job();
            job3.setTitle("Data Scientist");
            job3.setDescription(
                    "Analyze large datasets and build machine learning models to improve our recommendation systems and customer experience.");
            job3.setCompanyName("Amazon");
            job3.setSalary("$130,000 - $190,000");
            job3.setJobType("Full-time");
            job3.setEmployer(employer3);
            job3.setDatePosted(new Date());
            job3.setOpen(true);
            jobService.saveJob(job3);

            Job job4 = new Job();
            job4.setTitle("Product Manager");
            job4.setDescription(
                    "Lead product strategy and work with cross-functional teams to build products that connect people around the world.");
            job4.setCompanyName("Meta");
            job4.setSalary("$140,000 - $200,000");
            job4.setJobType("Full-time");
            job4.setEmployer(employer4);
            job4.setDatePosted(new Date());
            job4.setOpen(true);
            jobService.saveJob(job4);

            Job job5 = new Job();
            job5.setTitle("iOS Developer");
            job5.setDescription(
                    "Create amazing experiences for iPhone and iPad users. Work with Swift, SwiftUI, and cutting-edge Apple technologies.");
            job5.setCompanyName("Apple Inc");
            job5.setSalary("$125,000 - $175,000");
            job5.setJobType("Full-time");
            job5.setEmployer(employer5);
            job5.setDatePosted(new Date());
            job5.setOpen(true);
            jobService.saveJob(job5);

            Job job6 = new Job();
            job6.setTitle("Backend Engineer");
            job6.setDescription(
                    "Design and build scalable backend systems that power Google's products. Experience with distributed systems required.");
            job6.setCompanyName("Google Inc");
            job6.setSalary("$115,000 - $170,000");
            job6.setJobType("Full-time");
            job6.setEmployer(employer1);
            job6.setDatePosted(new Date());
            job6.setOpen(true);
            jobService.saveJob(job6);

            Job job7 = new Job();
            job7.setTitle("UX Designer");
            job7.setDescription(
                    "Create intuitive and beautiful user experiences for Microsoft products. Portfolio required.");
            job7.setCompanyName("Microsoft Corp");
            job7.setSalary("$95,000 - $140,000");
            job7.setJobType("Full-time");
            job7.setEmployer(employer2);
            job7.setDatePosted(new Date());
            job7.setOpen(true);
            jobService.saveJob(job7);

            Job job8 = new Job();
            job8.setTitle("DevOps Engineer");
            job8.setDescription(
                    "Build and maintain AWS infrastructure. Automate deployments and ensure high availability of our services.");
            job8.setCompanyName("Amazon");
            job8.setSalary("$110,000 - $160,000");
            job8.setJobType("Full-time");
            job8.setEmployer(employer3);
            job8.setDatePosted(new Date());
            job8.setOpen(true);
            jobService.saveJob(job8);

            Job job9 = new Job();
            job9.setTitle("Machine Learning Engineer Intern");
            job9.setDescription(
                    "Summer internship working on cutting-edge AI projects. Great opportunity for students to learn and grow.");
            job9.setCompanyName("Meta");
            job9.setSalary("$8,000/month");
            job9.setJobType("Internship");
            job9.setEmployer(employer4);
            job9.setDatePosted(new Date());
            job9.setOpen(true);
            jobService.saveJob(job9);

            Job job10 = new Job();
            job10.setTitle("Security Engineer");
            job10.setDescription(
                    "Protect Apple's products and services. Work on security features that protect millions of users.");
            job10.setCompanyName("Apple Inc");
            job10.setSalary("$135,000 - $195,000");
            job10.setJobType("Full-time");
            job10.setEmployer(employer5);
            job10.setDatePosted(new Date());
            job10.setOpen(true);
            jobService.saveJob(job10);

            System.out.println("✅ Sample data seeded successfully!");
            System.out.println("   - 5 Employers created");
            System.out.println("   - 10 Jobs created");

        } catch (Exception e) {
            System.err.println("❌ Error seeding data: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
