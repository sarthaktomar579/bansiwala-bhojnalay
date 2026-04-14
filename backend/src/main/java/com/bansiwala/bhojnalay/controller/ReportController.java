package com.bansiwala.bhojnalay.controller;

import com.bansiwala.bhojnalay.dto.DailyReportResponse;
import com.bansiwala.bhojnalay.dto.StudentMealHistory;
import com.bansiwala.bhojnalay.service.MealRecordService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final MealRecordService mealRecordService;

    public ReportController(MealRecordService mealRecordService) {
        this.mealRecordService = mealRecordService;
    }

    @GetMapping("/daily")
    public ResponseEntity<DailyReportResponse> getDailyReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (date == null) date = LocalDate.now();
        return ResponseEntity.ok(mealRecordService.getDailyReport(date));
    }

    @GetMapping("/student/{studentId}/monthly")
    public ResponseEntity<StudentMealHistory> getStudentMonthlyReport(
            @PathVariable Long studentId,
            @RequestParam int year,
            @RequestParam int month) {
        return ResponseEntity.ok(mealRecordService.getStudentMonthlyHistory(studentId, year, month));
    }
}
