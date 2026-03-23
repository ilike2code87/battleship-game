package battleship.strategy;

import battleship.board.Board;
import battleship.board.Coordinate;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class RandomAttackStrategy implements AttackStrategy {
    private final Random random;
    private final int boardSize;
    private final Set<Coordinate> attemptedShots;

    public RandomAttackStrategy(int boardSize) {
        this.random = new Random();
        this.boardSize = boardSize;
        this.attemptedShots = new HashSet<>();
    }

    @Override
    public Coordinate chooseAttack(Board opponentBoard) {
        Coordinate coordinate;
        do {
            int row = random.nextInt(boardSize);
            int col = random.nextInt(boardSize);
            coordinate = new Coordinate(row, col);
        } while (attemptedShots.contains(coordinate));

        attemptedShots.add(coordinate);
        return coordinate;
    }
}