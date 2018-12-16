package Controller;

import Model.Gateway;
import Model.Player;
import Model.Symbol;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;


import java.io.IOException;
import java.util.ArrayList;

import static javafx.scene.text.TextAlignment.CENTER;

public class GamePageController {
    @FXML
    GridPane gameGrid;
    @FXML
    Label player1,player2;
    @FXML
    Button chatButton;
    @FXML
    TextArea chatArea;
    @FXML
    TextField chatField;

    private Gateway gateway;

    public void initialize() {
        gateway = Gateway.getInstance();
        gateway.setTextArea(chatArea);

        chatButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                sendComment(event);
            }
        });

        //Get usernames after chat is successfully implemented
        /*try {

            ArrayList<Player> players = gateway.getPlayers();
            player1.setText(players.get(0).getUsername());
            player2.setText(players.get(1).getUsername());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }*/

        for (int i = 0 ; i < 9 ; i++) {
            for (int j = 0; j < 9; j++) {
                addSymbol(i, j);
            }
        }
    }

    @FXML
    private void sendComment(ActionEvent event) {
        String text = chatField.getText();
        gateway.sendComment(text);
    }

    private void addSymbol(int colIndex, int rowIndex) {
        StackPane pane = new StackPane();
        pane.setOnMouseClicked(e -> {
            Label l = new Label();
            //change to char of user
            //l.setText(""+gateway.getCurrentPlayer().getType());
            l.setText("X");
            l.setStyle(" -fx-font-size: 30");
            l.setTextAlignment(CENTER);
            pane.getChildren().add(l);
            pane.setAlignment(l, Pos.CENTER);
            l.setTextFill(Color.color(1,1,1));
            Symbol s = new Symbol('O',colIndex,rowIndex);
            //ServerSymbolList.getInstance().addSymbol(s);
            System.out.printf("Mouse clicked cell [%d, %d]%n", colIndex, rowIndex);
        });
        gameGrid.add(pane, colIndex, rowIndex);
    }


}
