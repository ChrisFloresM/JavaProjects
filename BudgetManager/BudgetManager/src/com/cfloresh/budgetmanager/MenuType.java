package com.cfloresh.budgetmanager;

import com.cfloresh.budgetmanager.strategies.menus.*;

public enum MenuType {
    MAIN (new MainMenu(), """
               
                Choose your action:
                1) Add income
                2) Add purchase
                3) Show list of purchases
                4) Balance
                5) Save
                6) Load
                7) Analyze (Sort)
                0) Exit
                """),

    ADD_PURCHASE (new AddPurchaseMenu(), """
               
                Choose the type of purchase:
                1) Food
                2) Clothes
                3) Enterteinment
                4) Other
                5) Back
                """
    ),

    SHOW_PURCHASE (new ShowPurchaseMenu(),"""
               
                Choose the type of purchase:
                1) Food
                2) Clothes
                3) Enterteinment
                4) Other
                5) All
                6) Back
                """
    ),

    ANALYZE (new AnalyzeMenu(),  """
               
                How do you want to sort?:
                1) Sort all purchases
                2) Sort by type
                3) Sort certain type
                4) Back
                """
    ),

    SORT_CERTAIN_TYPE (new SortCertainTypeMenu(), """
               
                Choose the type of purchase:
                1) Food
                2) Clothes
                3) Enterteinment
                4) Other
                5) Back
                """);

    private final String body;
    private final Menu menuStrategy;

    MenuType(Menu menuStrategy, String body) {
        this.menuStrategy = menuStrategy;
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public void performAction(BudgetManager object, int value) {
        menuStrategy.performAction(object, value);
    }
}
