package org.cruise.controller.ticket;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.cruise.model.Ticket;

public class EditTicketController {

    @FXML
    private TextField ticketIdField;
    @FXML
    private TextField shipNameField;
    @FXML
    private TextField departurePortField;
    @FXML
    private TextField arrivalPortField;
    @FXML
    private TextField priceField;
    @FXML
    private ComboBox<String> cabinClassComboBox;

    private Ticket ticket; // Reference to the ticket being edited

    @FXML
    private void initialize() {
        // Initialize ComboBox items
        cabinClassComboBox.setItems(FXCollections.observableArrayList("Economy", "Business", "First"));
    }

    /**
     * Sets the ticket to be edited.
     *
     * @param ticket The ticket to edit.
     */
    public void setTicket(Ticket ticket) {
        this.ticket = ticket;

        // Populate fields with the ticket's current values
        ticketIdField.setText(String.valueOf(ticket.getTicketId()));  // Convert ticketId to String
        shipNameField.setText(ticket.getShipName());
        departurePortField.setText(ticket.getWay().getDeparturePort());
        arrivalPortField.setText(ticket.getWay().getArrivalPort());
        priceField.setText(String.valueOf(ticket.getPrice()));  // Convert price to String
        cabinClassComboBox.getSelectionModel().select(ticket.getCabinClass());
    }

    /**
     * Saves the changes made to the ticket.
     */
    @FXML
    private void saveChanges() {
        if (ticket != null) {
            try {
                // Update the ticket object with new values
                ticket.setTicketId(Integer.parseInt(ticketIdField.getText().trim()));  // Convert String to int
                ticket.setShipName(shipNameField.getText().trim());
                ticket.getWay().setDeparturePort(departurePortField.getText().trim());
                ticket.getWay().setArrivalPort(arrivalPortField.getText().trim());
                ticket.setPrice(Float.parseFloat(priceField.getText().trim()));  // Convert String to float
                ticket.setCabinClass(cabinClassComboBox.getSelectionModel().getSelectedItem());

                // Close the edit window
                closeWindow();
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid ticket ID or price format.");
            }
        } else {
            System.out.println("Error: No ticket to edit.");
        }
    }

    /**
     * Cancels the editing process without saving changes.
     */
    @FXML
    private void cancelEdit() {
        closeWindow();
    }

    /**
     * Closes the edit window.
     */
    private void closeWindow() {
        Stage stage = (Stage) ticketIdField.getScene().getWindow();
        stage.close();
    }
}
