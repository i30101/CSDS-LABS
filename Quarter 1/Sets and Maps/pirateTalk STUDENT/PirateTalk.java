//    d oberle 2021.
import java.io.*;
import java.util.*;
 
public class PirateTalk
{
      
   private Map<String, String> englishToPirate;		


   public PirateTalk()
   {
      englishToPirate = new HashMap();	//O(1) to retrieve a pirate word from an English word
   }
   
	//pre:  there exists files "english.txt" and "pirate.txt", both of the same size
	//			and whose corresponding elements match pirate words to english
	//post: fill up the Map englishToPirate where the english word is the key and the
	//			corresponding pirate word is the value
   public void initialize() throws IOException
   {
      int numWords = filesScanner.getFileSize("english.txt");
   	//assume that pirate.txt has the same number of words
      String[] englishWords = new String[numWords];
      String[] pirateWords = new String[numWords];
      filesScanner.readFile(englishWords, "english.txt");
      filesScanner.readFile(pirateWords, "pirate.txt");
      for(int i=0; i<numWords; i++)
         englishToPirate.put(englishWords[i], pirateWords[i]);
   }
   
	//shows all current words in the database
   public String toString()
   {
      String ans = "";
      // Iterator it=englishToPirate.keySet().iterator();
      // while(it.hasNext())
      // {
         // String englishWord = ""+it.next();
         // String pirateWord = ""+englishToPirate.get(englishWord);
         // ans+=englishWord+":"+pirateWord+"\n";	
      // }
      for(String englishWord: englishToPirate.keySet())
      {  
         String pirateWord = englishToPirate.get(englishWord);
         ans += (englishWord + ":" + pirateWord+"\n");
      }
      return ans;
   }

//post:  returns the word cleaned of external punctuation
   public String clean(String word)
   {
      word=word.trim();
      int first;
      for(first=0; first<word.length(); first++)
         if(Character.isLetterOrDigit(word.charAt(first)))
         {
            word = word.substring(first);
            break;     
         }
      if (first == word.length())	//loop ended because the word was made of all punctuation and didn't find a letter
         return null;
      int last;
      for(last=word.length()-1; last>=0; last--)
         if(Character.isLetter(word.charAt(last)))
         {
            word = word.substring(0, last+1);
            break;     
         }
      return word;
   }

/*******************************************************************************************
WRITE THE METHODS BELOW
*******************************************************************************************/


//post:  returns the pirate equivalent of the English word.
//			if the word is in the Map englishToPirate, return it's pirate equivalent
//			otherwise,
//			if the word is in the flipped version of englishToPirate, return the word unaltered
//			otherwise, 
//			use the following rules:
//		   	if the word ends with "ing", return the word as ending with "in-"
//			i.e. "rowing" is returned as "rowin-"
//			drop all your 'v' characters and replace them with a '-'.
//			i.e. "over" is returned as "o-er"
//			otherwise,
//			if the word meets none of the above cases, return the same word unaltered
   public String getWord(String word)
   {
      word = clean(word);
   
   
      return word;
   }

//post:  will return a map whose keySet is comprised of all the unique pirate words
//			and whose values are ArrayLists of all the english words that mapped to that particular pirate word.
//i.e.   one such Map element might have a key of "ahoy" and a value ["hello", "hi", "greetings"]
//			another element might have the key "there" and the value ["thar"] 

   public Map<String, ArrayList<String>> flip()
   {
      Map<String, ArrayList<String>> ans = new TreeMap();
   
      return ans;
   }

}