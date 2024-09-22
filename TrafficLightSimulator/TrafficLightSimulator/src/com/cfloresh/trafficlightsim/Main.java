package com.cfloresh.trafficlightsim;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        TrafficLightSim trafficLightSim = new TrafficLightSim();

        System.out.println("Welcome to the traffic management system");
        boolean repeatCycle;

        System.out.print("Input number of roads: ");
        do {
            repeatCycle = trafficLightSim.readNumberOfRoadsOrIntervals("Roads", sc);
        } while(repeatCycle);

        System.out.print("Input number of intervals: ");
        do {
            repeatCycle = trafficLightSim.readNumberOfRoadsOrIntervals("Interval", sc);
        } while(repeatCycle);

        while(true) {
            clearScreen();
            trafficLightSim.showMenu();
            trafficLightSim.performAction(sc.nextLine());
            if (trafficLightSim.isExitState()) {
                break;
            }
            sc.nextLine();
        }

        sc.close();

    }

    private static void clearScreen() {
        try {
            var clearCommand = System.getProperty("os.name").contains("Windows")
                    ? new ProcessBuilder("cmd", "/c", "cls")
                    : new ProcessBuilder("clear");
            clearCommand.inheritIO().start().waitFor();
        }
        catch (InterruptedException | IOException e) {
            System.out.println("Error while clearing screen");
        }
    }
}