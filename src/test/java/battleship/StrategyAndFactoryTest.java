package battleship;

import battleship.board.Board;
import battleship.board.Coordinate;
import battleship.player.ComputerPlayer;
import battleship.ship.Ship;
import battleship.ship.ShipFactory;
import battleship.strategy.AttackStrategy;
import battleship.strategy.RandomAttackStrategy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StrategyAndFactoryTest {

    @Test
    void shipFactory_createsDestroyer() {
        ShipFactory factory = new ShipFactory();
        Ship ship = factory.createShip("destroyer");

        assertEquals("Destroyer", ship.getName());
        assertEquals(3, ship.getSize());
    }

    @Test
    void computerPlayer_usesInjectedStrategy() {
        AttackStrategy strategy = new RandomAttackStrategy(5);
        ComputerPlayer player = new ComputerPlayer("CPU", strategy);
        Coordinate move = player.chooseMove(new Board(5));

        assertNotNull(move);
    }
}