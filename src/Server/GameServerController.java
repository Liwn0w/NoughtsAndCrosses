package Server;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class GameServerController implements Initializable {
    @FXML
    private TextArea chatArea;

    private int playerNo = 0;
    private Chat chat;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        chat = new Chat();
        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(8000);

                while (true) {
                    Socket socket = serverSocket.accept();
                    playerNo++;
                    Platform.runLater(() -> {
                        chatArea.appendText("Started thread for player "+ playerNo + "\n");
                    });

                    new Thread(new HandleAClient(socket, chat, chatArea)).start();

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    class HandleAClient implements Runnable, ChatConstants {
        private Socket socket; // A connected socket
        private Chat chat; // Reference to shared chat
        private TextArea chatArea;
        private String userName;

        public HandleAClient(Socket socket, Chat chat, TextArea chatArea) {
            this.socket = socket;
            this.chat = chat;
            this.chatArea = chatArea;
        }

        public void run() {
            try {
                // Create reading and writing streams
                BufferedReader inputFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter outputToClient = new PrintWriter(socket.getOutputStream());

                // Continuously serve the client
                while (true) {
                    // Receive request code from the client
                    int request = Integer.parseInt(inputFromClient.readLine());
                    // Process request
                    switch (request) {
                        case SEND_USERNAME: {
                            userName = inputFromClient.readLine();
                            break;
                        }
                        case SEND_COMMENT: {
                            String comment = inputFromClient.readLine();
                            chat.addComment(userName + "> " + comment);
                            break;
                        }
                        case GET_COMMENT_COUNT: {
                            outputToClient.println(chat.getSize());
                            outputToClient.flush();
                            break;
                        }
                        case GET_COMMENT: {
                            int n = Integer.parseInt(inputFromClient.readLine());
                            outputToClient.println(chat.getComment(n));
                            outputToClient.flush();
                        }
                    }
                }
            } catch (IOException ex) {
                Platform.runLater(() -> chatArea.appendText("Exception in client thread: " + ex.toString() + "\n"));
            }
        }
    }
}


