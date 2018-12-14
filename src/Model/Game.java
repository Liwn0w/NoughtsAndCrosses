package Model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Game {
    private Player player1, player2;
    private boolean gameOver;
    private Player winner;
    //later feature? Change to actual time and not just int
    private int time;

    Socket socket;

    public Game() throws IOException {
        System.out.println("Player ready");
        socket = new Socket("localhost", 4321);
        System.out.println("Game connected to server");
    }

    public void start(Player p) throws IOException, ClassNotFoundException {

    }


}
