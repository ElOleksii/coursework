package org.cruises.controller.ticket;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.cruises.model.*;
import org.cruises.service.database.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TicketController {

    @FXML private ComboBox<Passenger> passengerComboBox;
    @FXML private ComboBox<Cruise> cruiseComboBox;
    @FXML private ComboBox<Cashier> cashierComboBox;
    @FXML private TextField ticketPriceField;
    @FXML private TableView<Ticket> ticketTable;
    @FXML private Button addTicketButton;
    @FXML private Button cancelEditButton;

    private ObservableList<Ticket> ticketList;
    private TicketDAO ticketDAO;

    private Map<Integer, Route> routeMap = new HashMap<>();
    private Ticket editingTicket = null;

    @FXML
    public void initialize() {
        ticketDAO = new TicketDAO();
        ticketList = FXCollections.observableArrayList();

        loadRoutes();
        initTable();
        loadTickets();
        loadComboBoxData();

        setupCruiseComboBoxDisplay();
        setupContextMenu();

        cancelEditButton.setVisible(false);
    }

    private void loadRoutes() {
        try {
            List<Route> routes = new RouteDAO().getAll();
            routeMap = routes.stream().collect(Collectors.toMap(Route::getRouteId, r -> r));
        } catch (SQLException e) {
            showAlert("Error loading routes: " + e.getMessage());
        }
    }

    private void initTable() {
        TableColumn<Ticket, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("ticketId"));

        TableColumn<Ticket, Integer> passengerCol = new TableColumn<>("Passenger ID");
        passengerCol.setCellValueFactory(new PropertyValueFactory<>("passengerId"));

        TableColumn<Ticket, Integer> cruiseCol = new TableColumn<>("Cruise ID");
        cruiseCol.setCellValueFactory(new PropertyValueFactory<>("cruiseId"));

        TableColumn<Ticket, Integer> cashierCol = new TableColumn<>("Cashier ID");
        cashierCol.setCellValueFactory(new PropertyValueFactory<>("cashierId"));

        TableColumn<Ticket, Float> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        ticketTable.getColumns().setAll(idCol, passengerCol, cruiseCol, cashierCol, priceCol);
        ticketTable.setItems(ticketList);
    }

    private void loadTickets() {
        try {
            List<Ticket> tickets = ticketDAO.getAll();
            ticketList.setAll(tickets);
        } catch (SQLException e) {
            showAlert("Error loading tickets: " + e.getMessage());
        }
    }

    private void loadComboBoxData() {
        try {
            passengerComboBox.setItems(FXCollections.observableArrayList(new PassengerDAO().getAll()));
            cruiseComboBox.setItems(FXCollections.observableArrayList(new CruiseDAO().getAll()));
            cashierComboBox.setItems(FXCollections.observableArrayList(new CashierDAO().getAll()));
        } catch (SQLException e) {
            showAlert("Error loading combo box data: " + e.getMessage());
        }
    }

    private void setupCruiseComboBoxDisplay() {
        cruiseComboBox.setCellFactory(param -> new ListCell<Cruise>() {
            @Override
            protected void updateItem(Cruise cruise, boolean empty) {
                super.updateItem(cruise, empty);
                setText(empty || cruise == null ? null : "Cruise " + cruise.getCruiseId() + ": " + cruise.getRouteDescription());
            }
        });

        cruiseComboBox.setButtonCell(new ListCell<Cruise>() {
            @Override
            protected void updateItem(Cruise cruise, boolean empty) {
                super.updateItem(cruise, empty);
                setText(empty || cruise == null ? null : "Cruise " + cruise.getCruiseId() + ": " + cruise.getRouteDescription());
            }
        });
    }

    @FXML
    private void addTicket() {
        try {
            Passenger passenger = passengerComboBox.getValue();
            Cruise cruise = cruiseComboBox.getValue();
            Cashier cashier = cashierComboBox.getValue();
            float price = Float.parseFloat(ticketPriceField.getText());

            if (passenger == null || cruise == null || cashier == null) {
                showAlert("Please select Passenger, Cruise, and Cashier.");
                return;
            }

            if (editingTicket != null) {
                editingTicket.setPassengerId(passenger.getPassengerId());
                editingTicket.setCruiseId(cruise.getCruiseId());
                editingTicket.setCashierId(cashier.getCashierId());
                editingTicket.setTotalPrice(price);

                if (ticketDAO.update(editingTicket)) {
                    loadTickets();
                    clearFields();
                } else {
                    showAlert("Failed to update ticket.");
                }

            } else {
                Ticket ticket = new Ticket(0, passenger.getPassengerId(), cruise.getCruiseId(), cashier.getCashierId(), price);
                if (ticketDAO.save(ticket)) {
                    loadTickets();
                    clearFields();
                } else {
                    showAlert("Failed to save ticket.");
                }
            }

        } catch (NumberFormatException e) {
            showAlert("Please enter a valid price.");
        } catch (Exception e) {
            showAlert("Invalid input: " + e.getMessage());
        }
    }

    @FXML
    private void cancelEdit() {
        clearFields();
    }

    private void clearFields() {
        passengerComboBox.getSelectionModel().clearSelection();
        cruiseComboBox.getSelectionModel().clearSelection();
        cashierComboBox.getSelectionModel().clearSelection();
        ticketPriceField.clear();
        editingTicket = null;
        addTicketButton.setText("Add Ticket");
        cancelEditButton.setVisible(false);
        ticketTable.getSelectionModel().clearSelection();
    }

    private void setupContextMenu() {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem editItem = new MenuItem("Edit");
        editItem.setOnAction(event -> {
            Ticket selectedTicket = ticketTable.getSelectionModel().getSelectedItem();
            if (selectedTicket != null) {
                editingTicket = selectedTicket;
                passengerComboBox.setValue(findPassengerById(selectedTicket.getPassengerId()));
                cruiseComboBox.setValue(findCruiseById(selectedTicket.getCruiseId()));
                cashierComboBox.setValue(findCashierById(selectedTicket.getCashierId()));
                ticketPriceField.setText(String.valueOf(selectedTicket.getTotalPrice()));

                addTicketButton.setText("Update Ticket");
                cancelEditButton.setVisible(true);
            }
        });

        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(event -> {
            Ticket selectedTicket = ticketTable.getSelectionModel().getSelectedItem();
            if (selectedTicket != null) {
                try {
                    boolean deleted = ticketDAO.delete(selectedTicket);
                    if (deleted) {
                        loadTickets();
                        clearFields();
                    } else {
                        showAlert("Failed to delete ticket.");
                    }
                } catch (SQLException e) {
                    showAlert("Error deleting ticket: " + e.getMessage());
                }
            }
        });

        contextMenu.getItems().addAll(editItem, deleteItem);
        ticketTable.setContextMenu(contextMenu);
    }


    private Passenger findPassengerById(int id) {
        return passengerComboBox.getItems().stream()
                .filter(p -> p.getPassengerId() == id)
                .findFirst().orElse(null);
    }

    private Cruise findCruiseById(int id) {
        return cruiseComboBox.getItems().stream()
                .filter(c -> c.getCruiseId() == id)
                .findFirst().orElse(null);
    }

    private Cashier findCashierById(int id) {
        return cashierComboBox.getItems().stream()
                .filter(c -> c.getCashierId() == id)
                .findFirst().orElse(null);
    }

    private void showAlert(String message) {
        showAlert(message, Alert.AlertType.ERROR);
    }

    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle("Alert");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
