package com.example.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;


import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Attendance {
	
	   
	   @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    private String empId;
	    private String empName;
	    private LocalDate date; // Initially as String
	    private LocalTime inTime; // Initially as String
	    private LocalTime outTime; // Initially as String
	    private String workingDayStatus; // Initially as String
//comment
	    //mkmkkmkm

}
