package com.cfloresh.budgetmanager.strategies.menus;

import com.cfloresh.budgetmanager.BudgetManager;
import com.cfloresh.budgetmanager.MenuType;
import com.cfloresh.budgetmanager.States;

public class MainMenu implements Menu {
    @Override
    public void performAction(BudgetManager object, int value) {
        switch (value) {
            case 1 -> object.setState(States.ADD_INCOME);
            case 2 -> object.setMenu(MenuType.ADD_PURCHASE);
            case 3 -> object.checkPurchaseMapEmpty();
            case 4 -> object.setState(States.BALANCE);
            case 5 -> object.setState(States.SAVE);
            case 6 -> object.setState(States.LOAD);
            case 7 -> {
                object.setMenu(MenuType.ANALYZE);
            }
            case 0 -> object.setState(States.EXIT);
            default -> throw new NumberFormatException();
        }
    }
}
