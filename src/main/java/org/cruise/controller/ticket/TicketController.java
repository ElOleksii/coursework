package org.cruise.controller.ticket;

import com.fasterxml.jackson.core.type.TypeReference;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
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
import org.cruise.model.Ticket;
import org.cruise.service.FileManagement;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.cruise.service.ErrorHandler.showAlert;

public class TicketController extends ObjectControllerTemplate<Ticket> {

    @FXML
    private TextField ticketShipNameField;
    @FXML
    private TextField ticketDeparturePortField;
    @FXML
    private TextField ticketArrivalPortField;
    @FXML
    private ComboBox<String> ticketCabinClassField;
    @FXML
    private TextField ticketPriceField;
    @FXML
    private DatePicker ticketDatePicker;

    @FXML
    private TableView<Ticket> ticketTable;

    private final String filePath = "data/tickets.json";

    @FXML
    protected void initialize() {
        super.filePath = this.filePath;  // Передаємо шлях до файлу в базовий клас
        super.tableView = this.ticketTable;  // Передаємо таблицю в базовий клас
        super.dataList = FXCollections.observableArrayList();  // Ініціалізація списку об'єктів

        super.initialize();  // Викликаємо базовий метод ініціалізації
    }

    @Override
    protected void setupTableColumns() {
        TableColumn<Ticket, Integer> ticketIdColumn = new TableColumn<>("Ticket ID");
        ticketIdColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getTicketId()));
        ticketIdColumn.setPrefWidth(200);

        TableColumn<Ticket, String> shipNameColumn = new TableColumn<>("Ship Name");
        shipNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getShipName()));
        shipNameColumn.setPrefWidth(200);

        TableColumn<Ticket, String> departurePortColumn = new TableColumn<>("Departure Port");
        departurePortColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDeparturePort()));
        departurePortColumn.setPrefWidth(200);

        TableColumn<Ticket, String> arrivalPortColumn = new TableColumn<>("Arrival Port");
        arrivalPortColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getArrivalPort()));
        arrivalPortColumn.setPrefWidth(200);

        TableColumn<Ticket, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(cellData -> {
            String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(cellData.getValue().getDate());
            return new SimpleStringProperty(formattedDate);
        });
        dateColumn.setPrefWidth(200);

        TableColumn<Ticket, String> cabinClassColumn = new TableColumn<>("Cabin Class");
        cabinClassColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCabinClass()));
        cabinClassColumn.setPrefWidth(200);

        TableColumn<Ticket, Float> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPrice()));
        priceColumn.setPrefWidth(200);

        tableView.getColumns().addAll(ticketIdColumn, shipNameColumn, departurePortColumn, arrivalPortColumn, dateColumn, cabinClassColumn, priceColumn);
    }

    @Override
    protected void openEditWindow() {
        Ticket selectedTicket = tableView.getSelectionModel().getSelectedItem();
        if (selectedTicket != null) {
            try {
                // Load the EditTicket.fxml file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CreateObjectsViews/editTicket.fxml"));
                Stage stage = new Stage();
                stage.setTitle("Edit Ticket");

                Parent root = loader.load();
                EditTicketController controller = loader.getController();
                controller.setTicket(selectedTicket);

                stage.setScene(new Scene(root));
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No ticket selected");
        }
    }

    @Override
    protected TypeReference<List<Ticket>> getTypeReference() {
        return new TypeReference<List<Ticket>>() {};
    }

    @FXML
    private void addTicket() {
        String shipName = ticketShipNameField.getText().trim();
        String departurePort = ticketDeparturePortField.getText().trim();
        String arrivalPort = ticketArrivalPortField.getText().trim();
        String cabinClass = ticketCabinClassField.getValue();
        String priceText = ticketPriceField.getText().trim();


        if (shipName.isEmpty() || departurePort.isEmpty() || arrivalPort.isEmpty() || cabinClass.isEmpty() || priceText.isEmpty()) {
            showAlert("Input Error", "All fields are required.");
            return;
        }
        if (ticketDatePicker.getValue() == null) {
            System.out.println("Error: Date is required.");
            return;
        }

        Date date = java.sql.Date.valueOf(ticketDatePicker.getValue());

        // Validate input fields


        float price;
        try {
            price = Float.parseFloat(priceText);
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid price.");
            return;
        }

        long ticketId = System.currentTimeMillis();

        Ticket newTicket = new Ticket(
                (int) ticketId,  // Use the timestamp as the ticket ID
                shipName,
                departurePort,
                arrivalPort,
                date,
                cabinClass,
                price
        );

        addItem(newTicket);

        ticketShipNameField.clear();
        ticketDeparturePortField.clear();
        ticketArrivalPortField.clear();
        ticketCabinClassField.setValue(null);
        ticketPriceField.clear();
        ticketDatePicker.setValue(null);
    }
}
