package com.espol.proyectoestruturadatos.model.player;

import com.espol.proyectoestruturadatos.model.board.Symbol;

/**
 * @author Cevallos Guzman Gabriel Abraham.
 * @author Cruz Macias Helen Romina.
 * @author Pincay Salazar Dylan Jeanpier.
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
