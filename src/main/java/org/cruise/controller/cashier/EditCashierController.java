package org.cruise.controller.cashier;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.cruise.controller.cashier.CashierController;
import org.cruise.model.Cashier;
import org.cruise.service.ValidationService;

import static org.cruise.service.ErrorHandler.showAlert;

public class EditCashierController {

    @FXML
    private TextField fullNameField;

    @FXML
    private TextField phoneNumberField;

    @FXML
    private TextField organizationField;

    @FXML
    private CheckBox shiftCheckBox;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    private Cashier cashierToEdit;
    private CashierController parentController;


    public void initializeCashier(Cashier cashier, CashierController parentController) {
        this.cashierToEdit = cashier;
        this.parentController = parentController;
        populateFields();
    }


    private void populateFields() {
        if (cashierToEdit != null) {
            fullNameField.setText(cashierToEdit.getFullName());
            phoneNumberField.setText(cashierToEdit.getPhoneNumber());
            organizationField.setText(cashierToEdit.getOrganizationName());
            shiftCheckBox.setSelected(cashierToEdit.isShift());
        }

    }

    @FXML
    public void saveChanges() {
        if (cashierToEdit != null) {
            // Валідація полів
            if (!ValidationService.isValidStringLength(fullNameField, "Full Name", 30) ||
                    !ValidationService.isValidStringLength(organizationField, "Organization Name", 30) ||
                    !ValidationService.isValidPhoneNumber(phoneNumberField, "Phone Number")) {
                return;
            }

            String fullName = fullNameField.getText().trim();
            String phoneNumber = phoneNumberField.getText().trim();
            String organizationName = organizationField.getText().trim();

            if (fullName.isEmpty() || phoneNumber.isEmpty() || organizationName.isEmpty()) {
                showAlert("Validation Error", "All fields are required.", Alert.AlertType.ERROR);
                return;
            }

            // Перевірка унікальності номера телефону
            if (!ValidationService.isValidPhoneNumber(phoneNumberField, phoneNumber)) {
                showAlert("Validation Error", "Phone number must be unique.", Alert.AlertType.ERROR);
                return;
            }

            // Оновлення об'єкта
            cashierToEdit.setFullName(fullName);
            cashierToEdit.setPhoneNumber(phoneNumber);
            cashierToEdit.setOrganizationName(organizationName);
            cashierToEdit.setShift(shiftCheckBox.isSelected());

            // Оновлення таблиці
            if (parentController != null) {
                parentController.updateCashierInTable(cashierToEdit);
            }

            closeWindow();
        } else {
            System.out.println("Cashier is null.");
        }
    }


    @FXML
    private void cancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) fullNameField.getScene().getWindow();
        stage.close();
    }
}
