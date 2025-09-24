package org.cruises.controller.passenger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.cruises.model.Passenger;
import org.cruises.service.database.PassengerDAO;

import java.sql.SQLException;
import java.util.List;

public class PassengerController {

    @FXML private TextField passengerNameField;
    @FXML private TextField passengerPhoneNumberField;
    @FXML private TableView<Passenger> passengerTable;
    @FXML private Button addPassengerButton;
    @FXML private Button cancelEditButton;

    private ObservableList<Passenger> passengerList;
    private PassengerDAO passengerDAO;

    private Passenger selectedPassenger = null;

    @FXML
    public void initialize() {
        passengerDAO = new PassengerDAO();
        passengerList = FXCollections.observableArrayList();

        initTable();
        loadPassengers();
        setupContextMenu();

        cancelEditButton.setVisible(false);
    }

    private void initTable() {
        TableColumn<Passenger, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("passengerId"));

        TableColumn<Passenger, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));

        TableColumn<Passenger, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        passengerTable.getColumns().clear();
        passengerTable.getColumns().addAll(idCol, nameCol, phoneCol);
        passengerTable.setItems(passengerList);
    }

    private void loadPassengers() {
        try {
            List<Passenger> passengers = passengerDAO.getAll();
            passengerList.setAll(passengers);
        } catch (SQLException e) {
            showAlert("Error loading passengers: " + e.getMessage());
        }
    }

    private void setupContextMenu() {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem editItem = new MenuItem("Edit");
        editItem.setOnAction(e -> editSelectedPassenger());

        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(e -> deleteSelectedPassenger());

        contextMenu.getItems().addAll(editItem, deleteItem);

        passengerTable.setContextMenu(contextMenu);

        passengerTable.setRowFactory(tv -> {
            TableRow<Passenger> row = new TableRow<>();
            row.contextMenuProperty().bind(
                    javafx.beans.binding.Bindings.when(row.emptyProperty())
                            .then((ContextMenu)null)
                            .otherwise(contextMenu)
            );
            return row;
        });
    }

    private void editSelectedPassenger() {
        Passenger passenger = passengerTable.getSelectionModel().getSelectedItem();
        if (passenger != null) {
            selectedPassenger = passenger;
            passengerNameField.setText(passenger.getFullName());
            passengerPhoneNumberField.setText(passenger.getPhoneNumber());

            addPassengerButton.setText("Update");
            cancelEditButton.setVisible(true);
        }
    }

    private void deleteSelectedPassenger() {
        Passenger passenger = passengerTable.getSelectionModel().getSelectedItem();
        if (passenger != null) {
            try {
                boolean deleted = passengerDAO.delete(passenger);
                if (deleted) {
                    passengerList.remove(passenger);
                    if (passenger.equals(selectedPassenger)) {
                        clearFields();
                    }
                } else {
                    showAlert("Failed to delete passenger.");
                }
            } catch (SQLException e) {
                showAlert("Error deleting passenger: " + e.getMessage());
            }
        }
    }

    @FXML
    private void addPassenger() {
        String name = passengerNameField.getText();
        String phone = passengerPhoneNumberField.getText();

        if (name.isEmpty() || phone.isEmpty()) {
            showAlert("Please fill in all fields.");
            return;
        }

        if (selectedPassenger != null) {
            selectedPassenger.setFullName(name);
            selectedPassenger.setPhoneNumber(phone);

            try {
                boolean updated = passengerDAO.update(selectedPassenger);
                if (updated) {
                    passengerTable.refresh();
                    clearFields();
                } else {
                    showAlert("Failed to update passenger.");
                }
            } catch (SQLException e) {
                showAlert("Error updating passenger: " + e.getMessage());
            }
        } else {
            Passenger passenger = new Passenger();
            passenger.setFullName(name);
            passenger.setPhoneNumber(phone);

            try {
                boolean saved = passengerDAO.save(passenger);
                if (saved) {
                    passengerList.add(passenger);
                    clearFields();
                } else {
                    showAlert("Failed to save passenger.");
                }
            } catch (SQLException e) {
                showAlert("Error saving passenger: " + e.getMessage());
            }
        }
    }

    @FXML
    private void cancelEdit() {
        clearFields();
    }

    private void clearFields() {
        passengerNameField.clear();
        passengerPhoneNumberField.clear();
        selectedPassenger = null;
        addPassengerButton.setText("Add");
        cancelEditButton.setVisible(false);
        passengerTable.getSelectionModel().clearSelection();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
