package Controller;

import Model.Game;
import Model.Gateway;
import Model.Player;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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
                if(username.getText().equals("Insert username")) {
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
                    gateway.sendPlayer(new Player(username.getText()));
                    p.changeScreen(event, "/View/GamePage.fxml");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
