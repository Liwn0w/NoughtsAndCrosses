package Server;

import Model.Player;
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
    private ServerPlayerList playerList;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        chat = new Chat();
        playerList = new ServerPlayerList();

        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(8000);

                while (true) {
                    Socket socket = serverSocket.accept();
                    playerNo++;
                    Platform.runLater(() -> {
                        chatArea.appendText("Started thread for player "+ playerNo + "\n");
                    });

                    new Thread(new HandleAClient(socket, chat,playerList)).start();

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    class HandleAClient implements Runnable, ChatConstants {
        private Socket socket; // A connected socket
        private Chat chat; // Reference to shared chat
        private Player player;
        private ServerPlayerList playerList; //reference to shared playerList

        public HandleAClient(Socket socket, Chat chat, ServerPlayerList playerList) {
            this.socket = socket;
            this.chat = chat;
            this.playerList = playerList;
        }

        public void run() {
            try {
                // Create reading and writing streams
                //BufferedReader inputFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                //PrintWriter outputToClient = new PrintWriter(socket.getOutputStream());

                //create object streams
                ObjectInputStream inputFromClient = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream outputToClient = new ObjectOutputStream(socket.getOutputStream());

                // Continuously serve the client
                while (true) {
                    // Receive request code from the client
                    int request = Integer.parseUnsignedInt((String) inputFromClient.readObject());
                    System.out.println(request);
                    // Process request
                    switch (request) {
                        case SEND_USERNAME: {
                            player = (Player) inputFromClient.readObject();
                            playerList.addPlayer(player);

                            for(Player p: playerList.getPlayerList()) {
                                System.out.println(p.getUsername());
                            }
                            break;
                        }

                        case GET_PLAYER_NO: {
                            outputToClient.writeObject(Integer.toString(playerList.getSize()));
                            System.out.println("Get Comment count: "+ playerList.getSize());
                            outputToClient.flush();
                            break;
                        }

                        case GET_PLAYER: {
                            int p = Integer.parseUnsignedInt( (String) inputFromClient.readObject());
                            outputToClient.writeObject(playerList.getPlayer(p));
                            outputToClient.flush();
                            break;
                        }

                        case SEND_COMMENT: {
                            String comment = (String) inputFromClient.readObject();
                            System.out.println("comment: " +comment);
                            chat.addComment(player.getUsername() + "> " + comment);
                            break;
                        }
                        case GET_COMMENT_COUNT: {
                            outputToClient.writeObject(Integer.toString(chat.getSize()));
                            System.out.println("Get Comment count: "+ chat.getSize());
                            outputToClient.flush();
                            break;
                        }
                        case GET_COMMENT: {
                            int n = Integer.parseUnsignedInt( (String) inputFromClient.readObject());
                            outputToClient.writeObject(chat.getComment(n));
                            System.out.println("Get comment at n: " + chat.getComment(n));
                            outputToClient.flush();
                        }

                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}


