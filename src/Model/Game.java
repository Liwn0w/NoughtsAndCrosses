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
    private char[][] board;
    private char[][] winBoard; //board of winners of each game

    public Game() {
        board = new char[9][9];
        winBoard = new char[3][3];
    }

    public void addSymbol(Symbol s) {
        board[s.getRowIndex()][s.getColIndex()] = s.getVal();
        System.out.println("Winboard: ");
        checkWinBoard(s);
        for(char[] row : winBoard) {
            printRow(row);
        }

        System.out.println("MainBoard: ");
        for(char[] row : board) {
            printRow(row);
        }
    }

    public static Game getInstance() {
        if(instance== null) instance = new Game();
        return instance;
    }

    //check row if all symbols are the same, but check only from colStart to colEnd
    private boolean checkRow(Symbol s, int row, int col, char[][] board) {
        char val = s.getVal();
        if(col<=2) {
            if(board[row][0] == val && board[row][1]==val && board[row][2]==val) {
                return true;
            } else return false;
        } else if(col> 2 && col<=5) {
            if(board[row][3] == val && board[row][4]==val && board[row][5]==val) {
                return true;
            } else return false;
        } else {
            if(board[row][6] == val && board[row][7]==val && board[row][8]==val) {
                return true;
            } else return false;
        }
    }

    //Check col in big board
    private boolean checkCol(Symbol s, int row, int col, char[][] board) {
        char val = s.getVal();
        if(row<=2) {
            if(board[0][col] == val && board[1][col]==val && board[2][col]==val) {
                return true;
            } else return false;
        } else if(row>2 && row<=5) {
            if(board[3][col] == val && board[4][col]==val && board[5][col]==val) {
                return true;
            } else return false;
        } else {
            if(board[6][col] == val && board[7][col]==val && board[8][col]==val) {
                return true;
            } else return false;
        }
    }

    private boolean checkTiltedWinBoard(Symbol s) {
        char val = s.getVal();
        if(winBoard[0][0] == val && winBoard[1][1] == val && winBoard[2][2] == val
                    || winBoard[0][2] == val && winBoard[1][1] == val && winBoard[2][0] == val) return true;
        else return false;
        }

    private boolean checkTilted(Symbol s, int row, int col, char[][] board) {
        char val = s.getVal();
        if(row<=2) {
            if(col<=2&& board[0][0] == val && board[1][1] == val && board[2][2] == val
                    || board[0][2] == val && board[1][1] == val && board[2][0] == val) return true;
            else if (col> 2 && col<=5 && board[0][3] == val && board[1][4] == val && board[2][5] == val
                    || board[0][5] == val && board[1][4] == val && board[2][3] == val) return true;
            else if (col > 5 && col<=8 && board[0][6] == val && board[1][7] == val && board[2][8] == val
                    || board[0][8] == val && board[1][7] == val && board[2][6] == val) return true;
            else return false;
        } else if(row>2 && row<=5) {
            if(col<=2&& board[3][0] == val && board[4][1] == val && board[5][2] == val
                    || board[3][2] == val && board[4][1] == val && board[5][0] == val) return true;
            else if (col<=5 && board[3][3] == val && board[4][4] == val && board[5][5] == val
                    || board[3][5] == val && board[4][4] == val && board[5][3] == val) return true;
            else if (col<=8 && board[3][6] == val && board[4][7] == val && board[5][8] == val
                    || board[3][8] == val && board[4][7] == val && board[5][6] == val) return true;
            else return false;
        } else {
            if(col<=2&& board[6][0] == val && board[7][1] == val && board[8][2] == val
                    || board[6][2] == val && board[7][1] == val && board[8][0] == val) return true;
            else if (col<=5 && board[6][3] == val && board[7][4] == val && board[8][5] == val
                    || board[6][5] == val && board[7][4] == val && board[8][3] == val) return true;
            else if (col<=8 && board[6][6] == val && board[7][7] == val && board[8][8] == val
                    || board[6][8] == val && board[7][7] == val && board[8][6] == val) return true;
            else return false;
        }

    }

    /*Check for winners in 9x9 board*/
    private void checkBoard(Symbol s) {
        /*Check for all winning combinations for the position of new symbol*/
        int row = s.getRowIndex();
        int col = s.getColIndex();
        int winRow;
        int winCol;
        if (checkCol(s,row,col,board) || checkRow(s,row,col,board) || checkTilted(s,row,col,board)) {
            if(col<=2) winCol = 0;
            else if (col<=5 && col > 2) winCol = 1;
            else winCol = 2;

            if(row<=2) winRow = 0;
            else if (row<=5 && row>2) winRow = 1;
            else winRow = 2;

            if(winBoard[winRow][winCol]==0) {
                winBoard[winRow][winCol] = s.getVal();
                System.out.println("One minor board is won");
            }
        }
    }

    /*Check for winners in winBoard*/
    private void checkWinBoard(Symbol s) {
        checkBoard(s);
        //check 3x3 winboard and send to server "Game over, winner is: +currentplayer"
        if(checkCol(s,0,0, winBoard) ||
                checkCol(s,0,1, winBoard) ||
                checkCol(s,0,2, winBoard) ||
                checkRow(s,0,0, winBoard) ||
                checkRow(s,1,0, winBoard) ||
                checkRow(s,2,0, winBoard) ||
                checkTiltedWinBoard(s)){
            //set char in winboard if won.
            try {
                Gateway.getInstance().sendGameOver();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void printRow(char[] row) {
        for (char i : row) {
            System.out.print(i);
            System.out.print("\t");
        }
        System.out.println();
    }


}
