package org.cruise.controller.template;

import javafx.scene.Node;
import javafx.stage.Stage;

public abstract class EditObjectTemplate<T> {

    protected T objectToEdit;
    private ObjectControllerTemplate<T> objectController;

    public void setObjectToEdit(T object, ObjectControllerTemplate<T> controller) {
        this.objectToEdit = object;
        this.objectController = controller;
        populateFields();
    }

    protected abstract void populateFields();

    protected abstract void applyChanges();

    protected ObjectControllerTemplate<T> getObjectController() {
        return objectController;
    }

    protected void closeWindow(Node node) {
        // Close the window - this method will be shared among all editors
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }
}
