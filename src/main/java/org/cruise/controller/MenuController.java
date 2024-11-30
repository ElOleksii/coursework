package org.cruise.controller;

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
    private StackPane dynamicContent; // Контейнер для динамічного контенту

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
    private void showTotalPassengers() {
        loadView("/fxml/requestsViews/TotalPassengers.fxml");
    }

    @FXML
    private void showPassengersOnShip() {
        loadView("/fxml/requestsViews/PassengersOnShip.fxml");
    }

    @FXML
    private void showShipList() {
        loadView("/fxml/requestsViews/ShipList.fxml");
    }

    @FXML
    private void showPopularCabinClass() {
        loadView("/fxml/requestsViews/MostPopularCabinClass.fxml");
    }

    @FXML
    private void showTotalRevenue() {
        loadView("/fxml/requestsViews/TotalRevenue.fxml");
    }

    @FXML
    private void showPopularArrivalPort() {
        loadView("/fxml/requestsViews/MostPopularPort.fxml");
    }

    @FXML
    private void showAllRequest() {
        loadView("/fxml/requests.fxml");
    }
}
