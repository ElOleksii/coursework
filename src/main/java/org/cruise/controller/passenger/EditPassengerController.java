package org.cruise.controller.passenger;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.cruise.controller.passenger.PassengerController;
import org.cruise.model.Passenger;
import org.cruise.service.ValidationService;

import static org.cruise.service.ErrorHandler.showAlert;

public class EditPassengerController {

    @FXML
    private TextField fullNameField;

    @FXML
    private TextField phoneNumberField;

    @FXML
    private TextField addressField;

    @FXML
    private TextField ticketIdField;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    private Passenger passengerToEdit;
    private PassengerController parentController;


    public void initializePassenger(Passenger passenger, PassengerController parentController) {
        this.passengerToEdit = passenger;
        this.parentController = parentController;
        populateFields();
    }


    private void populateFields() {
        if (passengerToEdit != null) {
            fullNameField.setText(passengerToEdit.getFullName());
            phoneNumberField.setText(passengerToEdit.getPhoneNumber());
            addressField.setText(passengerToEdit.getAddress());
            ticketIdField.setText(String.valueOf(passengerToEdit.getTicketId()));
        }
    }


    @FXML
    public void saveChanges() {
        if (passengerToEdit != null) {
            try {
                // Валідація довжини полів
                if (!ValidationService.isValidStringLength(fullNameField, "Full Name", 30) ||
                        !ValidationService.isValidStringLength(addressField, "Address", 50)) {
                    return;
                }

                // Валідація номера телефону
                if (!ValidationService.isValidPhoneNumber(phoneNumberField, "Phone Number")) {
                    return;
                }

                // Перевірка на пусті поля
                String fullName = fullNameField.getText().trim();
                String phoneNumber = phoneNumberField.getText().trim();
                String address = addressField.getText().trim();
                String ticketIdText = ticketIdField.getText().trim();

                if (fullName.isEmpty() || phoneNumber.isEmpty() || address.isEmpty() || ticketIdText.isEmpty()) {
                    showAlert("Validation Error", "All fields are required.", Alert.AlertType.ERROR);
                    return;
                }

                // Валідація Ticket ID
                int ticketId;
                try {
                    ticketId = Integer.parseInt(ticketIdText);
                } catch (NumberFormatException e) {
                    showAlert("Validation Error", "Ticket ID must be a valid number.", Alert.AlertType.ERROR);
                    return;
                }

                // Перевірка унікальності Ticket ID
                if (parentController != null && !parentController.isTicketIdUnique(ticketId, passengerToEdit)) {
                    showAlert("Validation Error", "Ticket ID must be unique.", Alert.AlertType.ERROR);
                    return;
                }

                // Оновлення об'єкта
                passengerToEdit.setFullName(fullName);
                passengerToEdit.setPhoneNumber(phoneNumber);
                passengerToEdit.setAddress(address);
                passengerToEdit.setTicketId(ticketId);

                // Оновлення таблиці
                if (parentController != null) {
                    parentController.updatePassengerInTable(passengerToEdit);
                }

                closeWindow();
            } catch (NumberFormatException e) {
                System.out.println("Invalid ticket ID entered.");
            }
        } else {
            System.out.println("Passenger is null");
        }
    }



    @FXML
    private void cancel() {
        closeWindow();
    }

    /**
     * Закриває поточне вікно.
     */
    private void closeWindow() {
        Stage stage = (Stage) fullNameField.getScene().getWindow();
        stage.close();
    }
}
