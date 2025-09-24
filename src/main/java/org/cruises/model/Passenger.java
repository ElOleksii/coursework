package org.cruises.model;

public class Passenger {
    protected int passengerId;
    protected String fullName;
    protected String phoneNumber;

    public int getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(int passengerId) {
        this.passengerId = passengerId;
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

    public Passenger(String fullName, String phoneNumber, String address, int ticketId) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
    }

    public int getId() {
        return passengerId;
    }

    public void setId(int id) {
        this.passengerId = id;
    }


    public Passenger() {}

    public Passenger(int passengerId, String fullName, String phoneNumber) {
        this.passengerId = passengerId;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
    }



    @Override
    public String toString() {
        return fullName;
    }
}
