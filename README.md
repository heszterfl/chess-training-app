# Chess Training App

A Java-based backend application for parsing, processing, and storing chess games using PGN (Portable Game Notation).

This project focuses on clean architecture, domain-driven design, and building a complete data processing pipeline from raw PGN input to persistent storage.

---

## Features

### PGN Parsing
- Parses chess games from PGN string format
- Extracts:
    - Players
    - Date
    - Result
    - Move list (SAN notation)

### SAN Move Resolution
- Converts SAN moves (e.g. `e4`, `Nf3`) into domain `Move` objects
- Resolves:
    - Piece type
    - Color
    - From / To positions
- Uses real board state for validation

### Game Simulation
- Maintains a `Board` state
- Validates moves before applying them
- Ensures legal move execution

### Persistence Layer
- SQLite-based storage
- Stores:
    - Games
    - Moves (with order)
- Clean repository structure:
    - `GameRepository`
    - `MoveRepository`

### End-to-End PGN Import

Complete pipeline:

```
PGN String -> Parser -> Converter -> Move Resolution -> Database
```

---

## Architecture Overview

The project follows a layered structure:

```
pgn/
  ├── PgnParser
  ├── PgnGame
  ├── PgnGameConverter
  ├── SanMoveResolver

domain/
  ├── Board
  ├── Move
  ├── Game
  ├── Piece hierarchy

persistence/
  ├── Database
  ├── GameRepository
  ├── MoveRepository

app/
  ├── PgnImportService
```

### Key Design Principles
- Separation of concerns
- Single responsibility per class
- No mixing of parsing, domain logic, and persistence
- Testable components

---

## Example Flow

```java
String pgn = """
[White "Carlsen"]
[Black "Nakamura"]
[Date "2026-03-18"]
[Result "1-0"]

1. e4 e5 2. Nf3 Nc6
""";

PgnImportService service = new PgnImportService();
Game game = service.importPgn(pgn);
```

Result:
- Game stored in DB
- Moves stored in correct order
- Board logic validated each move

---

## Testing

The project includes:

### Unit Tests
- PGN parsing
- Move resolution
- Repository logic

### Integration Tests
- Full PGN import pipeline
- Database verification
- Move correctness (from/to, piece, color)

### Example

```
PGN -> import -> DB -> query -> assert correctness
```

---

## Database

- SQLite database

Tables:
- games
- moves

Moves are stored with:
- game_id
- move_index (ensures order)
- piece, color, from, to

---

## Tech Stack

- Java 17
- Maven
- JUnit 5
- SQLite (JDBC)

---

## Current Limitations

- No support yet for:
    - Castling (O-O)
    - En passant
    - Promotion
    - PGN comments or variations
- Only simple SAN moves supported

---

## Future Improvements

- Full PGN support (comments, variations)
- Special chess rules (castling, promotion, en passant)
- UI / frontend integration
- Game search and filtering
- Import from PGN files

---

## Purpose of the Project

This project was built as part of a backend portfolio to demonstrate:

- Object-oriented design
- Clean architecture
- Data processing pipelines
- Integration of parsing + domain logic + persistence
- Writing meaningful tests

---

## How to Run

1. Clone repository

2. Build the project:

```bash
mvn clean install
```

3. Run tests:

```bash
mvn test
```

---

## Project Structure

```
src/
  ├── main/java/hu/eszter/chess/
  │   ├── domain/
  │   ├── pgn/
  │   ├── persistence/
  │   └── app/
  │
  └── test/java/hu/eszter/chess/
```

---

## Author

Developed as part of a transition into backend software engineering with a focus on Java and clean system design.
