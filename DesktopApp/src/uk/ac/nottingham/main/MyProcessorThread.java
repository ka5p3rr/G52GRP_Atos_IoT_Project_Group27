package uk.ac.nottingham.main;

import javafx.application.Platform;
import javafx.scene.control.Slider;
import uk.ac.nottingham.controllers.ScenariosController;
import uk.ac.nottingham.server.Connection;

import java.io.BufferedReader;
import java.io.IOException;

/** Processing the files. Changes the values in timeline {@link Slider}. */
public class MyProcessorThread extends Thread {
  private boolean keepRunning;
  private ScenariosController scenariosController;
  private BufferedReader pipeReader;
  private BufferedReader tankReader;

  /**
   * Constructor.
   *
   * @param scenariosController {@link ScenariosController} FXML controller for scenarios screen
   * @param pipeReader {@link BufferedReader} reading the data file
   */
  public MyProcessorThread(
      ScenariosController scenariosController,
      BufferedReader pipeReader,
      BufferedReader tankReader) {
    keepRunning = true;
    this.scenariosController = scenariosController;
    this.pipeReader = pipeReader;
    this.tankReader = tankReader;
  }

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

      if (!keepRunning) {
        return;
      }

      assert pipeLine != null;
      final String[] pipeValues = pipeLine.split(",");
      assert tankLine != null;
      final String[] tankValues = tankLine.split(",");

      final double pipeX = getFirstColumn(pipeValues);
      final double pipeY = getSecondColumn(pipeValues);
      final double tankX = getFirstColumn(tankValues);
      final double tankY = getSecondColumn(tankValues);
      resizeAxes(pipeX, tankX);

      final double percentage = (tankY / 10) * 100;
      Connection.setData("demo," + (int) percentage);

      Platform.runLater(
          () -> {
            scenariosController.tankText.setText((int) percentage + "%");
            scenariosController.addValue(pipeX, pipeY, tankX, tankY);
          });

      try {
        Thread.sleep(40);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    Platform.runLater(
        () -> {
          scenariosController.setPause();
          scenariosController.setFinished(true);
          Connection.resetData();
          System.out.println("Data reading finished");
        });
  }

  private void resizeAxes(double pipeX, double tankX) {
    if (pipeX > 25) {
      scenariosController.resizePipeTimeXAxis(50);
    }
    if (pipeX > 50) {
      scenariosController.resizePipeTimeXAxis(75);
    }
    if (pipeX > 75) {
      scenariosController.resizePipeTimeXAxis(100);
    }

    if (tankX > 25) {
      scenariosController.resizeTankTimeAxis(50);
    }
    if (tankX > 50) {
      scenariosController.resizeTankTimeAxis(75);
    }
    if (tankX > 75) {
      scenariosController.resizeTankTimeAxis(100);
    }
  }

  private double getFirstColumn(String[] data) {
    double value = 0;
    if (data.length >= 2) {
      value = Double.parseDouble(data[0]);
    }
    return Math.abs(value);
  }

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
