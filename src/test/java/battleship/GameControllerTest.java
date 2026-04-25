package battleship;

import battleship.board.AttackResult;
import battleship.board.Board;
import battleship.board.Coordinate;
import battleship.command.AttackCommand;
import battleship.command.PlaceShipCommand;
import battleship.game.GameController;
import battleship.player.ComputerPlayer;
import battleship.player.Player;
import battleship.ship.Destroyer;
import battleship.ship.PatrolBoat;
import battleship.strategy.RandomAttackStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameControllerTest {

    private Player p1, p2;
    private Board  b1, b2;
    private GameController game;

    @BeforeEach
    void setUp() {
        p1 = new ComputerPlayer("Alice", new RandomAttackStrategy(5));
        p2 = new ComputerPlayer("Bob",   new RandomAttackStrategy(5));
        b1 = new Board(5);
        b2 = new Board(5);
        game = new GameController(p1, b1, p2, b2);
    }

    @Test
    void initialState_notStarted() {
        assertFalse(game.isGameStarted());
        assertFalse(game.isGameOver());
    }

    @Test
    void attackCommand_rejectedBeforeStart() {
        assertFalse(new AttackCommand(p1, new Coordinate(0, 0)).isValid(game));
    }

    @Test
    void startGame_setsGameStarted() {
        game.startGame();
        assertTrue(game.isGameStarted());
    }

    @Test
    void attack_advancesTurn() {
        placeOneShipEach();
        game.startGame();
        assertEquals(p1, game.getCurrentPlayer());
        game.execute(new AttackCommand(p1, new Coordinate(0, 0)));
        assertEquals(p2, game.getCurrentPlayer());
    }

    @Test
    void wrongPlayerAttack_isRejected() {
        placeOneShipEach();
        game.startGame();
        assertFalse(new AttackCommand(p2, new Coordinate(0, 0)).isValid(game));
    }

    @Test
    void repeatedAttack_isRejected() {
        placeOneShipEach();
        game.startGame();
        game.execute(new AttackCommand(p1, new Coordinate(0, 0)));
        game.execute(new AttackCommand(p2, new Coordinate(0, 0)));
        assertFalse(new AttackCommand(p1, new Coordinate(0, 0)).isValid(game));
    }

    @Test
    void sinkingAllShips_endsGame() {
        game.execute(new PlaceShipCommand(p1, new PatrolBoat(), new Coordinate(0,0), true));
        game.execute(new PlaceShipCommand(p2, new PatrolBoat(), new Coordinate(0,0), true));
        game.startGame();

        game.execute(new AttackCommand(p1, new Coordinate(0, 0)));
        game.execute(new AttackCommand(p2, new Coordinate(4, 4)));
        int last = game.execute(new AttackCommand(p1, new Coordinate(0, 1)));

        assertEquals(AttackResult.SUNK, last);
        assertTrue(game.isGameOver());
        assertEquals(p1, game.getWinner());
    }

    @Test
    void history_recordsCommands() {
        placeOneShipEach();
        game.startGame();
        game.execute(new AttackCommand(p1, new Coordinate(1, 1)));
        game.execute(new AttackCommand(p2, new Coordinate(2, 2)));
        // 2 place commands + 2 attack commands = 4 total
        assertEquals(4, game.getHistory().size());
    }

    private void placeOneShipEach() {
        game.execute(new PlaceShipCommand(p1, new Destroyer(), new Coordinate(0,0), true));
        game.execute(new PlaceShipCommand(p2, new Destroyer(), new Coordinate(0,0), true));
    }
}