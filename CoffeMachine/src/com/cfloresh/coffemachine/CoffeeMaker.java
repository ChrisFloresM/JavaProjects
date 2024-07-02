package com.cfloresh.coffemachine;

import java.util.Scanner;

public class CoffeeMaker {

    private int totalWater;
    private int totalMilk;
    private int totalCoffe;
    private int totalCups;
    private int totalMoney;

    private CoffeeOrder coffeeOrder;

    private String userInput;

    private String state = "switchMessage";
    private String msgState = "start";
    private String fillSubSt = "water";

    private boolean exit = false;
    private boolean readInput = true;

    /* Constructor */
/*
    public CoffeeMaker() {
        totalWater = 400;
        totalMilk = 540;
        totalCoffe = 120;
        totalCups = 9;
        totalMoney = 550;
    }*/

    /* Getters and Setters */

    /* ====================  Action methods for the class ==================== */

    /* Method to get input from the user*/
    public void getUserInput(String input) {
        this.userInput = input;
    }

    public boolean getReadInput() {
        return this.readInput;
    }

    public boolean getExit() {
        return this.exit;
    }

    public void stateMachine() {
        switch(state) {
            case "switchMessage":
                switchMessage();
                readInput = !(msgState.equalsIgnoreCase("take") || msgState.equalsIgnoreCase("remaining"));
                break;

            case "start":
                startOperation();
                readInput = false;
                break;

            case "buy":
                functionBuy();
                readInput = false;
                break;

            case "fill":
                functionFill();
                readInput = false;
                break;

            case "take":
                functionTake();
                readInput = false;
                break;

            case "remaining":
                functionRemaining();
                readInput = false;
                break;

            default:
                System.out.println("Error");
                state = "switchMessage";
                msgState = "start";
        }
    }

    private void switchMessage() {
        switch(msgState) {
            case "start":
                System.out.println("Write action (buy, fill, take, remaining, exit): ");
                state = "start";
                break;

            case "buy":
                System.out.println("what do you want to buy? 1 - espresso, 2 - latte, 3 - capuccino, back - to main menu: ");
                state = userInput;
                break;

            case "fillWater":
                System.out.println("Write how many ml of water you want to add:");
                state = "fill";
                break;

            case "fillMilk":
                System.out.println("Write how many ml of milk you want to add:");
                state = "fill";
                break;

            case "fillCoffee":
                System.out.println("Write how many grams of coffee beans you want to add:");
                state = "fill";
                break;

            case "fillCups":
                System.out.println("Write how many disposable cups you want to add:");
                state = "fill";
                break;

            case "take":
                state = "take";
                break;

            case "remaining":
                state = "remaining";
                break;

        }
    }

    /* Start coffe machine operation */
    public void startOperation() {
         if (userInput.equalsIgnoreCase("exit")) {
            exit = true;
         } else {
             state = "switchMessage";
             msgState = userInput;

             if(msgState.equalsIgnoreCase("fill")) {
                 msgState = "fillWater";
             }

             System.out.println();
         }
    }

    /* printStatus */
    public void functionRemaining() {
        String status = String.format("""
                The coffee machine has:
                %d ml of water
                %d ml of milk
                %d g of coffee beans
                %d disposable cups
                $%d of money
                """, totalWater, totalMilk, totalCoffe, totalCups, totalMoney);

        System.out.print(status);
        state = "switchMessage";
        System.out.println();
        msgState = "start";

    }

    /* Validate order */
    public boolean validateOrder() {
        boolean result = true;

        int orderWater = coffeeOrder.getOrderWater();
        int orderMilk = coffeeOrder.getOrderMilk();
        int orderCoffee = coffeeOrder.getOrderCoffe();

        if (totalWater >= orderWater && totalMilk >= orderMilk && totalCoffe >= orderCoffee) {
            System.out.println("I have enough resources, making you a coffee!\n");
            return result;
        }

        result = false;

        if(totalWater < orderWater) {
            System.out.println("Sorry, not enough water!\n");
        }

        if(totalMilk < orderMilk) {
            System.out.println("Sorry, not enough milk!\n");
        }

        if(totalCoffe < orderCoffee) {
            System.out.println("Sorry, not enough coffee beans!\n");
        }

        if(totalCups < 1) {
            System.out.println("Sorry, not enough disposable cups!\n");
        }

        return result;
    }

    public void functionBuy(){

        String coffeType = userInput;

        if (!coffeType.equalsIgnoreCase("back")) {
            coffeeOrder = new CoffeeOrder(Integer.parseInt(coffeType));
            if(validateOrder()) {
                totalWater -= coffeeOrder.getOrderWater();
                totalMilk -= coffeeOrder.getOrderMilk();
                totalCoffe -= coffeeOrder.getOrderCoffe();
                totalCups--;
                totalMoney += coffeeOrder.getOrderPrice();
            }
        } else {
            System.out.println();
        }

        state = "switchMessage";
        msgState = "start";

    }

    public void functionFill() {
        switch(msgState) {
            case "fillWater":
                totalWater += Integer.parseInt(userInput);
                state = "switchMessage";
                msgState = "fillMilk";
                break;

            case "fillMilk":
                totalMilk += Integer.parseInt(userInput);
                state = "switchMessage";
                msgState = "fillCoffee";
                break;

            case "fillCoffee":
                totalCoffe += Integer.parseInt(userInput);
                state = "switchMessage";
                msgState = "fillCups";
                break;

            case "fillCups":
                totalCups += Integer.parseInt(userInput);
                state = "switchMessage";
                msgState = "start";
                System.out.println();
                break;

            default:
                System.out.println("Error");
        }
    }

    public void functionTake() {
        System.out.printf("I gave you $%d\n", totalMoney);
        totalMoney = 0;
        state = "switchMessage";
        System.out.println();
        msgState = "start";
    }
}

