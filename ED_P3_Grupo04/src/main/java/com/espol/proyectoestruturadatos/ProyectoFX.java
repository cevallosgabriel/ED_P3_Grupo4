package com.espol.proyectoestruturadatos;

import com.espol.proyectoestruturadatos.dstructure.Minimax;
import com.espol.proyectoestruturadatos.model.board.Board;
import com.espol.proyectoestruturadatos.model.board.Box;
import com.espol.proyectoestruturadatos.model.board.Symbol;
import controller.BoardController;
import controller.MainController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Cevallos Guzman Gabriel Abraham.
 * @author Cruz Macias Helen Romina.
 * @author Pincay Salazar Dylan Jeanpier.
 */

public class ProyectoFX extends Application {

    private static final String guardado = "guardado.ser";
    private static final String autoGuardado = "autoGuardado.ser";

    private MainController mainController;
    private Button[] cellButtons;
    private Label statusLabel;
    private GridPane boardGrid;

    private TextField txtPlayerName;
    private RadioButton rbHumanX;
    private RadioButton rbHumanO;
    private RadioButton rbStartHuman;
    private RadioButton rbStartBot;

    private CheckBox chkRecorrido;
    private CheckBox chkSugerencias;
    private CheckBox chkResaltado;

    private boolean isBotThinking = false;
    private int suggestedCellIndex = -1;

    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Tres en Raya");
        mainController = new MainController();

        BorderPane root = new BorderPane();

        VBox headerBanner = new VBox();
        headerBanner.getStyleClass().add("header-banner");
        Label titleLabel = new Label("TRES EN RAYA");
        titleLabel.getStyleClass().add("title-text");
        headerBanner.getChildren().add(titleLabel);
        root.setTop(headerBanner);

        VBox contentBox = new VBox(14);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(16));

        VBox configCard = new VBox(10);
        configCard.setAlignment(Pos.CENTER);
        configCard.getStyleClass().add("card-container");

        Label nameLabel = new Label("Jugador:");
        nameLabel.getStyleClass().add("label-bold");
        txtPlayerName = new TextField("JaHeGa");
        txtPlayerName.setPrefWidth(150);
        txtPlayerName.setPromptText("Ingrese su nombre");
        txtPlayerName.getStyleClass().add("text-field-custom");

        HBox nameBox = new HBox(8, nameLabel, txtPlayerName);
        nameBox.setAlignment(Pos.CENTER);

        Label symbolLabel = new Label("Tu Ficha:");
        symbolLabel.getStyleClass().add("label-bold");

        ToggleGroup symbolGroup = new ToggleGroup();
        rbHumanX = new RadioButton("X");
        rbHumanX.setToggleGroup(symbolGroup);
        rbHumanX.setSelected(true);

        rbHumanO = new RadioButton("O");
        rbHumanO.setToggleGroup(symbolGroup);

        HBox symbolBox = new HBox(8, symbolLabel, rbHumanX, rbHumanO);
        symbolBox.setAlignment(Pos.CENTER);

        Label turnLabel = new Label("Inicia:");
        turnLabel.getStyleClass().add("label-bold");

        ToggleGroup turnGroup = new ToggleGroup();
        rbStartHuman = new RadioButton("Humano");
        rbStartHuman.setToggleGroup(turnGroup);
        rbStartHuman.setSelected(true);

        rbStartBot = new RadioButton("Computadora");
        rbStartBot.setToggleGroup(turnGroup);

        HBox turnBox = new HBox(8, turnLabel, rbStartHuman, rbStartBot);
        turnBox.setAlignment(Pos.CENTER);

        configCard.getChildren().addAll(nameBox, symbolBox, turnBox);

        VBox actionCard = new VBox();
        actionCard.setAlignment(Pos.CENTER);
        actionCard.getStyleClass().add("card-container");

        Button btnNewGame = new Button("NUEVA");
        btnNewGame.getStyleClass().add("button-secondary");
        btnNewGame.setOnAction(e -> restartGame());

        Button btnUndo = new Button("DESHACER");
        btnUndo.getStyleClass().add("button-secondary");
        btnUndo.setOnAction(e -> undoLastMovements());

        Button btnSave = new Button("GUARDAR");
        btnSave.getStyleClass().add("button-primary");
        btnSave.setOnAction(e -> manualSaveGame());

        Button btnLoad = new Button("REANUDAR");
        btnLoad.getStyleClass().add("button-secondary");
        btnLoad.setOnAction(e -> loadSavedGame());

        HBox actionBox = new HBox(8, btnNewGame, btnUndo, btnSave, btnLoad);
        actionBox.setAlignment(Pos.CENTER);
        actionCard.getChildren().add(actionBox);

        chkRecorrido = new CheckBox("Recorrido");
        chkRecorrido.getStyleClass().add("check-box");
        chkRecorrido.setSelected(true);

        chkSugerencias = new CheckBox("Sugerencias");
        chkSugerencias.getStyleClass().add("check-box");
        chkSugerencias.setSelected(true);
        chkSugerencias.setOnAction(e -> updateBoardUI());

        chkResaltado = new CheckBox("Resaltado");
        chkResaltado.getStyleClass().add("check-box");
        chkResaltado.setSelected(true);
        chkResaltado.setOnAction(e -> updateBoardUI());

        HBox optionsBox = new HBox(16, chkRecorrido, chkSugerencias, chkResaltado);
        optionsBox.setAlignment(Pos.CENTER);
        optionsBox.getStyleClass().add("options-box");

        statusLabel = new Label("Selecciona tus opciones e inicia el juego.");
        statusLabel.getStyleClass().add("status-badge");

        boardGrid = new GridPane();
        boardGrid.setAlignment(Pos.CENTER);
        boardGrid.setHgap(10);
        boardGrid.setVgap(10);

        cellButtons = new Button[9];
        for (int i = 0; i < 9; i++) {
            final int index = i;
            Button btn = new Button("");
            btn.setPrefSize(100, 100);
            btn.getStyleClass().add("cell-button-empty");
            btn.setOnAction(e -> handleCellClick(index));
            cellButtons[i] = btn;

            int row = i / 3;
            int col = i % 3;
            boardGrid.add(btn, col, row);
        }

        contentBox.getChildren().addAll(configCard, actionCard, optionsBox, statusLabel, boardGrid);
        root.setCenter(contentBox);

        Scene scene = new Scene(root, 560, 720);
        try {
            scene.getStylesheets().add(getClass().getResource("/com/espol/proyectoestruturadatos/css/styles.css").toExternalForm());
        } catch (Exception ex) {
        }

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        startFirstGame();
    }

    private String getPlayerName() {
        String name = txtPlayerName != null ? txtPlayerName.getText().trim() : "";
        return name.isEmpty() ? "Humano" : name;
    }

    private void startFirstGame() {
        boolean isHumanX = rbHumanX.isSelected();
        boolean humanStarts = rbStartHuman.isSelected();

        mainController.startNewGame(isHumanX, humanStarts);
        suggestedCellIndex = -1;
        updateBoardUI();

        if (!humanStarts) {
            triggerBotTurn();
        } else {
            statusLabel.setText("Turno de " + getPlayerName() + " (" + mainController.getChooseController().getHumanSymbol() + ")");
            calculateHumanSuggestion();
        }
    }

    private void restartGame() {
        if (isBotThinking) return;
        startFirstGame();
    }

    private void undoLastMovements() {
        if (isBotThinking || mainController.getBoardController() == null) {
            return;
        }

        if (!mainController.getBoardController().canUndo()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Deshacer Movimiento");
            alert.setHeaderText(null);
            alert.setContentText("No hay suficientes movimientos para deshacer.");
            alert.showAndWait();
            return;
        }

        boolean undone = mainController.undoLastTwoMoves();
        if (undone) {
            suggestedCellIndex = -1;
            updateBoardUI();
            autoSaveGame();
            statusLabel.setText("Movimientos deshechos. Turno de " + getPlayerName() + " (" + mainController.getChooseController().getHumanSymbol() + ")");
            calculateHumanSuggestion();
        }
    }

    private void handleCellClick(int index) {
        if (isBotThinking || mainController.getBoardController() == null || mainController.getBoardController().isGameOver()) {
            return;
        }

        if (!mainController.getBoardController().isHumanTurn()) {
            return;
        }

        Board board = mainController.getBoardController().getBoard();
        if (board.boxes[index].isEmpty()) {
            boolean moved = mainController.getBoardController().makeHumanMove(index);
            if (moved) {
                suggestedCellIndex = -1;
                updateBoardUI();
                autoSaveGame();
                if (!checkGameOver()) {
                    triggerBotTurn();
                }
            }
        }
    }

    private void triggerBotTurn() {
        if (chkRecorrido.isSelected()) {
            executeVisualBotMove();
        } else {
            mainController.getBoardController().executeBotMove();
            updateBoardUI();
            autoSaveGame();
            if (!checkGameOver()) {
                statusLabel.setText("Turno de " + getPlayerName() + " (" + mainController.getChooseController().getHumanSymbol() + ")");
                calculateHumanSuggestion();
            }
        }
    }

    private void calculateHumanSuggestion() {
        if (chkSugerencias.isSelected() && mainController.getBoardController() != null && !mainController.getBoardController().isGameOver()) {
            Board board = mainController.getBoardController().getBoard();
            Symbol humanSymbol = mainController.getChooseController().getHumanSymbol();
            Symbol botSymbol = mainController.getChooseController().getBotSymbol();

            int bestMove = Minimax.getBestMoveForHuman(board, humanSymbol, botSymbol);
            if (bestMove != -1) {
                suggestedCellIndex = bestMove;
                updateBoardUI();
                int row = (bestMove / 3) + 1;
                int col = (bestMove % 3) + 1;
                statusLabel.setText("Sugerencia: Fila " + row + ", Columna " + col);
            }
        }
    }

    private void executeVisualBotMove() {
        if (mainController.getBoardController() == null || mainController.getBoardController().isGameOver()) {
            return;
        }

        Board board = mainController.getBoardController().getBoard();
        List<Integer> availableMoves = board.getAvailableMovements();
        if (availableMoves.isEmpty()) {
            return;
        }

        isBotThinking = true;
        setButtonsEnabled(false);
        statusLabel.setText("Evaluando opciones con Minimax...");

        Symbol botSymbol = mainController.getChooseController().getBotSymbol();
        Symbol humanSymbol = mainController.getChooseController().getHumanSymbol();
        int bestMove = Minimax.getBestMove(board, botSymbol, humanSymbol);

        Timeline timeline = new Timeline();
        int stepDelayMs = 350;
        int timeOffsetMs = 0;

        for (int cellIndex : availableMoves) {
            final int targetCell = cellIndex;
            timeOffsetMs += stepDelayMs;

            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(timeOffsetMs), e -> {
                cellButtons[targetCell].getStyleClass().setAll("cell-button-evaluating");
                statusLabel.setText("Evaluando casilla " + targetCell + "...");
            }));

            if (targetCell != bestMove) {
                timeOffsetMs += stepDelayMs;
                timeline.getKeyFrames().add(new KeyFrame(Duration.millis(timeOffsetMs), e -> {
                    cellButtons[targetCell].getStyleClass().setAll("cell-button-rejected");
                    statusLabel.setText("Camino descartado en casilla " + targetCell + ". Regresando...");
                }));
            }
        }

        timeOffsetMs += stepDelayMs;
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(timeOffsetMs), e -> {
            cellButtons[bestMove].getStyleClass().setAll("cell-button-selected");
            statusLabel.setText("Camino óptimo seleccionado en casilla " + bestMove);
        }));

        timeOffsetMs += 500;
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(timeOffsetMs), e -> {
            mainController.getBoardController().executeBotMove();
            updateBoardUI();
            autoSaveGame();
            isBotThinking = false;
            setButtonsEnabled(true);

            if (!checkGameOver()) {
                statusLabel.setText("Turno de " + getPlayerName() + " (" + humanSymbol + ")");
                calculateHumanSuggestion();
            }
        }));

        timeline.play();
    }

    private void setButtonsEnabled(boolean enabled) {
        for (Button btn : cellButtons) {
            if (btn != null) {
                btn.setDisable(!enabled);
            }
        }
    }

    private boolean checkGameOver() {
        if (mainController.getBoardController() != null && mainController.getBoardController().isGameOver()) {
            suggestedCellIndex = -1;
            updateBoardUI();
            autoSaveGame();

            String title = mainController.getFinalResultTitle();
            String message = mainController.getFinalResultMessage();

            statusLabel.setText("Juego Finalizado: " + title);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Fin del Juego");
            alert.setHeaderText(title);
            alert.setContentText("Jugador: " + getPlayerName() + "\n\n" + message);
            alert.showAndWait();
            return true;
        }
        return false;
    }

    private void updateBoardUI() {
        if (mainController.getBoardController() == null) return;

        Board board = mainController.getBoardController().getBoard();
        Symbol winnerSymbol = board.getWinner();
        int[] winningLine = (chkResaltado.isSelected()) ? board.getWinningLineIndices(winnerSymbol) : null;

        for (int i = 0; i < 9; i++) {
            Box box = board.boxes[i];
            Button btn = cellButtons[i];

            boolean isWinningCell = false;
            if (winningLine != null) {
                for (int wIdx : winningLine) {
                    if (wIdx == i) {
                        isWinningCell = true;
                        break;
                    }
                }
            }

            btn.getStyleClass().clear();

            if (isWinningCell) {
                btn.getStyleClass().add("cell-button-winning");
                btn.setText(box.getSymbol().toString());
            } else if (box == null || box.isEmpty()) {
                if (i == suggestedCellIndex && chkSugerencias.isSelected() && mainController.getBoardController().isHumanTurn()) {
                    btn.getStyleClass().add("cell-button-hint");
                    btn.setText("");
                } else {
                    btn.getStyleClass().add("cell-button-empty");
                    btn.setText("");
                }
            } else {
                Symbol s = box.getSymbol();
                btn.setText(s.toString());
                if (s.equals(Symbol.X)) {
                    btn.getStyleClass().add("cell-button-x");
                } else {
                    btn.getStyleClass().add("cell-button-o");
                }
            }
        }
        if (boardGrid != null) {
            boardGrid.requestLayout();
        }
    }

    private void autoSaveGame() {
        saveGameToFile(autoGuardado, false);
    }

    private void manualSaveGame() {
        boolean success = saveGameToFile(guardado, true);
        if (success) {
            File file = new File(guardado);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Partida Guardada");
            alert.setHeaderText(null);
            alert.setContentText("Partida de " + getPlayerName() + " guardada exitosamente.\nUbicación: " + file.getAbsolutePath());
            alert.showAndWait();
        }
    }

    private boolean saveGameToFile(String fileName, boolean showErrors) {
        if (mainController == null || mainController.getBoardController() == null) return false;

        try {
            GameSaveData data = new GameSaveData();
            data.playerName = getPlayerName();
            data.isHumanX = rbHumanX.isSelected();
            data.humanStarts = rbStartHuman.isSelected();
            data.isHumanTurn = mainController.getBoardController().isHumanTurn();
            data.hasEnded = mainController.getBoardController().getBoard().hasEnded;
            data.moveHistory = new ArrayList<>(mainController.getBoardController().getMoveHistory());

            Board board = mainController.getBoardController().getBoard();
            for (int i = 0; i < 9; i++) {
                if (board.boxes[i] != null && !board.boxes[i].isEmpty()) {
                    data.boardSymbols[i] = board.boxes[i].getSymbol().toString();
                } else {
                    data.boardSymbols[i] = null;
                }
            }

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
                oos.writeObject(data);
            }
            return true;
        } catch (Exception ex) {
            if (showErrors) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error al Guardar");
                alert.setHeaderText("No se pudo guardar la partida");
                alert.setContentText(ex.getMessage());
                alert.showAndWait();
            }
            return false;
        }
    }

    private void loadSavedGame() {
        File file = new File(guardado);
        if (!file.exists()) {
            file = new File(autoGuardado);
        }

        if (!file.exists()) {
            restartGame();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Reanudar Partida");
            alert.setHeaderText(null);
            alert.setContentText("No hay partida guardada previa. Se inició una nueva partida vacía.");
            alert.showAndWait();
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            GameSaveData data = (GameSaveData) ois.readObject();

            if (data == null) {
                restartGame();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Reanudar Partida");
                alert.setHeaderText(null);
                alert.setContentText("No hay partida guardada previa. Se inició una nueva partida vacía.");
                alert.showAndWait();
                return;
            }

            txtPlayerName.setText(data.playerName);
            rbHumanX.setSelected(data.isHumanX);
            rbHumanO.setSelected(!data.isHumanX);
            rbStartHuman.setSelected(data.humanStarts);
            rbStartBot.setSelected(!data.humanStarts);

            mainController.getChooseController().setPreferences(data.isHumanX, data.humanStarts);
            Symbol humanSymbol = mainController.getChooseController().getHumanSymbol();
            Symbol botSymbol = mainController.getChooseController().getBotSymbol();

            BoardController restoredBc = new BoardController(humanSymbol, botSymbol, true);
            restoredBc.setHumanTurn(data.isHumanTurn);
            if (data.moveHistory != null) {
                restoredBc.setMoveHistory(data.moveHistory);
            }

            Board board = restoredBc.getBoard();
            board.hasEnded = data.hasEnded;

            int countPieces = 0;
            for (int i = 0; i < 9; i++) {
                String sym = data.boardSymbols[i];
                if ("X".equals(sym)) {
                    board.boxes[i].setSymbol(Symbol.X);
                    countPieces++;
                } else if ("O".equals(sym)) {
                    board.boxes[i].setSymbol(Symbol.O);
                    countPieces++;
                } else {
                    board.boxes[i].setSymbol(null);
                }
            }

            if (board.isWinner(humanSymbol)) {
                board.setWinner(humanSymbol);
            } else if (board.isWinner(botSymbol)) {
                board.setWinner(botSymbol);
            }

            mainController.setBoardController(restoredBc);
            suggestedCellIndex = -1;
            updateBoardUI();

            if (data.hasEnded) {
                statusLabel.setText("Partida Reanudada (Finalizada)");
            } else if (data.isHumanTurn) {
                statusLabel.setText("Partida Reanudada. Turno de " + getPlayerName() + " (" + humanSymbol + ")");
                calculateHumanSuggestion();
            } else {
                triggerBotTurn();
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Partida Reanudada Exitosamente");
            alert.setHeaderText(null);
            alert.setContentText("Partida de " + data.playerName + " cargada correctamente.\nFichas restauradas en tablero: " + countPieces);
            alert.showAndWait();

        } catch (Exception ex) {
            restartGame();
            Alert alertError = new Alert(Alert.AlertType.INFORMATION);
            alertError.setTitle("Reanudar Partida");
            alertError.setHeaderText(null);
            alertError.setContentText("No hay partida guardada previa. Se inició una nueva partida vacía.");
            alertError.showAndWait();
        }
    }

    public static class GameSaveData implements Serializable {
        private static final long serialVersionUID = 1L;
        public String playerName;
        public boolean isHumanX;
        public boolean humanStarts;
        public boolean isHumanTurn;
        public boolean hasEnded;
        public String[] boardSymbols = new String[9];
        public List<Integer> moveHistory = new ArrayList<>();
    }
}
