package com.espol.proyectoestruturadatos.model.player;

import com.espol.proyectoestruturadatos.model.board.Symbol;

/**
 * @author Gabriel Cevallos, Dylan Jeanpier Pincay Salazar, Helen Cruz
 */
public abstract class Player {
    private Symbol symbol;

    public Player(Symbol symbol) {
        this.symbol = symbol;
    }

    public Symbol getSymbol() {
        return symbol;
    }
}
