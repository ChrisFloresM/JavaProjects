package com.cfloresh.budgetmanager;

import java.util.ArrayList;

public class BudgetManager {

    private double balance;
    private ArrayList<String> expenses;

    private States state;

    private boolean receiveInput = false;
    private String input;

    private boolean exitState = false;

    /* Getter and Setter for - receiveInput */
    public boolean isRceiveUserInpu() {
        return this.receiveInput;
    }

    public void setReceiveUserInput(boolean set) {
        this.receiveInput = set;
    }

    /* Setter for user input */
    public void setInput(String input) {
        this.input = input;
    }

    /* Getter for exit state */
    public boolean isExitState() {
        return this.exitState;
    }

    /* Class Constructor */
    public BudgetManager() {
        expenses = new ArrayList<>();
        state = States.SHOW_MENU;
    }

    /* State machine function */
    public void stateMachine() {
        state.action(this);
    }

    public void showMenu() {
        String menu = """
               
                Choose your action:
                1) Add income
                2) Add purchase
                3) Show list of purchases
                4) Balance
                0) Exit
                """;
        System.out.print(menu);
    }

    public void addIncome() {

    }

    public void exit() {
        exitState = true;
    }


}
