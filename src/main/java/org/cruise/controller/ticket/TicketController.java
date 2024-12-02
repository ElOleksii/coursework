package org.cruise.controller.ticket;

import com.fasterxml.jackson.core.type.TypeReference;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.cruise.controller.template.ObjectControllerTemplate;
import org.cruise.model.Ticket;
import org.cruise.service.FileManagement;
import org.cruise.service.ValidationService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
        super.filePath = this.filePath;
        super.tableView = this.ticketTable;
        super.dataList = FXCollections.observableArrayList();

        super.initialize();
        ticketDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(java.time.LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date.isBefore(java.time.LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffcccc;");
                }
            }
        });
    }

    @Override
    protected void setupTableColumns() {
        TableColumn<Ticket, Integer> ticketIdColumn = new TableColumn<>("Ticket ID");
        ticketIdColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getTicketId()));
        ticketIdColumn.setPrefWidth(100);

        TableColumn<Ticket, String> shipNameColumn = new TableColumn<>("Ship Name");
        shipNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getShipName()));
        shipNameColumn.setPrefWidth(150);

        TableColumn<Ticket, String> departurePortColumn = new TableColumn<>("Departure Port");
        departurePortColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getWay().getDeparturePort()));
        departurePortColumn.setPrefWidth(150);

        TableColumn<Ticket, String> arrivalPortColumn = new TableColumn<>("Arrival Port");
        arrivalPortColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getWay().getArrivalPort()));
        arrivalPortColumn.setPrefWidth(150);

        TableColumn<Ticket, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(cellData -> {
            String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(cellData.getValue().getDate());
            return new SimpleStringProperty(formattedDate);
        });
        dateColumn.setPrefWidth(120);

        TableColumn<Ticket, String> cabinClassColumn = new TableColumn<>("Cabin Class");
        cabinClassColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCabinClass()));
        cabinClassColumn.setPrefWidth(100);

        TableColumn<Ticket, Float> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPrice()));
        priceColumn.setPrefWidth(100);

        tableView.getColumns().addAll(ticketIdColumn, shipNameColumn, departurePortColumn, arrivalPortColumn, dateColumn, cabinClassColumn, priceColumn);
    }

    @Override
    protected void openEditWindow() {
        Ticket selectedTicket = tableView.getSelectionModel().getSelectedItem();
        if (selectedTicket != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EditObjectsViews/editTicket.fxml"));
                Parent root = loader.load();

                EditTicketController controller = loader.getController();
                controller.initializeTicket(selectedTicket, this);

                Stage stage = new Stage();
                stage.setTitle("Edit Ticket");
                stage.setScene(new Scene(root));
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No ticket selected.");
        }
    }


    @Override
    protected TypeReference<List<Ticket>> getTypeReference() {
        return new TypeReference<List<Ticket>>() {};
    }

    @FXML
    private void addTicket() {
        if (!ValidationService.isValidStringLength(ticketShipNameField, "Ship Name", 30) ||
                !ValidationService.isValidStringLength(ticketDeparturePortField, "Departure Port", 30) ||
                !ValidationService.isValidStringLength(ticketArrivalPortField, "Arrival Port", 30)) {
            return;
        }

        String shipName = ticketShipNameField.getText().trim();
        String departurePort = ticketDeparturePortField.getText().trim();
        String arrivalPort = ticketArrivalPortField.getText().trim();
        String cabinClass = ticketCabinClassField.getValue();
        String priceText = ticketPriceField.getText().trim();

        if (shipName.isEmpty() || departurePort.isEmpty() || arrivalPort.isEmpty() || cabinClass == null || priceText.isEmpty()) {
            showAlert("Input Error", "All fields are required.", Alert.AlertType.ERROR);
            return;
        }



        if (ticketDatePicker.getValue() == null) {
            showAlert("Input Error", "Date is required.", Alert.AlertType.ERROR);
            return;
        }

        if (!ValidationService.arePortsDifferent(ticketDeparturePortField, ticketArrivalPortField)) {
            return;
        }

        Date date = java.sql.Date.valueOf(ticketDatePicker.getValue());

        float price;
        try {
            price = Float.parseFloat(priceText);
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Invalid price value. Please enter a valid number.", Alert.AlertType.ERROR);
            return;
        }

        String ticketIdString = UUID.randomUUID().toString().replace("-", "");
        int ticketId = Math.abs(ticketIdString.hashCode());

        Ticket newTicket = new Ticket(
                ticketId,
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
        ticketCabinClassField.setValue("Select Class");
        ticketCabinClassField.setPlaceholder(new Label("Select Class"));
    }

    public void updateTicketInTable(Ticket updatedTicket) {
        int index = dataList.indexOf(updatedTicket);
        if (index >= 0) {
            dataList.set(index, updatedTicket);
            tableView.refresh();
            FileManagement.saveToFile(dataList, filePath);
        }
    }

}
