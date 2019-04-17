package uk.ac.nottingham.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import uk.ac.nottingham.main.Main;
import uk.ac.nottingham.server.Server;
import java.io.IOException;

/**
 * WelcomeScreenController class to launch other screens.
 */
public class WelcomeScreenController {
    /**
     * Creates a dialog pop up with connection information.
     */
    @FXML
    public void createNetworkInformationDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Connection information");
        alert.setHeaderText(null);

        // create a label with connection information and add it to the dialog pane
        Label label = new Label(Server.getNetInfo());
        alert.getDialogPane().setContent(label);

        alert.showAndWait();
    }

    /**
     * Method called by clicking the Scenarios button, which launches the according screen.
     * @throws IOException when the fxml file can't be loaded
     */
    @FXML
    public void launchScenarios() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/uk/ac/nottingham/fxml/Scenarios.fxml"));
        Stage primaryStage = Main.getStage();
        primaryStage.setScene(new Scene(root));
    }

    /**
     * Called from the UI. It opens the Notification Demo Screen.
     * @throws IOException when the fxml file can't be loaded
     */
    @FXML
    public void launchNotificationDemo() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/uk/ac/nottingham/fxml/NotificationDemo.fxml"));
        Stage primaryStage = Main.getStage();
        primaryStage.setScene(new Scene(root));
    }
}
