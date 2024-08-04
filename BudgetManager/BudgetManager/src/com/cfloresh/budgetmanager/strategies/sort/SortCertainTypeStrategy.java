package com.cfloresh.budgetmanager.strategies.sort;

import com.cfloresh.budgetmanager.BudgetManager;
import com.cfloresh.budgetmanager.MenuType;
import com.cfloresh.budgetmanager.Purchase;
import com.cfloresh.budgetmanager.PurchaseTypeEntry;

import java.util.List;

public class SortCertainTypeStrategy implements SortingStrategy {
    @Override
    public void sort(BudgetManager object) {
        PurchaseTypeEntry expenses = object.getExpenses();

        if (expenses.getPurchasesList().isEmpty()) {
            System.out.println("\nThe purchase list is empty!");
            object.setMenu(MenuType.ANALYZE);
            return;
        }

        List<Purchase> toSort = expenses.getPurchasesList();
        int limit = toSort.size() - 2;
        Purchase currentPurchase;
        Purchase nextPurchase;

        for (int i = 0; i < toSort.size() - 1; i++) {

            for (int j = 0; j <= limit - i; j++) {
                currentPurchase = toSort.get(j);
                nextPurchase = toSort.get(j + 1);
                if(currentPurchase.getPurchasePrice() < nextPurchase.getPurchasePrice()) {
                    toSort.set(j, nextPurchase);
                    toSort.set(j + 1, currentPurchase);
                }
            }
        }

        object.showPurchases();
        object.setMenu(MenuType.ANALYZE);
    }
}
