package org.cruise.controller;

import javafx.fxml.FXML;

public class HomeViewController {

    @FXML
    private void onCreateObject() {
        System.out.println("Create Object button clicked!");
    }

    @FXML
    private void onLoadObjects() {
        System.out.println("Load Objects button clicked!");
    }
}
