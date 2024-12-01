package org.cruise.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import org.cruise.model.Passenger;
import org.cruise.model.Ticket;
import org.cruise.service.CruiseService;
import org.cruise.service.FileManagement;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RequestController {

    @FXML
    private TextField shipNameTextField;

    @FXML
    private ComboBox<String> requestSelector;

    @FXML
    private TextArea resultsTextArea;

    @FXML
    private HBox requestBox;

    @FXML
    private TableView<Map.Entry<String, Long>> shipTable;

    @FXML
    private TableColumn<Map.Entry<String, Long>, String> shipNameColumn;

    @FXML
    private TableColumn<Map.Entry<String, Long>, Long> passengerCountColumn;

    @FXML
    private StackPane resultPane;

    private CruiseService cruiseService;

    public void initialize() {
        List<Ticket> tickets = FileManagement.loadFromFile("data/tickets.json", new TypeReference<List<Ticket>>() {});
        List<Passenger> passengers = FileManagement.loadFromFile("data/passengers.json", new TypeReference<List<Passenger>>() {});

        this.cruiseService = new CruiseService(passengers, tickets);

        requestSelector.valueProperty().addListener((observable, oldValue, newValue) -> handleRequestChange(newValue));

        // Налаштування колонок таблиці
        shipNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getKey()));
        passengerCountColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getValue()));

        // Початково приховати всі елементи результатів
        hideAllResults();
    }

    private void handleRequestChange(String selectedRequest) {
        hideAllResults();

        if ("Passengers on Ship".equals(selectedRequest)) {
            if (shipNameTextField == null) {
                shipNameTextField = new TextField();
                shipNameTextField.setPromptText("Enter Ship Name");
            }
            if (!requestBox.getChildren().contains(shipNameTextField)) {
                requestBox.getChildren().add(1, shipNameTextField); // Додаємо після ComboBox
            }
        } else {
            requestBox.getChildren().remove(shipNameTextField);
        }
    }

    private void hideAllResults() {
        resultsTextArea.setVisible(false);
        shipTable.setVisible(false);
    }

    @FXML
    private void handleGetRequest() {
        String selectedRequest = requestSelector.getValue();

        if (selectedRequest != null) {
            switch (selectedRequest) {
                case "Total Passengers":
                    resultsTextArea.setVisible(true);
                    shipTable.setVisible(false);
                    int totalPassengers = cruiseService.getTotalPassengerCount();
                    resultsTextArea.setText("Total Passengers: " + totalPassengers);
                    break;

                case "Passengers on Ship":
                    resultsTextArea.setVisible(true);
                    shipTable.setVisible(false);
                    String shipName = shipNameTextField.getText().trim();
                    if (!shipName.isEmpty()) {
                        long passengersOnShip = cruiseService.getPassengerCountForShip(shipName);
                        resultsTextArea.setText("Passengers on " + shipName + ": " + passengersOnShip);
                    } else {
                        resultsTextArea.setText("Please enter the name of the ship.");
                    }
                    break;

                case "Ship List":
                    resultsTextArea.setVisible(false);
                    shipTable.setVisible(true);

                    Map<String, Long> shipPassengerCounts = cruiseService.getShipList();
                    List<Map.Entry<String, Long>> shipEntries = shipPassengerCounts.entrySet().stream().collect(Collectors.toList());
                    shipTable.getItems().setAll(shipEntries);
                    break;

                case "Popular Cabin Class":
                    resultsTextArea.setVisible(true);
                    shipTable.setVisible(false);
                    String popularCabinClass = cruiseService.getMostPopularCabinClass();
                    resultsTextArea.setText("Most Popular Cabin Class: " + popularCabinClass);
                    break;

                case "Total Revenue":
                    resultsTextArea.setVisible(true);
                    shipTable.setVisible(false);
                    double totalRevenue = cruiseService.getTotalRevenue();
                    resultsTextArea.setText("Total Revenue: $" + totalRevenue);
                    break;

                case "Popular Arrival Port":
                    resultsTextArea.setVisible(true);
                    shipTable.setVisible(false);
                    String popularPort = cruiseService.getMostPopularArrivalPort();
                    resultsTextArea.setText("Most Popular Arrival Port: " + popularPort);
                    break;

                default:
                    resultsTextArea.setVisible(true);
                    shipTable.setVisible(false);
                    resultsTextArea.setText("No request selected.");
            }
        } else {
            hideAllResults();
            resultsTextArea.setText("Please select a request.");
        }
    }
}
