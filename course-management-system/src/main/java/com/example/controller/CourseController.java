package com.example.controller;

import com.example.onlinelearning.model.Course; 
import com.example.service.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/courses") 
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    // CREATE a new course
    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {

        Course newCourse = courseService.addCourse(course);
        return new ResponseEntity<>(newCourse, HttpStatus.CREATED);
    }

    // READ all courses
    @GetMapping
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    // READ a single course by ID
    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable int id) {
 
        Course course = courseService.getCourseById(id);
        return ResponseEntity.ok(course);
    }

    // UPDATE a course
    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable int id, @RequestBody Course courseDetails) {

        Course updatedCourse = courseService.updateCourse(id, courseDetails);
        return ResponseEntity.ok(updatedCourse);
    }

    // DELETE a course
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable int id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleErrors(IllegalStateException e) {
        String errorMessage = e.getMessage();

        if (errorMessage.contains("already exists")) {
            return new ResponseEntity<>(errorMessage, HttpStatus.CONFLICT);
        } else if (errorMessage.contains("not found")) {
            return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }
}