package com.cfloresh.budgetmanager.strategies.menus;

import com.cfloresh.budgetmanager.BudgetManager;
import com.cfloresh.budgetmanager.MenuType;
import com.cfloresh.budgetmanager.States;
import com.cfloresh.budgetmanager.strategies.sort.SortCertainTypeStrategy;

public class SortCertainTypeMenu implements Menu {
    @Override
    public void performAction(BudgetManager object, int value) {
        switch (value) {
            case 1, 2, 3, 4 -> {
                object.selectKey(value);
                object.setSortingStrategy(new SortCertainTypeStrategy());
            }
            case 5 -> {
                object.setMenu(MenuType.ANALYZE);
                return;
            }
            default -> throw new NumberFormatException();
        }
        object.setState(States.ANALYZE);
    }

}
