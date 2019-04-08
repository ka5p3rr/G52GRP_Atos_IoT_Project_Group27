package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.Main;
import server.Connection;
import server.Server;

import java.io.IOException;

/**
 * WelcomeScreenController class to launch other screens.
 */
public class WelcomeScreenController {
    private static Parent currentRoot;

    static Parent getCurrentRoot() {
        return currentRoot;
    }

    /**
     * Loads the Welcome Screen FXML. Sets the title of the application, main icon and the scene.
     * @throws IOException when the FXML file can't be loaded
     */
    @FXML
    public void launchWelcomeScreen() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/FXML/WelcomeScreen.fxml"));
        currentRoot = root;
        Stage primaryStage = Main.getStage();
        primaryStage.setScene(new Scene(root));
        // set all properties
        primaryStage.setTitle("Group 27");
        primaryStage.getIcons().add(new Image("/resources/icon.png"));
        primaryStage.show();
        primaryStage.setResizable(false);
//        primaryStage.setFullScreen(true);
        primaryStage.centerOnScreen();
    }

    /**
     * Creates a dialog pop up with connection information.
     */
    @FXML
    public void createAlertDialog() {
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
     * @throws IOException when the FXML file can't be loaded
     */
    @FXML
    public void launchScenarios() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/FXML/Scenarios.fxml"));
        currentRoot = root;
        Stage primaryStage = Main.getStage();
        primaryStage.setScene(new Scene(root));
    }

    /**
     * Called from the UI. It opens the Notification Demo Screen.
     * @throws IOException when the FXML file can't be loaded
     */
    @FXML
    public void launchNotificationDemo() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/FXML/NotificationDemo.fxml"));
        currentRoot = root;
        Stage primaryStage = Main.getStage();
        primaryStage.setScene(new Scene(root));

        // get the current percentage and show it on screen
        Text text = (Text) root.lookup("#currentlySetText");
        text.setText("Currently set to: " + Connection.getData());
    }
}
