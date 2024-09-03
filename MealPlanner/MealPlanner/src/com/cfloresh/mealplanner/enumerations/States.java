package com.cfloresh.mealplanner.enumerations;

import com.cfloresh.mealplanner.MealPlanner;

public enum States {
    MAIN_STATE ("What would you like to do (add, show, plan, list plan, save, exit)?", "What would you like to do (add, show, exit)?") {
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

    GET_MEAL_INGREDIENTS("Input the ingredients:", "Wrong format. Use letters only!") {
        public void performAction(MealPlanner mealPlanner) {
            mealPlanner.getMealIngredients();
        }
    },

    SHOW("Which category do you want to print (breakfast, lunch, dinner)?", "Wrong meal category! Choose from: breakfast, lunch, dinner.") {
        public void performAction(MealPlanner mealPlanner) {
            mealPlanner.showState();
        }
    },

    PLAN("Choose the breakfast for Monday from the list above:", "Wrong format. Use letters only!") {
        public void performAction(MealPlanner mealPlanner) {
            mealPlanner.planState();
        }
    },

    LIST_PLAN(null, null) {
        public void performAction(MealPlanner mealPlanner) {
            mealPlanner.listPlan();
        }
    },

    SAVE("Input a filename: ", "Invalid name for the output file.") {
        public void performAction(MealPlanner mealPlanner) {
            mealPlanner.save();
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

    public void setStateMessage(String stateMessage) {
        this.stateMessage = stateMessage;
    }

    abstract public void performAction(MealPlanner object);
}
