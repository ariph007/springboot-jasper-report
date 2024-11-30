package com.devmentor.report.service.impl;

import com.devmentor.report.model.response.EmployeeReportResponse;
import com.devmentor.report.model.response.File;
import com.devmentor.report.persistent.entity.Employee;
import com.devmentor.report.persistent.repository.EmployeeRepository;
import com.devmentor.report.service.EmployeeService;
import lombok.AllArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


@AllArgsConstructor
@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;

    @Override
    public File generateReportEmployeePdf(String month) throws JRException {
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

    private EmployeeReportResponse mappingToResponse(Employee employee) {
        EmployeeReportResponse response = new EmployeeReportResponse();
        response.setDepartmentName(employee.getDepartment().getName());
        response.setSalary(BigDecimal.valueOf(employee.getSalary()));
        response.setFirstName(employee.getFirstName());
        response.setLastName(employee.getLastName());
        response.setHireDate(employee.getHireDate());
        response.setJobTitle(employee.getJob().getTitle());
        return response;
    }


    @Override
    public File generateReportEmployeeExcel(String month) throws IOException {
        File file = new File();
        final String excelName = "Employee Report " + month;
        file.setFileName(excelName);
        file.setFileExt("xls");

        //! Create workbook
        Workbook employeeReportWorkbook = new HSSFWorkbook();

        //! Create Creation Helper
        CreationHelper creationHelper = employeeReportWorkbook.getCreationHelper();

        //! Create sheet
        Sheet employeeReportSheet = employeeReportWorkbook.createSheet(excelName);

        //! Create Title
        Row rowTitle = employeeReportSheet.createRow(0);
        Cell cellTitle = rowTitle.createCell(0);
        cellTitle.setCellValue((excelName));

        //! Styling title
        CellStyle titleStyle = employeeReportWorkbook.createCellStyle();
        Font titleFont = employeeReportWorkbook.createFont();
        //! Set title to bold
        titleFont.setBold(true);
        titleStyle.setFont(titleFont);
        //! Set title horizontal alignment to right
        titleStyle.setAlignment(HorizontalAlignment.RIGHT);
        cellTitle.setCellStyle(titleStyle);

        //! Merge title
        employeeReportSheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));

        //! Create Logo
        ClientAnchor anchor = creationHelper.createClientAnchor();
        anchor.setRow1(0);
        anchor.setRow2(3);
        anchor.setCol1(0);
        anchor.setCol2(4);

        final Drawing<?> drawing = employeeReportSheet.createDrawingPatriarch();

        InputStream companyLogo = getClass().getClassLoader().getResourceAsStream("logo/samsung.png");
        assert companyLogo != null;
        final int pictureIdx = employeeReportWorkbook.addPicture(IOUtils.toByteArray(companyLogo), Workbook.PICTURE_TYPE_PNG);
        final Picture picture = drawing.createPicture(anchor, pictureIdx);
        picture.resize(0.6, 0.8);

        //! Set Column width
        employeeReportSheet.setColumnWidth(0, 6 * 256);   //? No
        employeeReportSheet.setColumnWidth(1, 15 * 256);   //? First Name
        employeeReportSheet.setColumnWidth(2, 15 * 256);   //? Last Name
        employeeReportSheet.setColumnWidth(3, 10 * 256);   //? Salary
        employeeReportSheet.setColumnWidth(4, 15 * 256);   //? Hire Date
        employeeReportSheet.setColumnWidth(5, 20 * 256);   //? Department
        employeeReportSheet.setColumnWidth(6, 20 * 256);   //? Job Title

        List<String> headers = Arrays.asList("No", "First Name", "Last Name", "Salary", "Hire Date", "Department", "Job Title");
        Row headerRow = employeeReportSheet.createRow(4);

        //! Fill Background and Foreground Color
        CellStyle headerStyle = employeeReportWorkbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.ROYAL_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());

        Font headerFont = employeeReportWorkbook.createFont();
        headerFont.setFontHeightInPoints((short) 10);
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFont(headerFont);

        //! Create header
        for (int i = 0; i < headers.size(); i++) {
            //! first looping -> Row 0 Cell O -> No
            Cell cellHeader = headerRow.createCell(i);
            cellHeader.setCellStyle(headerStyle);
            cellHeader.setCellValue(headers.get(i));
        }

        AtomicInteger index = new AtomicInteger(1);
        List<List<String>> records = employeeRepository.findAll().stream().map(response -> List.of(
                String.valueOf(index.getAndIncrement()),
                response.getFirstName(),
                response.getLastName(),
                response.getSalary().toString(),
                response.getHireDate().toString(),
                response.getDepartment().getName(),
                response.getJob().getTitle()
        )).toList();

        CellStyle recordStyle = employeeReportWorkbook.createCellStyle();
        recordStyle.setBorderBottom(BorderStyle.THIN);
        recordStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        recordStyle.setBorderTop(BorderStyle.THIN);
        recordStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        recordStyle.setBorderRight(BorderStyle.THIN);
        recordStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        recordStyle.setBorderLeft(BorderStyle.THIN);
        recordStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());


        //! Create Row records
        int rowState = 5;
        DataFormat format = employeeReportWorkbook.createDataFormat();
        for (int i = 0; i < records.size(); i++) { //! loop the records row -> vertically
            Row recordRow = employeeReportSheet.createRow(rowState);
            for (int j = 0; j < records.get(i).size(); j++) { //! loop the records cell -> horizontal
                RichTextString cellValue = creationHelper.createRichTextString(records.get(i).get(j));
                Cell recordCell = recordRow.createCell(j);

                if(j == 3){
                    recordCell.setCellValue(Integer.parseInt(records.get(i).get(j)));
                    recordStyle.setDataFormat(format.getFormat("#,##0"));
                }else{
                    recordCell.setCellValue(cellValue);
                }
                recordCell.setCellStyle(recordStyle);

            }
            rowState++;
        }

        //! Create Summary
        rowState +=2;

        Row summaryRow = employeeReportSheet.createRow(rowState);
        Cell summaryCell = summaryRow.createCell(0);
        summaryCell.setCellValue("Total Salary");

        Cell sumCell = summaryRow.createCell(2);
        String strFormula = String.format("SUM(D6:D%s)", rowState -2);
        sumCell.setCellFormula(strFormula);
        CellStyle sumStyle = employeeReportWorkbook.createCellStyle();
        sumStyle.setDataFormat(format.getFormat("#,##0"));
        sumCell.setCellStyle(sumStyle);

        employeeReportSheet.addMergedRegion(new CellRangeAddress(rowState, rowState,0, 1 ));


        //! Write workbook to output stream
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            employeeReportWorkbook.write(bos);
        } finally {
            bos.close();
        }

        byte[] bytes = bos.toByteArray();
        file.setData(bytes);
        return file;
    }
}
