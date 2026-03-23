package battleship.player;

import battleship.board.Board;
import battleship.board.Coordinate;
import battleship.strategy.AttackStrategy;

public class ComputerPlayer implements Player {
    private final String name;
    private final AttackStrategy attackStrategy;

    public ComputerPlayer(String name, AttackStrategy attackStrategy) {
        this.name = name;
        this.attackStrategy = attackStrategy;
    }

    @Override
    public Coordinate chooseMove(Board opponentBoard) {
        return attackStrategy.chooseAttack(opponentBoard);
    }

    @Override
    public String getName() {
        return name;
    }
}