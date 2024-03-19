import java.util.*;
import java.io.*;

public class Score
{
   private long time;      //amount of time the client took to solve the puzzle
   private int difficulty; //difficulty level of the puzzle: BEGINNER = 3, ADVANCED = 2, EXPERT = 1, CLASSIC = 0
   private int size;       //size of the puzzle: SMALL = 0, MEDIUM = 1, LARGE = 2
   private String initials;//initials of the client that got the high schore
   
   //pre: t>=0, d>=0 and d<GamePanel.NUMDIFFICULTIES, s>=0 and s<GamePanel.NUMSIZES, i!=null and i.length()==3
   public Score(long t, int d, int s, String i)
   {
      time = t;
      difficulty = d;
      size = s;
      initials = i;
   }
   
   public long getTime()
   {
       return time;
   }
   
   public String toString()
   {
      return time+" "+difficulty+" "+size+" "+initials;
   }
   
  //pre:  fileName is the name of a valid file in the data folder
  //post: populates and returns a List of Score objects read in from the file
  //      if there is a problem, returns a set of cleaned scores
   public static ArrayList<Score> readHighScores(String fileName)
   {
      ArrayList<Score> list = new ArrayList();
      try
      {
         java.util.Scanner input = new java.util.Scanner(new FileReader(fileName));
         while (input.hasNextLine())		//while there is another value in the file
         {
            try
            {
               String line = input.nextLine();
               String [] parts = line.split(" ");
               if(parts.length != 4)
               {
                  System.out.println("File is corrupt");
                  return null;
               }
               int t = Integer.parseInt(parts[0].trim());
               int d = Integer.parseInt(parts[1].trim());
               int s = Integer.parseInt(parts[2].trim());
               String i = parts[3].trim();
               list.add(new Score(t, d, s, i));
            }
            catch (java.util.InputMismatchException ex1)			//file is corrupted or doesn't exist
            {
               System.out.println("File is corrupt");
               return null;  
            }			
            catch (java.util.NoSuchElementException ex2)			//file is corrupted or doesn't exist
            {
               System.out.println("File is corrupt");
               return null;    
            }	
         }
         input.close();	
         return list;
      }
      catch (IOException ex3)			//file is corrupted or doesn't exist - clear high scores and remake the file
      {
         System.out.println("File does not exist");
         return null;
      }		
   }
   
    //pre:  fileName is the name of the file containting high-scores in a data subfolder
    //post: Writes the new high-scores out to the file in the case that they have been changed
   public static void writeToFile(String fileName, ArrayList<Score> list)
   {
      PrintStream imageWriter = null;
      File imageFile = new File(fileName);
      if(imageFile.exists())		//make sure to write over old file
      {
         imageFile.delete();
         imageFile = new File(fileName);
      }         
      while(imageWriter == null){
         try{
            imageWriter = new PrintStream(new FileOutputStream(imageFile, true));
         }
         catch(Exception E){
            System.exit(42);
         }
      }
      for(Score score: list)
      {
         imageWriter.println(score);
      }
      imageWriter.close();
   }

   //returns a new list of empty scores
   public static ArrayList<Score> generateBlankScores()
   {
      ArrayList<Score> list = new ArrayList();
      for(int d = 0; d < GamePanel.NUMDIFFICULTIES; d++)
      {
         for(int s = 0; s < GamePanel.NUMSIZES; s++)
         {
            list.add(new Score(0, d, s, "AAA"));
         }
      }
      return list;
   }
   
   //returns the score in the high-scores list with a certain difficulty and size
   //returns a blank score if no such score can be found
   public static Score findHighScore(int d, int s, ArrayList<Score> list)
   {
      for(Score score: list)
      {
         if(score.difficulty == d && score.size == s)
         {
            return score;
         }
      }
      return new Score(0, d, s, "AAA");
   }
   
   //updates the high-score list with a new high-score
   //returns true of successful
   public static boolean updateHighScore(long t, int d, int s, ArrayList<Score> list)
   {
      for(int i = 0; i < list.size(); i++)
      {
         Score score = list.get(i);
         if(score.difficulty == d && score.size == s && (score.time==0 || t < score.time))
         {
            list.set(i, new Score(t, d, s, "AAA"));
            return true;
         }
      }
      return false;
   }
}