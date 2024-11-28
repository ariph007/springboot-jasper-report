package com.devmentor.report.service.impl;

import com.devmentor.report.model.response.EmployeeReportResponse;
import com.devmentor.report.model.response.File;
import com.devmentor.report.persistent.entity.Employee;
import com.devmentor.report.persistent.repository.EmployeeRepository;
import com.devmentor.report.service.EmployeeService;
import lombok.AllArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;


@AllArgsConstructor
@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;

    @Override
    public File generateReportEmployee(String month) throws JRException {
        File file = new File();
        file.setFileName("EmployeeReport " + month);
        file.setFileExt("pdf");

        List<EmployeeReportResponse> res = employeeRepository.findAll().stream().map(this::mappingToResponse).toList();
        List<EmployeeReportResponse> employeeReportResponse = new ArrayList<>(res);


        JRBeanCollectionDataSource employees = new JRBeanCollectionDataSource(employeeReportResponse);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("employees", employees);
        parameters.put("monthReport", month);

        InputStream filePath = getClass().getClassLoader().getResourceAsStream("templates/report-employee.jrxml");

        JasperReport report = JasperCompileManager.compileReport(filePath);
        JasperPrint print = JasperFillManager.fillReport(report, parameters, employees);

        removeBlankPage(print.getPages());

        byte[] bytes = JasperExportManager.exportReportToPdf(print);
        file.setData(bytes);

        return file;
    }

    private void removeBlankPage(List<JRPrintPage> pages) {
        pages.removeIf(page -> page.getElements().isEmpty());
    }

    private EmployeeReportResponse mappingToResponse(Employee employee){
        EmployeeReportResponse response = new EmployeeReportResponse();
        response.setDepartmentName(employee.getDepartment().getName());
        response.setSalary(BigDecimal.valueOf(employee.getSalary()));
        response.setFirstName(employee.getFirstName());
        response.setLastName(employee.getLastName());
        response.setHireDate(employee.getHireDate());
        response.setJobTitle(employee.getJob().getTitle());
        return response;
    }
}
