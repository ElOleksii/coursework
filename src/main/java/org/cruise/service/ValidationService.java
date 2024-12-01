package org.cruise.service;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import static org.cruise.service.ErrorHandler.showAlert;

public class ValidationService {

    // Метод для перевірки, що текстове поле містить лише числа і значення в діапазоні
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
            inputField.setStyle(null);  // Якщо все вірно, очищаємо стиль
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
        // Перевірка, що номер телефону починається з + і містить лише цифри (довжина від 10 до 15 символів)
        if (!input.matches("\\+\\d{10,15}")) {
            showAlert("Validation Error", fieldName + " must be a valid phone number starting with '+' and contain 10-15 digits.", Alert.AlertType.ERROR);
            inputField.setStyle("-fx-border-color: red;");
            return false;
        }
        inputField.setStyle("-fx-border-color: transparent;");  // Якщо все вірно, очищаємо стиль
        return true;
    }

    public static boolean arePortsDifferent(TextField departurePortField, TextField arrivalPortField) {
        String departurePort = departurePortField.getText().trim();
        String arrivalPort = arrivalPortField.getText().trim();

        // Перевірка, що обидва поля не є порожніми
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

        // Якщо валідація пройшла успішно, скидаємо стилі
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
        inputField.setStyle(null);  // Якщо все вірно, очищаємо стиль
        return true;
    }


}