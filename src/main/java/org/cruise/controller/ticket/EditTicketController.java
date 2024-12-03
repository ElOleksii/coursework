package org.cruise.controller.ticket;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.cruise.controller.ticket.TicketController;
import org.cruise.model.Ticket;
import org.cruise.service.ValidationService;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.cruise.service.ValidationService.showAlert;

public class EditTicketController {

    @FXML
    private TextField shipNameField;

    @FXML
    private TextField departurePortField;

    @FXML
    private TextField arrivalPortField;

    @FXML
    private DatePicker ticketDatePicker;

    @FXML
    private TextField priceField;

    @FXML
    private TextField cabinClassField;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    private Ticket ticketToEdit;
    private TicketController parentController;


    public void initializeTicket(Ticket ticket, TicketController parentController) {
        this.ticketToEdit = ticket;
        this.parentController = parentController;
        populateFields();
    }


    private void populateFields() {
        if (ticketToEdit != null) {
            shipNameField.setText(ticketToEdit.getShipName());
            departurePortField.setText(ticketToEdit.getWay().getDeparturePort());
            arrivalPortField.setText(ticketToEdit.getWay().getArrivalPort());
            ticketDatePicker.setValue(ticketToEdit.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            priceField.setText(String.valueOf(ticketToEdit.getPrice()));
            cabinClassField.setText(ticketToEdit.getCabinClass());
        }
    }


    @FXML
    public void saveChanges() {
        if (ticketToEdit != null) {
            try {
                // Валідація довжини текстових полів
                if (!ValidationService.isValidStringLength(shipNameField, "Ship Name", 30) ||
                        !ValidationService.isValidStringLength(departurePortField, "Departure Port", 50) ||
                        !ValidationService.isValidStringLength(arrivalPortField, "Arrival Port", 50)) {
                    return;
                }

                if (!ValidationService.arePortsDifferent(departurePortField, arrivalPortField)) {
                    return;
                }

                LocalDate date = ticketDatePicker.getValue();
                if (date == null || date.isBefore(LocalDate.now())) {
                    showAlert("Validation Error", "Date must be in the future.", javafx.scene.control.Alert.AlertType.ERROR);
                    return;
                }

                // Валідація ціни
                if (!ValidationService.isNumericWithRange(priceField, "Price", 0.01, 10_000)) {
                    return;
                }

                // Застосування змін
                ticketToEdit.setShipName(shipNameField.getText().trim());
                ticketToEdit.getWay().setDeparturePort(departurePortField.getText().trim());
                ticketToEdit.getWay().setArrivalPort(arrivalPortField.getText().trim());
                ticketToEdit.setDate(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                ticketToEdit.setPrice(Float.parseFloat(priceField.getText().trim()));
                ticketToEdit.setCabinClass(cabinClassField.getText().trim());

                // Оновлення в таблиці
                if (parentController != null) {
                    parentController.updateTicketInTable(ticketToEdit);
                }

                closeWindow();
            } catch (NumberFormatException e) {
                showAlert("Validation Error", "Invalid price value. Please enter a valid number.", javafx.scene.control.Alert.AlertType.ERROR);
            }
        } else {
            System.out.println("Ticket is null.");
        }
    }

    /**
     * Закриває вікно редагування.
     */
    @FXML
    private void cancel() {
        closeWindow();
    }

    /**
     * Закриває поточне вікно.
     */
    private void closeWindow() {
        Stage stage = (Stage) shipNameField.getScene().getWindow();
        stage.close();
    }
}
