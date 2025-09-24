package org.cruises.controller.ship;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.cruises.model.Ship;
import org.cruises.service.database.ShipDAO;

import java.sql.SQLException;
import java.util.List;

public class ShipController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField stateroomCountField;

    @FXML
    private TableView<Ship> shipTable;

    @FXML
    private TableColumn<Ship, Integer> shipIdCol;

    @FXML
    private TableColumn<Ship, String> nameCol;

    @FXML
    private TableColumn<Ship, Integer> stateroomCountCol;

    @FXML
    private Button addShipButton;

    @FXML
    private Button cancelEditButton;

    private Ship selectedShip = null;

    private final ShipDAO shipDAO = new ShipDAO();
    private final ObservableList<Ship> shipList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        shipIdCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getShipId()).asObject());
        nameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getShipName()));
        stateroomCountCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getStateroomCount()).asObject());

        loadShips();
        setupContextMenu();

        cancelEditButton.setVisible(false);
    }

    private void setupContextMenu() {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem editItem = new MenuItem("Edit");
        editItem.setOnAction(e -> {
            Ship selected = shipTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                selectedShip = selected;
                nameField.setText(selected.getShipName());
                stateroomCountField.setText(String.valueOf(selected.getStateroomCount()));

                addShipButton.setText("Update Ship");
                cancelEditButton.setVisible(true);
            }
        });

        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(e -> {
            Ship selected = shipTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                try {
                    shipDAO.delete(selected);
                    loadShips();
                    clearFields();
                } catch (SQLException ex) {
                    showAlert("Error", "Failed to delete ship: " + ex.getMessage());
                }
            }
        });

        contextMenu.getItems().addAll(editItem, deleteItem);
        shipTable.setContextMenu(contextMenu);
    }

    private void loadShips() {
        try {
            List<Ship> ships = shipDAO.getAll();
            shipList.setAll(ships);
            shipTable.setItems(shipList);
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to load ships: " + e.getMessage());
        }
    }

    @FXML
    public void addShip() {
        try {
            String name = nameField.getText().trim();
            String stateroomText = stateroomCountField.getText().trim();

            if (name.isEmpty() || stateroomText.isEmpty()) {
                showAlert("Input Error", "All fields must be filled.");
                return;
            }

            int stateroomCount = Integer.parseInt(stateroomText);

            if (selectedShip != null) {
                selectedShip.setShipName(name);
                selectedShip.setStateroomCount(stateroomCount);

                if (shipDAO.update(selectedShip)) {
                    loadShips();
                    clearFields();
                } else {
                    showAlert("Error", "Failed to update ship.");
                }
            } else {
                Ship ship = new Ship();
                ship.setShipName(name);
                ship.setStateroomCount(stateroomCount);

                if (shipDAO.save(ship)) {
                    loadShips();
                    clearFields();
                } else {
                    showAlert("Error", "Failed to add ship.");
                }
            }

        } catch (NumberFormatException e) {
            showAlert("Input Error", "Stateroom Count must be a valid number.");
        } catch (Exception e) {
            showAlert("Error", "Invalid input: " + e.getMessage());
        }
    }

    @FXML
    public void cancelEdit() {
        clearFields();
    }

    private void clearFields() {
        nameField.clear();
        stateroomCountField.clear();
        selectedShip = null;
        addShipButton.setText("Add Ship");
        cancelEditButton.setVisible(false);
        shipTable.getSelectionModel().clearSelection();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }
}