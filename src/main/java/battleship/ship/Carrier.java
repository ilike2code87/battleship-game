package battleship.ship;

public class Carrier extends Ship {
    public Carrier() { super("Carrier", 5); }

    @Override
    public String getType() { return "Carrier"; }
}