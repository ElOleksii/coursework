package org.cruise.controller.cashier;

import com.fasterxml.jackson.core.type.TypeReference;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
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

import org.cruise.service.ValidationService;

import java.io.IOException;
import java.util.List;

import static org.cruise.service.ErrorHandler.showAlert;

public class CashierController extends ObjectControllerTemplate<Cashier> {

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
        super.filePath = this.filePath;  // Set file path for the base class
        super.tableView = this.cashierTable;  // Set the table view for the base class
        super.dataList = FXCollections.observableArrayList();  // Initialize the list of objects

        super.initialize();  // Call base class initialization
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

        TableColumn<Cashier, String> shiftColumn = new TableColumn<>("Shift");
        shiftColumn.setCellValueFactory(cellData -> {
            boolean isShift = cellData.getValue().isShift();
            return new SimpleStringProperty(isShift ? "Yes" : "No");
        });
        shiftColumn.setPrefWidth(200);

        tableView.getColumns().addAll(fullNameColumn, phoneNumberColumn, organizationColumn, shiftColumn);
    }


    @Override
    protected void openEditWindow() {
        Cashier selectedCashier = tableView.getSelectionModel().getSelectedItem();
        if (selectedCashier != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CreateObjectsViews/editCashier.fxml"));
                Parent root = loader.load();
                EditCashierController controller = loader.getController();
                controller.setObjectToEdit(selectedCashier, this);

                Stage stage = new Stage();
                stage.setTitle("Edit Cashier");
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
        if (!ValidationService.isValidStringLength(cashierNameField, "Cashier Name", 30) ||
                !ValidationService.isValidStringLength(cashierOrganizationField, "Organization Name", 30)) {
            return;
        }
        String fullName = cashierNameField.getText().trim();
        String phoneNumber = cashierPhoneNumberField.getText().trim();
        String organizationName = cashierOrganizationField.getText().trim();
        boolean shift = cashierShiftCheckBox.isSelected();

        if (fullName.isEmpty() || organizationName.isEmpty() || phoneNumber.isEmpty()) {
            showAlert("Input Error", "All fields are required.", Alert.AlertType.ERROR);
            return;
        }
        if (!ValidationService.isValidPhoneNumber(cashierPhoneNumberField, "Phone Number")) {
            return;
        }

        Cashier newCashier = new Cashier(fullName, phoneNumber, organizationName, shift);

        addItem(newCashier);

        cashierNameField.clear();
        cashierPhoneNumberField.clear();
        cashierOrganizationField.clear();
        cashierShiftCheckBox.setSelected(false);
    }

    public void updateItem(Cashier updatedCashier) {
        int index = dataList.indexOf(updatedCashier);
        if (index >= 0) {
            dataList.set(index, updatedCashier);
            tableView.refresh();
        }
    }
}
