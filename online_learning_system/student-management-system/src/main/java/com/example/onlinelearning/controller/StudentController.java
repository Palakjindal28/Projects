package com.example.onlinelearning.controller;

import com.example.onlinelearning.model.Student;
import com.example.onlinelearning.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // CREATE a new student
    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        Student newStudent = studentService.createStudent(student);
        return new ResponseEntity<>(newStudent, HttpStatus.CREATED);
    }

    // READ all students
    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable("id") Long id) {
        return studentService.getStudentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE an existing student
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable("id") Long id, @RequestBody Student studentDetails) {
        Student updatedStudent = studentService.updateStudent(id, studentDetails);
        return ResponseEntity.ok(updatedStudent);
    }

    // DELETE a student
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable("id") Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Centralized exception handler for StudentController.
     * Catches any IllegalStateException thrown by the methods above.
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleStudentErrors(IllegalStateException e) {
        String errorMessage = e.getMessage();

        // Check the error message to return the correct HTTP status.
        if (errorMessage.contains("already exists")) {
            // For duplicate data like an email, return 409 Conflict.
            return new ResponseEntity<>(errorMessage, HttpStatus.CONFLICT);
        } else if (errorMessage.contains("not found")) {
            // For a missing student, return 404 Not Found.
            return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
        }

        // For any other general errors, return a 400 Bad Request.
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }
}