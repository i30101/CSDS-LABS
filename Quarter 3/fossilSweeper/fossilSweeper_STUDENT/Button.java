import java.awt.Color;
import java.awt.Shape;
import java.awt.Rectangle;
import java.awt.Graphics;
import javax.swing.ImageIcon;

//a Button class that has data fields and abilities for dynamic buttons in a game compatible with a MouseListener
public class Button
{
   private Shape shape;       //We can send any class that implements java.awt.Shape interface
   private String title;      //The title is the text we want drawn in the Button (if any).
   private String extraInfo;  //additional info we might want to show in the button
   private Color color, regularColor, highlightColor, textColor;
   /*color is the current color of the Button which will change from regularColor to highlightColor as the mouse moves over it*/
   private ImageIcon image, regularImage, highlightImage;
   /*using graphic images for the button instead of color*/
   private int numAnts;       //the number of spaces we are adjacent to that have an ant-hill.
   private static ImageIcon [] antImages = { new ImageIcon("images/ants0.gif"), new ImageIcon("images/ants1.gif"), new ImageIcon("images/ants2.gif"),
                                             new ImageIcon("images/ants3.gif"), new ImageIcon("images/ants4.gif"), new ImageIcon("images/ants5.gif"),
                                             new ImageIcon("images/ants6.gif"), new ImageIcon("images/ants7.gif"), new ImageIcon("images/ants8.gif")};
   private static ImageIcon [] numberImages = { new ImageIcon("images/number0.gif"), new ImageIcon("images/number1.gif"), new ImageIcon("images/number2.gif"),
                                             new ImageIcon("images/number3.gif"), new ImageIcon("images/number4.gif"), new ImageIcon("images/number5.gif"),
                                             new ImageIcon("images/number6.gif"), new ImageIcon("images/number7.gif"), new ImageIcon("images/number8.gif")};                                          
   private static ImageIcon unclickedImage = new ImageIcon("images/unclicked.jpg");
   private static ImageIcon flaggedImage = new ImageIcon("images/flagged.jpg");
   private static ImageIcon antHill = new ImageIcon("images/antHill.gif"), antHillFlagged = new ImageIcon("images/antHillFlagged.gif"),
                            antHill2 = new ImageIcon("images/antHill2.gif"), antHill2Flagged = new ImageIcon("images/antHill2Flagged.gif");
   private boolean highlighted, clicked, flagged;     //has the button been highlighted or clicked or had a flag placed on it
   
   public Button(int x, int y, int size)
   {
      shape = new Rectangle(x, y, size, size);
      numAnts = 0;
      title = "FieldCell";
      extraInfo = null;
      regularColor = null;
      highlightColor = null;
      textColor = null;
      color = regularColor;
      regularImage = antImages[numAnts];
      highlightImage = null;
      image = regularImage;
      highlighted = false;
      clicked = false;
      flagged = false;
   }
   
   public Button(Shape s, String t, Color rc, Color hc, Color tc)
   {
      shape = s;
      title = t;
      extraInfo = null;
      regularColor = rc;
      highlightColor = hc;
      textColor = tc;
      color = regularColor;
      regularImage = null;
      highlightImage = null;
      image = null;
      numAnts = 0;
      highlighted = false;
      clicked = false;
      flagged = false;
   }
   
   public Button(Shape s, String t, ImageIcon ri, ImageIcon hi)
   {
      shape = s;
      title = t;
      extraInfo = null;
      regularColor = null;
      highlightColor = null;
      textColor = null;
      regularImage = ri;
      highlightImage = hi;
      image = regularImage;
      numAnts = 0;
      highlighted = false;
      clicked = false;
   }
   
   public boolean isFlagged()
   {
      return flagged;
   }
   
   public void setFlagged(boolean state)
   {
      flagged = state;
   }
   
   public void setClicked(boolean state)
   {
      clicked = state;
      if(GamePanel.getGameMode() == GamePanel.GAME && state && this.getTitle().contains("FieldCell"))
      {
         int buttonX = (int)(this.shape.getBounds().getX());
         int buttonY = (int)(this.shape.getBounds().getY());
         int x = buttonX + (GamePanel.getCellSize()/2);  //location of player crosshair in pixel space
         int y = buttonY + (GamePanel.getCellSize()/2);
         Color smokeColor = Color.orange;
         int numAnts = this.getNumAnts();
         if(numAnts == GamePanel.ANTHILL)
         {
            smokeColor = Color.black;
            Sound.antHill();
         }
         else
         {
            int red = Math.max(0, smokeColor.getRed() - (numAnts*10));
            int green = Math.max(0, smokeColor.getGreen() - (numAnts*10));
            int blue = Math.max(0, smokeColor.getBlue() - (numAnts*10));
            smokeColor = new Color(red, green, blue);
            Sound.click();
         }
         GamePanel.circles.add(new Circle(x, y, 5, smokeColor));
      }
   }
   
   public boolean hasBeenClicked()
   {
      return clicked;
   }
   
   public void setNumAnts(int na)
   {
      numAnts = na;
   }
   
   public int getNumAnts()
   {
      return numAnts;
   }
   
   public Shape getShape()
   {
      return shape;
   }
   
   public String getTitle()
   {
      return title;
   }
   
   public void setExtraInfo(String ei)
   {
      extraInfo = ei;
   }
   
   public String getExtraInfo()
   {
      return extraInfo;
   }
   
   public Color getColor()
   {
      return color;
   }
   
   public Color getRegularColor()
   {
      return regularColor;
   }
   
   public Color getHighlightColor()
   {
      return highlightColor;
   }
   
   public Color getTextColor()
   {
      return textColor;
   }
   
   public ImageIcon getImageIcon()
   {
      return image;
   }
   
   public ImageIcon getRegularImage()
   {
      return regularImage;
   }

   public ImageIcon getHighlightImage()
   {
      return highlightImage;
   }

/*These methods will be called in the mouseMoved method from the MouseListener interface.  
If the mouseX and mouseY positions are within the Button's shape, 
we can call highlight() which changes the color to the highlightColor*/
   public void highlight()
   {
      if(highlightColor != null)
      {
         color = highlightColor;
      }
      if(highlightImage != null)
      {
         image = highlightImage;
      }
      highlighted = true;
   }
   
   public void unHighlight()
   {
      if(regularColor != null)
      {
         color = regularColor;
      }
      if(regularImage != null)
      {
         image = regularImage;
      }
      highlighted = false;
   }
   
   public void drawButton(Graphics g)
   {
      int x = (int)(this.getShape().getBounds().getX());
      int y = (int)(this.getShape().getBounds().getY());
      int width = (int)(this.getShape().getBounds().getWidth());
      int height = (int)(this.getShape().getBounds().getHeight());
      g.setColor(this.getColor());	
      if(image != null)
      {
         if(title.contains("FieldCell"))
         {
            int index = numAnts; 
            if(hasBeenClicked() || GamePanel.getGameMode()==GamePanel.AFTERMATH)      //already clicked on ro reveal all after the game is done
            {
               if(index > 0 && index < antImages.length)
               {
                  if(GamePanel.getStyle() == GamePanel.NUMBER_STYLE && index >= 0 && index < numberImages.length)
                  {
                     image = numberImages[index];
                  }
                  else
                  {
                     image = antImages[index];
                  }
               }
               else  //no ants or anthill
               {
                  if(index == GamePanel.ANTHILL)          //ant hill
                  {
                     if(GamePanel.getGameMode()==GamePanel.AFTERMATH && flagged)
                     {
                        if(GamePanel.getStyle() == GamePanel.NUMBER_STYLE)
                        {
                           image = antHill2Flagged;
                        }
                        else
                        {
                           image = antHillFlagged;
                        }
                     }
                     else if(GamePanel.getStyle() == GamePanel.NUMBER_STYLE)
                     {
                        image = antHill2;
                     }
                     else
                     {
                        image = antHill;
                     }
                  }
               }
            } 
            else     //not clicked on yet - hide the image
            {
               if(flagged)
               {
                  image = flaggedImage;
               }
               else
               {
                  image = unclickedImage;
               }
            }
         }
         g.drawImage(image.getImage(), x, y, width, height, null);  
      }
      else if(this.getShape() instanceof Rectangle)  
      {       
         g.fillRect(x, y, width, height);
      }
      else
      {       
         g.fillOval(x, y, width, height);
      }
      if(image == null)
      {   
         g.setColor(this.getTextColor());	 
         String message = this.getTitle();
         int mWidth = g.getFontMetrics().stringWidth(message);
         int mHeight = y+(height/2);
         if(extraInfo != null)
         {
            mHeight = y+(height/3);
         }
         g.drawString(message, (x+(width/2)) - (mWidth/2), mHeight);       
         if(extraInfo != null)
         {
            message = this.getExtraInfo();
            mWidth = g.getFontMetrics().stringWidth(message);
            g.drawString(message, (x+(width/2)) - (mWidth/2), y+(2*height/3)); 
         }   
      }
      Color outLineColor = Color.black;
      if(this.getColor() != null)
      {
         outLineColor = this.getColor().darker().darker().darker();
      }
      g.setColor(outLineColor);
      if(this.getShape() instanceof Rectangle)   
      {      
         g.drawRect(x, y, width, height);
         if(this.getTitle()!=null && !this.getTitle().contains("FieldCell"))
         {  //extra thick border for options button
            g.setColor(outLineColor);
            g.drawRect(x-1,y-1,width+2, height+2);
            g.setColor(outLineColor.brighter());
            g.drawRect(x-2,y-2,width+4, height+4);
         }
      }
      else
      {       
         g.drawRect(x, y, width, height);
      }
   }
   
   public void setShape(Shape s)
   {
      shape = s;
   }
   
   public void setTitle(String t)
   {
      title = t;
   }
   
   public void setRegularColor(Color rc)
   {
      regularColor = rc;
   }
           
   public void setHighlightColor(Color hc)
   {
      highlightColor = hc;
   }
   
   public void setTextColor(Color tc)
   {
      textColor = tc;
   }
   
   public String toString()
   {
      return ""+Math.abs(numAnts); 
   }
}