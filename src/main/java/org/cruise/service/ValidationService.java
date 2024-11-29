package org.cruise.service;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

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

    // Метод для перевірки, що текстове поле містить правильний номер телефону
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


    // Метод для показу повідомлення з помилкою
    private static void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}