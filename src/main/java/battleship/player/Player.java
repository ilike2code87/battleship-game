package battleship.player;

import battleship.board.Board;
import battleship.board.Coordinate;

public interface Player {
    Coordinate chooseMove(Board opponentBoard);
    String getName();
}