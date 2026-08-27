package controller;

import com.espol.proyectoestruturadatos.model.board.Symbol;

/**
 * @author Cevallos Guzman Gabriel Abraham.
 * @author Cruz Macias Helen Romina.
 * @author Pincay Salazar Dylan Jeanpier.
 */
public class MainController {

    private ChooseController chooseController;
    private BoardController boardController;

    public MainController() {
        this.chooseController = new ChooseController();
    }

    public void startNewGame(boolean isHumanX, boolean humanStarts) {
        chooseController.setPreferences(isHumanX, humanStarts);
        boardController = new BoardController(
            chooseController.getHumanSymbol(),
            chooseController.getBotSymbol(),
            chooseController.isHumanStarts()
        );
    }

    public ChooseController getChooseController() {
        return chooseController;
    }

    public BoardController getBoardController() {
        return boardController;
    }

    public void setBoardController(BoardController boardController) {
        this.boardController = boardController;
    }

    public String getFinalResultMessage() {
        if (boardController == null) return "";
        Symbol winner = boardController.getWinner();
        Symbol humanSymbol = chooseController.getHumanSymbol();
        if (winner == null) {
            return "Ha sido un Empate.";
        } else if (winner.equals(humanSymbol)) {
            return "Felicidades, has Ganado!";
        } else {
            return "Ha ganado la Computadora. Suerte a la proxima!";
        }
    }

    public boolean undoLastTwoMoves() {
        if (boardController == null) return false;
        return boardController.undoLastTwoMoves();
    }

    public String getFinalResultTitle() {
        if (boardController == null) return "";
        Symbol winner = boardController.getWinner();
        Symbol humanSymbol = chooseController.getHumanSymbol();
        if (winner == null) {
            return "Empate";
        } else if (winner.equals(humanSymbol)) {
            return "Victoria";
        } else {
            return "Derrota";
        }
    }
}
