 //Mash, Mangle & Munch - Rev. Dr. Douglas R Oberle, June 2012  doug.oberle@fcps.edu
 //EXPLOSION OBJECT
    public class Explosion extends Entity
   {
      
   	//pre: 	r and c must be valid indecies of the board in MyGridExample
   	//			image must at least be of size 1 x 1 and contain String values of image file names
   	//			ad >= 1
   	//ARGS:  name, x pixel loc, y pixel loc, image collection, animation delay
       public Explosion(String n, int X, int Y, String[][][] image, int ad)
      {
         super(n,X,Y,image,ad);
      }
   
   }