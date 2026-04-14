package com.bansiwala.bhojnalay.dto;

import com.bansiwala.bhojnalay.enums.CheckInMethod;
import com.bansiwala.bhojnalay.enums.MealType;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class CheckInResponse {
    private Long recordId;
    private Long studentId;
    private String studentName;
    private LocalDate mealDate;
    private MealType mealType;
    private LocalDateTime checkInTime;
    private CheckInMethod checkInMethod;
    private String message;

    public CheckInResponse() {}

    public CheckInResponse(Long recordId, Long studentId, String studentName, LocalDate mealDate,
                           MealType mealType, LocalDateTime checkInTime, CheckInMethod checkInMethod, String message) {
        this.recordId = recordId;
        this.studentId = studentId;
        this.studentName = studentName;
        this.mealDate = mealDate;
        this.mealType = mealType;
        this.checkInTime = checkInTime;
        this.checkInMethod = checkInMethod;
        this.message = message;
    }

    public Long getRecordId() { return recordId; }
    public void setRecordId(Long recordId) { this.recordId = recordId; }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public LocalDate getMealDate() { return mealDate; }
    public void setMealDate(LocalDate mealDate) { this.mealDate = mealDate; }

    public MealType getMealType() { return mealType; }
    public void setMealType(MealType mealType) { this.mealType = mealType; }

    public LocalDateTime getCheckInTime() { return checkInTime; }
    public void setCheckInTime(LocalDateTime checkInTime) { this.checkInTime = checkInTime; }

    public CheckInMethod getCheckInMethod() { return checkInMethod; }
    public void setCheckInMethod(CheckInMethod checkInMethod) { this.checkInMethod = checkInMethod; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
