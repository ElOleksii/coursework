package org.cruise.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeViewController {

    @FXML
    private void onCreatePassenger(ActionEvent event) {
        openNewWindow("/fxml/CreateObjectsViews/createPassenger.fxml", "Create Passenger");
    }

    @FXML
    private void onCreateTicket(ActionEvent event) {
        openNewWindow("/fxml/CreateObjectsViews/createTicket.fxml", "Create Ticket");
    }

    @FXML
    private void onViewReports(ActionEvent event) {
        openNewWindow("/fxml/ReportsViews/viewReports.fxml", "View Reports");
    }

    @FXML
    private void onSettings(ActionEvent event) {
        openNewWindow("/fxml/SettingsViews/settings.fxml", "Settings");
    }

    private void openNewWindow(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
