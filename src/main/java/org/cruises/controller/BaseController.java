package org.cruises.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public abstract class BaseController<T> {

    protected TableView<T> table;
    protected ObservableList<T> data = FXCollections.observableArrayList();

    protected T selectedItem;

    protected abstract List<T> getAllItems() throws SQLException;
    protected abstract boolean saveItem(T item) throws SQLException;
    protected abstract boolean updateItem(T item) throws SQLException;
    protected abstract void deleteItem(T item) throws SQLException;

    protected abstract T buildItemFromFields();
    protected abstract void fillFieldsFromItem(T item);
    protected abstract boolean validateInput();

    protected void init(TableView<T> tableView) {
        this.table = tableView;
        setupContextMenu();
        loadData();
    }

    protected void loadData() {
        try {
            data.setAll(getAllItems());
            table.setItems(data);
        } catch (SQLException e) {
            showAlert("Database Error", e.getMessage());
        }
    }

    protected void refreshData() {
        loadData();
        clearFields();
    }

    public void onSaveOrUpdate() {
        if (!validateInput()) return;

        T item = buildItemFromFields();

        try {
            if (selectedItem != null) {
                updateItem(item);
            } else {
                saveItem(item);
            }
            refreshData();
        } catch (SQLException e) {
            showAlert("Database Error", e.getMessage());
        }
    }

    public void onCancelEdit() {
        clearFields();
    }

    protected void clearFields() {
        selectedItem = null;
        table.getSelectionModel().clearSelection();
    }

    private void setupContextMenu() {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem editItem = new MenuItem("Edit");
        editItem.setOnAction(e -> {
            selectedItem = table.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                fillFieldsFromItem(selectedItem);
            }
        });

        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(e -> {
            T item = table.getSelectionModel().getSelectedItem();
            if (item != null) {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                        "Are you sure you want to delete?",
                        ButtonType.YES, ButtonType.NO);
                Optional<ButtonType> result = confirm.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.YES) {
                    try {
                        deleteItem(item);
                        refreshData();
                    } catch (SQLException ex) {
                        showAlert("Error", ex.getMessage());
                    }
                }
            }
        });

        contextMenu.getItems().addAll(editItem, deleteItem);
        table.setContextMenu(contextMenu);
    }

    protected void showAlert(String title, String content) {
        new Alert(Alert.AlertType.ERROR, content, ButtonType.OK).showAndWait();
    }
}
