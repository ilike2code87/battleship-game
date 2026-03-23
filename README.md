## Design Patterns

### Strategy Pattern
The Strategy pattern is used for attack behavior. The `AttackStrategy` interface defines how an attack is chosen, and `RandomAttackStrategy` provides one concrete implementation. This allows attack behavior to be swapped without changing player logic.

### Factory Pattern
The Factory pattern is used for ship creation. The `ShipFactory` class centralizes the logic for creating different ship types such as Carrier, Battleship, Destroyer, Submarine, and PatrolBoat. This keeps creation logic out of the rest of the game.

### State Pattern
The State pattern is used to represent the phases of the game. The `GameState` interface is implemented by `SetupState`, `InProgressState`, and `GameOverState`. This makes it easier to manage the game flow cleanly.

## OO Principles

- **Coding to abstractions:** the project uses interfaces such as `AttackStrategy`, `Player`, and `GameState`.
- **Polymorphism:** multiple classes implement the same interfaces, such as `RandomAttackStrategy` implementing `AttackStrategy` and state classes implementing `GameState`.
- **Dependency Injection:** `ComputerPlayer` receives an `AttackStrategy` through its constructor rather than creating one internally.

## Tests
This project currently includes tests for:
- valid ship placement
- overlapping ship placement rejection
- hit detection
- miss detection
- ship sinking
- factory ship creation
- injected strategy behavior