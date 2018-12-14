package Model;

import javafx.concurrent.Task;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

class HandleAClient extends Task {
    private Socket socket;
    private ServerSymbolList symbolList;
    private ServerPlayerList playerList;

    public HandleAClient(Socket socket, ServerSymbolList symbolList, ServerPlayerList playerList) {
        this.socket = socket;
        this.symbolList = symbolList;
        this.playerList = playerList;
    }

    /*public void run() {
        try {
            ObjectInputStream objectsFromClient = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream objectsToClient = new ObjectOutputStream(socket.getOutputStream());
            Player player1 = (Player) objectsFromClient.readObject();
            playerList.addPlayer(player1);
        while(true) {
            if(playerList.getPlayerList().size()<2) {
                objectsToClient.writeObject(playerList);
                objectsToClient.flush();
            } else break;
                //Connect game with symbolList
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }*/

    @Override
    protected Object call() throws Exception {
        try {
            ObjectInputStream objectsFromClient = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream objectsToClient = new ObjectOutputStream(socket.getOutputStream());
            Player player1 = (Player) objectsFromClient.readObject();
            playerList.addPlayer(player1);
            updateValue(playerList);
            objectsToClient.writeObject(playerList);
            objectsToClient.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return playerList;
    }
}
