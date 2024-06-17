package com.example.excelReader;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

import com.example.entity.Attendance;
import com.example.entity.EmpAttendance;

@Configuration
public class ExRead {

    public List<Attendance> AttendanceData(MultipartFile file) throws IOException {

        List<Attendance> attendanceList = new ArrayList<>();
        try (InputStream inputStream = file.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0)
                    continue; // Skip header row

                Attendance attendance = new Attendance();

                attendance.setEmpName(getCellValueAsString(row.getCell(0)));
                attendance.setEmpId(getCellValueAsString(row.getCell(1)));
                attendance.setDate(getCellValueAsLocalDate(row.getCell(2)));
                attendance.setInTime(getCellValueAsLocalTime(row.getCell(3)));
                attendance.setOutTime(getCellValueAsLocalTime(row.getCell(4)));
                attendance.setWorkingDayStatus(getCellValueAsString(row.getCell(5)));

                attendanceList.add(attendance);
            }
        }

        return attendanceList;
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return null;
        }
        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf((int) cell.getNumericCellValue());
        } else {
            return null;
        }
    }

    private LocalDate getCellValueAsLocalDate(Cell cell) {
        if (cell == null) {
            return null;
        }
        if (cell.getCellType() == CellType.NUMERIC) {
            Date date = cell.getDateCellValue();
            return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        } else {
            return null;
        }
    }

    private LocalTime getCellValueAsLocalTime(Cell cell) {
        if (cell == null) {
            return null;
        }
        if (cell.getCellType() == CellType.NUMERIC) {
            Date date = cell.getDateCellValue();
            return date.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
        } else {
            return null;
        }
    }

   
}
