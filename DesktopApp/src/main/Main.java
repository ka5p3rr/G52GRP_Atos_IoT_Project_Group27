package main;

import Controllers.MainController;
import server.Server;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main class that creates the primary {@link Stage} for the application.
 * It acts as a "singleton" by providing a static function {@link #getStage()} that returns the primary {@link Stage}.
 */
public class Main extends Application {
    /**
     * Main application {@link Stage}.
     */
    private static Stage stage;

    /**
     * Static function that returns the main {@link Stage} created by this class.
     * @return returns the main stage of the application
     */
    public static Stage getStage(){
        return stage;
    }

    /**
     * Starts the the application and instantiates the {@link MainController} and calls a function to set the Welcome Screen {@link javafx.scene.Scene}.
     * @param primaryStage main {@link Stage} of the application
     */
    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        // can throw an IOException when the FXML file fails to load
        try {
            new MainController().launchWelcomeScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Stops the Server when the FXML Application exits.
     */
    @Override
    public void stop(){
        Server.stopServer();
    }

    /**
     * Main method that launches the application. Runs the TCP IP Server on a new thread.
     * @param args the primary {@link Stage} of the application
     */
    public static void main(String[] args) {
        // run the server
        Server server = Server.getInstance();
        server.start();
        System.out.println("server started");

        // run the UI
        launch(args);

        try {
            server.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
