package com.devmentor.report.service.impl;

import com.devmentor.report.persistent.entity.Department;
import com.devmentor.report.persistent.repository.DepartmentRepository;
import com.devmentor.report.service.DepartmentService;
import lombok.AllArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

@AllArgsConstructor
@Service
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;

    @Override
    public void migrate(MultipartFile file) {
        InputStream excelFile;
        try {
            excelFile = file.getInputStream();
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "corrupt or error in file");
        }

        // Implement the migration logic here
        Workbook wb = null;
        try {
            wb = new XSSFWorkbook(excelFile);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "corrupt or error in file");
        }

        Sheet departmentSheet = wb.getSheet("Template Import Data Department");
        Iterator<Row> iterator = departmentSheet.iterator();

        int rowIndex = 0;

        while (iterator.hasNext()){
            Row currentRow = iterator.next();
            if(rowIndex > 2){
                int cellIndex = 1;
                Department department = new Department();
                while (cellIndex <= 2){
                    Cell currentCell = currentRow.getCell(cellIndex);
                    if(cellIndex == 1){
                        department.setName(currentCell.getStringCellValue());
                    }
                    department.setLocation(currentCell.getStringCellValue());
                    cellIndex++;
                }
                departmentRepository.saveAndFlush(department);
            }

            rowIndex++;
        }



    }
}
