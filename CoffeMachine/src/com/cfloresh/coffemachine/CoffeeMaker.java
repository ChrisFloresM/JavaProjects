package com.cfloresh.coffemachine;

public class CoffeeMaker {

    /* State machine control variables */
    private boolean readInput;
    private boolean exit;
    private String userInput;
    private States state;

    /* internal instance fields related to the coffee machine */
    private int totalWater;
    private int totalMilk;
    private int totalCoffe;
    private int totalCups;
    private int totalMoney;

    /* CoffeeOrder object with information about the order */
    private CoffeeOrder coffeeOrder;

    /* enum of states */
    enum States {
        START,
        BUY,
        FILL_WATER,
        FILL_MILK,
        FILL_COFFEE,
        FILL_CUPS,
        REMAINING,
        TAKE,
        EXIT
/*        abstract void stateAction();*/
    }

    /* Constructor to initialize fields and state */
    public CoffeeMaker() {
        totalWater = 400;
        totalMilk = 540;
        totalCoffe = 120;
        totalCups = 9;
        totalMoney = 550;

        readInput = false;
        exit = false;
        state = States.START;
    }

    public void stateMachine(){
        switch (this.state) {
            case START -> stateStart();
            case BUY -> stateBuy();
            case FILL_WATER -> stateFillWater();
            case FILL_MILK -> stateFillMilk();
            case FILL_COFFEE -> stateFillCoffee();
            case FILL_CUPS -> stateFillCups();
            case REMAINING -> stateRemaining();
            case TAKE -> stateTake();
            case EXIT -> stateExit();
        }
    }

    public void setUserInput(String input) {
        this.userInput = input;
    }

    public boolean getExit() {
        return this.exit;
    }

    public boolean getReadInput() {
        return this.readInput;
    }

    private void stateExit() {
        this.exit = true;
    }

    private void stateStart() {
        if (!this.readInput) {
            System.out.println("Write action (buy, fill, take, remaining, exit): ");
            this.readInput = true;
        } else {
            this.readInput = false;
            if (!validateStartInput()) {
                System.out.println("Invalid command!\n");
                return;
            }

            if (this.userInput.equalsIgnoreCase("fill")) {
                this.state = States.FILL_WATER;
            } else {
                this.state = States.valueOf(this.userInput.toUpperCase());
            }

        }
    }

    private boolean validateStartInput() {

        boolean validBuyCmd = this.userInput.equalsIgnoreCase("buy");
        boolean validFillCmd = this.userInput.equalsIgnoreCase("fill");
        boolean validTakeCmd = this.userInput.equalsIgnoreCase("take");
        boolean validRemCmd = this.userInput.equalsIgnoreCase("remaining");
        boolean validExitCmd = this.userInput.equalsIgnoreCase("exit");

        return validBuyCmd || validFillCmd || validTakeCmd || validRemCmd || validExitCmd;

    }

    private void stateBuy(){
        if(!this.readInput) {
            System.out.println("\nwhat do you want to buy? 1 - espresso, 2 - latte, 3 - capuccino, back - to main menu: ");
            this.readInput = true;
        } else {
            this.readInput = false;

            if(!validateBuyInput()) {
                System.out.println("Invalid command!");
                return;
            }

            String coffeType = this.userInput;
            if (!coffeType.equalsIgnoreCase("back")) {
                coffeeOrder = new CoffeeOrder(Integer.parseInt(coffeType));
                if (validateOrder()) {
                    totalWater -= coffeeOrder.getOrderWater();
                    totalMilk -= coffeeOrder.getOrderMilk();
                    totalCoffe -= coffeeOrder.getOrderCoffe();
                    totalCups--;
                    totalMoney += coffeeOrder.getOrderPrice();
                }
            }

            this.state = States.START;
            System.out.println();
        }
    }

    private boolean validateBuyInput() {
        boolean validEspressCmd = this.userInput.equalsIgnoreCase("1");
        boolean validLatteCmd = this.userInput.equalsIgnoreCase("2");
        boolean validCapCmd = this.userInput.equalsIgnoreCase("3");
        boolean validBackCmd = this.userInput.equalsIgnoreCase("back");

        return validEspressCmd || validLatteCmd || validCapCmd || validBackCmd;
    }

    private boolean validateOrder() {
        boolean result = true;

        int orderWater = coffeeOrder.getOrderWater();
        int orderMilk = coffeeOrder.getOrderMilk();
        int orderCoffee = coffeeOrder.getOrderCoffe();

        if (totalWater >= orderWater && totalMilk >= orderMilk && totalCoffe >= orderCoffee) {
            System.out.println("I have enough resources, making you a coffee!");
            return result;
        }

        result = false;

        if(totalWater < orderWater) {
            System.out.println("Sorry, not enough water!");
        }

        if(totalMilk < orderMilk) {
            System.out.println("Sorry, not enough milk!");
        }

        if(totalCoffe < orderCoffee) {
            System.out.println("Sorry, not enough coffee beans!");
        }

        if(totalCups < 1) {
            System.out.println("Sorry, not enough disposable cups!");
        }

        return result;
    }

    private void stateFillWater() {
        if(!this.readInput) {
            System.out.println("\nWrite how many ml of water you want to add:");
            this.readInput = true;
        } else {
            this.readInput = false;
            if(!validateFillInput()) {
                System.out.println("Input a numeric value!");
                return;
            }
            this.totalWater += Integer.parseInt(this.userInput);
            this.state = States.FILL_MILK;
        }
    }

    private boolean validateFillInput() {
        for (Character c : this.userInput.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    private void stateFillMilk() {
        if(!this.readInput) {
            System.out.println("\nWrite how many ml of milk you want to add:");
            this.readInput = true;
        } else {
            this.readInput = false;
            if(!validateFillInput()) {
                System.out.println("Input a numeric value!");
                return;
            }
            this.totalMilk += Integer.parseInt(this.userInput);
            this.state = States.FILL_COFFEE;
        }
    }

    private void stateFillCoffee() {
        if(!this.readInput) {
            System.out.println("\nWrite how many grams of coffee you want to add:");
            this.readInput = true;
        } else {
            this.readInput = false;
            if(!validateFillInput()) {
                System.out.println("Input a numeric value!");
                return;
            }
            this.totalCoffe += Integer.parseInt(this.userInput);
            this.state = States.FILL_CUPS;
        }
    }

    private void stateFillCups() {
        if(!this.readInput) {
            System.out.println("\nWrite how many disposable cups you want to add:");
            this.readInput = true;
        } else {
            this.readInput = false;
            if(!validateFillInput()) {
                System.out.println("Input a numeric value!");
                return;
            }
            this.totalCups += Integer.parseInt(this.userInput);
            this.state = States.START;
            System.out.println();
        }
    }

    private void stateRemaining() {
        String status = String.format("""
                
                The coffee machine has:
                %d ml of water
                %d ml of milk
                %d g of coffee beans
                %d disposable cups
                $%d of money
                """, totalWater, totalMilk, totalCoffe, totalCups, totalMoney);

        System.out.print(status);
        this.state = States.START;
        System.out.println();
    }

    private void stateTake() {
        System.out.printf("I gave you $%d\n", totalMoney);
        totalMoney = 0;
        this.state = States.START;
        System.out.println();
    }
}



