package com.cfloresh.battleshipgame;
import java.util.Scanner;

public class BattleshipMain {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        Game newGame = new Game();
        newGame.requestUserInput(scan);
        newGame.startGame(scan);
    }
}