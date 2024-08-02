package com.cfloresh.budgetmanager.strategies.menus;

import com.cfloresh.budgetmanager.BudgetManager;

public class MenuManager {
    private Menu currentMenu;

    public MenuManager(Menu currentMenu) {
        this.currentMenu = currentMenu;
    }

    public void setCurrentMenu(Menu currentMenu) {
        this.currentMenu = currentMenu;
    }

    public void performAction(BudgetManager object, int value) {
        currentMenu.performAction(object, value);
    }
}
