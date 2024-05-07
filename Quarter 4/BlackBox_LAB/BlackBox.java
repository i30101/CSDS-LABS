/**
 * @author Andrew Kim
 * @version 1.0.0
 * @since 5 May 2024
 * 
 * BlackBox driver class
 */

 import java.util.*;

public class BlackBox {
    public static char[][] board;

    // board parameters
    private static final int SIZE = 12;
    private static final int NUM_MIRRORS = 10;

    // characters for printing board
    public static final String SPACE = " ";
    public static final char EMPTY = '.';
    public static final char RIGHT_MIRROR = '/';
    public static final char LEFT_MIRROR = '\\';

    // coordinates of mirrors that should be shown
    private static ArrayList<Integer> visibleRowCoords = new ArrayList<Integer>();
    private static ArrayList<Integer> visibleColCoords = new ArrayList<Integer>();


    /**
     * Generates random coordinate within size limits
     * Will not give coordinate value of a laser
     * @return single random coordinate value
     */
    private static int randomCoord() {
        return 1 + (int) (Math.random() * (SIZE - 2));
    }


    /**
     * Sees if square is a mirror
     * @param x x-coordinate in question
     * @param y y-coordinate in question
     * @return whether the point in question is a mirror or not
     */
    private static boolean isMirror(int x, int y) {
        return board[x][y] == RIGHT_MIRROR || board[x][y] == LEFT_MIRROR;
    }


    /**
     * Reords that the author has found a mirror
     * @param x x-coordinate of mirror that was found
     * @param y y-coordinate of mirror that was found
     * @return whether the mirror 
     */
    private static boolean foundMirror(int x, int y) {
        if (wasFound(x, y)) {
            return false;
        }
        // store coordinates of visible mirrors
        visibleRowCoords.add(x);
        visibleColCoords.add(y);
        return true;
    }

    
    /**
     * Whether the mirror has been found or not
     * @param row x-coordinate of mirror
     * @param col y-coordinate of mirror
     * @return whether the mirror has been found
     */
    private static boolean wasFound(int row, int col) {
        return visibleRowCoords.contains(row) && visibleColCoords.contains(col);
    }


    /**
     * Traverses board upward for given position
     * @param startRow row where traversal starts
     * @param startCol remains the same
     * @return eventual target of the laser
     */
    private static char moveUp(int startRow, int startCol) {
        for (int r = startRow - 1; r > 0; r--) {
            if (board[r][startCol] == RIGHT_MIRROR) {
                return moveRight(r, startCol);
            } else if (board[r][startCol] == LEFT_MIRROR) {
                return moveLeft(r, startCol);
            }
        }
        return board[0][startCol];
    }


    /**
     * Traverses board downward for given position
     * @param startRow row where traversal starts
     * @param startCol remains the same
     * @return eventual target of the laser
     */
    private static char moveDown(int startRow, int startCol) {
        for (int r = startRow + 1; r < SIZE - 1; r++) {
            if (board[r][startCol] == RIGHT_MIRROR) {
                return moveLeft(r, startCol);
            } else if (board[r][startCol] == LEFT_MIRROR) {
                return moveRight(r, startCol);
            }
        }
        return board[SIZE - 1][startCol];
    }


    /**
     * Traverses board to the left for given position
     * @param startRow remains the same
     * @param startCol column where traversal starts
     * @return eventual target of the laser
     */
    private static char moveLeft(int startRow, int startCol) {
        for (int c = startCol - 1; c > 0; c--) {
            if (board[startRow][c] == RIGHT_MIRROR) {
                return moveDown(startRow, c);
            } else if (board[startRow][c] == LEFT_MIRROR) {
                return moveUp(startRow, c);
            }
        }
        return board[startRow][0];
    }


    /**
     * Traverses board to the left for the given position
     * @param startRow remains the same
     * @param startCol column where traversal starts
     * @return eventual target of the laser
     */
    private static char moveRight(int startRow, int startCol) {
        for (int c = startCol + 1; c < SIZE - 1; c++) {
            if (board[startRow][c] == RIGHT_MIRROR) {
                return moveUp(startRow, c);
            } else if (board[startRow][c] == LEFT_MIRROR) {
                return moveDown(startRow, c);
            }
        }
        return board[startRow][SIZE - 1];
    }


    /**
     * Initializes Black Box board
     * Sets lasers to letters
     * Leaves blanks spaces null
     * Adds mirrors as slashes or backslashes
     */
    public static void initializeBoard() {
        // create 2D array for board
        board = new char[SIZE][SIZE];

        // iterate through board
        for (int i = 0; i < SIZE - 2; i++) {
            // place top laser
            board[0][i + 1] = (char) ('a' + i);

            // place bottom laser
            board[SIZE - 1][i + 1] = (char) ('A' + i);

            // place left laser
            board[i + 1][0] = (char) ('k' + i);

            // place right laser
            board[i + 1][SIZE - 1] = (char) ('K' + i);
        }

        // assign mirrors
        for (int i = 0; i < NUM_MIRRORS; i++) {
            int randRow;
            int randCol;

            // get mirror coordinates that haven't already been occupied
            do {
                randRow = randomCoord();
                randCol = randomCoord();
            } while (board[randRow][randCol] != 0);

            // randomly decide which way the mirror is pointed and place on board
            board[randRow][randCol] = (Math.random() > 0.5) ? LEFT_MIRROR : RIGHT_MIRROR;
        }
    }


    /**
     * Shows board in terminal
     * @param showMirrors whether mirrors should be printed or not
     * If showMirrors is false, boards that were found are still printed
     */
    public static void printBoard(boolean showMirrors) {
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[0].length; c++) {
                if (board[r][c] != 0 && (showMirrors || !isMirror(r, c) || wasFound(r, c))) {
                    System.out.print(String.valueOf(board[r][c]));
                } else {
                    System.out.print(EMPTY);
                }
                System.out.print(SPACE);
            }
            System.out.println();
        }
        System.out.println();
    }


    /**
     * Clears terminal
     */
    private static void clear() {
        System.out.print("\033\143");
    }


    public static void main(String[] args) {
        clear();
        System.out.println("\\**************** Welcome to BLACK BOX! ****************/\n");
        initializeBoard();
        // printBoard(false);
        printBoard(true);
        for (int col = 1; col < SIZE - 1; col++) {
            System.out.println("firing from: " + board[SIZE - 1][col]);
            System.out.println("hit laser: " + moveUp(SIZE - 1, col));
        }
    }
}
