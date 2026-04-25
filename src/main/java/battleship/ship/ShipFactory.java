package battleship.ship;

public class ShipFactory {

    public Ship createShip(String type) {
        switch (type.toLowerCase()) {
            case "carrier":    return new Carrier();
            case "battleship": return new Battleship();
            case "destroyer":  return new Destroyer();
            case "submarine":  return new Submarine();
            case "patrolboat": return new PatrolBoat();
            default: throw new IllegalArgumentException("Unknown ship type: " + type);
        }
    }

    public Ship[] createStandardFleet() {
        return new Ship[]{
                new Carrier(),
                new Battleship(),
                new Destroyer(),
                new Submarine(),
                new PatrolBoat()
        };
    }
}
