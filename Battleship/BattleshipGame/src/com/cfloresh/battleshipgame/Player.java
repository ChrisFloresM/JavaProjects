package com.cfloresh.battleshipgame;

import java.util.Scanner;

public class Player {

    private final int TOTAL_SHIPS = 5;

    private final Board board;
    private final Board fogBoard;
    private final Ship[] gameShips = new Ship[TOTAL_SHIPS];

    private boolean winCondition = false;

    private boolean shotDrankShip = false;

    public boolean getWinCondtion() {
        return this.winCondition;
    }

    public Board getBoard() {
        return this.board;
    }

    public Board getFogBoard() {
        return this.fogBoard;
    }

    public Ship[] getGameShips() { return this.gameShips; }

    public Player() {
        this.board = new Board();
        this.fogBoard = new Board();
    }

    public void requestUserInput(Scanner scan) {
        ShipType[] shipTypes = ShipType.values();

        String start;
        String end;
        board.printBoard();

        for (int i = 0; i < TOTAL_SHIPS; i++){

            System.out.printf("Enter the coordinates of the %s (%d cells)\n", shipTypes[i].getTypeName(), shipTypes[i].getTotalShipCell());

            while(true) {
                start = scan.next();
                end = scan.next();
                gameShips[i] = new Ship(start, end, shipTypes[i]);

                if (!gameShips[i].isShipIsValid()) {
                    System.out.println("\nError! Wrong ship location! Try again:");
                    continue;
                }

                if(!gameShips[i].isValidShipLength()) {
                    System.out.printf("\nError! Wrong length of the %s! Try again:%n", shipTypes[i].getTypeName());
                    continue;
                }

                if(!placeShip(gameShips[i])) {
                    System.out.println("\nError! You placed it too close to another one. Try again:");
                    continue;
                }

                board.printBoard();
                System.out.println();
                scan.nextLine();
                break;
            }

        }
    }

    public void takeShot(Scanner scan, Board enemyBoard, Board fogEnemyBoard, Ship[] enemyShips) {

        String shotPosition;
        while(true) {
            shotPosition = scan.nextLine();

            if (Board.checkInvalidCoordinates(shotPosition)) {
                System.out.println("\nError. Wrong coordinates! Try again:");
                continue;
            }

            break;
        }

        shotAtPosition(shotPosition, enemyBoard, fogEnemyBoard, enemyShips);
    }

    private void findHitShip(String coord, Board enemyBoard, Ship[] enemyShips){

        ShipType shipType;

        for(int i = 0; i < TOTAL_SHIPS; i++) {
            for(String shipCoord : enemyShips[i].getShipPositions()) {
                if(coord.equals(shipCoord)) {
                    shipType = enemyShips[i].getShipType();
                    shipType.increaseTotalShots();

                    if(shipType.getTotalShots() == shipType.getTotalShipCell()) {
                        enemyBoard.increaseTotalDrankShips();
                        shotDrankShip = true;
                    }

                    return;
                }
            }
        }
    }

    private void shotAtPosition(String coord, Board enemyBoard, Board fogEnemyBoard, Ship[] enemyShips) {

        int shotPositionR = coord.charAt(0) - 64;
        int shotPositionC = Integer.parseInt(coord.substring(1));

        String currentPosition = enemyBoard.getBoardCell(shotPositionR, shotPositionC);
        String cellValue = currentPosition.equals("O") || currentPosition.equals("X") ? "X" : "M";

        if(currentPosition.equals("O")) {
            findHitShip(coord, enemyBoard, enemyShips);
        }

        enemyBoard.setBoardCell(shotPositionR, shotPositionC, cellValue);
        fogEnemyBoard.setBoardCell(shotPositionR, shotPositionC, cellValue);
        //fogBoard.printBoard();

        if (cellValue.equals("X")) {

            if(enemyBoard.getTotalDrankShips() == TOTAL_SHIPS) {
                System.out.println("\nYou sank the last ship. You won. Congratulations!");
                winCondition = true;
                return;
            }

            if(shotDrankShip) {
                System.out.println("\nYou sank a ship! Specify a new target:");
                shotDrankShip = false;
                return;
            }

            System.out.println("\nYou hit a Ship! ");
        } else {
            System.out.println("\nYou missed!");
        }
    }

    private boolean placeShip(Ship ship) {

        /* Check the bounds of the ship and the required space to place it */
        int shipStartCol = ship.getColStart();
        int shipEndCol = ship.getColEnd();

        char shipStartRow = ship.getRowStart();
        char shipEndRow = ship.getRowEnd();

        if(shipStartRow > shipEndRow) {
            char temp = shipEndRow;
            shipEndRow = shipStartRow;
            shipStartRow = temp;
        }

        if(shipStartCol > shipEndCol) {
            int temp = shipEndCol;
            shipEndCol = shipStartCol;
            shipStartCol = temp;
        }

        for(int r = shipStartRow - 65; r <= shipEndRow - 63 && r <= 10 ; r++) {
            for(int c = shipStartCol - 1; c <= shipEndCol + 1 && c <= 10; c++) {
                if(this.board.getBoard()[r][c].equals("O")) {
                    return false;
                }
            }
        }

        for (int r = shipStartRow - 64; r <= shipEndRow - 64; r++) {
            for(int c = shipStartCol; c <= shipEndCol; c++) {
                this.board.setBoardCell(r, c, "O");
            }
        }

        return true;
    }

}
