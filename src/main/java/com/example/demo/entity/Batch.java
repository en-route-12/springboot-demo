package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Entity
@Table(name = "batch")
public class Batch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int batchId;
    @ManyToOne
    @JoinColumn(name = "courseName", referencedColumnName = "name")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "instructor", referencedColumnName = "userId")
    private UserRegistration instructor;
    private String batchName;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<String> days;
    private LocalTime startTime;
    private LocalTime endTime;
    private String location;
    private String locationAddress;
}
