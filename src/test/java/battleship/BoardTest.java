package battleship;

import battleship.board.Board;
import battleship.board.Coordinate;
import battleship.ship.Ship;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    @Test
    void placeShip_validPlacement() {
        Board board = new Board(5);
        Ship ship = new Ship("Destroyer", 2);

        board.placeShip(ship, new Coordinate(0,0), true);
        assertTrue(true); // no exception = success
    }

    @Test
    void placeShip_overlap_throwsException() {
        Board board = new Board(5);
        Ship ship1 = new Ship("A", 2);
        Ship ship2 = new Ship("B", 2);

        board.placeShip(ship1, new Coordinate(0,0), true);
        assertThrows(IllegalArgumentException.class, () ->
                board.placeShip(ship2, new Coordinate(0,0), true));
    }

    @Test
    void attack_hit_returnsTrue() {
        Board board = new Board(5);
        Ship ship = new Ship("A", 1);

        board.placeShip(ship, new Coordinate(1,1), true);
        assertTrue(board.attack(new Coordinate(1,1)));
    }

    @Test
    void attack_miss_returnsFalse() {
        Board board = new Board(5);
        assertFalse(board.attack(new Coordinate(2,2)));
    }

    @Test
    void ship_sinks_after_hits() {
        Ship ship = new Ship("A", 2);
        ship.registerHit();
        ship.registerHit();

        assertTrue(ship.isSunk());
    }
}