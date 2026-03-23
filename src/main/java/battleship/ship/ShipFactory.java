package battleship.ship;

public class ShipFactory {

    public Ship createShip(String type) {
        return switch (type.toLowerCase()) {
            case "carrier" -> new Ship("Carrier", 5);
            case "battleship" -> new Ship("Battleship", 4);
            case "destroyer" -> new Ship("Destroyer", 3);
            case "submarine" -> new Ship("Submarine", 3);
            case "patrolboat" -> new Ship("PatrolBoat", 2);
            default -> throw new IllegalArgumentException("Unknown ship type: " + type);
        };
    }
}