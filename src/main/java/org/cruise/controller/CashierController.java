package org.cruise.controller;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.cruise.model.Cashier;

import java.io.IOException;
import java.util.Stack;

public class CashierController {

    // FXML fields for input
    @FXML
    private TextField cashierNameField;
    @FXML
    private TextField cashierPhoneNumberField;
    @FXML
    private TextField cashierOrganizationField;
    @FXML
    private CheckBox cashierShiftCheckBox;

    @FXML
    private TableView<Cashier> cashierTable;

    public static ObservableList<Cashier> cashierList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        TableColumn<Cashier, String> fullNameColumn = new TableColumn<>("Full Name");
        fullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        fullNameColumn.setPrefWidth(200);

        TableColumn<Cashier, String> phoneNumberColumn = new TableColumn<>("Phone Number");
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        phoneNumberColumn.setPrefWidth(200);

        TableColumn<Cashier, String> organizationColumn = new TableColumn<>("Organization");
        organizationColumn.setCellValueFactory(new PropertyValueFactory<>("organizationName"));
        organizationColumn.setPrefWidth(200);

        TableColumn<Cashier, Boolean> shiftColumn = new TableColumn<>("Shift");
        shiftColumn.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().isShift()).asObject());
        shiftColumn.setPrefWidth(200);

        // Add columns to the table
        cashierTable.getColumns().addAll(fullNameColumn, phoneNumberColumn, organizationColumn, shiftColumn);

        // Bind the observable list to the table
        cashierTable.setItems(cashierList);

        ContextMenu contextMenu = new ContextMenu();

        // Create the delete menu item
        MenuItem deleteItem = new MenuItem("Delete Cashier");
        deleteItem.setOnAction(event -> deleteCashier());

        MenuItem editItem = new MenuItem("Edit Cashier");
        editItem.setOnAction(event -> openEditWindow());

        // Add delete menu item to the context menu
        contextMenu.getItems().add(editItem);
        contextMenu.getItems().add(deleteItem);

        // Set the context menu on the TableView
        cashierTable.setContextMenu(contextMenu);

        // Add listener for right-click (context menu)
        cashierTable.setRowFactory(tv -> {
            TableRow<Cashier> row = new TableRow<>();
            row.setOnContextMenuRequested(event -> {
                if (!row.isEmpty()) {
                    contextMenu.show(row, event.getScreenX(), event.getScreenY());
                }
            });
            return row;
        });

    }

    private void openEditWindow() {
        // Get the selected cashier
        Cashier selectedCashier = cashierTable.getSelectionModel().getSelectedItem();
        if (selectedCashier != null) {
            try {
                // Load the EditCashier.fxml file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CreateObjectsViews/editCashier.fxml"));
                Stage stage = new Stage();
                stage.setTitle("Edit Cashier");

                // Pass the selected cashier to the EditCashierController
                EditCashierController controller = new EditCashierController(selectedCashier);
                loader.setController(controller);
                stage.setScene(new Scene(loader.load()));
                stage.showAndWait();

                // After editing, update the table if necessary (it will be done automatically)
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void deleteCashier() {
        // Get the selected cashier
        Cashier selectedCashier = cashierTable.getSelectionModel().getSelectedItem();
        if (selectedCashier != null) {
            // Remove from the global list
            cashierList.remove(selectedCashier);
        }
    }
    @FXML
    private void addCashier() {
        // Get values from input fields using their fx:id
        String fullName = cashierNameField.getText().trim();  // Using cashierNameField
        String phoneNumber = cashierPhoneNumberField.getText().trim();  // Using cashierPhoneNumberField
        String organizationName = cashierOrganizationField.getText().trim();  // Using cashierOrganizationField
        boolean shift = cashierShiftCheckBox.isSelected();  // Using cashierShiftCheckBox

        // Validate input and add cashier
        if (fullName.isEmpty() || phoneNumber.isEmpty() || organizationName.isEmpty()) {
            System.out.println("Error: All fields are required.");
            return;
        }

        Cashier newCashier = new Cashier(fullName, phoneNumber, organizationName, shift);
        cashierList.add(newCashier);

        // Clear input fields after adding
        cashierNameField.clear();
        cashierPhoneNumberField.clear();
        cashierOrganizationField.clear();
        cashierShiftCheckBox.setSelected(false);
    }
}
