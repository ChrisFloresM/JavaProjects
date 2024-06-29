package com.cfloresh.coffemachine;

import java.util.Scanner;

public class CoffeMachineMain {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        CoffeeMaker coffeMachine = new CoffeeMaker();

        System.out.println("Write how many cups of coffee you will need:");
        coffeMachine.setTotalCoups(scan.nextInt());

        coffeMachine.calculateIngredients();

        System.out.printf("""
                        For %d cups of coffee you will need:
                        %d ml of water
                        %d ml of milk
                        %d g of coffee beans""",
                coffeMachine.getTotalCoups(),
                coffeMachine.getTotalWater(),
                coffeMachine.getTotalMilk(),
                coffeMachine.getTotalCoffe());
    }
}
