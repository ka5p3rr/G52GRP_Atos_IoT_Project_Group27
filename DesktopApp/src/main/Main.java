package main;

import GUI.Controller;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main class used that creates the primary {@link Stage} for the application. It acts as a singleton by providing a static function {@link #getStage()} that returns the primary {@link Stage}.
 */
public class Main extends Application {
    private static Stage stage;

    /**
     * Static function that returns the main {@link Stage} created by the this class.
     * @return returns the main stage of the application
     */
    public static Stage getStage(){
        return stage;
    }

    /**
     * Starts the the application and instantiates the {@link Controller} and calls a function to set the {@link javafx.scene.Scene}.
     * @param primaryStage main {@link Stage} of the application
     */
    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        // can throw an IOException when the FXML file fails to load
        try {
            new Controller().launchWelcomeScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Main method that launches the application.
     * @param args the primary {@link Stage} of the application
     */
    public static void main(String[] args) {
        launch(args);
    }
}
