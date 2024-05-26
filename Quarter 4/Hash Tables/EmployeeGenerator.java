/**
 * @author Andrew Kim
 * @since 25 May 2024
 * @version 1.0.0
 * 
 * Generates employee numbers with random names
 */

import java.util.*;
import java.io.*;

public class EmployeeGenerator {
    private static int NUM_EMPLOYEES = 10000;
    private static String[] VOWELS = "aeiou".split("");
    private static String[] CONSONANTS = "bcdfghjklmnpqrstvwxyz".split("");

    private static String WRITE_PATH = "./data/hashData.txt";

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
        String name = randomConsonant();

        for (int i = 0; i < ( (int) (Math.random() * 7) + 2); i++) {
            if (i % 2 == 0) {
                name += randomVowel();
            } else {
                name += randomConsonant();
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
            FileWriter writer = new FileWriter(WRITE_PATH);
            
            // trunate file before writing
            writer.flush();

            writer.write(writeString);
            writer.close();
        } catch (IOException e) {
            System.out.println("Error writing file");
        }
    }
}
