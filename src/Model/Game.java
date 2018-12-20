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
    private static Game instance;

    public Game() {

    }

    public void addSymbol(Symbol s) {

    }

    public static Game getInstance() {
        if(instance== null) instance = new Game();
        return instance;
    }


}
