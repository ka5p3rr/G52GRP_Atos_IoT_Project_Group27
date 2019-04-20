package uk.ac.nottingham.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import uk.ac.nottingham.server.Server;

import java.io.IOException;

/**
 * Main class that creates the primary {@link Stage} for the application.
 * It acts as a "singleton" by providing a static function {@link #getStage()} that returns the
 * primary {@link Stage}.
 */
public class Main extends Application {
    /**
     * Main application {@link Stage}.
     */
    private static Stage stage;
    /**
     * Determines whether the stage is full-screen or not.
     */
    private static boolean launchFullscreen = false;

    /**
     * Set the properties of the WelcomeScreen {@link Scene}. it is provided as a public static
     * method so other classes can go back to this screen. It adds an EventFilter to the scene
     * for quick full-screen toggle using the CTRL-F shortcut.
     *
     * @param root {@link Parent} with all the UI elements loaded from the fxml file
     * @return the {@link Scene} created
     */
    public static Scene initWelcomeScreenScene(Parent root) {
        Scene scene = new Scene(root);
        scene.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
            if (new KeyCodeCombination(KeyCode.F, KeyCombination.SHORTCUT_DOWN).match(event)) {
                launchFullscreen = !launchFullscreen;
                Main.getStage().close();
                new Main().start(new Stage());
            }
        });
        return scene;
    }

    /**
     * Starts the the application by setting the properties of the current {@link Stage}. Also
     * sets the first {@link Scene}.
     *
     * @param primaryStage main {@link Stage} of the application
     */
    @Override
    public void start(Stage primaryStage) {
        if (Server.getInstance() == null) {
            makeNoNetworkAlert();
            return;
        }

        stage = primaryStage;
        // can throw an IOException when the fxml file fails to load
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/uk/ac/nottingham/fxml/WelcomeScreen.fxml"));
            primaryStage.setScene(initWelcomeScreenScene(root));
            if (launchFullscreen && primaryStage.getStyle() != StageStyle.UNDECORATED) {
                primaryStage.initStyle(StageStyle.UNDECORATED);
            }
            // set all properties
            primaryStage.setTitle("Group 27");
            primaryStage.getIcons().add(new Image("/uk/ac/nottingham/resources/icon.png"));
            primaryStage.show();
            primaryStage.setMinWidth(1280);
            primaryStage.setMinHeight(720);
            primaryStage.centerOnScreen();
            primaryStage.setMaximized(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Stops the Server when the fxml Application exits.
     */
    @Override
    public void stop() {
        Server server = Server.getInstance();
        if (server != null)
            server.stopServer();
    }

    private void makeNoNetworkAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Connection error");
        alert.setHeaderText("No active network connection");
        alert.setContentText("Please connect to a network and try again.");
        alert.showAndWait();
    }

    /**
     * Main method that launches the application. Runs the TCP IP Server on a new thread.
     *
     * @param args the primary {@link Stage} of the application
     */
    public static void main(String[] args) {
        // start the server
        Server server = Server.getInstance();
        if (server != null) {
            server.start();
        }

        // run the UI
        launch(args);
    }

    /**
     * Static function that returns the main {@link Stage} created by this class.
     *
     * @return returns the main stage of the application
     */
    public static Stage getStage() {
        return stage;
    }
}
