package org.cruises.model;

public class Route {
    private int routeId;
    private String departurePort;
    private String arrivalPort;

    public Route() {}

    public Route(int routeId, String departurePort, String arrivalPort) {
        this.routeId = routeId;
        this.departurePort = departurePort;
        this.arrivalPort = arrivalPort;
    }

    public Route(String departurePort, String arrivalPort) {
        this.departurePort = departurePort;
        this.arrivalPort = arrivalPort;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
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

    @Override
    public String toString() {
        return getDeparturePort() + " - " + getArrivalPort();
    }
}
