package com.example.demo.repository;

import com.example.demo.entity.Batch;
import com.example.demo.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BatchRepository extends JpaRepository<Batch, Integer> {
    List<Batch> findByCourse(Course course);
}
