package com.example.demo.repository;

import com.example.demo.entity.Course;
import com.example.demo.entity.UserRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, String>{
    List<Course> findByUser(UserRegistration user);
    Course findByName(String course);
}
