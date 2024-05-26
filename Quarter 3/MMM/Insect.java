 //Mash, Mangle & Munch - Rev Dr Douglas R Oberle, July 2012  doug.oberle@fcps.edu
 //Entity <- Player <- (abstract) Monster <- Insect
 
public class Insect extends Monster
{
 //ARGS:  row, col, image collection
   public Insect(int r, int c, String[][][] image)
   { 
      super("WoeMantis", r, c, image, 10, 30, 0, 100, 5, "SHRIEK", 30);
      setIsFlyer(true);				//we can fly (and do damage when we land)
      setIsShooter(true);			//we cab shoot a projectile (SHRIEK attack)
      setDamageInWater(true);		//we drown in water
      setIsFragile(true);			//we take 75% more damage from projectiles
      //super ARGS:  name, row, col, image collection, animation delay, stomp power, speed penalty, reload time, walk damage, projectileType, burnDamage
   }

   	//ARGS:  name, row loc, col loc, image collection, stomp power, speed penalty, reload time		
   public Insect(String n, int r, int c, String[][][] image, int sp, int spp, int rt)
   {
      super (n,  r, c, image, 10, sp, spp, rt, 5, "SHRIEK", 30);
      setIsFlyer(true);
      setIsShooter(true);
      setDamageInWater(true);
      setIsFragile(true);
   }


 //returns true if the monster can grab the unit of type specified by name
   public boolean canGrabUnit(String name)
   {
      if(name.startsWith("CYCLE") || name.startsWith("CAR") || name.startsWith("CROWD") || name.equals("AIR newscopter")
      || (this.isFlying() && name.startsWith("AIR")))		
         return true;	//WoeMantis can grab any aircraft if flying
      return false;
   }

//grabs a unit of type specified by name
   public void grabUnit(String name)
   {
      super.setClawContents(name);
   }

   //eats a unit and updates health and claw contents
   public void eatUnit()
   {
      String[] contents = this.getClawContents();
      int index = -1;		//index of claw contents that are full
      if(!contents[0].equals("empty"))
         index = 0;
      else
         if(!contents[1].equals("empty"))
            index = 1;
      if(index >= 0)
      {
         if(this.getHunger() > 0)
            this.setHunger(this.getHunger()-1);
         if(contents[index].startsWith("CROWD"))		//more health for eating a crowd of people
         {
            this.setHealth(this.getHealth()+5);
            this.setHunger(0); 
         }
         else
            if(contents[index].endsWith("bus"))			//satisfy hunger more for eating a bus
            {
               this.setHealth(this.getHealth()+4);
               this.setHunger(this.getHunger()-2);
            }
            else
               if(contents[index].endsWith("bomber"))			//satisfy hunger more for eating a bomber
               {
                  this.setHealth(this.getHealth()+5);
                  this.setHunger(this.getHunger()-3);
               }
               else
               {
                  this.setHealth(this.getHealth()+2);
                  this.setHunger(this.getHunger()-1);
               }
         this.clearClawContents();   
      }
   }

   public String reloadingMessage()
   {
      return "Out of breath!";
   }
}