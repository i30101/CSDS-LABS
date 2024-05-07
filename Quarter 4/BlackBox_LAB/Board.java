/**
 * @author Andrew Kim
 * @version 2.0.0
 * @since 5 May 2024
 * 
 * Black Box Board class
 */

 import java.util.*;

public class Board {
    private char[][] board;

    // board parameters
    private int size;
    private int numMirrors;

    // characters for printing board
    private static final String SPACE = " ";
    private static final char EMPTY = '.';
    private static final char RIGHT_MIRROR = '/';
    private static final char LEFT_MIRROR = '\\';

    // coordinates of mirrors that should be shown
    private ArrayList<Integer> visibleRowCoords = new ArrayList<Integer>();
    private ArrayList<Integer> visibleColCoords = new ArrayList<Integer>();


    /**
     * Creates custom Board
     * @param s the size of the board (s by s)
     * @param n the number of mirrors
     */
    public Board(int s, int n) {
        size = s;
        numMirrors = n;
        initializeBoard();
    }


    /*
     * Default constructor for Board object
     */
    public Board() {
        this(12, 10);
    }

    /**
     * Generates random coordinate within size limits
     * Will not give coordinate value of a laser
     * @return single random coordinate value
     */
    private int randomCoord() {
        return 1 + (int) (Math.random() * (size - 2));
    }


    /**
     * Sees if square is a mirror
     * @param x x-coordinate in question
     * @param y y-coordinate in question
     * @return whether the point in question is a mirror or not
     */
    public boolean isMirror(int x, int y) {
        return board[x][y] == RIGHT_MIRROR || board[x][y] == LEFT_MIRROR;
    }


    /**
     * Reords that the author has found a mirror
     * @param row x-coordinate of mirror that was found
     * @param col y-coordinate of mirror that was found
     * @return whether the mirror 
     */
    public void foundMirror(int row, int col) {
        // store coordinates of visible mirrors
        if (!wasFound(row, col)) {
            visibleRowCoords.add(row);
            visibleColCoords.add(col);
        }
    }

    
    /**
     * Whether the mirror has been found or not
     * @param row x-coordinate of mirror
     * @param col y-coordinate of mirror
     * @return whether the mirror has been found
     */
    private boolean wasFound(int row, int col) {
        return visibleRowCoords.contains(row) && visibleColCoords.contains(col);
    }


    /**
     * Traverses board upward for given position
     * @param startRow row where traversal starts
     * @param startCol remains the same
     * @return eventual target of the laser
     */
    public char moveUp(int startRow, int startCol) {
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
    public char moveDown(int startRow, int startCol) {
        for (int r = startRow + 1; r < size - 1; r++) {
            if (board[r][startCol] == RIGHT_MIRROR) {
                return moveLeft(r, startCol);
            } else if (board[r][startCol] == LEFT_MIRROR) {
                return moveRight(r, startCol);
            }
        }
        return board[size - 1][startCol];
    }


    /**
     * Traverses board to the left for given position
     * @param startRow remains the same
     * @param startCol column where traversal starts
     * @return eventual target of the laser
     */
    public char moveLeft(int startRow, int startCol) {
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
    public char moveRight(int startRow, int startCol) {
        for (int c = startCol + 1; c < size - 1; c++) {
            if (board[startRow][c] == RIGHT_MIRROR) {
                return moveUp(startRow, c);
            } else if (board[startRow][c] == LEFT_MIRROR) {
                return moveDown(startRow, c);
            }
        }
        return board[startRow][size - 1];
    }


    /**
     * Initializes Black Box board
     * Sets lasers to letters
     * Leaves blanks spaces null
     * Adds mirrors as slashes or backslashes
     */
    private void initializeBoard() {
        // create 2D array for board
        board = new char[size][size];

        // iterate through board
        for (int i = 0; i < size - 2; i++) {
            // place top laser
            board[0][i + 1] = (char) ('a' + i);

            // place bottom laser
            board[size - 1][i + 1] = (char) ('A' + i);

            // place left laser
            board[i + 1][0] = (char) ('k' + i);

            // place right laser
            board[i + 1][size - 1] = (char) ('K' + i);
        }

        // assign mirrors
        for (int i = 0; i < numMirrors; i++) {
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
    public void print(boolean showMirrors) {
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
     * Gets character at desired coordinates
     * @param row desired row
     * @param col desired column
     * @return the character at row, col
     */
    public char get(int row, int col) {
        return board[row][col];
    }
}
