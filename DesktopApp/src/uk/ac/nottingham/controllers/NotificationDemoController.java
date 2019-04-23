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

/**
 * FXML controller for the NotificationDemo.fxml file. It has several functions that are executed on
 * a button press. Allows user to go back to welcome screen or test the notification system on the
 * accompanying android app.
 */
public class NotificationDemoController {
  /** Text with the currently set tank percentage, which is sent over to android devices. */
  @FXML private Text currentlySetText;

  /**
   * Called once on an implementing controller when the contents of its associated document have
   * been completely loaded.
   */
  @FXML
  private void initialize() {
    // get the current percentage and show it on screen
    currentlySetText.setText("Currently set to: " + Connection.getData());
  }

  /**
   * Reloads the WelcomeScreen.fxml file and sets the scene on the current {@link
   * javafx.stage.Stage}. Also resets the server data.
   *
   * @throws IOException when the fxml file can't be loaded
   */
  @FXML
  private void returnToWelcomeScreen() throws IOException {
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
  private void notificationDemo(ActionEvent event) {
    String eventString = ((Button) event.getSource()).getText();
    String data;

    switch (eventString) { // check what button was pressed
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
    Connection.setData(data); // set the server data

    String[] values = data.split(",");
    if (values.length >= 2) {
      int i = Integer.parseInt(values[1]);
      data = i + "%";
    }

    currentlySetText.setText("Currently set to: " + data); // update the UI
  }
}
