package com.cfloresh.budgetmanager;

import com.cfloresh.budgetmanager.strategies.sort.SortingStrategy;

import java.util.*;

public class BudgetManager {

    private MenuType menu = MenuType.MAIN;

    private double balance;
    private double totalSum;
    private boolean receivePurchasePrice = false;

    private PurchaseTypeEntry expenses;
    private final List<Purchase> allPurchases;
    private final Map<String, PurchaseTypeEntry> purchasesByType;

    private String currentKey;
    private States state;

    private boolean receiveInput = false;
    private String input;

    private boolean exitState = false;

    private final FileManager fileManager;

    private SortingStrategy sortingStrategy;

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

    public PurchaseTypeEntry getExpenses() {
        return expenses;
    }

    public List<Purchase> getAllPurchases() {
        return allPurchases;
    }

    public Map<String, PurchaseTypeEntry> getPurchasesByType() {
        return purchasesByType;
    }

    public void setSortingStrategy(SortingStrategy sortingStrategy) {
        this.sortingStrategy = sortingStrategy;
    }

    /* Class Constructor */
    public BudgetManager() {
        expenses = new PurchaseTypeEntry(new ArrayList<>());
        allPurchases = new ArrayList<>();
        purchasesByType = new LinkedHashMap<>();
        fileManager = new FileManager();
        initializePurchases();

        state = States.SHOW_MENU;
        balance = 0;
        totalSum = 0;
        currentKey = "Food";
    }

    private void initializePurchases() {
        purchasesByType.put("Food", new PurchaseTypeEntry(new ArrayList<>()));
        purchasesByType.put("Entertainment", new PurchaseTypeEntry(new ArrayList<>()));
        purchasesByType.put("Clothes", new PurchaseTypeEntry(new ArrayList<>()));
        purchasesByType.put("Other", new PurchaseTypeEntry(new ArrayList<>()));
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
                expenses = purchasesByType.computeIfAbsent(currentKey, k -> new PurchaseTypeEntry(new ArrayList<>()));
            } catch (NumberFormatException e) {
                System.out.println("Invalid input!");
            } finally {
                receiveInput = false;
            }
        }
    }

    public void checkPurchaseMapEmpty() {
        if (allPurchases.isEmpty()) {
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
        expenses.addToList(currentPurchase);
        allPurchases.add(currentPurchase);
    }

    public void addPurchaseOperation() {
        try {
            /* Caculate total purchases and new balance */
            double purchaseValue = Double.parseDouble(input);
            totalSum += purchaseValue;

            expenses.increasePurchasesTotal(purchaseValue);
            balance = balance - purchaseValue < 0 ? 0 : balance - purchaseValue;

            /* Add expense value to the list */
            int expenseIdx = expenses.getPurchasesList().size() - 1;
            Purchase currentExpense = expenses.getPurchasesList().get(expenseIdx);
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

        if (state.equals(States.ANALYZE) && expenses.getPurchasesList().isEmpty()) {
            System.out.println("The purchase list is empty");
            return;
        }

        System.out.println(currentKey + ":");
        if(expenses.getPurchasesList().isEmpty()) {
            System.out.println("The purchase list is empty");
        } else {
            for (Purchase purchase : expenses.getPurchasesList()) {
                System.out.println(purchase.toString());
            }
            System.out.printf("Total sum: $%.2f\n", expenses.getPurchasesTotal());
        }

        state = States.SHOW_MENU;
    }

    public void showAllPurchases() {
        if (!state.equals(States.ANALYZE)) {
            System.out.println();
        }
        for (Purchase purchase : allPurchases) {
            System.out.println(purchase.toString());
        }
        System.out.println("Total Sum: $" + String.format("%.2f", totalSum));
    }

    public void showBalance() {
        System.out.printf("\nBalance: $%.2f\n", balance);
        state = States.SHOW_MENU;
    }

    public void savePurchases() {

        if (allPurchases.isEmpty()) {
            System.out.println("\nThe purchase list is empty");
            state = States.SHOW_MENU;
            return;
        }

        fileManager.createFileRoute();

        for (Purchase purchase : allPurchases) {
            fileManager.writeToFile(purchase.getPurchaseDescription());
        }

        fileManager.writeToFile("Balance#Balance#" + balance);

        System.out.println("\nPurchases were saved!");
        state = States.SHOW_MENU;
    }

    public void loadPurchases() {
        List<String> filePurchases = fileManager.getFromFile();
        int listSize = filePurchases.size();

        /* Clear in case previous input were added to avoid re-load already loaded elements */
        if (!allPurchases.isEmpty()) {
            balance = 0;
            purchasesByType.clear();
            allPurchases.clear();

        }

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
                currentKey = "Food"; //Default value in order to avoid adding a "Balance" key on the map
                return;
            }

            expenses = purchasesByType.computeIfAbsent(currentKey, k -> new PurchaseTypeEntry(new ArrayList<>()));

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

    /* Method analyze corresponding to final stage of the project */
    public void analyze() {
        sortingStrategy.sort(this);
        state = States.SHOW_MENU;
    }

}


