/*
package com.example.excelReader;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	private static final LocalTime COMPANY_START_TIME = LocalTime.of(10, 0);
	private static final LocalTime COMPANY_START_NIGHT_TIME = LocalTime.of(9, 0);

	public static boolean checkExcelFormat(MultipartFile file) {
		String contentType = file.getContentType();
		Map<String, Object> response = new HashMap<>();
		if ("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equals(contentType)) {
			return true;
		} else {

			return false;
		}
	}

	public List<EmpAttendance> saveAttendanceData(MultipartFile file) throws IOException {

		SimpleDateFormat outputFormat = new SimpleDateFormat("hh:mm:ss a");

		List<EmpAttendance> attendanceList = new ArrayList<>();
		try (InputStream inputStream = file.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {

			Sheet sheet = workbook.getSheetAt(0);
			for (Row row : sheet) {
				if (row.getRowNum() == 0)
					continue; // Skip header row

				EmpAttendance attendance = new EmpAttendance();

				attendance.setEmpName(row.getCell(0).getStringCellValue());
				attendance.setEmpId(row.getCell(1).getStringCellValue());
				attendance.setDate(row.getCell(2).getLocalDateTimeCellValue().toLocalDate());
				attendance.setInTime(row.getCell(3).getLocalDateTimeCellValue().toLocalTime());
				attendance.setOutTime(row.getCell(4).getLocalDateTimeCellValue().toLocalTime());

				attendance.setWorkingDayStatus(row.getCell(5).getStringCellValue());

				Duration effectiveDuration = Duration.between(attendance.getInTime(), attendance.getOutTime());

				String effectiveHours = formatDuration(effectiveDuration);

				attendance.setEffectiveHours(effectiveHours);
				attendance.setGrossHours(effectiveHours); // Assuming gross hours are same as effective hours

				if (attendance.getInTime().isAfter(COMPANY_START_TIME)) {
					Duration lateDuration = Duration.between(COMPANY_START_TIME, attendance.getInTime());
					attendance.setStatus("Late by " + formatDuration(lateDuration));
				} else {
					attendance.setStatus("OnTime");
				}

				attendanceList.add(attendance);
				// attendanceRepo.save(attendance);
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
*/

package com.example.excelReader;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
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

	private static final LocalTime COMPANY_START_TIME = LocalTime.of(10, 0);
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
							LocalTime inTime = cell.getLocalDateTimeCellValue().toLocalTime();
							attendance.setInTime(outputFormat.format(java.sql.Time.valueOf(inTime)));
							break;
						case "out-time":
							LocalTime outTime = cell.getLocalDateTimeCellValue().toLocalTime();
							attendance.setOutTime(outputFormat.format(java.sql.Time.valueOf(outTime)));
							break;
						case "status":

							attendance.setWorkingDayStatus(cell.getStringCellValue());
							break;
						default:
							break;
						}
					}
				}

				LocalTime inTime = row.getCell(headerMap.get("in-time")).getLocalDateTimeCellValue().toLocalTime();
				LocalTime outTime = row.getCell(headerMap.get("out-time")).getLocalDateTimeCellValue().toLocalTime();

				Duration effectiveDuration = Duration.between(inTime, outTime);
				String effectiveHours = formatDuration(effectiveDuration);

				attendance.setEffectiveHours(effectiveHours);
				attendance.setGrossHours(effectiveHours); // Assuming gross hours are same as effective hours

				if (inTime.isAfter(COMPANY_START_TIME)) {
					Duration lateDuration = Duration.between(COMPANY_START_TIME, inTime);
					attendance.setStatus("Late by " + formatDuration(lateDuration));
				} else {
					attendance.setStatus("OnTime");
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
