package com.cfloresh.budgetmanager;

import java.util.List;

public class PurchaseTypeEntry {

    private final List<Purchase> purchasesList;
    private double purchasesTotal;

    /* Constructor */
    public PurchaseTypeEntry(List<Purchase> purchasesList) {
        this.purchasesList = purchasesList;
        this.purchasesTotal = 0;
    }

    /* purchaseList Getter */
    public List<Purchase> getPurchasesList() {
        return purchasesList;
    }

    /* purchaseTotal getter */
    public double getPurchasesTotal() {
        return purchasesTotal;
    }

    /* Other class methods */
    public void addToList(Purchase purchase) {
        purchasesList.add(purchase);
    }

    public void increasePurchasesTotal(double amount) {
        purchasesTotal += amount;
    }


}
