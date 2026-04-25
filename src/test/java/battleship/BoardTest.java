package battleship;

import battleship.board.AttackResult;
import battleship.board.Board;
import battleship.board.Coordinate;
import battleship.ship.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    @Test
    void placeShip_validPlacement() {
        Board board = new Board(5);
        board.placeShip(new Destroyer(), new Coordinate(0, 0), true);
    }

    @Test
    void placeShip_overlap_throwsException() {
        Board board = new Board(5);
        board.placeShip(new PatrolBoat(), new Coordinate(0, 0), true);
        assertThrows(IllegalArgumentException.class,
                () -> board.placeShip(new PatrolBoat(), new Coordinate(0, 0), true));
    }

    @Test
    void attack_hit_returnsHIT() {
        Board board = new Board(5);
        board.placeShip(new Battleship(), new Coordinate(1, 1), true);
        assertEquals(AttackResult.HIT, board.attack(new Coordinate(1, 1)));
    }

    @Test
    void attack_miss_returnsMISS() {
        Board board = new Board(5);
        assertEquals(AttackResult.MISS, board.attack(new Coordinate(2, 2)));
    }

    @Test
    void attack_alreadyAttacked() {
        Board board = new Board(5);
        board.attack(new Coordinate(0, 0));
        assertEquals(AttackResult.ALREADY_ATTACKED, board.attack(new Coordinate(0, 0)));
    }

    @Test
    void attack_sinksShip_returnsSUNK() {
        Board board = new Board(5);
        board.placeShip(new PatrolBoat(), new Coordinate(0, 0), true);
        board.attack(new Coordinate(0, 0));
        assertEquals(AttackResult.SUNK, board.attack(new Coordinate(0, 1)));
    }

    @Test
    void submarine_stealth_reportsMISS() {
        Board board = new Board(5);
        board.placeShip(new Submarine(), new Coordinate(0, 0), true);
        assertEquals(AttackResult.MISS, board.attack(new Coordinate(0, 0)));
    }

    @Test
    void allShipsSunk_trueWhenAllSunk() {
        Board board = new Board(5);
        board.placeShip(new PatrolBoat(), new Coordinate(0, 0), true);
        assertFalse(board.allShipsSunk());
        board.attack(new Coordinate(0, 0));
        board.attack(new Coordinate(0, 1));
        assertTrue(board.allShipsSunk());
    }
}