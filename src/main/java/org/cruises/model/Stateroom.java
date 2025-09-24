package org.cruises.model;

public class Stateroom {
    private int stateroomId;
    private String stateroomClass;
    private int capacity;
    private int price;
    private int shipId;
    private boolean isReserved;

    public Stateroom() {}

    public int getStateroomId() {
        return stateroomId;
    }

    public void setStateroomId(int stateroomId) {
        this.stateroomId = stateroomId;
    }

    public String getStateroomClass() {
        return stateroomClass;
    }

    public void setStateroomClass(String stateroomClass) {
        this.stateroomClass = stateroomClass;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getShipId() {
        return shipId;
    }

    public void setShipId(int shipId) {
        this.shipId = shipId;
    }

    public boolean isReserved() {
        return isReserved;
    }

    public void setReserved(boolean reserved) {
        isReserved = reserved;
    }

    public Stateroom(int stateroomId, String stateroomClass, int price, int shipId, boolean isReserved) {
        this.stateroomId = stateroomId;
        this.stateroomClass = stateroomClass;
        this.price = price;
        this.shipId = shipId;
        this.isReserved = isReserved;
    }


    @Override
    public String toString() {
        return "Stateroom " + stateroomId + " [" + stateroomClass + ", " + capacity + " ppl, $" + price +
                ", Reserved: " + isReserved + "]";
    }

}
