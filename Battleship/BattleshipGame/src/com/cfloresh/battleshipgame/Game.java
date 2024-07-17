package com.cfloresh.battleshipgame;

import java.io.IOException;
import java.util.Scanner;

public class Game {

    private final int TOTAL_PLAYERS = 2;

    private final Player[] players = new Player[TOTAL_PLAYERS];

    public void setPlayerShips(Scanner scan) {
        // code
        //populateGameShips();
        for (int i = 0; i < TOTAL_PLAYERS; i++) {
            players[i] = new Player();
            System.out.printf("Player %d, place your ships on the game field: %n%n", i + 1);
            players[i].requestUserInput(scan);
            System.out.println("\nPress Enter and pass the move to another player");
            scan.nextLine();
            clearConsole();
        }
    }

    //Testing code
    private void populateGameShips() {
        ShipType[] shipTypes = ShipType.values();

        players[0] = new Player();
        players[0].gameShips[0] = new Ship("A1", "A5", shipTypes[0]);
        players[0].gameShips[1] = new Ship("C1", "C4", shipTypes[1]);
        players[0].gameShips[2] = new Ship("E1", "E3", shipTypes[2]);
        players[0].gameShips[3] = new Ship("G1", "G3", shipTypes[3]);
        players[0].gameShips[4] = new Ship("I1", "I2", shipTypes[4]);

        players[0].placeShip(players[0].gameShips[0]);
        players[0].placeShip(players[0].gameShips[1]);
        players[0].placeShip(players[0].gameShips[2]);
        players[0].placeShip(players[0].gameShips[3]);
        players[0].placeShip(players[0].gameShips[4]);

        players[1] = new Player();
        players[1].gameShips[0] = new Ship("A10", "A6", shipTypes[0]);
        players[1].gameShips[1] = new Ship("C10", "C7", shipTypes[1]);
        players[1].gameShips[2] = new Ship("E10", "E8", shipTypes[2]);
        players[1].gameShips[3] = new Ship("G10", "G8", shipTypes[3]);
        players[1].gameShips[4] = new Ship("I10", "I9", shipTypes[4]);

        players[1].placeShip(players[1].gameShips[0]);
        players[1].placeShip(players[1].gameShips[1]);
        players[1].placeShip(players[1].gameShips[2]);
        players[1].placeShip(players[1].gameShips[3]);
        players[1].placeShip(players[1].gameShips[4]);
    }
    //Testing code

    public void startGame(Scanner scan) {

        Player currentPlayer;
        Player enemyPlayer;

        int n = 0;
        while(true) {
            currentPlayer = players[n % 2];
            enemyPlayer = players[(n + 1) % 2];

            showBoards(enemyPlayer.getFogBoard(), currentPlayer.getBoard());

            System.out.printf("\nPlayer %d, is your turn:\n", (n % 2) + 1);
            currentPlayer.takeShot(scan, enemyPlayer.getBoard(), enemyPlayer.getFogBoard(), enemyPlayer.getGameShips());

            if(currentPlayer.getWinCondtion()) {
                System.out.println("\nYou sank the last ship. You won. Congratulations!");
                showBoards(enemyPlayer.getBoard(), currentPlayer.getBoard());
                break;
            } else {
                System.out.println("\nPress Enter and pass the move to another player");
                scan.nextLine();
                clearConsole();
            }

            n++;
        }
    }

    private void showBoards(Board boardA, Board boardB) {
        boardA.printBoard();
        System.out.println("-----------------------");
        boardB.printBoard();
    }

    private void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
