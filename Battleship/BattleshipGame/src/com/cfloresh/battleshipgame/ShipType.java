package com.cfloresh.battleshipgame;

public enum ShipType {
    AIRCRAFT_CARRIER(5, "Aircraft Carrier"),
    BATTLESHIP(4, "Battleship"),
    SUBMARINE(3, "Submarine"),
    CRUISER(3, "Cruiser"),
    DESTROYER(2, "Destroyer");

    private final int totalShipCells;
    private final String typeName;
    private int totalShots;

    ShipType(int totalShipCells, String typeName) {
        this.totalShipCells = totalShipCells;
        this.typeName = typeName;
        this.totalShots = 0;
    }

    public int getTotalShipCell() {
        return this.totalShipCells;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public void increaseTotalShots() {
        totalShots++;
    }

    public int getTotalShots() {
        return totalShots;
    }
}