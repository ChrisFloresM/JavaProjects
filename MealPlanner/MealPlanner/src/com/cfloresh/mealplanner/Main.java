package com.cfloresh.mealplanner;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        MealPlanner mealPlanner = new MealPlanner();


        while (!mealPlanner.isExit()) {
            mealPlanner.stateMachine();
            if (mealPlanner.isReadInput()) {
                mealPlanner.setUserInput(scan.nextLine());
            }
        }

    }
}
