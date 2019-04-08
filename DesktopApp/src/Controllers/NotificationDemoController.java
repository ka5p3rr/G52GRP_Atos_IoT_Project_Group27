package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.Main;
import server.Connection;

import java.io.IOException;

public class NotificationDemoController {
    /**
     * Reloads the Welcome Screen FXML scene.
     * @throws IOException when the FXML file can't be loaded
     */
    @FXML
    public void returnToWelcomeScreen() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/FXML/WelcomeScreen.fxml"));
        Stage primaryStage = Main.getStage();
        primaryStage.setScene(new Scene(root));
        Connection.setData("");
    }

    /**
     * Set the data to send from server on button click. Also changes the text in the FXML to match this.
     * @param event on click button event
     */
    @FXML
    public void notificationDemo(ActionEvent event) {
        String eventString = event.getSource().toString();
        String data = null;
        if(eventString.contains("50")) {
            data = "demo 50%";
        } else if(eventString.contains("70")) {
            data = "demo 70%";
        } else if (eventString.contains("90")){
            data = "demo 90%";
        } else if(eventString.contains("0")) {
            data = "demo 0%";
        }
        
        Connection.setData(data);

        Parent root = WelcomeScreenController.getCurrentRoot();
        Text text = (Text) root.lookup("#currentlySetText");
        text.setText("Currently set to: " + Connection.getData());
    }
}
