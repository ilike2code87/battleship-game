package battleship.ship;

public abstract class Ship {

    protected final String name;
    protected final int size;
    protected int hits;

    protected Ship(String name, int size) {
        this.name = name;
        this.size = size;
        this.hits = 0;
    }

    public void registerHit() {
        if (hits < size) hits++;
    }

    public boolean isSunk() {
        return hits >= size;
    }

    public boolean isStealthy() {
        return false;
    }

    public int getSize()    { return size; }
    public String getName() { return name; }

    public abstract String getType();
}