package org.cruise.model;

public class Cashier extends Person {
    protected String organizationName;
    protected boolean shift;

    public Cashier(String fullName, String phoneNumber, String organizationName, boolean shift) {
        super(fullName, phoneNumber);
        this.organizationName = organizationName;
        this.shift = shift;
    }

    public Cashier() {
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public boolean isShift() {
        return shift;
    }

    public void setShift(boolean shift) {
        this.shift = shift;
    }

    public String toString() {
        return "Cashier{" +
                "fullName='" + getFullName() + '\'' +
                ", phoneNumber='" + getPhoneNumber() + '\'' +
                ", organizationName='" + organizationName + '\'' +
                ", shift=" + (shift ? "Day" : "Night") +
                '}';
    }
}
