package org.cruises.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MenuController {

    @FXML
    private StackPane dynamicContent;

    @FXML
    private VBox sideNavBar; // Ліве бокове меню

    @FXML
    private Button burgerMenuButton; // Кнопка меню-бургер

    private boolean isNavBarCollapsed = true;

    private Map<String, Node> views = new HashMap<>();


    @FXML
    protected void initialize() {
        preloadView("/fxml/CreateObjectsViews/cashierView.fxml", "cashierView");
        preloadView("/fxml/CreateObjectsViews/passengerView.fxml", "passengerView");
        preloadView("/fxml/CreateObjectsViews/ticketView.fxml", "ticketView");
        preloadView("/fxml/CreateObjectsViews/cruiseView.fxml", "cruiseView");
        loadView("/fxml/HomeView.fxml"); // Завантажуємо початковий вигляд Home
    }

    // Метод для завантаження конкретного вигляду
    private void loadView(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Node view = loader.load();
            dynamicContent.getChildren().setAll(view); // Заміна поточного вигляду новим
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void preloadView(String fxmlFile, String viewKey) {
        try {
            Node view = FXMLLoader.load(getClass().getResource(fxmlFile));
            views.put(viewKey, view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Метод для згортання/розгортання бокового меню
    @FXML
    private void toggleNavBar() {
        boolean isVisible = sideNavBar.isVisible();
        sideNavBar.setVisible(!isVisible);
        sideNavBar.setManaged(!isVisible);
    }

    // Методи для завантаження конкретних виглядів, прив'язаних до кнопок у меню
    @FXML
    private void switchToCashierView() {
        loadView("/fxml/CreateObjectsViews/cashierView.fxml");
    }

    @FXML
    private void switchToPassengerView() {
        loadView("/fxml/CreateObjectsViews/passengerView.fxml");
    }

    @FXML
    private void switchToTicketView() {
        loadView("/fxml/CreateObjectsViews/ticketView.fxml");
    }

    @FXML
    private void switchToCruiseView() {
        loadView("/fxml/CreateObjectsViews/cruiseView.fxml");
    }

    @FXML
    private void switchToShipView() {
        loadView("/fxml/CreateObjectsViews/shipView.fxml");
    }

    @FXML
    private void switchToStateroomView() {
        loadView("/fxml/CreateObjectsViews/stateroomView.fxml");
    }

    @FXML
    private void switchToRouteView() {
        loadView("/fxml/CreateObjectsViews/routeView.fxml");
    }

    @FXML
    private void switchToMakeOrderView() {
        loadView("/fxml/CreateObjectsViews/makeOrderView.fxml");
    }

    @FXML
    private void switchToOrdersView() {
        loadView("/fxml/CreateObjectsViews/ordersView.fxml");
    }

    @FXML
    private void showAllRequest() {
        loadView("/fxml/Request.fxml");
    }


}
