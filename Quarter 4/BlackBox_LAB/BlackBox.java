/**
 * @author Andrew Kim
 * @version 2.0.0
 * @since 7 May 2025
 * 
 * Black Box driver class
 */

import java.util.*;

public class BlackBox {
    public static Board board;

    public static Scanner scanner;

    private static final int SIZE = 12;
    private static final int NUM_MIRRORS = 10;
    private static int numShots = 25;
    private static int numGuesses = 20;

    private static ArrayList<String> knownPaths;

    private static int numFound = 0;

    /**
     * Clears terminal
     */
    private static void clear() {
        System.out.print("\033\143");
    }


    /**
     * Gets coordinate guess of user
     * @param message terminal query
     * @return a valid coordinate value within board dimensions
     */
    private static int getGuess(String message) {
        int guess = 0;
        String input;
        while (guess == 0) {
            System.out.print(message);
            input = scanner.nextLine();
            try {
                int tempGuess = Integer.parseInt(input);
                if (tempGuess > 0 && tempGuess < SIZE - 1) {
                    guess = tempGuess;
                } else {
                    System.out.println("Guess out of range, please try again.");
                }
            } catch (Exception e) {
                System.out.println("\nInvalid input, please try again.");
            }
        }
        return guess;
    }


    /**
     * User chooses whether to shoot or guess
     * @return if the user will shoot
     */
    private static boolean willShoot() {
        while (true) {
            System.out.print("Type (f) to fire a laser and (g) to take a guess: ");
            String input = scanner.nextLine().toLowerCase();
            if (input.equals("f")) {
                return true;
            } else if (input.equals("g")) {
                return false;
            } else {
                System.out.println("Invalid input, please try again.");
            }
        }
    }

    
    /**
     * Gets input from the user and shoots a laser
     * @return the laser where the shot lands
     */
    private static char shootLaser() {
        while (true) {
            System.out.print("Enter the letter of the laser you want to fire: ");
            String input = scanner.nextLine();
            try {
                char laser = input.charAt(0);
                char landed = '.';
                if (laser >= 'a' && laser < 'a' + SIZE - 2) {
                    landed = board.moveDown(0, laser - 'a' + 1);
                } else if (laser >= 'A' && laser < 'A' + SIZE - 2) {
                    landed = board.moveUp(SIZE - 1, laser - 'A' + 1);
                } else if (laser >= 'k' && laser < 'k' + SIZE - 2) {
                    landed = board.moveRight(laser - 'k' + 1, 0);
                } else if (laser >= 'K' && laser < 'K' + SIZE - 2) {
                    landed = board.moveLeft(laser - 'K' + 1, SIZE - 1);
                } else {
                    System.out.println("Invalid laser, please try again.");
                }
                knownPaths.add(laser + " -> " + landed);
                return landed;
            } catch (Exception e) {
                System.out.println("Invalid input, please try again.");
            }
        }
    }


    public static void main(String[] args) {
        board = new Board(SIZE, NUM_MIRRORS);
        scanner = new Scanner(System.in);

        knownPaths = new ArrayList<String>();

        clear();

        System.out.println();

        System.out.print(
            "\\******** Welcome to BLACK BOX! ********/\n\n" + 
            "Instructions:" + 
            "\n - There are " + NUM_MIRRORS + " mirrors placed around the " + SIZE + " by " + SIZE + " board." + 
            "\n - Each letter on the board is a laser." + 
            "\n - You can shoot a laser to see which laser it lands on." +
            "\n - You have " + numShots + " shots to see how lasers bounce around the board." + 
            "\n - You have " + numGuesses + " guesses to find the location of the mirrors." + 
            "\n - If you find all the mirrors, you win. Best of luck!\n" + 
            "\nPress enter to begin. "
        );
        // game starts when user sends input
        String start = scanner.nextLine();
        System.out.println(start);
        
        clear();
        while (numFound < NUM_MIRRORS && (numShots > 0 && numGuesses > 0)) {
            clear();
            board.print(false);
            System.out.println("Mirrors found: " + numFound + "/" + NUM_MIRRORS);
            System.out.println("Shots left: " + numShots + "/25");
            System.out.println("Guesses left: " + numGuesses + "/20\n");
            System.out.println("Known paths:");
            for (String knownPath : knownPaths) {
                System.out.println(knownPath);
            }
            System.out.println();
            if (willShoot()) {
                System.out.println("Laser landed on: " + shootLaser());
                numShots--;
            } else {
                int row = getGuess("Row # of guess: ");
                int col = getGuess("Column # of guess: ");
                if (board.isMirror(row, col)) {
                    System.out.println("You guessed correctly!");
                    board.foundMirror(row, col);
                    numFound++;
                } else {
                    System.out.println("Guess again! No mirror there.");
                }
                numGuesses--;
            }
            System.out.print("Press enter to continue. ");
            start = scanner.nextLine();
        }
        if (numFound == NUM_MIRRORS) {
            System.out.println("Congrats. You did it!");
        } else {
            System.out.println("Better luck next time... Here's the board: ");
            board.print(true);
        }
    }
}
