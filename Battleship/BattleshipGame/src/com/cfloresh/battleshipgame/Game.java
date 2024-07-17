package com.cfloresh.battleshipgame;

import java.io.IOException;
import java.util.Scanner;

public class Game {

    private final int TOTAL_PLAYERS = 2;

    private final Player[] players = new Player[TOTAL_PLAYERS];

    public void setPlayerShips(Scanner scan) {
        for (int i = 0; i < TOTAL_PLAYERS; i++) {
            players[i] = new Player();
            System.out.printf("Player %d, place your ships on the game field: %n%n", i + 1);
            players[i].requestUserInput(scan);
            System.out.println("\nPress Enter and pass the move to another player");
            scan.nextLine();
            clearConsole();
        }
    }

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
