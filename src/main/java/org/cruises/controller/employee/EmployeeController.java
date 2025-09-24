package org.cruises.controller.employee;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import org.cruises.model.Cashier;
import org.cruises.service.database.CashierDAO;

import java.sql.SQLException;

public class EmployeeController {

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

    @FXML
    private Button cashierSubmitButton;

    @FXML
    private Button cancelEditButton;


    private final CashierDAO cashierDAO = new CashierDAO();
    private final ObservableList<Cashier> cashierList = FXCollections.observableArrayList();

    private Cashier selectedCashier = null;

    @FXML
    public void initialize() {
        initTable();
        loadCashiers();
        setupContextMenu();
        cashierSubmitButton.setText("Add Cashier");
    }

    private void initTable() {
        TableColumn<Cashier, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("cashierId"));

        TableColumn<Cashier, String> nameCol = new TableColumn<>("Full Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));

        TableColumn<Cashier, String> phoneCol = new TableColumn<>("Phone Number");
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        TableColumn<Cashier, String> orgCol = new TableColumn<>("Organization");
        orgCol.setCellValueFactory(new PropertyValueFactory<>("organizationName"));

        TableColumn<Cashier, Boolean> shiftCol = new TableColumn<>("Shift");
        shiftCol.setCellValueFactory(new PropertyValueFactory<>("shift"));

        cashierTable.getColumns().addAll(idCol, nameCol, phoneCol, orgCol, shiftCol);
        cashierTable.setItems(cashierList);
    }

    private void loadCashiers() {
        cashierList.clear();
        try {
            cashierList.addAll(cashierDAO.getAll());
        } catch (SQLException e) {
            showAlert("Database Error", "Could not load cashiers: " + e.getMessage());
        }
    }

    private void setupContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem editItem = new MenuItem("Edit");
        MenuItem deleteItem = new MenuItem("Delete");

        editItem.setOnAction(event -> {
            Cashier selected = cashierTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                selectedCashier = selected;
                cashierNameField.setText(selected.getFullName());
                cashierPhoneNumberField.setText(selected.getPhoneNumber());
                cashierOrganizationField.setText(selected.getOrganizationName());
                cashierShiftCheckBox.setSelected(selected.isShift());

                cashierSubmitButton.setText("Update Cashier");
                cancelEditButton.setVisible(true);
            }
        });



        deleteItem.setOnAction(event -> {
            Cashier selected = cashierTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                try {
                    if (cashierDAO.delete(selected)) {
                        cashierList.remove(selected);
                    } else {
                        showAlert("Error", "Failed to delete cashier.");
                    }
                } catch (SQLException e) {
                    showAlert("Database Error", e.getMessage());
                }
            }
        });

        contextMenu.getItems().addAll(editItem, deleteItem);

        cashierTable.setRowFactory(tv -> {
            TableRow<Cashier> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.SECONDARY) {
                    cashierTable.getSelectionModel().select(row.getItem());
                    contextMenu.show(row, event.getScreenX(), event.getScreenY());
                } else {
                    contextMenu.hide();
                }
            });
            return row;
        });
    }

    @FXML
    private void cancelEdit() {
        selectedCashier = null;
        clearFields();
        cashierSubmitButton.setText("Add Cashier");
        cancelEditButton.setVisible(false);
    }


    @FXML
    private void addCashier() {
        String name = cashierNameField.getText().trim();
        String phone = cashierPhoneNumberField.getText().trim();
        String org = cashierOrganizationField.getText().trim();
        boolean shift = cashierShiftCheckBox.isSelected();

        if (name.isEmpty() || phone.isEmpty() || org.isEmpty()) {
            showAlert("Validation Error", "All fields must be filled!");
            return;
        }

        try {
            if (selectedCashier != null) {
                selectedCashier.setFullName(name);
                selectedCashier.setPhoneNumber(phone);
                selectedCashier.setOrganizationName(org);
                selectedCashier.setShift(shift);

                if (cashierDAO.update(selectedCashier)) {
                    cashierTable.refresh();
                } else {
                    showAlert("Error", "Failed to update cashier.");
                }

                selectedCashier = null;
            } else {
                Cashier cashier = new Cashier();
                cashier.setFullName(name);
                cashier.setPhoneNumber(phone);
                cashier.setOrganizationName(org);
                cashier.setShift(shift);

                if (cashierDAO.save(cashier)) {
                    cashierList.add(cashier);
                } else {
                    showAlert("Error", "Failed to save cashier.");
                }
            }

            clearFields();
            selectedCashier = null;
            cashierSubmitButton.setText("Add Cashier");
            cancelEditButton.setVisible(false);

        } catch (SQLException e) {
            showAlert("Database Error", e.getMessage());
        }
    }

    private void clearFields() {
        cashierNameField.clear();
        cashierPhoneNumberField.clear();
        cashierOrganizationField.clear();
        cashierShiftCheckBox.setSelected(false);
        selectedCashier = null;
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
