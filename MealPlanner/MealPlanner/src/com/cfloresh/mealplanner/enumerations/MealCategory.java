package com.cfloresh.mealplanner.enumerations;

public enum MealCategory {
    BREAKFAST("breakfast"),
    LUNCH("lunch"),
    DINNER("dinner");

    private final String categoryName;
    static public final MealCategory[] values = values();

    MealCategory(String categoryName) {
        this.categoryName = categoryName;
    }

    public MealCategory nextCategory() {
        return values[(ordinal() + 1) % values.length];
    }

    public String getCategoryName() {
        return categoryName;
    }
}
