package battleship.ship;

public class Battleship extends Ship {
    public Battleship() { super("Battleship", 4); }

    @Override
    public String getType() { return "Battleship"; }
}