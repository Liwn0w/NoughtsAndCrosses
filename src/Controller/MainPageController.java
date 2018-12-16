package Controller;

import Model.Game;
import Model.Gateway;
import Model.Player;
import Server.ChatConstants;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainPageController implements Initializable {

    @FXML
    Button startButton;
    @FXML
    TextField username;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Gateway gateway = Gateway.getInstance();
        PageChanger p = new PageChanger();
        //insert an onlick that empties the username textfield, once clicked on it

        username.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (username.getText().equals("Insert username")) {
                    username.setText("");
                }
            }
        });

        //insert reference to next page from startbutton
        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    //Game game = new Game();
                    gateway.sendUsername(username.getText());
                    p.changeScreen(event, "/View/GamePage.fxml");
                    new Thread(new TranscriptCheck(gateway)).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    class TranscriptCheck implements Runnable, ChatConstants {
        private Gateway gateway; // Gateway to the server
        private TextArea textArea; // Where to display comments
        private int N; // How many comments we have read

        /**
         * Construct a thread
         */
        public TranscriptCheck(Gateway gateway) {
            this.gateway = gateway;
            this.textArea = gateway.getTextArea();
            this.N = 0;
        }

        /**
         * Run a thread
         */
        public void run() {
            while (true) {
                if (gateway.getCommentCount() > N) {
                    String newComment = gateway.getComment(N);
                    Platform.runLater(() -> textArea.appendText(newComment + "\n"));
                    N++;
                } else {
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException ex) {
                    }
                }
            }
        }
    }
}
