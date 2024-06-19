package com.eco.environet.finance.model;

import lombok.Data;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Data
public class DateRange {
    private LocalDate startDate;
    private LocalDate endDate;
    public DateRange(){
        this.startDate = LocalDate.now();
        this.endDate = null;
    }
    public DateRange(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "DateRange[startDate=" + startDate +
                       ", endDate=" + endDate + "]";
    }

    public boolean isInFuture() {
        LocalDate currentDate = LocalDate.now();
        return startDate.isAfter(currentDate) && endDate.isAfter(currentDate);
    }
    public boolean isValid() {
        if (startDate == null && endDate != null) {
            return true; // dateRange filter
        }
        if (endDate == null) {
            return true; // Goal is ongoing
        } else {
            return startDate.isBefore(endDate);
        }
    }

    public long getDurationInDays() {
        LocalDate endDateToUse = endDate != null ? endDate : LocalDate.now();
        return ChronoUnit.DAYS.between(startDate, endDateToUse);
    }

    public long getWorkingDays() {
        long workingDays = 0;
        LocalDate currentDate = startDate;

        if(endDate != null){
            while (currentDate.isBefore(endDate) || currentDate.equals(endDate)) {
                if (isWorkingDay(currentDate)) {
                    workingDays++;
                }
                currentDate = currentDate.plusDays(1);
            }

            return workingDays;
        }

        return workingDays;
    }

    // TODO // Hardcoded holidays
    private static final LocalDate[] hardcodedHolidays = {
            LocalDate.of(0, 1, 1),  // New Year's Day
            LocalDate.of(0, 2, 15), // Independence Day
            LocalDate.of(0, 2, 16), // Independence Day
            LocalDate.of(0, 5, 1),  // International Work Day
            LocalDate.of(0, 5, 2),  // International Work Day
            LocalDate.of(0, 12, 25), // Catholic Christmas Day
            LocalDate.of(0, 1, 7),  // Orthodox Christmas Day
            // Add more holidays as needed
    };
    private boolean isWorkingDay(LocalDate date) {
        // Check if the day falls on a weekend (Saturday or Sunday)
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
            return false;
        }
        // Check if the day falls on a holiday
        for (LocalDate holiday : hardcodedHolidays) {
            if (holiday.equals(date)) {
                return false; // It's a holiday
            }
        }
        return true;
    }
}
