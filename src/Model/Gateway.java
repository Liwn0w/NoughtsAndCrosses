package Model;

import Server.ServerPlayerList;
import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

import static Server.ChatConstants.*;

public class Gateway {
    //private ObjectInputStream objectsFromServer;
    //private ObjectOutputStream objectsToServer;

    private PrintWriter outputToServer;
    private BufferedReader inputFromServer;
    private static Gateway instance;
    private TextArea textArea;

    public Gateway() {
        try {
            Socket socket = new Socket("localhost", 8000);

            // Create an output stream to send data to the server
            outputToServer = new PrintWriter(socket.getOutputStream());

            // Create an input stream to read data from the server
            inputFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));

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

    // Start the chat by sending in the user's handle.
    public void sendUsername(String username) {
        outputToServer.println(SEND_USERNAME);
        outputToServer.println(username);
        outputToServer.flush();
    }

    // Send a new comment to the server.
    public void sendComment(String comment) {
        outputToServer.println(SEND_COMMENT);
        outputToServer.println(comment);
        outputToServer.flush();
    }

    // Ask the server to send us a count of how many comments are
    // currently in the transcript.
    public int getCommentCount() {
        outputToServer.println(GET_COMMENT_COUNT);
        outputToServer.flush();
        int count = 0;
        try {
            count = Integer.parseInt(inputFromServer.readLine());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return count;
    }

    // Fetch comment n of the transcript from the server.
    public String getComment(int n) {
        outputToServer.println(GET_COMMENT);
        outputToServer.println(n);
        outputToServer.flush();
        String comment = "";
        try {
            comment = inputFromServer.readLine();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return comment;
    }
    public static Gateway getInstance() {
        if(instance==null) instance = new Gateway();
        return instance;
    }


    public TextArea getTextArea() {
        return textArea;
    }

    public static void deleteInstance() {
        instance = null;
    }
}
