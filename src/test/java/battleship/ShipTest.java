package battleship;

import battleship.ship.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ShipTest {

    @Test
    void carrier_sinksAfterFiveHits() {
        Carrier carrier = new Carrier();
        for (int i = 0; i < 5; i++) carrier.registerHit();
        assertTrue(carrier.isSunk());
    }

    @Test
    void submarine_isStealthy() {
        assertTrue(new Submarine().isStealthy());
    }

    @Test
    void submarine_sinksAfterThreeHits() {
        Submarine sub = new Submarine();
        for (int i = 0; i < 3; i++) sub.registerHit();
        assertTrue(sub.isSunk());
    }

    @Test
    void standardShips_areNotStealthy() {
        assertFalse(new Carrier().isStealthy());
        assertFalse(new Battleship().isStealthy());
        assertFalse(new Destroyer().isStealthy());
        assertFalse(new PatrolBoat().isStealthy());
    }

    @Test
    void patrolBoat_sinksAfterTwoHits() {
        PatrolBoat boat = new PatrolBoat();
        boat.registerHit();
        assertFalse(boat.isSunk());
        boat.registerHit();
        assertTrue(boat.isSunk());
    }

    @Test
    void factory_createsCorrectSubclasses() {
        ShipFactory factory = new ShipFactory();
        assertInstanceOf(Carrier.class,    factory.createShip("carrier"));
        assertInstanceOf(Battleship.class, factory.createShip("battleship"));
        assertInstanceOf(Destroyer.class,  factory.createShip("destroyer"));
        assertInstanceOf(Submarine.class,  factory.createShip("submarine"));
        assertInstanceOf(PatrolBoat.class, factory.createShip("patrolboat"));
    }

    @Test
    void factory_throwsOnUnknownType() {
        assertThrows(IllegalArgumentException.class,
                () -> new ShipFactory().createShip("hovercraft"));
    }

    @Test
    void factory_standardFleet_hasFiveShips() {
        assertEquals(5, new ShipFactory().createStandardFleet().length);
    }
}