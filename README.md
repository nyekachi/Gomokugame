# Gomokugame
# Gomoku (Five in a Row):

A command-line implementation of the classic Gomoku game, allowing two modes of play:

- Human vs Human: Two players alternate turns, entering moves by row and column.
- Human vs AI: Play against a computer opponent powered by a Minimax algorithm with alpha-beta pruning.

 Features

- 9×9 Board Display  
  - Displays row and column indices for easy reference.  
  - Uses `.` to represent empty spaces, `B` for Black stones, and `W` for White stones.

-  Command-Line interface
  - Text-based menu to select game mode.  
  - Prompts for player names and symbol choices (B or W).  
  - In-game prompts for entering moves (row and column).

- Input Validation
  - Ensures moves are within board boundaries (0–8).  
  - Prevents placement on already occupied cells.  
  - Re-prompts players on invalid input until a valid move is entered.

- Game Logic  
  - Checks for five consecutive stones horizontally, vertically, or diagonally.  
  - Detects draw when the board is full with no winner.  
  - Automatically switches turns between players.

- AI Opponent  
  - Uses Minimax search up to a configurable depth (default 3).  
  - Implements alpha-beta pruning to skip unnecessary branches and improve performance.  
  - Heuristic board evaluation based on consecutive stone counts and blocked lines.

Getting Started

1. Compile:
   bash
   javac GomokuGame.java
   
2. Run:
   bash
   java GomokuGame
   
3. Follow Prompts:
   - Select game mode (1 or 2).  
   - Enter player names and symbols.  
   - During your turn, input your move as two numbers (row column).


