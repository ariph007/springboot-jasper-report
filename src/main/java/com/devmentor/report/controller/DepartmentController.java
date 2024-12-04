package com.devmentor.report.controller;

import com.devmentor.report.model.response.File;
import com.devmentor.report.service.DepartmentService;
import com.devmentor.report.service.EmployeeService;
import lombok.AllArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;


    @PostMapping(value = "departments/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> importDepartments(@RequestParam MultipartFile file) {
        departmentService.migrate(file);
        return ResponseEntity.ok("departments template has been successfully imported");
    }

}
