package com.cfloresh.budgetmanager.strategies.sort;

import com.cfloresh.budgetmanager.BudgetManager;
import com.cfloresh.budgetmanager.Purchase;

import java.util.List;

public class SortAllStrategy implements  SortingStrategy {

    @Override
    public void sort(BudgetManager object) {
        List<Purchase> allPurchases = object.getAllPurchases();

        if (allPurchases.isEmpty()) {
            System.out.println("\nThe purchase list is empty!");
            return;
        }

        int limit = allPurchases.size() - 2;
        Purchase currentPurchase;
        Purchase nextPurchase;

        for (int i = 0; i < allPurchases.size() - 1; i++) {

            for (int j = 0; j <= limit - i; j++) {
                currentPurchase = allPurchases.get(j);
                nextPurchase = allPurchases.get(j + 1);
                if(allPurchases.get(j).getPurchasePrice() < allPurchases.get(j + 1).getPurchasePrice()) {
                    allPurchases.set(j, nextPurchase);
                    allPurchases.set(j + 1, currentPurchase);
                }
            }
        }

        System.out.println("\nAll: ");
        object.showAllPurchases();
    }

}
