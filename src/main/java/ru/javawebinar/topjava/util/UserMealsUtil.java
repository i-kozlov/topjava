package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

/**
 * GKislin
 * 31.05.2015.
 */
public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );
        List<UserMealWithExceed> result = getFilteredMealsWithExceeded(mealList, LocalTime.of(11, 0), LocalTime.of(23, 0), 2000);
//        .toLocalDate();
//        .toLocalTime();
        result.stream().forEach(System.out::println);
    }

    public static List<UserMealWithExceed> getFilteredMealsWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Set<LocalDate> uniqueDates = new HashSet<>();
        mealList.stream().forEach(meal -> uniqueDates.add(meal.getDateTime().toLocalDate()));

        List<UserMealWithExceed> result = new ArrayList<>();

        for (LocalDate ld : uniqueDates) {

            IntSummaryStatistics incomeStats = mealList.stream()
                    .filter(meal -> meal.getDateTime().toLocalDate().equals(ld))
                    .collect(Collectors.summarizingInt(UserMeal::getCalories));
            boolean exceedLimit = incomeStats.getSum() > caloriesPerDay;
            mealList.stream()
                    .filter(meal -> meal.getDateTime().toLocalDate().equals(ld))
                    .filter(meal -> TimeUtil.isBetween(meal.getDateTime().toLocalTime(), startTime, endTime))
                    .forEach(meal -> {
                        result.add(get(meal, exceedLimit));
                    });
        }


        return result;
    }

    private static UserMealWithExceed get(UserMeal meal, boolean exceed) {
        return new UserMealWithExceed(meal.getDateTime(), meal.getDescription(), meal.getCalories(), exceed);
    }
}
