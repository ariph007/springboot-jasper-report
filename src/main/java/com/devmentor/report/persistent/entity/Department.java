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
@Table(name = "department", uniqueConstraints = @UniqueConstraint(columnNames = "department_name"))
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "department_seq_gen")
    @SequenceGenerator(name = "department_seq_gen", sequenceName = "department_department_id_seq", allocationSize = 1)
    @Column(name = "department_id", nullable = false)
    private Long id;


    @Column(name = "department_name", nullable = false)
    private String name;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private ZonedDateTime createdAt = ZonedDateTime.now();

    @Column(name = "updated_at", nullable = false)
    @LastModifiedBy
    private ZonedDateTime updatedAt = ZonedDateTime.now();


}
