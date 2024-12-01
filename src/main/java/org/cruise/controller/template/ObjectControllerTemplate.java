package org.cruise.controller.template;

import com.fasterxml.jackson.core.type.TypeReference;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.cruise.service.FileManagement;

import java.util.List;

public abstract class ObjectControllerTemplate<T> {

    @FXML
    protected TableView<T> tableView;

    protected ObservableList<T> dataList = FXCollections.observableArrayList();

    protected String filePath;

    @FXML
    protected void initialize() {
        loadData();
        setupTableColumns();
        setupContextMenu();
        bindDataToTable();
    }

    private void loadData() {
        List<T> loadedData = FileManagement.loadFromFile(filePath, getTypeReference());
        dataList.addAll(loadedData);
    }

    protected abstract void setupTableColumns();

    private void setupContextMenu() {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem editItem = new MenuItem("Edit");
        editItem.setOnAction(event -> openEditWindow());

        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(event -> deleteItem());

        contextMenu.getItems().addAll(editItem, deleteItem);

        tableView.setContextMenu(contextMenu);

        tableView.setRowFactory(tv -> {
            TableRow<T> row = new TableRow<>();
            row.setOnContextMenuRequested(event -> {
                if (!row.isEmpty()) {
                    contextMenu.show(row, event.getScreenX(), event.getScreenY());
                }
            });
            return row;
        });
    }

    private void bindDataToTable() {
        tableView.setItems(dataList);
    }

    protected abstract void openEditWindow();

    protected abstract TypeReference<List<T>> getTypeReference();

    private void deleteItem() {
        T selectedItem = tableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            dataList.remove(selectedItem);
            FileManagement.saveToFile(dataList, filePath);
        }
    }

    protected void addItem(T item) {
        dataList.add(item);
        FileManagement.saveToFile(dataList, filePath);
    }

    public void updateItem(T updatedItem) {
        int index = dataList.indexOf(updatedItem);
        if (index != -1) {
            dataList.set(index, updatedItem);
            FileManagement.saveToFile(dataList, filePath);
        }
    }


}
