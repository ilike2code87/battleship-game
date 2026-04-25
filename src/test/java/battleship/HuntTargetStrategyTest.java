package battleship;

import battleship.board.AttackResult;
import battleship.board.Board;
import battleship.board.Coordinate;
import battleship.strategy.HuntTargetStrategy;
import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

public class HuntTargetStrategyTest {

    @Test
    void hunt_neverRepeatsACoordinate() {
        HuntTargetStrategy strategy = new HuntTargetStrategy(5);
        Board board = new Board(5);
        Set<Coordinate> seen = new HashSet<>();

        for (int i = 0; i < 25; i++) {
            Coordinate coord = strategy.chooseAttack(board);
            assertTrue(seen.add(coord), "Coordinate repeated: " + coord);
            strategy.recordResult(coord, AttackResult.MISS);
        }
    }

    @Test
    void afterHit_targetsAdjacentCell() {
        HuntTargetStrategy strategy = new HuntTargetStrategy(10);
        Board board = new Board(10);

        strategy.recordResult(new Coordinate(5, 5), AttackResult.HIT);
        Coordinate next = strategy.chooseAttack(board);

        int dr = Math.abs(next.getRow() - 5);
        int dc = Math.abs(next.getCol() - 5);
        assertTrue((dr == 1 && dc == 0) || (dr == 0 && dc == 1));
    }

    @Test
    void afterSunk_returnsToHunt() {
        HuntTargetStrategy strategy = new HuntTargetStrategy(10);
        Board board = new Board(10);
        Set<Coordinate> seen = new HashSet<>();

        strategy.recordResult(new Coordinate(5, 5), AttackResult.HIT);
        strategy.recordResult(new Coordinate(5, 5), AttackResult.SUNK);

        for (int i = 0; i < 10; i++) {
            Coordinate c = strategy.chooseAttack(board);
            assertTrue(seen.add(c), "Repeated: " + c);
            strategy.recordResult(c, AttackResult.MISS);
        }
    }
}