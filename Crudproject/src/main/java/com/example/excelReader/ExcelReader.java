package com.example.excelReader;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

import com.example.entity.EmpAttendance;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class ExcelReader {

    private static final LocalTime COMPANY_START_MORNING_TIME = LocalTime.of(10, 0);
    private static final LocalTime COMPANY_START_AFTERNOON = LocalTime.of(14, 0);
    private static final LocalTime COMPANY_START_NIGHT = LocalTime.of(21, 0);
    public static final String[] HEADERS = { "empName", "empId", "date", "in-time", "out-time", "status" };

    public static boolean checkExcelFormat(MultipartFile file) {
        String contentType = file.getContentType();
        return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equals(contentType);
    }

    public List<EmpAttendance> saveAttendanceData(MultipartFile file) throws IOException {

        // Define the output format (12-hour format)
        SimpleDateFormat outputFormat = new SimpleDateFormat("hh:mm:ss a");

        List<EmpAttendance> attendanceList = new ArrayList<>();
        try (InputStream inputStream = file.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            Map<String, Integer> headerMap = new HashMap<>();
            int rowNumber = 0;

            for (Row row : sheet) {
                if (rowNumber == 0) {
                    for (Cell cell : row) {
                        headerMap.put(cell.getStringCellValue().toLowerCase(), cell.getColumnIndex());
                    }
                    rowNumber++;
                    continue; // Skip header row
                }

                EmpAttendance attendance = new EmpAttendance();
                attendance.setEmpId(UUID.randomUUID().toString()); // Set unique identifier

                LocalDateTime inDateTime = null;
                LocalDateTime outDateTime = null;

                for (String header : HEADERS) {
                    Integer cellIndex = headerMap.get(header.toLowerCase());
                    if (cellIndex != null) {
                        Cell cell = row.getCell(cellIndex);
                        switch (header) {
                            case "empName":
                                attendance.setEmpName(cell.getStringCellValue());
                                break;
                            case "empId":
                                attendance.setEmpId(cell.getStringCellValue());
                                break;
                            case "date":
                                attendance.setDate(cell.getLocalDateTimeCellValue().toLocalDate());
                                break;
                            case "in-time":
                                inDateTime = cell.getLocalDateTimeCellValue();
                                attendance.setInTime(outputFormat.format(java.sql.Time.valueOf(inDateTime.toLocalTime())));
                                break;
                            case "out-time":
                                outDateTime = cell.getLocalDateTimeCellValue();
                                attendance.setOutTime(outputFormat.format(java.sql.Time.valueOf(outDateTime.toLocalTime())));
                                break;
                            case "status":
                                attendance.setWorkingDayStatus(cell.getStringCellValue());
                                break;
                            default:
                                break;
                        }
                    }
                }

                if (inDateTime != null && outDateTime != null) {
                    if (outDateTime.isBefore(inDateTime)) {
                        outDateTime = outDateTime.plusDays(1);
                    }

                    Duration effectiveDuration = Duration.between(inDateTime, outDateTime);
                    String effectiveHours = formatDuration(effectiveDuration);

                    attendance.setEffectiveHours(effectiveHours);
                    attendance.setGrossHours(effectiveHours); // Assuming gross hours are same as effective hours

                    // Determine the shift based on the in-time
                    LocalTime shiftStartTime;
                    if (inDateTime.toLocalTime().isBefore(COMPANY_START_MORNING_TIME)) {
                        shiftStartTime = COMPANY_START_NIGHT;
                    } else if (inDateTime.toLocalTime().isBefore(COMPANY_START_AFTERNOON)) {
                        shiftStartTime = COMPANY_START_MORNING_TIME;
                    } else if (inDateTime.toLocalTime().isBefore(COMPANY_START_NIGHT)) {
                        shiftStartTime = COMPANY_START_AFTERNOON;
                    } else {
                        shiftStartTime = COMPANY_START_NIGHT;
                    }

                    if (inDateTime.toLocalTime().isAfter(shiftStartTime)) {
                        Duration lateDuration = Duration.between(shiftStartTime, inDateTime.toLocalTime());
                        attendance.setStatus("Late by " + formatDuration(lateDuration));
                    } else if (inDateTime.equals(outDateTime)) {
                        attendance.setStatus(null);
                    } else {
                        attendance.setStatus("OnTime");
                    }
                } else {
                    attendance.setEffectiveHours("00:00:00");
                    attendance.setGrossHours("00:00:00");
                    attendance.setStatus("No in-time or out-time");
                }

                attendanceList.add(attendance);
            }
        }

        return attendanceList;
    }

    private String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
