package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.Main;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Controller class for all GUI FXML files.
 */
public class Controller {
    /**
     *
     */
    public Button runButton;

    /**
     * Loads the Welcome Screen FXML. Sets the title of the application, main icon and the scene.
     * @throws IOException when the FXML file can't be loaded
     */
    @FXML
    public void launchWelcomeScreen() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/GUI/WelcomeScreen.fxml"));
        Stage primaryStage = Main.getStage();
        primaryStage.setTitle("Group 27");
        primaryStage.getIcons().add(new Image("/resources/icon.png"));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.setResizable(false);
        netStat(root);
    }

    /**
     * Helper function that sets the text on Welcome screen with IP Address and Host Name.
     * @param root {@link Parent} root to loop up the text on Welcome Screen
     */
    private void netStat(Parent root) {
        Text txt = (Text) root.lookup("#ConnectionInfoText");
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            String ipAddress = inetAddress.getHostAddress();
            String hostName = inetAddress.getHostName();
            txt.setText("IP Address: " + ipAddress + " Host Name: " + hostName);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method called by clicking the Scenario 1 button, which launches the according screen.
     * @throws IOException when the FXML file can't be loaded
     */
    @FXML
    public void launchScenarioOne() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/GUI/ScenarioOne.fxml"));
        Stage primaryStage = Main.getStage();
        primaryStage.setScene(new Scene(root));
        primaryStage.centerOnScreen();
    }

    /**
     * Run button controller that changes the colour and text of the {@link Button} on click.
     * @param event
     */
    @FXML
    public void runButtonController(ActionEvent event) {
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
}
