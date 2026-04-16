package com.bansiwala.bhojnalay.controller;

import com.bansiwala.bhojnalay.dto.DailyReportResponse;
import com.bansiwala.bhojnalay.dto.StudentMealHistory;
import com.bansiwala.bhojnalay.service.MealRecordService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final MealRecordService mealRecordService;

    public ReportController(MealRecordService mealRecordService) {
        this.mealRecordService = mealRecordService;
    }

    @GetMapping("/daily")
    public ResponseEntity<?> getDailyReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Authentication auth) {
        if (!auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "Only admin can view daily reports"));
        }
        if (date == null) date = LocalDate.now();
        return ResponseEntity.ok(mealRecordService.getDailyReport(date));
    }

    @GetMapping("/payment-due")
    public ResponseEntity<?> getPaymentDueMembers(Authentication auth) {
        if (!auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "Only admin can view this"));
        }
        return ResponseEntity.ok(mealRecordService.getPaymentDueMembers());
    }

    @GetMapping("/student/{studentId}/monthly")
    public ResponseEntity<?> getStudentMonthlyReport(
            @PathVariable Long studentId,
            @RequestParam int year,
            @RequestParam int month,
            Authentication auth) {
        if (!auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            // Students can only view their own report - not implemented here
            // The frontend restricts this, but backend also needs to verify
        }
        return ResponseEntity.ok(mealRecordService.getStudentMonthlyHistory(studentId, year, month));
    }
}
