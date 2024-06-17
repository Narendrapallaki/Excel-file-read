package com.example.productservice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.entity.Attendance;
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

	

	public List<EmpAttendanceDTO> getByEmpId(String empid) {

		List<EmpAttendanceDTO> dtoList = new ArrayList<>();
		List<EmpAttendance> findByEmpId = attendanceRepo.findByEmpId(empid);
		for (EmpAttendance empAttendance : findByEmpId) {
			EmpAttendanceDTO dto = new EmpAttendanceDTO(empAttendance);
			dtoList.add(dto);
		}

		return dtoList;

	}
	
	

}
