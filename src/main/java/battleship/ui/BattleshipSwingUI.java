package battleship.ui;

import battleship.board.*;
import battleship.command.*;
import battleship.game.GameController;
import battleship.player.*;
import battleship.ship.*;
import battleship.strategy.HuntTargetStrategy;

import javax.swing.*;
import java.awt.*;

public class BattleshipSwingUI extends JFrame {
    private static final int BOARD_SIZE = 10;

    private GameController game;
    private Player human;
    private Player cpu;

    private JButton[][] playerButtons;
    private JButton[][] enemyButtons;

    private JTextArea logArea;
    private JLabel statusLabel;
    private JButton rotateButton;

    private Ship[] humanFleet;
    private int shipIndex;
    private boolean placingShips;
    private boolean horizontal = true;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BattleshipSwingUI().setVisible(true));
    }

    public BattleshipSwingUI() {
        setTitle("Battleship");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 650);
        setLocationRelativeTo(null);

        buildScreen();
        startNewGame();
    }

    private void buildScreen() {
        setLayout(new BorderLayout(10, 10));

        statusLabel = new JLabel("Battleship", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 22));
        add(statusLabel, BorderLayout.NORTH);

        JPanel boardsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        playerButtons = new JButton[BOARD_SIZE][BOARD_SIZE];
        enemyButtons = new JButton[BOARD_SIZE][BOARD_SIZE];

        boardsPanel.add(createBoardPanel("Your Board - Click to Place Ships", playerButtons, false));
        boardsPanel.add(createBoardPanel("Enemy Board - Click to Attack", enemyButtons, true));
        add(boardsPanel, BorderLayout.CENTER);

        logArea = new JTextArea(8, 40);
        logArea.setEditable(false);

        JButton restartButton = new JButton("Restart Game");
        restartButton.addActionListener(e -> startNewGame());

        rotateButton = new JButton("Rotate: Horizontal");
        rotateButton.addActionListener(e -> {
            horizontal = !horizontal;
            rotateButton.setText(horizontal ? "Rotate: Horizontal" : "Rotate: Vertical");
        });

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1));
        buttonPanel.add(rotateButton);
        buttonPanel.add(restartButton);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(new JScrollPane(logArea), BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createBoardPanel(String title, JButton[][] buttons, boolean enemyBoard) {
        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.setBorder(BorderFactory.createTitledBorder(title));

        JPanel gridPanel = new JPanel(new GridLayout(BOARD_SIZE, BOARD_SIZE));

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(45, 45));
                button.setMargin(new Insets(0, 0, 0, 0));
                button.setBackground(new Color(80, 150, 220));
                button.setOpaque(true);
                button.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                final int r = row;
                final int c = col;

                if (enemyBoard) {
                    button.addActionListener(e -> handleHumanAttack(r, c));
                } else {
                    button.addActionListener(e -> handleShipPlacement(r, c));
                }

                buttons[row][col] = button;
                gridPanel.add(button);
            }
        }

        outerPanel.add(gridPanel, BorderLayout.CENTER);
        return outerPanel;
    }

    private void startNewGame() {
        ShipFactory factory = new ShipFactory();

        human = new UiPlayer("You");
        cpu = new ComputerPlayer("Computer", new HuntTargetStrategy(BOARD_SIZE));

        game = new GameController(
                human, new Board(BOARD_SIZE),
                cpu, new Board(BOARD_SIZE)
        );

        humanFleet = factory.createStandardFleet();
        shipIndex = 0;
        placingShips = true;
        horizontal = true;

        resetButtons();
        logArea.setText("");
        rotateButton.setEnabled(true);
        rotateButton.setText("Rotate: Horizontal");

        statusLabel.setText("Place your " + currentShipName());
        log("Click your board to place ships.");
        log("Placing: " + currentShipName());
    }

    private void handleShipPlacement(int row, int col) {
        if (!placingShips) return;

        Ship ship = humanFleet[shipIndex];

        try {
            game.execute(new PlaceShipCommand(human, ship, new Coordinate(row, col), horizontal));
            drawShip(playerButtons, ship, row, col, horizontal);

            log("Placed " + ship.getName() + " at (" + row + "," + col + ")");

            shipIndex++;

            if (shipIndex >= humanFleet.length) {
                finishSetup();
            } else {
                statusLabel.setText("Place your " + currentShipName());
                log("Placing: " + currentShipName());
            }

        } catch (IllegalArgumentException ex) {
            log("Invalid placement. Try another spot.");
        }
    }

    private void finishSetup() {
        placingShips = false;
        rotateButton.setEnabled(false);

        ShipFactory factory = new ShipFactory();
        placeComputerFleet(factory.createStandardFleet());

        game.startGame();

        statusLabel.setText("Your turn. Click the enemy board to attack.");
        log("All ships placed. Game started.");
    }

    private void placeComputerFleet(Ship[] fleet) {
        placeCpuShip(fleet[0], 0, 5, false);
        placeCpuShip(fleet[1], 1, 1, false);
        placeCpuShip(fleet[2], 5, 6, false);
        placeCpuShip(fleet[3], 6, 2, true);
        placeCpuShip(fleet[4], 9, 7, true);
    }

    private void placeCpuShip(Ship ship, int row, int col, boolean horizontal) {
        game.execute(new PlaceShipCommand(cpu, ship, new Coordinate(row, col), horizontal));
    }

    private void handleHumanAttack(int row, int col) {
        if (placingShips) {
            log("Place all your ships first.");
            return;
        }

        if (game.isGameOver()) return;
        if (game.getCurrentPlayer() != human) return;

        try {
            int result = game.execute(new AttackCommand(human, new Coordinate(row, col)));
            updateEnemyCell(row, col, result);
            log("You attacked (" + row + "," + col + ") -> " + resultText(result));

            if (checkGameOver()) return;

            runComputerTurn();

        } catch (IllegalArgumentException ex) {
            log("Invalid move. You already attacked that spot.");
        }
    }

    private void runComputerTurn() {
        Coordinate target = cpu.chooseMove(game.getOpponentBoard());
        int result = game.execute(new AttackCommand(cpu, target));

        updatePlayerCell(target.getRow(), target.getCol(), result);
        log("Computer attacked " + target + " -> " + resultText(result));

        checkGameOver();
    }

    private boolean checkGameOver() {
        if (!game.isGameOver()) {
            statusLabel.setText("Your turn. Click the enemy board to attack.");
            return false;
        }

        statusLabel.setText("Game over. Winner: " + game.getWinner().getName());
        log("Game over. Winner: " + game.getWinner().getName());
        disableEnemyBoard();
        return true;
    }

    private void drawShip(JButton[][] buttons, Ship ship, int row, int col, boolean horizontal) {
        for (int i = 0; i < ship.getSize(); i++) {
            int r = horizontal ? row : row + i;
            int c = horizontal ? col + i : col;

            buttons[r][c].setBackground(new Color(90, 170, 90));
            buttons[r][c].setText("S");
        }
    }

    private void updateEnemyCell(int row, int col, int result) {
        JButton button = enemyButtons[row][col];
        button.setEnabled(false);

        if (result == AttackResult.HIT || result == AttackResult.SUNK) {
            button.setBackground(Color.RED);
            button.setText(result == AttackResult.SUNK ? "X" : "H");
        } else {
            button.setBackground(Color.LIGHT_GRAY);
            button.setText("M");
        }
    }

    private void updatePlayerCell(int row, int col, int result) {
        JButton button = playerButtons[row][col];

        if (result == AttackResult.HIT || result == AttackResult.SUNK) {
            button.setBackground(Color.RED);
            button.setText(result == AttackResult.SUNK ? "X" : "H");
        } else {
            button.setBackground(Color.LIGHT_GRAY);
            button.setText("M");
        }
    }

    private String currentShipName() {
        Ship ship = humanFleet[shipIndex];
        return ship.getName() + " size " + ship.getSize();
    }

    private String resultText(int result) {
        return switch (result) {
            case AttackResult.HIT -> "HIT";
            case AttackResult.MISS -> "MISS";
            case AttackResult.SUNK -> "SUNK";
            case AttackResult.ALREADY_ATTACKED -> "ALREADY ATTACKED";
            default -> "UNKNOWN";
        };
    }

    private void resetButtons() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                playerButtons[row][col].setBackground(new Color(80, 150, 220));
                playerButtons[row][col].setText("");
                playerButtons[row][col].setEnabled(true);

                enemyButtons[row][col].setBackground(new Color(80, 150, 220));
                enemyButtons[row][col].setText("");
                enemyButtons[row][col].setEnabled(true);
            }
        }
    }

    private void disableEnemyBoard() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                enemyButtons[row][col].setEnabled(false);
            }
        }
    }

    private void log(String message) {
        logArea.append(message + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    private static class UiPlayer implements Player {
        private final String name;

        private UiPlayer(String name) {
            this.name = name;
        }

        @Override
        public Coordinate chooseMove(Board opponentBoard) {
            throw new UnsupportedOperationException("UI player clicks instead.");
        }

        @Override
        public String getName() {
            return name;
        }
    }
}