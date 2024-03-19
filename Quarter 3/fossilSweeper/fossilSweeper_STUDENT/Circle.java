//D Oberle 5/19/20
import java.awt.Color;
import java.util.Set;
import java.util.TreeSet;
public class Circle 
{   
   private int x, y;                //upper-left corner coordinates of the smallest square around the circle
   private int radius, opacity;
   private Color color;
  
   //pre:  dX,dY are >= 0 and < screen dimensions, dR > 0
   //post: construct a circle with a random color 
   public Circle(int dX, int dY, int dR) {
      x = dX;
      y = dY;
      radius = dR;
      opacity = 255;
      int r = (int)(Math.random() * 255);
      int g = (int)(Math.random() * 255);
      int b = (int)(Math.random() * 255);      
      color = new Color(r,g,b,opacity);
   }
   
   //pre:  dX,dY are >= 0 and < screen dimensions, dR > 0, c != null
   //post: construct a circle with a certain color.  If c is Color.red or Color.blue, it makes the circle a redish or blueish tone
   public Circle(int dX, int dY, int dR, Color c) 
   {
      x = dX;
      y = dY;
      radius = dR;
      opacity = 255;     
      if(c.equals(Color.yellow))    //punch powerup
      {
         color = c;
      }
      else
      {
         int r = (int)(Math.random() * 255);
         int g = (int)(Math.random() * 255);
         int b = (int)(Math.random() * 255); 
         Set<Integer>colorParts = new TreeSet<Integer>();
         colorParts.add(r);
         colorParts.add(g);
         colorParts.add(b);         //puts the largest value at the end
         while(colorParts.size() < 3)
         {  //if there were any repeat values in r,g,b, the set would only allow unique elements.  So, fill it up to size 3.
            colorParts.add((int)(Math.random() * 255));
         } 
         Object [] temp = colorParts.toArray();
         if(c.equals(Color.red))          //make redish for player 1
         {
            r = (Integer)(temp[2]);       //make largest value red
            g = (Integer)(temp[0]);
            b = (Integer)(temp[1]);
         }
         else if(c.equals(Color.blue))    //make blueish for player 2
         {
            r = (Integer)(temp[0]);
            g = (Integer)(temp[1]);
            b = (Integer)(temp[2]);       //make largest value blue
         }
         color = new Color(r,g,b,opacity);
      }
   }
   
   //post: increases the radius and makes the circle more transparent (smoke that is fading away)  
   public void age() 
   {
      radius++;
      opacity-=5;
   }
   
   public Color getColor()
   {
      int r = color.getRed();
      int g = color.getGreen();
      int b = color.getBlue();
      return new Color(r, g, b, opacity);
   }
   
   public int getX() 
   {
      return x;
   }
   
   public int getY() 
   {
      return y;
   }
   
   public int getRadius() 
   {
      return radius;
   }
   
   public int getOpacity() 
   {
      return opacity;
   }
   
   //post: returns true if the circle is completely transparent
   public boolean isDead() 
   {
      if (opacity <= 0) 
      {
         return true;
      } 
      return false;
   }
   
}