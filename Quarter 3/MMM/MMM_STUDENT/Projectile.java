 //Mash, Mangle & Munch - Rev. Dr. Douglas R Oberle, June 2012  doug.oberle@fcps.edu
 //BULLET OBJECT
    public class Projectile extends Entity
   {
      private int power;		//the power of the bullet  	
      private String type;		//what type of bullet it is (ROCKET, thrown CAR, tank SHELL,  etc)
      private int speed;		//how fast the bullet travels
      private boolean inAir;	//shoot at an air target
      private int owner;		//index of the owner of the bullet (so a player doesn't shoot themselves)
      private int detX;			//x position to detonate at
      private int detY;			//y postition to detonate at
   	
   	//pre: 	r and c must be valid indecies of the board in MyGridExample
   	//			image must at least be of size 1 x 1 and contain String values of image file names
   	//			ad >= 1
   	//ARGS:  name, x pixel loc, y pixel loc, bullet power, image collection, animation delay, bullet type, bullet speed, air shot?, owner, detonation x loc, detonation y loc
       public Projectile(String n, int X, int Y, int p, String[][][] image, int ad, String t, int s, boolean ia, int o, int dx, int dy)
      {
         super(n,X,Y,image,ad);
         power = p;
         type = t;
         speed = s;
         inAir = ia;
         owner = o;
         detX = dx;
         detY = dy;
      }
   
       public int getDetX()
      {
         return detX;
      }
   	
       public int getDetY()
      {
         return detY;
      }
   
       public void setDetX(int dx)
      {
         detX = dx;
      }
   
       public void setDetY(int cy)
      {
         detY = cy;
      }
   
       public int getOwner()
      {
         return owner;
      }
   
       public void setOwner(int o)
      {
         owner = o;
      }
   
       public boolean inAir()
      {
         return inAir;
      }
   	
       public void setInAir(boolean ia)
      {
         inAir = ia;
      }
   
       public int getPower()
      {
         return power;
      }
   	
       public void setPower(int p)
      {
         power = p;
      }
      
       public String getType()
      {
         return type;
      }
   	
       public void setType(String t)
      {
         type = t;
      }
      
       public int getSpeed()
      {
         return speed;
      }
   	
       public void setSpeed(int s)
      {
         speed = s;
      }
   }
