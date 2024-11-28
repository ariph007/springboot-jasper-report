package com.devmentor.report.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeReportResponse {
    private String firstName;
    private String lastName;
    private BigDecimal salary;
    private LocalDate hireDate;
    private String departmentName;
    private String jobTitle;
}
