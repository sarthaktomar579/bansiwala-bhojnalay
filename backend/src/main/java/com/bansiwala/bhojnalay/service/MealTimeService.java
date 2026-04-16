package com.bansiwala.bhojnalay.service;

import com.bansiwala.bhojnalay.enums.MealType;
import com.bansiwala.bhojnalay.exception.MealTimeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
public class MealTimeService {

    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

    @Value("${meal.time.lunch.start}")
    private String lunchStart;

    @Value("${meal.time.lunch.end}")
    private String lunchEnd;

    @Value("${meal.time.dinner.start}")
    private String dinnerStart;

    @Value("${meal.time.dinner.end}")
    private String dinnerEnd;

    public MealType detectCurrentMeal() {
        LocalTime now = LocalTime.now();

        if (isWithin(now, lunchStart, lunchEnd)) return MealType.LUNCH;
        if (isWithin(now, dinnerStart, dinnerEnd)) return MealType.DINNER;

        throw new MealTimeException(
            String.format("No meal is being served right now (%s). Lunch: %s-%s, Dinner: %s-%s",
                now.format(TIME_FMT),
                lunchStart, lunchEnd,
                dinnerStart, dinnerEnd));
    }

    public boolean isMealTimeActive() {
        try {
            detectCurrentMeal();
            return true;
        } catch (MealTimeException e) {
            return false;
        }
    }

    private boolean isWithin(LocalTime now, String start, String end) {
        LocalTime s = LocalTime.parse(start, TIME_FMT);
        LocalTime e = LocalTime.parse(end, TIME_FMT);
        return !now.isBefore(s) && !now.isAfter(e);
    }
}
