package battleship.command;

import battleship.board.AttackResult;
import battleship.board.Board;
import battleship.board.Coordinate;
import battleship.game.GameController;
import battleship.player.Player;

public class AttackCommand implements Command {

    private final Player attacker;
    private final Coordinate target;
    private int result = -1;

    public AttackCommand(Player attacker, Coordinate target) {
        this.attacker = attacker;
        this.target   = target;
    }

    @Override
    public boolean isValid(GameController context) {
        if (!context.isGameStarted())                      return false;
        if (context.isGameOver())                          return false;
        if (!context.getCurrentPlayer().equals(attacker)) return false;
        return !context.getOpponentBoard().isAttacked(target);
    }

    @Override
    public int execute(GameController context) {
        Board opponentBoard = context.getOpponentBoard();

        result = opponentBoard.attack(target);

        attacker.recordResult(target, result);

        if (opponentBoard.allShipsSunk()) {
            context.endGame(attacker);
        } else {
            context.advanceTurn();
        }

        return result;
    }

    @Override
    public String describe() {
        return String.format("%s attacks (%d,%d) -> %d",
                attacker.getName(), target.getRow(), target.getCol(), result);
    }

    public Player getAttacker()   { return attacker; }
    public Coordinate getTarget() { return target; }
    public int getResult()        { return result; }
}