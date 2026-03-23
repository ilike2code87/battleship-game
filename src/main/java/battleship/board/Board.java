package battleship.board;

import battleship.ship.Ship;

public class Board {
    private final Cell[][] grid;
    private final int size;

    public Board(int size) {
        this.size = size;
        this.grid = new Cell[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = new Cell();
            }
        }
    }

    public void placeShip(Ship ship, Coordinate start, boolean horizontal) {
        int row = start.getRow();
        int col = start.getCol();

        for (int i = 0; i < ship.getSize(); i++) {
            int r = horizontal ? row : row + i;
            int c = horizontal ? col + i : col;

            if (!inBounds(r, c) || grid[r][c].hasShip()) {
                throw new IllegalArgumentException("Invalid ship placement");
            }
        }

        for (int i = 0; i < ship.getSize(); i++) {
            int r = horizontal ? row : row + i;
            int c = horizontal ? col + i : col;

            grid[r][c].placeShip(ship);
        }
    }

    public boolean attack(Coordinate coord) {
        if (!inBounds(coord.getRow(), coord.getCol())) {
            throw new IllegalArgumentException("Out of bounds");
        }
        return grid[coord.getRow()][coord.getCol()].attack();
    }

    private boolean inBounds(int row, int col) {
        return row >= 0 && col >= 0 && row < size && col < size;
    }
}