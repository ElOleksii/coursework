package org.cruise.controller.passenger;

import com.fasterxml.jackson.core.type.TypeReference;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.cruise.controller.template.ObjectControllerTemplate;
import org.cruise.model.Cashier;
import org.cruise.model.Passenger;
import org.cruise.service.FileManagement;

import java.io.IOException;
import java.util.List;

public class PassengerController extends ObjectControllerTemplate<Passenger> {

    // FXML fields for input
    @FXML
    private TextField passengerNameField;
    @FXML
    private TextField passengerPhoneNumberField;
    @FXML
    private TextField passengerAddressField;
    @FXML
    private TextField passengerTicketIdField;

    @FXML
    private TableView<Passenger> passengerTable;

    private final String filePath = "data/passengers.json";


    @FXML
    protected void initialize() {
        super.filePath = this.filePath;  // Передаємо шлях до файлу в базовий клас
        super.tableView = this.passengerTable;  // Передаємо таблицю в базовий клас
        super.dataList = FXCollections.observableArrayList();  // Ініціалізація списку об'єктів

        super.initialize();  // Викликаємо базовий метод ініціалізації
    }

    @Override
    protected void setupTableColumns() {
        TableColumn<Passenger, String> fullNameColumn = new TableColumn<>("Full Name");
        fullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        fullNameColumn.setPrefWidth(200);

        TableColumn<Passenger, String> phoneNumberColumn = new TableColumn<>("Phone Number");
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        phoneNumberColumn.setPrefWidth(200);

        TableColumn<Passenger, String> addressColumn = new TableColumn<>("Address");
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        addressColumn.setPrefWidth(200);

        TableColumn<Passenger, Integer> ticketIdColumn = new TableColumn<>("Ticket ID");
        ticketIdColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getTicketId()));
        ticketIdColumn.setPrefWidth(200);

        // Add columns to the table
        tableView.getColumns().addAll(fullNameColumn, phoneNumberColumn, addressColumn, ticketIdColumn);
    }

    @Override
    protected void openEditWindow() {
        // Get the selected Passenger
        Passenger selectedPassenger = tableView.getSelectionModel().getSelectedItem();
        if (selectedPassenger != null) {
            try {
                // Load the EditPassenger.fxml file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CreateObjectsViews/editPassenger.fxml"));
                Stage stage = new Stage();
                stage.setTitle("Edit Passenger");

                // Load the FXML and set the controller's Passenger via the setter method
                Parent root = loader.load();
//                EditPassengerController controller = loader.getController();
//                controller.setPassenger(selectedPassenger);

                // Set the scene and show the stage
                stage.setScene(new Scene(root));
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No Passenger selected");
        }
    }

    @Override
    protected TypeReference<List<Passenger>> getTypeReference() {
        return new TypeReference<List<Passenger>>() {};
    }

    @FXML
    private void addPassenger() {
        // Get values from input fields using their fx:id
        String fullName = passengerNameField.getText().trim();
        String phoneNumber = passengerPhoneNumberField.getText().trim();
        String address = passengerAddressField.getText().trim();
        int ticketId;
        try {
            ticketId = Integer.parseInt(passengerTicketIdField.getText().trim());
        } catch (NumberFormatException e) {
            System.out.println("Error: Ticket ID must be a valid number.");
            return;
        }

        // Validate input and add Passenger
        if (fullName.isEmpty() || phoneNumber.isEmpty() || address.isEmpty()) {
            System.out.println("Error: All fields are required.");
            return;
        }

        // Create the new Passenger object
        Passenger newPassenger = new Passenger(fullName, phoneNumber, address, ticketId);

        // Use the inherited addItem method to add Passenger and save to file
        addItem(newPassenger);

        // Clear input fields
        passengerNameField.clear();
        passengerPhoneNumberField.clear();
        passengerAddressField.clear();
        passengerTicketIdField.clear();
    }
}
