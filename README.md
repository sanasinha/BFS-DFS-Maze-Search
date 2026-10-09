# CS 440 Lab 2: Maze Routing with BFS & DFS

Agents that find their way through grid mazes using classic graph search, built for **CS 440: Introduction to Artificial Intelligence** at Boston University (Fall 2026).

## The agents

Both agents implement `search(src, goal, state)`, which returns a `Path` from the start to the `FINISH` tile. The course framework's `MazeAgent` state machine handles executing that plan turn by turn.

### `BFSMazeAgent`: breadth-first search
- Uses a FIFO **queue** of partial paths, so the maze is explored in rings of increasing distance from the start.
- A coordinate is marked **visited when it is enqueued**, so every square enters the queue at most once.
- A neighbour is expanded only if it is in bounds and its tile can be passed through. Every step costs `1`.
- Because every step costs the same, the first path to reach the goal uses the **fewest moves**.
- It considers all 8 values of `Direction`, so diagonal steps are allowed.

### `DFSMazeAgent`: depth-first search
- Uses a LIFO **stack**, so it follows one corridor as deep as it goes before backtracking.
- A coordinate is marked **visited when it is popped**. Duplicate stack entries are skipped.
- It moves only in the 4 cardinal directions (`Direction.getCardinalDirections()`), and every step costs `1`.
- It always finds a route if one exists, but **not necessarily the shortest**.

### Path representation

Paths use the course's `Path<Coordinate>` type. It is a reverse singly-linked list, so extending a path takes O(1) time and shares memory with the path it extends:

```java
new Path<>(oldPath, neighbor, 1.0)
```

## Project layout

```
src/labs/routing/agents/
├── BFSMazeAgent.java
└── DFSMazeAgent.java
tests/labs/routing/
├── RunAllTests.java
├── agents/BFSMazeAgentTests.java
└── states/StateRepository.java
routing.srcs       # list of source files passed to javac
```

## Running it

You need **Java 21**. The maze engine (`labs-search-global-routing-jar`), `argparse4j` and the JUnit 5 jars are not in this repo. Put them in `lib/` before compiling.

```bash
# macOS / Linux, from the repo root
javac -cp "./lib/*:." @routing.srcs

# run an agent (add -h to see all options)
java -cp "./lib/*:." edu.bu.labs.routing.Main            src.labs.routing.agents.BFSMazeAgent
java -cp "./lib/*:." edu.bu.labs.routing.Main            src.labs.routing.agents.DFSMazeAgent

# run the unit tests
java -cp "./lib/*:." tests.labs.routing.RunAllTests
```

On Windows, use `;` instead of `:` in the classpath.

---
*Coursework for CS 440 at Boston University. The maze engine, `MazeAgent` state machine and test scaffolding were provided by the course staff. The BFS and DFS implementations are my own.*
