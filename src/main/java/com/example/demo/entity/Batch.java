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
    private int batchId;
    private String batchName;
    private LocalDate startDate;
    private LocalDate endDate;
    @ElementCollection
    @CollectionTable(name = "batch_days", joinColumns = @JoinColumn(name = "batch_id"))
    @Column(name = "days")
    private List<String> days;
    private LocalTime startTime;
    private LocalTime endTime;
    private String location;
    private String locationAddress;
    @ManyToOne
    @JoinColumn(name = "courseName", referencedColumnName = "name")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "instructor", referencedColumnName = "userId")
    private UserRegistration user;
}
