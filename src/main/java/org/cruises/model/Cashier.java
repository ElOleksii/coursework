package org.cruises.model;

public class Cashier {
    private int cashierId;
    protected String fullName;
    protected String phoneNumber;
    protected String organizationName;
    protected boolean shift;

    public Cashier(String fullName, String phoneNumber, String organizationName, boolean shift) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.organizationName = organizationName;
        this.shift = shift;
    }

    public Cashier() {
    }

    public int getCashierId() {
        return cashierId;
    }

    public void setCashierId(int cashierId) {
        this.cashierId = cashierId;
    }

    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public int getId() {
        return cashierId;
    }

    public void setId(int id) {
        this.cashierId = id;
    }
    public Cashier(int cashierId, String fullName) {
        this.cashierId = cashierId;
        this.fullName = fullName;
    }


    public String toString() {
        return fullName + ", " + phoneNumber + ", " + organizationName;
    }
}