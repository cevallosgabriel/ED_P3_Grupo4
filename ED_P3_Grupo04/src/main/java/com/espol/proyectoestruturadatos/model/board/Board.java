package com.espol.proyectoestruturadatos.model.board;

import com.espol.proyectoestruturadatos.model.board.iterators.IteratorByColumn;
import com.espol.proyectoestruturadatos.model.board.iterators.IteratorByDiagonal;
import com.espol.proyectoestruturadatos.model.board.iterators.IteratorByRow;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Cevallos Guzman Gabriel Abraham.
 * @author Cruz Macias Helen Romina.
 * @author Pincay Salazar Dylan Jeanpier.
 */
public class Board {
    public final Box[] boxes = new Box[9];
    private Symbol winner;
    public boolean hasEnded;

    public Board() {
        this.hasEnded = false;
        this.winner = null;
        for (int i = 0; i < boxes.length; i++) {
            boxes[i] = new Box();
        }
    }

    public Board(Board board) {
        this();
        for (int i = 0; i < boxes.length; i++) {
            if (board.boxes[i] != null && !board.boxes[i].isEmpty()) {
                this.boxes[i].setSymbol(board.boxes[i].getSymbol());
            }
        }
        this.winner = board.winner;
        this.hasEnded = board.hasEnded;
    }

    public Symbol getWinner() {
        return winner;
    }

    public void setWinner(Symbol winner) {
        this.winner = winner;
    }

    public boolean isFull() {
        for (Box box : boxes) {
            if (box.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public Iterator<Box[]> rowIterator() {
        return new IteratorByRow(this);
    }

    public Iterator<Box[]> columnIterator() {
        return new IteratorByColumn(this);
    }

    public Iterator<Box[]> diagonalIterator() {
        return new IteratorByDiagonal(this);
    }

    public boolean isWinner(Symbol symbol) {
        if (symbol == null) return false;
        @SuppressWarnings("unchecked")
        Iterator<Box[]>[] iterators = new Iterator[]{this.rowIterator(), this.columnIterator(), this.diagonalIterator()};
        for (Iterator<Box[]> it : iterators) {
            while (it.hasNext()) {
                Box[] subBoxes = it.next();
                if (this.checkLineWinner(symbol, subBoxes)) {
                    return true;
                }
            }
        }
        return false;
    }

    public int[] getWinningLineIndices(Symbol symbol) {
        if (symbol == null) return null;
        for (int r = 0; r < 3; r++) {
            if (checkLineWinner(symbol, new Box[]{boxes[r * 3], boxes[r * 3 + 1], boxes[r * 3 + 2]})) {
                return new int[]{r * 3, r * 3 + 1, r * 3 + 2};
            }
        }
        for (int c = 0; c < 3; c++) {
            if (checkLineWinner(symbol, new Box[]{boxes[c], boxes[c + 3], boxes[c + 6]})) {
                return new int[]{c, c + 3, c + 6};
            }
        }
        if (checkLineWinner(symbol, new Box[]{boxes[0], boxes[4], boxes[8]})) {
            return new int[]{0, 4, 8};
        }
        if (checkLineWinner(symbol, new Box[]{boxes[2], boxes[4], boxes[6]})) {
            return new int[]{2, 4, 6};
        }
        return null;
    }

    private boolean checkLineWinner(Symbol symbol, Box[] subBoxes) {
        int count = 0;
        for (Box box : subBoxes) {
            if (box.getSymbol() != null && box.getSymbol().equals(symbol)) {
                count++;
            }
        }
        return count == subBoxes.length;
    }

    public void setSymbol(Symbol symbol, int arrayIndex) {
        if (arrayIndex >= 0 && arrayIndex < 9 && boxes[arrayIndex].isEmpty()) {
            boxes[arrayIndex].setSymbol(symbol);

            final boolean winnerFound = this.isWinner(symbol);
            final boolean full = this.isFull();

            if (winnerFound) {
                this.winner = symbol;
            }

            if (winnerFound || full) {
                this.hasEnded = true;
            }
        }
    }

    public void clearBox(int arrayIndex) {
        if (arrayIndex >= 0 && arrayIndex < 9) {
            boxes[arrayIndex].setSymbol(null);
            this.winner = null;
            this.hasEnded = false;
        }
    }

    public List<Integer> getAvailableMovements() {
        List<Integer> moves = new ArrayList<>();
        for (int i = 0; i < boxes.length; i++) {
            if (boxes[i].isEmpty()) {
                moves.add(i);
            }
        }
        return moves;
    }

    public int calculateUtility(Symbol computerSymbol, Symbol humanSymbol) {
        if (isWinner(computerSymbol)) {
            return 1000;
        }
        if (isWinner(humanSymbol)) {
            return -1000;
        }

        int pComputer = countAvailableLines(computerSymbol, humanSymbol);
        int pHuman = countAvailableLines(humanSymbol, computerSymbol);

        return pComputer - pHuman;
    }

    private int countAvailableLines(Symbol player, Symbol opponent) {
        int count = 0;
        @SuppressWarnings("unchecked")
        Iterator<Box[]>[] iterators = new Iterator[]{this.rowIterator(), this.columnIterator(), this.diagonalIterator()};
        for (Iterator<Box[]> it : iterators) {
            while (it.hasNext()) {
                Box[] subBoxes = it.next();
                if (isLineAvailable(subBoxes, opponent)) {
                    count++;
                }
            }
        }
        return count;
    }

    private boolean isLineAvailable(Box[] subBoxes, Symbol opponent) {
        for (Box box : subBoxes) {
            if (box.getSymbol() != null && box.getSymbol().equals(opponent)) {
                return false;
            }
        }
        return true;
    }
}
