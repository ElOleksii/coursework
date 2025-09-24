package org.cruises.controller;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.cruises.service.CruiseQueries;

import java.sql.*;

public class RequestController {

    @FXML
    private ComboBox<String> sqlRequestSelector;
    @FXML
    private TextField parameterField;
    @FXML
    private TextArea resultTextArea;
    @FXML
    private TableView<ObservableList<String>> resultTable;

    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;

    private final CruiseQueries queries = new CruiseQueries();

    @FXML
    public void initialize() {
        resultTable.setVisible(false);
        sqlRequestSelector.setItems(FXCollections.observableArrayList(
                "1. Find Passengers by Cashier",
                "2. Find Passengers by Name",
                "3. Cruises in Date Range",
                "4. Get Orders Count (Last Week)",
                "5. Ticket Count by Cashier",
                "6. Max Seller Cashier",
                "7. Max Price Cruise per Route",
                "8. Ships Without Cruises (Last Week)",
                "9. Cruise Usage Activity"
        ));

        sqlRequestSelector.valueProperty().addListener((obs, oldVal, newVal) -> {
            if ("3. Cruises in Date Range".equals(newVal)) {
                parameterField.setDisable(true);

                startDatePicker.setVisible(true);
                endDatePicker.setVisible(true);
            } else {
                parameterField.setDisable(false);

                startDatePicker.setVisible(false);
                endDatePicker.setVisible(false);
            }
        });

        // Ініціалізація: ховаємо datePicker-и
        startDatePicker.setVisible(false);
        endDatePicker.setVisible(false);

        // Поле параметра доступне за замовчуванням
        parameterField.setDisable(false);
    }

    @FXML
    private void handleExecuteQuery(ActionEvent event) {
        String selectedQuery = sqlRequestSelector.getValue();

        if (selectedQuery == null || selectedQuery.isEmpty()) {
            resultTextArea.setText("Please select a query.");
            return;
        }

        try {
            ResultSet rs = null;

            switch (selectedQuery) {
                case "1. Find Passengers by Cashier":
                    String param1 = parameterField.getText().trim();
                    if (param1.isEmpty()) {
                        resultTextArea.setText("Please enter cashier name.");
                        return;
                    }
                    rs = queries.getPassengersByCashier(param1);
                    break;

                case "2. Find Passengers by Name":
                    String param2 = parameterField.getText().trim();
                    if (param2.isEmpty()) {
                        resultTextArea.setText("Please enter name pattern.");
                        return;
                    }
                    rs = queries.getPassengersByNameLike(param2 + "%");  // додано '%'
                    break;

                case "3. Cruises in Date Range":
                    if (startDatePicker.getValue() == null || endDatePicker.getValue() == null) {
                        resultTextArea.setText("Please select both start and end dates.");
                        return;
                    }
                    String startDate = startDatePicker.getValue().toString();
                    String endDate = endDatePicker.getValue().toString();
                    rs = queries.getCruisesInDateRange(startDate, endDate);
                    break;

                case "4. Get Orders Count (Last Week)":
                    rs = queries.getOrdersLastWeek();
                    break;

                case "5. Ticket Count by Cashier":
                    rs = queries.getTicketCountByCashier();
                    break;

                case "6. Max Seller Cashier":
                    rs = queries.getMaxSellerCashier();
                    break;

                case "7. Max Price Cruise per Route":
                    rs = queries.getMaxPriceCruisePerRoute();
                    break;

                case "8. Ships Without Cruises (Last Week)":
                    rs = queries.getShipsWithoutCruisesLastWeek_LeftJoin();
                    break;

                case "9. Cruise Usage Activity":
                    rs = queries.getCruiseUsageActivityUnion();
                    break;

                default:
                    resultTextArea.setText("Unknown query selected.");
                    return;
            }

            if (rs != null) {
                displayResults(rs);
                Statement stmt = rs.getStatement();
                Connection conn = stmt.getConnection();
                rs.close();
                stmt.close();
                conn.close();
            }

        } catch (SQLException e) {
            resultTextArea.setText("SQL Error: " + e.getMessage());
            e.printStackTrace();
        }
    }



    private void displayResults(ResultSet rs) throws SQLException {
        resultTextArea.clear();
        resultTable.getColumns().clear();
        resultTable.getItems().clear();

        ResultSetMetaData meta = rs.getMetaData();
        int columnCount = meta.getColumnCount();

        ObservableList<TableColumn<ObservableList<String>, ?>> columns = FXCollections.observableArrayList();

        for (int i = 1; i <= columnCount; i++) {
            final int colIndex = i - 1;
            TableColumn<ObservableList<String>, String> col = new TableColumn<>(meta.getColumnName(i));
            col.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().get(colIndex)));
            columns.add(col);
        }

        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
        while (rs.next()) {
            ObservableList<String> row = FXCollections.observableArrayList();
            for (int i = 1; i <= columnCount; i++) {
                row.add(rs.getString(i));
            }
            data.add(row);
        }

        if (data.isEmpty()) {
            resultTextArea.setText("No results found.");
            resultTable.setVisible(false);
        } else {
            resultTable.getColumns().addAll(columns);
            resultTable.setItems(data);
            resultTable.setVisible(true);
        }
    }
}
