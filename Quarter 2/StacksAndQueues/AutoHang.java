/**
 * @author Andrew Kim
 * @version 1.0.0
 * @since 5 November 2023
 * Reverse Auto Hangman game
 */


// import external libraries
import java.util.*;


public class AutoHang {
    // stack for letters of word
    public static MyStack<String> word;
    
    // counter for current word
    public static int i;

    // total score
    public static int score;

    // scanner to get user input
    public static Scanner scanner;



    /**
     * Converts word to stack of letters
     * @param word the word to be turned into a stack
     * @return the word converted into a stack, first letter at bottom
     */
    public static MyStack<String> toStack(String word) {
        MyStack<String> newStack = new MyStack<String>();
        for(int i = 0; i < word.length(); i++) {
            newStack.push("" + word.charAt(i));;
        }
        return newStack;
    }


    /**
     * Gets 1-letter guess from user
     * @param oneLetter one-letter requirement
     * @param query what the input is used for
     * @return single letter user guess
     */
    public static String getUserInput(boolean oneLetter, String query) {
        while(true) {
            System.out.print(query);
            String input = scanner.nextLine().toLowerCase();
            System.out.println();
            // input string is one character and contains alphabetical characters only
            if (input.matches("[a-zA-Z]+")) {
                if (oneLetter) {
                    if(input.length() == 1) {
                        return input;
                    }
                } else {
                    return input;
                }
            }
            System.out.println("Invalid input, please try again.");
        }
    }


    /**
     * Clears output terminal
     */
    public static void clearTerminal() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }


    /**
     * Delays program by 1 second
     * @param seconds delay of program in seconds
     */
    public static void delay(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
    }
    

    public static void main(String[] args) {
        scanner = new Scanner(System.in);

        score = 0;
        clearTerminal();
        
        System.out.println("Welcome to Automatic Reverse Hangman!");

        // get Hangman word
        String originalWord = getUserInput(false, "Player 1, enter your word: ");
        word = toStack(originalWord);
        i = word.size() - 2;

        // get theme
        String theme = getUserInput(false, "Player 1, enter the theme of your word: ");
        
        // clear terminal
        clearTerminal();
        System.out.println("Word saved, terminal cleared!");

        delay(3);
        clearTerminal();
        
        String guess = "";
        String guessed = word.pop();
        while (word.size()> 0) {
            System.out.println("The theme is: " + theme);
            System.out.println("Your score: " + score + "\n");
            
            // print out word
            for (int j = 0; j < i + 1; j++) {
                System.out.print ("_ ");
            }
            System.out.println(guessed + "\n");
            
            guess = getUserInput(true, "Enter your 1-letter guess: ");
                        
            // control structure for guess checking
            int newPoints = 0;
            if (guess.equals(word.peek())) {
                newPoints = i + 1;
                score += newPoints;
                System.out.println("Congrats! You get " + newPoints + " points.");
                i--;
                
                word.pop();
                guessed = guess + " " + guessed;
            } else {
                newPoints = originalWord.length() - i - 1;
                score -= newPoints;
                System.out.println("Drats! your guess was wrong. Your score decreased by " + newPoints + ".");
            }

            delay(3);
            clearTerminal();
        }
        System.out.println("Wonderful! You successfully guessed the word: " + originalWord + ".");
        System.out.println("Your final score was: " + score);
        scanner.close();
    }
}
