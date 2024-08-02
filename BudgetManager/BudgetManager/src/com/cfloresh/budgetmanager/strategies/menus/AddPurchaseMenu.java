package com.cfloresh.budgetmanager.strategies.menus;

import com.cfloresh.budgetmanager.BudgetManager;
import com.cfloresh.budgetmanager.States;

public class AddPurchaseMenu implements Menu{

    @Override
    public void performAction(BudgetManager object, int value) {
        switch (value) {
            case 1, 2, 3, 4 -> object.selectKey(value);
            case 5 -> {
                object.backMainMenu();
                return;
            }
            default -> throw new NumberFormatException();
        }
        object.setState(States.ADD_PURCHASE);
    }
}
