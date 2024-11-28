package com.devmentor.report.persistent.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@Entity
@Table(name = "employee", uniqueConstraints = @UniqueConstraint(columnNames = "employee_name"))
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    @Id
    @Column(name = "employee_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "hire_date", nullable = false)
    private LocalDate hireDate;

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @Column(name = "salary", nullable = false)
    private Integer salary;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private ZonedDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @LastModifiedBy
    private ZonedDateTime updatedAt;

}
