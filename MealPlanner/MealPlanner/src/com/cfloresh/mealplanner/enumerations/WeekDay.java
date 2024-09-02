package com.cfloresh.mealplanner.enumerations;

public enum WeekDay {
    MONDAY("Monday"),
    TUESDAY("Tuesday"),
    WEDNESDAY("Wednesday"),
    THURSDAY("Thursday"),
    FRIDAY("Friday"),
    SATURDAY("Saturday"),
    SUNDAY("Sunday");

    private final String dayName;

    static public final WeekDay[] values = values();

    WeekDay(String dayName) {
        this.dayName = dayName;
    }

    public WeekDay nextDay() {
        return values[(ordinal() + 1) % values.length];
    }

    public String getDayName() {
        return dayName;
    }
}
