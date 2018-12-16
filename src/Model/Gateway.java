package Model;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.io.*;
import java.net.Socket;

import static Server.ChatConstants.*;

public class Gateway {
    private ObjectInputStream inputFromServer;
    private ObjectOutputStream outputToServer;

    //private PrintWriter outputToServer;
    //private BufferedReader inputFromServer;
    private static Gateway instance;
    private TextArea textArea;
    private Label player2;

    public Gateway() {
        try {
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

    public void setTextArea(TextArea textArea) {
        this.textArea = textArea;
    }

    public void setPlayer2(Label player2) {this.player2 = player2;}

    // Start the chat by sending in the user's handle.
    public void sendUsername(String username) throws IOException {
        outputToServer.writeObject(Integer.toString(SEND_USERNAME));
        outputToServer.writeObject(new Player(username));
        outputToServer.flush();
    }

    // Send a new comment to the server.
    public void sendComment(String comment) throws IOException {
        outputToServer.writeObject(Integer.toString(SEND_COMMENT));
        outputToServer.writeObject(comment);
        outputToServer.flush();
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

    public int getPlayerNo() throws IOException, ClassNotFoundException {
        outputToServer.writeObject(Integer.toString(GET_PLAYER_NO));
        outputToServer.flush();
        return Integer.parseUnsignedInt((String) inputFromServer.readObject());
    }

    public Player getPlayer(int n) throws IOException, ClassNotFoundException {
        outputToServer.writeObject(Integer.toString(GET_PLAYER));
        outputToServer.writeObject(Integer.toString(n-1));
        outputToServer.flush();
        return (Player) inputFromServer.readObject();
    }

    public static Gateway getInstance() {
        if(instance==null) instance = new Gateway();
        return instance;
    }


    public TextArea getTextArea() {
        return textArea;
    }

    public Label getPlayer2() {
        return player2;
    }

    public static void deleteInstance() {
        instance = null;
    }
}
