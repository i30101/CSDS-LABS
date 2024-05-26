 //Mash, Mangle & Munch - Rev. Dr. Douglas R Oberle, June 2012   doug.oberle@fcps.edu
 //SCORE OBJECT  
    public class playerScore
   {
      private long damage;		//how much property damage was done
      private String name;		//monster name
      private int time;			//how much time elapsed
   
       public playerScore()
      {
         damage = 0;
         name = "";
         time = 0;
      }
     
     	//ARGS:	property damage done, name, time elapsed
       public playerScore(long d, String n, int t)
      {
         damage = d;
         name = n;
         time = t;
      }
   
       public long getDamage()
      {
         return damage;
      }
   	
       public void setDamage(long d)
      {
         damage = d;
      }
    
       public String getName()
      {
         return name;
      }
   	
       public void setName(String n)
      {
         name = n;
      }
   	
       public int getTime()
      {
         return time;
      }
   	
       public void setTime(int t)
      {
         time = t;
      }
      
       public String toString()
      {
         return damage + " " + name + " " + time;
      }
   }