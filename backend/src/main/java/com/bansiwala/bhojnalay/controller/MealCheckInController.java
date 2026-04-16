package com.bansiwala.bhojnalay.controller;

import com.bansiwala.bhojnalay.dto.CheckInRequest;
import com.bansiwala.bhojnalay.dto.CheckInResponse;
import com.bansiwala.bhojnalay.enums.MealType;
import com.bansiwala.bhojnalay.service.MealRecordService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/meals")
public class MealCheckInController {

    private final MealRecordService mealRecordService;

    public MealCheckInController(MealRecordService mealRecordService) {
        this.mealRecordService = mealRecordService;
    }

    @PostMapping("/check-in/qr")
    public ResponseEntity<CheckInResponse> checkInByQr(@RequestBody CheckInRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mealRecordService.checkInByQr(request));
    }

    @PostMapping("/check-in/fingerprint")
    public ResponseEntity<CheckInResponse> checkInByFingerprint(@RequestBody CheckInRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mealRecordService.checkInByFingerprint(request));
    }

    @PostMapping("/check-in/manual/{studentId}")
    public ResponseEntity<CheckInResponse> checkInManual(
            @PathVariable Long studentId,
            @RequestParam(required = false) MealType mealType,
            @RequestParam(required = false) Integer thaliCount) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mealRecordService.checkInManual(studentId, mealType, thaliCount));
    }

    @PostMapping("/check-in/scan/{studentId}")
    public ResponseEntity<CheckInResponse> checkInByScan(
            @PathVariable Long studentId,
            @RequestParam(defaultValue = "1") int thaliCount) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mealRecordService.checkInByScan(studentId, thaliCount));
    }

    @PatchMapping("/records/{recordId}/thalis")
    public ResponseEntity<Map<String, Object>> updateThaliCount(
            @PathVariable Long recordId,
            @RequestBody Map<String, Integer> body) {
        int newCount = body.getOrDefault("thaliCount", 1);
        return ResponseEntity.ok(mealRecordService.updateMealRecordThalis(recordId, newCount));
    }

    @GetMapping("/today")
    public ResponseEntity<List<CheckInResponse>> getTodayRecords(
            @RequestParam(required = false) MealType mealType) {
        return ResponseEntity.ok(mealRecordService.getTodayRecords(mealType));
    }
}
