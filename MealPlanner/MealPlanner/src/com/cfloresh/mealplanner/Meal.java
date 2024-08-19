package com.cfloresh.mealplanner;

import java.util.ArrayList;
import java.util.List;

public class Meal {

    private final String category;
    private final String name;
    private final List<Ingredient> ingredients;
    private final int mealID;

    private static int currentMealID = 0;

    public Meal(String category, String name) {
        this.category = category;
        this.name = name;
        this.ingredients = new ArrayList<>();

        currentMealID++;
        this.mealID = currentMealID;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public int getMealID() {
        return mealID;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void addIngredient(String ingredient) {
        ingredients.add(new Ingredient(ingredient, mealID));
    }

    public static void setCurrentMealID(int currentMealID) {
        Meal.currentMealID = currentMealID;
    }
}
