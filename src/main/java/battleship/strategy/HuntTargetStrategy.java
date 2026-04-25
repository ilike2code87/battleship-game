package battleship.strategy;

import battleship.board.AttackResult;
import battleship.board.Board;
import battleship.board.Coordinate;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

public class HuntTargetStrategy implements AttackStrategy {

    private boolean hunting = true;
    private final int boardSize;
    private final Set<Coordinate> fired = new HashSet<>();
    private final Deque<Coordinate> targetQueue = new ArrayDeque<>();

    public HuntTargetStrategy(int boardSize) {
        this.boardSize = boardSize;
    }

    @Override
    public Coordinate chooseAttack(Board opponentBoard) {
        if (!hunting) {
            Coordinate next = drainQueue();
            if (next != null) return next;
            hunting = true;
        }
        return hunt();
    }

    @Override
    public void recordResult(Coordinate coord, int result) {
        if (result == AttackResult.HIT) {
            hunting = false;
            enqueueAdjacent(coord);
        } else if (result == AttackResult.SUNK) {
            hunting = true;
            targetQueue.clear();
        }
    }

    private Coordinate drainQueue() {
        while (!targetQueue.isEmpty()) {
            Coordinate candidate = targetQueue.poll();
            if (!fired.contains(candidate)) {
                fired.add(candidate);
                return candidate;
            }
        }
        return null;
    }

    private Coordinate hunt() {
        for (int r = 0; r < boardSize; r++)
            for (int c = 0; c < boardSize; c++)
                if ((r + c) % 2 == 0) {
                    Coordinate coord = new Coordinate(r, c);
                    if (!fired.contains(coord)) { fired.add(coord); return coord; }
                }
        for (int r = 0; r < boardSize; r++)
            for (int c = 0; c < boardSize; c++) {
                Coordinate coord = new Coordinate(r, c);
                if (!fired.contains(coord)) { fired.add(coord); return coord; }
            }
        throw new IllegalStateException("No valid moves remaining");
    }

    private void enqueueAdjacent(Coordinate coord) {
        int r = coord.getRow(), c = coord.getCol();
        int[][] dirs = {{-1,0},{1,0},{0,-1},{0,1}};
        for (int[] d : dirs) {
            int nr = r + d[0], nc = c + d[1];
            if (nr >= 0 && nr < boardSize && nc >= 0 && nc < boardSize) {
                Coordinate adj = new Coordinate(nr, nc);
                if (!fired.contains(adj)) targetQueue.add(adj);
            }
        }
    }
}