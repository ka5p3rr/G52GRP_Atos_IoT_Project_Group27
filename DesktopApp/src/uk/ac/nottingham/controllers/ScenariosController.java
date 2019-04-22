package uk.ac.nottingham.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import uk.ac.nottingham.main.GraphManager;
import uk.ac.nottingham.main.Main;
import uk.ac.nottingham.main.MyProcessorThread;
import uk.ac.nottingham.main.Notification;
import uk.ac.nottingham.server.Connection;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/** Controller for the Scenarios screen. */
public class ScenariosController {
  /** Run button in Scenarios scene. */
  public Button runButton;
  /** Text showing currently selected scenario. */
  public Text selectedScenarioText;
  /** Pane with graphs. */
  public GridPane middleGridPane;

  public Text tankText;

  private MyProcessorThread thread;
  private StackedAreaChart<Number, Number> pipeChart;
  private StackedAreaChart<Number, Number> tankChart;
  private XYChart.Series<Number, Number> pipeSeries;
  private XYChart.Series<Number, Number> tankSeries;
  private BufferedReader pipeReader;
  private BufferedReader tankReader;
  private boolean isFinished = false;

  public void initialize() {
    changeToScenarioOne();
    initializeGraphs();
  }

  private void initializeGraphs() {
    if (pipeChart != null) {
      middleGridPane.getChildren().remove(pipeChart);
    }
    if (tankChart != null) {
      middleGridPane.getChildren().remove(tankChart);
    }

    /* ********** PIPE CHART ********** */
    NumberAxis xPipeAxis = GraphManager.getNewNumberAxis("Relative time");
    xPipeAxis.setAutoRanging(false);
    xPipeAxis.setLowerBound(0);
    xPipeAxis.setUpperBound(100);
    xPipeAxis.setTickUnit(5);
    NumberAxis yPipeAxis = GraphManager.getNewNumberAxis("Water flow (m^3/s)");
    yPipeAxis.setTickLabelRotation(-90);
    pipeChart = GraphManager.getNewLineChart(xPipeAxis, yPipeAxis, "Pipe chart");
    pipeSeries = new XYChart.Series<>();
    pipeChart.getData().add(pipeSeries);

    /* ********** TANK CHART ********** */
    NumberAxis xTankAxis = GraphManager.getNewNumberAxis("Relative time");
    xTankAxis.setAutoRanging(false);
    xTankAxis.setLowerBound(0);
    xTankAxis.setUpperBound(100);
    xTankAxis.setTickUnit(5);
    NumberAxis yTankAxis = GraphManager.getNewNumberAxis("Water level (m)");
    yTankAxis.setTickLabelRotation(-90);
    tankChart = GraphManager.getNewLineChart(xTankAxis, yTankAxis, "Tank chart");
    tankSeries = new XYChart.Series<>();
    tankChart.getData().add(tankSeries);

    middleGridPane.add(pipeChart, 0, 0);
    middleGridPane.add(tankChart, 0, 1);
  }

  /**
   * Adds the argument values to {@link XYChart.Series}, which is then displayed in the {@link
   * Scene}.
   *
   * @param pipeX x coordinate for pipe
   * @param pipeY y coordinate for pipe
   * @param tankX x coordinate for tank
   * @param tankY y coordinate for tank
   */
  public void addValue(double pipeX, double pipeY, double tankX, double tankY) {
    pipeSeries.getData().add(new XYChart.Data<>(pipeX, pipeY));
    tankSeries.getData().add(new XYChart.Data<>(tankX, tankY));
  }

  /**
   * Changes the colour and text of the RUN / PAUSE {@link Button} on click.
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

  @FXML
  public void changeToScenarioOne() {
    // return if thread is running
    if (thread != null) {
      Notification.show(Notification.RUNNING_SIMULATION);
      return;
    }

    try {
      setReaders();
    } catch (IOException e) {
      e.printStackTrace();
    }
    selectedScenarioText.setText("1");
  }

  @FXML
  public void changeToScenarioTwo() {
    // return if thread is running
    if (thread != null) {
      Notification.show(Notification.RUNNING_SIMULATION);
      return;
    }
    selectedScenarioText.setText("2");
  }

  private void setRun() {
    runButton.setStyle("-fx-background-color: Orange;");
    runButton.setText("PAUSE");

    if (isFinished) {
      initializeGraphs();

      try {
        setReaders();
      } catch (IOException e) {
        e.printStackTrace();
      }

      isFinished = false;
    }

    thread = new MyProcessorThread(this, pipeReader, tankReader);
    thread.start();
  }

  public void setPause() {
    thread.cancel();
    thread = null;
    runButton.setStyle("-fx-background-color: Green;");
    runButton.setText("RUN");
  }

  public void setFinished(boolean finished) {
    isFinished = finished;
  }

  private void setReaders() throws IOException {
    if (pipeReader != null) pipeReader.close();
    if (tankReader != null) tankReader.close();
    pipeReader =
        new BufferedReader(new FileReader("src\\uk\\ac\\nottingham\\resources\\MainPipe.csv"));
    tankReader =
        new BufferedReader(new FileReader("src\\uk\\ac\\nottingham\\resources\\TankLevel.csv"));
  }

  /**
   * Reloads the Welcome Screen. Called on back button press.
   *
   * @throws IOException when the fxml file can't be loaded
   */
  @FXML
  public void returnToWelcomeScreen() throws IOException {
    if (thread != null) {
      thread.cancel();
      thread = null;
    }
    Connection.resetData();

    Parent root =
        FXMLLoader.load(getClass().getResource("/uk/ac/nottingham/fxml/WelcomeScreen.fxml"));
    Scene scene = Main.initWelcomeScreenScene(root);
    Main.getStage().setScene(scene);
  }
}
