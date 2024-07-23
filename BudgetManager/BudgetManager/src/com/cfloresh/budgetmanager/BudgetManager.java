package com.cfloresh.budgetmanager;

import java.util.ArrayList;

public class BudgetManager {

    private MenuType menu = MenuType.MAIN;

    private double balance;
    private double totalPurchases;
    private boolean receivePurchasePrice = false;

    private ArrayList<Purchase> expenses;
    private final ArrayList<ArrayList<Purchase>> purchaseTypes;

    private States state;

    private boolean receiveInput = false;
    private String input;

    private boolean exitState = false;

    /* Getter and Setter for - receiveInput */
    public boolean isReceiveInput() {
        return this.receiveInput;
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
        purchaseTypes = new ArrayList<>();

        purchaseTypes.add(new ArrayList<>());
        purchaseTypes.add(new ArrayList<>());
        purchaseTypes.add(new ArrayList<>());
        purchaseTypes.add(new ArrayList<>());

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
            System.out.print(menu.getBody());
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
        if (menu.equals(MenuType.MAIN)) {
            switch (userSelection) {
                case 1 -> state = States.ADD_INCOME;
                case 2 -> menu = MenuType.ADD_PURCHASE;
                case 3 -> {
                    if (purchaseTypeListIsEmpty()) {
                        System.out.println("\nThe purchase list is empty!");
                    } else {
                        menu = MenuType.SHOW_PURCHASE;
                    }
                }
                case 4 -> state = States.BALANCE;
                case 0 -> state = States.EXIT;
                default -> throw new NumberFormatException();
            }

            return;
        }

        if (userSelection <= 0 ||
                ((menu.equals(MenuType.SHOW_PURCHASE) && userSelection > 6)) ||
                ((menu.equals(MenuType.ADD_PURCHASE) && userSelection > 5)) ){
            throw new NumberFormatException();
        }

        if ((menu.equals(MenuType.SHOW_PURCHASE) && userSelection == 6) ||
                (menu.equals(MenuType.ADD_PURCHASE) && userSelection == 5)) {
            menu = MenuType.MAIN;
            state = States.SHOW_MENU;
            return;
        }

        if (userSelection <= 4) {
            expenses = purchaseTypes.get(userSelection - 1);
        }

        if (menu.equals(MenuType.ADD_PURCHASE)) {
            state = States.ADD_PURCHASE;

        } else {
            if (userSelection == 5) {
                showAllPurchases();
            } else {
                state = States.SHOW_LIST_PURCHASES;
            }
        }
    }

    public boolean purchaseTypeListIsEmpty() {
        for (ArrayList<Purchase> purchases : purchaseTypes) {
            if (!purchases.isEmpty()) {
                return false;
            }
        }

        return true;
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
                    expenses.add(new Purchase(input));
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
                    Purchase currentExpense = expenses.get(expenseIdx);
                    currentExpense.setPrice(purchaseValue);
                    currentExpense.generatePurchaseDescription();
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

            for (Purchase purchase : expenses) {
                System.out.println(purchase.getPurchaseDescription());
            }

            System.out.printf("Total sum: $%.2f\n", totalPurchases);
        }

        state = States.SHOW_MENU;
    }

    public void showAllPurchases() {
        for (ArrayList<Purchase> purchases : purchaseTypes) {
            for (Purchase purchase : purchases) {
                System.out.println(purchase.getPurchaseDescription());
            }
        }
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
