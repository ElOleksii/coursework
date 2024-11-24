package org.cruise.model;

public class Passenger extends Person {
    protected String address;
    protected int ticketId;

    public Passenger(String fullName, String phoneNumber, String address, int ticketId) {
        super(fullName, phoneNumber);
        this.address = address;
        this.ticketId = ticketId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }
}
