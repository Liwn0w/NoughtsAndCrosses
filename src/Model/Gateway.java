package Model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Gateway {
    private ObjectInputStream objectsFromServer;
    private ObjectOutputStream objectsToServer;
    private static Gateway instance;
    private Player currentPlayer;
    private ArrayList<Player> players;

    public Gateway() {
         players = new ArrayList<>();
        try {
            Socket socket = new Socket("localhost", 4321);
            System.out.println("Connected to server");
            objectsToServer = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("outputstream done");
            objectsFromServer = new ObjectInputStream(socket.getInputStream());
            System.out.println("constructor done");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendPlayer(Player p) throws IOException {
        objectsToServer.writeObject(p);
        currentPlayer = p;
        System.out.println("Player sent to server");
    }

    public int getPlayerListSize() {
        return players.size();
    }

    public ArrayList<Player> getPlayers() throws IOException, ClassNotFoundException {
        //Player player1 = new Player("lily");
        while(true) {
            ServerPlayerList playerList = (ServerPlayerList) objectsFromServer.readObject();
            if (playerList.getPlayerList().size() < 2) {
                System.out.println("Waiting for second player...");
            } else {
                players.add(playerList.getPlayerList().get(0));
                players.add(playerList.getPlayerList().get(1));
                players.get(0).setType('X');
                players.get(1).setType('O');
                System.out.println(players.get(0).getUsername() + " is " + players.get(0).getType());
                System.out.println(players.get(1).getUsername() + " is " + players.get(1).getType());
                return players;
            }
        }
    }
    public static Gateway getInstance() {
        if(instance==null) instance = new Gateway();
        return instance;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public static void deleteInstance() {
        instance = null;
    }
}
