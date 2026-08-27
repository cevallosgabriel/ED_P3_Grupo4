package com.espol.proyectoestruturadatos.dstructure;

import com.espol.proyectoestruturadatos.model.board.Board;
import com.espol.proyectoestruturadatos.model.board.Symbol;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Gabriel Cevallos, Dylan Jeanpier Pincay Salazar, Helen Cruz
 */
public class Tree<E> {
    private TreeNode<E> root;

    public Tree(E data) {
        this.root = new TreeNode<>(data, -1, 0, true);
    }

    public TreeNode<E> getRoot() {
        return root;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public TreeNode<E> addChild(TreeNode<E> parent, E data, int movement, boolean maximizing) {
        TreeNode<E> child = new TreeNode<>(data, movement, parent.getDepth() + 1, maximizing);
        parent.addChild(child);
        return child;
    }

    public static Tree<Board> buildDecisionTree(Board board, Symbol computerSymbol, Symbol humanSymbol) {
        Tree<Board> tree = new Tree<>(new Board(board));
        TreeNode<Board> root = tree.getRoot();

        for (int moveL1 : board.getAvailableMovements()) {
            Board boardL1 = new Board(board);
            boardL1.setSymbol(computerSymbol, moveL1);

            TreeNode<Board> nodeL1 = tree.addChild(root, boardL1, moveL1, false);

            if (!boardL1.hasEnded && !boardL1.isFull()) {
                for (int moveL2 : boardL1.getAvailableMovements()) {
                    Board boardL2 = new Board(boardL1);
                    boardL2.setSymbol(humanSymbol, moveL2);
                    tree.addChild(nodeL1, boardL2, moveL2, true);
                }
            }
        }
        return tree;
    }
}
