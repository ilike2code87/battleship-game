package battleship.ship;

public class Submarine extends Ship {
    public Submarine() { super("Submarine", 3); }

    @Override
    public boolean isStealthy() { return true; }

    @Override
    public String getType() { return "Submarine"; }
}