package com.example.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import org.hibernate.annotations.ManyToAny;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmpAttendance {
	
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    private String empName;
	    private Long empId;
	    private LocalDate date;
	    private String inTime;
	    private String outTime;
	    private String effectiveHours;
	    private String grossHours;
	    private String status;
	    private String workingDayStatus;
	  
	   
	   

}
