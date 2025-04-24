/**
 * Gomoku (Five in a Row) Game Implementation
 * 
 * This program implements a command-line Gomoku game with two modes:
 * 1. Human vs Human (2-player)
 * 2. Human vs AI (1-player) using Minimax algorithm
 * 
 * The game uses a 9x9 board and follows standard Gomoku rules.
 */

import java.util.Scanner;

public class GomokuGame {
    public static void main(String[] args) {
        GomokuGame game = new GomokuGame();
        game.startGame();
    }

    private Board board;
    private Player player1;
    private Player player2;
    private boolean isAgainstAI;
    
    /**
     * Starts the Gomoku game
     */
    public void startGame() {
        // Initialize game settings
        selectGameMode();
        initializePlayers();
        
        // Main game loop
        while (!board.isGameOver()) {
            Player currentPlayer = (board.getCurrentTurn() == 'B') ? player1 : player2;
            board.display();
            
            if (isAgainstAI && currentPlayer instanceof AIPlayer) {
                // AI's turn
                ((AIPlayer)currentPlayer).makeMove(board);
            } else {
                // Human player's turn
                currentPlayer.makeMove(board);
            }
            
            // Check for win/draw after each move
            if (board.checkWin()) {
                board.display();
                System.out.println(currentPlayer.getName() + " wins!");
                break;
            } else if (board.isDraw()) {
                board.display();
                System.out.println("The game is a draw!");
                break;
            }
            
            // Switch turns
            board.switchTurn();
        }
    }
    
    private void selectGameMode() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select game mode:");
        System.out.println("1. Human vs AI");
        System.out.println("2. Human vs Human");
        System.out.print("Enter choice (1 or 2): ");
        int choice = scanner.nextInt();
        isAgainstAI = (choice == 1);
    }
    
    private void initializePlayers() {
        Scanner scanner = new Scanner(System.in);
        board = new Board();
        
        if (isAgainstAI) {
            System.out.print("Enter your name: ");
            String humanName = scanner.next();
            System.out.print("Choose your symbol (B or W): ");
            char humanSymbol = scanner.next().charAt(0);
            
            player1 = (humanSymbol == 'B') ? 
                new HumanPlayer(humanName, 'B') : 
                new HumanPlayer(humanName, 'W');
            player2 = (humanSymbol == 'B') ? 
                new AIPlayer('W') : 
                new AIPlayer('B');
        } else {
            System.out.print("Enter Player 1 name: ");
            String player1Name = scanner.next();
            System.out.print("Player 1, choose your symbol (B or W): ");
            char player1Symbol = scanner.next().charAt(0);
            
            System.out.print("Enter Player 2 name: ");
            String player2Name = scanner.next();
            char player2Symbol = (player1Symbol == 'B') ? 'W' : 'B';
            System.out.println("Player 2, you will be: " + player2Symbol);
            
            player1 = new HumanPlayer(player1Name, player1Symbol);
            player2 = new HumanPlayer(player2Name, player2Symbol);
        }
    }
}

/**
 * Represents the Gomoku game board and handles board-related operations.
 * Uses a 2D char array to represent the game state.
 */
class Board {
    private static final int SIZE = 9; // 9x9 board
    private char[][] grid;
    private char currentTurn; // 'B' or 'W'
    
    public Board() {
        grid = new char[SIZE][SIZE];
        initializeBoard();
        currentTurn = 'B'; // Black goes first
    }
    
    /**
     * Initializes the board with empty spaces
     */
    private void initializeBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                grid[i][j] = '.';
            }
        }
    }
    
    /**
     * Displays the current board state
     */
    public void display() {
        System.out.print("  ");
        for (int i = 0; i < SIZE; i++) {
            System.out.print(i + " ");
        }
        System.out.println();
        
        for (int i = 0; i < SIZE; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < SIZE; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }
    
    /**
     * Places a stone on the board if the move is valid
     * @param row The row index (0-based)
     * @param col The column index (0-based)
     * @param stone The stone to place ('B' or 'W')
     * @return true if move was valid and made, false otherwise
     */
    public boolean placeStone(int row, int col, char stone) {
        if (isValidMove(row, col)) {
            grid[row][col] = stone;
            return true;
        }
        return false;
    }
    
    /**
     * Checks if a move is valid
     */
    private boolean isValidMove(int row, int col) {
        return row >= 0 && row < SIZE && col >= 0 && col < SIZE && grid[row][col] == '.';
    }
    
    /**
     * Checks if the current player has won
     * @return true if five stones are connected
     */
    public boolean checkWin() {
        // Check all directions for five consecutive stones
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (grid[i][j] == currentTurn) {
                    // Check horizontal
                    if (j <= SIZE - 5 && checkDirection(i, j, 0, 1)) return true;
                    // Check vertical
                    if (i <= SIZE - 5 && checkDirection(i, j, 1, 0)) return true;
                    // Check diagonal down-right
                    if (i <= SIZE - 5 && j <= SIZE - 5 && checkDirection(i, j, 1, 1)) return true;
                    // Check diagonal up-right
                    if (i >= 4 && j <= SIZE - 5 && checkDirection(i, j, -1, 1)) return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Helper method to check for five consecutive stones in a direction
     */
    private boolean checkDirection(int row, int col, int rowDir, int colDir) {
        for (int i = 1; i < 5; i++) {
            if (grid[row + i * rowDir][col + i * colDir] != currentTurn) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Checks if the game is a draw
     * @return true if board is full with no winner
     */
    public boolean isDraw() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (grid[i][j] == '.') {
                    return false;
                }
            }
        }
        return !checkWin();
    }
    
    /**
     * Switches the current turn between players
     */
    public void switchTurn() {
        currentTurn = (currentTurn == 'B') ? 'W' : 'B';
    }
    
    // Getters and other utility methods
    public char getCurrentTurn() { return currentTurn; }
    public boolean isGameOver() { return checkWin() || isDraw(); }
    public int getSize() { return SIZE; }
    public char[][] getGrid() { return grid; }
}

/**
 * Abstract base class for all player types (human and AI)
 */
abstract class Player {
    protected String name;
    protected char stone; // 'B' or 'W'
    
    public Player(String name, char stone) {
        this.name = name;
        this.stone = stone;
    }
    
    /**
     * Makes a move on the board
     * @param board The game board
     */
    public abstract void makeMove(Board board);
    
    // Getters
    public String getName() { return name; }
    public char getStone() { return stone; }
}

/**
 * Represents a human player in the Gomoku game
 */
class HumanPlayer extends Player {
    public HumanPlayer(String name, char stone) {
        super(name, stone);
    }
    
    @Override
    public void makeMove(Board board) {
        Scanner scanner = new Scanner(System.in);
        boolean validMove = false;
        
        while (!validMove) {
            System.out.print(name + ", enter your move (row column): ");
            int row = scanner.nextInt();
            int col = scanner.nextInt();
            
            validMove = board.placeStone(row, col, stone);
            if (!validMove) {
                System.out.println("Invalid move. Try again.");
            }
        }
    }
}

/**
 * Represents an AI player that uses the Minimax algorithm to make moves
 */
class AIPlayer extends Player {
    private static final int MAX_DEPTH = 3; // Controls AI difficulty
    
    public AIPlayer(char stone) {
        super("AI", stone);
    }
    
    @Override
    public void makeMove(Board board) {
        System.out.println("AI is thinking...");
        int[] bestMove = findBestMove(board);
        board.placeStone(bestMove[0], bestMove[1], stone);
        System.out.println("AI places at (" + bestMove[0] + ", " + bestMove[1] + ")");
    }
    
    /**
     * Finds the best move using Minimax algorithm
     * @param board The current game board
     * @return int array with [row, col] of best move
     */
    private int[] findBestMove(Board board) {
        int[] bestMove = new int[]{-1, -1};
        int bestValue = Integer.MIN_VALUE;
        
        // Evaluate all possible moves
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                if (board.getGrid()[i][j] == '.') {
                    // Simulate the move
                    board.getGrid()[i][j] = stone;
                    
                    // Calculate move value using Minimax
                    int moveValue = minimax(board, MAX_DEPTH, false);
                    
                    // Undo the move
                    board.getGrid()[i][j] = '.';
                    
                    // Update best move if needed
                    if (moveValue > bestValue) {
                        bestMove[0] = i;
                        bestMove[1] = j;
                        bestValue = moveValue;
                    }
                }
            }
        }
        return bestMove;
    }
    
    /**
     * Minimax algorithm implementation with alpha-beta pruning
     * @param board The game board
     * @param depth Current depth in the game tree
     * @param isMaximizing Whether it's the maximizing player's turn
     * @return The evaluated score of the board state
     */
    private int minimax(Board board, int depth, boolean isMaximizing) {
        // Base cases: terminal state or depth limit reached
        if (board.checkWin()) {
            return isMaximizing ? -1000 : 1000;
        }
        if (board.isDraw() || depth == 0) {
            return evaluateBoard(board);
        }
        
        if (isMaximizing) {
            int bestValue = Integer.MIN_VALUE;
            // Maximizing player's turn (AI)
            for (int i = 0; i < board.getSize(); i++) {
                for (int j = 0; j < board.getSize(); j++) {
                    if (board.getGrid()[i][j] == '.') {
                        board.getGrid()[i][j] = stone;
                        int value = minimax(board, depth - 1, false);
                        board.getGrid()[i][j] = '.';
                        bestValue = Math.max(bestValue, value);
                    }
                }
            }
            return bestValue;
        } else {
            int bestValue = Integer.MAX_VALUE;
            // Minimizing player's turn (human)
            for (int i = 0; i < board.getSize(); i++) {
                for (int j = 0; j < board.getSize(); j++) {
                    if (board.getGrid()[i][j] == '.') {
                        board.getGrid()[i][j] = (stone == 'B') ? 'W' : 'B';
                        int value = minimax(board, depth - 1, true);
                        board.getGrid()[i][j] = '.';
                        bestValue = Math.min(bestValue, value);
                    }
                }
            }
            return bestValue;
        }
    }
    
    /**
     * Evaluates the board state for the AI
     * @param board The game board to evaluate
     * @return A score representing how favorable the position is for the AI
     */
    private int evaluateBoard(Board board) {
        int score = 0;
        char opponent = (stone == 'B') ? 'W' : 'B';
        
        // Evaluate all possible lines of 5 consecutive positions
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                // Check horizontal lines
                if (j <= board.getSize() - 5) {
                    score += evaluateLine(board, i, j, 0, 1);
                }
                // Check vertical lines
                if (i <= board.getSize() - 5) {
                    score += evaluateLine(board, i, j, 1, 0);
                }
                // Check diagonal lines
                if (i <= board.getSize() - 5 && j <= board.getSize() - 5) {
                    score += evaluateLine(board, i, j, 1, 1);
                }
                if (i >= 4 && j <= board.getSize() - 5) {
                    score += evaluateLine(board, i, j, -1, 1);
                }
            }
        }
        return score;
    }
    
    /**
     * Evaluates a line of 5 positions for scoring
     */
    private int evaluateLine(Board board, int row, int col, int rowDir, int colDir) {
        int aiCount = 0;
        int humanCount = 0;
        
        for (int i = 0; i < 5; i++) {
            char cell = board.getGrid()[row + i * rowDir][col + i * colDir];
            if (cell == stone) aiCount++;
            if (cell == ((stone == 'B') ? 'W' : 'B')) humanCount++;
        }
        
        // Score based on how many stones are in the line
        if (aiCount == 5) return 1000;
        if (humanCount == 5) return -1000;
        if (humanCount > 0 && aiCount > 0) return 0; // Blocked line
        
        // Give higher scores for more consecutive stones
        if (aiCount == 4) return 100;
        if (aiCount == 3) return 10;
        if (aiCount == 2) return 5;
        if (aiCount == 1) return 1;
        
        if (humanCount == 4) return -100;
        if (humanCount == 3) return -10;
        if (humanCount == 2) return -5;
        if (humanCount == 1) return -1;
        
        return 0;
    }
}