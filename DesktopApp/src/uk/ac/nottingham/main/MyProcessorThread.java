package uk.ac.nottingham.main;

import javafx.application.Platform;
import javafx.scene.text.Text;
import uk.ac.nottingham.controllers.ScenariosController;
import uk.ac.nottingham.managers.NotificationManager;
import uk.ac.nottingham.server.Connection;

import java.io.BufferedReader;
import java.io.IOException;

/** Processing the files. Updates the UI with loaded values. */
public class MyProcessorThread extends Thread {
  /** Flag variable used to kill this thread. */
  private boolean keepRunning;
  /** {@link ScenariosController} used to access its methods. */
  private ScenariosController scenariosController;
  /** Pipe {@link BufferedReader}. */
  private BufferedReader pipeReader;
  /** Tank {@link BufferedReader}. */
  private BufferedReader tankReader;
  /** Tank capacity {@link Text} to be changed. */
  private Text tankText;

  /**
   * Constructor. Sets all member variables.
   *
   * @param scenariosController {@link ScenariosController} FXML controller for scenarios screen
   * @param pipeReader {@link BufferedReader} reading the data file pf the simulation
   * @param tankReader {@link BufferedReader} reading the data file of the simulation
   * @param tankText tank capacity {@link Text} to be changed according to simulation data
   */
  public MyProcessorThread(
      ScenariosController scenariosController,
      BufferedReader pipeReader,
      BufferedReader tankReader,
      Text tankText) {
    keepRunning = true;
    this.scenariosController = scenariosController;
    this.pipeReader = pipeReader;
    this.tankReader = tankReader;
    this.tankText = tankText;
  }

  /**
   * {@inheritDoc}
   *
   * <p>Run the processor thread. Reading the files line by line and update the charts.
   */
  @Override
  public void run() {
    String pipeLine = null;
    String tankLine = null;

    while (true) {
      // read line - if null end of file reached -> break out
      try {
        if ((pipeLine = pipeReader.readLine()) == null) break;
        if ((tankLine = tankReader.readLine()) == null) break;
      } catch (IOException e) {
        e.printStackTrace();
      }

      if (!keepRunning) { // return to stop the thread fro running
        return;
      }

      // separate the data
      assert pipeLine != null;
      final String[] pipeValues = pipeLine.split(",");
      assert tankLine != null;
      final String[] tankValues = tankLine.split(",");
      // get the data
      final double pipeX = getFirstColumn(pipeValues);
      final double pipeY = getSecondColumn(pipeValues);
      final double tankX = getFirstColumn(tankValues);
      final double tankY = getSecondColumn(tankValues);
      resizeAxes(pipeX, tankX);
      // calculate current percentage - only 10s
      final double percentage = (tankY / 10) * 100;
      Connection.setData("demo," + (int) percentage);

      Platform.runLater( // runs on the UI thread
          () -> {
            tankText.setText((int) percentage + "%");
            scenariosController.addValue(pipeX, pipeY, tankX, tankY);
          });

      try {
        Thread.sleep(40); // sleep for 40 ms
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    // when the whole file was read
    Platform.runLater(
        () -> {
          scenariosController.setPause();
          scenariosController.setFinished(true);
          Connection.resetData();
          NotificationManager.show(NotificationManager.SIMULATION_FINISHED);
        });
  }

  /**
   * Resize the axes in the charts according to newly added values.
   *
   * @param pipeX current X axis value
   * @param tankX current Y axis value
   */
  private void resizeAxes(double pipeX, double tankX) {
    if (pipeX == 25) {
      scenariosController.resizePipeTimeAxis(50);
    }
    if (pipeX == 50) {
      scenariosController.resizePipeTimeAxis(75);
    }
    if (pipeX == 75) {
      scenariosController.resizePipeTimeAxis(100);
    }

    if (tankX == 25) {
      scenariosController.resizeTankTimeAxis(50);
    }
    if (tankX == 50) {
      scenariosController.resizeTankTimeAxis(75);
    }
    if (tankX == 75) {
      scenariosController.resizeTankTimeAxis(100);
    }
  }

  /**
   * Gets first value (zeroth index) from an array of data. Returns an absolute value. If array
   * index not available return 0.
   *
   * @param data array to get data from
   * @return absolute value of date from an array
   */
  private double getFirstColumn(String[] data) {
    double value = 0;
    if (data.length >= 2) {
      value = Double.parseDouble(data[0]);
    }
    return Math.abs(value);
  }

  /**
   * Gets second value (first index) from an array of data. Returns an absolute value. If array
   * index not available return 0.
   *
   * @param data array to get data from
   * @return absolute value of date from an array
   */
  private double getSecondColumn(String[] data) {
    double value = 0;
    if (data.length >= 2) {
      value = Double.parseDouble(data[1]);
    }
    return Math.abs(value);
  }

  /** Stop running this thread. */
  public void cancel() {
    keepRunning = false;
  }
}
