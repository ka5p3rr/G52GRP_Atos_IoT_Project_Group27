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
import uk.ac.nottingham.main.Main;
import uk.ac.nottingham.main.MyProcessorThread;
import uk.ac.nottingham.managers.GraphManager;
import uk.ac.nottingham.managers.NotificationManager;
import uk.ac.nottingham.server.Connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Controller for the Scenarios screen. It also initializes the charts for displaying simulation
 * demo data. Offers several functions to alter those charts. Allows running and resuming the
 * simulation.
 */
public class ScenariosController {
  @FXML private Text currentValueText;
  /** Run button in Scenarios scene. */
  @FXML private Button runButton;
  /** Text showing currently selected scenario. */
  @FXML private Text selectedScenarioText;
  /** Pane with graphs. */
  @FXML private GridPane middleGridPane;
  /** Text showing the current tank capacity. */
  @FXML private Text tankText;

  /** Thread running the file processing. */
  private MyProcessorThread thread;
  /** {@link StackedAreaChart} used to display pipe information. */
  private StackedAreaChart<Number, Number> pipeChart;
  /** {@link StackedAreaChart} used to display tank information */
  private StackedAreaChart<Number, Number> tankChart;
  /** {@link XYChart.Series} storing data for the pipe chart. */
  private XYChart.Series<Number, Number> pipeSeries;
  /** {@link XYChart.Series} storing data for the tank chart. */
  private XYChart.Series<Number, Number> tankSeries;
  /** X (horizontal) {@link NumberAxis} for the pipe chart. */
  private NumberAxis xPipeAxis;
  /** X (horizontal) {@link NumberAxis} for the tank chart. */
  private NumberAxis xTankAxis;
  /** {@link BufferedReader} to read the file data of the pipe. */
  private BufferedReader pipeReader;
  /** {@link BufferedReader} to read the file data of the tank. */
  private BufferedReader tankReader;
  /** Set to true when one full run finished. Otherwise it's false. */
  private boolean isFinished = false;

  /**
   * Called once on an implementing controller when the contents of its associated document have
   * been completely loaded.
   */
  @FXML
  private void initialize() {
    tankText.setText("0%");
    currentValueText.setText("Pipe: \t0.00 m^3/s\n" + "Tank: \t0.00 m");
    changeToScenarioOne(); // set the default scenario
    initializeCharts(); // initialize charts on the screen
  }

  /**
   * Initializes the charts by setting the properties of the axes, adding the data series and
   * setting up the charts as well. The charts are then added to a {@link GridPane}.
   */
  private void initializeCharts() {
    if (pipeChart != null) { // if already initialized -> remove
      middleGridPane.getChildren().remove(pipeChart);
    }
    if (tankChart != null) { // if already initialized -> remove
      middleGridPane.getChildren().remove(tankChart);
    }

    /* ********** PIPE CHART ********** */
    xPipeAxis = GraphManager.getTimeXAxis("Relative time"); // set the X axis
    NumberAxis yPipeAxis = GraphManager.getNewNumberAxis("Water flow (m^3/s)"); // set the Y axis
    yPipeAxis.setTickLabelRotation(-90);
    pipeChart = GraphManager.getChart(xPipeAxis, yPipeAxis, "Pipe chart"); // initialize new chart
    pipeSeries = new XYChart.Series<>(); // new series to be displayed on the chart
    pipeChart.getData().add(pipeSeries);

    /* ********** TANK CHART ********** */
    xTankAxis = GraphManager.getTimeXAxis("Relative time"); // set the X axis
    NumberAxis yTankAxis = GraphManager.getNewNumberAxis("Water level (m)"); // set the Y axis
    yTankAxis.setTickLabelRotation(-90);
    tankChart = GraphManager.getChart(xTankAxis, yTankAxis, "Tank chart"); // initialize new chart
    tankSeries = new XYChart.Series<>(); // new series to be displayed on the chart
    tankChart.getData().add(tankSeries);

    // add the charts to the grid pane
    middleGridPane.add(pipeChart, 0, 0);
    middleGridPane.add(tankChart, 0, 1);
  }

  /**
   * Add the argument values to {@link XYChart.Series}, which is then displayed in the {@link
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
   * Sets the upper bound of the X {@link NumberAxis} for the pipe.
   *
   * @param upperBound the upper bound number to set
   */
  public void resizePipeTimeAxis(int upperBound) {
    xPipeAxis.setUpperBound(upperBound);
  }

  /**
   * Sets the upper bound of the X {@link NumberAxis} for the tank.
   *
   * @param upperBound the upper bound number to set
   */
  public void resizeTankTimeAxis(int upperBound) {
    xTankAxis.setUpperBound(upperBound);
  }

  /**
   * Changes the colour and text of the RUN / PAUSE {@link Button} on click.
   *
   * @param event {@link ActionEvent} calling this method
   */
  @FXML
  public void runButton(ActionEvent event) {
    String string = event.getSource().toString();
    if (string.contains("RUN")) {
      setRun(); // start running
    } else {
      setPause(); // pause
    }
  }

  /** Changes the scenario to be loaded. */
  @FXML
  private void changeToScenarioOne() {
    if (thread != null) { // if the thread is running show notification and return
      NotificationManager.show(NotificationManager.SIMULATION_RUNNING_ERROR);
      return;
    }

    try {
      setReaders(); // reset the readers
    } catch (IOException e) {
      e.printStackTrace();
    }
    selectedScenarioText.setText("1"); // update UI
  }

  /** Change scenario to two */
  @FXML
  private void changeToScenarioTwo() {
    if (thread != null) { // if the thread is running show notification and return
      NotificationManager.show(NotificationManager.SIMULATION_RUNNING_ERROR);
      return;
    }
    selectedScenarioText.setText("2"); // update UI
  }

  /**
   * Style the RUN button to pause. Starts running the simulation demo or when it has been paused it
   * just resumes the current demo.
   */
  private void setRun() {
    runButton.setStyle("-fx-background-color: Orange;");
    runButton.setText("PAUSE");

    if (isFinished) { // if the simulation finished initialize the charts again and reset readers
      initializeCharts();
      try {
        setReaders();
      } catch (IOException e) {
        e.printStackTrace();
      }
      isFinished = false;
    }

    thread = new MyProcessorThread(this, pipeReader, tankReader, tankText, currentValueText);
    thread.start(); // start running the processor thread
  }

  /** Style the RUN button to run. Stop running the processor thread. */
  public void setPause() {
    thread.cancel(); // kill the thread
    thread = null;
    runButton.setStyle("-fx-background-color: Green;");
    runButton.setText("RUN");
  }

  /**
   * Initialize the {@link BufferedReader}s that are used in the processor thread. Can be also
   * called to reset them.
   *
   * @throws IOException when files can't be loaded
   */
  private void setReaders() throws IOException {
    if (pipeReader != null) pipeReader.close();
    if (tankReader != null) tankReader.close();
    pipeReader =
        new BufferedReader(
            new InputStreamReader(
                getClass().getResourceAsStream("/uk/ac/nottingham/resources/MainPipe.csv")));
    tankReader =
        new BufferedReader(
            new InputStreamReader(
                getClass().getResourceAsStream("/uk/ac/nottingham/resources/TankLevel.csv")));
  }

  /**
   * Reloads the Welcome Screen. Called on back button press.
   *
   * @throws IOException when the fxml file can't be loaded
   */
  @FXML
  private void returnToWelcomeScreen() throws IOException {
    if (thread != null) { // if the processor thread is running stop it
      thread.cancel();
      thread = null;
    }
    Connection.resetData(); // reset the server data

    Parent root =
        FXMLLoader.load(getClass().getResource("/uk/ac/nottingham/fxml/WelcomeScreen.fxml"));
    Scene scene = Main.initWelcomeScreenScene(root);
    Main.getStage().setScene(scene); // set and load the new scene
  }

  /**
   * Sets the finished flag.
   *
   * @param finished new boolean value to be set
   */
  public void setFinished(boolean finished) {
    isFinished = finished;
  }
}
