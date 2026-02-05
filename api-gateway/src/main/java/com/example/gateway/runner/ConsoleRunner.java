package com.example.gateway.runner;

import com.fasterxml.jackson.core.type.TypeReference; 
import com.fasterxml.jackson.databind.ObjectMapper; 
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.util.List; 
import java.util.Map; 
import java.util.Scanner;

@Component
public class ConsoleRunner {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper; 
    private final String studentServiceUrl = "http://localhost:8081/api/students";
    private final String courseServiceUrl = "http://localhost:8082/api/courses";

    public ConsoleRunner(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper; 
    }

    @EventListener(ApplicationReadyEvent.class)
    public void start() {
        new Thread(this::runMainMenu).start();
    }

    private void runMainMenu() {
        Scanner scanner = new Scanner(System.in);
        int choice;
        do {
            System.out.println("\n===== Main Menu =====");
            System.out.println("1. Student Access");
            System.out.println("2. Course Access");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    runStudentMenu(scanner);
                    break;
                case 2:
                    runCourseMenu(scanner);
                    break;
                case 3:
                    System.out.println("Exiting console.");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } while (choice != 3);
        scanner.close();
    }
    
    private void runStudentMenu(Scanner scanner) {
        int choice;
        do {
            System.out.println("\n--- Student Management Menu ---");
            System.out.println("1. View All Students");
            System.out.println("2. Add a New Student");
            System.out.println("3. Update a Student");
            System.out.println("4. Delete a Student");
            System.out.println("5. Return to Main Menu");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (choice) {
                    case 1: 
                        System.out.println("\n--- Fetching All Students ---");
                        String studentJsonResponse = restTemplate.getForObject(studentServiceUrl, String.class);
                        List<Map<String, Object>> students = objectMapper.readValue(studentJsonResponse, new TypeReference<>() {});
                        if (!students.isEmpty()) {
                            for (Map<String, Object> student : students) {
                                System.out.println("ID: " + student.get("id") + " | Name: " + student.get("firstName") + " " + student.get("lastName") + " | Email: " + student.get("email"));
                            }
                        } else {
                            System.out.println("No students found.");
                        }
                        break;
                    case 2:
                        System.out.print("Enter First Name: "); String fname = scanner.nextLine();
                        System.out.print("Enter Last Name: "); String lname = scanner.nextLine();
                        System.out.print("Enter Email: "); String email = scanner.nextLine();
                        String studentJson = String.format("{\"firstName\":\"%s\", \"lastName\":\"%s\", \"email\":\"%s\"}", fname, lname, email);

                        HttpHeaders studentHeaders = new HttpHeaders();
                        studentHeaders.setContentType(MediaType.APPLICATION_JSON);
                        HttpEntity<String> studentEntity = new HttpEntity<>(studentJson, studentHeaders);

                        String newStudent = restTemplate.postForObject(studentServiceUrl, studentEntity, String.class);
                        System.out.println("Successfully added student: " + newStudent);
                        break;
                    case 3: 
                        System.out.print("Enter the ID of the student to update: ");
                        long studentIdToUpdate = scanner.nextLong();
                        scanner.nextLine(); 
                        System.out.print("Enter new First Name: "); String newFname = scanner.nextLine();
                        System.out.print("Enter new Last Name: "); String newLname = scanner.nextLine();
                        System.out.print("Enter new Email: "); String newEmail = scanner.nextLine();
                        String updatedStudentJson = String.format("{\"firstName\":\"%s\", \"lastName\":\"%s\", \"email\":\"%s\"}", newFname, newLname, newEmail);
                        HttpHeaders updateStudentHeaders = new HttpHeaders();
                        updateStudentHeaders.setContentType(MediaType.APPLICATION_JSON);
                        HttpEntity<String> updateStudentEntity = new HttpEntity<>(updatedStudentJson, updateStudentHeaders);
                        ResponseEntity<String> studentResponse = restTemplate.exchange(
                                studentServiceUrl + "/" + studentIdToUpdate,
                                HttpMethod.PUT,
                                updateStudentEntity,
                                String.class
                        );
                        System.out.println("Successfully updated student: " + studentResponse.getBody());
                        break;
                    case 4: 
                        System.out.print("Enter the ID of the student to delete: ");
                        long studentIdToDelete = scanner.nextLong();
                        scanner.nextLine();
                        restTemplate.delete(studentServiceUrl + "/" + studentIdToDelete);
                        System.out.println("Student with ID " + studentIdToDelete + " deleted successfully (if it existed).");
                        break;
                    case 5:
                        return;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (HttpClientErrorException e) {
                System.err.println("ERROR: Service responded with an error: " + e.getResponseBodyAsString());
            } catch (Exception e) {
                System.err.println("ERROR: Could not connect to the Student service. Is it running on port 8081?");
            }
        } while (true);
    }
    
    private void runCourseMenu(Scanner scanner) {
        int choice;
        do {
            System.out.println("\n--- Course Management Menu ---");
            System.out.println("1. View All Courses");
            System.out.println("2. Add a New Course");
            System.out.println("3. Update a Course");
            System.out.println("4. Delete a Course");
            System.out.println("5. Return to Main Menu");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (choice) {
                    case 1:
                        System.out.println("\n--- Fetching All Courses ---");
                        String courseJsonResponse = restTemplate.getForObject(courseServiceUrl, String.class);
                        List<Map<String, Object>> courses = objectMapper.readValue(courseJsonResponse, new TypeReference<>() {});
                        if (!courses.isEmpty()) {
                            for (Map<String, Object> course : courses) {
                                System.out.println("ID: " + course.get("id") + " | Title: " + course.get("title") + " | Instructor: " + course.get("instructor"));
                            }
                        } else {
                            System.out.println("No courses found.");
                        }
                        break;
                    case 2: 
                        System.out.print("Enter Title: "); String title = scanner.nextLine();
                        System.out.print("Enter Description: "); String desc = scanner.nextLine();
                        System.out.print("Enter Instructor: "); String inst = scanner.nextLine();
                        String courseJson = String.format("{\"title\":\"%s\", \"description\":\"%s\", \"instructor\":\"%s\"}", title, desc, inst);

                        HttpHeaders courseHeaders = new HttpHeaders();
                        courseHeaders.setContentType(MediaType.APPLICATION_JSON);
                        HttpEntity<String> courseEntity = new HttpEntity<>(courseJson, courseHeaders);

                        String newCourse = restTemplate.postForObject(courseServiceUrl, courseEntity, String.class);
                        System.out.println("Successfully added course: " + newCourse);
                        break;
                    case 3: 
                        System.out.print("Enter the ID of the course to update: ");
                        long courseIdToUpdate = scanner.nextLong();
                        scanner.nextLine(); 
                        System.out.print("Enter new Title: "); String newTitle = scanner.nextLine();
                        System.out.print("Enter new Description: "); String newDesc = scanner.nextLine();
                        System.out.print("Enter new Instructor: "); String newInst = scanner.nextLine();
                        String updatedCourseJson = String.format("{\"title\":\"%s\", \"description\":\"%s\", \"instructor\":\"%s\"}", newTitle, newDesc, newInst);

                        HttpHeaders updateCourseHeaders = new HttpHeaders();
                        updateCourseHeaders.setContentType(MediaType.APPLICATION_JSON);
                        HttpEntity<String> updateCourseEntity = new HttpEntity<>(updatedCourseJson, updateCourseHeaders);

                        ResponseEntity<String> courseResponse = restTemplate.exchange(
                                courseServiceUrl + "/" + courseIdToUpdate,
                                HttpMethod.PUT,
                                updateCourseEntity,
                                String.class
                        );
                        System.out.println("Successfully updated course: " + courseResponse.getBody());
                        break;
                    case 4: 
                        System.out.print("Enter the ID of the course to delete: ");
                        long idToDelete = scanner.nextLong();
                        scanner.nextLine();

                        restTemplate.delete(courseServiceUrl + "/" + idToDelete);
                        System.out.println("Course with ID " + idToDelete + " deleted successfully (if it existed).");
                        break;
                    case 5:
                        return;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (HttpClientErrorException e) {
                System.err.println("ERROR: Service responded with an error: " + e.getResponseBodyAsString());
            } catch (Exception e) {
                System.err.println("ERROR: Could not connect to the Course service. Is it running on port 8082?");
            }
        } while (true);
    }
}