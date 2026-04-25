package battleship.command;

import battleship.game.GameController;

public interface Command {
    boolean isValid(GameController context);
    int execute(GameController context);
    String describe();
}