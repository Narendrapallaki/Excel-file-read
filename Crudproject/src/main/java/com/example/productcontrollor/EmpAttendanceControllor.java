package com.example.productcontrollor;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.entity.EmpAttendance;
import com.example.entity.EmpAttendanceDTO;
import com.example.productservice.EmpAttendanceService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class EmpAttendanceControllor {

	@Autowired
	private EmpAttendanceService empAttendanceService;

	@PostMapping("/upload")
	public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {

		
		log.info("nari{}",file.getOriginalFilename());
		try {
//			List<EmpAttendance> saveAttendanceData = empAttendanceService.saveAttendanceData(file);
			String saveAttendanceData = empAttendanceService.save(file);

			if (saveAttendanceData != null && !saveAttendanceData.isEmpty()) {
				return new ResponseEntity<>(saveAttendanceData, HttpStatus.OK);
			} else {
				return new ResponseEntity<>("No attendance data processed", HttpStatus.NO_CONTENT);
			}
		} catch (IOException e) {
			return new ResponseEntity<>("Error processing the file: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	@PostMapping("/excel")
	public ResponseEntity<?> uploadFile1(@RequestParam("file") MultipartFile file) throws IOException {

		
		log.info("nari     {}",file.getOriginalFilename());
		try {
//			List<EmpAttendance> saveAttendanceData = empAttendanceService.saveAttendanceData(file);
			String saveAttendanceData = empAttendanceService.saveData(file);

			if (saveAttendanceData != null && !saveAttendanceData.isEmpty()) {
				return new ResponseEntity<>(saveAttendanceData, HttpStatus.OK);
			} else {
				return new ResponseEntity<>("No attendance data processed", HttpStatus.NO_CONTENT);
			}
		} catch (IOException e) {
			return new ResponseEntity<>("Error processing the file: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@GetMapping("/getById/{empid}")
	public ResponseEntity<List<EmpAttendanceDTO>>getByEmpid(@PathVariable("empid") String empid)
	
	{
		log.info("emp id {}",empid);
	 List<EmpAttendanceDTO> byEmpId = empAttendanceService.getByEmpId(empid);
		log.info("emtity {}",byEmpId);
		if (byEmpId!=null) {
			
			return new ResponseEntity<>(byEmpId, HttpStatus.OK);
			
		} else {
              return new ResponseEntity<>(byEmpId, HttpStatus.NOT_FOUND);
		}
		
	}
	
}
