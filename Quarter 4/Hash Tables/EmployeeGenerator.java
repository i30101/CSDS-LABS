/**
 * @author Andrew Kim
 * @since 25 May 2024
 * @version 1.0.0
 * 
 * Generates employee numbers with random names
 */

import java.io.*;

public class EmployeeGenerator {
    private static int NUM_EMPLOYEES = 10000;
    private static String[] VOWELS = "aeiou".split("");
    private static String[] CONSONANTS = "bcdfghjklmnpqrstvwxyz".split("");

    public static String FILEPATH = "./data/hashData.txt";

    private static String randomLetter(String[] letters) {
        return letters[(int) (Math.random() * letters.length)];
    }

    private static String randomVowel() {
        return randomLetter(VOWELS);
    }

    private static String randomConsonant() {
        return randomLetter(CONSONANTS);
    }

    private static String randomName() {
        String name = "";

        for (int i = 0; i < ( (int) (Math.random() * 6) + 3); i++) {
            if (i % 2 == 0) {
                name += randomConsonant();
            } else {
                name += randomVowel();
            }
        }
        return name;
    }

    public static void main(String[] args) {

        String writeString = "";

        for (int i = 0; i < NUM_EMPLOYEES; i++) {
            writeString += "" + i + " " + randomName() + "\n";
        }

        try {
            FileWriter writer = new FileWriter(FILEPATH);
            
            // trunate file before writing
            writer.flush();

            writer.write(writeString);
            writer.close();
        } catch (IOException e) {
            System.out.println("Error writing file");
        }
        System.out.println("Random employee generation complete");
    }
}
