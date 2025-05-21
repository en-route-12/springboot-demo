package com.example.demo.entity;

import com.example.demo.service.MasterService;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "student")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String courseName;
    @ManyToOne
    @JoinColumn(name = "batchId", referencedColumnName = "batchId")
    private Batch batch;
    private String batchName;
    private String timeslot;
    private String profileImg;
    private String studentName;
    @ElementCollection
    @CollectionTable(name = "student_button", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "button")
    private List<String> button;
}
