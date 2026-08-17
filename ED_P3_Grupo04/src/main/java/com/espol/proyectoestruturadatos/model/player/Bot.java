package com.espol.proyectoestruturadatos.model.player;

import com.espol.proyectoestruturadatos.dstructure.Minimax;
import com.espol.proyectoestruturadatos.model.board.Board;
import com.espol.proyectoestruturadatos.model.board.Symbol;

/**
 * @author Cevallos Guzman Gabriel Abraham.
 * @author Cruz Macias Helen Romina.
 * @author Pincay Salazar Dylan Jeanpier.
 */

public class Bot extends Player {

    public Bot(Symbol symbol) {
        super(symbol);
    }

    public int playTurn(Board board, Symbol humanSymbol) {
        int bestMove = Minimax.getBestMove(board, getSymbol(), humanSymbol);
        if (bestMove != -1) {
            board.setSymbol(getSymbol(), bestMove);
        }
        return bestMove;
    }
}