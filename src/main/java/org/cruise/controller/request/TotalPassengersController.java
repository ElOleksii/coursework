package org.cruise.controller.request;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.cruise.service.CruiseService;
import org.cruise.model.Passenger;
import org.cruise.service.FileManagement;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;

public class TotalPassengersController {

    @FXML
    private Label totalPassengersLabel;

    private CruiseService cruiseService;

    @FXML
    private void initialize() {
        List<Passenger> passengers = FileManagement.loadFromFile("data/passengers.json", new TypeReference<List<Passenger>>() {});
        cruiseService = new CruiseService(passengers, null);
        refreshTotalPassengers();
    }

    @FXML
    private void refreshTotalPassengers() {
        int totalPassengers = cruiseService.getTotalPassengerCount();
        totalPassengersLabel.setText(String.valueOf(totalPassengers));
    }
}
