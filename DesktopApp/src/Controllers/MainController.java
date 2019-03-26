package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.Main;
import server.Connection;

import java.io.IOException;

/**
 * MainController class to launch other screens.
 */
public class MainController {
    /**
     * Loads the Welcome Screen FXML. Sets the title of the application, main icon and the scene.
     * @throws IOException when the FXML file can't be loaded
     */
    @FXML
    public void launchWelcomeScreen() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/FXML/WelcomeScreen.fxml"));
        Stage primaryStage = Main.getStage();
        primaryStage.setScene(new Scene(root));
        // set all properties
        primaryStage.setTitle("Group 27");
        primaryStage.getIcons().add(new Image("/resources/icon.png"));
        primaryStage.show();
        primaryStage.setResizable(false);
        primaryStage.centerOnScreen();
    }

    /**
     * Method called by clicking the Scenarios button, which launches the according screen.
     * @throws IOException when the FXML file can't be loaded
     */
    @FXML
    public void launchScenarios() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/FXML/Scenarios.fxml"));
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
        Stage primaryStage = Main.getStage();
        primaryStage.setScene(new Scene(root));

        // get the current percentage and show it on screen
        Text text = (Text) root.lookup("#currentlySetText");
        text.setText("Currently set to: " + Connection.getData());
    }

    /**
     * Reloads the Welcome Screen FXML scene.
     * @throws IOException when the FXML file can't be loaded
     */
    @FXML
    public void returnToWelcomeScreen() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/FXML/WelcomeScreen.fxml"));
        Stage primaryStage = Main.getStage();
        primaryStage.setScene(new Scene(root));
    }

    /**
     * Set the data to send from server on button click. Also changes the text in the FXML to match this.
     * @param event on click button event
     */
    @FXML
    public void notificationDemo(ActionEvent event) {
        String string = event.getSource().toString();
        if(string.contains("50")) {
            Connection.setData("50%");
        } else if(string.contains("70")) {
            Connection.setData("70%");
        } else if (string.contains("90")){
            Connection.setData("90%");
        } else if(string.contains("0")) {
            Connection.setData("0%");
        }

        try {
            launchNotificationDemo();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
