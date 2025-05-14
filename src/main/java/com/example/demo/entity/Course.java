package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.net.URL;

@Data
@Entity
@Table(name = "course")
public class Course {
    @Id
    private String name;
    private URL icon;
    private String color;

    @ManyToOne
    @JoinColumn(name = "masterUserId", referencedColumnName = "userId")
    private UserRegistration user;
}
