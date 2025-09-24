package org.cruises.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.cruises.model.*;
import org.cruises.model.logic.Calc;
import org.cruises.model.logic.CruiseOffice;
import org.cruises.model.logic.MapStateroom;
import org.cruises.model.logic.Register;
import org.cruises.service.database.*;

import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;

public class BookingController {

    @FXML
    private ComboBox<Passenger> passengerComboBox;
    @FXML
    private ComboBox<Cruise> cruiseComboBox;
    @FXML
    private ComboBox<Stateroom> cabinComboBox;
    @FXML
    private ComboBox<Cashier> cashierComboBox;
    @FXML
    private Label totalPriceLabel;

    private final Calc calc = new Calc();
    private final MapStateroom mapStateroom = new MapStateroom();
    private final Register register = new Register();
    private final CruiseOffice cruiseOffice = new CruiseOffice(1, "New York City, Smth Street, 15");

    private final PassengerDAO passengerDAO = new PassengerDAO();
    private final CruiseDAO cruiseDAO = new CruiseDAO();
    private final CashierDAO cashierDAO = new CashierDAO();

    @FXML
    public void initialize() {
        loadPassengers();
        loadCruises();
        loadCashiers();


        cruiseComboBox.setOnAction(e -> loadAvailableCabins());
        cabinComboBox.setOnAction(e -> updateTotalPrice());
    }

    private void loadPassengers() {
        try {
            List<Passenger> passengers = passengerDAO.getAll();
            passengerComboBox.setItems(FXCollections.observableArrayList(passengers));
        } catch (SQLException e) {
            showError("Database Error", "Cannot load passengers: " + e.getMessage());
        }
    }

    private void loadCruises() {
        try {
            List<Cruise> cruises = cruiseDAO.getAll();
            cruiseComboBox.setItems(FXCollections.observableArrayList(cruises));
        } catch (SQLException e) {
            showError("Database Error", "Cannot load cruises: " + e.getMessage());
        }
    }

    private void loadCashiers() {
        try {
            List<Cashier> cashiers = cashierDAO.getAll();
            cashierComboBox.setItems(FXCollections.observableArrayList(cashiers));
        } catch (SQLException e) {
            showError("Database Error", "Cannot load cashiers: " + e.getMessage());
        }
    }

    private void loadAvailableCabins() {
        Cruise selectedCruise = cruiseComboBox.getSelectionModel().getSelectedItem();
        if (selectedCruise != null) {
            try {
                cabinComboBox.setItems(FXCollections.observableArrayList(
                        mapStateroom.findAvailableByShip(selectedCruise.getShipId())
                ));
            } catch (SQLException e) {
                showError("Database Error", "Cannot load staterooms: " + e.getMessage());
            }
        }
    }

    private void updateTotalPrice() {
        Stateroom selectedCabin = cabinComboBox.getValue();
        Cruise selectedCruise = cruiseComboBox.getValue();
        if (selectedCabin != null && selectedCruise != null) {
            double price = calc.calculateTotalPrice(selectedCabin, selectedCruise);
            totalPriceLabel.setText(String.format("$%.2f", price));
        }
    }

    @FXML
    private void handleBookTicket() {
        Passenger passenger = passengerComboBox.getValue();
        Cruise cruise = cruiseComboBox.getValue();
        Stateroom cabin = cabinComboBox.getValue();
        Cashier cashier = cashierComboBox.getValue();

        if (passenger == null || cruise == null || cabin == null || cashier == null) {
            showError("Validation Error", "Please select all fields before booking.");
            return;
        }

        try {
            mapStateroom.markReserved(cabin.getStateroomId());
            register.createTicket(passenger.getPassengerId(), cruise.getCruiseId(), cabin, cashier, cruise);

            String path = Paths.get("tickets", "ticket_" + passenger.getPassengerId() + "_" + cruise.getCruiseId() + ".pdf")
                    .toAbsolutePath().toString();
            register.generateTicketPDF(passenger, cruise, cabin, path);

            showInfo("Success", "Ticket booked successfully! PDF saved:\n" + path);

            cabinComboBox.getItems().remove(cabin);
            totalPriceLabel.setText("");
        } catch (Exception e) {
            showError("Error", "Failed to book ticket: " + e.getMessage());
        }
    }


    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showInfo(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
