package org.cruise.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class RequestController {

    @FXML
    private ComboBox<String> requestSelector;

    @FXML
    private TextArea resultsTextArea;

    @FXML
    private void handleGetRequest() {
        String selectedRequest = requestSelector.getValue();

        if (selectedRequest != null) {
            switch (selectedRequest) {
                case "Total Passengers":
                    resultsTextArea.setText("Displaying total passengers...");
                    break;
                case "Passengers on Ship":
                    resultsTextArea.setText("Displaying passengers on ship...");
                    break;
                case "Ship List":
                    resultsTextArea.setText("Displaying ship list...");
                    break;
                case "Popular Cabin Class":
                    resultsTextArea.setText("Displaying popular cabin class...");
                    break;
                case "Total Revenue":
                    resultsTextArea.setText("Displaying total revenue...");
                    break;
                case "Popular Arrival Port":
                    resultsTextArea.setText("Displaying popular arrival port...");
                    break;
                default:
                    resultsTextArea.setText("No request selected.");
            }
        } else {
            resultsTextArea.setText("Please select a request.");
        }
    }
}
