 //Mash, Mangle & Munch - Rev Dr Douglas R Oberle, July 2012  doug.oberle@fcps.edu
 //Entity <- Player <- (abstract) Monster

public abstract class Monster extends Player
{
   private int stompPower;				//damage done by stomping
   private int walkDamage;		      //damage done by just walking on a structure (but not jumping on it)
   private int hunger;
   private String[] clawContents;	//what we are holding in our hands
   private int jumpAlt;					//y distance traveled when we jump (stomp)
   private boolean areJumping;		//to know when to start/stop jumping
   private boolean hitJumpPeak;
   private String projectileType;	//description of the bullet type the monster fires
   private int burnDamage;				//amount of damage done by standing on a burning structure  
  
   public Monster()
   {
      super();
      clawContents = null;
      stompPower = 0;
      jumpAlt = 0;
      hunger = 0; 
      walkDamage = 50;
      projectileType = "FIRE";
      burnDamage = 10; 
   }  

//ARGS:  name, row, col, image collection, animation delay, stomp power, speed penalty, reload time, walk Damage, projectileType, burnDamage 
   /**
    * 
    * @param n name
    * @param r row
    * @param c column
    * @param image image
    * @param ad animation delay
    * @param sp stomp power
    * @param spp speed penalty
    * @param rtr reload time
    * @param wd walk damage
    * @param pt projectile type
    * @param bd burn damage
    */
   public Monster(String n, int r, int c, String[][][] image, int ad, int sp, int spp, int rt, int wd, String pt, int bd)
   {
      super(n, r, c, image, ad, spp, rt);
   //ARGS:  name, row loc, col loc, image collection, animation delay, speed penalty, reload time			
      clawContents = new String[2];
      clearClawContents(); 
      stompPower = sp;
      hunger = 0;  
      jumpAlt = 0;
      walkDamage = wd;
      projectileType = pt;
      burnDamage = bd; 
   }
          
   public void setStompPower(int sp)
   {
      stompPower = sp;
   }

   public int getStompPower()
   {
      return stompPower;
   } 

   public int getHunger()
   {
      return hunger;
   }

   public void setHunger(int h)
   {
      hunger = h;
      if(hunger < 0)
         hunger = 0;
   }

   public String[] getClawContents()
   {
      return clawContents;
   }
 
   public void setClawContents(String cc)
   {
      clawContents[0] = cc;
   }  

   public void setClawContents(String[] cc)
   {
      clawContents[0] = cc[0];
      clawContents[1] = cc[1];
   }  


   public void clearClawContents()
   {
      clawContents[0] = "empty"; 
      clawContents[1] = "empty"; 
   }
 
   //post:  returns the type that the monster shoots - each monster has its own projectile type
   public String projectileType()
   {
      return projectileType;
   }


//returns the damage that a monster does to a structure just by walking on it (not stomping) - each monster can deal a different amount of damage
   public int walkDamage()
   {
      return walkDamage;
   }

//returns the amount of damage done to a monster if it is standing on a burning structure - each monster reacts to fire differently     
   public int burnDamage()
   {
      return burnDamage;
   }

 
   public boolean isJumping()
   {
      return areJumping;
   }

   public boolean hitJumpPeak()
   {
      return hitJumpPeak;
   }

   public void setIsJumping(boolean aj)
   {
      areJumping = aj;
   }

   public void setHitJumpPeak(boolean hjp)
   {
      hitJumpPeak = hjp;
   }

   public int getJumpAlt()
   {
      return jumpAlt;
   }

   public void setJumpAlt(int ja)
   {
      jumpAlt = ja;
   }

   public void addJumpAlt(int ja)
   {
      jumpAlt += ja;
   }

   public void subtJumpAlt(int ja)
   {
      jumpAlt -= ja;
   }

//grabs a unit of type specified by name - abstract, because different monsters grab units differently (Gorilla has two hands to grab with)
   public abstract void grabUnit(String name);
 
//returns true if the monster can grab the unit of type specified by name - abstract becuase different monsters can grab and hold different subsets of the vehicles
   public abstract boolean canGrabUnit(String name);

//eats a unit and updates health and claw contents - each monster benifits from eating different units in different ways
   public abstract void eatUnit();

}
