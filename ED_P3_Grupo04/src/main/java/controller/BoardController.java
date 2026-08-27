package controller;

import com.espol.proyectoestruturadatos.model.board.Board;
import com.espol.proyectoestruturadatos.model.board.Symbol;
import com.espol.proyectoestruturadatos.model.player.Bot;
import com.espol.proyectoestruturadatos.model.player.Human;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

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
    private Stack<Integer> moveHistory;

    public BoardController(Symbol humanSymbol, Symbol botSymbol, boolean humanStarts) {
        this.board = new Board();
        this.human = new Human(humanSymbol);
        this.bot = new Bot(botSymbol);
        this.isHumanTurn = humanStarts;
        this.moveHistory = new Stack<>();
    }

    public boolean makeHumanMove(int index) {
        if (!isHumanTurn || board.hasEnded) {
            return false;
        }

        if (board.boxes[index].isEmpty()) {
            human.playTurn(board, index);
            moveHistory.push(index);
            if (!board.hasEnded) {
                isHumanTurn = false;
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
        if (moveIndex != -1) {
            moveHistory.push(moveIndex);
        }
        isHumanTurn = true;
        return moveIndex;
    }

    public boolean canUndo() {
        return moveHistory != null && moveHistory.size() >= 2;
    }

    public boolean undoLastTwoMoves() {
        if (!canUndo()) {
            return false;
        }
        int lastMove = moveHistory.pop();
        int previousMove = moveHistory.pop();

        board.clearBox(lastMove);
        board.clearBox(previousMove);

        isHumanTurn = true;
        return true;
    }

    public List<Integer> getMoveHistory() {
        return new ArrayList<>(moveHistory);
    }

    public void setMoveHistory(List<Integer> history) {
        moveHistory.clear();
        if (history != null) {
            moveHistory.addAll(history);
        }
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
