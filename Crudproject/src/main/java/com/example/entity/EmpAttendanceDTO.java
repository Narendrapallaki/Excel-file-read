package com.example.entity;
//package com.example.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.Locale;

import com.example.entity.EmpAttendance;

import lombok.Data;

@Data
public class EmpAttendanceDTO {
    private String empName;
    private String empId;
    private String date;
    private String inTime;
    private String outTime;
    private String effectiveHours;
    private String grossHours;
    private String status;
    private String workingDayStatus;
    private String dayOfWeek;

    public EmpAttendanceDTO(EmpAttendance attendance) {
        this.empName = attendance.getEmpName();
        this.empId = attendance.getEmpId();
        this.date = formatDate(attendance.getDate());
        this.inTime = attendance.getInTime();
        this.outTime = attendance.getOutTime();
        this.effectiveHours = attendance.getEffectiveHours();
        this.grossHours = attendance.getGrossHours();
        this.status = attendance.getStatus();
        this.workingDayStatus = attendance.getWorkingDayStatus();
        this.dayOfWeek = formatDayOfWeek(attendance.getDate());
    }

    private String formatDate(LocalDate date) {
        String month = date.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        int day = date.getDayOfMonth();
        String dayOfWeek = date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        return String.format("%s %d, %s", month, day, dayOfWeek);
    }

    private String formatDayOfWeek(LocalDate date) {
        return date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
    }

    // Getters and Setters for all fields

}
