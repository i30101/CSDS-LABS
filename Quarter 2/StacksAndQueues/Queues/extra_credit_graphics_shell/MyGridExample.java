import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.Timer;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MyGridExample extends JPanel
{
   private static final int DELAY=100;	//#miliseconds delay between each screen refreshes for the timer

   private Timer t;							//used to set the speed of each time increment in the simulator
   
/*<<<<<<<<<<<<<<<<DEFINE NEEDED GLOBAL DATA FIELDS>>>>>>>>>>>>>>*/
//define 2 queues for lanes of traffic (main & maple)
//define values for delay and prob for each lane (mainDelay, mainProb, mapleDelay, mapleProb)
//private boolean mainGreen (true: green on main & red on maple, false: red on main & green on maple)
//private int frameNum;	count of frames to keep track of time
//define number of cycles
//define counter for number of cycles completed (numCycles)

   public MyGridExample(/*add arguments for delay and prob for each lane and number of cycles*/)
   {
   //create 2 queues for lanes of traffic
   //assign values for delay and prob for each lane  and number of cycles
   //mainGreen = true;
   //frameNum = 0;
   //numCycles = 0;
      t = new Timer(DELAY, new Listener());				//the higher the value of the first argument, the slower the enemy will move
      t.start();     
   }

   //THIS METHOD IS ONLY CALLED THE MOMENT A KEY IS HIT - NOT AT ANY OTHER TIME
	//pre:   k is a valid keyCode
	//post:  exits the program if you hit Q or Esc
   public void processUserInput(int k)
   {
      if(k==KeyEvent.VK_Q || k==KeyEvent.VK_ESCAPE)					//End the program	
         System.exit(1);
      repaint();			//refresh the screen
   }

   /*<<<<<<<<<<<<<<<<HERE IS WHERE YOU DRAW THE GRAPHICS>>>>>>>>>>>>>>*/
   /*
   The method paintComponent is called every time the repaint() method is called
   Look up the Java Graphics API here:https://docs.oracle.com/javase/7/docs/api/java/awt/Graphics.html
   You will find ways of changing colors, drawing shapes and writing text in graphics
   */
   public void paintComponent(Graphics g)
   {
      super.paintComponent(g); 
      g.setColor(Color.blue);		   //draw a blue rectangle
      g.fillRect(0, 0, 800, 800);   //x, y, width, height
      //draw maple from origin to the right
   	//draw main from origin down
   }
   
   private class Listener implements ActionListener
   {
   /*<<<<<<<<<<<<<<<<MAIN PROGRAM LOGIC GOES HERE>>>>>>>>>>>>>>*/
      public void actionPerformed(ActionEvent e)	//this is called for each timer iteration - traffic mechanics
      {
        /*
      if(numCycles completed >= cycles)
      {
      show stats (results);
      System.exit(1);
      }
      if(mainGreen)
      {
      if(frameNum >= mainDelay)
      {
         frameNum = 0;
      	mainGreen = false;
      }
      else
      {
      maybe add a car to main and/or maple
      if main has cars
          remove a car from main
      frameNum++;	 
      }
      }
      else	//main is red and maple is green
      {
      if(frameNum >= mapleDelay)
      {
         frameNum = 0;
      	mainGreen = true;
      }
      else
      {
      maybe add a car to main and/or maple
      if main has cars
          remove a car from maple
       frameNum++;	 	 
      }
      }
      */
         repaint();
      }
   }

}
