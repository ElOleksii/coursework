package org.cruises.controller.stateroom;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.cruises.model.Ship;
import org.cruises.model.Stateroom;
import org.cruises.service.database.ShipDAO;
import org.cruises.service.database.StateroomDAO;

import java.sql.SQLException;
import java.util.List;

public class StateroomController {

    @FXML
    private ComboBox<String> stateroomClassComboBox;

    @FXML
    private TextField capacityField;

    @FXML
    private TextField priceField;

    @FXML
    private ComboBox<Ship> shipComboBox;

    @FXML
    private CheckBox isReservedCheckBox;

    @FXML
    private TableView<Stateroom> stateroomTable;

    @FXML
    private TableColumn<Stateroom, Integer> stateroomIdCol;

    @FXML
    private TableColumn<Stateroom, String> stateroomClassCol;

    @FXML
    private TableColumn<Stateroom, Integer> capacityCol;

    @FXML
    private TableColumn<Stateroom, Integer> priceCol;

    @FXML
    private TableColumn<Stateroom, Integer> shipIdCol;

    @FXML
    private Button addStateroomButton;

    @FXML
    private TableColumn<Stateroom, Boolean> isReservedCol;


    @FXML
    private Button cancelEditButton;

    private Stateroom selectedStateroom = null;

    private final StateroomDAO stateroomDAO = new StateroomDAO();
    private final ShipDAO shipDAO = new ShipDAO();
    private final ObservableList<Stateroom> stateroomList = FXCollections.observableArrayList();
    private final ObservableList<Ship> shipList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Використовуємо той же стиль що і в інших контролерах
        stateroomIdCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getStateroomId()).asObject());
        stateroomClassCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStateroomClass()));
        capacityCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getCapacity()).asObject());
        priceCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPrice()).asObject());
        shipIdCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getShipId()).asObject());
        isReservedCol.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().isReserved()).asObject());
        isReservedCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : (item ? "Yes" : "No"));
            }
        });


        initStateroomClassComboBox();
        loadShips();
        loadStaterooms();
        setupContextMenu();

        cancelEditButton.setVisible(false);
    }

    private void setupContextMenu() {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem editItem = new MenuItem("Edit");
        editItem.setOnAction(e -> {
            Stateroom selected = stateroomTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                selectedStateroom = selected;
                stateroomClassComboBox.setValue(selected.getStateroomClass());
                capacityField.setText(String.valueOf(selected.getCapacity()));
                priceField.setText(String.valueOf(selected.getPrice()));
                isReservedCheckBox.setSelected(selected.isReserved());

                shipComboBox.getSelectionModel().select(
                        shipList.stream()
                                .filter(s -> s.getShipId() == selected.getShipId())
                                .findFirst()
                                .orElse(null)
                );

                addStateroomButton.setText("Update Stateroom");
                cancelEditButton.setVisible(true);
            }
        });

        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(e -> {
            Stateroom selected = stateroomTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                try {
                    stateroomDAO.delete(selected);
                    loadStaterooms();
                    clearFields();
                } catch (SQLException ex) {
                    showAlert("Error", "Failed to delete stateroom: " + ex.getMessage());
                }
            }
        });

        contextMenu.getItems().addAll(editItem, deleteItem);
        stateroomTable.setContextMenu(contextMenu);
    }

    private void initStateroomClassComboBox() {
        stateroomClassComboBox.getItems().addAll("Econom", "Standart", "Business");
    }

    private void loadShips() {
        try {
            List<Ship> ships = shipDAO.getAll();
            shipList.setAll(ships);
            shipComboBox.setItems(shipList);
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to load ships: " + e.getMessage());
        }
    }

    private void loadStaterooms() {
        try {
            List<Stateroom> staterooms = stateroomDAO.getAll();
            stateroomList.setAll(staterooms);
            stateroomTable.setItems(stateroomList);
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to load staterooms: " + e.getMessage());
        }
    }

    @FXML
    public void addStateroom() {
        try {
            String roomClass = stateroomClassComboBox.getValue();
            String capacityText = capacityField.getText().trim();
            String priceText = priceField.getText().trim();
            Ship selectedShip = shipComboBox.getValue();

            if (roomClass == null || capacityText.isEmpty() || priceText.isEmpty() || selectedShip == null) {
                showAlert("Input Error", "All fields must be filled and Ship selected.");
                return;
            }

            int capacity = Integer.parseInt(capacityText);
            int price = Integer.parseInt(priceText);

            if (selectedStateroom != null) {
                // Оновлення існуючої каюти
                selectedStateroom.setStateroomClass(roomClass);
                selectedStateroom.setCapacity(capacity);
                selectedStateroom.setPrice(price);
                selectedStateroom.setShipId(selectedShip.getShipId());
                selectedStateroom.setReserved(isReservedCheckBox.isSelected());

                if (stateroomDAO.update(selectedStateroom)) {
                    loadStaterooms();
                    clearFields();
                } else {
                    showAlert("Error", "Failed to update stateroom.");
                }
            } else {
                // Додавання нової каюти
                Stateroom stateroom = new Stateroom();
                stateroom.setStateroomClass(roomClass);
                stateroom.setCapacity(capacity);
                stateroom.setPrice(price);
                stateroom.setShipId(selectedShip.getShipId());
                stateroom.setReserved(false);

                if (stateroomDAO.save(stateroom)) {
                    loadStaterooms();
                    clearFields();
                } else {
                    showAlert("Error", "Failed to add stateroom.");
                }
            }

        } catch (NumberFormatException e) {
            showAlert("Input Error", "Capacity and Price must be valid numbers.");
        } catch (Exception e) {
            showAlert("Error", "Invalid input: " + e.getMessage());
        }
    }

    @FXML
    public void cancelEdit() {
        clearFields();
    }

    private void clearFields() {
        stateroomClassComboBox.getSelectionModel().clearSelection();
        capacityField.clear();
        priceField.clear();
        shipComboBox.getSelectionModel().clearSelection();
        isReservedCheckBox.setSelected(false);
        selectedStateroom = null;
        addStateroomButton.setText("Add Stateroom");
        cancelEditButton.setVisible(false);
        stateroomTable.getSelectionModel().clearSelection();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }
}