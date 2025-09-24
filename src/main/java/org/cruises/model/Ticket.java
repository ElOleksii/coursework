package org.cruises.model;

public class Ticket {
   private int ticketId;
   private int passengerId;
   private int cruiseId;
   private int cashierId;
   private float totalPrice;

    public Ticket() {

    }


    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public int getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(int passengerId) {
        this.passengerId = passengerId;
    }

    public int getCruiseId() {
        return cruiseId;
    }

    public void setCruiseId(int cruiseId) {
        this.cruiseId = cruiseId;
    }

    public int getCashierId() {
        return cashierId;
    }

    public void setCashierId(int cashierId) {
        this.cashierId = cashierId;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Ticket(int ticketId, int passengerId, int cruiseId, int cashierId, float price) {
        this.ticketId = ticketId;
        this.passengerId = passengerId;
        this.cruiseId = cruiseId;
        this.cashierId = cashierId;
        this.totalPrice = price;
    }

}
