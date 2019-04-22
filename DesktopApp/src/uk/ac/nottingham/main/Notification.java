package uk.ac.nottingham.main;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Class responsible for creating on screen notification. Provides one static method {@link
 * #show(String)} that creates and shows the notification. Default notification timeout is set to
 * 2000 ms. Different timout can be set bo calling {@link #show(String, int)}. This class also
 * provide a predefined string for running simulation error.
 */
public class Notification {
  public static final String RUNNING_SIMULATION =
      "Unable to change the scenario when running simulation!";

  private Notification() {}

  private static Popup createPopup(final String message) {
    Stage stage = Main.getStage();
    final Popup popup = new Popup();
    popup.setAutoFix(true);
    Label label = new Label(message);
    label.getStylesheets().add("/uk/ac/nottingham/resources/css/Stylesheet.css");
    label.getStyleClass().add("popup");
    popup.getContent().add(label);
    popup.setOnShown(
        e -> {
          popup.setX(stage.getX() + stage.getWidth() / 2 - popup.getWidth() / 2);
          popup.setY(stage.getY() + stage.getHeight() / 2 - popup.getHeight() / 2);
        });
    popup.show(stage);
    return popup;
  }

  /**
   * Create an on screen notification with two seconds timeout.
   *
   * @param message text to set in the notification
   */
  public static void show(final String message) {
    Popup popup = createPopup(message);
    new Timeline(new KeyFrame(Duration.millis(2000), ae -> popup.hide())).play();
  }

  /**
   * Create an on screen notification.
   *
   * @param message text to set in the notification
   * @param timeout sets the timeout of the notification
   */
  public static void show(final String message, final int timeout) {
    Popup popup = createPopup(message);
    new Timeline(new KeyFrame(Duration.millis(timeout), ae -> popup.hide())).play();
  }
}
