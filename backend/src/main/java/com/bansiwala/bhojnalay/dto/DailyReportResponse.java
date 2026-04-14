package com.bansiwala.bhojnalay.dto;

import java.time.LocalDate;

public class DailyReportResponse {
    private LocalDate date;
    private long breakfastCount;
    private long lunchCount;
    private long dinnerCount;
    private long totalCheckIns;

    public DailyReportResponse() {}

    public DailyReportResponse(LocalDate date, long breakfastCount, long lunchCount, long dinnerCount, long totalCheckIns) {
        this.date = date;
        this.breakfastCount = breakfastCount;
        this.lunchCount = lunchCount;
        this.dinnerCount = dinnerCount;
        this.totalCheckIns = totalCheckIns;
    }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public long getBreakfastCount() { return breakfastCount; }
    public void setBreakfastCount(long breakfastCount) { this.breakfastCount = breakfastCount; }

    public long getLunchCount() { return lunchCount; }
    public void setLunchCount(long lunchCount) { this.lunchCount = lunchCount; }

    public long getDinnerCount() { return dinnerCount; }
    public void setDinnerCount(long dinnerCount) { this.dinnerCount = dinnerCount; }

    public long getTotalCheckIns() { return totalCheckIns; }
    public void setTotalCheckIns(long totalCheckIns) { this.totalCheckIns = totalCheckIns; }
}
