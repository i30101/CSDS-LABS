 //Mash, Mangle & Munch - Rev Dr Douglas R Oberle, June 2012  doug.oberle@fcps.edu
 //PLAYER OBJECT  

import javax.swing.ImageIcon;
  
public class Player extends Entity
{
   private int row;			      //start row for the player
   private int col;			      //start col for the player

   private int health;
   private int reloadTime;			//# frames needed inbetween shots
   private int lastShotTime;		//the frame time that the last shot was taken
   private int headTurnTime;		//the frame time that we alst turned our head (so mouse wheel control is better)
  	
   private int bodyDirection;		//which way is our body facing
   private int headDirection;		//which way is our head facing

   private boolean head360;				//can we rotate our head 360 degrees like a tank?
   private boolean isSwimmer;				//can we swim at full speed in the water?
   private boolean isFlyer;				//can we fly
   private boolean isDigger;				//can we dig underground (and water is instant death)
   private boolean isThrower;				//is our projectile attack done by picking up and throwing vehicles  
   private boolean isShooter;				//do we spit, breathe or shoot a projectile that requires that we are not weakened with hunger	
   private boolean canSplit;				//can we split into a twin at the expense of a loss of health?
   private boolean healInWater;			//can we heal when in water?
   private boolean isAmbidextorous;		//can we grab with both hands?
   private boolean damageInWater;		//do we drown in water?	(damage over time)
   private boolean lessAppetite;			//do we have less appetite? (50% slower hunger)
   private boolean imperviousToBullets;//no damage from bullets?
   private boolean isFragile;				//do we take 75% more damage from projectiles?
   private boolean slimeTrail;			//do we leave a trail of toxic slime?
   private boolean energyAbsorber;		//don't grab units to eat, but get power from absorbing energy from elec towers, power plants and trains
												//(and short circuit in water)
   private boolean canEatAll;				//can we grab and eat any type of unit?
   private boolean mindControl;			//can we make enemy units fight each other?
   private boolean cantGrab;           //for one reason or another, our player can't hold on to food and will eat it right away

   private boolean isFlying;		//is the player currently in flight?
   private boolean isDigging;		//is the player digging underground?

   private int speedPenalty;		//the higher it is, the slower we move    

   private int moveIncrX;			//pixel increments for transitioning between array coordinates
   private int moveIncrY;

   private int tempX;		//save locations for graphic position of where the player is 
   private int tempY;		//to be used to draw the player in motion when transitioning from one cell to another
      
   private int detRow;		//row value the bomber has to reach to detonate the nuke		
   private int detCol;		//col value the bomber has to reach to detonate the nuke		
   private int detDir;		//direciton bomber is moving for detonation

 
   public Player()
   {
      super("NONE", 0, 0, null, 0);
   //ARGS:  name, x pixel loc, y pixel loc, image array, animation delay
      row = 0;
      col = 0;
      bodyDirection = 0;
      headDirection = 0;
   
      head360 = false;
      isSwimmer = false;
      isFlyer = false;
      isDigger = false;
      isThrower = false;
      isShooter = false;
      healInWater = false;
      canSplit = false;
      isAmbidextorous = false;
      damageInWater = false;
      lessAppetite = false;
      imperviousToBullets = false;
      isFragile = false;
      slimeTrail = false;
      energyAbsorber = false;
      canEatAll = false;
      mindControl = false;
      cantGrab = false;
      
      isFlying = false;
      isDigging = false;
      speedPenalty = 0;
      health = 100;
      moveIncrX = 0;
      moveIncrY = 0;
      tempX = 0;
      tempY = 0;
      reloadTime = 0;
      lastShotTime = 0;
      headTurnTime = 0;
      detRow = -1;
      detCol = -1;
   }

//pre: 	r and c must be valid indecies of the board in MyGridExample
//			image must at least be of size 1 x 1 and contain String values of image file names
//			ad >= 1
//ARGS:  name, row loc, col loc, image collection, animation delay, speed penalty, reload time		
   public Player(String n, int r, int c, String[][][] image, int ad, int spp, int rt)
   {
      super(n, 0, 0, image, ad);
   //ARGS:  name, x pixel loc, y pixel loc, image array, animation delay
      row = r;
      col = c;
      bodyDirection = RIGHT;
      headDirection = RIGHT;
   
      head360 = false;
      isSwimmer = false;
      isFlyer = false;
      isDigger = false;
      isThrower = false;
      isShooter = false;
      canSplit = false;
      healInWater = false;
      isAmbidextorous = false;
      damageInWater = false;
      lessAppetite = false;
      imperviousToBullets = false;
      isFragile = false;
      slimeTrail = false;
      energyAbsorber = false;
      canEatAll = false;
      mindControl = false;
      cantGrab = false;
      
      isFlying = false;
      isDigging = false;
      speedPenalty = spp;
      health = 100;
      moveIncrX = 0;
      moveIncrY = 0;
      tempX = 0;
      tempY = 0;
      reloadTime = rt;
      lastShotTime = reloadTime;
      headTurnTime = 0;
      detRow = -1;
      detCol = -1;
   }

   public void setIsSwimmer(boolean is)
   {
      isSwimmer = is;
   }

   public boolean isSwimmer()
   {
      return isSwimmer;
   }

   public void setIsFlyer(boolean i)
   {
      isFlyer = i;
   }

   public boolean isFlyer()
   {
      return isFlyer;
   }

   public void setIsDigger(boolean id)
   {
      isDigger = id;
   }

   public boolean isDigger()
   {
      return isDigger;
   }

   public void setIsThrower(boolean it)
   {
      isThrower = it;
   }

   public boolean isThrower()
   {
      return isThrower;
   }

   public void setIsShooter(boolean it)
   {
      isShooter = it;
   }

   public boolean isShooter()
   {
      return isShooter;
   }

   public void setCanSplit(boolean cs)
   {
      canSplit = cs;
   }

   public boolean canSplit()
   {
      return canSplit;
   }

   public void setHealInWater(boolean hiw)
   {
      healInWater = hiw;
   }

   public boolean healInWater()
   {
      return healInWater;
   }

   public boolean isAmbidextorous()
   {
      return isAmbidextorous;
   }

   public void setIsAmbidextorous(boolean ia)
   {
      isAmbidextorous = ia;
   }

   public boolean damageInWater()
   {
      return damageInWater;
   }

   public void setDamageInWater(boolean diw)
   {
      damageInWater = diw;
   }

   public boolean lessAppetite()
   {
      return lessAppetite;
   }

   public boolean canEatAll()
   {
      return canEatAll;
   }

   public void setCanEatAll(boolean cea)
   {
      canEatAll = cea;
   }

   public boolean cantGrab()
   {
      return cantGrab;
   }

   public void setCantGrab(boolean cg)
   {
      cantGrab = cg;
   }

   public void setLessAppetite(boolean la)
   {
      lessAppetite = la;
   }

   public boolean imperviousToBullets()
   {
      return imperviousToBullets;
   }

   public void setImperviousToBullets(boolean i2b)
   {
      imperviousToBullets = i2b;
   } 
 
   public boolean isFragile()
   {
      return isFragile;
   }

   public void setIsFragile(boolean iF)
   {
      isFragile = iF;
   }

   public boolean isMindControl()
   {
      return mindControl;
   }

   public void setIsMindControl(boolean mC)
   {
      mindControl = mC;
   }

   public boolean slimeTrail()
   {
      return slimeTrail;
   }
 
   public void setSlimeTrail(boolean st)
   {
      slimeTrail = st;
   }
 
   public boolean energyAbsorber()
   {
      return energyAbsorber;
   }
 
   public void setEnergyAbsorber(boolean ea)
   {
      energyAbsorber = ea;
   }
   public void setHead360(boolean h360)
   {
      head360 = h360;
   }

   public boolean head360()
   {
      return head360;
   }

 
   public int getHeadTurnTime()
   {
      return headTurnTime;
   }

   public void setHeadTurnTime(int ht)
   {
      headTurnTime = ht;
   }

   public int getLastShotTime()
   {
      return lastShotTime;
   }

   public void setLastShotTime(int lst)
   {
      lastShotTime = lst;
   }

   public int getReloadTime()
   {
      return reloadTime;
   }

   public void setReloadTime(int rt)
   {
      reloadTime = rt;
   }
   
   public void clearDirections()
   {
      super.clearDirections();
      moveIncrX = 0;
      moveIncrY = 0;
   }

   public int getSpeedPenalty()
   {
      return speedPenalty;
   }

   public void setSpeedPenalty(int sp)
   {
      speedPenalty = sp;
   }

//pre:  SIZE is the pixel size of the player
//post: returns the actual x coordinate in pixel space
   public int findX(int SIZE)
   {
      setX((SIZE*this.getCol()) + this.getMoveIncrX());
      return getX();
   }

//pre:  SIZE is the pixel size of the player
//post: returns the actual y coordinate in pixel space
   public int findY(int SIZE)
   {
      setY((SIZE*this.getRow()) + this.getMoveIncrY());
      return getY();
   }

   public int getMoveIncrX()
   {
      return moveIncrX;
   }

   public int getMoveIncrY()
   {
      return moveIncrY;
   }

   public void setMoveIncrX(int x)
   {
      moveIncrX = x;
   }

   public void setMoveIncrY(int y)
   {
      moveIncrY = y;
   }

   public void addMoveIncrX(int x)
   {
      moveIncrX += x;
   }

   public void addMoveIncrY(int y)
   {
      moveIncrY += y;
   }

   public void setCoord(int r, int c)
   {
      row = r;
      col = c;
   }

   
   public void setRow(int r)
   {
      row = r;
   }

   
   public void setCol(int c)
   {
      col = c;
   }

   public int getRow()
   {
      return row;
   }

   public int getCol()
   {
      return col;
   }

   public int getTempX()
   {
      return tempX;
   }

   public int getTempY()
   {
      return tempY;
   }

   public void setTempX(int x)
   {
      tempX = x;
   }

   public void setTempY(int y)
   {
      tempY = y;
   } 

   
   public void setBodyDirection(int bd)
   {
      bodyDirection = bd;
   }

   public int getBodyDirection()
   {
      return bodyDirection;
   } 

   public void setHeadDirection(int hd)
   {
      headDirection = hd;
   }

   public int getHeadDirection()
   {
      return headDirection;
   } 


//post:	returns an image of the player depending on which way they are facing
//			but doesn't advance the animation index
   public ImageIcon getPicture()
   {
      ImageIcon[][][] pictures = getPictures();
      if(pictures==null)
         return null;
      if(bodyDirection == UP && UP < pictures.length)
      {
         if(headDirection < pictures[0][0].length)
            return  pictures[UP][getAnimationIndex()][headDirection];
         return pictures[UP][getAnimationIndex()][0];
      }
      if(bodyDirection == RIGHT  && RIGHT <  pictures.length)
      {
         if(headDirection < pictures[0][0].length)
            return  pictures[RIGHT][getAnimationIndex()][headDirection];
         return  pictures[RIGHT][getAnimationIndex()][0];
      }
      if(bodyDirection == DOWN  && DOWN <  pictures.length)
      {
         if(headDirection < pictures[0][0].length)
            return  pictures[DOWN][getAnimationIndex()][headDirection];
         return  pictures[DOWN][getAnimationIndex()][0];
      }
      if(bodyDirection == LEFT  && LEFT <  pictures.length)
      {
         if(headDirection < pictures[0][0].length)
            return pictures[LEFT][getAnimationIndex()][headDirection];
         return pictures[LEFT][getAnimationIndex()][0];
      }
      return pictures[0][0][0];
   }

//post:	returns an image of the player depending on which way they are facing
//			and advances the animation index
   public ImageIcon getPictureAndAdvance()
   {
      ImageIcon temp;
      ImageIcon[][][] pictures = getPictures();
      if(pictures==null)
         return null;
      if(bodyDirection == UP && UP < pictures.length)
      {
         if(headDirection < pictures[0][0].length)
            temp =  pictures[UP][getAnimationIndex()][headDirection];
         else
            temp =  pictures[UP][getAnimationIndex()][0];
      }
      else
         if(bodyDirection == RIGHT  && RIGHT < pictures.length)
         {
            if(headDirection < pictures[0][0].length)
               temp = pictures[RIGHT][getAnimationIndex()][headDirection];
            else
               temp =  pictures[RIGHT][getAnimationIndex()][0];
         }
         else
            if(bodyDirection == DOWN  && DOWN < pictures.length)
            {
               if(headDirection < pictures[0][0].length)
                  temp = pictures[DOWN][getAnimationIndex()][headDirection];
               else
                  temp =  pictures[DOWN][getAnimationIndex()][0];
            }
            else
               if(bodyDirection == LEFT  && LEFT < pictures.length)
               {
                  if(headDirection < pictures[0][0].length)
                     temp = pictures[LEFT][getAnimationIndex()][headDirection];
                  else
                     temp =  pictures[LEFT][getAnimationIndex()][0];
               }
               else
                  temp = pictures[0][getAnimationIndex()][0];
      advanceAnimation();
      return temp;
   }

   
   public int getHealth()
   {
      return health;
   }	
  	
   public void setHealth(int h)
   {
      health = h;
      if(health > 100)
         health = 100;
   }
    	
   public void damage (int d)
   {
      health -= d;
      if(mindControl && health < 25)
         Utilities.breakMindControl();
   }

   public void setDetRow(int dr)
   {
      detRow = dr;
   }

   public void setDetCol(int dc)
   {
      detCol = dc;
   }

   public int getDetRow()
   {
      return detRow;
   }

   public int getDetCol()
   {
      return detCol;
   }

   public void setDetDir(int dd)
   {
      detDir = dd;
   }

   public int getDetDir()
   {
      return detDir;
   }
 
   public boolean isFlying()
   {
      return isFlying;
   }

   public void setIsFlying(boolean iF)
   {
      isFlying = iF;
   }

   public boolean isDigging()
   {
      return isDigging;
   }

   public void setIsDigging(boolean iF)
   {
      isDigging = iF;
   }

   public String reloadingMessage()
   {
      return "Reloading!";
   }
}