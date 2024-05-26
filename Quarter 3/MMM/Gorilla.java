 //Mash, Mangle & Munch - Rev Dr Douglas R Oberle, July 2012  doug.oberle@fcps.edu
 //Entity <- Player <- (abstract) Monster <- Gorilla
 
public class Gorilla extends Monster
{
 //ARGS:  row, col, image collection
   public Gorilla(int r, int c, String[][][] image)
   {
      super("King-Clunk", r, c, image, 10, 30, 0, 0, 10, "CAR", 5);
      //super ARGS:  name, row, col, image collection, animation delay, stomp power, speed penalty, reload time, walk damage, projectileType, burnDamage
      setIsThrower(true);			//we can pick up and throw projectiles
      setIsAmbidextorous(true);	//we can grab and hold with both hands
      setDamageInWater(true);		//we drown in water
      setLessAppetite(true);		//we don't get hungry as fast
   }

  //ARGS:  name, row loc, col loc, image collection, stomp power, speed penalty, reload time		
   public Gorilla(String n, int r, int c, String[][][] image, int sp, int spp, int rt)
   {
      super (n,  r, c, image, 10, sp, spp, rt, 10, "CAR", 5);
      setIsThrower(true);
      setIsAmbidextorous(true);
      setDamageInWater(true);
      setLessAppetite(true);
   }

 //returns true if the monster can grab the unit of type specified by name
   public boolean canGrabUnit(String name)
   {
      if(name.startsWith("CYCLE") || name.startsWith("CAR") || name.startsWith("CROWD") || name.equals("AIR newscopter"))
         return true;
      return false;
   }

//grabs a unit of type specified by name
   public void grabUnit(String name)
   {
      String[] contents = super.getClawContents();
      if(contents[0].equals("empty"))
         contents[0] = name;
      else
         contents[1] = name;
      super.setClawContents(contents);   
   }

 //post:  returns the type that the monster shoots
   public String projectileType()
   {
      String[] contents = super.getClawContents();
      if(!contents[0].startsWith("CROWD") && !contents[0].startsWith("empty"))
         return contents[0];
      if(!contents[1].startsWith("CROWD") && !contents[1].startsWith("empty"))
         return contents[1];
      return "empty";
   }
   
	   //eats a unit and updates health and claw contents
   public void eatUnit()
   {
      String[] contents = super.getClawContents();
      int index = -1;		//index of claw contents that are full
      if(!contents[0].equals("empty"))
         index = 0;
      else
         if(!contents[1].equals("empty"))
            index = 1;
    	//if they are holding a CROWD, prioritize eating them
      if(contents[0].startsWith("CROWD"))
         index = 0;
      else
         if(contents[1].startsWith("CROWD"))
            index = 1;
      if(index >= 0)
      {
         if(contents[index].startsWith("CROWD"))		//more health for eating a crowd of people
         {
            super.setHealth(super.getHealth()+10);
            super.setHunger(0); 
         }
         else
            if(contents[index].endsWith("bus"))			//satisfy hunger more for eating a bus
            {
               super.setHealth(super.getHealth()+8);
               super.setHunger(super.getHunger()-2);
            }
            else
            {
               super.setHealth(super.getHealth()+5);
               super.setHunger(super.getHunger()-1);
            }
         contents[index] = "empty";
         super.setClawContents(contents);     
      }
   }

   public String reloadingMessage()
   {
      return "Nothing to throw!";
   }
}