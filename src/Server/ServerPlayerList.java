package Server;

import Model.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServerPlayerList implements Serializable {
    private List<Player> playerList = Collections.synchronizedList(new ArrayList<>());

    public ServerPlayerList(){}

    public void addPlayer(Player p) {
        playerList.add(p);
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public int getSize() { return playerList.size();}

    public Player getPlayer(int p) { return playerList.get(p);}

}
