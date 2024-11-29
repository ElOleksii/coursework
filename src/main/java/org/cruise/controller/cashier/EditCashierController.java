package org.cruise.controller.cashier;

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


    @FXML
    public void initialize() {
        if (cashier != null) {
            System.out.println("Full Name: " + cashier.getFullName());
        } else {
            System.out.println("Cashier object is null.");
        }
    }

    public void setCashier(Cashier cashier) {
        this.cashier = cashier;
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
