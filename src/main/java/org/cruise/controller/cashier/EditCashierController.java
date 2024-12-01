package org.cruise.controller.cashier;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.cruise.controller.template.EditObjectTemplate;
import org.cruise.model.Cashier;

public class EditCashierController extends EditObjectTemplate<Cashier> {

    // FXML Fields for editing Cashier properties
    @FXML
    private TextField fullNameField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private TextField organizationField;
    @FXML
    private CheckBox shiftCheckBox;

    @Override
    protected void populateFields() {
        if (objectToEdit != null) {
            // Populate the fields with the current data of the cashier
            fullNameField.setText(objectToEdit.getFullName());
            phoneNumberField.setText(objectToEdit.getPhoneNumber());
            organizationField.setText(objectToEdit.getOrganizationName());
            shiftCheckBox.setSelected(objectToEdit.isShift());
        } else {
            System.out.println("Cashier object is null.");
        }
    }

    @Override
    protected void applyChanges() {
        if (objectToEdit != null) {
            // Apply changes to the cashier object
            objectToEdit.setFullName(fullNameField.getText().trim());
            objectToEdit.setPhoneNumber(phoneNumberField.getText().trim());
            objectToEdit.setOrganizationName(organizationField.getText().trim());
            objectToEdit.setShift(shiftCheckBox.isSelected());

            // Save the changes to the file or list
            // Assuming `dataList` and `filePath` are accessible from here
            if (getObjectController() != null) {
                getObjectController().updateItem(objectToEdit);
            }
        } else {
            System.out.println("Cashier object is null during applyChanges.");
        }
    }

    @FXML
    private void cancel() {
        // Close the edit window without saving changes
        closeWindow(fullNameField);
    }

    @FXML
    private void saveChanges() {
        applyChanges();
        closeWindow(fullNameField);
    }
}
