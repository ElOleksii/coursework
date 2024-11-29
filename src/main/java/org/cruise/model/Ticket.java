package org.cruise.model;
import java.util.Date;

public class Ticket {
    protected int ticketId;
    protected String shipName;
    protected String departurePort;
    protected String arrivalPort;
    protected Date date;
    protected String cabinClass;
    protected float price;

    public Ticket(int ticketId, String shipName, String departurePort, String arrivalPort, Date date, String cabinClass, float price) {
        this.ticketId = ticketId;
        this.shipName = shipName;
        this.departurePort = departurePort;
        this.arrivalPort = arrivalPort;
        this.date = date;
        this.cabinClass = cabinClass;
        this.price = price;
    }

    public Ticket(){
    }

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public String getDeparturePort() {
        return departurePort;
    }

    public void setDeparturePort(String departurePort) {
        this.departurePort = departurePort;
    }

    public String getArrivalPort() {
        return arrivalPort;
    }

    public void setArrivalPort(String arrivalPort) {
        this.arrivalPort = arrivalPort;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCabinClass() {
        return cabinClass;
    }

    public void setCabinClass(String cabinClass) {
        this.cabinClass = cabinClass;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
