package com.cfloresh.coffemachine;

import java.util.Scanner;

public class CoffeMachineMain {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        CoffeeMaker coffeMachine = new CoffeeMaker();

        coffeMachine.printStatus();
        coffeMachine.requestFunction(scan);
        System.out.println();
        coffeMachine.printStatus();
    }
}
