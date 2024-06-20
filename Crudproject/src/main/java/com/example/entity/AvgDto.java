package com.example.entity;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class AvgDto {
	
	
	private double avgArivalPer;
	private String avgPerDay;

}
