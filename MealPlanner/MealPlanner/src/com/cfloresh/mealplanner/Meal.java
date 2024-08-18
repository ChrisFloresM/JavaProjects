package com.cfloresh.mealplanner;

public class Meal {

    private final String category;
    private final String name;
    private final String[] ingredients;

    private static int mealID = 0;

    public Meal(String category, String name, String[] ingredients) {
        this.category = category;
        this.name = name;
        this.ingredients = ingredients;
        mealID++;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public String[] getIngredients() {
        return ingredients;
    }

    public static int getMealID() {
        return mealID;
    }

    public static void setMealID(int mealID) {
        Meal.mealID = mealID;
    }
}
