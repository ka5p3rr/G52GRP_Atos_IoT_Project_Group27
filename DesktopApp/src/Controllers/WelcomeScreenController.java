package Controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.Main;
import server.Connection;
import server.Server;

import java.io.IOException;
import java.util.Optional;

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

        if(createLaunchDialog()) {
            primaryStage.initStyle(StageStyle.UNDECORATED);
        }
        // set all properties
        primaryStage.setTitle("Group 27");
        primaryStage.getIcons().add(new Image("/resources/icon.png"));
        primaryStage.show();
        primaryStage.setMinWidth(1280);
        primaryStage.setMinHeight(720);
        primaryStage.centerOnScreen();
        primaryStage.setMaximized(true);
    }

    private boolean createLaunchDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "",ButtonType.YES, ButtonType.NO);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Launch configuration");
        alert.setHeaderText("Do you want to launch in fullscreen?");

        Optional<ButtonType> result = alert.showAndWait();
        return result.filter(buttonType -> buttonType == ButtonType.YES).isPresent();
    }

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
