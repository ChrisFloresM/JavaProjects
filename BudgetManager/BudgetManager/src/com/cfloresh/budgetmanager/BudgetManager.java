package com.cfloresh.budgetmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BudgetManager {

    private MenuType menu = MenuType.MAIN;

    private double balance;
    private double totalSum;
    private boolean receivePurchasePrice = false;

    private List<Purchase> expenses;
    private final Map<String, List<Purchase>> purchaseTypes;
    private final Map<String, Double> purchaseTypesTotal;

    private String currentKey;

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
        purchaseTypes = new HashMap<>();
        purchaseTypesTotal = new HashMap<>();

        state = States.SHOW_MENU;
        balance = 0;
        totalSum = 0;
        currentKey = "Food";
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
                    if (purchaseTypes.isEmpty()) {
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

        if (menu.equals(MenuType.ADD_PURCHASE)) {
           switch (userSelection) {
               case 1, 2, 3, 4 -> selectKey(userSelection);
               case 5 -> {
                   backMainMenu();
                   return;
               }
               default -> throw new NumberFormatException();
           }
            state = States.ADD_PURCHASE;

        } else if (menu.equals(MenuType.SHOW_PURCHASE)) {
            switch (userSelection) {
                case 1, 2, 3, 4 -> selectKey(userSelection);
                case 5 ->  {
                    showAllPurchases();
                    state = States.SHOW_MENU;
                    return;
                }
                case 6 -> {
                    backMainMenu();
                    return;
                }
                default -> throw new NumberFormatException();
            }
            state = States.SHOW_LIST_PURCHASES;
        }

        expenses = purchaseTypes.computeIfAbsent(currentKey, k -> new ArrayList<>());
    }

    private void backMainMenu() {
        menu = MenuType.MAIN;
        state = States.SHOW_MENU;
    }

    private void selectKey(int input) {
        switch (input) {
            case 1 -> currentKey = "Food";
            case 2 -> currentKey = "Clothes";
            case 3 -> currentKey = "Entertainment";
            case 4 -> currentKey = "Other";
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
                    expenses.add(new Purchase(input));
                    receivePurchasePrice = true;
                }
                receiveInput = false;
            } else {
                try {
                    /* Caculate total purchases and new balance */
                    double purchaseValue = Double.parseDouble(input);
                    totalSum += purchaseValue;

                    purchaseTypesTotal.merge(currentKey, purchaseValue, Double::sum);
                    balance = balance - purchaseValue < 0 ? 0 : balance - purchaseValue;

                    /* Add expense value to the list */
                    int expenseIdx = expenses.size() - 1;
                    Purchase currentExpense = expenses.get(expenseIdx);
                    currentExpense.setPrice(purchaseValue);
                    currentExpense.generatePurchaseDescription();
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

        System.out.println(currentKey + ":");
        if(expenses.isEmpty()) {
            System.out.println("The purchase list is empty");
        } else {
            for (Purchase purchase : expenses) {
                System.out.println(purchase.getPurchaseDescription());
            }
            System.out.printf("Total sum: $%.2f\n", purchaseTypesTotal.get(currentKey));
        }

        state = States.SHOW_MENU;
    }

    public void showAllPurchases() {
        System.out.println();
        for (List<Purchase> purchases : purchaseTypes.values()) {
            for (Purchase purchase : purchases) {
                System.out.println(purchase.getPurchaseDescription());
            }
        }
        System.out.println("Total Sum: $" + totalSum);
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
