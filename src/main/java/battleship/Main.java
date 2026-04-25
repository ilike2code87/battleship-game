package battleship;

import battleship.board.Board;
import battleship.board.Coordinate;
import battleship.command.AttackCommand;
import battleship.command.PlaceShipCommand;
import battleship.game.GameController;
import battleship.player.ComputerPlayer;
import battleship.player.Player;
import battleship.ship.Ship;
import battleship.ship.ShipFactory;
import battleship.strategy.HuntTargetStrategy;
import battleship.strategy.RandomAttackStrategy;

public class Main {
    public static void main(String[] args) {
        int boardSize = 10; //10x10 grid
        ShipFactory factory = new ShipFactory(); //build our ships

        //dependency injection because we are creating two computer players and passing in their strategies from outside
        //players strategy is decision is here
        Player cpu1 = new ComputerPlayer("Random", new RandomAttackStrategy(boardSize));
        Player cpu2 = new ComputerPlayer("Hunter", new HuntTargetStrategy(boardSize));

        //places ships for both players
        Board board1 = new Board(boardSize);
        Board board2 = new Board(boardSize);

        GameController game = new GameController(cpu1, board1, cpu2, board2);

        placeFleet(game, cpu1, factory.createStandardFleet());
        placeFleet(game, cpu2, factory.createStandardFleet());

        game.startGame(); //starts game
        System.out.println("Game started: " + cpu1.getName() + " vs " + cpu2.getName());

        int turn = 0;
        while (!game.isGameOver()) { //keep looping until someone wins
            Player current = game.getCurrentPlayer(); //ask controller whos turn it is
            Coordinate target = current.chooseMove(game.getOpponentBoard());
            // ask that player where they want to attack
            // we pass them the opponent's board so they can see what's been attacked already
            // for ComputerPlayer this calls the strategy (Random or HuntTarget)
            int result = game.execute(new AttackCommand(current, target));
            // wrap the attack in a Command object and send it to the controller
            // the controller validates it, executes it, and adds it to history
            // result comes back as a number: 0=HIT, 1=MISS, 2=SUNK, 3=ALREADY_ATTACKED
            System.out.printf("Turn %3d | %-8s attacks (%d,%d) -> %d%n",
                    ++turn, current.getName(),  // print who attacked
                    target.getRow(), // print the row they attacked
                    target.getCol(), // print the column they attacked
                    result); //print result
        }

        System.out.println("\nGame over! Winner: " + game.getWinner().getName());
        System.out.println("Total moves: " + game.getHistory().size());
    }

    private static void placeFleet(GameController game, Player player, Ship[] fleet) {
        // takes a player and their array of ships and places them all on the board
        int row = 0; // start placing at row 0, move down one row per ship
        for (Ship ship : fleet) { // loop through each ship in the fleet
            game.execute(new PlaceShipCommand(player, ship,
                    new Coordinate(row, 0), true));
            // wrap the placement in a PlaceShipCommand
            // new Coordinate(row, 0) means start at column 0 on the current row
            // true means place it horizontally
            row++; // move to the next row for the next ship
        }
    }
}