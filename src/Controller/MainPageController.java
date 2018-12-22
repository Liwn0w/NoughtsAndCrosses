package Controller;

import Model.Game;
import Model.Gateway;
import Model.Player;
import Model.Symbol;
import Server.ChatConstants;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static javafx.scene.text.TextAlignment.CENTER;

public class MainPageController implements Initializable {

    @FXML
    Button startButton;
    @FXML
    TextField username;
    boolean usernameIsChanged;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Gateway gateway = Gateway.getInstance();
        PageChanger p = new PageChanger();
        usernameIsChanged = false;
        //insert an onlick that empties the username textfield, once clicked on it

        username.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (!usernameIsChanged) {
                    username.clear();
                    usernameIsChanged=true;
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
        private Label player2;
        private GridPane gameGrid; //gamegrid for both players
        private int N; // How many comments we have read
        private int S; // how many symbols we have send
        private boolean gameStarted;
        private Symbol lastSymbol; //used to check same symbol can only be added once, then switch

        /**
         * Construct a thread
         */
        public TranscriptCheck(Gateway gateway) {
            this.gateway = gateway;
            this.textArea = gateway.getTextArea();
            this.player2 = gateway.getPlayer2();
            this.gameGrid = gateway.getGameGrid();
            this.N = 0;
            this.S = 0;
            gameStarted = false;
        }

        /**
         * Run a thread
         */
        public void run() {
            while (true) {
                try {
                    //Change later when there are many players -
                    //to check that every one has a partner, e.g. partner %2 is 0
                    if(!gameStarted && gateway.getPlayerNo()>=2 && gateway.getPlayerNo()%2==0) {
                        Player secondPlayer = gateway.getPlayer(gateway.getPlayerNo());
                        Platform.runLater(()-> {
                            if(secondPlayer.getUsername().equals(gateway.getCurrentPlayer().getUsername())) {
                            player2.setText("You: "+secondPlayer.getUsername());
                            } else {
                                player2.setText("Opponent: "+secondPlayer.getUsername());
                            }
                        });
                        gameStarted = true;
                    }
                    try {
                        if(gameStarted && gateway.getSymbolCount()> S)
                        {
                            Symbol s = gateway.getLatestSymbol();
                            if(lastSymbol == null || s.getVal()!=lastSymbol.getVal()) {
                                Platform.runLater(() ->{
                                    addSymbol(s, gameGrid);

                                });
                                lastSymbol = s;
                                S++;
                            } else System.out.println("Not switching players");

                            /*if(lastSymbol==null || lastSymbol.getVal()!=gateway.getCurrentPlayer().getType()) {
                                System.out.println(gateway.getCurrentPlayer().getUsername() +" label green");
                            }*/
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

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
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void addSymbol(Symbol s, GridPane g) {
        Label l = new Label();
        l.setText(""+s.getVal());
        l.setStyle(" -fx-font-size: 30");
        l.setTextAlignment(CENTER);
        l.setTextFill(Color.color(1,1,1));
        //System.out.printf("Server added symbol at [%d, %d]%n", s.getColIndex(), s.getRowIndex());
        //gameGrid.getChildren().remove(getNodeByRowColumnIndex(s.getRowIndex(),s.getColIndex(),g));
        StackPane pane = new StackPane();
        pane.getChildren().add(l);
        pane.setAlignment(l, Pos.CENTER);
        Game.getInstance().addSymbol(s);
        g.add(pane,s.getColIndex(),s.getRowIndex());
    }
}
