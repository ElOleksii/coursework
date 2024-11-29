package org.cruise.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

import java.io.IOException;


public class MenuController {

    @FXML
    protected void initialize() {
        loadView("/fxml/HomeView.fxml");
    }

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

    @FXML
    public void showTotalPassengers() {
        loadView("/fxml/requestsViews/TotalPassengers.fxml");
    }

    public void showPassengersOnShip() {
        loadView("/fxml/requestsViews/PassengersOnShip.fxml");
    }

    public void showShipList() {
        loadView("/fxml/requestsViews/ShipList.fxml");
    }

    public void showPopularCabinClass() {
        loadView("/fxml/requestsViews/MostPopularCabinClass.fxml");
    }

    public void showTotalRevenue() {
        loadView("/fxml/requestsViews/TotalRevenue.fxml");
    }

    public void showPopularArrivalPort() {
        loadView("/fxml/requestsViews/MostPopularPort.fxml");

    }
}
