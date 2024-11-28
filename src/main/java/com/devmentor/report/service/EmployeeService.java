package com.devmentor.report.service;

import com.devmentor.report.model.response.File;
import net.sf.jasperreports.engine.JRException;

public interface EmployeeService {
    File generateReportEmployee(String month) throws JRException;
}
