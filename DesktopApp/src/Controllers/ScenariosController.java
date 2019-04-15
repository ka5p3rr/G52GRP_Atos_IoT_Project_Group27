package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.Main;

import java.io.IOException;


/**
 * Controller for the Scenarios screen.
 * IT WILL IMPLEMENT MORE FUNCTIONALITY LATER
 * RIGHT NOW ONLY RUN / RESUME BUTTON CHANGES COLOURS
 */
public class ScenariosController {
    /**
     * Run button in Scenarios scene
     */
    public Button runButton;
    public Text selectedScenarioText;

    public void initialize() {
        changeToScenarioOne();
    }

    /**
     * Reloads the Welcome Screen. Called on back button press.
     * @throws IOException when the FXML file can't be loaded
     */
    @FXML
    public void launchWelcomeScreen() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/FXML/WelcomeScreen.fxml"));
        Stage primaryStage = Main.getStage();
        primaryStage.setScene(new Scene(root));
    }


    /**
     * Changes the colour and text of the RUN / RESUME {@link Button} on click.
     * @param event {@link ActionEvent}
     */
    @FXML
    public void runButton(ActionEvent event) {
        String string = event.getSource().toString();
        if(string.contains("RUN")) {
            runButton.setStyle("-fx-background-color: Orange;");
            runButton.setText("RESUME");
        }
        else {
            runButton.setStyle("-fx-background-color: Green;");
            runButton.setText("RUN");
        }
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
