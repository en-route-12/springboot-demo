package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Data
@Entity
public class Login {
    @Id
    private String email;

    @Column(unique = true)
    private String password;
}
