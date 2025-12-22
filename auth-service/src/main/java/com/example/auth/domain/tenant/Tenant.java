package com.example.auth.domain.tenant;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tenants")
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    protected Tenant() {}

    public Tenant(String name, String status) {
        this.name = name;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    // getters only (no setter for id)
}
