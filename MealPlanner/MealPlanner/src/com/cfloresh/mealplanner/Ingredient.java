package com.cfloresh.mealplanner;

public class Ingredient {

    private final String name;
    private final int mealID;
    private final int ingredientID;

    private static int currentIngredientID = 0;

    public Ingredient(String name, int meal_id) {
        this.name = name;
        this.mealID = meal_id;
        currentIngredientID++;
        this.ingredientID = currentIngredientID;
    }

    public int getIngredientID() {
        return ingredientID;
    }

    public String getName() {
        return name;
    }

    public int getMeal_id() {
        return mealID;
    }

    public static void setCurrentIngredientID(int currentIngredientID) {
        Ingredient.currentIngredientID = currentIngredientID;
    }
}
