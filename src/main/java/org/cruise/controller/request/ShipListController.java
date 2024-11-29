package org.cruise.controller.request;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.cruise.service.CruiseService;
import org.cruise.model.Passenger;
import org.cruise.model.Ticket;
import org.cruise.service.FileManagement;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;
import java.util.Map;

public class ShipListController {

    @FXML
    private TableView<ShipInfo> shipTable;

    @FXML
    private TableColumn<ShipInfo, String> shipNameColumn;

    @FXML
    private TableColumn<ShipInfo, Long> passengerCountColumn;

    private CruiseService cruiseService;

    @FXML
    private void initialize() {
        // Завантажуємо дані пасажирів і квитків, і ініціалізуємо сервіс
        List<Passenger> passengers = FileManagement.loadFromFile("data/passengers.json", new TypeReference<List<Passenger>>() {});
        List<Ticket> tickets = FileManagement.loadFromFile("data/tickets.json", new TypeReference<List<Ticket>>() {});
        cruiseService = new CruiseService(passengers, tickets);

        // Ініціалізація колонок таблиці
        shipNameColumn.setCellValueFactory(new PropertyValueFactory<>("shipName"));
        passengerCountColumn.setCellValueFactory(new PropertyValueFactory<>("passengerCount"));

        // Отримуємо дані про судна та їх кількість пасажирів
        List<String> shipNames = cruiseService.getShipList();
        ObservableList<ShipInfo> shipInfoList = FXCollections.observableArrayList();

        for (String shipName : shipNames) {
            long passengerCount = cruiseService.getPassengerCountForShip(shipName);
            shipInfoList.add(new ShipInfo(shipName, passengerCount));
        }

        // Встановлюємо дані в таблицю
        shipTable.setItems(shipInfoList);
    }

    // Внутрішній клас для представлення інформації про судна
    public static class ShipInfo {
        private final String shipName;
        private final Long passengerCount;

        public ShipInfo(String shipName, Long passengerCount) {
            this.shipName = shipName;
            this.passengerCount = passengerCount;
        }

        public String getShipName() {
            return shipName;
        }

        public Long getPassengerCount() {
            return passengerCount;
        }
    }
}
