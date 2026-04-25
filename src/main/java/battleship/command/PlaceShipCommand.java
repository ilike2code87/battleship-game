package battleship.command;

import battleship.board.AttackResult;
import battleship.board.Coordinate;
import battleship.game.GameController;
import battleship.player.Player;
import battleship.ship.Ship;

public class PlaceShipCommand implements Command {

    private final Player     player;
    private final Ship       ship;
    private final Coordinate start;
    private final boolean    horizontal;

    public PlaceShipCommand(Player player, Ship ship,
                            Coordinate start, boolean horizontal) {
        this.player     = player;
        this.ship       = ship;
        this.start      = start;
        this.horizontal = horizontal;
    }

    @Override
    public boolean isValid(GameController context) {
        return !context.isGameStarted() && !context.isGameOver();
    }

    @Override
    public int execute(GameController context) {
        context.getBoard(player).placeShip(ship, start, horizontal);
        return AttackResult.MISS;
    }

    @Override
    public String describe() {
        return String.format("%s places %s at (%d,%d) %s",
                player.getName(), ship.getName(),
                start.getRow(), start.getCol(),
                horizontal ? "horizontal" : "vertical");
    }
}