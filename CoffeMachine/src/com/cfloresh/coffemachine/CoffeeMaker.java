package com.cfloresh.coffemachine;

import java.util.Scanner;

public class CoffeeMaker {

    private int totalWater;
    private int totalMilk;
    private int totalCoffe;
    private int totalCups;
    private int totalMoney;

    private CoffeeOrder coffeeOrder;

    /* Constructor */

    public CoffeeMaker() {
        totalWater = 400;
        totalMilk = 540;
        totalCoffe = 120;
        totalCups = 9;
        totalMoney = 550;
    }

    /* Getters and Setters */

    /* ====================  Action methods for the class ==================== */

    /* Start coffe machine operation */
    public void startOperation() {
        Scanner scan = new Scanner(System.in);
        while(true) {
            System.out.println("Write action (buy, fill, take, remaining, exit): ");
            String function = scan.nextLine();

            if(function.equalsIgnoreCase("exit")) {
                break;
            }

            requestFunction(scan, function);
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
    }

    /* Validate order */
    public boolean validateOrder() {
        boolean result = true;

        int orderWater = coffeeOrder.getOrderWater();
        int orderMilk = coffeeOrder.getOrderMilk();
        int orderCoffee = coffeeOrder.getOrderCoffe();

        if (totalWater >= orderWater && totalMilk >= orderMilk && totalCoffe >= orderCoffee) {
            System.out.println("I have enough resources, making you a coffee!");
            return result;
        }

        result = false;

        if(totalWater < orderWater) {
            System.out.println("Sorry, not enough water!");
        }

        if(totalMilk < orderMilk) {
            System.out.println("Sorry, not enough milk!");
        }

        if(totalCoffe < orderCoffee) {
            System.out.println("Sorry, not enough coffee beans!");
        }

        if(totalCups < 1) {
            System.out.println("Sorry, not enough disposable cups!");
        }

        return result;
    }

    public void requestFunction(Scanner scan, String request) {
        System.out.println();
        switch (request) {
            case "buy" -> functionBuy(scan);
            case "fill" -> functionFill(scan);
            case "take" -> functionTake();
            case "remaining" -> functionRemaining();
            default -> System.out.println("Not recognized function");
        }
    }

    public void functionBuy(Scanner scan){
        System.out.println("what do you want to buy? 1 - espresso, 2 - latte, 3 - capuccino, back - to main menu: ");
        String coffeType = scan.nextLine();

        if (!coffeType.equalsIgnoreCase("back")) {
            coffeeOrder = new CoffeeOrder(Integer.parseInt(coffeType));
            if(validateOrder()) {
                totalWater -= coffeeOrder.getOrderWater();
                totalMilk -= coffeeOrder.getOrderMilk();
                totalCoffe -= coffeeOrder.getOrderCoffe();
                totalCups--;
                totalMoney += coffeeOrder.getOrderPrice();
            }
        }

    }

    public void functionFill(Scanner scan) {
        System.out.println("Write how many ml of water you want to add:");
        totalWater += scan.nextInt();

        System.out.println("Write how many ml of milk you want to add:");
        totalMilk += scan.nextInt();

        System.out.println("Write how many grams of coffee beans you want to add:");
        totalCoffe += scan.nextInt();

        System.out.println("Write how many disposable cups you want to add:");
        totalCups += scan.nextInt();

        //clear the scan buffer input
        scan.nextLine();
    }

    public void functionTake() {
        System.out.printf("I gave you $%d\n", totalMoney);
        totalMoney = 0;
    }
}

