package battleship.player;

import battleship.board.Board;
import battleship.board.Coordinate;

import java.util.Scanner;

public class HumanPlayer implements Player {

    private final String name;
    private final Scanner scanner;

    public HumanPlayer(String name, Scanner scanner) {
        this.name    = name;
        this.scanner = scanner;
    }

    @Override
    public Coordinate chooseMove(Board opponentBoard) {
        System.out.printf("%s - enter attack (row col): ", name);
        int row = scanner.nextInt();
        int col = scanner.nextInt();
        return new Coordinate(row, col);
    }

    @Override
    public String getName() { return name; }
}