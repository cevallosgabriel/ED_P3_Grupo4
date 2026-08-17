package com.espol.proyectoestruturadatos.model.player;

import com.espol.proyectoestruturadatos.model.board.Board;
import com.espol.proyectoestruturadatos.model.board.Symbol;

/**
 * @author Cevallos Guzman Gabriel Abraham.
 * @author Cruz Macias Helen Romina.
 * @author Pincay Salazar Dylan Jeanpier.
 */

public class Human extends Player {

    public Human(Symbol symbol) {
        super(symbol);
    }

    public void playTurn(Board board, int index) {
        board.setSymbol(getSymbol(), index);
    }
}