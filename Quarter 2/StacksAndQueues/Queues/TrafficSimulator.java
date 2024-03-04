/**
 * @author Andrew Kim
 * @version 1.0.0
 * @since 6 November 2023
 * Traffic Simulator for Peiod 2 Data Structures
 */


// import external libraries
import java.util.*;


public class TrafficSimulator {
    // Main street queue
    private static MyQueue main;
    private static int mainDelay;
    private static double mainProb;


    // Maple street queue
    private static MyQueue maple;
    private static int mapleDelay;
    private static double mapleProb;

    private static int numCycles;

    /**
     * Generates a random uppercase or lowercase letter
     * @return random letter
     */
    public static String randLetter() {
        int num = (int) (Math.random() * 26);
        if (Math.random() < 0.5) {
        return "" + (char) ('a' + num);
        }
        return "" + (char) ('A' + num);
    }

    public static void main(String[] args) {
        mainDelay = 10;
        mainProb = 0.6;
        mapleDelay = 12;
        mapleProb = 0.4;
        numCycles = 3;
        int numCars = 0;
        main = new MyQueue<String>();
        maple = new MyQueue<String>();

        for (int i = 0; i < numCycles; i++) {
            for (int j = 0; j < mainDelay; j++) {
                System.out.println("Main street: " + main);
                System.out.println("Maple street: " + maple);

                // car might enter two streets
                if (Math.random() < mainProb) {
                    main.add(randLetter());
                }
                if (Math.random() < mapleProb) {
                    maple.add(randLetter());
                }
                if (!main.isEmpty()) {
                    main.remove();
                    numCars++;
                }
            }

            // Main red, Maple green
            for (int j = 0; j < mapleDelay; j++) {
                // show contents of Main and Maple
                System.out.println("Main street: " + main);
                System.out.println("Maple street: " + maple);

                // car might enter two streets
                if (Math.random() < mainProb) {
                    main.add(randLetter());
                }
                if (Math.random() < mapleProb) {
                    maple.add(randLetter());
                }
                if (!maple.isEmpty()) {
                    maple.remove();
                    numCars++;
                }
            }
        }

        System.out.println("Number of cars that made it through: " + numCars);
    }
}
