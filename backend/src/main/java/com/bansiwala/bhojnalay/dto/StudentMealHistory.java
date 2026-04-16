package com.bansiwala.bhojnalay.dto;

import com.bansiwala.bhojnalay.enums.CheckInMethod;
import com.bansiwala.bhojnalay.enums.MealType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class StudentMealHistory {
    private Long studentId;
    private String studentName;
    private int year;
    private int month;
    private List<MealEntry> meals;
    private long totalLunch;
    private long totalDinner;

    public StudentMealHistory() {}

    public StudentMealHistory(Long studentId, String studentName, int year, int month,
                              List<MealEntry> meals, long totalLunch, long totalDinner) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.year = year;
        this.month = month;
        this.meals = meals;
        this.totalLunch = totalLunch;
        this.totalDinner = totalDinner;
    }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public int getMonth() { return month; }
    public void setMonth(int month) { this.month = month; }

    public List<MealEntry> getMeals() { return meals; }
    public void setMeals(List<MealEntry> meals) { this.meals = meals; }

    public long getTotalLunch() { return totalLunch; }
    public void setTotalLunch(long totalLunch) { this.totalLunch = totalLunch; }

    public long getTotalDinner() { return totalDinner; }
    public void setTotalDinner(long totalDinner) { this.totalDinner = totalDinner; }

    public static class MealEntry {
        private Long recordId;
        private LocalDate date;
        private MealType mealType;
        private LocalDateTime checkInTime;
        private CheckInMethod checkInMethod;
        private int thaliCount;

        public MealEntry() {}

        public MealEntry(Long recordId, LocalDate date, MealType mealType, LocalDateTime checkInTime, CheckInMethod checkInMethod, int thaliCount) {
            this.recordId = recordId;
            this.date = date;
            this.mealType = mealType;
            this.checkInTime = checkInTime;
            this.checkInMethod = checkInMethod;
            this.thaliCount = thaliCount;
        }

        public Long getRecordId() { return recordId; }
        public void setRecordId(Long recordId) { this.recordId = recordId; }

        public LocalDate getDate() { return date; }
        public void setDate(LocalDate date) { this.date = date; }

        public MealType getMealType() { return mealType; }
        public void setMealType(MealType mealType) { this.mealType = mealType; }

        public LocalDateTime getCheckInTime() { return checkInTime; }
        public void setCheckInTime(LocalDateTime checkInTime) { this.checkInTime = checkInTime; }

        public CheckInMethod getCheckInMethod() { return checkInMethod; }
        public void setCheckInMethod(CheckInMethod checkInMethod) { this.checkInMethod = checkInMethod; }

        public int getThaliCount() { return thaliCount; }
        public void setThaliCount(int thaliCount) { this.thaliCount = thaliCount; }
    }
}
