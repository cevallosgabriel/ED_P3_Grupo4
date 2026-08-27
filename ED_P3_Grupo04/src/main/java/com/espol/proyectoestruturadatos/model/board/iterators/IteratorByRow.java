package com.espol.proyectoestruturadatos.model.board.iterators;

import com.espol.proyectoestruturadatos.model.board.Board;
import com.espol.proyectoestruturadatos.model.board.Box;
import java.util.Iterator;

/**
 * @author Gabriel Cevallos, Dylan Jeanpier Pincay Salazar, Helen Cruz
 */
public class IteratorByRow implements Iterator<Box[]> {

    private Box[] boxes;
    private int index;

    public IteratorByRow(Board board) {
        this.boxes = board.boxes;
        this.index = 0;
    }

    @Override
    public boolean hasNext() {
        return index < 3;
    }

    @Override
    public Box[] next() {
        Box[] row = new Box[3];

        for (int i = 0; i < 3; i++) {
            row[i] = boxes[index * 3 + i];
        }

        index++;
        return row;
    }
}
