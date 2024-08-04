package com.cfloresh.budgetmanager.strategies.menus;

import com.cfloresh.budgetmanager.BudgetManager;
import com.cfloresh.budgetmanager.MenuType;
import com.cfloresh.budgetmanager.States;
import com.cfloresh.budgetmanager.strategies.sort.*;

public class AnalyzeMenu implements Menu {
    @Override
    /* TODO: Implement this method  correctly */
    public void performAction(BudgetManager object, int value) {
        switch (value) {
            case 1 -> object.setSortingStrategy(new SortAllStrategy());
            case 2 -> object.setSortingStrategy(new SortByTypeStrategy());
            case 3 -> {
                object.setMenu(MenuType.SORT_CERTAIN_TYPE);
                return;
            }
            case 4 -> object.backMainMenu();
            default -> throw new NumberFormatException();
        }

        object.setState(States.ANALYZE);
    }
}
