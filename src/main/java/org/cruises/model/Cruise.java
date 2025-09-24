package org.cruises.model;

import java.sql.Date;

public class Cruise {
    private int cruiseId;
    private int routeId;
    private Date startDate;
    private Date endDate;
    private String cruiseName;
    private float basicPrice;
    private String routeDescription;
    private int shipId;
    private String shipName;




    public Cruise() {}

    public int getCruiseId() {
        return cruiseId;
    }

    public void setCruiseId(int cruiseId) {
        this.cruiseId = cruiseId;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }


    public String getRouteDescription() {
        return routeDescription;
    }

    public void setRouteDescription(String routeDescription) {
        this.routeDescription = routeDescription;
    }

    public String getCruiseName() {
        return cruiseName;
    }

    public void setCruiseName(String cruiseName) {
        this.cruiseName = cruiseName;
    }

    public float getBasicPrice() {
        return basicPrice;
    }

    public void setBasicPrice(float basicPrice) {
        this.basicPrice = basicPrice;
    }

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

    public Cruise(int cruiseId, String cruiseName, Date startDate, int shipId) {
        this.cruiseId = cruiseId;
        this.cruiseName = cruiseName;
        this.startDate = startDate;
        this.shipId = shipId;
    }


    @Override
    public String toString() {
        return cruiseName + " (ID: " + cruiseId + "), " + startDate + " — " + endDate +
                ", Ship: " + shipName + ", Route: " + routeDescription +
                ", Price: $" + basicPrice;
    }

}
