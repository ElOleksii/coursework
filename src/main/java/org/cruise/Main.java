package org.cruise;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/layout.fxml"));
        Parent root = loader.load();  // Corrected line: you need to use the loaded root object

        // Create scene using the root loaded from the FXML
        Scene scene = new Scene(root, 800, 600);  // Use 'root' here instead of fxmlLoader.load()

        // Set up the primary stage
        primaryStage.setTitle("Cruise Management System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
