//    d oberle 2021.
import java.io.*;
import java.util.*;

/**The Animal Guessing Program:	d oberle, 2021
 * This game will attempt to guess an animal that the user is thinking of by asking yes/no questions.
 * If the program does not know the animal, it will add it to its data base, making it a little smarter for the next user.
 * Implement an array as a heap.  Each index has a parent at (index/2), a left child as (index*2) and a right child at index*2+1.
 * The index path following a "no" response will go to the left child (left subtree).  
 * The index path following a "yes" response will go to the right child (right subtree).*/

public class animalGuesserShell {

    /** Given the name of a file that exists in the same folder, this counts the number of lines in it. O(n)
     * Helper method for readFile(String fileName).
     * @param fileName the name of a file that exists in the same folder as this file.
     * @throws IOException  in case we are trying to find the size of a file that does not exist.
     * @return  the number of lines in the file. 
     */
    public static int getFileSize(String fileName) throws IOException {
        Scanner input = new Scanner(new FileReader(fileName));
        int size=0;
        while (input.hasNextLine()) {                   //while there is another line in the file
            size++;										//add to the size
            input.nextLine();							//go to the next line in the file
        }
        input.close();									//always close the files when you are done
        return size;
    }
    

    /** Fills and returns an array of Strings where each element is a line from a file that exists in the same folder. O(n)
     * @param fileName  the name of a file that exists in the same folder as this file.
     * @throws IOException  in case we are trying to read from a file that does not exist.
     * @return  a String array of all the elements in filename.txt, with index 0 unused (heap). 
     */
    public static String[] readFile(String fileName) throws IOException {
        int size = getFileSize(fileName);		//holds the # of elements in the file
        String[] list = new String[size];		//a heap will not use index 0;
        Scanner input = new Scanner(new FileReader(fileName));
        int i=0;								//index for placement in the array
        String line;	
        while (input.hasNextLine()) {				//while there is another line in the file
            line=input.nextLine();					//read in the next Line in the file and store it in line
            if(line.equals("null")) {
                line = null;
            }
            list[i]= line;							//add the line into the array
            i++;									//advance the index of the array         
        }
        input.close();	
        return list;					
    }
    

    /** displays all of the elements of the array words for testing. O(n) 
     * @param words  a non-null array of words.
     */     
    public static void showArray(String[] words){
        for (int i=0; i<words.length; i++) 
            System.out.println(words[i] + " ");
        System.out.println();
        System.out.println("Size of array:" + words.length);
    }
    

    /** Writes the contents of an array of Strings out to a file where each array element is on a separate line. O(n)
     * @param array  an array of Strings that you want to write out to a file.
     * @throws IOException  in case we are trying to write to a file that can not be created.  
     * @param filename  the name of a file.
     */   
    public static void writeToFile(String[] array, String filename) throws IOException {
        System.setOut(new PrintStream(new FileOutputStream(filename)));
        for(int i = 0; i < array.length; i++) 
            System.out.println(array[i]);
        System.out.flush();    
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
    }
    

    /** Looks to see if a character is a vowel. O(1) 
     * For use of seeing if the article "a" or "an" needs to come before an animal name.
     * i.e. "Is it an ardvark", or "Is it a narwhal".
     * @param let  the alphabetic character to check; precondition: let is between'A' and 'Z', OR between 'a'  and'z'.
     * @return  true if let is a vowel, false if let is not a vowel. 
     */
    public static boolean isVowel(char let) {
        return (let=='a' || let=='e' || let=='i' || let=='o' || let=='u' || let=='A' || let=='E' || let=='I' || let=='O' || let=='U');
    }
    

    /** Looks to see if a String is likely a "no" type response. O(1)
     * For use to see if a client's response to a question requires moving to the left-subtree.
     * @param ans  a non-null String.
     * @return  returns true if a user prompt is N, No, NO, n, nO. 
     */
    public static boolean isNo(String ans) {
        return (ans.toLowerCase().equals("no") || ans.toLowerCase().equals("n"));
    }
    

    /** Looks to see if a String is likely a "yes" type response. O(1)
     * For use to see if a client's response to a question requires moving to the right-subtree.
     * @param ans  a non-null String.
     * @return  returns true if a user prompt is y, Y, Yes, yes, YES, yES, or yeS 
     */      
    public static boolean isYes(String ans) {
        return (ans.toLowerCase().equals("yes") || ans.toLowerCase().equals("y"));
    }


    

    /** The main-function: here is where you will code the Animal-Guesser game.
     * @param argv  because, you know...we need this.
     * @throws IOException  because we will need to call methods that can throw this exception.  
     */   
    public static void main(String argv[]) throws IOException {
        Scanner scanner = new Scanner(System.in);
        
        // initialize heap
        int questionsLength = getFileSize("./animal.txt");
        String[] questions = readFile("./animal.txt");
        questions[0] = null;

        // guesser control structure
        int index = 1;
        String input = null;
        String newAnimal = null;
        String newQuestion = null;
        while(true) {
            // ask question
            System.out.print("index: " + index + " " + questions[index] + " ");

            // get answer
            input = scanner.nextLine();

            // get child index
            if(isNo(input)) {
                index *= 2;
            }else if(isYes(input)) {
                index = index * 2 + 1;
            }else {     // impromper input, run another instance of while loop
                continue;
            }

            // System.out.println(questions[index]);
            // System.out.println(questions[index] == null);
            if(index > questionsLength || questions[index] == null) {
                if(isNo(input)) {   // user has reached end and guesses did not find animal
                    System.out.println("We couldn't guess your animal!");
                    System.out.print("What was your animal? ");
                    newAnimal = scanner.nextLine();
                    System.out.println("What is a simple yes or no question that would lead someone to your animal if they answered with \"yes\"?");
                    newQuestion = scanner.nextLine();
                    System.out.println("Thanks for your input!");
                    break;
                }else {     // user has guessed input
                    System.out.println("I'm a genius! I guesed your animal!");
                    break;
                }
            }
        }
        scanner.close();

        // add new animal to questions
        String[] newQuestions = null;
        if(newAnimal != null && newQuestion != null) {  // user gave question for a new animal
            // create array of new questions, make bigger if necessary
            int newQuestionsLength = (questionsLength > index * 2) ? questionsLength : index * 2 + 1;
            newQuestions = new String[newQuestionsLength + 1];

            // add exisiting values to questions
            for(int i = 0; i < questionsLength; i++) {
                newQuestions[i] = questions[i];
            }

            // add new question
            newQuestions[index] = newQuestion;

            // add new animal
            newQuestions[index * 2 + 1] = "Is your animal " + (isVowel(input.charAt(0)) ? "an " : "a ") + newAnimal + "?";;
            writeToFile(newQuestions, "./animal.txt");
        }


        // write new questions list into text file
    }
}