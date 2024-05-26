 //Mash, Mangle & Munch - Rev Dr Douglas R Oberle, July 2012  doug.oberle@fcps.edu
 //Entity <- Player <- Vehicle
public class Vehicle extends Player
{
   private boolean onField;		//has the player entered the visible panel?  Used to have enemy AI respawn if they leave the panel
   private int stunTime;			//the time in which a player is stunned and can't move or shoot (in frames)  
   private boolean mindControlled;	//is the player's brain scrambled, making them attack others?
   private double propertyValue;	

   public Vehicle()
   {
      super();
      stunTime = 0;
      mindControlled = false;
      propertyValue = 0;
      onField = false;
   }

//pre: 	r and c must be valid indecies of the board in MyGridExample
//			image must at least be of size 1 x 1 and contain String values of image file names
//			ad >= 1
//ARGS:  name, row loc, col loc, image collection, animation delay, speed penalty, reload time, property value			
   public Vehicle(String n, int r, int c, String[][][] image, int ad, int spp, int rt, int pv)
   {
      super(n, r, c, image, ad, spp, rt);
   //ARGS:  name, row loc, col loc, image collection, animation delay, speed penalty, reload time	
      stunTime = 0;
      mindControlled = false;
      propertyValue = pv;
      onField = false;
      if(this.getName().startsWith("AIR"))
      {
         setIsFlyer(true);
         setIsFlying(true);
      }
      else if(this.getName().startsWith("TANK") || this.getName().endsWith("jeep"))
         setHead360(true);   
   }

   public double getPropertyValue()
   {
      return propertyValue;
   }

   public void setPropertyValue(int pv)
   {
      propertyValue = pv;
   }

   public boolean getOnField()
   {
      return onField;
   }

   public void setOnField(boolean of)
   {
      onField = of;
   }

   public int getStunTime()
   {
      return stunTime;
   }

   public void setStunTime(int st)
   {
      stunTime = st;
   }

   public boolean isMindControlled()
   {
      return mindControlled;
   }

   public void setMindControlled(boolean iMC)
   {
      mindControlled = iMC;
   }
}