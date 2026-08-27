package com.espol.proyectoestruturadatos.model.player;

import com.espol.proyectoestruturadatos.model.board.Board;
import com.espol.proyectoestruturadatos.model.board.Symbol;

/**
 * @author Gabriel Cevallos, Dylan Jeanpier Pincay Salazar, Helen Cruz
 */
public class Human extends Player {

    public Human(Symbol symbol) {
        super(symbol);
    }

    public void playTurn(Board board, int index) {
        board.setSymbol(getSymbol(), index);
    }
}
