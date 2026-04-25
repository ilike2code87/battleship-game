package battleship.strategy;

import battleship.board.Board;
import battleship.board.Coordinate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomAttackStrategy implements AttackStrategy {

    private final List<Coordinate> remaining;

    public RandomAttackStrategy(int boardSize) {
        remaining = new ArrayList<>();
        for (int r = 0; r < boardSize; r++)
            for (int c = 0; c < boardSize; c++)
                remaining.add(new Coordinate(r, c));
        Collections.shuffle(remaining, new Random());
    }

    @Override
    public Coordinate chooseAttack(Board opponentBoard) {
        if (remaining.isEmpty())
            throw new IllegalStateException("No valid moves remaining");
        return remaining.remove(remaining.size() - 1);
    }
}