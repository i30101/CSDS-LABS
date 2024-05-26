 //Mash, Mangle & Munch - Rev. Dr. Douglas R Oberle, June 2012   doug.oberle@fcps.edu
 //STRUCTURE OBJECT  
   import javax.swing.ImageIcon;
   public class Structure extends Entity
   {
      private int row;					//start row for the player
      private int col;					//start col for the player
      private int panel;				//stores the number of which panel the structure is in
      private boolean isPassable;	//can we walk over the object
      private boolean isDestroyable;//can we destroy the object, making it passable
      private int height;				//height of building to determine the draw location on the screen
      private int sizeValue;			//stores the image size
      private String[][][] images;	//stores structure images
      private int heightDiff;			//variations in building height
      private int health;				//the health of the object if destroyable
      private int imageIndex;			//the structure # in the image array
      private long propertyValue;	
      private boolean onFire;			//are we on fire?
      private int webValue;			//0-no web on us, 1-single web, 2-web chained to another tower close to it
     	//pre: 	r and c must be valid indecies of the board in MyGridExample
   	//			image must at least be of size 1 x 1 and contain String values of image file names
   	//			ad >= 1
   	//			image has the healthy structure animations in row 0, destroyed animaions in row 1
   	//			a neg health means the structure is passable and destroyable (trees and small buildings)
   	//ARGS:  name, row, col, panel, image collection, animation delay, is passable?, is destroyable?, structure height, health, image size, image index, property value
      /**
       * Creates a new structure
       * @param n name of structure
       * @param r row
       * @param c column
       * @param p panel
       * @param image image
       * @param ad animation delay
       * @param iP is passable
       * @param iD is destroyable
       * @param ht height
       * @param h health
       * @param SIZE size
       * @param ii image index
       * @param pv property value
       */
      public Structure(String n, int r, int c, int p, String[][][] image, int ad, boolean iP, boolean iD, int ht, int h, int SIZE, int ii, long pv)
      {
         super(n, 0, 0, image, ad);
         row = r;
         col = c;
         panel = p;
         images = image;
         isPassable = iP;
         isDestroyable = iD;
         height = ht;
         sizeValue = SIZE;
         health = h;
         imageIndex = ii;
         if(image[ii][0][0].indexOf("coaster")>=0)		//make rollercoaster parts the same height
            heightDiff = (int)(SIZE/1.75);
         else   
            if(super.getName().endsWith("LANDMARK") && height == 2)  
               heightDiff = (int)(SIZE/1.0);
            else
               if(super.getName().equals("ELEC TOWER 0"))  
                  heightDiff = (int)(SIZE/1.5);
               else
                  if(super.getName().startsWith("WATER TOWER"))  
                     heightDiff = (int)(SIZE/2);
                  else
                     if(super.getName().equals("ELEC TOWER 1") || super.getName().startsWith("Blop"))  
                        heightDiff = 0;
                     else
                        heightDiff = (int)(Math.random()*(SIZE/1.5));
         propertyValue = pv;
         onFire = false;
         webValue = 0;	
         //System.out.println(this);
      }
   
      public Structure clone()
      {
         return (new Structure(this.getName(), row, col, panel, images, this.getAnimationDelay(), isPassable, isDestroyable, height, health, sizeValue, imageIndex, propertyValue));
      }
   
      public String toString()
      {	//used to store structures in a file for custom built maps
         return getName() + ":" + row + " " + col + " " + panel + " " + getAnimationDelay() + " " + isPassable + " " + isDestroyable + " " + height + " " + health + " " + imageIndex + " " + propertyValue;
      }
   
      public boolean isPassableByCrowd()
      {
         if(super.getName().startsWith("Blop") && health == 0)
            return true;
         return false;
      }
   
      public boolean isPassableByTank()
      {
         if(super.getName().startsWith("Blop") || health == 0)
            return true;
         return false;
      }
      
   	//post:  returns true if the bullet b can be shot over the current structure
      public boolean canShootOver(Projectile b)
      {
         if(b.inAir())
            return true;
         if(super.getName().startsWith("Blop"))
         { //shells and fire will damage and destroy Blop-glop
            if((b.getType().startsWith("SHELL") || b.getType().startsWith("FIRE")) && health < 0)
               return false;
            return true;
         }
         if((super.getName().indexOf("FUEL")>=0 || super.getName().indexOf("BUISNESS")>=0) && health < 0)
            return false;	//houses and trees are too short - can be shot over
         if(health <= 0)
            return true;  
         return false;    
      }
   
      public boolean onFire()
      {
         return onFire;
      }
   
      public void setOnFire(boolean of)
      {
         if(!super.getName().startsWith("ELEC") && isDestroyable && health>0)  
            onFire = of;
      }
           
      public void damage(int points)
      {
         if(isDestroyable)
         {
            health -= points;
            if(health <= 0)
            {
               health = 0;
               isPassable = true;
               onFire = false;
            }
         }
      }
   
   	//post:	shows building ok (col 0) or destroyed (col 1) for building at index row i
   	//			doesn't advance the animation index
      public ImageIcon getPicture()
      {
         int i = imageIndex;
         ImageIcon[][][] pics = super.getPictures();
         if(health!=0 && i >= 0 && i<pics.length)	//a neg health means the structure is passable and destroyable (trees and small buildings)
            return pics[i][0][0];						//show healthy structure (col 0 animaitons)
         else													//show destroyed structure (col 1)
            if(i >= 0 && i<pics.length && pics[0].length > 1)
               return pics[i][1][0];
         return pics[0][0][0];
      }
   	//post:	advances the animation index but stops on the last image
      public void advanceAnimation()
      {
         int nf = super.getNumFrames();
         int ai = super.getAnimationIndex();
         if(nf == Integer.MAX_VALUE)
            super.setNumFrames(0);
         nf++;
         super.setNumFrames(nf);
         if(nf % (super.getAnimationDelay()) == 0 && ai < (super.getPictures()[0].length)-1)
         {
            ai++;
            super.setAnimationIndex(ai);
         }
      }
   
   //post:	returns an image of the structure at index i
   	//			and advances the animation index
      public ImageIcon getPictureAndAdvance()
      {
         int i = imageIndex;
         ImageIcon[][][] pics = super.getPictures();
         ImageIcon temp;
         if(i >= 0 && i<pics.length)		
         {
            if(health != 0)				//a neg health means the structure is passable and destroyable (trees and small buildings)
               temp = pics[i][0][0];	//show healthy structure (col 0 animaitons)
            else
            {
               temp = pics[i][super.getAnimationIndex()][0];
               advanceAnimation();
            }
         }
         else
            temp = pics[0][0][0];
         return temp;
      }
   
   
   //pre:  SIZE is the pixel size of the player
   //post: returns the actual x coordinate in pixel space
      public int findX(int SIZE)
      {
         setX(SIZE*this.getCol());
         return getX();
      }
   
   //pre:  SIZE is the pixel size of the player
   //post: returns the actual y coordinate in pixel space
      public int findY(int SIZE)
      {
         setY(SIZE*this.getRow());
         return getY();
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
   
      public int getPanel()
      {
         return panel;
      }
   	
      public void setPanel(int p)
      {
         panel = p;
      }
   
   
      public void setHeight(int ht)
      {
         height = ht;
      }
   	
      public int getHeight()
      {
         return height;
      }
   	
   
      public boolean isPassable()
      {
         return isPassable;
      }
      
      public boolean isDestroyable()
      {
         return isDestroyable;
      }
      
      public int getHealth()
      {
         return health;
      }
      
      public void setIsPassable(boolean iP)
      {
         isPassable = iP;
      }
   	
      public void setIsDestroyable(boolean iD)
      {
         isDestroyable = iD;
      }
   
      public void setHealth(int h)
      {
         health = h;
      }
   
      public int getHeightDiff()
      {
         return heightDiff;
      }
      
      public int getImageIndex()
      {
         return imageIndex;
      }
   	
      public void setImageIndex(int ii)
      {
         imageIndex = ii;
      }
      
      public long getPropertyValue()
      {
         return propertyValue;
      }
   	
      public void setPropertyValue(long pv)
      {
         propertyValue = pv;
      }
   
      public int getWebValue()	
      {
         return  webValue;
      }
     
      public void setWebValue(int v)
      {
         webValue = v;	
      }	
   }
