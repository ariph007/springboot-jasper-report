package com.devmentor.report.controller;

import com.devmentor.report.model.response.File;
import com.devmentor.report.service.EmployeeService;
import lombok.AllArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @PostMapping(value = "employees/report-pdf")
    public ResponseEntity<File> generateReportPdf(@RequestParam("month") String month) throws JRException {
        return ResponseEntity.ok(employeeService.generateReportEmployeePdf(month));
    }

    @PostMapping(value = "employees/report-excel")
    public ResponseEntity<File> generateReportExcel(@RequestParam("month") String month) throws IOException {
        return ResponseEntity.ok(employeeService.generateReportEmployeeExcel(month));
    }

}
