    //Mash, Mangle & Munch - Rev Dr Douglas R Oberle, July 2012  doug.oberle@fcps.edu
    //Entity <- Player <- (abstract) Monster <- Robot (hello Karel)
public class Robot extends Monster
{
    //ARGS:  row, col, image collection
   public Robot(int r, int c, String[][][] image)
   {
      super("BoobooTron", r, c, image, 15, 100, 2, 30, 50, "BEAM", 1);
         //super ARGS:  name, row, col, image collection, animation delay, stomp power, speed penalty, reload time, walk Damage, projectileType, burnDamage
      setHead360(true);				//we can turn canon (head) 360 degrees
      setEnergyAbsorber(true);	//we don't eat enemy units but can absorb energy from electric towers, power plants and trains (and short circuit in water)
      setIsShooter(true);	      //we can shoot a projectile (BEAM)
   }
   
      	//ARGS:  name, row loc, col loc, image collection, stomp power, speed penalty, reload time		
   public Robot(String n, int r, int c, String[][][] image, int sp, int spp, int rt)
   {
      super (n,  r, c, image, 15, sp, spp, rt, 50, "BEAM", 1);
      setHead360(true);
      setEnergyAbsorber(true);
      setIsShooter(true);	      //we can shoot a projectile (BEAM)
   }
   
    //returns true if the monster can grab the unit of type specified by name
    //Robot can grab electric towers and trains
   public boolean canGrabUnit(String name)
   {
      if(name.startsWith("TRAIN"))
         return true;
      return false;
   }
   
   //grabs a unit of type specified by name
   //The robot has no arms
   public void grabUnit(String name)
   {
   }
   
   //post: eats a unit and updates health and claw contents
   //The robot doesn't eat
   public void eatUnit()
   {
   }    
      
   public String reloadingMessage()
   {
      return "Recharging!";
   }
   
}