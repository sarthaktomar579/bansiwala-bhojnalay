package com.bansiwala.bhojnalay.dto;

import java.time.LocalDate;

public class DailyReportResponse {
    private LocalDate date;
    private long lunchCount;
    private long dinnerCount;
    private long totalCheckIns;
    private long lunchThalis;
    private long dinnerThalis;
    private long totalThalis;

    public DailyReportResponse() {}

    public DailyReportResponse(LocalDate date, long lunchCount, long dinnerCount,
                               long totalCheckIns, long lunchThalis, long dinnerThalis, long totalThalis) {
        this.date = date;
        this.lunchCount = lunchCount;
        this.dinnerCount = dinnerCount;
        this.totalCheckIns = totalCheckIns;
        this.lunchThalis = lunchThalis;
        this.dinnerThalis = dinnerThalis;
        this.totalThalis = totalThalis;
    }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public long getLunchCount() { return lunchCount; }
    public void setLunchCount(long lunchCount) { this.lunchCount = lunchCount; }

    public long getDinnerCount() { return dinnerCount; }
    public void setDinnerCount(long dinnerCount) { this.dinnerCount = dinnerCount; }

    public long getTotalCheckIns() { return totalCheckIns; }
    public void setTotalCheckIns(long totalCheckIns) { this.totalCheckIns = totalCheckIns; }

    public long getLunchThalis() { return lunchThalis; }
    public void setLunchThalis(long lunchThalis) { this.lunchThalis = lunchThalis; }

    public long getDinnerThalis() { return dinnerThalis; }
    public void setDinnerThalis(long dinnerThalis) { this.dinnerThalis = dinnerThalis; }

    public long getTotalThalis() { return totalThalis; }
    public void setTotalThalis(long totalThalis) { this.totalThalis = totalThalis; }
}
