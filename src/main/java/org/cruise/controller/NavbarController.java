package org.cruise.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class NavbarController {

    @FXML
    private StackPane dynamicContent; // Container for dynamic views

    @FXML
    private void switchToCashierView() {
        loadView("/fxml/CreateObjectsViews/cashierView.fxml");
    }

    @FXML
    private void switchToPassengerView() {
        loadView("/fxml/CreateObjectsViews/passengerView.fxml");
    }

    @FXML
    private void switchToTicketView() {
        loadView("/fxml/CreateObjectsViews/ticketView.fxml");
    }

    private void loadView(String fxmlFile) {
        try {
            Node view = FXMLLoader.load(getClass().getResource(fxmlFile));
            dynamicContent.getChildren().setAll(view); // Replace current view
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
