package com.devmentor.report.service;

import com.devmentor.report.model.response.File;
import net.sf.jasperreports.engine.JRException;

import java.io.IOException;

public interface EmployeeService {
    File generateReportEmployeePdf(String month) throws JRException;
    File generateReportEmployeeExcel(String month) throws IOException;
}
