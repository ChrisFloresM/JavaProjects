package com.cfloresh.budgetmanager;

public class Purchase {

    private final String concept;
    private double price;

    private String purchaseDescription;

    public Purchase(String concept) {
        this.concept = concept;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void generatePurchaseDescription() {
        purchaseDescription = String.format("%s $%.2f", concept, price);
    }

    public String getPurchaseDescription() {
        return purchaseDescription;
    }
}
