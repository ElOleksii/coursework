package org.cruise.service;

import org.cruise.model.Cashier;
import org.cruise.model.Passenger;
import org.cruise.model.Ticket;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class CruiseService {

    protected List<Passenger> passengers;
    protected List<Ticket> tickets;
    protected List<Cashier> cashiers;

    public CruiseService(List<Passenger> passengers, List<Ticket> tickets) {
        this.passengers = passengers;
        this.tickets = tickets;
    }

    public int getTotalPassengerCount() {
        return passengers.size();
    }

    public long getPassengerCountForShip(String shipName) {
        if (tickets == null) {
            throw new IllegalStateException("Tickets list is not initialized");
        }
        return tickets.stream()
                .filter(ticket -> shipName.equals(ticket.getShipName()))
                .count();
    }

    public Map<String, Long> getShipList() {
        return tickets.stream()
                .collect(Collectors.groupingBy(Ticket::getShipName, Collectors.counting()));
    }

    public String getMostPopularCabinClass() {
        return tickets.stream()
                .collect(Collectors.groupingBy(Ticket::getCabinClass, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Unknown");
    }

    public double getTotalRevenue() {
        return tickets.stream()
                .mapToDouble(Ticket::getPrice)
                .sum();
    }

    public String getMostPopularArrivalPort() {
        return tickets.stream()
                .collect(Collectors.groupingBy(ticket -> ticket.getWay().getArrivalPort(), Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Unknown");
    }


}
