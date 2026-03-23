package battleship.board;

import battleship.ship.Ship;

public class Cell {
    private Ship ship;
    private boolean hit;

    public boolean hasShip() {
        return ship != null;
    }

    public void placeShip(Ship ship) {
        this.ship = ship;
    }

    public boolean isHit() {
        return hit;
    }

    public boolean attack() {
        if (hit) return false; // already attacked
        hit = true;

        if (ship != null) {
            ship.registerHit();
            return true;
        }
        return false;
    }
}