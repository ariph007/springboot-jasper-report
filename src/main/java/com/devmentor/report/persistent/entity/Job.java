package com.devmentor.report.persistent.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;

import java.time.ZonedDateTime;

@Entity
@Table(name = "job", uniqueConstraints = @UniqueConstraint(columnNames = "job_name"))
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Job {
    @Id
    @Column(name = "job_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;


    @Column(name = "job_title", nullable = false)
    private String title;

    @Column(name = "min_salary", nullable = false)
    private Integer minSalary;

    @Column(name = "max_salary", nullable = false)
    private Integer maxSalary;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private ZonedDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @LastModifiedBy
    private ZonedDateTime updatedAt;


}
