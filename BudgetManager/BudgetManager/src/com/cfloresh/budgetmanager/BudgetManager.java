package com.cfloresh.budgetmanager;

import java.util.ArrayList;

public class BudgetManager {

    private double balance;
    private double totalPurchases;
    private boolean receivePurchasePrice = false;

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
            case 2 -> state = States.ADD_PURCHASE;
            case 3 -> state = States.SHOW_LIST_PURCHASES;
            case 4 -> state = States.BALANCE;
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
            if (!receivePurchasePrice) {
                System.out.println("\nEnter purchase name: ");
            } else {
                System.out.println("Enter its price: ");
            }
            receiveInput = true;
        } else {
            if (!receivePurchasePrice) {
                if (input == null || input.isBlank()) {
                    System.out.println("Invalid input!");
                } else {
                    expenses.add(input);
                    receivePurchasePrice = true;
                }
                receiveInput = false;
            } else {
                try {
                    /* Caculate total purchases and new balance */
                    double purchaseValue = Double.parseDouble(input);
                    totalPurchases += purchaseValue;
                    balance = balance - purchaseValue < 0 ? 0 : balance - purchaseValue;

                    /* Add expense value to the list */
                    int expenseIdx = expenses.size() - 1;
                    String currentExpense = expenses.get(expenseIdx) + " $" + input;
                    expenses.set(expenseIdx, currentExpense);
                    System.out.println("Purchase was added!");

                    /* Go to another state */
                    state = States.SHOW_MENU;
                    receivePurchasePrice = false;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input!");
                }
                finally {
                    receiveInput = false;
                }
            }
        }
    }

    public void showPurchases() {
        System.out.println();

        if(expenses.isEmpty()) {
            System.out.println("The purchase list is empty");
        } else {
            for (String purchase : expenses) {
                System.out.println(purchase);
            }

            System.out.printf("Total sum: $%.2f\n", totalPurchases);
        }

        state = States.SHOW_MENU;
    }

    public void showBalance() {
        System.out.printf("\nBalance: $%.2f\n", balance);
        state = States.SHOW_MENU;
    }

    public void exit() {
        System.out.println("\nBye!");
        exitState = true;
    }
}
