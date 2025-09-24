package org.cruises.service;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.util.List;


public class ValidationService {

    public static boolean isNumericWithRange(TextField inputField, String fieldName, double minValue, double maxValue) {
        String input = inputField.getText().trim();
        if (input == null || input.isEmpty()) {
            showAlert("Validation Error", fieldName + " cannot be empty.", Alert.AlertType.ERROR);
            inputField.setStyle("-fx-border-color: red;");
            return false;
        }
        try {
            double value = Double.parseDouble(input);
            if (value < minValue || value > maxValue) {
                showAlert("Validation Error", fieldName + " must be between " + minValue + " and " + maxValue + ".", Alert.AlertType.ERROR);
                inputField.setStyle("-fx-border-color: red;");
                return false;
            }
            inputField.setStyle(null);
            return true;
        } catch (NumberFormatException e) {
            showAlert("Validation Error", fieldName + " must be a valid number.", Alert.AlertType.ERROR);
            inputField.setStyle("-fx-border-color: red;");
            return false;
        }
    }

    public static boolean isValidPhoneNumber(TextField inputField, String fieldName) {
        String input = inputField.getText().trim();
        if (input == null || input.isEmpty()) {
            showAlert("Validation Error", fieldName + " cannot be empty.", Alert.AlertType.ERROR);
            inputField.setStyle("-fx-border-color: red;");
            return false;
        }
        if (!input.matches("\\+\\d{10,15}")) {
            showAlert("Validation Error", fieldName + " must be a valid phone number starting with '+' and contain 10-15 digits.", Alert.AlertType.ERROR);
            inputField.setStyle("-fx-border-color: red;");
            return false;
        }
        inputField.setStyle("-fx-border-color: transparent;");
        return true;
    }

    public static boolean arePortsDifferent(TextField departurePortField, TextField arrivalPortField) {
        String departurePort = departurePortField.getText().trim();
        String arrivalPort = arrivalPortField.getText().trim();

        if (departurePort.isEmpty() || arrivalPort.isEmpty()) {
            showAlert("Validation Error", "Both Departure Port and Arrival Port must be provided.", Alert.AlertType.ERROR);
            departurePortField.setStyle("-fx-border-color: red;");
            arrivalPortField.setStyle("-fx-border-color: red;");
            return false;
        }

        if (departurePort.equalsIgnoreCase(arrivalPort)) {
            showAlert("Validation Error", "Departure Port and Arrival Port cannot be the same.", Alert.AlertType.ERROR);
            departurePortField.setStyle("-fx-border-color: red;");
            arrivalPortField.setStyle("-fx-border-color: red;");
            return false;
        }

        departurePortField.setStyle("-fx-border-color: transparent;");
        arrivalPortField.setStyle("-fx-border-color: transparent;");
        return true;
    }

    public static boolean isValidStringLength(TextField inputField, String fieldName, int maxLength) {
        String input = inputField.getText().trim();
        if (input == null || input.isEmpty()) {
            showAlert("Validation Error", fieldName + " cannot be empty.", Alert.AlertType.ERROR);
            inputField.setStyle("-fx-border-color: red;");
            return false;
        }
        if (input.length() > maxLength) {
            showAlert("Validation Error", fieldName + " cannot exceed " + maxLength + " characters.", Alert.AlertType.ERROR);
            inputField.setStyle("-fx-border-color: red;");
            return false;
        }
        inputField.setStyle(null);
        return true;
    }

    public static void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static <T> boolean isDuplicateByFullName(List<T> dataList, String fullName, Class<T> type) {
        boolean duplicateExists = dataList.stream().anyMatch(item -> {
            if (type.isInstance(item)) {
                if (item instanceof org.cruises.model.Cashier cashier) {
                    return cashier.getFullName().equalsIgnoreCase(fullName);
                } else if (item instanceof org.cruises.model.Passenger passenger) {
                    return passenger.getFullName().equalsIgnoreCase(fullName);
                }
            }
            return false;
        });

        if (duplicateExists) {
            showAlert("Duplicate Error", "An item with the same Full Name already exists.", Alert.AlertType.ERROR);
        }

        return duplicateExists;
    }

    public static <T> boolean isDuplicateByPhoneNumber(List<T> dataList, String phoneNumber, Class<T> type) {
        boolean duplicateExists = dataList.stream().anyMatch(item -> {
            if (type.isInstance(item)) {
                if (item instanceof org.cruises.model.Cashier cashier) {
                    return cashier.getPhoneNumber().equals(phoneNumber);
                } else if (item instanceof org.cruises.model.Passenger passenger) {
                    return passenger.getPhoneNumber().equals(phoneNumber);
                }
            }
            return false;
        });

        if (duplicateExists) {
            showAlert("Duplicate Error", "An item with the same Phone Number already exists.", Alert.AlertType.ERROR);
        }

        return duplicateExists;
    }

}