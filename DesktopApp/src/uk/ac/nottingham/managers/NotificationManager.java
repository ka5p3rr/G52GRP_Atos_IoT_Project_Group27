package uk.ac.nottingham.managers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;
import uk.ac.nottingham.main.Main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.BufferedInputStream;

/**
 * Class responsible for creating on screen notification. Provides one static method {@link
 * #show(String)} that creates and shows the notification. Default notification timeout is set to
 * 2000 ms. Different timout can be set bo calling {@link #show(String, int)}. This class also
 * provide a predefined string for running simulation error and finishing simulation.
 */
public class NotificationManager {
  public static final String SIMULATION_RUNNING_ERROR =
      "Unable to change the scenario when running simulation!";
  public static final String SIMULATION_FINISHED = "Simulation finished successfully!";

  private NotificationManager() {}

  /**
   * Just creates a popup on screen without any timeout.
   *
   * @param message text to show in the pop up
   * @return newly created {@link Popup}
   */
  private static Popup createPopup(final String message) {
    Stage stage = Main.getStage();
    final Popup popup = new Popup();
    popup.setAutoFix(true);
    Label label = new Label(message);
    label.getStylesheets().add("/uk/ac/nottingham/css/Stylesheet.css");
    label.getStyleClass().add("popup");
    popup.getContent().add(label);
    popup.setOnShown(
        e -> {
          popup.setX(stage.getX() + stage.getWidth() / 2 - popup.getWidth() / 2);
          popup.setY(stage.getY() + stage.getHeight() / 2 - popup.getHeight() / 2);
        });
    return popup;
  }

  /**
   * Create an on screen notification with two seconds timeout.
   *
   * @param message text to set in the notification
   */
  public static void show(final String message) {
    Popup popup = createPopup(message);
    popup.show(Main.getStage()); // show the notification popup
    playNotificationSound(); // play notification sound
    new Timeline(new KeyFrame(Duration.millis(2000), ae -> popup.hide())).play(); // auto hide
  }

  /**
   * Create an on screen notification.
   *
   * @param message text to set in the notification
   * @param timeout sets the timeout of the notification
   */
  public static void show(final String message, final int timeout) {
    Popup popup = createPopup(message);
    popup.show(Main.getStage()); // show the notification popup
    playNotificationSound(); // play notification sound
    new Timeline(new KeyFrame(Duration.millis(timeout), ae -> popup.hide())).play(); // auto hide
  }

  /** Play a notification sound. */
  private static synchronized void playNotificationSound() {
    try {
      Clip clip = AudioSystem.getClip();
      BufferedInputStream bufferedInputStream =
          new BufferedInputStream(
              NotificationManager.class.getResourceAsStream(
                  "/uk/ac/nottingham/resources/notification.wav"));
      AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bufferedInputStream);
      clip.open(audioInputStream);
      clip.start();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
