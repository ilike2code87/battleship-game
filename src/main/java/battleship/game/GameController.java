package battleship.game;

import battleship.board.Board;
import battleship.command.Command;
import battleship.player.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameController {

    private final Player player1;
    private final Player player2;
    private final Board  board1;
    private final Board  board2;

    private Player  currentPlayer;
    private boolean gameStarted = false;
    private boolean gameOver    = false;
    private Player  winner;

    private final List<Command> history = new ArrayList<>();

    public GameController(Player player1, Board board1,
                          Player player2, Board board2) {
        this.player1       = player1;
        this.board1        = board1;
        this.player2       = player2;
        this.board2        = board2;
        this.currentPlayer = player1;
    }

    public void startGame() {
        if (gameStarted)
            throw new IllegalStateException("Game already started");
        gameStarted = true;
        currentPlayer = player1;
    }

    public int execute(Command command) {
        if (!command.isValid(this))
            throw new IllegalArgumentException("Invalid command: " + command.describe());
        int result = command.execute(this);
        history.add(command);
        return result;
    }

    public void advanceTurn() {
        currentPlayer = (currentPlayer == player1) ? player2 : player1;
    }

    public void endGame(Player winner) {
        this.winner   = winner;
        this.gameOver = true;
    }

    public Board getOpponentBoard() {
        return (currentPlayer == player1) ? board2 : board1;
    }

    public Board getBoard(Player player) {
        if (player == player1) return board1;
        if (player == player2) return board2;
        throw new IllegalArgumentException("Unknown player");
    }

    public Player  getCurrentPlayer() { return currentPlayer; }
    public boolean isGameStarted()    { return gameStarted; }
    public boolean isGameOver()       { return gameOver; }
    public Player  getWinner()        { return winner; }

    public List<Command> getHistory() {
        return Collections.unmodifiableList(history);
    }
}