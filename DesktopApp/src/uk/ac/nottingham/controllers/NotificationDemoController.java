package uk.ac.nottingham.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import uk.ac.nottingham.main.Main;
import uk.ac.nottingham.server.Connection;

import java.io.IOException;

public class NotificationDemoController {
  public Text currentlySetText;

  public void initialize() {
    // get the current percentage and show it on screen
    currentlySetText.setText("Currently set to: " + Connection.getData());
  }

  /**
   * Reloads the Welcome Screen fxml scene.
   *
   * @throws IOException when the fxml file can't be loaded
   */
  @FXML
  public void returnToWelcomeScreen() throws IOException {
    Parent root =
        FXMLLoader.load(getClass().getResource("/uk/ac/nottingham/fxml/WelcomeScreen.fxml"));
    Scene scene = Main.initWelcomeScreenScene(root);
    Main.getStage().setScene(scene);
    Connection.resetData();
  }

  /**
   * Set the data to send from server on button click. Also changes the text in the fxml to match
   * this.
   *
   * @param event on click button event
   */
  @FXML
  public void notificationDemo(ActionEvent event) {
    String eventString = ((Button) event.getSource()).getText();
    String data;

    switch (eventString) {
      case "0%":
        data = "notification,0";
        break;
      case "50%":
        data = "notification,50";
        break;
      case "70%":
        data = "notification,70";
        break;
      case "90%":
        data = "notification,90";
        break;
      default:
        return;
    }
    Connection.setData(data);

    String[] values = data.split(",");
    if (values.length >= 2) {
      int i = Integer.parseInt(values[1]);
      data = i + "%";
    }

    currentlySetText.setText("Currently set to: " + data);
  }
}
