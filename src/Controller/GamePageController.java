package Controller;

import Model.Game;
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
    private ArrayList<Player> players;
    private Game game;

    public void initialize() {
        gateway = Gateway.getInstance();
        game = Game.getInstance();
        gateway.setTextArea(chatArea);
        gateway.setPlayer2(player2);
        gateway.setGameGrid(gameGrid);
        players = new ArrayList<>();
        try {
            players = updatePlayers();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        chatButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    sendComment(event);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        //Get usernames after chat is successfully implemented
            player1.setText(players.get(0).getUsername());
            if(players.size()<2) {
                player2.setText("Waiting...");
            } else {
                player2.setText(players.get(1).getUsername());
            }

        for (int i = 0 ; i < 9 ; i++) {
            for (int j = 0; j < 9; j++) {
                initializeGrid(i,j);
            }
        }

    }

    public void initializeGrid(int i, int j) {
        StackPane pane = new StackPane();
        pane.setOnMouseClicked(e -> {
            try {
                if(!gateway.isFirstSymbolSent() || gateway.getLatestSymbol().getVal()!=gateway.getCurrentPlayer().getType()) {
                    Symbol s = new Symbol(gateway.getCurrentPlayer().getType(),i,j);
                    Label l = new Label();
                    l.setText(""+s.getVal());
                    l.setStyle(" -fx-font-size: 30");
                    l.setTextAlignment(CENTER);
                    pane.getChildren().add(l);
                    pane.setAlignment(l, Pos.CENTER);
                    try {
                        gateway.sendSymbol(s);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    l.setTextFill(Color.color(1,1,1));
                    game.addSymbol(s);
                    System.out.printf("Mouse clicked cell [%d, %d]%n", s.getColIndex(), s.getRowIndex());
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            }
        });
        gameGrid.add(pane,i, j);
    }

    private ArrayList<Player> updatePlayers() throws IOException, ClassNotFoundException {
        int no = gateway.getPlayerNo();
        if(players.size()==0 && no>=2) {
            players.add(gateway.getPlayer(no-1));
        }
        players.add(gateway.getPlayer(no));
        return players;
    }

    @FXML
    private void sendComment(ActionEvent event) throws IOException {
        String text = chatField.getText();
        gateway.sendComment(text);
        chatField.setText("");

    }

}
