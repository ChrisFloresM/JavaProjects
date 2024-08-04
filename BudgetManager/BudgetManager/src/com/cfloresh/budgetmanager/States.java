package com.cfloresh.budgetmanager;

/* Enum States for state machine */
public enum States {
    SHOW_MENU {
        @Override
        public void action(BudgetManager object) {
            object.showMenu();
        }

    },

    ADD_INCOME {
        @Override
        public void action(BudgetManager object) {
            object.addIncome();
        }
    },

    ADD_PURCHASE {
        @Override
        public void action(BudgetManager object) { object.addPurchase();}
    },
    SHOW_LIST_PURCHASES {
        @Override
        public void action(BudgetManager object) {object.showPurchases();}
    },

    BALANCE {
        public void action(BudgetManager object) {object.showBalance();}
    },

    EXIT {
        @Override
        public void action(BudgetManager object) {
            object.exit();
        }
    },

    SAVE {
        @Override
        void action(BudgetManager object) {
            object.savePurchases();
        }
    },

    LOAD {
        @Override
        void action(BudgetManager object) {
            object.loadPurchases();
        }
    },

    ANALYZE {
        @Override
        void action(BudgetManager object) {
            object.analyze();
        }
    };

    abstract void action(BudgetManager object);
}
