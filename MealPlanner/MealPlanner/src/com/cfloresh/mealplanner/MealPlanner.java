package com.cfloresh.mealplanner;

import com.cfloresh.mealplanner.enumerations.MealCategory;
import com.cfloresh.mealplanner.enumerations.States;
import static com.cfloresh.mealplanner.enumerations.States.*;
import com.cfloresh.mealplanner.database.DataBaseManager;
import com.cfloresh.mealplanner.enumerations.WeekDay;
import com.cfloresh.mealplanner.files.FileManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MealPlanner {

    /*************************************** Instance Fields ***************************************/

    /* Input related fields */
    private boolean readInput;
    private String userInput;

    /* State related fields */
    private States state;
    private boolean exit;

    /* Meal related fields*/
    private Meal currentMeal;
    private String currentMealName;
    private String[] currentMealIngredients;
    private String currentMealCategory;

    /* Regex used multiple times */
    private final String menuOptionsRegex = "(breakfast|lunch|dinner)";
    private final String mealNameRegex = "^[a-zA-Z]{3,}[^\\d]*";

    /* Variables used for planning state */
    private WeekDay currentPlanDay;
    private MealCategory currentPlanCategory;
    private boolean planCleaned;

    /* Fields used for file management */
    private String fileName;

    /*************************************** Constructor / Getters / Setters ***************************************/
    /* Constructor */
    public MealPlanner() {
        state = MAIN_STATE;
        readInput = false;
        exit = false;
        planCleaned = false;

        currentPlanDay = WeekDay.MONDAY;
        currentPlanCategory = MealCategory.BREAKFAST;

        DataBaseManager.createTables();
        DataBaseManager.getLastIdsValues();
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
        String regex = "(add|show|plan|list plan|save|exit)";

        genericStateAction(state.getStateMessage(), regex, MAIN_STATE);
    }

    public void addState() {
        genericStateAction(state.getStateMessage(), menuOptionsRegex, GET_MEAL_NAME);
    }

    public void getMealName() {

        genericStateAction(state.getStateMessage(), mealNameRegex, GET_MEAL_INGREDIENTS);
    }

    public void getMealIngredients() {
        String regex = "^[a-zA-Z]{3,}[^\\d]*(,\\s[a-zA-Z]{3,}[^\\d]*)*$";

        genericStateAction(state.getStateMessage(), regex, MAIN_STATE);
    }

    public void showState() {
        genericStateAction(state.getStateMessage(), menuOptionsRegex, MAIN_STATE);
    }

    public void planState() {

        clearPlan();

        if (!readInput) {
            if(showCategoryMeals()) {
                buildStateMessage();
            } else {
                System.out.println("No meals found for category: " + currentPlanCategory.getCategoryName());
                backMainMenu();
                return;
            }

        }

        genericStateAction(state.getStateMessage(), mealNameRegex, PLAN);
    }

    public void listPlan() {
        if (DataBaseManager.planIsNotEmpty()) {
            showWeekPlan();
        } else {
            System.out.println("Database does not contain any meal plans");
        }

        backMainMenu();
    }

    public void save() {

        if (DataBaseManager.planIsNotEmpty()) {
            genericStateAction(state.getStateMessage(), ".+", MAIN_STATE);
        } else {
            System.out.println("Unable to save. Plan your meals first.");
            backMainMenu();
        }


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

        readInput = false;
        if (validateInput(regex)) {
            switch (state) {
                case MAIN_STATE -> nextState = userInput.equals("list plan") ? LIST_PLAN : States.valueOf(userInput.toUpperCase());

                case ADD -> currentMealCategory = userInput;
                case GET_MEAL_NAME -> {
                    currentMealName = userInput;
                    createNewMeal();
                }
                case GET_MEAL_INGREDIENTS -> {
                    currentMealIngredients = userInput.split(",");
                    addMealIngredients();
                }
                case SHOW -> printMeals();
                case PLAN -> nextState = setPlanForCurrentDay();
                case SAVE -> {
                    fileName = userInput;
                    saveToFile(getListOfIngredients());
                }
                default -> System.out.println("Invalid State!!");
            }
            state = nextState;
        }
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
        currentMeal = new Meal(currentMealCategory, currentMealName);
    }

    private void addMealIngredients() {
        for (String ingredient : currentMealIngredients) {
            currentMeal.addIngredient(ingredient);
        }
        saveToDatabase();
    }

    private void saveToDatabase() {
        DataBaseManager.insertMealsData(currentMeal.getCategory(), currentMeal.getName(), currentMeal.getMealID());

        for (Ingredient ingredient: currentMeal.getIngredients()) {
            DataBaseManager.insertIngredientsData(ingredient.getName(), ingredient.getIngredientID(), ingredient.getMeal_id());
        }

        System.out.println("The meal has been added!");
    }

    public void printMeals() {
        DataBaseManager.readMealsData(userInput);
    }

    public void backMainMenu() {
        state = MAIN_STATE;
        readInput = false;
    }

    private void buildStateMessage() {
        if (state != PLAN) {
            System.out.println("Error state! You shouldn't be here!");
            backMainMenu();
            return;
        }

        String currentDay = currentPlanDay.getDayName();
        String currentCategory = currentPlanCategory.getCategoryName();

        state.setStateMessage(String.format("Choose the %s for %s from the list above:", currentCategory, currentDay));
    }

    private boolean showCategoryMeals() {
        /* Look into the database for all the meals for the current meal category and print it ordered alphabetically */
        return DataBaseManager.getMealsByCategory(currentPlanCategory.getCategoryName(), currentPlanDay.getDayName());
    }

    private States setPlanForCurrentDay() {
        States nextState = PLAN;

        if (DataBaseManager.mealExistInTable(currentPlanCategory.getCategoryName(), userInput)) {
            int mealId = DataBaseManager.getMealId(currentPlanCategory.getCategoryName(), userInput);
            DataBaseManager.insertPlanData(currentPlanDay.getDayName(), userInput, currentPlanCategory.getCategoryName(), mealId);
        } else {
            System.out.println("This meal doesn’t exist. Choose a meal from the list above.");
            readInput = true;
            return nextState;
        }

        /* Move to the next day IF the current plan category is Dinner */
        if (currentPlanCategory.equals(MealCategory.DINNER)){
            System.out.printf("Yeah! We planned the meals for %s.%n", currentPlanDay.getDayName());
            currentPlanDay = currentPlanDay.nextDay();

            if(currentPlanDay.equals(WeekDay.MONDAY)){
                showWeekPlan();
                backMainMenu();
                planCleaned = false;
                nextState = MAIN_STATE;
            }
        }

        /* Move to the next category */
        currentPlanCategory = currentPlanCategory.nextCategory();

        return nextState;
    }

    public void clearPlan() {
        if (!planCleaned) {
            DataBaseManager.clearPlan();
            planCleaned = true;
        }
    }

    private void showWeekPlan() {
        WeekDay currentDay = WeekDay.MONDAY;
        Map<String, String> dayPlan;
        boolean exitLoop = false;
        System.out.println();

        while (!exitLoop) {
            dayPlan = DataBaseManager.getDayPlan(currentDay.getDayName());

            System.out.println(currentDay.getDayName());
            for (Map.Entry<String, String> entry : dayPlan.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }

            System.out.println();
            currentDay = currentDay.nextDay();

            exitLoop = currentDay == WeekDay.MONDAY;
        }
    }

    private Map<String, Integer> getListOfIngredients() {
        List<String> IngredientList = DataBaseManager.getIngredientsFromPlan();

        Map<String, Integer> ingredientsCount = new HashMap<>();

        for (String ingredient : IngredientList) {
            if (!ingredientsCount.containsKey(ingredient)) {
                ingredientsCount.put(ingredient, 1);
            } else {
                ingredientsCount.put(ingredient, ingredientsCount.get(ingredient) + 1);
            }
        }

        return ingredientsCount;
    }

    private void saveToFile(Map<String, Integer> ingredientList) {
        FileManager fileManager = new FileManager(userInput);
        String toWrite;

        for (Map.Entry<String, Integer> entry : ingredientList.entrySet()) {
            if (entry.getValue() > 1) {
                toWrite = String.format("%s x%d", entry.getKey(), entry.getValue());
            } else {
                toWrite = entry.getKey();
            }

            fileManager.writeToFile(toWrite);
        }

        System.out.println("Saved!");
    }
}
