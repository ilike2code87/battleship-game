package battleship.ship;

public class Destroyer extends Ship {
    public Destroyer() { super("Destroyer", 3); }

    @Override
    public String getType() { return "Destroyer"; }
}