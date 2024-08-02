package com.cfloresh.budgetmanager.strategies.menus;

import com.cfloresh.budgetmanager.BudgetManager;
import com.cfloresh.budgetmanager.States;

public class ShowPurchaseMenu implements Menu{
    @Override
    public void performAction(BudgetManager object, int value) {
        switch (value) {
            case 1, 2, 3, 4 -> object.selectKey(value);
            case 5 ->  {
                object.showAllPurchases();
                object.setState(States.SHOW_MENU);
                return;
            }
            case 6 -> {
                object.backMainMenu();
                return;
            }
            default -> throw new NumberFormatException();
        }
        object.setState(States.SHOW_LIST_PURCHASES);
    }
}
