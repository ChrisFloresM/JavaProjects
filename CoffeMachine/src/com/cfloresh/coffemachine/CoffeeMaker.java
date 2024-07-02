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

    /* enum of states - each state and its defined actions */
    enum States {
        START {
            void stateAction(String input, CoffeeMaker instance) {
                if (!instance.getReadInput()) {
                    System.out.println("Write action (buy, fill, take, remaining, exit): ");
                    instance.setReadInput(true);
                } else {
                    instance.setReadInput(false);
                    if (!instance.validateStartInput(input)) {
                        System.out.println("Invalid command!\n");
                        return;
                    }

                    if (input.equalsIgnoreCase("fill")) {
                        instance.setState(States.FILL_WATER);
                    } else {
                        instance.setState(States.valueOf(input.toUpperCase()));
                    }

                }
            }
        },

        BUY {
            void stateAction(String input, CoffeeMaker instance) {
                if(!instance.getReadInput()) {
                    System.out.println("\nwhat do you want to buy? 1 - espresso, 2 - latte, 3 - capuccino, back - to main menu: ");
                    instance.setReadInput(true);
                } else {
                    instance.setReadInput(false);
                    if(!instance.validateBuyInput(input)) {
                        System.out.println("Invalid command!");
                        return;
                    }

                    if (!input.equalsIgnoreCase("back")) {
                        instance.setCoffeeOrder(new CoffeeOrder(Integer.parseInt(input)));
                        if (instance.validateOrder()) {
                            instance.processOrder();
                        }
                    }

                    instance.setState(States.START);
                    System.out.println();
                }
            }
        },

        FILL_WATER {
            void stateAction(String input, CoffeeMaker instance) {
                //do nothing
                if(!instance.getReadInput()) {
                    System.out.println("\nWrite how many ml of water you want to add:");
                    instance.setReadInput(true);
                } else {
                    instance.setReadInput(false);
                    if(!instance.validateFillInput(input)) {
                        System.out.println("Input a numeric value!");
                        return;
                    }
                    instance.increaseWater(Integer.parseInt(input));
                    instance.setState(States.FILL_MILK);
                }
            }
        },

        FILL_MILK {
            void stateAction(String input, CoffeeMaker instance) {
                //do nothing
                if(!instance.getReadInput()) {
                    System.out.println("\nWrite how many ml of milk you want to add:");
                    instance.setReadInput(true);
                } else {
                    instance.setReadInput(false);
                    if(!instance.validateFillInput(input)) {
                        System.out.println("Input a numeric value!");
                        return;
                    }
                    instance.increaseMilk(Integer.parseInt(input));
                    instance.setState(States.FILL_COFFEE);
                }
            }
        },
        FILL_COFFEE {
            void stateAction(String input, CoffeeMaker instance) {
                //do nothing
                if(!instance.getReadInput()) {
                    System.out.println("\nWrite how many grams of coffee beans you want to add:");
                    instance.setReadInput(true);
                } else {
                    instance.setReadInput(false);
                    if(!instance.validateFillInput(input)) {
                        System.out.println("Input a numeric value!");
                        return;
                    }
                    instance.increaseCoffee(Integer.parseInt(input));
                    instance.setState(States.FILL_CUPS);
                }
            }
        },

        FILL_CUPS {
            void stateAction(String input, CoffeeMaker instance) {
                //do nothing
                if(!instance.getReadInput()) {
                    System.out.println("\nWrite how many disposable cups you want to add:");
                    instance.setReadInput(true);
                } else {
                    instance.setReadInput(false);
                    if(!instance.validateFillInput(input)) {
                        System.out.println("Input a numeric value!");
                        return;
                    }
                    instance.increaseCups(Integer.parseInt(input));
                    instance.setState(States.START);
                }
            }
        },
        REMAINING {
            void stateAction(String input, CoffeeMaker instance) {
                //do nothing
                String status = String.format("""
                
                The coffee machine has:
                %d ml of water
                %d ml of milk
                %d g of coffee beans
                %d disposable cups
                $%d of money
                """, instance.getTotalWater(), instance.getTotalMilk(), instance.getTotalCoffe(),
                        instance.getTotalCups(), instance.getTotalMoney());

                System.out.print(status);
                instance.setState(States.START);
                System.out.println();
            }
        },

        TAKE {
            void stateAction(String input, CoffeeMaker instance) {
                //do nothing
                System.out.printf("I gave you $%d\n", instance.getTotalMoney());
                instance.setTotalMoney(0);
                instance.setState(States.START);
                System.out.println();
            }
        },

        EXIT {
            void stateAction(String input, CoffeeMaker instance) {
                //do nothing
                instance.setExit(true);
            }
        };

        abstract void stateAction(String input, CoffeeMaker instance);
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

    /* state machine method which will be called on each iteration while the machine is ON */
    public void stateMachine(){
        this.state.stateAction(this.userInput, this);
    }

    public void setUserInput(String input) {
        this.userInput = input;
    }

    /* Getters and setters - Exit flag*/
    public boolean getExit() {
        return this.exit;
    }

    public void setExit(boolean state) {
        this.exit = state;
    }

    /* Getter and setter - readInput flag */
    public boolean getReadInput() {
        return this.readInput;
    }

    public void setReadInput(boolean readInput) {
        this.readInput = readInput;
    }

    /* Setter for the CoffeOrder object */
    public void setCoffeeOrder(CoffeeOrder coffeeOrder) {
        this.coffeeOrder = coffeeOrder;
    }

    /* Setter for the state flag */
    public void setState(States state) {
        this.state = state;
    }

    /* Setters to increase each ingredient by a given amount */
    public void increaseWater(int value) {
        this.totalWater += value;
    }

    public void increaseMilk(int value) {
        this.totalMilk += value;
    }

    public void increaseCoffee(int value) {
        this.totalCoffe += value;
    }

    public void increaseCups(int value) {
        this.totalCups += value;
    }

    /* Getter for each ingredient */
    public int getTotalWater() {
        return totalWater;
    }

    public int getTotalMilk() {
        return totalMilk;
    }

    public int getTotalCoffe() {
        return totalCoffe;
    }

    public int getTotalCups() {
        return totalCups;
    }

    public int getTotalMoney() {
        return totalMoney;
    }

    /* Setter to reset the money when taken from the user */
    public void setTotalMoney(int totalMoney) {
        this.totalMoney = totalMoney;
    }

    /* Method used to validate the input from the user on the Start state */
    public boolean validateStartInput(String input) {
        boolean validBuyCmd = input.equalsIgnoreCase("buy");
        boolean validFillCmd = input.equalsIgnoreCase("fill");
        boolean validTakeCmd = input.equalsIgnoreCase("take");
        boolean validRemCmd = input.equalsIgnoreCase("remaining");
        boolean validExitCmd = input.equalsIgnoreCase("exit");

        return validBuyCmd || validFillCmd || validTakeCmd || validRemCmd || validExitCmd;

    }

    /* Method to validate the input from the user on the Buy state */
    public boolean validateBuyInput(String input) {
        boolean validEspressCmd = input.equalsIgnoreCase("1");
        boolean validLatteCmd = input.equalsIgnoreCase("2");
        boolean validCapCmd = input.equalsIgnoreCase("3");
        boolean validBackCmd = input.equalsIgnoreCase("back");

        return validEspressCmd || validLatteCmd || validCapCmd || validBackCmd;
    }

    /* Method to validate that an order can be performed */
    public boolean validateOrder() {
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

    /* Method to process a coffe order by decreasing the amount of ingredients */
    public void processOrder(){
        totalWater -= coffeeOrder.getOrderWater();
        totalMilk -= coffeeOrder.getOrderMilk();
        totalCoffe -= coffeeOrder.getOrderCoffe();
        totalCups--;
        totalMoney += coffeeOrder.getOrderPrice();
    }

    /* Method to validate the user input on any of the Fill states */
    public boolean validateFillInput(String input) {
        for (Character c : input.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

}



