package com.example.productservice;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.entity.Attendance;
import com.example.entity.AvgDto;
import com.example.entity.EmpAttendance;
import com.example.entity.EmpAttendanceDTO;
import com.example.excelReader.ExRead;
import com.example.excelReader.ExcelReader;
import com.example.repo.AtteRepo;
import com.example.repo.EmpAttendanceRepo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmpAttendanceService {

	@Autowired
	private EmpAttendanceRepo attendanceRepo;

	@Autowired
	private ExcelReader excelReader;
	@Autowired
	private ExRead exRead;
	@Autowired
	private AtteRepo att;

	public String save(MultipartFile file) throws IOException {
		if (!ExcelReader.checkExcelFormat(file)) {
			return "Invalid file format. Please upload an Excel file.";
		}

		List<EmpAttendance> saveAttendanceData = excelReader.saveAttendanceData(file);

		log.info("Save data: {}", saveAttendanceData);

		Iterable<EmpAttendance> saveAll = attendanceRepo.saveAll(saveAttendanceData);

		if (saveAll != null) {
			return "User is created.....!";
		} else {
			return "User not created.........X";
		}
	}

	public String saveData(MultipartFile file) throws IOException {

		List<Attendance> saveAttendanceData = exRead.AttendanceData(file);

		log.info("Save data: {}", saveAttendanceData);

		Iterable<Attendance> saveAll = att.saveAll(saveAttendanceData);

		if (saveAll != null) {
			return " created.....!";
		} else {
			return " not created.........X";
		}
	}

	public List<EmpAttendance> getAllAttendance() {

		Iterable<EmpAttendance> findAll = attendanceRepo.findAll();

		return (List<EmpAttendance>) findAll;

	}

	public List<EmpAttendanceDTO> getByEmpId(Long empid) {

		List<EmpAttendanceDTO> dtoList = new ArrayList<>();
		List<EmpAttendance> findByEmpId = attendanceRepo.findByEmpId(empid);
		int size = findByEmpId.size();
		System.out.println(size);
		EmpAttendanceDTO dto = null;
		int count = 0;
		for (EmpAttendance empAttendance : findByEmpId) {
			dto = new EmpAttendanceDTO(empAttendance);

			String status = dto.getStatus();

			if (status.equals("OnTime")) {

				count++;
			}
			dtoList.add(dto);
		}
		// if (size > 0) {
		double percentage = ((double) count / size) * 100;
		dto.setOnTimeArrival(percentage);
		System.out.println("OnTime Arrival Percentage: " + percentage + "%");
//	    } else {
//	        System.out.println("No workdays recorded.");
//	    }
		return dtoList;

	}

	public List<AvgDto> getAvgDto(Long empid, LocalDate statDate, LocalDate endDate) {

		System.out.println(empid + "|" + statDate + "|" + endDate);
		List<AvgDto> aDto = new ArrayList<>();
//		attendanceRepo.fin
		AvgDto avgDto = new AvgDto();
		List<EmpAttendance> fnd = attendanceRepo.findByEmpIdAndDateBetween(empid, statDate, endDate);
		log.info("range data :{}", fnd);
		int count = 0;
		int size = fnd.size();
		Duration totalEffectiveDuration = Duration.ZERO;
        
		for (EmpAttendance empAttendance : fnd) {

			String effectiveHoursStr = empAttendance.getEffectiveHours();

			LocalTime effectiveHours = LocalTime.parse(effectiveHoursStr, DateTimeFormatter.ofPattern("HH:mm:ss"));

			 totalEffectiveDuration = totalEffectiveDuration.plusHours(effectiveHours.getHour())
	                    .plusMinutes(effectiveHours.getMinute());
	            
			String status = empAttendance.getStatus();
			System.out.println(status);
			if (status.equals("OnTime")) {

				count++;
			}
			
		}
		System.out.println(size + " | " + count);
		 long totalHours = totalEffectiveDuration.toHours();
	        long totalMinutes = totalEffectiveDuration.toMinutes() % 60;
	        String totalEffectiveHours = String.format("%02d:%02d", totalHours, totalMinutes);
	        
		double percentage = ((double) count / size) * 100;
		System.out.println(percentage);
		avgDto.setAvgArivalPer(percentage);
  avgDto.setAvgPerDay(totalEffectiveHours);
		aDto.add(avgDto);
		return aDto;

	}

}
