//MONSTER MAKER UTILITIES  
import javax.swing.ImageIcon;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.PrintStream;
import java.io.FileOutputStream;

//handles building of custom monsters
public class MonsterMaker extends MMMPanel
{
   private static final String[] projectiles = {"BEAM", "FIRE", "SHRIEK", "WEB"};
   private static int projectileIndex = 0;	         //to traverse through projectile types
   private static int scrollValue = -1;  		         //to allow keyboard scrolling of monster options
   protected static String monsterName=null;				//stores the name of the custom monster
   protected static int monsterMoney=1000;				//stores the amount of points to add monster attributes
   private static final int NUM_CUSTOM_ELEMENTS = 27;
   private static int [] customTextColor = new int[NUM_CUSTOM_ELEMENTS];

//pre:  0<=highlightArea<customInfo.length
//post: changes text color brighter given a specific highlight area (num)
   public static void setTextColor2(Graphics g, int num)
   {
      if(highlightArea==num)
         g.setColor(Color.yellow.brighter());
      else
         g.setColor(Color.yellow.darker());
   }

//pre:  0<=monsterNum<playerImages.length
//post: returns true if this monster type has images to allow it to rotate its head 360 degrees
   public static boolean canRotateHead360(int monsterNum)
   {  //    Robot           Blop              Worm            UFO               FSM
      if(monsterNum==2 || monsterNum==4 || monsterNum==5 || monsterNum==6 || monsterNum==8)
         return true;
      return false;
   }
   
//pre:  0<=monsterNum<playerImages.length
//post: returns true if this monster type can fly
   public static boolean canFly(int monsterNum)
   {  //    Mantis         Blop               UFO               FSM
      if(monsterNum==3 || monsterNum==4 || monsterNum==6 || monsterNum==8)
         return true;
      return false;
   }

//pre:  0<=highlightArea<customInfo.length
//post: changes text color brighter given a specific highlight area (num), conflicting options as red
   public static void setTextColor(Graphics g, int num)
   {
      for(int i=0; i<customTextColor.length; i++)
         customTextColor[i] = 0;
   
      if(num == highlightArea)
         customTextColor[num] = 1;
      if(highlightArea==8)         
      {                             //soft hide conflicts with
         customTextColor[10] = 2;   //        bullet deflection
      }
      else if(highlightArea==10)    
      {                             //bullet deflection conflicts with
         customTextColor[8] = 2;    //        soft hide
      }
      else if(highlightArea==1)     //monster body type could conflict with head 360 turn ability
      {                             
         int bodyType = Integer.parseInt(customInfo[1]);
         if(!canRotateHead360(bodyType))		//only the robot, worm, blop and ufo gets 360 head turn option
            customTextColor[11] = 2;         //360 head    
         if(!canFly(bodyType))               //only mantis, blop and UFO can fly
            customTextColor[15] = 2;         //flyer   
      }
      else if(highlightArea==15)    
      {                             //flyer conflicts with
         customTextColor[16] = 2;   //        digger
      }
      else if(highlightArea==16)    
      {                             //digger conflicts with
         customTextColor[15] = 2;   //        flyer   
         customTextColor[12] = 2;   //        swimmer   
         customTextColor[13] = 2;   //        water healer
      }
      else if(highlightArea==12 || highlightArea==13)    
      {                             //swimmer and water healer conflict with
         customTextColor[14] = 2;   //        drowner   
         customTextColor[22] = 2;   //        and energy absorber
         customTextColor[16] = 2;   //        digger
      }
      else if(highlightArea==14)    
      {                             //drowner conflicts with
         customTextColor[12] = 2;   //        swimmer   
         customTextColor[13] = 2;   //        water healer
      }
      else if(highlightArea==9 || highlightArea==23)    
      {                             //lower appetite and eat all conflict with 
         customTextColor[22] = 2;   //        energy absorber
      }
      else if(highlightArea==17)    
      {                             //thrower conflicts with
         customTextColor[19] = 2;   //        shooter   
         customTextColor[20] = 2;   //        splitter
         customTextColor[22] = 2;   //        energy absorber
         customTextColor[24] = 2;   //        mind control
      }
      else if(highlightArea==18)
      {                             //ambidextorous conflicts with
         customTextColor[22] = 2;   //        energy absorber
         customTextColor[25] = 2;   //        one that can't grab
      }
      
      else if(highlightArea==19)    
      {                             //shooter conflicts with
         customTextColor[17] = 2;   //        thrower   
         customTextColor[20] = 2;   //        splitter
         customTextColor[24] = 2;   //        mind control
      }
      else if(highlightArea==20)    
      {                             //splitter conflicts with
         customTextColor[17] = 2;   //        thrower   
         customTextColor[19] = 2;   //        shooter
         customTextColor[24] = 2;   //        mind control
      }
      else if(highlightArea==22)    
      {                             //energy absorber conflicts with
         customTextColor[17] = 2;   //        thrower   
         customTextColor[9] = 2;    //        lower appetite
         customTextColor[18] = 2;   //        ambidextorous
         customTextColor[12] = 2;   //        swimmer   
         customTextColor[13] = 2;   //        water healer
         customTextColor[23] = 2;   //        eat all units
         customTextColor[25] = 2;   //        one that can't grab
      }
      else if(highlightArea==24)    
      {                             //mind control conflicts with
         customTextColor[17] = 2;   //        thrower   
         customTextColor[19] = 2;   //        shooter
         customTextColor[20] = 2;   //        splitter
      }
      else if(highlightArea==25)    
      {                             //can't grab conflicts with
         customTextColor[18] = 2;   //        ambidextorous
         customTextColor[22] = 2;   //        energy absorber
      }
   
      if(customTextColor[num] == 2)
         g.setColor(Color.red.darker().darker());
      else             
         if(customTextColor[num] == 1)
            g.setColor(Color.yellow.brighter());
         else
            g.setColor(Color.yellow.darker());
   }

//post: computes monster money used/left with chosen attributes and stats
   public static int computeMoney()
   {
      int money = 1000;
      int stomp = Integer.parseInt(customInfo[3]);			//-
      int speed = Integer.parseInt(customInfo[4]);			//+
      int reload = Integer.parseInt(customInfo[5]);		//+
      int autostomp = Integer.parseInt(customInfo[6]);	//-
      String projectile = customInfo[7];
      int burn = Integer.parseInt(customInfo[8]);			//+
      boolean head360 = (Boolean.parseBoolean(customInfo[9]));				//-
      boolean isSwimmer = (Boolean.parseBoolean(customInfo[10]));			//-
      boolean isFlyer =	(Boolean.parseBoolean(customInfo[11]));			//-
      boolean isDigger =(Boolean.parseBoolean(customInfo[12]));			//-
      boolean isThrower = (Boolean.parseBoolean(customInfo[13]));			//-
      boolean isShooter = (Boolean.parseBoolean(customInfo[14]));			//-
      boolean canSplit = (Boolean.parseBoolean(customInfo[15]));			//-
      boolean healInWater = (Boolean.parseBoolean(customInfo[16]));		//-
      boolean isAmbidextorous = (Boolean.parseBoolean(customInfo[17]));	//-
      boolean damageInWater = (Boolean.parseBoolean(customInfo[18]));	//+
      boolean lessAppetite = (Boolean.parseBoolean(customInfo[19]));		//-
      boolean imperviousToBullets = (Boolean.parseBoolean(customInfo[20]));	//-
      boolean isFragile = (Boolean.parseBoolean(customInfo[21]));			//+
      boolean slimeTrail = (Boolean.parseBoolean(customInfo[22]));		//-
      boolean energyAbsorber = (Boolean.parseBoolean(customInfo[23]));	//-
      boolean canEatAll = (Boolean.parseBoolean(customInfo[24]));			//-
      boolean mindControl = (Boolean.parseBoolean(customInfo[25]));		//-
      boolean cantGrab = (Boolean.parseBoolean(customInfo[26]));		   //+
      money -= (stomp*3 + autostomp*2);//stomp:20-200, autostomp: 5-90
      money += (burn*2 + speed*100);	//burn:0-100, speed 0-2
      if(isShooter)
      {
         money -= 350;
         if(projectile.equals("BEAM"))
            money -= 200;
         else if(projectile.equals("FIRE"))
            money -= 250;
         else if(projectile.equals("WEB"))
            money -= 100;
         else if(projectile.equals("SHRIEK"))
            money -= 100;
         money += reload;		//20-200   
      }
      if(cantGrab)
         money += 100;
      if(damageInWater)
         money += 100;
      if(isFragile)
         money += 150;
      if(head360)
         money -= 100;
      if(isSwimmer)
         money -= 150;
      if(isFlyer)
         money -= 500;
      if(isDigger)
         money -= 450;	//making us a digger sets drowner to true, so the cost is increased by 100 to offset the gain of 100 by being a drowner
      if(isThrower)
         money -= 250;
      if(canSplit)
         money -= 500;
      if(healInWater)
         money -= 200;
      if(isAmbidextorous)
         money -= 150;
      if(lessAppetite)
         money -= 100;
      if(imperviousToBullets)
         money -= 350;
      if(slimeTrail)
         money -= 50;
      if(energyAbsorber)
         money += 50;
      if(canEatAll)
         money -= 50;
      if(mindControl)
         money -= 400;
      if(customInfo[0].startsWith("TROGDOR") && projectile.equals("FIRE") && customInfo[1].equals("5"))
         money += 1000;   //cheat code for the burninator
      if(customInfo[0].startsWith("FSM") && mindControl && customInfo[1].equals("8"))
         money += 1000;   //cheat code for the burninator
      return money;
   }

//pre:  option is an element from customInfo array that is "true" or "false"
//post: returns "ON" if option is selected, empty string otherwise
   public static String displayIfTrue(String option)
   {
      if(option.equals("true"))
         return "ON";
      return "";
   }

//pre:   g != null
//post:  allows the player to select a monster before the game starts
   public static void monsterLab(Graphics g)
   {
      ImageDisplay.showAndRotateMonster(g, Integer.parseInt(customInfo[1]));
   
      int size = (int)(cellSize/2);
      int x = cellSize + size;
      int y = cellSize + size;
      
      g.setFont(new Font("Monospaced", Font.BOLD, size));
      setTextColor(g, 0);
      g.drawString("NAME            :"+customInfo[0], x, y+=size);
      setTextColor(g, 1);
      g.drawString("BODY TYPE       :"+customInfo[1], x, y+=size);
      setTextColor(g, 2);
      g.drawString("SPEED PENALTY   :"+customInfo[4], x, y+=size);
      setTextColor(g, 3);
      g.drawString("STOMP POWER     :"+customInfo[3], x, y+=size);
      setTextColor(g, 4);
      g.drawString("WALK DAMAGE     :"+customInfo[6], x, y+=size);
      setTextColor(g, 5);
      g.drawString("RELOAD TIME     :"+customInfo[5], x, y+=size);
      setTextColor(g, 6);
      if(Boolean.parseBoolean(customInfo[14]))	//shooter
         g.drawString("PROJECTILE      :"+customInfo[7], x, y+=size);
      else
         g.drawString("PROJECTILE      :NONE", x, y+=size);
      setTextColor(g, 7);
      g.drawString("BURN DAMAGE     :"+customInfo[8], x, y+=size);
      setTextColor(g, 8);
      g.drawString("SOFT HIDE       :"+displayIfTrue(customInfo[21]), x, y+=size);
      setTextColor(g, 9);
      g.drawString("LOWER APPETITE  :"+displayIfTrue(customInfo[19]), x, y+=size);
      setTextColor(g, 10);
      g.drawString("BULLET DEFLECTOR:"+displayIfTrue(customInfo[20]), x, y+=size);
      setTextColor(g, 11);
      g.drawString("TURN HEAD 360   :"+displayIfTrue(customInfo[9]), x, y+=size);
      setTextColor(g, 12);
      g.drawString("SWIMMER         :"+displayIfTrue(customInfo[10]), x, y+=size);
      setTextColor(g, 13);
      g.drawString("HEAL IN WATER   :"+displayIfTrue(customInfo[16]), x, y+=size);
      setTextColor(g, 14);
      g.drawString("DROWN IN WATER  :"+displayIfTrue(customInfo[18]), x, y+=size);
      setTextColor(g, 15);
      g.drawString("FLYER           :"+displayIfTrue(customInfo[11]), x, y+=size);
      setTextColor(g, 16);
      g.drawString("DIGGER          :"+displayIfTrue(customInfo[12]), x, y+=size);
      setTextColor(g, 17);
      g.drawString("THROWER         :"+displayIfTrue(customInfo[13]), x, y+=size);
      setTextColor(g, 18);
      g.drawString("AMBIDEXTROUS    :"+displayIfTrue(customInfo[17]), x, y+=size);
      setTextColor(g, 19);
      g.drawString("SHOOTER         :"+displayIfTrue(customInfo[14]), x, y+=size);
      setTextColor(g, 20);
      g.drawString("SPLITTER        :"+displayIfTrue(customInfo[15]), x, y+=size);
      setTextColor(g, 21);
      g.drawString("TOXIC TRAIL     :"+displayIfTrue(customInfo[22]), x, y+=size);
      setTextColor(g, 22);
      g.drawString("ENERGY ABSORBER :"+displayIfTrue(customInfo[23]), x, y+=size);
      setTextColor(g, 23);
      g.drawString("CAN EAT ALL     :"+displayIfTrue(customInfo[24]), x, y+=size);
      setTextColor(g, 24);
      g.drawString("MIND CONTROL    :"+displayIfTrue(customInfo[25]), x, y+=size);
      setTextColor(g, 25);
      g.drawString("CAN'T GRAB      :"+displayIfTrue(customInfo[26]), x, y+=size);
   
      y += (size*2);
      g.setColor(Color.yellow);
      monsterMoney = computeMoney();
      if(monsterMoney == 0)
         g.setColor(Color.white);
      else if(monsterMoney < 0)
         g.setColor(Color.red);
   
      g.drawString("MONSTER MONEY :"+monsterMoney, x, y+=size);
   
   //draw the mouse pointer
      g.drawImage(cursors[4].getImage(), mouseX, mouseY, cellSize/2, cellSize/2, null);  //scaled image
   
   }

   protected static void changeAttribute(int button)
   {
      if(highlightArea==0)
      {//enter monster name
         textMode = true;
      }
      else if(highlightArea==1)
      {//change monster body type
         int newType = Integer.parseInt(customInfo[1]);
         if(button == MouseEvent.BUTTON1)
            newType--;
         else
            newType++;
         if(newType >= playerImages.length)
            newType = 0;
         else if(newType < 0)
            newType = playerImages.length - 1;   
         customInfo[1] = ""+newType;
      //adjust animation speed for certain models
         int animSpeed =  animation_delay; 
         if(newType == 1 || newType == 2)	//Dinosaur or Robot
            animSpeed = (int)(animation_delay*1.5);
         else if (newType == 5)				//Worm
            animSpeed = (int)(animation_delay/2);
         if(!canRotateHead360(newType) && Boolean.parseBoolean(customInfo[9])==true)    			 
            customInfo[9] = "false";	//only some gets the 360 degree head turn
         if(!canFly(newType) && Boolean.parseBoolean(customInfo[11])==true)    			 
            customInfo[11] = "false";	//only some can fly
         customInfo[2] = ""+animSpeed;
      }
      else if(highlightArea==2)
      {//speed penalty
         int newSpeed = Integer.parseInt(customInfo[4]);
         if(button == MouseEvent.BUTTON1)
            newSpeed--;
         else
            newSpeed++;
         if(newSpeed > 2)
            newSpeed = 0;
         else if (newSpeed < 0)
            newSpeed = 2;   
         customInfo[4] = ""+newSpeed;
      }
      else if(highlightArea==3)
      {//stomp power 20-200
         int stomp = Integer.parseInt(customInfo[3]);
         if(button == MouseEvent.BUTTON1)
            stomp-=10;
         else
            stomp+=10;
         if(stomp < 20)
            stomp = 20;
         else if(stomp > 200)
            stomp = 200;
         customInfo[3] = ""+stomp;
      }
      else if(highlightArea==4)
      {//walk damage 5-90
         int walk = Integer.parseInt(customInfo[6]);
         if(button == MouseEvent.BUTTON1)
            walk-=5;
         else
            walk+=5;
         if(walk < 5)
            walk = 5;
         else if(walk > 90)
            walk = 90;
         customInfo[6] = ""+walk;
      }
      else if(highlightArea==5)
      {//reload time 20-200
         int rtime = Integer.parseInt(customInfo[5]);
         if(button == MouseEvent.BUTTON1)
            rtime-=10;
         else
            rtime+=10;
         if(rtime < 20)
            rtime = 20;
         else if(rtime > 200)
            rtime = 200;
         customInfo[5] = ""+rtime;
      }
      else if(highlightArea==6)
      {//projectile type
         String currProj = customInfo[7];
         projectileIndex = -1;
         for(int i=0; i<projectiles.length; i++)
            if(currProj.equals(projectiles[i]))
               projectileIndex = i;
         if(projectileIndex == -1)
            projectileIndex = 0; 
         if(button == MouseEvent.BUTTON1)     
            projectileIndex = (projectileIndex+1) % projectiles.length;
         else
         {
            projectileIndex = (projectileIndex-1);
            if(projectileIndex < 0)
               projectileIndex = projectiles.length - 1;
         }
         customInfo[7] = projectiles[projectileIndex];     
      }
      else if(highlightArea==7)
      {//burn damage
         int burn = Integer.parseInt(customInfo[8]);
         if(button == MouseEvent.BUTTON1)
            burn-=5;
         else
            burn+=5;
         if(burn < 0)
            burn = 0;
         else if(burn > 100)
            burn = 100;
         customInfo[8] = ""+burn;
      }
      else if(highlightArea==8)
      {//soft hide
         boolean option = Boolean.parseBoolean(customInfo[21]);
         option = !option;
         customInfo[21] = ""+option;
         if(option == true && Boolean.parseBoolean(customInfo[20]) == true)
            customInfo[20] = "false";
      }
      else if(highlightArea==9)
      {//lower appetite
         boolean option = Boolean.parseBoolean(customInfo[19]);
         option = !option;
         customInfo[19] = ""+option;
         if(option == true && Boolean.parseBoolean(customInfo[23]) == true)
            customInfo[23] = "false";	//can't be energy absorber
      }
      else if(highlightArea==10)
      {//bullet deflection
         boolean option = Boolean.parseBoolean(customInfo[20]);
         option = !option;
         customInfo[20] = ""+option;
         if(option == true && Boolean.parseBoolean(customInfo[21]) == true)
            customInfo[21] = "false";	//can't have soft hide
      }
      else if(highlightArea==11)
      {//360 head
         int bodyType = Integer.parseInt(customInfo[1]);
         if(canRotateHead360(bodyType))		//only the robot, blop, worm and ufo gets 360 head turn option
         {
            boolean option = Boolean.parseBoolean(customInfo[9]);
            option = !option;
            customInfo[9] = ""+option;
         }
         else
            customInfo[9] = "false";
      }
      else if(highlightArea==12)
      {//swimmer
         boolean option = Boolean.parseBoolean(customInfo[10]);
         option = !option;
         customInfo[10] = ""+option;
         if(option == true && Boolean.parseBoolean(customInfo[23]) == true)
            customInfo[23] = "false";	//can't be energy absorber
         if(option == true && Boolean.parseBoolean(customInfo[18]) == true)
            customInfo[18] = "false";	//can't be drowner
         if(option == true && Boolean.parseBoolean(customInfo[12]) == true)
            customInfo[12] = "false";	//can't be digger
      }
      else if(highlightArea==13)
      {//water healer
         boolean option = Boolean.parseBoolean(customInfo[16]);
         option = !option;
         customInfo[16] = ""+option;
         if(option == true && Boolean.parseBoolean(customInfo[23]) == true)
            customInfo[23] = "false";	//can't be energy absorber
         if(option == true && Boolean.parseBoolean(customInfo[18]) == true)
            customInfo[18] = "false";	//can't be drowner
         if(option == true && Boolean.parseBoolean(customInfo[12]) == true)
            customInfo[12] = "false";	//can't be digger
      }
      else if(highlightArea==14)
      {//drowner
         boolean option = Boolean.parseBoolean(customInfo[18]);
         option = !option;
         customInfo[18] = ""+option;
         if(option == true && Boolean.parseBoolean(customInfo[10]) == true)
            customInfo[10] = "false";	//can't be swimmer
         if(option == true && Boolean.parseBoolean(customInfo[16]) == true)
            customInfo[16] = "false";	//can't be water healer
      }
      else if(highlightArea==15)
      {//flyer
         boolean option = Boolean.parseBoolean(customInfo[11]);
         int bodyType = Integer.parseInt(customInfo[1]);
         if(canFly(bodyType))		//only the robot, blop, worm and ufo gets 360 head turn option
         {
            option = !option;
            customInfo[11] = ""+option;
            if(option == true && Boolean.parseBoolean(customInfo[12]) == true)
               customInfo[12] = "false";	//can't be digger
         }
         else
            customInfo[11] = "false";
      }
      else if(highlightArea==16)
      {//digger
         boolean option = Boolean.parseBoolean(customInfo[12]);
         option = !option;
         customInfo[12] = ""+option;
         if(option == true && Boolean.parseBoolean(customInfo[11]) == true)
            customInfo[11] = "false";	//can't be flyer
         if(option == true && Boolean.parseBoolean(customInfo[10]) == true)
            customInfo[10] = "false";	//can't be swimmer
         if(option == true && Boolean.parseBoolean(customInfo[16]) == true)
            customInfo[16] = "false";	//can't be water healer
         if(option == true && Boolean.parseBoolean(customInfo[18]) == false)
            customInfo[18] = "true";	//MUST be drowner
      }
      else if(highlightArea==17)
      {//thrower
         boolean option = Boolean.parseBoolean(customInfo[13]);
         option = !option;
         customInfo[13] = ""+option;
         if(option == true && Boolean.parseBoolean(customInfo[14]) == true)
            customInfo[14] = "false";	//can't be shooter
         if(option == true && Boolean.parseBoolean(customInfo[15]) == true)
            customInfo[15] = "false";	//can't be splitter
         if(option == true && Boolean.parseBoolean(customInfo[23]) == true)
            customInfo[23] = "false";	//can't be energy absorber
         customInfo[7] = "CAR"; 
      }
      else if(highlightArea==18)
      {//ambidextorous
         boolean option = Boolean.parseBoolean(customInfo[17]);
         option = !option;
         customInfo[17] = ""+option;
         if(option == true && Boolean.parseBoolean(customInfo[23]) == true)
            customInfo[23] = "false";	//can't be energy absorber
         if(option == true && Boolean.parseBoolean(customInfo[26]) == true)
            customInfo[26] = "false";	//cant be one that can't grab anything
      }
      else if(highlightArea==19)
      {//shooter
         boolean option = Boolean.parseBoolean(customInfo[14]);
         option = !option;
         customInfo[14] = ""+option;
         if(option == true && Boolean.parseBoolean(customInfo[13]) == true)
            customInfo[13] = "false";	//can't be thrower
         if(option == true && Boolean.parseBoolean(customInfo[15]) == true)
            customInfo[15] = "false";	//can't be splitter
         if(option == true && Boolean.parseBoolean(customInfo[25]) == true)
            customInfo[25] = "false";	//can't be mind control  
         String currProj = customInfo[7];
         projectileIndex = -1;
         for(int i=0; i<projectiles.length; i++)
            if(currProj.equals(projectiles[i]))
               projectileIndex = i;
         if(projectileIndex == -1)
            projectileIndex = 0;      
         customInfo[7] = projectiles[projectileIndex];
      }
      else if(highlightArea==20)
      {//splitter
         boolean option = Boolean.parseBoolean(customInfo[15]);
         option = !option;
         customInfo[15] = ""+option;
         if(option == true && Boolean.parseBoolean(customInfo[13]) == true)
            customInfo[13] = "false";	//can't be thrower
         if(option == true && Boolean.parseBoolean(customInfo[14]) == true)
            customInfo[14] = "false";	//can't be shooter
         if(option == true && Boolean.parseBoolean(customInfo[25]) == true)
            customInfo[25] = "false";	//can't be mind control  
      }
      else if(highlightArea==21)
      {//toxic trail
         boolean option = Boolean.parseBoolean(customInfo[22]);
         option = !option;
         customInfo[22] = ""+option;
      }
      else if(highlightArea==22)
      {//energy absorber
         boolean option = Boolean.parseBoolean(customInfo[23]);
         option = !option;
         customInfo[23] = ""+option;
         if(option == true && Boolean.parseBoolean(customInfo[19]) == true)
            customInfo[19] = "false";  //can't be lower appetite
         if(option == true && Boolean.parseBoolean(customInfo[13]) == true)
            customInfo[13] = "false";	//cant be thrower
         if(option == true && Boolean.parseBoolean(customInfo[17]) == true)
            customInfo[17] = "false";	//cant be ambidextorous
         if(option == true && Boolean.parseBoolean(customInfo[10]) == true)
            customInfo[10] = "false";	//cant be swimmer
         if(option == true && Boolean.parseBoolean(customInfo[16]) == true)
            customInfo[16] = "false";	//cant be water healer
         if(option == true && Boolean.parseBoolean(customInfo[24]) == true)
            customInfo[24] = "false";	//can't be able to eat all units
         if(option == true && Boolean.parseBoolean(customInfo[26]) == true)
            customInfo[26] = "false";	//cant be one that can't grab anything
      }
      else if(highlightArea==23)
      {//canEatAll
         boolean option = Boolean.parseBoolean(customInfo[24]);
         option = !option;
         customInfo[24] = ""+option;
         if(option == true && Boolean.parseBoolean(customInfo[23]) == true)
            customInfo[23] = "false";	//can't be energy absorber
      }
      else if(highlightArea==24)
      {//mindControl
         boolean option = Boolean.parseBoolean(customInfo[25]);
         option = !option;
         customInfo[25] = ""+option;
         customInfo[5] = "200";
         if(option == true && Boolean.parseBoolean(customInfo[13]) == true)
            customInfo[13] = "false";	//can't be thrower
         if(option == true && Boolean.parseBoolean(customInfo[15]) == true)
            customInfo[15] = "false";	//can't be splitter
         if(option == true && Boolean.parseBoolean(customInfo[14]) == true)
            customInfo[14] = "false";	//can't be shooter  
      }
      else if(highlightArea==25)
      {//can't grab units - automatically eat them
         boolean option = Boolean.parseBoolean(customInfo[26]);
         option = !option;
         customInfo[26] = ""+option;
         if(option == true && Boolean.parseBoolean(customInfo[17]) == true)
            customInfo[17] = "false";	//cant be ambidextorous
         if(option == true && Boolean.parseBoolean(customInfo[23]) == true)
            customInfo[23] = "false";	//can't be energy absorber
      }
   
   }

//pre: g!=null, x>=0, y >=0, size > 0
//post:displays monster attribute narratives depending on which feature is highlighted (as opposed to showing key commands)
   private static int showAttributes(Graphics g, int x, int y, int size)
   {
      y = size*24;
      String cost = "";
      g.setFont(new Font("Monospaced", Font.BOLD, (int)(size*.75)));
      g.setColor(Color.orange);
   
      if(highlightArea==0)
      {
         g.drawString("Your monster's name       ", x, y+=((int)(size*.75)));
         g.drawString("must be 10 characters or  ", x, y+=((int)(size*.75)));
         g.drawString("less.  Make it clever.    ", x, y+=((int)(size*.75)));
         y+=((int)(size*.75)*12);
         cost = "0";
      }
      else if(highlightArea==1)
      {
         g.drawString("Monster body type (0-7)   ", x, y+=((int)(size*.75)));
         g.drawString("0) Giant Gorilla          ", x, y+=((int)(size*.75)));
         g.drawString("1) Dinosaur / Dragon      ", x, y+=((int)(size*.75)));
         g.drawString("2) Robot / BattleMech     ", x, y+=((int)(size*.75)));
         g.drawString("3) Giant Insect / Mantis  ", x, y+=((int)(size*.75)));
         g.drawString("4) Gellatinous Mass       ", x, y+=((int)(size*.75)));
         g.drawString("5) Serpent / Worm         ", x, y+=((int)(size*.75)));
         g.drawString("6) Flying Saucer / UFO    ", x, y+=((int)(size*.75)));
         g.drawString("7) Hairy Beast            ", x, y+=((int)(size*.75)));
         y+=((int)(size*.75)*6);
         cost = "0";
      }
      else if(highlightArea==2)
      {
         g.drawString("Speed penalty is weighed  ", x, y+=((int)(size*.75)));
         g.drawString("against how fast you walk.", x, y+=((int)(size*.75)));
         g.drawString("The higher the value, the ", x, y+=((int)(size*.75)));
         g.drawString("slower you lumber around  ", x, y+=((int)(size*.75)));
         g.drawString("the city.  Values range   ", x, y+=((int)(size*.75)));
         g.drawString("from 0 (fast) to 2 (slow).", x, y+=((int)(size*.75)));
         y+=((int)(size*.75)*9);
         cost = "+100 / point";
      }
      else if(highlightArea==3)
      {
         g.drawString("Stomp power quantifies how", x, y+=((int)(size*.75)));
         g.drawString("much damage you do to a   ", x, y+=((int)(size*.75)));
         g.drawString("structure when you jump on", x, y+=((int)(size*.75)));
         g.drawString("it.  The value range is   ", x, y+=((int)(size*.75)));
         g.drawString("from 20 to 200.           ", x, y+=((int)(size*.75)));
         y+=((int)(size*.75)*10);
         cost = "-3 / point";
      }
      else if(highlightArea==4)
      {
         g.drawString("Walk damage describes how ", x, y+=((int)(size*.75)));
         g.drawString("much damage you inflict on" , x, y+=((int)(size*.75)));
         g.drawString("structures just by walking", x, y+=((int)(size*.75)));
         g.drawString("over them.  The value     ", x, y+=((int)(size*.75)));
         g.drawString("range is from 5 to 90.    ", x, y+=((int)(size*.75)));
         g.drawString("Walk damage should be less", x, y+=((int)(size*.75)));
         g.drawString("than stomp power.         ", x, y+=((int)(size*.75)));
         y+=((int)(size*.75)*8);
         cost = "-2 / point";
      }
      else if(highlightArea==5)
      {
         g.drawString("Reload time determines how", x, y+=((int)(size*.75)));
         g.drawString("much time is required to  " , x, y+=((int)(size*.75)));
         g.drawString("rest between shooting,    ", x, y+=((int)(size*.75)));
         g.drawString("breathing or belching a   ", x, y+=((int)(size*.75)));
         g.drawString("projectile attack.        ", x, y+=((int)(size*.75)));
         g.drawString("Value range is from 20-200", x, y+=((int)(size*.75)));
         y+=((int)(size*.75)*9);
         cost = "+1 / point";
      }
      else if(highlightArea==6)
      {
         g.drawString("Projectile type determines", x, y+=((int)(size*.75)));
         g.drawString("the kind of ordinace that " , x, y+=((int)(size*.75)));
         g.drawString("your monster shoots.      ", x, y+=((int)(size*.75)));
         g.drawString("BEAM: laser/death ray -200", x, y+=((int)(size*.75)));
         g.drawString("FIRE: burns structures-250", x, y+=((int)(size*.75)));
         g.drawString("SHRIEK: stuns units   -100", x, y+=((int)(size*.75)));
         g.drawString("WEB:traps units       -100", x, y+=((int)(size*.75)));
         y+=((int)(size*.75)*8);
         cost = "-100 to -250";
      }
      else if(highlightArea==7)
      {
         g.drawString("Burn damage represents the", x, y+=((int)(size*.75)));
         g.drawString("number of health points   " , x, y+=((int)(size*.75)));
         g.drawString("lost by standing in fire  ", x, y+=((int)(size*.75)));
         g.drawString("(or a structure that is   ", x, y+=((int)(size*.75)));
         g.drawString("burning.                  ", x, y+=((int)(size*.75)));
         y+=((int)(size*.75)*10);
         cost = "+2 / point";
      }
      else if(highlightArea==8)
      {
         g.drawString("If you have a soft hide,  ", x, y+=((int)(size*.75)));
         g.drawString("you take 75% more damage  " , x, y+=((int)(size*.75)));
         g.drawString("from bullets and shells.  ", x, y+=((int)(size*.75)));
         g.drawString("You can not have both a   ", x, y+=((int)(size*.75)));
         g.drawString("soft hide and bullet      ", x, y+=((int)(size*.75)));
         g.drawString("deflection.               ", x, y+=((int)(size*.75)));
         y+=((int)(size*.75)*9);
         cost = "+150";
      }
      else if(highlightArea==9)
      {
         g.drawString("With a lower appetite,    ", x, y+=((int)(size*.75)));
         g.drawString("you requre food less often" , x, y+=((int)(size*.75)));
         g.drawString("than your monster peers.  ", x, y+=((int)(size*.75)));
         g.drawString("The rate of hunger is half", x, y+=((int)(size*.75)));
         g.drawString("that of other monsters.   ", x, y+=((int)(size*.75)));
         g.drawString("You can not have a lower  ", x, y+=((int)(size*.75)));
         g.drawString("appetite and be an energy ", x, y+=((int)(size*.75)));
         g.drawString("absorber (they don't eat).", x, y+=((int)(size*.75)));
         y+=((int)(size*.75)*7);
         cost = "-100";
      }
      else if(highlightArea==10)
      {
         g.drawString("A bullet deflector (or in ", x, y+=((int)(size*.75)));
         g.drawString("the case of the blop, a   " , x, y+=((int)(size*.75)));
         g.drawString("bullet absorber) takes no ", x, y+=((int)(size*.75)));
         g.drawString("damage from ordinance     ", x, y+=((int)(size*.75)));
         g.drawString("fired from guns, but twice", x, y+=((int)(size*.75)));
         g.drawString("the damage from fire and  ", x, y+=((int)(size*.75)));
         g.drawString("explosives.  You can not  ", x, y+=((int)(size*.75)));
         g.drawString("have this option as well  ", x, y+=((int)(size*.75)));
         g.drawString("as a soft hide.           ", x, y+=((int)(size*.75)));
         y+=((int)(size*.75)*6);
         cost = "-350";
      }
      else if(highlightArea==11)
      {
         g.drawString("With this option, your    ", x, y+=((int)(size*.75)));
         g.drawString("creation can turn their   " , x, y+=((int)(size*.75)));
         g.drawString("head 360 degrees.  This is", x, y+=((int)(size*.75)));
         g.drawString("the most useful if you can", x, y+=((int)(size*.75)));
         g.drawString("shoot a projectile attack,", x, y+=((int)(size*.75)));
         g.drawString("but pretty much useless if", x, y+=((int)(size*.75)));
         g.drawString("you can not.  It is only  ", x, y+=((int)(size*.75)));
         g.drawString("available to the Robot,   ", x, y+=((int)(size*.75)));
         g.drawString("Blop, Worm and UFO.       ", x, y+=((int)(size*.75)));
         y+=((int)(size*.75)*7);
         cost = "-100";
      }
      else if(highlightArea==12)
      {
         g.drawString("A swimmer can move faster ", x, y+=((int)(size*.75)));
         g.drawString("when in water.  You can   " , x, y+=((int)(size*.75)));
         g.drawString("not be a swimmer and a    ", x, y+=((int)(size*.75)));
         g.drawString("monster that drowns, nor  ", x, y+=((int)(size*.75)));
         g.drawString("one that absorbs energy.  ", x, y+=((int)(size*.75)));
         g.drawString("Swimming is most useful   ", x, y+=((int)(size*.75)));
         g.drawString("if paired with the ability", x, y+=((int)(size*.75)));
         g.drawString("to heal in water.         ", x, y+=((int)(size*.75)));
         y+=((int)(size*.75)*7);
         cost = "-150";
      }
      else if(highlightArea==13)
      {
         g.drawString("Healing in water is great ", x, y+=((int)(size*.75)));
         g.drawString("if you are a swimmer.  You" , x, y+=((int)(size*.75)));
         g.drawString("can not have this option  ", x, y+=((int)(size*.75)));
         g.drawString("with a monster that drowns", x, y+=((int)(size*.75)));
         g.drawString("in water, not one that is ", x, y+=((int)(size*.75)));
         g.drawString("an energy absorber.       ", x, y+=((int)(size*.75)));
         y+=((int)(size*.75)*9);
         cost = "-200";
      }
      else if(highlightArea==14)
      {
         g.drawString("You won't last long in the", x, y+=((int)(size*.75)));
         g.drawString("water with this option.   " , x, y+=((int)(size*.75)));
         g.drawString("It can not be paired with ", x, y+=((int)(size*.75)));
         g.drawString("swimming or healing in    ", x, y+=((int)(size*.75)));
         g.drawString("water.                    ", x, y+=((int)(size*.75)));
         y+=((int)(size*.75)*10);
         cost = "+100";
      }
      else if(highlightArea==15)
      {
         g.drawString("Flying will allow you to  ", x, y+=((int)(size*.75)));
         g.drawString("soar over large structures" , x, y+=((int)(size*.75)));
         g.drawString("and move quickly across   ", x, y+=((int)(size*.75)));
         g.drawString("the city.  But all armed  ", x, y+=((int)(size*.75)));
         g.drawString("units will be able to see ", x, y+=((int)(size*.75)));
         g.drawString("you when in flight, and   ", x, y+=((int)(size*.75)));
         g.drawString("will likely take a shot.  ", x, y+=((int)(size*.75)));
         g.drawString("Landing on structures will", x, y+=((int)(size*.75)));
         g.drawString("do a lot of damage and may", x, y+=((int)(size*.75)));
         g.drawString("start a fire.  You can not", x, y+=((int)(size*.75)));
         g.drawString("be a flyer and a digger.  ", x, y+=((int)(size*.75)));
         g.drawString("Landing on an electric    ", x, y+=((int)(size*.75)));
         g.drawString("tower or power station    ", x, y+=((int)(size*.75)));
         g.drawString("will result in a shocking ", x, y+=((int)(size*.75)));
         g.drawString("death.                    ", x, y+=((int)(size*.75)));
         cost = "-500";
      }
      else if(highlightArea==16)
      {
         g.drawString("A digger can burrow under ", x, y+=((int)(size*.75)));
         g.drawString("ground and inflict a great" , x, y+=((int)(size*.75)));
         g.drawString("amound of damage to any   ", x, y+=((int)(size*.75)));
         g.drawString("structure above or below. ", x, y+=((int)(size*.75)));
         g.drawString("While underground, enemy  ", x, y+=((int)(size*.75)));
         g.drawString("units can not see you. But", x, y+=((int)(size*.75)));
         g.drawString("if you dig under a power  ", x, y+=((int)(size*.75)));
         g.drawString("station, electric tower or", x, y+=((int)(size*.75)));
         g.drawString("body of water, you are    ", x, y+=((int)(size*.75)));
         g.drawString("instantly killed.  You can", x, y+=((int)(size*.75)));
         g.drawString("not be a digger and flyer.", x, y+=((int)(size*.75)));
         g.drawString("The holes left in the     ", x, y+=((int)(size*.75)));
         g.drawString("ground will be impassable ", x, y+=((int)(size*.75)));
         g.drawString("by ground units, so be    ", x, y+=((int)(size*.75)));
         g.drawString("mindful of where you dig. ", x, y+=((int)(size*.75)));
         cost = "-350";
      }
      else if(highlightArea==17)
      {
         g.drawString("A thrower can pick up     ", x, y+=((int)(size*.75)));
         g.drawString("and throw vehicles as     " , x, y+=((int)(size*.75)));
         g.drawString("deadly projectiles, where ", x, y+=((int)(size*.75)));
         g.drawString("upon hitting a structure  ", x, y+=((int)(size*.75)));
         g.drawString("might start a fire.       ", x, y+=((int)(size*.75)));
         g.drawString("Throwers can not also     ", x, y+=((int)(size*.75)));
         g.drawString("shoot projectiles, nor can", x, y+=((int)(size*.75)));
         g.drawString("they be energy absorbers  ", x, y+=((int)(size*.75)));
         g.drawString("or splitters.             ", x, y+=((int)(size*.75)));
         g.drawString("The thrower is more       ", x, y+=((int)(size*.75)));
         g.drawString("potent if they are also   ", x, y+=((int)(size*.75)));
         g.drawString("ambidextorous.            ", x, y+=((int)(size*.75)));
         y+=((int)(size*.75)*3);
         cost = "-250";
      }
      else if(highlightArea==18)
      {
         g.drawString("An ambidextorous can grab ", x, y+=((int)(size*.75)));
         g.drawString("enemy units with both     " , x, y+=((int)(size*.75)));
         g.drawString("hands (claws, paws,       ", x, y+=((int)(size*.75)));
         g.drawString("appendages, whatever).    ", x, y+=((int)(size*.75)));
         g.drawString("This is great to snatch up", x, y+=((int)(size*.75)));
         g.drawString("food to save for later.   ", x, y+=((int)(size*.75)));
         g.drawString("One can not be both ambi- ", x, y+=((int)(size*.75)));
         g.drawString("dextorous and an energy   ", x, y+=((int)(size*.75)));
         g.drawString("absorber, since they don't", x, y+=((int)(size*.75)));
         g.drawString("really eat.  Ambidextorous", x, y+=((int)(size*.75)));
         g.drawString("and throwers make a great ", x, y+=((int)(size*.75)));
         g.drawString("match.                    ", x, y+=((int)(size*.75)));
         y+=((int)(size*.75)*3);
         cost = "-150";
      }
      else if(highlightArea==19)
      {
         g.drawString("The shooter can breathe,  ", x, y+=((int)(size*.75)));
         g.drawString("spit, belch, or shoot some" , x, y+=((int)(size*.75)));
         g.drawString("kind of projectile.  A    ", x, y+=((int)(size*.75)));
         g.drawString("resting time will be      ", x, y+=((int)(size*.75)));
         g.drawString("required between each     ", x, y+=((int)(size*.75)));
         g.drawString("shot fired.  A shooter can", x, y+=((int)(size*.75)));
         g.drawString("not be a thrower, a       ", x, y+=((int)(size*.75)));
         g.drawString("splitter or mind control. ", x, y+=((int)(size*.75)));
         y+=((int)(size*.75)*7);
         cost = "-350 to 550";
      }
      else if(highlightArea==20)
      {
         g.drawString("The splitter can divide   ", x, y+=((int)(size*.75)));
         g.drawString("in two (or, say, release  " , x, y+=((int)(size*.75)));
         g.drawString("a clone of itself) at the ", x, y+=((int)(size*.75)));
         g.drawString("expense of losing some    ", x, y+=((int)(size*.75)));
         g.drawString("health.  The twin will run", x, y+=((int)(size*.75)));
         g.drawString("off on its own, drawing   ", x, y+=((int)(size*.75)));
         g.drawString("crosshairs away from you  ", x, y+=((int)(size*.75)));
         g.drawString("and doing some damage of  ", x, y+=((int)(size*.75)));
         g.drawString("their own.  Splitters can ", x, y+=((int)(size*.75)));
         g.drawString("recombine with their twins", x, y+=((int)(size*.75)));
         g.drawString("to regain health back. One", x, y+=((int)(size*.75)));
         g.drawString("must pick between being a ", x, y+=((int)(size*.75)));
         g.drawString("shooter, a thrower, a     ", x, y+=((int)(size*.75)));
         g.drawString("splitter or mind control. ", x, y+=((int)(size*.75)));
         y+=((int)(size*.75));
         cost = "-500";
      }
      else if(highlightArea==21)
      {
         g.drawString("Monsters that leave a     ", x, y+=((int)(size*.75)));
         g.drawString("toxic tral can easily trap" , x, y+=((int)(size*.75)));
         g.drawString("ground units to eat later.", x, y+=((int)(size*.75)));
         g.drawString("But be careful about where", x, y+=((int)(size*.75)));
         g.drawString("you lumber about - if you ", x, y+=((int)(size*.75)));
         g.drawString("render all enterances into", x, y+=((int)(size*.75)));
         g.drawString("a city zone impassable,   ", x, y+=((int)(size*.75)));
         g.drawString("then you will be hard     ", x, y+=((int)(size*.75)));
         g.drawString("pressed to find food.     ", x, y+=((int)(size*.75)));
         y+=((int)(size*.75)*6);
         cost = "-50";
      }
      else if(highlightArea==22)
      {
         g.drawString("Rather than eating, an    ", x, y+=((int)(size*.75)));
         g.drawString("energy absorber gets their" , x, y+=((int)(size*.75)));
         g.drawString("fill by sapping the energy", x, y+=((int)(size*.75)));
         g.drawString("from electric towers,     ", x, y+=((int)(size*.75)));
         g.drawString("power stations and trains.", x, y+=((int)(size*.75)));
         g.drawString("But this convenience comes", x, y+=((int)(size*.75)));
         g.drawString("at a price:  you are      ", x, y+=((int)(size*.75)));
         g.drawString("always losing energy, and ", x, y+=((int)(size*.75)));
         g.drawString("vulnerable to water.  An  ", x, y+=((int)(size*.75)));
         g.drawString("energy absorber can not be", x, y+=((int)(size*.75)));
         g.drawString("a thrower, ambidextorous, ", x, y+=((int)(size*.75)));
         g.drawString("swimmer, water healer nor ", x, y+=((int)(size*.75)));
         g.drawString("one with a lower appetite.", x, y+=((int)(size*.75)));
         y+=((int)(size*.75)*2);
         cost = "-50";
      }
      else if(highlightArea==23)
      {
         g.drawString("A monster designed with   ", x, y+=((int)(size*.75)));
         g.drawString("this quality can grab and " , x, y+=((int)(size*.75)));
         g.drawString("eat any type of unit it   ", x, y+=((int)(size*.75)));
         g.drawString("can catch.  Otherwise you ", x, y+=((int)(size*.75)));
         g.drawString("are limited to crowds,    ", x, y+=((int)(size*.75)));
         g.drawString("cars, buses, and low      ", x, y+=((int)(size*.75)));
         g.drawString("flying news choppers. This", x, y+=((int)(size*.75)));
         g.drawString("attribue is incompatable  ", x, y+=((int)(size*.75)));
         g.drawString("with monsters that are    ", x, y+=((int)(size*.75)));
         g.drawString("energy absorbers.         ", x, y+=((int)(size*.75)));
         y+=((int)(size*.75)*5);
         cost = "+50";
      }
      else if(highlightArea==24)
      {
         g.drawString("A monster with mind       ", x, y+=((int)(size*.75)));
         g.drawString("control can launch a      " , x, y+=((int)(size*.75)));
         g.drawString("psycic attack that        ", x, y+=((int)(size*.75)));
         g.drawString("scrambles the brains of   ", x, y+=((int)(size*.75)));
         g.drawString("their victims, causing    ", x, y+=((int)(size*.75)));
         g.drawString("them to attack fellow     ", x, y+=((int)(size*.75)));
         g.drawString("humans.  A mind controller", x, y+=((int)(size*.75)));
         g.drawString("can not also be a thrower,", x, y+=((int)(size*.75)));
         g.drawString("a splitter nor a shooter, ", x, y+=((int)(size*.75)));
         g.drawString("so take your pick already!", x, y+=((int)(size*.75)));
         y+=((int)(size*.75)*5);
         cost = "+400";
      }
      else if(highlightArea==25)
      {
         g.drawString("Monsters that can't grab  ", x, y+=((int)(size*.75)));
         g.drawString("units might not have arms," , x, y+=((int)(size*.75)));
         g.drawString("like a serpent or worm, or", x, y+=((int)(size*.75)));
         g.drawString("might not have the self-  ", x, y+=((int)(size*.75)));
         g.drawString("control to hold on to food", x, y+=((int)(size*.75)));
         g.drawString("before eating it.         ", x, y+=((int)(size*.75)));
         g.drawString("Naturally, if one can not ", x, y+=((int)(size*.75)));
         g.drawString("hold food also can not be ", x, y+=((int)(size*.75)));
         g.drawString("ambidextorous.            ", x, y+=((int)(size*.75)));
         y+=((int)(size*.75)*6);
         cost = "+100";
      }
   
      if(cost.length() > 0)
      {
         g.setFont(new Font("Monospaced", Font.BOLD, (int)(size)));
         g.setColor(Color.yellow);
         y += size;
         g.drawString("COST: " + cost, x, y+=((int)(size*.75)));
      }
   
      return y;
   }

//pre:  fileName is the name of a valid file in the maps folder comprised of the list of valid map file names
//post: fills up list with each map file name from fileName
   public static String[] readCustomInfo(String fileName)
   {
      String[] list = new String[NUM_CUSTOM_ELEMENTS];
      int index = 0;
      try
      {
         java.util.Scanner input = new java.util.Scanner(new java.io.FileReader(fileName));
         while (input.hasNext())		//while there is another value in the file
         {
            try
            {
               String line = input.next();
               if(!line.startsWith("*"))	//comment
                  list[index++] = line;
            }
            catch (java.util.InputMismatchException ex1)			//file is corrupted or doesn't exist
            {
               message = "Error loading "+fileName;
               messageTime = numFrames;
               return null;
            }			
            catch (java.util.NoSuchElementException ex2)			//file is corrupted or doesn't exist
            {
               message = "Error loading "+fileName;
               messageTime = numFrames;
               return null;
            }	
            catch (java.lang.IndexOutOfBoundsException ex3)			//file is corrupted or doesn't exist
            {
               message = "Error loading "+fileName;
               messageTime = numFrames;
               return null;
            }
         }
         input.close();	
      }
      catch (java.io.IOException ex3)			//file is corrupted or doesn't exist - clear high scores and remake the file
      {
         System.out.println(fileName+" does not exist");
         return null;
      }
      return list;		
   }

//pre:  k is a vailid key event
//post: sets commands in motion depending on key input
   public static void processKey(int k)
   {
      if(textMode)
         readName(k);
      else
      {
         if(k==KeyEvent.VK_F)
         {
            if(monsterMoney >= 0)
               saveToFile();
         }
         else if(k==KeyEvent.VK_R)
         {
            if(monsterMoney < 0)					//reset to saved custom if monster money is over the limit
               customInfo = readCustomInfo("custom.txt");
            resetGame();
         }
         else if(k==KeyEvent.VK_COMMA || k==KeyEvent.VK_LEFT)				// < key - decrease attribute
            changeAttribute(MouseEvent.BUTTON1);
         else if(k==KeyEvent.VK_PERIOD || k==KeyEvent.VK_RIGHT || k==KeyEvent.VK_SPACE || k==KeyEvent.VK_ENTER)			
            changeAttribute(MouseEvent.BUTTON3);								// > key - increase attribute
         else if(k==KeyEvent.VK_UP)
         {
            scrollValue--;
            if(scrollValue < 0)
               scrollValue = NUM_CUSTOM_ELEMENTS-2;
            highlightArea = scrollValue;   
         }  
         else if(k==KeyEvent.VK_DOWN)
         {
            scrollValue++;
            if(scrollValue >= NUM_CUSTOM_ELEMENTS-1)
               scrollValue = 0;
            highlightArea = scrollValue;   
         }   
      }
   }

//post:  user enters the name of their custom monster
   public static void readName(int k)
   {
      if(monsterName==null)
         monsterName = "";
      if(k==KeyEvent.VK_ENTER)
      {
         if(monsterName.length() > 0)
            customInfo[0] = monsterName;
         textMode = false;
      }
      else if(((k>=KeyEvent.VK_A && k<=KeyEvent.VK_Z) || (k>=KeyEvent.VK_0 && k<=KeyEvent.VK_9) || k==KeyEvent.VK_SUBTRACT || k==KeyEvent.VK_MINUS) && monsterName.length()<10)
         monsterName += (char)(k);
      else if(k==KeyEvent.VK_BACK_SPACE && monsterName.length() > 0) 
         monsterName = monsterName.substring(0, monsterName.length()-1);
      if(monsterName.equalsIgnoreCase("Gobzilly") || monsterName.equalsIgnoreCase("King-Clunk") || monsterName.equalsIgnoreCase("WoeMantis") || 
        monsterName.equalsIgnoreCase("BoobooTron") ||  monsterName.equalsIgnoreCase("The-Blop") ||  monsterName.equalsIgnoreCase("Wormoid"))
      {
         monsterName = "COPYCAT";  
      }
      else if(isSuspect(monsterName))
      {
         monsterName = "POTTYMOUTH";
      }
   }

//post:  shows keys and info in monster maker mode
   public static void monsterDisplay(Graphics g, int x, int y, int size)
   {  
      g.setFont(new Font("Monospaced", Font.BOLD, (int)(size*.75)));
      g.setColor(Color.yellow.darker());
      g.drawString("----------------------", x, y+=(size));
      g.setColor(Color.yellow);
      if(textMode)
      {
         g.drawString("Enter monster's name: ", x, y+=(size));
      
         g.setColor(Color.yellow);
         g.fillRect(x, y+(size/2) ,(size*12), size);
      
         if(monsterName!= null && monsterName.length() > 0)
         {
            g.setColor(Color.red.darker());
            g.drawString(monsterName, x, y+(size + size/4));
         }
         y+=(size);
      }
      else
         y+=(size*2);
      g.setColor(Color.yellow.darker());
      g.drawString("----------------------", x, y+=(size));
      g.setColor(Color.yellow);
      g.drawString("Scroll options:       ", x, y+=(size));
      g.drawString("UP / DOWN  or mouse   ", x, y+=(size));
      y += size;
      g.drawString("Decrease value:       ", x, y+=(size));
      g.drawString("LEFT CLICK or <       ", x, y+=(size));
      y += size;
      g.drawString("Increase value:       ", x, y+=(size));
      g.drawString("RIGHT CLICK or >      ", x, y+=(size));
      y += size;
      g.setColor(Color.yellow.darker());
      g.drawString("----------------------", x, y+=(size));
      g.setColor(Color.yellow);
      if(monsterMoney < 0)
      {
         g.setColor(Color.red);
         g.drawString("GET MONSTER MONEY >= 0", x, y+=(size));
      }
      else
         g.drawString("F:       WRITE TO FILE", x, y+=(size));
      y += size;
      g.setColor(Color.yellow);
      g.drawString("R:      RETURN TO MAIN", x, y+=(size));
   
      y = showAttributes(g, x, y, size);
   }
   
   public static String shiftWord(String word)
   {
      if(word==null || word.length()==0)
         return word;
      String ans="";
      int shift = 3;
      for(int i=word.length()-1; i>=0; i--)
      {
         char let = word.charAt(i);
         if(((int)(let))+shift <= 122)
            ans += (char)(let+shift);
         else
            ans += ((char)(48+(122-((int)(let)))));
      }
      return ans;
   }
   
   public static boolean isSuspect(String typed)
   {
      if(typed==null || typed.length()<4)
         return false;
      typed = typed.toLowerCase().trim();
      String test = "spxkf0nqxs0hurkz0kfwle0horkvvd01vvxs0wqxf0nfrf0nflg0nfxi0wlkv0qpdg";
      String[] parts = typed.split(" ");
      String[] parts2 = test.split(shiftWord(""+((char)(122)))); 
      for(String word: parts)
         for(String word2: parts2)
            if(shiftWord(word).contains(word2))
               return true;
      return false;
   }

//post:  writes custom monster to Custom.txt to be saved for later gameplay
   public static void saveToFile()
   {
      PrintStream imageWriter = null;
      File imageFile = new File("Custom.txt");
      if(imageFile.exists())		//make sure to write over old file
      {
         imageFile.delete();
         imageFile = new File("Custom.txt");
      }
      
      while(imageWriter == null){
         try{
            imageWriter = new PrintStream(new FileOutputStream(imageFile, true));
         }
         catch(Exception E){
            System.exit(42);
         }
      }
   
      imageWriter.println("*monster-name* " + customInfo[0]);	
      imageWriter.println("*image-set-index* " + customInfo[1]);
      imageWriter.println("*animation-delay* " + customInfo[2]);
      imageWriter.println("*stomp-power* " + customInfo[3]);
      imageWriter.println("*speed-penalty* " + customInfo[4]);
      imageWriter.println("*reload-time* " + customInfo[5]);
      imageWriter.println("*autostomp-power* " + customInfo[6]);
      imageWriter.println("*projectile-type* " + customInfo[7]);
      imageWriter.println("*burn-damage* " + customInfo[8]);
      imageWriter.println("*head360* " + customInfo[9]);
      imageWriter.println("*isSwimmer* " + customInfo[10]);
      imageWriter.println("*isFlyer* " + customInfo[11]);
      imageWriter.println("*isDigger* " + customInfo[12]);
      imageWriter.println("*isThrower* " + customInfo[13]);
      imageWriter.println("*isShooter* " + customInfo[14]);
      imageWriter.println("*canSplit* " + customInfo[15]);
      imageWriter.println("*healInWater* " + customInfo[16]);
      imageWriter.println("*isAmbidextorous* " + customInfo[17]);
      imageWriter.println("*damageInWater* " + customInfo[18]);
      imageWriter.println("*lessAppetite* " + customInfo[19]);
      imageWriter.println("*imperviousToBullets* " + customInfo[20]);
      imageWriter.println("*isFragile* " + customInfo[21]);
      imageWriter.println("*slimeTrail* " + customInfo[22]);
      imageWriter.println("*energyAbsorber* " + customInfo[23]);
      imageWriter.println("*canEatAll* " + customInfo[24]);
      imageWriter.println("*mindControl* " + customInfo[25]);
      imageWriter.println("*cantGrab* " + customInfo[26]);
      imageWriter.close();
   }
}