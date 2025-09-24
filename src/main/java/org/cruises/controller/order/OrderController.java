package org.cruises.controller.order;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.cruises.model.Order;
import org.cruises.service.database.OrderDAO;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class OrderController {

    @FXML private TextField passengerIdField;
    @FXML private TextField statusField;
    @FXML private TableView<Order> orderTable;
    @FXML private Button addOrderButton;
    @FXML private Button cancelEditButton;

    private final ObservableList<Order> orderList = FXCollections.observableArrayList();
    private final OrderDAO orderDAO = new OrderDAO();
    private Order selectedOrder = null;

    @FXML
    public void initialize() {
        initTable();
        loadOrders();
        setupContextMenu();
        cancelEditButton.setVisible(false);
    }

    private void initTable() {
        TableColumn<Order, Integer> idCol = new TableColumn<>("Order ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("orderId"));

        TableColumn<Order, Integer> passengerCol = new TableColumn<>("Passenger ID");
        passengerCol.setCellValueFactory(new PropertyValueFactory<>("passengerId"));

        TableColumn<Order, LocalDateTime> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("orderDateTime"));

        TableColumn<Order, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("orderStatus"));

        orderTable.getColumns().setAll(idCol, passengerCol, dateCol, statusCol);
        orderTable.setItems(orderList);
    }

    private void loadOrders() {
        try {
            List<Order> orders = orderDAO.getAll();
            orderList.setAll(orders);
        } catch (SQLException e) {
            showAlert("Error loading orders: " + e.getMessage());
        }
    }

    private void setupContextMenu() {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem editItem = new MenuItem("Edit");
        editItem.setOnAction(e -> editSelectedOrder());

        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(e -> deleteSelectedOrder());

        contextMenu.getItems().addAll(editItem, deleteItem);

        orderTable.setRowFactory(tv -> {
            TableRow<Order> row = new TableRow<>();
            row.contextMenuProperty().bind(
                    javafx.beans.binding.Bindings.when(row.emptyProperty())
                            .then((ContextMenu) null)
                            .otherwise(contextMenu)
            );
            return row;
        });
    }

    private void editSelectedOrder() {
        Order order = orderTable.getSelectionModel().getSelectedItem();
        if (order != null) {
            selectedOrder = order;
            passengerIdField.setText(String.valueOf(order.getPassengerId()));
            statusField.setText(order.getOrderStatus());
            addOrderButton.setText("Update");
            cancelEditButton.setVisible(true);
        }
    }

    private void deleteSelectedOrder() {
        Order order = orderTable.getSelectionModel().getSelectedItem();
        if (order != null) {
            try {
                boolean deleted = orderDAO.delete(order);
                if (deleted) {
                    orderList.remove(order);
                    if (order.equals(selectedOrder)) {
                        clearFields();
                    }
                } else {
                    showAlert("Failed to delete order.");
                }
            } catch (SQLException e) {
                showAlert("Error deleting order: " + e.getMessage());
            }
        }
    }

    @FXML
    private void addOrder() {
        String passengerIdText = passengerIdField.getText();
        String status = statusField.getText();

        if (passengerIdText.isEmpty() || status.isEmpty()) {
            showAlert("Please fill in all fields.");
            return;
        }

        try {
            int passengerId = Integer.parseInt(passengerIdText);

            if (selectedOrder != null) {
                selectedOrder.setPassengerId(passengerId);
                selectedOrder.setOrderStatus(status);

                boolean updated = orderDAO.update(selectedOrder);
                if (updated) {
                    orderTable.refresh();
                    clearFields();
                } else {
                    showAlert("Failed to update order.");
                }
            } else {
                Order order = new Order();
                order.setPassengerId(passengerId);
                order.setOrderStatus(status);
                order.setOrderDateTime(LocalDateTime.now());

                boolean saved = orderDAO.save(order);
                if (saved) {
                    orderList.add(order);
                    clearFields();
                } else {
                    showAlert("Failed to save order.");
                }
            }
        } catch (NumberFormatException e) {
            showAlert("Passenger ID must be a number.");
        } catch (SQLException e) {
            showAlert("Database error: " + e.getMessage());
        }
    }

    @FXML
    private void cancelEdit() {
        clearFields();
    }

    private void clearFields() {
        passengerIdField.clear();
        statusField.clear();
        selectedOrder = null;
        addOrderButton.setText("Add Order");
        cancelEditButton.setVisible(false);
        orderTable.getSelectionModel().clearSelection();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
