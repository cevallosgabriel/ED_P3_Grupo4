package com.espol.proyectoestruturadatos.model.board;

/**
 * @author Gabriel Cevallos, Dylan Jeanpier Pincay Salazar, Helen Cruz
 */
public class Box {
    private Symbol symbol;

    public Box() {
        this.symbol = null;
    }

    public Box(Box box) {
        if (box != null && box.symbol != null) {
            this.symbol = new Symbol(box.symbol.getCharacter(), box.symbol.getColor());
        } else {
            this.symbol = null;
        }
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
    }

    public boolean isEmpty() {
        return symbol == null;
    }
}
