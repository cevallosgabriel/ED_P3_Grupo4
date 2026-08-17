package controller;

import com.espol.proyectoestruturadatos.model.board.Symbol;

/**
 * @author Cevallos Guzman Gabriel Abraham.
 * @author Cruz Macias Helen Romina.
 * @author Pincay Salazar Dylan Jeanpier.
 */

public class ChooseController {

    private Symbol humanSymbol;
    private Symbol botSymbol;
    private boolean humanStarts;

    public ChooseController() {
        setPreferences(true, true);
    }

    public void setPreferences(boolean isHumanX, boolean humanStarts) {
        if (isHumanX) {
            this.humanSymbol = Symbol.X;
            this.botSymbol = Symbol.O;
        } else {
            this.humanSymbol = Symbol.O;
            this.botSymbol = Symbol.X;
        }
        this.humanStarts = humanStarts;
    }

    public Symbol getHumanSymbol() {
        return humanSymbol;
    }

    public Symbol getBotSymbol() {
        return botSymbol;
    }

    public boolean isHumanStarts() {
        return humanStarts;
    }
}
