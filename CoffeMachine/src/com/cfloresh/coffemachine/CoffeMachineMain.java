package com.cfloresh.coffemachine;

import java.util.Scanner;

public class CoffeMachineMain {

    public static void main(String[] args) {
        CoffeeMaker coffeMachine = new CoffeeMaker();
        Scanner scan = new Scanner(System.in);

        while(!coffeMachine.getExit()) {
            coffeMachine.stateMachine();
            if(coffeMachine.getReadInput()) {
                coffeMachine.setUserInput(scan.nextLine());
            }
        }
    }
}
