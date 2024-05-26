//    d oberle 2021.
import java.io.*;
import java.util.*;
import java.util.HashMap;
import java.util.Scanner;
 

public class MorseCodeDriver {
    // English keys and Morse values
    public static HashMap<String, String> englishMorse;

    // Morse keys and English values
    public static HashMap<String, String> morseEnglish;


    /**
     * Translates English to Morse
     * @param english plaintext
     * @return ciphertext
     * @throws IOException character does not have corresponding Morse character
     */
    public static String englishToMorse(String english) throws IOException {
        // split text into words
        String[] englishWords = english.toUpperCase().split(" ");
        
        // output morse
        String morse = "";

        // number of words
        int numWords = englishWords.length;
        
        // go through words
        for (int i = 0; i < numWords; i++) {
            // go through letters
            for (String letter : englishWords[i].split("")) {
                // check if letter has a corresponding Morse code
                if (englishMorse.containsKey(letter)) {
                    morse += englishMorse.get(letter) + " ";
                }else {
                    throw new IOException("Character does not exist in Morse");
                }
            }

            // mark end of word if not at last word
            if (i < numWords - 1) {
                morse += "/ ";

            }
        }
        return morse;
    }


    /**
     * Translates Morse to English
     * @param morse ciphertext
     * @return plaintext
     * @throws IOException code combination does not have corresponding English character
     */
    public static String morseToEnglish(String morse) throws IOException {
        // split text into words
        String[] morseWords = morse.split(" / ");

        // output english
        String english = "";

        // number of words
        int numWords = morseWords.length;

        // go through words
        for (int i = 0; i < numWords; i++) {
            // go through letters
            for (String code : morseWords[i].split(" ")) {
                // check of letter has a corresponding Morse code
                if (morseEnglish.containsKey(code)) {
                    english += morseEnglish.get(code);
                }else {
                    throw new IOException("Code does not exist in English");
                }
            }

            // mark end of word if not last word
            if (i < numWords) {
                english += " ";
            }
        }
        return english;
    }


    public static void main(String[] arg) throws IOException {
        
        // initialize HashMaps
        englishMorse = new HashMap<String, String>();
        morseEnglish = new HashMap<String, String>();

        // add values to HashMaps
        for(int i = 0; i < MorseCode.english.length; i++) {
            String english = MorseCode.english[i];
            String morse = MorseCode.code[i];
            englishMorse.put(english, morse);
            morseEnglish.put(morse, english);
        }
        
        // control structure
        System.out.println("Welcome to Andrew\'s English-Morse translator!");
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.print(
                "\n" + 
                "(1): Translate English to Morse\n" + 
                "(2): Translate Morse to English\n" + 
                "(0): Exit program\n" + 
                "Enter your choice: "
            );
            String input = scanner.nextLine();
            switch(input) {
                case "1":   // user wants to encode English into Morse
                    System.out.print("Enter the name of your file: ");
                    String englishFile = scanner.nextLine();
                    try {
                        File file = new File(englishFile);
                        Scanner englishReader = new Scanner(file);
                        System.out.println(englishReader.nextLine());
                        while (englishReader.hasNextLine()) {
                            String data = englishReader.nextLine();
                            // System.out.println(data);
                            System.out.println("English encoded into Morse: " + englishToMorse(data));
                        }
                        englishReader.close();
                    } catch (FileNotFoundException e) {
                        System.out.println("Could not find your file.");
                        e.printStackTrace();
                    }
                    break;
                case "2":   // user wants to encode Morse into English
                    System.out.print("Enter the name of your file: ");
                    String morseFile = scanner.nextLine();
                    try {
                        File file = new File(morseFile);
                        Scanner morseReader = new Scanner(file);
                        while (morseReader.hasNextLine()) {
                            String data = morseReader.nextLine();
                            // System.out.println(data);
                            System.out.println("Morse decoded into English: " + morseToEnglish(data));
                        }
                        morseReader.close();
                    } catch (FileNotFoundException e) {
                        System.out.println("Could not find yoru file.");
                        e.printStackTrace();
                    }
                    break;
                case "0":
                    System.out.println("Goodbye.");
                    running = false;
                    break;
                default:
                    System.out.println("Input not recognized. Please try again.");
                    continue;
            }
        }

        scanner.close();
    }
}
