package org.cruises.controller.cruise;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.cruises.controller.BaseController;
import org.cruises.model.Cruise;
import org.cruises.model.Route;
import org.cruises.model.Ship;
import org.cruises.service.database.CruiseDAO;
import org.cruises.service.database.RouteDAO;
import org.cruises.service.database.ShipDAO;

import javafx.event.ActionEvent;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class CruiseController extends BaseController<Cruise> {

    @FXML private ComboBox<Route> routeComboBox;
    @FXML private ComboBox<Ship> shipComboBox;
    @FXML private TextField cruiseNameField;
    @FXML private TextField basicPriceField;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private TableView<Cruise> cruiseTable;

    @FXML private TableColumn<Cruise, Integer> cruiseIdCol;
    @FXML private TableColumn<Cruise, String> cruiseNameCol;
    @FXML private TableColumn<Cruise, Double> basicPriceCol;
    @FXML private TableColumn<Cruise, Date> startDateCol;
    @FXML private TableColumn<Cruise, Date> endDateCol;
    @FXML private TableColumn<Cruise, String> routeDescriptionCol;
    @FXML private TableColumn<Cruise, String> shipNameCol;

    @FXML private Button addCruiseButton;
    @FXML private Button cancelEditButton;

    private final CruiseService cruiseService = new CruiseService();
    private final ObservableList<Route> routeList = FXCollections.observableArrayList();
    private final ObservableList<Ship> shipList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        init(cruiseTable);
        initColumns();
        loadCombos();
        cancelEditButton.setVisible(false);
    }

    private void initColumns() {
        cruiseIdCol.setCellValueFactory(cd -> new SimpleIntegerProperty(cd.getValue().getCruiseId()).asObject());
        cruiseNameCol.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getCruiseName()));
        basicPriceCol.setCellValueFactory(cd -> new SimpleDoubleProperty(cd.getValue().getBasicPrice()).asObject());
        startDateCol.setCellValueFactory(cd -> new SimpleObjectProperty<>(cd.getValue().getStartDate()));
        endDateCol.setCellValueFactory(cd -> new SimpleObjectProperty<>(cd.getValue().getEndDate()));
        routeDescriptionCol.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getRouteDescription()));
        shipNameCol.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getShipName()));
    }

    private void loadCombos() {
        try {
            routeList.setAll(cruiseService.getAllRoutes());
            shipList.setAll(cruiseService.getAllShips());
            routeComboBox.setItems(routeList);
            shipComboBox.setItems(shipList);
        } catch (SQLException e) {
            showAlert("Error", e.getMessage());
        }
    }

    private void refreshTable() throws SQLException {
        cruiseTable.setItems(FXCollections.observableArrayList(
                cruiseService.getAllCruises()
        ));
    }

    @FXML
    private void addCruise(ActionEvent event) {
        if (validateInput()) {
            Cruise cruise = buildItemFromFields();
            try {
                if (selectedItem == null) {
                    saveItem(cruise);
                } else {
                    updateItem(cruise);
                }
                clearFields();
                refreshTable();
                addCruiseButton.setText("Add Cruise");
                cancelEditButton.setVisible(false);
            } catch (SQLException e) {
                showAlert("Database Error", e.getMessage());
            }
        }
    }
    @FXML
    private void cancelEdit(javafx.event.ActionEvent event) {
        clearFields();
        addCruiseButton.setText("Add Cruise");
        cancelEditButton.setVisible(false);
        selectedItem = null;
    }




    @Override
    protected List<Cruise> getAllItems() throws SQLException {
        return cruiseService.getAllCruises();
    }

    @Override
    protected boolean saveItem(Cruise item) throws SQLException {
        return cruiseService.saveCruise(item);
    }

    @Override
    protected boolean updateItem(Cruise item) throws SQLException {
        item.setCruiseId(selectedItem.getCruiseId());
        refreshTable();
        return cruiseService.updateCruise(item);
    }

    @Override
    protected void deleteItem(Cruise item) throws SQLException {
        cruiseService.deleteCruise(item);
    }

    @Override
    protected Cruise buildItemFromFields() {
        Cruise cruise = new Cruise();
        cruise.setCruiseName(cruiseNameField.getText());
        cruise.setBasicPrice(Float.parseFloat(basicPriceField.getText()));
        cruise.setRouteId(routeComboBox.getValue().getRouteId());
        cruise.setShipId(shipComboBox.getValue().getShipId());
        cruise.setStartDate(Date.valueOf(startDatePicker.getValue()));
        cruise.setEndDate(Date.valueOf(endDatePicker.getValue()));
        return cruise;
    }

    @Override
    protected void fillFieldsFromItem(Cruise item) {
        cruiseNameField.setText(item.getCruiseName());
        basicPriceField.setText(String.valueOf(item.getBasicPrice()));
        startDatePicker.setValue(item.getStartDate().toLocalDate());
        endDatePicker.setValue(item.getEndDate().toLocalDate());
        routeComboBox.getSelectionModel().select(
                routeList.stream().filter(r -> r.getRouteId() == item.getRouteId()).findFirst().orElse(null)
        );
        shipComboBox.getSelectionModel().select(
                shipList.stream().filter(s -> s.getShipId() == item.getShipId()).findFirst().orElse(null)
        );
        addCruiseButton.setText("Update Cruise");
        cancelEditButton.setVisible(true);
    }

    @Override
    protected boolean validateInput() {
        if (cruiseNameField.getText().isBlank()) {
            showAlert("Validation Error", "Cruise name cannot be empty");
            return false;
        }
        try {
            if (Float.parseFloat(basicPriceField.getText()) <= 0) {
                showAlert("Validation Error", "Price must be positive");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Price must be a number");
            return false;
        }
        if (routeComboBox.getValue() == null || shipComboBox.getValue() == null) {
            showAlert("Validation Error", "Please select route and ship");
            return false;
        }
        if (startDatePicker.getValue() == null || endDatePicker.getValue() == null ||
                !endDatePicker.getValue().isAfter(startDatePicker.getValue())) {
            showAlert("Validation Error", "Invalid dates");
            return false;
        }
        return true;
    }
}
