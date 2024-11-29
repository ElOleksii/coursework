package org.cruise.controller.request;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.cruise.service.CruiseService;
import org.cruise.model.Ticket;
import org.cruise.service.FileManagement;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;

public class MostPopularPortController {

    @FXML
    private Label mostPopularPortLabel;


    private CruiseService cruiseService;

    @FXML
    private void initialize() {
        List<Ticket> tickets = FileManagement.loadFromFile("data/tickets.json", new TypeReference<List<Ticket>>() {});
        cruiseService = new CruiseService(null, tickets);
        findMostPopularPort();
    }

    @FXML
    private void findMostPopularPort() {
        String mostPopularPort = cruiseService.getMostPopularArrivalPort();
        mostPopularPortLabel.setText(mostPopularPort);
    }
}
