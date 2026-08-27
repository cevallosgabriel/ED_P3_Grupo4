package com.espol.proyectoestruturadatos.model.board.iterators;

import com.espol.proyectoestruturadatos.model.board.Board;
import com.espol.proyectoestruturadatos.model.board.Box;
import java.util.Iterator;

/**
 * @author Gabriel Cevallos, Dylan Jeanpier Pincay Salazar, Helen Cruz
 */
public class IteratorByDiagonal implements Iterator<Box[]> {

    private Box[] boxes;
    private int index;

    public IteratorByDiagonal(Board board) {
        this.boxes = board.boxes;
        this.index = 0;
    }

    @Override
    public boolean hasNext() {
        return index < 2;
    }

    @Override
    public Box[] next() {
        Box[] diagonal = new Box[3];

        if (index == 0) {
            diagonal[0] = boxes[0];
            diagonal[1] = boxes[4];
            diagonal[2] = boxes[8];
        } else {
            diagonal[0] = boxes[2];
            diagonal[1] = boxes[4];
            diagonal[2] = boxes[6];
        }

        index++;
        return diagonal;
    }
}
