package com.cfloresh.budgetmanager.strategies.sort;

import com.cfloresh.budgetmanager.BudgetManager;
import com.cfloresh.budgetmanager.PurchaseTypeEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SortByTypeStrategy implements SortingStrategy {
    @Override
    public void sort(BudgetManager object) {
        Map <String, PurchaseTypeEntry> purchasesByType = object.getPurchasesByType();

        if (purchasesByType.isEmpty()) {
            System.out.println("\nThe purchase list is empty!");
            return;
        }

        List<Map.Entry<String, PurchaseTypeEntry>> purchasesEntries = new ArrayList<>(purchasesByType.entrySet());

        int limit = purchasesEntries.size() - 2;
        Map.Entry<String, PurchaseTypeEntry> currentPurchaseType;
        Map.Entry<String, PurchaseTypeEntry> nextPurchaseType;

        for (int i = 0; i < purchasesEntries.size() - 1; i++) {

            for (int j = 0; j <= limit - i; j++) {
                currentPurchaseType = purchasesEntries.get(j);
                nextPurchaseType = purchasesEntries.get(j + 1);
                if(currentPurchaseType.getValue().getPurchasesTotal() < nextPurchaseType.getValue().getPurchasesTotal()) {
                    purchasesEntries.set(j, nextPurchaseType);
                    purchasesEntries.set(j + 1, currentPurchaseType);
                }
            }
        }

        /* Rebuild the map in order to have it sorted */
        purchasesByType.clear();

        for(Map.Entry<String, PurchaseTypeEntry> entry : purchasesEntries) {
            purchasesByType.put(entry.getKey(), entry.getValue());
        }

        System.out.println("\nTypes:");
        for (Map.Entry<String, PurchaseTypeEntry> entry : new ArrayList<>(purchasesByType.entrySet())) {
            String key = entry.getKey();
            double total = entry.getValue().getPurchasesTotal();

            String outputMsg = total == 0 ? String.format("%s - $0", key) : String.format("%s - $%.2f", key, total);
            System.out.println(outputMsg);
        }
    }
}
