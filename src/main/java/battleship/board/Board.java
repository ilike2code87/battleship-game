package battleship.board;

import battleship.ship.Ship;
import java.util.ArrayList;
import java.util.List;

public class Board {

    private final Cell[][] grid;
    private final int size;
    private final List<Ship> ships = new ArrayList<>();

    public Board(int size) {
        this.size = size;
        this.grid = new Cell[size][size];
        for (int r = 0; r < size; r++)
            for (int c = 0; c < size; c++)
                grid[r][c] = new Cell();
    }

    public void placeShip(Ship ship, Coordinate start, boolean horizontal) {
        int row = start.getRow(), col = start.getCol();

        for (int i = 0; i < ship.getSize(); i++) {
            int r = horizontal ? row     : row + i;
            int c = horizontal ? col + i : col;
            if (!inBounds(r, c) || grid[r][c].hasShip())
                throw new IllegalArgumentException(
                        "Invalid placement at (" + r + "," + c + ")");
        }

        for (int i = 0; i < ship.getSize(); i++) {
            int r = horizontal ? row     : row + i;
            int c = horizontal ? col + i : col;
            grid[r][c].placeShip(ship);
        }
        ships.add(ship);
    }

    public int attack(Coordinate coord) {
        if (!inBounds(coord.getRow(), coord.getCol()))
            throw new IllegalArgumentException("Out of bounds: " + coord);
        return grid[coord.getRow()][coord.getCol()].attack();
    }

    public boolean isAttacked(Coordinate coord) {
        return inBounds(coord.getRow(), coord.getCol())
                && grid[coord.getRow()][coord.getCol()].isAttacked();
    }

    public boolean allShipsSunk() {
        return ships.stream().allMatch(Ship::isSunk);
    }

    public int getSize() { return size; }

    private boolean inBounds(int row, int col) {
        return row >= 0 && col >= 0 && row < size && col < size;
    }
}