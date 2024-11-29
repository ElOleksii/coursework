package org.cruise.controller.request;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.cruise.model.Ticket;
import org.cruise.service.CruiseService;
import org.cruise.model.Passenger;
import org.cruise.service.FileManagement;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;

public class PassengerCountOnShipController {

    @FXML
    private TextField shipNameField;

    @FXML
    private Label passengerCountLabel;

    @FXML
    private Label passengerCountTitleLabel;

    private CruiseService cruiseService;

    @FXML
    private void initialize() {
        List<Passenger> passengers = FileManagement.loadFromFile("data/passengers.json", new TypeReference<List<Passenger>>() {});
        List<Ticket> tickets = FileManagement.loadFromFile("data/tickets.json", new TypeReference<List<Ticket>>() {}); // Додано завантаження квитків

        cruiseService = new CruiseService(passengers, tickets);
    }

    @FXML
    private void getPassengerCountOnShip() {
        String shipName = shipNameField.getText().trim();
        if (shipName.isEmpty()) {
            passengerCountLabel.setText("Please enter a ship name.");
            return;
        }
        long passengerCount = cruiseService.getPassengerCountForShip(shipName);
        passengerCountTitleLabel.setText("Number of Passengers on " + shipName + ":");
        passengerCountTitleLabel.setVisible(true);
        passengerCountLabel.setText(String.valueOf(passengerCount));
    }
}
