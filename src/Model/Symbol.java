package Model;

import java.io.Serializable;

public class Symbol implements Serializable {
    private int colIndex, rowIndex;
    private char val;

    public Symbol(char val, int colIndex, int rowIndex) {
        this.val = val;
        this.colIndex = colIndex;
        this.rowIndex = rowIndex;
    }

    public int getColIndex() {
        return colIndex;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public char getVal() {
        return val;
    }
}
