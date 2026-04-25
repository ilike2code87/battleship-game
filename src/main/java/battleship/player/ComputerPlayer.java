package battleship.player;

import battleship.board.Board;
import battleship.board.Coordinate;
import battleship.strategy.AttackStrategy;

public class ComputerPlayer implements Player {

    private final String name;
    private final AttackStrategy strategy;

    public ComputerPlayer(String name, AttackStrategy strategy) {
        this.name     = name;
        this.strategy = strategy;
    }

    @Override
    public Coordinate chooseMove(Board opponentBoard) {
        return strategy.chooseAttack(opponentBoard);
    }

    @Override
    public void recordResult(Coordinate coord, int result) {
        strategy.recordResult(coord, result);
    }

    @Override
    public String getName() { return name; }
}