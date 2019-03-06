package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller {
    void launchWelcomeScreen() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("WelcomeScreen.fxml"));
        Stage primaryStage = Main.getStage();
        primaryStage.setTitle("Group 27 Desktop App");
        primaryStage.getIcons().add(new Image("/resources/icon.png"));
        primaryStage.setScene(new Scene(root));
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    @FXML
    public void launchScenarioOne() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("ScenarioOne.fxml"));
        Stage primaryStage = Main.getStage();
        primaryStage.setScene(new Scene(root));
    }
}
