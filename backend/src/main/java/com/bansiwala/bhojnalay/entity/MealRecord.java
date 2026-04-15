package com.bansiwala.bhojnalay.entity;

import com.bansiwala.bhojnalay.enums.CheckInMethod;
import com.bansiwala.bhojnalay.enums.MealType;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "meal_records",
       uniqueConstraints = @UniqueConstraint(
           name = "uk_student_date_meal",
           columnNames = {"student_id", "meal_date", "meal_type"}))
public class MealRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(name = "meal_date", nullable = false)
    private LocalDate mealDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "meal_type", nullable = false, length = 20)
    private MealType mealType;

    @Column(name = "check_in_time", nullable = false)
    private LocalDateTime checkInTime = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "check_in_method", nullable = false, length = 20)
    private CheckInMethod checkInMethod;

    @Column(name = "thali_count", nullable = false)
    private int thaliCount = 1;

    public MealRecord() {}

    @PrePersist
    public void prePersist() {
        if (mealDate == null) mealDate = LocalDate.now();
        if (checkInTime == null) checkInTime = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public LocalDate getMealDate() { return mealDate; }
    public void setMealDate(LocalDate mealDate) { this.mealDate = mealDate; }

    public MealType getMealType() { return mealType; }
    public void setMealType(MealType mealType) { this.mealType = mealType; }

    public LocalDateTime getCheckInTime() { return checkInTime; }
    public void setCheckInTime(LocalDateTime checkInTime) { this.checkInTime = checkInTime; }

    public CheckInMethod getCheckInMethod() { return checkInMethod; }
    public void setCheckInMethod(CheckInMethod checkInMethod) { this.checkInMethod = checkInMethod; }

    public int getThaliCount() { return thaliCount; }
    public void setThaliCount(int thaliCount) { this.thaliCount = thaliCount; }
}
