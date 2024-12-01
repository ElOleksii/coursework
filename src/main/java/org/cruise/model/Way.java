package org.cruise.model;

public class Way {
    protected String departurePort;
    protected String arrivalPort;

    public Way(String departurePort, String arrivalPort) {
        this.departurePort = departurePort;
        this.arrivalPort = arrivalPort;
    }

    public String getDeparturePort() {
        return departurePort;
    }

    public Way() {
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
}
