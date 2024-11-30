package org.cruise.controller.cashier;

import com.fasterxml.jackson.core.type.TypeReference;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.cruise.controller.template.ObjectControllerTemplate;
import org.cruise.model.Cashier;
import org.cruise.service.FileManagement;
import org.cruise.service.ValidationService;

import java.io.IOException;
import java.util.List;

public class CashierController extends ObjectControllerTemplate<Cashier> {

    // FXML fields for input
    @FXML
    private TextField cashierNameField;
    @FXML
    private TextField cashierPhoneNumberField;
    @FXML
    private TextField cashierOrganizationField;
    @FXML
    private CheckBox cashierShiftCheckBox;

    @FXML
    private TableView<Cashier> cashierTable;

    private final String filePath = "data/cashiers.json";

    @FXML
    protected void initialize() {
        super.filePath = this.filePath;  // Передаємо шлях до файлу в базовий клас
        super.tableView = this.cashierTable;  // Передаємо таблицю в базовий клас
        super.dataList = FXCollections.observableArrayList();  // Ініціалізація списку об'єктів

        super.initialize();  // Викликаємо базовий метод ініціалізації
    }

    @Override
    protected void setupTableColumns() {
        TableColumn<Cashier, String> fullNameColumn = new TableColumn<>("Full Name");
        fullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        fullNameColumn.setPrefWidth(200);

        TableColumn<Cashier, String> phoneNumberColumn = new TableColumn<>("Phone Number");
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        phoneNumberColumn.setPrefWidth(200);

        TableColumn<Cashier, String> organizationColumn = new TableColumn<>("Organization");
        organizationColumn.setCellValueFactory(new PropertyValueFactory<>("organizationName"));
        organizationColumn.setPrefWidth(200);

        TableColumn<Cashier, Boolean> shiftColumn = new TableColumn<>("Shift");
        shiftColumn.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().isShift()).asObject());
        shiftColumn.setPrefWidth(200);

        // Add columns to the table
        tableView.getColumns().addAll(fullNameColumn, phoneNumberColumn, organizationColumn, shiftColumn);
    }

    @Override
    protected void openEditWindow() {
        // Get the selected cashier
        Cashier selectedCashier = tableView.getSelectionModel().getSelectedItem();
        if (selectedCashier != null) {
            try {
                // Load the EditCashier.fxml file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CreateObjectsViews/editCashier.fxml"));
                Stage stage = new Stage();
                stage.setTitle("Edit Cashier");

                // Load the FXML and set the controller's cashier via the setter method
                Parent root = loader.load();
                EditCashierController controller = loader.getController();
                controller.setCashier(selectedCashier);

                // Set the scene and show the stage
                stage.setScene(new Scene(root));
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No cashier selected");
        }
    }

    @Override
    protected TypeReference<List<Cashier>> getTypeReference() {
        return new TypeReference<List<Cashier>>() {};
    }

    @FXML
    private void addCashier() {
        // Get values from input fields using their fx:id
        String fullName = cashierNameField.getText().trim();
        String phoneNumber = cashierPhoneNumberField.getText().trim();
        String organizationName = cashierOrganizationField.getText().trim();
        boolean shift = cashierShiftCheckBox.isSelected();

        // Validate input fields
        if (fullName.isEmpty() || organizationName.isEmpty() || phoneNumber.isEmpty()) {
            showAlert("Input Error", "All fields are required.");
            return;
        }
        if (!ValidationService.isValidPhoneNumber(cashierPhoneNumberField, "Phone Number")) {
            return;
        }

        // Create the new Cashier object
        Cashier newCashier = new Cashier(fullName, phoneNumber, organizationName, shift);

        // Use the inherited addItem method to add cashier and save to file
        addItem(newCashier);

        // Clear input fields
        cashierNameField.clear();
        cashierPhoneNumberField.clear();
        cashierOrganizationField.clear();
        cashierShiftCheckBox.setSelected(false);
    }
}