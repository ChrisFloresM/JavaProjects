package com.cfloresh.battleshipgame;

class Ship {
    private final String start;
    private final String end;
    private boolean shipIsValid;
    private int shipLength;
    private boolean shipIsHorizontal;
    private boolean validShipLength;

    private String[] shipPositions;

    private int colStart;
    private int colEnd;

    private char rowStart;
    private char rowEnd;

    private final ShipType shipType;

    public boolean isShipIsValid() {
        return shipIsValid;
    }

    public String[] getShipPositions() {
        return shipPositions;
    }

    public ShipType getShipType() {
        return shipType;
    }

    public boolean isValidShipLength() {
        return validShipLength;
    }

    public int getColStart() {
        return colStart;
    }

    public int getColEnd() {
        return colEnd;
    }

    public char getRowStart() {
        return rowStart;
    }

    public char getROwEnd() {
        return rowEnd;
    }

    public Ship(String start, String end, ShipType shipType) {
        this.start = start;
        this.end = end;
        this.shipType = shipType;

        if(validityChecks()) {
            calculateShipPositions();
        }

    }

    private boolean validityChecks() {
        shipIsValid = validateShip();
        shipIsHorizontal = rowStart == rowEnd;
        shipLength = calculateShipLength();
        validShipLength = validateShipLength();

        return shipIsValid && validShipLength;
    }

    /* Validate the input ship position. Print error if it's an invalid position */

    private boolean validateShip() {

        /* 1. Validate that each position is valid -> If checkInvalidCoordinates returns true, it means that is invalid */
        if(Board.checkInvalidCoordinates(start) || Board.checkInvalidCoordinates(end)) {
            return false;
        }

        /* 2. Validate that both positions belongs to the same row or the same column */
        calculateColsAndRows();

        return rowStart == rowEnd || colStart == colEnd;
    }

    private int calculateShipLength() {
        /* 1. Get the length of the array - the number of positions the ship will take */
        if(shipIsHorizontal) {
            return Math.abs(colStart - colEnd) + 1;
        }
        return Math.abs(rowStart - rowEnd) + 1;
    }

    private boolean validateShipLength(){
        return shipLength == shipType.getTotalShipCell();
    }

    private void calculateColsAndRows() {
        colStart = Integer.parseInt(start.substring(1));
        colEnd = Integer.parseInt(end.substring(1));

        rowStart = start.charAt(0);
        rowEnd = end.charAt(0);
    }

    private void calculateShipPositions() {
        shipPositions = new String[shipLength];
        shipPositions[0] = start;
        shipPositions[shipLength - 1] = end;

        int arrPos = 1;
        boolean isReversed;

        if (shipIsHorizontal) {
            isReversed = colStart > colEnd;
            for (int i = 1; i < shipLength - 1; i++) {
                int col = isReversed ? colStart - i : colStart + i;
                shipPositions[arrPos++] = String.valueOf(rowEnd) + col;
            }
        } else {
            isReversed = rowStart > rowEnd;
            for (int i = 1; i < shipLength - 1; i++) {
                char row = (char) (isReversed ? rowStart - i : rowStart + i);
                shipPositions[arrPos++] = row + String.valueOf(colEnd);
            }
        }
    }

    public void showShipPositions() {
        for(String p : shipPositions) {
            System.out.print( p + " ");
        }
    }

}

/*
*
*
*     private void calculateShipPositions() {
        shipPositions = new String[shipLength];
        shipPositions[0] = start;
        shipPositions[shipLength - 1] = end;

        int arrPos = 1;
        boolean isReversed;

        if (shipIsHorizontal) {
            isReversed = colStartAsInt > colEndAsInt;
            for (int i = 1; i < shipLength - 1; i++) {
                int col = isReversed ? colStartAsInt - i : colStartAsInt + i;
                shipPositions[arrPos++] = String.valueOf(endPositionChars[0]) + col;
            }
        } else {
            isReversed = startPositionChars[0] > endPositionChars[0];
            for (int i = 1; i < shipLength - 1; i++) {
                char row = (char) (isReversed ? startPositionChars[0] - i : startPositionChars[0] + i);
                shipPositions[arrPos++] = row + String.valueOf(colEndAsInt);
            }
        }
    }
*
* */