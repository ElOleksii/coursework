package org.cruise.controller.request;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.cruise.service.CruiseService;
import org.cruise.model.Ticket;
import org.cruise.service.FileManagement;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;

public class MostPopularCabinClassController {

    @FXML
    private Label mostPopularCabinClassLabel;


    @FXML
    private Label mostPopularCabinClassText;

    private CruiseService cruiseService;

    @FXML
    private void initialize() {
        List<Ticket> tickets = FileManagement.loadFromFile("data/tickets.json", new TypeReference<List<Ticket>>() {});
        cruiseService = new CruiseService(null, tickets);
        findMostPopularCabinClass();
    }


    private void findMostPopularCabinClass() {
        String mostPopularCabinClass = cruiseService.getMostPopularCabinClass();
        mostPopularCabinClassLabel.setText(mostPopularCabinClass);
        mostPopularCabinClassText.setVisible(true);
    }
}

