package org.cruises.model;

public class Ship {
    private int shipId;
    private String shipName;
    private int stateroomCount;


    public Ship() {}

    public int getShipId() {
        return shipId;
    }

    public void setShipId(int shipId) {
        this.shipId = shipId;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }


    public int getStateroomCount() {
        return stateroomCount;
    }

    public void setStateroomCount(int stateroomCount) {
        this.stateroomCount = stateroomCount;
    }

    public String toString() {
        return shipName + " (total staterooms " + stateroomCount + " )";
    }
}
