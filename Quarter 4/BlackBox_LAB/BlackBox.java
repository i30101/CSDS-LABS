/**
 * @version 1.0.0
 * @author Andrew Kim
 * @since 5 May 2024
 * 
 * BlackBox driver classs
 */

public class BlackBox {
    public static char[][] board;

    private static final int size = 12;
    private static final int numMirrors = 10;

    private static final String space = " ";
    private static final String empty = ".";


    /**
     * Generates random coordinate within size limits
     * Will not give coordinate value of a laser
     * @return single random coordinate value
     */
    public static int randomCoord() {
        return 1 + (int) (Math.random() * (size - 2));
    }

    /**
     * Initializes Black Box board
     * Sets lasers to letters
     * Leaves blanks spaces null
     * Adds mirrors as slashes or backslashes
     */
    public static void initializeBoard() {
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
            int randX;
            int randY;

            // get mirror coordinates that haven't already been occupied
            do {
                randX = randomCoord();
                randY = randomCoord();
            } while (board[randX][randY] != 0);

            // randomly decide which way the mirror is pointed
            // and place in board
            board[randX][randY] = (Math.random() > 0.5) ? '/' : '\\'; 
        }

    }


    public static void printBoard(boolean showMirrors) {
        for (char[] row : board) {
            for (char c : row) {
                if (c == 0) {
                    System.out.print(empty);
                } else {
                    System.out.print(String.valueOf(c));
                }
                System.out.print(space);
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        initializeBoard();
        printBoard(true);

        // create first row
        System.out.println(String.valueOf(board[0][0]));
    }
}
