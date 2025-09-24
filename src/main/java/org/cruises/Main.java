package org.cruises;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.cruises.utils.DBConnection;
import java.sql.Connection;

import java.sql.SQLException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Layout.fxml"));
        Parent root = loader.load();  // Завантажуємо коректно root з FXML

        // Create scene using the root loaded from the FXML
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());

        
        // Load and set the icon
        primaryStage.setMaximized(true);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/icon.png")));


        // Set the stage properties
        primaryStage.setTitle("Cruise Management System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }




    public static void main(String[] args) {
        launch(args);
        try (Connection conn = DBConnection.getConnection()) {
            System.out.println("З'єднання успішне!");
        } catch (SQLException e) {
            System.out.println("Помилка підключення: " + e.getMessage());
        }
    }
}
