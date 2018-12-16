package Server;

import Model.Symbol;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServerSymbolList implements Serializable {

    private List<Symbol> symbolList = Collections.synchronizedList(new ArrayList<>());

    public ServerSymbolList(){}

    public void addSymbol(Symbol s) {
        symbolList.add(s);
    }

    public List<Symbol> getSymbolList() {
        return symbolList;
    }


}
