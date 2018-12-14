package Model;

import java.io.Serializable;

public class Player implements Serializable {
    private String username;
    private int wins;
    //X or O
    private char type;

    public Player(String username) {
        this.username = username;
    }

    public void setType(char type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public char getType() {
        return type;
    }
}
