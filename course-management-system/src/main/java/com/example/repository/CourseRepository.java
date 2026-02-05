package com.example.repository;

import com.example.onlinelearning.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    List<Course> findByTitle(String title);
}
