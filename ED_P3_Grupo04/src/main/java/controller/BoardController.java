package controller;

import com.espol.proyectoestruturadatos.model.board.Board;
import com.espol.proyectoestruturadatos.model.board.Symbol;
import com.espol.proyectoestruturadatos.model.player.Bot;
import com.espol.proyectoestruturadatos.model.player.Human;

/**
 * @author Cevallos Guzman Gabriel Abraham.
 * @author Cruz Macias Helen Romina.
 * @author Pincay Salazar Dylan Jeanpier.
 */

public class BoardController {

    private Board board;
    private Human human;
    private Bot bot;
    private boolean isHumanTurn;

    public BoardController(Symbol humanSymbol, Symbol botSymbol, boolean humanStarts) {
        this.board = new Board();
        this.human = new Human(humanSymbol);
        this.bot = new Bot(botSymbol);
        this.isHumanTurn = humanStarts;

        if (!humanStarts) {
            executeBotMove();
        }
    }

    public boolean makeHumanMove(int index) {
        if (!isHumanTurn || board.hasEnded) {
            return false;
        }

        if (board.boxes[index].isEmpty()) {
            human.playTurn(board, index);
            if (!board.hasEnded) {
                isHumanTurn = false;
                executeBotMove();
            }
            return true;
        }
        return false;
    }

    public int executeBotMove() {
        if (board.hasEnded) {
            return -1;
        }
        int moveIndex = bot.playTurn(board, human.getSymbol());
        isHumanTurn = true;
        return moveIndex;
    }

    public Board getBoard() {
        return board;
    }

    public boolean isHumanTurn() {
        return isHumanTurn;
    }

    public void setHumanTurn(boolean isHumanTurn) {
        this.isHumanTurn = isHumanTurn;
    }

    public boolean isGameOver() {
        return board.hasEnded || board.isFull();
    }

    public Symbol getWinner() {
        return board.getWinner();
    }
}
