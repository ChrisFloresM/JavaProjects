package com.cfloresh.mealplanner;

import com.cfloresh.mealplanner.enumerations.States;
import static com.cfloresh.mealplanner.enumerations.States.*;

import java.util.ArrayList;
import java.util.List;

public class MealPlanner {

    /*************************************** Instance Fields ***************************************/

    private final List<Meal> meals;

    /* Input related fields */
    private boolean readInput;
    private String userInput;

    /* State related fields */
    private States state;
    private boolean exit;
    String stateMessage;
    String regex;

    /* Meal related fields*/
    private String currentMealName;
    private String[] currentMealIngredients;
    private String currentMealCategory;

    /*************************************** Constructor / Getters / Setters ***************************************/
    /* Constructor */
    public MealPlanner() {
        state = MAIN_STATE;
        meals = new ArrayList<>();
        readInput = false;
        exit = false;
    }

    /* Setter for userInput */
    public void setUserInput(String userInput) {
        this.userInput = userInput;
    }

    /* getter for readInput */
    public boolean isReadInput() {
        return readInput;
    }

    public boolean isExit() {
        return exit;
    }

    /*************************************** State machine methods ***************************************/

    /* Main state machine */
    public void stateMachine() {
        state.performAction(this);
    }

    public void mainState() {
        String regex = "(add|show|exit)";

        genericStateAction(state.getStateMessage(), regex, MAIN_STATE);
    }

    public void addState() {
        String regex = "(breakfast|lunch|dinner)";

        genericStateAction(state.getStateMessage(), regex, GET_MEAL_NAME);
    }

    public void getMealName() {
        String regex = "^[a-zA-Z]{3,}[^\\d]*";

        genericStateAction(state.getStateMessage(), regex, GET_MEAL_INGREDIENTS);
    }

    public void getMealIngredients() {
        String regex = "^[a-zA-Z]{3,}[^\\d]*(,\\s[a-zA-Z]{3,}[^\\d]*)*$";

        genericStateAction(state.getStateMessage(), regex, MAIN_STATE);
    }

    public void showState() {
        printMeals();
        backMainMenu();
    }

    public void exitState() {
        System.out.println("Bye!");
        exit = true;
    }

    /*************************************** Util methods ***************************************/
    private void genericStateAction(String stateMessage, String regex, States nextState) {
        if (!readInput) {
            System.out.println(stateMessage);
            readInput = true;
            return;
        }

        if (validateInput(regex)) {

            switch (state) {
                case MAIN_STATE -> nextState = States.valueOf(userInput.toUpperCase());
                case ADD -> currentMealCategory = userInput;
                case GET_MEAL_NAME -> currentMealName = userInput;
                case GET_MEAL_INGREDIENTS -> {
                    currentMealIngredients = userInput.split(",\\s");
                    createNewMeal();
                }
                default -> System.out.println("Invalid State!!");
            }

            state = nextState;
        }
        readInput = false;
    }

    private boolean validateInput(String regex) {
        if (userInput.matches(regex)) {
            state.setDefaultStateMessage();
            return true;
        }

        state.setErrorStateMessage();
        return false;
    }

    private void createNewMeal() {
        meals.add(new Meal(currentMealCategory, currentMealName, currentMealIngredients));
        System.out.println("The meal has been addeed!");
    }

    public void printMeals() {
        if (meals.isEmpty()) {
            System.out.println("No meals saved. Add a meal first.");
            backMainMenu();
            return;
        }

        for (Meal meal : meals) {
            printMeal(meal);
        }

        System.out.println();
    }

    public void printMeal(Meal meal) {
        String[] ingredients = meal.getIngredients();
        StringBuilder buildIngredientsString = new StringBuilder();

        for (String ingredient : ingredients) {
            buildIngredientsString.append(ingredient).append("\n");
        }

        String ingredientsString = buildIngredientsString.toString();

        String output = String.format(
                "\nCategory: %s\n" +
                        "Name: %s\n" +
                        "Ingredients:\n%s", meal.getCategory(), meal.getName(), ingredientsString);


        System.out.print(output);
    }

    public void backMainMenu() {
        state = MAIN_STATE;
        readInput = false;
    }

}
