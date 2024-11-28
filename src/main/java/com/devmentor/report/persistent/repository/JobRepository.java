package com.devmentor.report.persistent.repository;

import com.devmentor.report.persistent.entity.Department;
import com.devmentor.report.persistent.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    
}
