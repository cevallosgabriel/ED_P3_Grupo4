package com.espol.proyectoestruturadatos.dstructure;

import com.espol.proyectoestruturadatos.model.board.Board;
import com.espol.proyectoestruturadatos.model.board.Symbol;
import java.util.List;

/**
 * @author Gabriel Cevallos, Dylan Jeanpier Pincay Salazar, Helen Cruz
 */
public class Minimax {

    public static int getBestMove(Board currentBoard, Symbol computerSymbol, Symbol humanSymbol) {
        List<Integer> availableMovesL1 = currentBoard.getAvailableMovements();
        if (availableMovesL1.isEmpty()) {
            return -1;
        }

        Tree<Board> tree = Tree.buildDecisionTree(currentBoard, computerSymbol, humanSymbol);
        TreeNode<Board> root = tree.getRoot();

        for (TreeNode<Board> nodeL1 : root.getChildren()) {
            if (nodeL1.isLeaf()) {
                int util = nodeL1.getData().calculateUtility(computerSymbol, humanSymbol);
                nodeL1.setUtility(util);
            } else {
                int minUtility = Integer.MAX_VALUE;
                for (TreeNode<Board> nodeL2 : nodeL1.getChildren()) {
                    int utilL2 = nodeL2.getData().calculateUtility(computerSymbol, humanSymbol);
                    nodeL2.setUtility(utilL2);

                    if (utilL2 < minUtility) {
                        minUtility = utilL2;
                    }
                }
                nodeL1.setUtility(minUtility);
            }
        }

        TreeNode<Board> bestNodeL1 = null;
        int maxUtility = Integer.MIN_VALUE;

        for (TreeNode<Board> nodeL1 : root.getChildren()) {
            if (nodeL1.getUtility() > maxUtility) {
                maxUtility = nodeL1.getUtility();
                bestNodeL1 = nodeL1;
            }
        }

        if (bestNodeL1 != null) {
            return bestNodeL1.getMovement();
        }

        return availableMovesL1.get(0);
    }

    /**
     * Calcula y sugiere el movimiento óptimo (con mayor probabilidad de ganar) para el Jugador Humano.
     */
    public static int getBestMoveForHuman(Board currentBoard, Symbol humanSymbol, Symbol computerSymbol) {
        List<Integer> availableMoves = currentBoard.getAvailableMovements();
        if (availableMoves.isEmpty()) {
            return -1;
        }

        int bestMove = -1;
        int maxUtility = Integer.MIN_VALUE;

        for (int move : availableMoves) {
            Board tempBoard = new Board(currentBoard);
            tempBoard.setSymbol(humanSymbol, move);

            int util = tempBoard.calculateUtility(humanSymbol, computerSymbol);
            if (util > maxUtility) {
                maxUtility = util;
                bestMove = move;
            }
        }

        return (bestMove != -1) ? bestMove : availableMoves.get(0);
    }
}
