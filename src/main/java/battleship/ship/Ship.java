package battleship.ship;

public class Ship {
    private final String name;
    private final int size;
    private int hits;

    public Ship(String name, int size) {
        this.name = name;
        this.size = size;
        this.hits = 0;
    }

    public void registerHit() {
        if (hits < size) {
            hits++;
        }
    }

    public boolean isSunk() {
        return hits >= size;
    }

    public int getSize() {
        return size;
    }

    public String getName() {
        return name;
    }
}