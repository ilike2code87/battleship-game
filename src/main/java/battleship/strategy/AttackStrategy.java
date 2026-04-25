package battleship.strategy;

import battleship.board.Board;
import battleship.board.Coordinate;

public interface AttackStrategy {
    Coordinate chooseAttack(Board opponentBoard);
    default void recordResult(Coordinate coord, int result) {}
}