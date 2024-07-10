package com.cfloresh.battleshipgame;

public class Board {

    private final String[][] board;
    private static final int SIZE = 11;
    private static final int VALID_COORDINATE_LEN = 2;
    private static int totalDrankShips = 0;

    public String[][] getBoard() {
        return board;
    }

    public void setBoardCell(int r, int c, String cellValue) {
        board[r][c] = cellValue;
    }

    public String getBoardCell(int r, int c) {
        return board[r][c];
    }

    public int getTotalDrankShips(){
        return totalDrankShips;
    }

    public Board(){
        board = new String[SIZE][SIZE];
        for(int i = 0; i < SIZE; i++) {
            for(int j = 0; j < SIZE; j++) {

                if(i == 0 && j == 0) {
                    this.board[i][j] = " ";
                    continue;
                }

                if(i == 0) {
                    this.board[i][j] = String.valueOf(j);
                    continue;
                }

                if(j == 0) {
                    this.board[i][j] = String.valueOf((char) ('@' + i));
                    continue;
                }

                this.board[i][j] = "~";

            }
        }
    }

    public void printBoard() {
        for(String[] rows : this.board) {
            for(String element : rows) {
                System.out.print(element + " ");
            }
            System.out.println();
        }
    }

    public void increaseTotalDrankShips(){
        totalDrankShips++;
    }

    public static boolean checkInvalidCoordinates(String coordinates) {
        String validRows = "ABCDEFGHIJ";
        String validCols = "123456789";

        /* 1. The input is whitespace or empty or contains whitespace */
        if(coordinates == null || coordinates.contains(" ") || coordinates.isEmpty()) {
            return true;
        }

        /* 2. Exceptional case: The input is 3 char long, this means the col position must be 10, otherwise it should be 2 char long */
        if(coordinates.length() != VALID_COORDINATE_LEN && !coordinates.contains("10")) {
            return true;
        }

        /* 3. Validate that rows and columns are valid digits or values */
        return !validRows.contains(String.valueOf(coordinates.charAt(0))) || !validCols.contains(String.valueOf(coordinates.charAt(1)));

        /* 4. Validate that bot positions are on the same column or the same row */
    }
}
