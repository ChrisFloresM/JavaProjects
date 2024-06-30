package com.cfloresh.coffemachine;

public class CoffeeOrder {
    /* prices per coffee type:                 Espresso | Latte | Capuccino*/
    public static final int[] WATER_PER_TYPE = {250,       350,   200};
    public static final int[] MILK_PER_TYPE =  {  0,        75,   100};
    public static final int[] COFEE_PER_TYPE = { 16,        20,    12};
    public static final int[] COST_PER_TYPE =  {  4,         7,     6};

    private final int coffeeType;
    private int orderCups;
    private int orderWater;
    private int orderMilk;
    private int orderCoffe;
    private int orderPrice;

    /* Constructor */
    public CoffeeOrder(int coffeeType) {
        this.coffeeType = coffeeType;
        calculateIngredients();
    }

    /* Getters and Setters */
    public int getOrderCups() {
        return orderCups;
    }

    public void setOrderCups(int orderCups) {
        this.orderCups = orderCups;
    }

    public int getOrderWater() {
        return orderWater;
    }

    public void setOrderWater(int orederWater) {
        this.orderWater = orederWater;
    }

    public int getOrderMilk() {
        return orderMilk;
    }

    public void setOrderMilk(int orderMilk) {
        this.orderMilk = orderMilk;
    }

    public int getOrderCoffe() {
        return orderCoffe;
    }

    public void setOrderCoffe(int orderCoffe) {
        this.orderCoffe = orderCoffe;
    }

    public int getCoffeeType() {
        return coffeeType;
    }

    public int getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(int orderPrice) {
        this.orderPrice = orderPrice;
    }

    /* ====================  Action methods for the class ==================== */

    /* Calculate required ingredients based on amount of cups for the order */
    public void calculateIngredients() {
        int localCoffetype = coffeeType - 1;

        orderWater = WATER_PER_TYPE[localCoffetype];
        orderMilk = MILK_PER_TYPE[localCoffetype];
        orderCoffe = COFEE_PER_TYPE[localCoffetype];
        orderPrice = COST_PER_TYPE[localCoffetype];
    }
}
