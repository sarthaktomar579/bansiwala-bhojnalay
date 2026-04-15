package com.bansiwala.bhojnalay.repository;

import com.bansiwala.bhojnalay.entity.MealRecord;
import com.bansiwala.bhojnalay.enums.MealType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface MealRecordRepository extends JpaRepository<MealRecord, Long> {

    boolean existsByStudentIdAndMealDateAndMealType(Long studentId, LocalDate mealDate, MealType mealType);

    List<MealRecord> findByMealDate(LocalDate mealDate);

    List<MealRecord> findByMealDateAndMealType(LocalDate mealDate, MealType mealType);

    List<MealRecord> findByStudentIdAndMealDateBetweenOrderByMealDateAscMealTypeAsc(
            Long studentId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT COUNT(m) FROM MealRecord m WHERE m.mealDate = :date AND m.mealType = :mealType")
    long countByDateAndMealType(@Param("date") LocalDate date, @Param("mealType") MealType mealType);

    @Query("SELECT COALESCE(SUM(m.thaliCount), 0) FROM MealRecord m WHERE m.mealDate = :date AND m.mealType = :mealType")
    long sumThalisByDateAndMealType(@Param("date") LocalDate date, @Param("mealType") MealType mealType);

    @Query("SELECT m FROM MealRecord m JOIN FETCH m.student WHERE m.mealDate = :date ORDER BY m.checkInTime DESC")
    List<MealRecord> findByMealDateWithStudent(@Param("date") LocalDate date);

    @Query("SELECT m FROM MealRecord m JOIN FETCH m.student " +
           "WHERE m.mealDate = :date AND m.mealType = :mealType ORDER BY m.checkInTime DESC")
    List<MealRecord> findByMealDateAndMealTypeWithStudent(
            @Param("date") LocalDate date, @Param("mealType") MealType mealType);
}
