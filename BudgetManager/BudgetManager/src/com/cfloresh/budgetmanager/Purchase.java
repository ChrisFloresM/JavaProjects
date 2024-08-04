package com.cfloresh.budgetmanager;

public class Purchase {

    private final String categoryKey;
    private final String concept;
    private double price;

    private String purchaseDescription;

    public Purchase(String categoryKey, String concept) {
        this.categoryKey = categoryKey;
        this.concept = concept;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void generatePurchaseDescription() {
        purchaseDescription = String.format("%s#%s#%f", categoryKey, concept, price);
    }

    public String getPurchaseDescription() {
        return purchaseDescription;
    }

    public double getPurchasePrice() { return price; }

    @Override
    public String toString() {
        return String.format("%s $%.2f", concept, price);
    }
}
