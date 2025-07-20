import java.util.Scanner;

public class TicTacToe {
    static char[][] board = new char[3][3];
    static Scanner scanner = new Scanner(System.in);
    static boolean vsAI = false;

    public static void main(String[] args) {
        do {
            initializeBoard();
            printBoard();
            chooseGameMode();
            playGame();
        } while (askReplay());
    }

    static void initializeBoard() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                board[i][j] = ' ';
    }

    static void printBoard() {
        System.out.println("-------------");
        for (int i = 0; i < 3; i++) {
            System.out.print("| ");
            for (int j = 0; j < 3; j++) {
                System.out.print(board[i][j] + " | ");
            }
            System.out.println("\n-------------");
        }
    }

    static void chooseGameMode() {
        System.out.println("Choose game mode:");
        System.out.println("1. Player vs Player");
        System.out.println("2. Player vs AI");
        int choice;
        while (true) {
            System.out.print("Enter 1 or 2: ");
            choice = scanner.nextInt();
            if (choice == 1 || choice == 2) break;
            System.out.println("Invalid choice. Try again.");
        }
        vsAI = (choice == 2);
    }

    static void playGame() {
        char currentPlayer = 'X';
        boolean gameEnded = false;

        while (!gameEnded) {
            if (vsAI && currentPlayer == 'O') {
                System.out.println("AI is making a move...");
                int[] bestMove = findBestMove();
                board[bestMove[0]][bestMove[1]] = 'O';
            } else {
                System.out.println("Player " + currentPlayer + ", enter your row and column (0, 1, or 2): ");
                int row = scanner.nextInt();
                int col = scanner.nextInt();

                if (row < 0 || col < 0 || row > 2 || col > 2 || board[row][col] != ' ') {
                    System.out.println("Invalid move. Try again.");
                    continue;
                }
                board[row][col] = currentPlayer;
            }

            printBoard();

            if (checkWin(currentPlayer)) {
                System.out.println("Player " + currentPlayer + " wins!");
                gameEnded = true;
            } else if (isDraw()) {
                System.out.println("It's a draw!");
                gameEnded = true;
            } else {
                currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
            }
        }
    }

    static boolean checkWin(char player) {
        for (int i = 0; i < 3; i++)
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) return true;
        for (int j = 0; j < 3; j++)
            if (board[0][j] == player && board[1][j] == player && board[2][j] == player) return true;
        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) return true;
        if (board[0][2] == player && board[1][1] == player && board[2][0] == player) return true;
        return false;
    }

    static boolean isDraw() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (board[i][j] == ' ') return false;
        return true;
    }

    static boolean askReplay() {
        System.out.print("Do you want to play again? (y/n): ");
        String input = scanner.next();
        return input.equalsIgnoreCase("y");
    }

    // AI logic (Minimax)
    static int evaluate() {
        if (checkWin('O')) return +10;
        if (checkWin('X')) return -10;
        return 0;
    }

    static int minimax(int depth, boolean isMax) {
        int score = evaluate();
        if (score == 10 || score == -10) return score;
        if (isDraw()) return 0;

        if (isMax) {
            int best = -1000;
            for (int i = 0; i < 3; i++)
                for (int j = 0; j < 3; j++)
                    if (board[i][j] == ' ') {
                        board[i][j] = 'O';
                        best = Math.max(best, minimax(depth + 1, false));
                        board[i][j] = ' ';
                    }
            return best;
        } else {
            int best = 1000;
            for (int i = 0; i < 3; i++)
                for (int j = 0; j < 3; j++)
                    if (board[i][j] == ' ') {
                        board[i][j] = 'X';
                        best = Math.min(best, minimax(depth + 1, true));
                        board[i][j] = ' ';
                    }
            return best;
        }
    }

    static int[] findBestMove() {
        int bestVal = -1000;
        int[] bestMove = {-1, -1};

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (board[i][j] == ' ') {
                    board[i][j] = 'O';
                    int moveVal = minimax(0, false);
                    board[i][j] = ' ';

                    if (moveVal > bestVal) {
                        bestMove[0] = i;
                        bestMove[1] = j;
                        bestVal = moveVal;
                    }
                }

        return bestMove;
    }
}
