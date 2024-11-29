package org.cruise.service;

import org.cruise.model.Passenger;
import org.cruise.model.Ticket;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class CruiseService {

    private List<Passenger> passengers;
    private List<Ticket> tickets;

    public CruiseService(List<Passenger> passengers, List<Ticket> tickets) {
        this.passengers = passengers;
        this.tickets = tickets;
    }

    public int getTotalPassengerCount() {
        return passengers.size();
    }

    public long getPassengerCountForShip(String shipName) {
        // Перевірка, щоб tickets не був null перед використанням
        if (tickets == null) {
            throw new IllegalStateException("Tickets list is not initialized");
        }
        return tickets.stream()
                .filter(ticket -> shipName.equals(ticket.getShipName()))
                .count();
    }

    public List<String> getShipList() {
        return tickets.stream()
                .map(Ticket::getShipName)
                .distinct()
                .collect(Collectors.toList());
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
                .collect(Collectors.groupingBy(Ticket::getArrivalPort, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Unknown");
    }

    public Optional<Passenger> findPassengerByTicketId(int ticketId) {
        return passengers.stream()
                .filter(passenger -> passenger.getTicketId() == ticketId)
                .findFirst();
    }
}
