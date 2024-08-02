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

    private final FileManager fileManager;

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

    /* Setter for state */
    public void setState(States state) {
        this.state = state;
    }

    /* Setter for Menu */
    public void setMenu(MenuType menu) {
        this.menu = menu;
    }

    /* Class Constructor */
    public BudgetManager() {
        expenses = new ArrayList<>();
        purchaseTypes = new HashMap<>();
        purchaseTypesTotal = new HashMap<>();
        fileManager = new FileManager();

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
                menu.performAction(this, Integer.parseInt(input));
                expenses = purchaseTypes.computeIfAbsent(currentKey, k -> new ArrayList<>());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input!");
            } finally {
                receiveInput = false;
            }
        }
    }

    public void checkPurchaseMapEmpty() {
        if (purchaseTypes.isEmpty()) {
            System.out.println("\nThe purchase list is empty!");
        } else {
            menu = MenuType.SHOW_PURCHASE;
        }
    }

    public void backMainMenu() {
        menu = MenuType.MAIN;
        state = States.SHOW_MENU;
    }

    public void selectKey(int input) {
        switch (input) {
            case 1 -> currentKey = "Food";
            case 2 -> currentKey = "Clothes";
            case 3 -> currentKey = "Entertainment";
            case 4 -> currentKey = "Other";
            default -> System.out.println("Invalid key!");
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
                    createPurchase();
                    receivePurchasePrice = true;
                }
                receiveInput = false;
            } else {
                addPurchaseOperation();
                System.out.println("Purchase was added!");
            }
        }
    }

    public void createPurchase() {
        Purchase currentPurchase = new Purchase(currentKey, input);
        expenses.add(currentPurchase);
    }

    public void addPurchaseOperation() {
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


    public void showPurchases() {
        System.out.println();

        System.out.println(currentKey + ":");
        if(expenses.isEmpty()) {
            System.out.println("The purchase list is empty");
        } else {
            for (Purchase purchase : expenses) {
                System.out.println(purchase.toString());
            }
            System.out.printf("Total sum: $%.2f\n", purchaseTypesTotal.get(currentKey));
        }

        state = States.SHOW_MENU;
    }

    public void showAllPurchases() {
        System.out.println();
        for (List<Purchase> purchases : purchaseTypes.values()) {
            for (Purchase purchase : purchases) {
                System.out.println(purchase.toString());
            }
        }
        System.out.println("Total Sum: $" + String.format("%.2f", totalSum));
    }

    public void showBalance() {
        System.out.printf("\nBalance: $%.2f\n", balance);
        state = States.SHOW_MENU;
    }

    public void savePurchases() {

        if(expenses.isEmpty()) {
            System.out.println("\nThe purchase list is empty");
            state = States.SHOW_MENU;
            return;
        }

        fileManager.createFileRoute();
        for (List<Purchase> purchases : purchaseTypes.values()) {
            for (Purchase purchase : purchases) {
                fileManager.writeToFile(purchase.getPurchaseDescription());
            }
        }
        fileManager.writeToFile("Balance#Balance#" + balance);

        System.out.println("\nPurchases were saved!");
        state = States.SHOW_MENU;
    }

    public void loadPurchases() {
        List<String> filePurchases = fileManager.getFromFile();
        int listSize = filePurchases.size();

        for (int i = 0; i < listSize; i++) {

            String[] splitPurchase = filePurchases.get(i).split("#");

            if (checkPurchaseFormatInvalid(splitPurchase)) {
                System.out.println("\nThe file is empty or corrupted!");
                state = States.SHOW_MENU;
                return;
            }

            currentKey = splitPurchase[0];

            if (i == listSize - 1) {
                if (!currentKey.equals("Balance")) {
                    System.out.println("\nThe file is corrupted!");
                    state = States.SHOW_MENU;
                    return;
                }

                balance = Double.parseDouble(splitPurchase[2]);
                System.out.println("\nPurchases were loaded!");
                state = States.SHOW_MENU;
                return;
            }

            expenses = purchaseTypes.computeIfAbsent(currentKey, k -> new ArrayList<>());

            input = splitPurchase[1];
            createPurchase();

            input = splitPurchase[2];
            addPurchaseOperation();
        }
    }

    private boolean checkPurchaseFormatInvalid(String[] inputLine) {

        String[] validKeys = {"Food", "Clothes", "Entertainment", "Other", "Balance"};
        boolean keyIsValid = false;

        if (inputLine.length != 3) {
            return true;
        }

        for(String key : validKeys) {
            if (inputLine[0].equals(key)) {
                keyIsValid = true;
                break;
            }
        }

        if(!keyIsValid) {  
            return true;
        }

        if (inputLine[1].isBlank()) {
            return true;
        }

        try {
            Double.parseDouble(inputLine[2]);
        } catch (NumberFormatException e) {
            return false;
        }

        return false;
    }

    public void exit() {
        System.out.println("\nBye!");
        exitState = true;
    }
}


