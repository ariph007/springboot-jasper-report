package com.devmentor.report.service;


import org.springframework.web.multipart.MultipartFile;

public interface DepartmentService {
    void migrate(MultipartFile file);
}
