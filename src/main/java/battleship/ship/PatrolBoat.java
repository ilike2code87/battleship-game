package battleship.ship;

public class PatrolBoat extends Ship {
    public PatrolBoat() { super("PatrolBoat", 2); }

    @Override
    public String getType() { return "PatrolBoat"; }
}