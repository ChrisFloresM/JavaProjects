package com.cfloresh.budgetmanager;

public enum MenuType {
    MAIN ("""
               
                Choose your action:
                1) Add income
                2) Add purchase
                3) Show list of purchases
                4) Balance
                0) Exit
                """),

    ADD_PURCHASE ("""
               
                Choose the type of purchase:
                1) Food
                2) Clothes
                3) Enterteinment
                4) Other
                5) Back
                """
    ),

    SHOW_PURCHASE ("""
               
                Choose the type of purchase:
                1) Food
                2) Clothes
                3) Enterteinment
                4) Other
                5) All
                6) Back
                """
    );

    private String body;

    MenuType(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }
}
