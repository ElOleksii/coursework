package org.cruises.controller.route;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.cruises.model.Route;
import org.cruises.service.database.RouteDAO;

import java.sql.SQLException;
import java.util.List;

public class RouteController {

    @FXML
    private TextField departureField;

    @FXML
    private TextField arrivalField;

    @FXML
    private TableView<Route> routeTable;

    @FXML
    private TableColumn<Route, Integer> idColumn;

    @FXML
    private TableColumn<Route, String> departureColumn;

    @FXML
    private TableColumn<Route, String> arrivalColumn;

    @FXML
    private Button addRouteButton;

    @FXML
    private Button cancelEditButton;

    private Route selectedRoute = null;

    private final RouteDAO routeDAO = new RouteDAO();

    private final ObservableList<Route> routeList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getRouteId()).asObject());
        departureColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDeparturePort()));
        arrivalColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getArrivalPort()));

        loadRoutes();
        setupContextMenu();

        cancelEditButton.setVisible(false);
    }

    private void setupContextMenu() {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem editItem = new MenuItem("Edit");
        editItem.setOnAction(e -> {
            Route selected = routeTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                selectedRoute = selected;
                departureField.setText(selected.getDeparturePort());
                arrivalField.setText(selected.getArrivalPort());

                addRouteButton.setText("Update Route");
                cancelEditButton.setVisible(true);
            }
        });

        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(e -> {
            Route selected = routeTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                try {
                    routeDAO.delete(selected);
                    loadRoutes();
                    clearFields();
                } catch (SQLException ex) {
                    showAlert("Error", "Failed to delete route: " + ex.getMessage());
                }
            }
        });

        contextMenu.getItems().addAll(editItem, deleteItem);
        routeTable.setContextMenu(contextMenu);
    }

    private void loadRoutes() {
        try {
            List<Route> routes = routeDAO.getAll();
            routeList.setAll(routes);
            routeTable.setItems(routeList);
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to load routes: " + e.getMessage());
        }
    }

    @FXML
    public void addRoute() {
        try {
            String departure = departureField.getText().trim();
            String arrival = arrivalField.getText().trim();

            if (departure.isEmpty() || arrival.isEmpty()) {
                showAlert("Input Error", "Please enter both departure and arrival ports.");
                return;
            }

            if (selectedRoute != null) {
                selectedRoute.setDeparturePort(departure);
                selectedRoute.setArrivalPort(arrival);

                if (routeDAO.update(selectedRoute)) {
                    loadRoutes();
                    clearFields();
                } else {
                    showAlert("Error", "Failed to update route.");
                }
            } else {
                // Додавання нового маршруту
                Route route = new Route();
                route.setDeparturePort(departure);
                route.setArrivalPort(arrival);

                if (routeDAO.save(route)) {
                    loadRoutes();
                    clearFields();
                } else {
                    showAlert("Error", "Failed to add route.");
                }
            }

        } catch (Exception e) {
            showAlert("Error", "Invalid input: " + e.getMessage());
        }
    }

    @FXML
    public void cancelEdit() {
        clearFields();
    }

    private void clearFields() {
        departureField.clear();
        arrivalField.clear();
        selectedRoute = null;
        addRouteButton.setText("Add Route");
        cancelEditButton.setVisible(false);
        routeTable.getSelectionModel().clearSelection();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }
}