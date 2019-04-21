package uk.ac.nottingham.main;

import javafx.application.Platform;
import javafx.scene.control.Slider;
import uk.ac.nottingham.controllers.ScenariosController;

/** Processing the files. Changes the values in timeline {@link Slider}. */
public class MyProcessorThread extends Thread {
  private boolean keepRunning;
  private ScenariosController scenariosController;
  private Slider MainSlider;

  /**
   * Constructor.
   *
   * @param scenariosController {@link ScenariosController} FXML controller for scenarios screen
   * @param MainSlider {@link Slider} timeline slider on the Scenarios scene
   */
  public MyProcessorThread(ScenariosController scenariosController, Slider MainSlider) {
    keepRunning = true;
    this.MainSlider = MainSlider;
    this.scenariosController = scenariosController;
  }

  @Override
  public void run() {

    // keep changing the slider values until you hit the max value
    while (MainSlider.getValue() != MainSlider.getMax()) {
      if (!keepRunning) {
        return;
      }

      MainSlider.increment();

      // DO THE WORK HERE
      // displaying the graph

      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    // when loop finished go back to zero and reset running button
    MainSlider.adjustValue(0);
    Platform.runLater(() -> scenariosController.setPause());
  }

  /** Stop running this thread. */
  public void cancel() {
    keepRunning = false;
  }
}
