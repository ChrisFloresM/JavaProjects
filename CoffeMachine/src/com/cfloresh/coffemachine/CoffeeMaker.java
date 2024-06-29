package com.cfloresh.coffemachine;

public class CoffeeMaker {

    private static final int WATER_PER_CUP = 200; // ml
    private static final int MILK_PER_CUP = 50;   // ml
    private static final int COFFE_PER_CUP = 15;  // g

    private int totalCoups;
    private int totalWater;
    private int totalMilk;
    private int totalCoffe;

    /* Setters */
    public void setTotalCoups(int totalCoups) {
        this.totalCoups = totalCoups;
    }

    /* Getters */

    public int getTotalCoups() {
        return totalCoups;
    }

    public int getTotalWater() {
        return totalWater;
    }

    public int getTotalMilk() {
        return totalMilk;
    }

    public int getTotalCoffe() {
        return totalCoffe;
    }

    /* Action methods from the class */

    /* printProcess */
    public void printProcess() {
        System.out.println("Starting to make a coffe");
        System.out.println("Grinding Coffee Beans");
        System.out.println("Boiling Water");
        System.out.println("Mixing boiled water with crushed coffee beans");
        System.out.println("Pouring coffee into the cup");
        System.out.println("Pouring some milk into the cup");
        System.out.println("Coffee is ready!");
    }

    public void calculateIngredients() {
        totalWater = totalCoups * WATER_PER_CUP;
        totalMilk = totalCoups * MILK_PER_CUP;
        totalCoffe = totalCoups * COFFE_PER_CUP;
    }


}
