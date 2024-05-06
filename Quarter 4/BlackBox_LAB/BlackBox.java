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
    private static final char rightMirror = '/';
    private static final char leftMirror = '\\';


    /**
     * Generates random coordinate within size limits
     * Will not give coordinate value of a laser
     * @return single random coordinate value
     */
    private static int randomCoord() {
        return 1 + (int) (Math.random() * (size - 2));
    }


    /**
     * Sees if a square is a mirror
     * @param c the character
     * @return whether the character is a mirror or not
     */
    private static boolean isMirror(char c) {
        return (c == rightMirror) || (c == leftMirror);
    }

    private static void moveUp(int x, int y) {

    }

    private static void moveDown(int x, int y) {

    }

    private static void moveLeft(int x, int y) {

    }

    private static void moveRight(int x, int y) {
        
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

            // randomly decide which way the mirror is pointed and place on board
            board[randX][randY] = (Math.random() > 0.5) ? leftMirror : rightMirror; 
        }
    }


    /**
     * Shows board in terminal
     * @param showMirrors whether mirrors should be printed or not
     */
    public static void printBoard(boolean showMirrors) {
        for (char[] row : board) {
            for (char c : row) {
                if (c != 0 && (showMirrors || !isMirror(c))) {
                    System.out.print(String.valueOf(c));
                } else {
                    System.out.print(empty);
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
