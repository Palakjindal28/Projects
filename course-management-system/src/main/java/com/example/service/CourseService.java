package com.example.service;

import com.example.onlinelearning.model.Course;
import com.example.repository.CourseRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CourseService {
    private final CourseRepository repo;

    public CourseService(CourseRepository repo) {
        this.repo = repo;
    }

    public Course addCourse(Course course) {
        if (!repo.findByTitle(course.getTitle()).isEmpty()) {
            throw new IllegalStateException("Course with title '" + course.getTitle() + "' already exists.");
        }
        return repo.save(course);
    }

    public List<Course> getAllCourses() {
        return repo.findAll();
    }

    public Course getCourseById(int id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalStateException("Course with id " + id + " not found."));
    }

    public Course updateCourse(int id, Course updatedCourseDetails) {
        Course existingCourse = repo.findById(id)
                .orElseThrow(() -> new IllegalStateException("Course with id " + id + " not found."));

        existingCourse.setTitle(updatedCourseDetails.getTitle());
        existingCourse.setDescription(updatedCourseDetails.getDescription());
        existingCourse.setInstructor(updatedCourseDetails.getInstructor());
        
        return repo.save(existingCourse);
    }

    public void deleteCourse(int id) {
        if (!repo.existsById(id)) {
            throw new IllegalStateException("Course with id " + id + " not found.");
        }
        repo.deleteById(id);
    }
}