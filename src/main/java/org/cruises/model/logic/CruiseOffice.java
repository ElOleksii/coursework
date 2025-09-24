package org.cruises.model.logic;

public class CruiseOffice {
    private int officeId;
    private String location;


    public CruiseOffice(int officeId, String location) {
        this.officeId = officeId;
        this.location = location;
    }

    public CruiseOffice() {}

    public int getOfficeId() {
        return officeId;
    }

    public void setOfficeId(int officeId) {
        this.officeId = officeId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
