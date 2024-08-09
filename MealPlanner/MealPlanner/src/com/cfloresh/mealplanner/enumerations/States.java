package com.cfloresh.mealplanner.enumerations;

import com.cfloresh.mealplanner.MealPlanner;

public enum States {
    MAIN_STATE ("What would you like to do (add, show, exit)?", "What would you like to do (add, show, exit)?") {
        public void performAction(MealPlanner mealPlanner) {
            mealPlanner.mainState();
        }
    },

    ADD ("Which meal do you want to add (breakfast, lunch, dinner)?", "Wrong meal category! Choose from: breakfast, lunch, dinner"){
        public void performAction(MealPlanner mealPlanner) {
            mealPlanner.addState();
        }
    },

    GET_MEAL_NAME ("Input the meal's name:", "Wrong format. Use letters only!") {
        public void performAction(MealPlanner mealPlanner) {
            mealPlanner.getMealName();
        }
    },

    GET_MEAL_INGREDIENTS("Input the meal's ingredients:", "Wrong format. Use letters only!") {
        public void performAction(MealPlanner mealPlanner) {
            mealPlanner.getMealIngredients();
        }
    },

    SHOW(null, null) {
        public void performAction(MealPlanner mealPlanner) {
            mealPlanner.showState();
        }
    },

    EXIT(null, null){
        public void performAction(MealPlanner mealPlanner) {
            mealPlanner.exitState();
        }
    };

    private String stateMessage;
    private final String errorMessage;
    private final String defaultMessage;

    States(String defaultStateMessage, String errorMessage) {
        this.defaultMessage = defaultStateMessage;
        this.errorMessage = errorMessage;
        this.stateMessage = defaultStateMessage;
    }

    public String getStateMessage() {
        return stateMessage;
    }

    public void setDefaultStateMessage() {
        this.stateMessage = defaultMessage;
    }

    public void setErrorStateMessage() {
        this.stateMessage = errorMessage;
    }

    abstract public void performAction(MealPlanner object);
}
