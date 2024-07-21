package com.cfloresh.budgetmanager;

import java.util.ArrayList;

public class BudgetManager {

    private double balance;
    private double totalPurchases;

    private ArrayList<String> expenses;

    private States state;

    private boolean receiveInput = false;
    private String input;

    private boolean exitState = false;

    /* Getter and Setter for - receiveInput */
    public boolean isReceiveInput() {
        return this.receiveInput;
    }

    public void setReceiveInput(boolean set) {
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
        balance = 0;
        totalPurchases = 0;
    }

    /* State machine function */
    public void stateMachine() {
        state.action(this);
    }

    public void showMenu() {
        if (!receiveInput) {
            String menu = """
               
                Choose your action:
                1) Add income
                2) Add purchase
                3) Show list of purchases
                4) Balance
                0) Exit
                """;
            System.out.print(menu);
            receiveInput = true;
        } else {
            try {
                performUserSelection(Integer.parseInt(input));
            } catch (NumberFormatException e) {
                System.out.println("Invalid input!");
            } finally {
                receiveInput = false;
            }
        }

    }

    private void performUserSelection(int userSelection){
        switch (userSelection) {
            case 1 -> state = States.ADD_INCOME;
            case 0 -> state = States.EXIT;
            default -> throw new NumberFormatException();
        }
    }

    public void addIncome() {
        if(!receiveInput) {
            System.out.println("\nEnter income: ");
            receiveInput = true;
        } else {
            try {
                balance += Double.parseDouble(input);
                System.out.println("Income was added!");
                state = States.SHOW_MENU;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input!");
            }
            finally {
                receiveInput = false;
            }
        }
    }

    public void addPurchase() {
        if(!receiveInput) {
            System.out.println("\nEnter purchase name: ");
        } else {
            if (validatePurchaseFormat()) {
                expenses.add(input);
                state = States.SHOW_MENU;
            }
        }
    }

    private boolean validatePurchaseFormat() {
        if (input.isBlank() || !input.contains("$")) {
            System.out.println("Invalid input!");
            return false;
        }

        int priceIdx = input.lastIndexOf('$');

        try {
            totalPurchases += Double.parseDouble(input.substring(priceIdx + 1));
            return true;
        } catch (NumberFormatException ex) {
            System.out.println("Error on the value of the price!");
            return false;
        } catch (NullPointerException ex) {
            System.out.println("Null input found");
            return false;
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("Not valid value for price was found");
            return false;
        }
    }

    public void exit() {
        exitState = true;
    }


}
