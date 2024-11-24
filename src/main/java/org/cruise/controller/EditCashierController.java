package org.cruise.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.cruise.model.Cashier;

public class EditCashierController {

    private Cashier cashier;

    @FXML
    private TextField fullNameField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private TextField organizationField;
    @FXML
    private CheckBox shiftCheckBox;

    public EditCashierController(Cashier cashier) {
        this.cashier = cashier;
    }
    

    @FXML
    private void initialize() {
        // Initialize fields with the current values from the selected cashier
        fullNameField.setText(cashier.getFullName());
        phoneNumberField.setText(cashier.getPhoneNumber());
        organizationField.setText(cashier.getOrganizationName());
        shiftCheckBox.setSelected(cashier.isShift()); // True for Day shift, False for Night shift
    }


    @FXML
    private void saveChanges() {
        // Save changes to the cashier object
        cashier.setFullName(fullNameField.getText().trim());
        cashier.setPhoneNumber(phoneNumberField.getText().trim());
        cashier.setOrganizationName(organizationField.getText().trim());
        cashier.setShift(shiftCheckBox.isSelected()); // Day (true) or Night (false)

        // Close the edit window
        Stage stage = (Stage) fullNameField.getScene().getWindow();
        stage.close();
    }


    @FXML
    private void cancel() {
        // Close the edit window without saving changes
        Stage stage = (Stage) fullNameField.getScene().getWindow();
        stage.close();
    }
}
