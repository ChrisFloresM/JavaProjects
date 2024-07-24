package com.cfloresh.budgetmanager;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        BudgetManager budgetManager = new BudgetManager();

        while(!budgetManager.isExitState()) {
            budgetManager.stateMachine();

            if(budgetManager.isReceiveInput()) {
                budgetManager.setInput(scan.nextLine());
            }
        }

    }
}
