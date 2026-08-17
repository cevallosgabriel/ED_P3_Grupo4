package com.espol.proyectoestruturadatos;

import com.espol.proyectoestruturadatos.model.board.Board;
import com.espol.proyectoestruturadatos.model.board.Box;
import com.espol.proyectoestruturadatos.model.board.Symbol;
import controller.BoardController;
import controller.MainController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

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

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Tres en Raya vs Computador");
        mainController = new MainController();

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));

        VBox topBox = new VBox(12);
        topBox.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("TRES EN RAYA");
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));

        Label nameLabel = new Label("Jugador:");
        nameLabel.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 13));
        txtPlayerName = new TextField("JaHeGa");
        txtPlayerName.setPrefWidth(160);
        txtPlayerName.setPromptText("Ingrese su nombre");
        txtPlayerName.setFont(Font.font("Segoe UI", 13));

        HBox nameBox = new HBox(8, nameLabel, txtPlayerName);
        nameBox.setAlignment(Pos.CENTER);

        Label symbolLabel = new Label("Tu Ficha:");
        symbolLabel.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 13));

        ToggleGroup symbolGroup = new ToggleGroup();
        rbHumanX = new RadioButton("X");
        rbHumanX.setToggleGroup(symbolGroup);
        rbHumanX.setSelected(true);
        rbHumanX.setFont(Font.font("Segoe UI", 12));

        rbHumanO = new RadioButton("O");
        rbHumanO.setToggleGroup(symbolGroup);
        rbHumanO.setFont(Font.font("Segoe UI", 12));

        HBox symbolBox = new HBox(8, symbolLabel, rbHumanX, rbHumanO);
        symbolBox.setAlignment(Pos.CENTER);

        Label turnLabel = new Label("Inicia:");
        turnLabel.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 13));

        ToggleGroup turnGroup = new ToggleGroup();
        rbStartHuman = new RadioButton("Humano");
        rbStartHuman.setToggleGroup(turnGroup);
        rbStartHuman.setSelected(true);
        rbStartHuman.setFont(Font.font("Segoe UI", 12));

        rbStartBot = new RadioButton("Computadora");
        rbStartBot.setToggleGroup(turnGroup);
        rbStartBot.setFont(Font.font("Segoe UI", 12));

        HBox turnBox = new HBox(8, turnLabel, rbStartHuman, rbStartBot);
        turnBox.setAlignment(Pos.CENTER);

        Button btnNewGame = new Button("Nueva Partida");
        btnNewGame.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        btnNewGame.setOnAction(e -> restartGame());

        Button btnSave = new Button("Guardar Partida");
        btnSave.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        btnSave.setOnAction(e -> manualSaveGame());

        Button btnLoad = new Button("Reanudar Partida");
        btnLoad.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        btnLoad.setOnAction(e -> loadSavedGame());

        HBox actionBox = new HBox(10, btnNewGame, btnSave, btnLoad);
        actionBox.setAlignment(Pos.CENTER);

        topBox.getChildren().addAll(titleLabel, nameBox, symbolBox, turnBox, actionBox);
        root.setTop(topBox);

        VBox centerBox = new VBox(15);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPadding(new Insets(10, 0, 10, 0));

        statusLabel = new Label("Easter egg:Ap desde matricula");
        statusLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        statusLabel.setStyle("-fx-text-fill: #333333;");

        boardGrid = new GridPane();
        boardGrid.setAlignment(Pos.CENTER);
        boardGrid.setHgap(8);
        boardGrid.setVgap(8);

        cellButtons = new Button[9];
        for (int i = 0; i < 9; i++) {
            final int index = i;
            Button btn = new Button("");
            btn.setPrefSize(105, 105);
            btn.setFont(Font.font("Segoe UI", FontWeight.BOLD, 36));
            btn.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #B0BEC5; -fx-border-width: 2; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand;");
            btn.setOnAction(e -> handleCellClick(index));
            cellButtons[i] = btn;

            int row = i / 3;
            int col = i % 3;
            boardGrid.add(btn, col, row);
        }

        centerBox.getChildren().addAll(statusLabel, boardGrid);
        root.setCenter(centerBox);

        Scene scene = new Scene(root, 520, 680);
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
        updateBoardUI();

        if (!humanStarts) {
            updateBoardUI();
            if (!checkGameOver()) {
                statusLabel.setText("La Computadora inició la partida. Turno de " + getPlayerName() + " (" + mainController.getChooseController().getHumanSymbol() + ")");
            }
        } else {
            statusLabel.setText("Turno de " + getPlayerName() + " (" + mainController.getChooseController().getHumanSymbol() + ")");
        }
    }

    private void restartGame() {
        startFirstGame();
    }

    private void handleCellClick(int index) {
        if (mainController.getBoardController() == null || mainController.getBoardController().isGameOver()) {
            return;
        }

        boolean moved = mainController.getBoardController().makeHumanMove(index);
        if (moved) {
            updateBoardUI();
            autoSaveGame();
            if (!checkGameOver()) {
                statusLabel.setText("Turno de " + getPlayerName() + " (" + mainController.getChooseController().getHumanSymbol() + ")");
            }
        }
    }

    private boolean checkGameOver() {
        if (mainController.getBoardController() != null && mainController.getBoardController().isGameOver()) {
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
        for (int i = 0; i < 9; i++) {
            Box box = board.boxes[i];
            Button btn = cellButtons[i];

            if (box == null || box.isEmpty()) {
                btn.setText("");
                btn.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #B0BEC5; -fx-border-width: 2; -fx-border-radius: 8; -fx-background-radius: 8; ");
            } else {
                Symbol s = box.getSymbol();
                btn.setText(s.toString());
                if (s.equals(Symbol.X)) {
                    btn.setStyle("-fx-background-color: #E3F2FD; -fx-text-fill: #1E88E5; -fx-border-color: #90CAF9; -fx-border-width: 2; -fx-border-radius: 8; -fx-background-radius: 8;");
                } else {
                    btn.setStyle("-fx-background-color: #FFEBEE; -fx-text-fill: #E53935; -fx-border-color: #EF9A9A; -fx-border-width: 2; -fx-border-radius: 8; -fx-background-radius: 8;");
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
            alert.setContentText("¡Partida de " + getPlayerName() + " guardada exitosamente!\nUbicación: " + file.getAbsolutePath());
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
                alert.setHeaderText("F no se pudimos guardar la partida");
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
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Sin Partida Guardada");
            alert.setHeaderText(null);
            alert.setContentText("No se encontró ningún archivo de partida guardada.");
            alert.showAndWait();
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            GameSaveData data = (GameSaveData) ois.readObject();

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
            updateBoardUI();

            if (data.hasEnded) {
                statusLabel.setText("Partida Reanudada (Finalizada)");
            } else if (data.isHumanTurn) {
                statusLabel.setText("Partida Reanudada. Turno de " + getPlayerName() 
                        + " (" + humanSymbol + ")");
            } else {
                restoredBc.executeBotMove();
                updateBoardUI();
                if (!checkGameOver()) {
                    statusLabel.setText("Partida Reanudada. Turno de " + getPlayerName() + " (" + humanSymbol + ")");
                }
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Partida Reanudada Exitosamente");
            alert.setHeaderText(null);
            alert.setContentText("¡Partida de " + data.playerName + " cargada correctamente!\nFichas restauradas en tablero: " + countPieces);
            alert.showAndWait();

        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error al Cargar");
            alert.setHeaderText("No se pudo reanudar la partida");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
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
    }
}
