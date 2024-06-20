package com.example.repo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.EmpAttendance;

@Repository
public interface EmpAttendanceRepo extends CrudRepository<com.example.entity.EmpAttendance, Long>{

	List<EmpAttendance>findByEmpId(Long empid);
	
	 List<EmpAttendance> findByEmpIdAndDateBetween(Long empId, LocalDate startDate, LocalDate endDate);

}
