package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameServer {
    //singleton instance
    private static GameServer instance;
    //list of symbols
    ServerSymbolList symbols;
    List<Player> playerList = Collections.synchronizedList(new ArrayList<>());

    //server things
    private static final int port = 4321;
    private ServerSocket ss = null;

    public void runServer() throws IOException, ClassNotFoundException {
            try {
                ServerSocket serverSocket = new ServerSocket(port);
                    while(true) {
                        Socket socket = serverSocket.accept();
                        Task<ObservableList<Player>> task = new Task<ObservableList<Player>>() {
                            @Override
                            protected ObservableList<Player> call() throws Exception {
                                ObservableList<Player> players = FXCollections.observableArrayList();
                                    ObjectOutputStream objectsToClient = new ObjectOutputStream(socket.getOutputStream());
                                    ObjectInputStream objectsFromClient = new ObjectInputStream(socket.getInputStream());
                                    Player player = (Player) objectsFromClient.readObject();
                                    players.add(player);
                                    System.out.println("player added");
                                    objectsToClient.writeObject(players);
                                    objectsToClient.flush();
                                    playerList.addAll(players);
                                return null;
                            }
                        };
                        new Thread(task).start();
                        //new HandleAClient(socket, symbols, players).call();
                    }
            }
            catch (IOException e) {
                System.out.println(e);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    public GameServer() throws IOException {
        //players = new ServerPlayerList();
        symbols = new ServerSymbolList();
        System.out.println("Waiting for players to connect");
    }


    public ServerSymbolList getSymbols() {
        return symbols;
    }

    public static GameServer getInstance() throws IOException {
        if (instance == null) instance = new GameServer();
        return instance;
    }

    public static void main(String[] args) {

                    try {
                        GameServer gs = GameServer.getInstance();
                       // ServerSymbolList symbolList = ServerSymbolList.getInstance();
                        //ServerSocket serverSocket = new ServerSocket(4321);
                        gs.runServer();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

    }
}


