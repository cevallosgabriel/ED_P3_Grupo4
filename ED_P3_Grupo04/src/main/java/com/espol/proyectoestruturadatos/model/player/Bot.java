package com.espol.proyectoestruturadatos.model.player;

import com.espol.proyectoestruturadatos.dstructure.Minimax;
import com.espol.proyectoestruturadatos.model.board.Board;
import com.espol.proyectoestruturadatos.model.board.Symbol;

/**
 * @author Gabriel Cevallos, Dylan Jeanpier Pincay Salazar, Helen Cruz
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
