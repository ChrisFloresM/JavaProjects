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

    /* printStatus */
    public void printStatus() {
        String status = String.format("""
                The coffee machine has:
                %d ml of water
                %d ml of milk
                %d g of coffee beans
                %d disposable cups
                $%d of money
                """, totalWater, totalMilk, totalCoffe, totalCups, totalMoney);

        System.out.println(status);
    }

    public void generateNewOrder(Scanner scan) {
        System.out.println("Write how many cups of coffee you will need:");

        /* Generate new order with the total cups requested by the user */
        coffeeOrder = new CoffeeOrder(scan.nextInt());
        coffeeOrder.calculateIngredients();
    }

    public void showOrder() {
        System.out.printf("""
                        For %d cups of coffee you will need:
                        %d ml of water
                        %d ml of milk
                        %d g of coffee beans""",
                coffeeOrder.getOrderCups(),
                coffeeOrder.getOrderWater(),
                coffeeOrder.getOrderMilk(),
                coffeeOrder.getOrderCoffe());
    }

    /* TODO: Modify this function to work with the new features */
    public String validateOrder(){

        int totalOrderCups = coffeeOrder.getOrderCups();

        float totalCupsCapacityWater = (float)totalWater / CoffeeOrder.WATER_PER_CUP;
        float totalCupsCapacityMilk = (float)totalMilk / CoffeeOrder.MILK_PER_CUP;
        float totalCupsCapacityCoffee = (float)totalCoffe / CoffeeOrder.COFFE_PER_CUP;

        int totalCupsCapacity = (int)(Math.min(Math.min(totalCupsCapacityCoffee, totalCupsCapacityMilk), totalCupsCapacityWater));

        if(totalCupsCapacity == totalOrderCups) {
            return "Yes, I can make that amount of coffee";
        }

        if(totalCupsCapacity > totalOrderCups) {
            return String.format("Yes, I can make that amount of coffe (and even %d more than that)", totalCupsCapacity - totalOrderCups);
        }

        return String.format("No, I can make only %d cup(s) of coffee", totalCupsCapacity);
    }

    public void requestFunction(Scanner scan) {
        System.out.println("Write action (buy, fill, take): ");
        switch (scan.nextLine()) {
            case "buy" -> functionBuy(scan);
            case "fill" -> functionFill(scan);
            case "take" -> functionTake();
            default -> System.out.println("Not recognized function");
        }
    }

    public void functionBuy(Scanner scan){
        System.out.println("what do you want to buy? 1 - espresso, 2 - latte, 3 - capuccino: ");
        coffeeOrder = new CoffeeOrder(scan.nextInt());
        totalWater -= coffeeOrder.getOrderWater();
        totalMilk -= coffeeOrder.getOrderMilk();
        totalCoffe -= coffeeOrder.getOrderCoffe();
        totalMoney += coffeeOrder.getOrderPrice();
        totalCups--;
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
    }

    public void functionTake() {
        System.out.printf("I gave you $%d\n", totalMoney);
        totalMoney = 0;
    }
}

