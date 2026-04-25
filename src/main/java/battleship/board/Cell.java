package battleship.board;

import battleship.ship.Ship;

public class Cell {

    private Ship ship;
    private boolean attacked;

    public boolean hasShip() { return ship != null; }

    public void placeShip(Ship ship) { this.ship = ship; }

    public boolean isAttacked() { return attacked; }

    public boolean wasHit() {
        return attacked && ship != null && !ship.isStealthy();
    }

    public int attack() {
        if (attacked) return AttackResult.ALREADY_ATTACKED;

        attacked = true;

        if (ship == null)      return AttackResult.MISS;

        ship.registerHit();

        if (ship.isStealthy()) return AttackResult.MISS;
        if (ship.isSunk())     return AttackResult.SUNK;
        return AttackResult.HIT;
    }
}