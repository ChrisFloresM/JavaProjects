package com.cfloresh.trafficlightsim;

import java.util.Scanner;

public class TrafficLightSim {

    /* Instance fields */
    private int numOfRoads;
    private int interval;
    private boolean exitState = false;
    private static final String defaultOption = "4";


    /* Getters and Setters */
    public int getNumOfRoads() {
        return numOfRoads;
    }

    public void setNumOfRoads(int numOfRoads) {
        this.numOfRoads = numOfRoads;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public boolean isExitState() {
        return exitState;
    }


    /* Main methods of the class */
    public void showMenu() {
        String menu = """
                Menu:
                1. Add road
                2. Delete road
                3. Open System
                0. Quit""";
        System.out.println(menu);
    }

    public boolean readNumberOfRoadsOrIntervals(String option, Scanner scan) {
        String input = scan.nextLine();

        if (!input.matches("\\d+$") || input.equals("0")) {
            System.out.print("Error! incorrect input. Try again: ");
            return true;
        }

        if (option.equals("Roads")) {
            numOfRoads = Integer.parseInt(input);
        } else if (option.equals("Interval")) {
            interval = Integer.parseInt(input);
        }

        return false;
    }

    public void performAction(String action) {

        if (!action.matches("\\d")) {
            action = defaultOption;
        }

        switch (Integer.parseInt(action)) {
            case 1: addRoad(); break;
            case 2: deleteRoad(); break;
            case 3: openSystem(); break;
            case 0: quit(); break;
            default: System.out.println("Incorrect option");
        }

    }

    public void addRoad() {
        System.out.println("Road added");
    }

    public void deleteRoad() {
        System.out.println("Road deleted");
    }

    public void openSystem() {
        System.out.println("System opened");
    }

    public void quit() {
        System.out.println("Bye!");
        exitState = true;
    }

}
