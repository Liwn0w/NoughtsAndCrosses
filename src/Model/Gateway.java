package Model;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.io.*;
import java.net.Socket;

import static Server.ChatConstants.*;
import static javafx.scene.text.TextAlignment.CENTER;

public class Gateway {
    private ObjectInputStream inputFromServer;
    private ObjectOutputStream outputToServer;

    //private PrintWriter outputToServer;
    //private BufferedReader inputFromServer;
    private static Gateway instance;
    private TextArea textArea;
    private Label player2;
    private GridPane gameGrid;
    private Player currentPlayer;
    private boolean isFirstSymbolSent;

    public Gateway() {
        try {
            isFirstSymbolSent = false;
            Socket socket = new Socket("localhost", 8000);

            // Create an output stream to send data to the server
            outputToServer = new ObjectOutputStream(socket.getOutputStream());

            // Create an input stream to read data from the server
            inputFromServer = new ObjectInputStream(socket.getInputStream());

            /*System.out.println("Connected to server");
            objectsToServer = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("outputstream done");
            objectsFromServer = new ObjectInputStream(socket.getInputStream());
            System.out.println("constructor done");*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isFirstSymbolSent() {
        return isFirstSymbolSent;
    }

    public void setTextArea(TextArea textArea) {
        this.textArea = textArea;
    }

    public void setPlayer2(Label player2) {this.player2 = player2;}

    public void setGameGrid(GridPane g) {gameGrid = g;}

    // Start the chat by sending in the user's handle.
    public void sendUsername(String username) throws IOException {
        outputToServer.writeObject(Integer.toString(SEND_USERNAME));
        Player currentPlayer = new Player(username);
        this.currentPlayer = currentPlayer;
        outputToServer.writeObject(currentPlayer);
        outputToServer.flush();
    }

    // Send a new comment to the server.
    public void sendComment(String comment) throws IOException {
        outputToServer.writeObject(Integer.toString(SEND_COMMENT));
        outputToServer.writeObject(comment);
        outputToServer.flush();
    }

    public void sendSymbol(Symbol s) throws IOException {
        outputToServer.writeObject(Integer.toString(SEND_SYMBOL));
        outputToServer.writeObject(s);
        outputToServer.flush();
        isFirstSymbolSent = true;
    }

    // Ask the server to send us a count of how many comments are
    // currently in the transcript.
    public int getCommentCount() throws IOException, ClassNotFoundException {
        outputToServer.writeObject(Integer.toString(GET_COMMENT_COUNT));
        outputToServer.flush();
        int count = 0;
        count = Integer.parseUnsignedInt((String) inputFromServer.readObject());
        return count;
    }

    // Fetch comment n of the transcript from the server.
    public String getComment(int n) throws IOException, ClassNotFoundException {
        outputToServer.writeObject(Integer.toString(GET_COMMENT));
        outputToServer.writeObject(Integer.toString(n));
        outputToServer.flush();
        String comment = (String) inputFromServer.readObject();
        return comment;
    }

    public Symbol getLatestSymbol() throws IOException, ClassNotFoundException {
        Symbol s = null;
        outputToServer.writeObject(Integer.toString(GET_SYMBOL));
        outputToServer.flush();
        s = (Symbol) inputFromServer.readObject();
        return s;
    }

    public int getPlayerNo() throws IOException, ClassNotFoundException {
        outputToServer.writeObject(Integer.toString(GET_PLAYER_NO));
        outputToServer.flush();
        return Integer.parseUnsignedInt((String) inputFromServer.readObject());
    }

    public Player getPlayer(int n) throws IOException, ClassNotFoundException {
        outputToServer.writeObject(Integer.toString(GET_PLAYER));
        outputToServer.writeObject(Integer.toString(n-1));
        outputToServer.flush();
        Player serverPlayer = (Player) inputFromServer.readObject();
        /*Error if two players have same username*/
        if(serverPlayer.getUsername().equals(currentPlayer.getUsername())) currentPlayer.setType(serverPlayer.getType());
        return serverPlayer;
    }

    public static Gateway getInstance() {
        if(instance==null) instance = new Gateway();
        return instance;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public TextArea getTextArea() {
        return textArea;
    }

    public Label getPlayer2() {
        return player2;
    }

    public GridPane getGameGrid() {
        return gameGrid;
    }

    public static void deleteInstance() {
        instance = null;
    }

    public int getSymbolCount() throws IOException, ClassNotFoundException {
        outputToServer.writeObject(Integer.toString(GET_SYMBOL_COUNT));
        outputToServer.flush();
        int symbolCount = 0;
        symbolCount = Integer.parseUnsignedInt((String) inputFromServer.readObject());
        return symbolCount;
    }




}
