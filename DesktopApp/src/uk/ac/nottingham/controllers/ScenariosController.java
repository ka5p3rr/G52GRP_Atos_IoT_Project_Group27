package uk.ac.nottingham.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.text.Text;
import uk.ac.nottingham.main.Main;
import uk.ac.nottingham.main.MyProcessorThread;
import uk.ac.nottingham.server.Connection;

import java.io.IOException;

/** Controller for the Scenarios screen. */
public class ScenariosController {
  /** Run button in Scenarios scene. */
  public Button runButton;
  /** Text showing currently selected scenario. */
  public Text selectedScenarioText;
  /** Timeline slider. */
  public Slider MainSlider;

  private MyProcessorThread thread;

  public void initialize() {
    changeToScenarioOne();
  }

  /**
   * Reloads the Welcome Screen. Called on back button press.
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
   * Changes the colour and text of the RUN / RESUME {@link Button} on click.
   *
   * @param event {@link ActionEvent}
   */
  @FXML
  public void runButton(ActionEvent event) {
    String string = event.getSource().toString();
    if (string.contains("RUN")) {
      setRun();
    } else {
      setPause();
    }
  }

  private void setRun() {
    runButton.setStyle("-fx-background-color: Orange;");
    runButton.setText("PAUSE");

    thread = new MyProcessorThread(this, MainSlider);
    thread.start();
  }

  public void setPause() {
    thread.cancel();
    runButton.setStyle("-fx-background-color: Green;");
    runButton.setText("RUN");
  }

  @FXML
  public void changeToScenarioOne() {
    selectedScenarioText.setText("1");
  }

  @FXML
  public void changeToScenarioTwo() {
    selectedScenarioText.setText("2");
  }
}
