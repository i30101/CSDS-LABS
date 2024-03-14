//Mash, Mangle & Munch - Rev Dr Douglas R Oberle, July 2012  doug.oberle@fcps.edu
//DISPLAY UTILITIES  
import javax.swing.ImageIcon;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.Color;
//handles loading all images and showing the board, options and stats to the screen
public class ImageDisplay extends MMMPanel
{
   private static String[] compass = {"North","East","South","West"};
   private static int rotation = 0;	//value to rotate player image in player selection screen;
   private static int headDir = 0;	//value for head direction in player selection screen
   private static int monster = 0;  //value used to show each monster for random monster selection
   private static ImageIcon title;	//stores images for the title screen
   private static int nukeSize = 1;	//for nuke effect

//pre:  0<=num<4
//post: returns a numeric direction as a string   
   private static String numToDir(int dir)
   {
      if(dir==UP) 			
         return "Up";	
      if(dir==RIGHT) 	
         return "Right";
      if(dir==DOWN) 
         return "Down";
      if(dir==LEFT)
         return "Left";
      return null;
   }

//pre:  0<=name<playerImages.length
//post: returns the folder name for the monster type at the specified index
   private static String numToName(int name)
   {
      if(name==0)
         return "king";
      if(name==1)
         return "gob";
      if(name==2)
         return "boo";
      if(name==3)
         return "woe";
      if(name==4)
         return "blop";
      if(name==5)
         return "worm";
      if(name==6)
         return "ufo"; 
      if(name==7)
         return "beast";  
      if(name==8)
         return "fsm";  
      if(name==9)
         return "puft"; 
      return null;
   }

//pre:  required image files exist in the specified folders - arrays are proper size
//post: fills arrays of images and creates image icons for game elements
   public static void loadImages()
   {
      String dayVersion = "";
      if(!day)
         dayVersion = "NIGHT";
   
   //load monster images i.e. "images/monsters/king/playerDownL0.GIF" is the gorilla, facing down, head turned left, 1st anim frame
      for(int name=0; name<playerImages.length; name++)
         for(int dir=UP; dir<=LEFT; dir++)
            for(int frame = 0; frame<4; frame++)
               for(int head=UP; head<=LEFT; head++)
               {	//there is no image where head is backwards fireciton from body direction, so just show the one where they are facing the same direction
                  String imageName = "images/monsters/"+numToName(name)+"/player"+numToDir(dir)+numToDir(head).charAt(0)+frame+".GIF";
                  if(Utilities.oppositeDirections(dir, head) && name!=2 && name!=5 && name!=6)	//except name==2,6 (boobootron,worm,ufo) whose head can rotate 360 degrees
                     imageName =  "images/monsters/"+numToName(name)+"/player"+numToDir(dir)+numToDir(dir).charAt(0)+frame+".GIF";
                  playerImages[name][dir][frame][head] = imageName;
               }
   
   //VEHICLES***************** i.e. "images/vehicles/motorcycle/cycleLeftNIGHT.GIF" is facing left for night time version
      for(int dir=UP; dir<=LEFT; dir++)
      {	//vehicles with no animation frames and no head direction changes
         motorcycleImages[dir][0][0]   = "images/vehicles/motorcycle/cycle"+numToDir(dir) + dayVersion + ".GIF";
         carImages[dir][0][0] 	  	   = "images/vehicles/car/car"+numToDir(dir) + dayVersion + ".GIF";
         busImages[dir][0][0] 	  	   = "images/vehicles/car/bus"+numToDir(dir) + dayVersion + ".GIF";
         artilleryImages[dir][0][0] 	= "images/vehicles/artillery/artillery"+numToDir(dir) +".GIF";
         fighterImages[dir][0][0] 	   = "images/vehicles/plane/fighter"+numToDir(dir) +".GIF";
         bomberImages[dir][0][0] 	   = "images/vehicles/plane/bomber"+numToDir(dir) +".GIF";
         bomber2Images[dir][0][0] 	   = "images/vehicles/plane/bomber2"+numToDir(dir) +".GIF";
      }
   
      for(int frame = 0; frame<2; frame++)
         for(int dir=UP; dir<=LEFT; dir++)
         {	//vehicles with 2 animation frames and no head direction changes
            firetruckImages[dir][frame][0]			= "images/vehicles/rescue/firetruck"+numToDir(dir) + frame + dayVersion + ".GIF";
            policeMotorcycleImages[dir][frame][0]	= "images/vehicles/motorcycle/cyclePolice"+numToDir(dir) + frame + dayVersion + ".GIF";
            policeImages[dir][frame][0]			 	= "images/vehicles/police/policeCar"+numToDir(dir) + frame + dayVersion + ".GIF";
            cessnaImages[dir][frame][0] 	   		= "images/vehicles/plane/cesna"+numToDir(dir) + frame + dayVersion + ".GIF";
            heliImages[dir][frame][0]					= "images/vehicles/heli/newsHeli"+numToDir(dir) + frame + ".GIF";
         }
   
      for(int dir=UP; dir<=LEFT; dir++)
         for(int head=UP; head<=LEFT; head++)
         {	//vehicles with no animation frames and 4 head direction changes     		
            tankImages[dir][0][head]				= "images/vehicles/tank/tank" + numToDir(dir) + numToDir(head) + ".GIF";
            flameTankImages[dir][0][head] 		= "images/vehicles/flameTank/tank" + numToDir(dir) + numToDir(head) + ".GIF";
            missileLauncherImages[dir][0][head]	= "images/vehicles/missileLauncher/missile" + numToDir(dir) + numToDir(head) + ".GIF";
            jeepImages[dir][0][head]	 			= "images/vehicles/jeep/jeep" + numToDir(dir) + numToDir(head) + ".GIF";
         }
   
      for(int frame = 0; frame < 4; frame++)
         for(int dir = UP; dir <= LEFT; dir++)
         {	//units with 4 animation frames and no head direction changes
            crowdImages[dir][frame][0] = "images/crowds/civ" + numToDir(dir) + frame + ".GIF";
            troopImages[dir][frame][0] = "images/crowds/troops" + numToDir(dir) + frame + ".GIF";
         }
   
      for(int dir = UP; dir <= LEFT; dir++)
      {
         trainImages[0][dir][0][0]	= "images/vehicles/train/TrainFront"+ numToDir(dir).charAt(0) + dayVersion + ".GIF";
         trainImages[1][dir][0][0] 	= "images/vehicles/train/TrainMid"+ numToDir(dir).charAt(0) + dayVersion + ".GIF";
         trainImages[2][dir][0][0] 	= "images/vehicles/train/TrainEnd"+ numToDir(dir).charAt(0) + dayVersion + ".GIF";
      }     
   
   //BULLETS*************************
   
      bulletImages[0][0][0] 		= "images/bullet/bullet.GIF";
   
      for(int dir = UP; dir <= LEFT; dir++)
      {	//for projectiles with 4 directions and no animaiton frames
         shellImages[dir][0][0] 			= "images/bullet/shell" + numToDir(dir) + ".GIF";
         machBulletImages[dir][0][0]	= "images/bullet/machBullet" + numToDir(dir) + ".GIF";
         shriekImages[dir][0][0] 		= "images/bullet/shriek" + numToDir(dir) + ".GIF";
      }
   
      junkImages[UP][0][0] 		= "images/bullet/junkUp.GIF";
      junkImages[DOWN][0][0] 		= "images/bullet/junkUp.GIF";
      junkImages[RIGHT][0][0]		= "images/bullet/junkRight.GIF";
      junkImages[LEFT][0][0] 		= "images/bullet/junkRight.GIF";
   
      beamImages[UP][0][0] 		= "images/bullet/beamUp.GIF";
      beamImages[DOWN][0][0] 		= "images/bullet/beamUp.GIF";
      beamImages[RIGHT][0][0]		= "images/bullet/beamRight.GIF";
      beamImages[LEFT][0][0] 		= "images/bullet/beamRight.GIF";
   
      for(int dir = UP; dir <= LEFT; dir++)
         for(int frame=0; frame<4; frame++)
         {//for projectiles with 4 directions and 4 animation frames
            rocketImages[dir][frame][0]		= "images/bullet/missile" + numToDir(dir) + frame+".GIF";
            monsterFireImages[dir][frame][0]	= "images/bullet/fire" + numToDir(dir) + frame+".GIF";
            webImages[dir][frame][0] 		 	= "images/bullet/web"+frame+".GIF";
         }
   
   //EXPLOSIONS****************
      for(int i=0; i<4; i++)
      {
         explosionImages[0][i][0] = "images/explosion/explosion"+i+".GIF";
         elecExplImages[0][i][0] = "images/explosion/elec"+i+".GIF";
         waterExplImages[0][i][0] = "images/explosion/waterExpl"+i+".GIF";
         puffImages[0][i][0] = "images/explosion/puff"+i+".GIF";
         fireImages[0][i] = "images/explosion/fire"+i+".GIF";
      }
   
   //STRUCTURES*******************
   //i.e "images/terrain/trees/trees10.GIF is the 1st frame of tree type 1
      for(int i=0; i<2; i++)
         for(int frame=0; frame<4; frame++)
         {  //for structures with 2 variations and 4 animation frames
            treeImages[i][frame][0] 		= "images/terrain/trees/trees" + (i+1) + frame + ".GIF";								
            elecTowerImages[i][frame][0] 	= "images/structures/elec" + (i+1) + dayVersion + ".GIF";
            waterTowerImages[i][frame][0] = "images/structures/waterTower/waterTower" + (i+1) + frame + dayVersion + ".GIF";
            houseImages[i][frame][0] 		= "images/structures/houses/houses" + i + frame + dayVersion + ".GIF";		//houses
         }     
    
   //fuel depot - gas stations
      for(int i=0; i<2; i++)
         for(int frame=0; frame<2; frame++)
         {  //for structures with 2 variations and 2 animation frames
            fuelDepotImages[i][frame][0] = "images/structures/gasStations/fuelTank" + i + frame + dayVersion + ".GIF";	
         }	
   
      for(int i=0; i<3; i++)
         for(int frame=0; frame<2; frame++)
         {  //for structures with 3 variations and 2 animation frames
            gasStationImages[i][frame][0] = "images/structures/gasStations/gasStation" + i + frame + ".GIF";		
         }
   
   //suburb buildings
      for(int i=0; i<4; i++)
         for(int frame=0; frame<4; frame++)
         {  //for structures with 4 variations and 4 animation frames
            buisnessImages[i][frame][0] = "images/structures/buisness/buisness" + i + frame + dayVersion + ".GIF";
            buildingImages[i][frame][0] = "images/structures/building/building" + i + frame + dayVersion + ".GIF";
         }
   
   //skyscrapers
      for(int i=0; i<6; i++)
         for(int frame=0; frame<4; frame++)
         {  //for structures with 6 variations and 4 animation frames
            skyscraperImages[i][frame][0] = "images/structures/tower/tower" + i + frame + dayVersion + ".GIF";
            casinoImages[i][frame][0] 		= "images/structures/casinos/casino"  + i + frame + dayVersion + ".GIF";	
            rideImages[i][frame][0] 		= "images/structures/amusement/ride" + i + frame + ".GIF";
         }
   
   //Blop-glop
   
      for(int i=0; i<4; i++)
         for(int frame=0; frame<4; frame++)
         {
            if(frame == 3)	//the same picture is used for the last frame of each of the 4 glop types
               blopGlopImages[i][frame][0] = "images/monsters/blop/blopGoo03.GIF";
            else
               blopGlopImages[i][frame][0] = "images/monsters/blop/blopGoo" + i + frame + ".GIF";
         }
   
   //Hole in the ground images
      for(int i=0; i<4; i++)
         holeImages[i][0][0]     = "images/monsters/worm/hole" + i + dayVersion + ".gif";
    
   //landmarks
      for(int i=0; i<10; i++)
         for(int frame=0; frame<4; frame++)
         {  //for structures with 10 variations and 4 animation frames
            landmarkImages[i][frame][0] = "images/structures/landmarks/lndmrk" + i + frame  + dayVersion + ".GIF";	
         }	
   
   //TERRAIN****************
   
      terrainImages[0][0]		= "images/terrain/blank1" + dayVersion + ".jpg";
      terrainImages[1][0]		= "images/terrain/park1" + dayVersion + ".jpg";
      terrainImages[2][0]		= "images/terrain/elec1" + dayVersion + ".jpg";
   
      terrainImages[3][0]		= "images/terrain/roads/SR1" + dayVersion + ".jpg";
      terrainImages[4][0]		= "images/terrain/roads/SU1" + dayVersion + ".jpg";
      terrainImages[5][0]		= "images/terrain/roads/SX1" + dayVersion + ".jpg";
   
      terrainImages[6][0]		= "images/terrain/water/water1" + dayVersion + ".jpg";
      terrainImages[6][1]		= "images/terrain/water/water2" + dayVersion + ".jpg";
      terrainImages[6][2]		= "images/terrain/water/water3" + dayVersion + ".jpg";
      terrainImages[6][3]		= "images/terrain/water/water4" + dayVersion + ".jpg";
   
      terrainImages[7][0]		= "images/terrain/water/water1b" + dayVersion + ".GIF";
      terrainImages[7][1]		= "images/terrain/water/water2b" + dayVersion + ".GIF";
      terrainImages[7][2]		= "images/terrain/water/water3b" + dayVersion + ".GIF";
      terrainImages[7][3]		= "images/terrain/water/water4b" + dayVersion + ".GIF";
   
      terrainImages[8][0]		= "images/terrain/water/bridge11" + dayVersion + ".jpg";	//horiz bridge
      terrainImages[8][1]		= "images/terrain/water/bridge12" + dayVersion + ".jpg";
      terrainImages[8][2]		= "images/terrain/water/bridge13" + dayVersion + ".jpg";
      terrainImages[8][3]		= "images/terrain/water/bridge14" + dayVersion + ".jpg";
   
      terrainImages[9][0]		= "images/terrain/water/bridge21" + dayVersion + ".jpg";	//vert bridge
      terrainImages[9][1]		= "images/terrain/water/bridge22" + dayVersion + ".jpg";
      terrainImages[9][2]		= "images/terrain/water/bridge23" + dayVersion + ".jpg";
      terrainImages[9][3]		= "images/terrain/water/bridge24" + dayVersion + ".jpg";
   
      terrainImages[10][0]		= "images/terrain/sand1" + dayVersion + ".jpg";
      terrainImages[11][0]		= "images/terrain/mud1" + dayVersion + ".jpg";
      terrainImages[12][0]		= "images/terrain/forest" + dayVersion + ".jpg";
      terrainImages[13][0]		= "images/terrain/field" + dayVersion + ".jpg";
   
      terrainImages[14][0]		= "images/terrain/roads/SX2" + dayVersion + ".jpg";		//parking lots
      terrainImages[15][0]		= "images/terrain/roads/SX3" + dayVersion + ".jpg";
   
      terrainImages[16][0]		= "images/terrain/tracks/TR1" + dayVersion + ".jpg";		//railroad tracks right
      terrainImages[17][0]		= "images/terrain/tracks/TU1" + dayVersion + ".jpg";		//rr up
      terrainImages[18][0]		= "images/terrain/tracks/TXR" + dayVersion + ".jpg";		//rr x-ing right
      terrainImages[19][0]		= "images/terrain/tracks/TXU" + dayVersion + ".jpg";		//rr x-ing up
      terrainImages[20][0]		= "images/terrain/tracks/TR2" + dayVersion + ".jpg";		//rr bridge right
      terrainImages[21][0]		= "images/terrain/tracks/TU2" + dayVersion + ".jpg";		//rr bridge up
   
   
   
      for(int r=0; r<terrain.length; r++)
      {
         for(int c=0; c<terrain[0].length; c++)
         {
            if(terrainImages[r][c] != null)
               terrain[r][c] = new ImageIcon( terrainImages[r][c]);
         }
      }
   
      for(int r=0; r<fire.length; r++)
      {
         for(int c=0; c<fire[0].length; c++)
         {
            if(fireImages[r][c] != null)
               fire[r][c] = new ImageIcon( fireImages[r][c]);
         }
      }
   
      for(int r=0; r<web.length; r++)
      {
         if(webImages[r] != null)
            web[r] = new ImageIcon( webImages[0][r][0]);
      }
   
   
   //MOUSE cursors
      cursors[UP] = new ImageIcon("images/mouseCursors/cursorUp.GIF");
      cursors[RIGHT] = new ImageIcon("images/mouseCursors/cursorRight.GIF");
      cursors[DOWN] = new ImageIcon("images/mouseCursors/cursorDown.GIF");
      cursors[LEFT] = new ImageIcon("images/mouseCursors/cursorLeft.GIF");
      cursors[4] = new ImageIcon("images/mouseCursors/cursorCenter.GIF");
   
   //TITLE SCREEN images
      int whichTitleScreen = (int)(Math.random()*5); 
      title = new ImageIcon("images/titleScreens/title"+whichTitleScreen+".jpg");
   
   }

   public static void drawTerrain(Graphics g, String terr, int x, int y)
   {
      if(terr.equals("S--"))				//empty lot
         g.drawImage(terrain[0][0].getImage(), x, y, cellSize, cellSize, null);  //scaled image
      else if(terr.equals("PK1"))			//park1
         g.drawImage(terrain[1][0].getImage(), x, y, cellSize, cellSize, null);  
      else if(terr.equals("PK2"))			//forest
         g.drawImage(terrain[12][0].getImage(), x, y, cellSize, cellSize, null);  
      else if(terr.equals("PK3"))			//field
         g.drawImage(terrain[13][0].getImage(), x, y, cellSize, cellSize, null);  
      else if(terr.startsWith("#"))		//not passable - elec tower or power plant
         g.drawImage(terrain[2][0].getImage(), x, y, cellSize, cellSize, null);  
      else if(terr.equals("SR1"))			//east-west street
         g.drawImage(terrain[3][0].getImage(), x, y, cellSize, cellSize, null); 
      else if(terr.equals("SU1"))			//north-south street
         g.drawImage(terrain[4][0].getImage(), x, y, cellSize, cellSize, null);  
      else if(terr.startsWith("SX1"))	//street intersection	
         g.drawImage(terrain[5][0].getImage(), x, y, cellSize, cellSize, null);  
      else if(terr.startsWith("SX2"))	//parking lot
         g.drawImage(terrain[14][0].getImage(), x, y, cellSize, cellSize, null);  
      else if(terr.startsWith("SX3"))	//parking lot
         g.drawImage(terrain[15][0].getImage(), x, y, cellSize, cellSize, null);  
      else if(terr.equals("TR1"))			//railroad tracks right
         g.drawImage(terrain[16][0].getImage(), x, y, cellSize, cellSize, null);  
      else if(terr.equals("TU1"))			//rr tracks up
         g.drawImage(terrain[17][0].getImage(), x, y, cellSize, cellSize, null);  
      else if(terr.startsWith("TXR"))	//rr x-ing right
         g.drawImage(terrain[18][0].getImage(), x, y, cellSize, cellSize, null);  
      else if(terr.equals("TXU"))			//rr x-ing up
         g.drawImage(terrain[19][0].getImage(), x, y, cellSize, cellSize, null);  
      else if(terr.equals("TR2"))			//rr bridge right
         g.drawImage(terrain[20][0].getImage(), x, y, cellSize, cellSize, null);  
      else if(terr.equals("TU2"))			//rr bridge up
         g.drawImage(terrain[21][0].getImage(), x, y, cellSize, cellSize, null);  
      else if(terr.indexOf("~") >= 0)	//pick random water image 
      {	//could be a bridge (S~1 or S~2) or water (~~~)
         int rand = (int)(Math.random()*4);
         if(terr.equals("S~1"))		//pick random horiz bridge image
            g.drawImage(terrain[8][rand].getImage(), x, y, cellSize, cellSize, null); 
         else if(terr.equals("S~2"))	//pick random vert bridge image
            g.drawImage(terrain[9][rand].getImage(), x, y, cellSize, cellSize, null); 
         else
            g.drawImage(terrain[6][rand].getImage(), x, y, cellSize, cellSize, null);  
      }
      else if(terr.equals("MD2"))		//sand, passable
         g.drawImage(terrain[10][0].getImage(), x, y, cellSize, cellSize, null);  
      else if(terr.equals("MD1"))	//mud, passable
         g.drawImage(terrain[11][0].getImage(), x, y, cellSize, cellSize, null);  
      else
         g.drawImage(terrain[0][0].getImage(), x, y, cellSize, cellSize, null); 
   
   }

//post:  shows different pictures on the screen in grid format depending on the values stored in the array board
   public static void showBoard(Graphics g)	
   {
      int x =0, y=0;							//upper left corner location of where image will be drawn
      int tempX = 0, tempY = 0;			//save locations for graphic position of where the player is to be used to draw the player in motion when transitioning from one cell to another
      for(int r=0;r<board.length;r++)
      {
         x = 0;								//reset the row distance
         for(int c=0;c<board[0].length;c++)
         {   
            drawTerrain(g, board[r][c][panel], x, y);
         
         //draw any Blop-glop or holes on/in the ground
            if(structures[r][c][panel]!=null && (structures[r][c][panel].getName().startsWith("Blop") || structures[r][c][panel].getName().startsWith("hole")))
            {
               Structure str = structures[r][c][panel];
               g.drawImage(str.getPictureAndAdvance().getImage(), x, y - str.getHeightDiff(), cellSize, cellSize + str.getHeightDiff(), null);  //scale
            }
         //show where all the players are
            for(int i=0; i<players.length; i++)
            {
               Player curr = players[i];
               if(curr==null || curr.getName().equals("NONE") || curr.isFlying() || curr.isDigging())	//draw flying players last so they show over the buildings and terrain
                  continue;
               String name = curr.getName();
               if(name.startsWith("AIR"))		//show aircraft last, so that they appear above everything else
                  continue;
               if(r==curr.getRow() && c==curr.getCol())
               { 
                  if(curr.getMoveIncrX() == 0 && curr.getMoveIncrY() == 0)	//draw the crosshair on the board after the cell has been drawn
                  {					//don't advance animation bc we are not moving - subtract jump altitude if we are jumping    
                     int jumpAltitude = 0;
                     if(curr instanceof Monster)
                        jumpAltitude = ((Monster)(curr)).getJumpAlt();
                     if(curr.getHealth()<=0)		//show dead monster
                        g.drawImage(curr.getPicture(UP,0,UP).getImage(), x, y - jumpAltitude, cellSize, cellSize, null);  //scaled image
                     else
                        g.drawImage(curr.getPicture().getImage(), x, y - jumpAltitude, cellSize, cellSize, null);  //scaled image
                     if(curr instanceof Vehicle && ((Vehicle)curr).getStunTime() > (numFrames + messageTime))
                     {
                        g.drawImage(web[0].getImage(), x, y, cellSize, cellSize, null);
                     }
                  
                     if(board[r][c][panel].equals("~~~"))			//we are in water - draw a 1/2 water on top of player
                     {
                        int rand = (int)(Math.random()*4);
                        g.drawImage(terrain[7][rand].getImage(), x, y, cellSize, cellSize, null);  //scaled image
                     }
                  }
                  else
                  {	
                     if(curr.isMovingLeft() || curr.isMovingUp())
                     {
                        g.drawImage(curr.getPictureAndAdvance().getImage(), x + curr.getMoveIncrX(), y + curr.getMoveIncrY(), cellSize, cellSize, null);  //scaled image
                        if(curr instanceof Vehicle && ((Vehicle)curr).getStunTime() > (numFrames + messageTime))
                        {
                           g.drawImage(web[(int)(Math.random()*web.length)].getImage(), x+ curr.getMoveIncrX(), y + curr.getMoveIncrY(), cellSize, cellSize, null);
                        }
                        if((curr.isMovingLeft() && !board[r][c-1][panel].equals("~~~")) || (curr.isMovingUp() && !board[r-1][c][panel].equals("~~~")))
                        {}	//if we are moving into a spot that is not water, don't draw 1/2 water on top of player
                        else
                           if(board[r][c][panel].equals("~~~"))		//we are in water - draw a 1/2 water on top of player
                           {
                              int rand = (int)(Math.random()*4);
                              g.drawImage(terrain[7][rand].getImage(), x+ curr.getMoveIncrX(), y + curr.getMoveIncrY(), cellSize, cellSize, null);  //scaled image
                           }
                     }
                     else
                     {
                        curr.setTempX(x);	//store the pixel location of where we need to draw the player if they are moving RIGHT or DOWN
                        curr.setTempY(y);
                     }
                  }
               }
            }
         //draw any passable or destroyed structures so that it is in front of the player - don't show Blop-glop or holes in the ground here because we don't want it to appear in front of any players
            if(structures[r][c][panel]!=null && !structures[r][c][panel].getName().startsWith("Blop") && !structures[r][c][panel].getName().startsWith("hole") && (structures[r][c][panel].getHealth()<=0 || structures[r][c][panel].isPassable()))
            {
               Structure str = structures[r][c][panel];
               String name = str.getName();
                   
               if(str.getHeight()==1)  //each structure has a hieghtDiff that is little variations in height 
               {
                  g.drawImage(str.getPictureAndAdvance().getImage(), x, y - str.getHeightDiff(), cellSize, cellSize + str.getHeightDiff(), null);  //scale
               }
               else							//add the height of the building	
                  g.drawImage(str.getPictureAndAdvance().getImage(), x, y - ((str.getHeight()-1)*cellSize) - str.getHeightDiff(), cellSize ,(str.getHeight()*cellSize) + str.getHeightDiff(), null); 
            }
            x+=cellSize;
         }
         y+=cellSize;
      }
   
      for(int i=0; i<players.length; i++)
      {
         Player curr = players[i];
         if(curr==null || curr.getName().equals("NONE") || curr.isFlying() || curr.isDigging())		
            continue;
         String name = curr.getName();
         if(name.startsWith("AIR"))		//show aircraft last, so that they appear above everything else
            continue;
         int r = curr.getRow();
         int c = curr.getCol();
         x = curr.getTempX() + curr.getMoveIncrX();
         y = curr.getTempY() + curr.getMoveIncrY();
         if(curr.getMoveIncrX() > 0 ||curr.getMoveIncrY() > 0)	//draw the player that is currently moving on the board after the cell has been drawn
         {
            if(curr.isMovingRight() || curr.isMovingDown())
            {
               g.drawImage(curr.getPictureAndAdvance().getImage(), x, y, cellSize, cellSize, null);  //scaled image
               if(curr instanceof Vehicle && ((Vehicle)curr).getStunTime() > (numFrames + messageTime))
                  g.drawImage(web[(int)(Math.random()*web.length)].getImage(), x, y, cellSize, cellSize, null);
               if((curr.isMovingRight() && !board[r][c+1][panel].equals("~~~")) || (curr.isMovingDown() && !board[r+1][c][panel].equals("~~~")))
               {}	//if we are moving into a spot that is not water, don't draw 1/2 water on top of player
               else
                  if(board[r][c][panel].equals("~~~"))		//we are in water - draw a 1/2 water on top of player
                  {
                     int rand = (int)(Math.random()*4);
                     g.drawImage(terrain[7][rand].getImage(), x, y, cellSize, cellSize, null);  //scaled image
                  }
            }
         }
      }
          	
      x = 0;		//draw any structures that are impassable so that it is in front of the player
      y = 0;		//upper left corner location of where image will be drawn
   
      for(int r=0;r<structures.length;r++)
      {
         x = 0;	//reset the row distance
         for(int c=0;c<structures[0].length;c++)
         {
            Structure curr = structures[r][c][panel];
            boolean showStr = false;
         //look for a player above it that is moving down, or below it that is moving up
         //if you can't find one, show the structure even if it is passable or destroyed
            if(curr!=null && curr.getHealth() > 0)
               showStr = true;
            boolean closePlayerFound = false;   
            for(int i=0; i<players.length; i++)
            {
               Player pl = players[i];
               if(pl==null || pl.getName().equals("NONE"))		//player 2 might not be activated
                  continue;
               if(pl.getName().indexOf("firetruck") >= 0)
               {	//put out any fire close to the firetruck, and spray away any Blop-glop
                  if(Math.abs(r-pl.getRow()) <=1 && Math.abs(c-pl.getCol()) <=1 && structures[r][c][panel]!=null)
                  {
                     if (structures[r][c][panel].onFire() || structures[r][c][panel].getName().startsWith("Blop"))
                     {
                        int x1 = pl.findX(cellSize)+(cellSize/2);
                        int y1 = pl.findY(cellSize)+(cellSize/2);
                        int x2 = x + (int)(Math.random()*(cellSize/4));
                        int y2 = y + (int)(Math.random()*(cellSize/4));
                        int randColor = (int)(Math.random()*256);
                        g.setColor(new Color(randColor,randColor,255));
                        g.drawLine(x1, y1, x2, y2);
                     }
                  }                     
               }
            
               int jumpAltitude = 0;
               if(pl instanceof Monster)
                  jumpAltitude = ((Monster)(pl)).getJumpAlt();
               int pR=pl.getRow();
               int pC=pl.getCol();
               if(curr!=null && curr.getHealth() <= 0)
               {
                  if(pR==r && pC==c && pl.isMovingDown())
                  {//if a structure is rubble, we want to  show the player over it when they are on the same position and moving down
                     closePlayerFound = true;
                     break;
                  }
                  if(pR==r+1 && pC==c && (pl.isMovingUp() || jumpAltitude != 0))
                  {//we want ot show the player over the rubble if they are one row below it and moving up
                     closePlayerFound = true;
                     break;
                  }
               }
            }
            if(!closePlayerFound && (curr!=null && curr.getHealth() <= 0) && !curr.getName().startsWith("Blop") && !curr.getName().startsWith("hole"))
               showStr=true;
            if(showStr)
            {	
               if(curr.getHeight()==1) //each structure has a heightDiff that is little variations in height   
                  g.drawImage(curr.getPictureAndAdvance().getImage(), x, y - curr.getHeightDiff(), cellSize, cellSize + curr.getHeightDiff(), null);  //scaled image
               else							//add the height of the building	
                  g.drawImage(curr.getPictureAndAdvance().getImage(), x, y - ((curr.getHeight()-1)*cellSize) - curr.getHeightDiff(), cellSize ,(curr.getHeight()*cellSize) + curr.getHeightDiff(), null);
               if(curr.onFire())							//draw any fire on this structure
               {
                  int picSize = cellSize;
                  int dW=0;		//variation in width
                  int dH=0; 		//variation in height
                  dW = (int)(Math.random()*(cellSize/4));
                  dH = (int)(Math.random()*(cellSize/4));
                  int rand = (int)(Math.random()*4);
                  g.drawImage(fire[0][rand].getImage(), x, y, cellSize+dW, cellSize+dH, null);  //scaled image
               }
               if(curr.getWebValue() > 0)	//draw webs that might be attached to this tower
               {
                  g.drawImage(web[0].getImage(), x,  y - ((curr.getHeight()-1)*cellSize) - curr.getHeightDiff(), cellSize, cellSize, null);
               }
            }
            x+=cellSize;
         }
         y+=cellSize;
      }
   
   //draw any jumping players, then redraw any structures from thier position down to the first street
   //since the structures are already drawn, a jumping player's head would clip under a structure that might be one row above him
   //so, if the player is jumping, redraw the player, then any structures below them on that city block
      for(int i=0; i<players.length; i++)
      {
         Player curr = players[i];
         if(curr==null || curr.getName().equals("NONE") || curr.isFlying() || curr.isDigging())	//draw flying players last so they show over the buildings and terrain
            continue;
         if(!(curr instanceof Monster))
            continue;
         int jumpAltitude = 0;
         jumpAltitude = ((Monster)(curr)).getJumpAlt();
         if(jumpAltitude == 0)
            continue;
         int r = curr.getRow();
         int c = curr.getCol();
         if(r-1 >=0 && structures[r-1][c][panel] == null)
            continue;
         g.drawImage(curr.getPicture().getImage(), curr.findX(cellSize), curr.findY(cellSize) - jumpAltitude, cellSize, cellSize, null);  //scaled image    
         if(board[r][c][panel].equals("~~~"))			//we are in water - draw a 1/2 water on top of player
         {
            int rand = (int)(Math.random()*4);
            g.drawImage(terrain[7][rand].getImage(), curr.findX(cellSize), curr.findY(cellSize), cellSize, cellSize, null);  //scaled image
         }
         for(int row=r; row<board.length; row++)
         {
            if (!board[row][c][panel].equals("S--") && !board[row][c][panel].equals("SX2") && !board[row][c][panel].equals("SX3") && !board[row][c][panel].startsWith("#") && !board[row][c][panel].startsWith("PK"))	
               break;	//these are all panels you might find in a city block where structures may need to be redrawn
            Structure str = structures[row][c][panel];
            if(str != null && !str.getName().startsWith("Blop") && !str.getName().startsWith("hole"))
            {
               x = c * cellSize;
               y = row * cellSize;
               if(str.getHeight()==1) //each structure has a heightDiff that is little variations in height   
                  g.drawImage(str.getPictureAndAdvance().getImage(), x, y - str.getHeightDiff(), cellSize, cellSize + str.getHeightDiff(), null);  //scaled image
               else							//add the height of the building	
                  g.drawImage(str.getPictureAndAdvance().getImage(), x, y - ((str.getHeight()-1)*cellSize) - str.getHeightDiff(), cellSize ,(str.getHeight()*cellSize) + str.getHeightDiff(), null);
               if(str.onFire())							//draw any fire on this structure
               {
                  int picSize = cellSize;
                  int dW=0;		//variation in width
                  int dH=0; 		//variation in height
                  dW = (int)(Math.random()*(cellSize/4));
                  dH = (int)(Math.random()*(cellSize/4));
                  int rand = (int)(Math.random()*4);
                  g.drawImage(fire[0][rand].getImage(), x, y, cellSize+dW, cellSize+dH, null);  //scaled image
               }
               if(str.getWebValue() > 0)	//draw webs that might be attached to this tower
               {
                  g.drawImage(web[0].getImage(), x,  y - ((str.getHeight()-1)*cellSize) - str.getHeightDiff(), cellSize, cellSize, null);
               }
            }
         }
      }
   
      for(int i=0; i<explosions.size(); i++)								//draw any explosions on the screen
      {
         int picSize = cellSize;
         Explosion curr = explosions.get(i);
         if(curr.getName().equals("BIG"))									//draw a larger explosion for aircraft
         {
            picSize*=2;
            g.drawImage(curr.getPictureAndAdvance().getImage(), curr.getX(), curr.getY(), picSize, picSize, null);  //scaled image
         }
         else
            g.drawImage(curr.getPictureAndAdvance().getImage(), curr.getX()+(picSize/2), curr.getY()+(picSize/2), picSize, picSize, null);  //scaled image
         if(curr.getAnimationIndex() >= explosionImages[0].length-1)	//remove expired explosions
         {
            explosions.remove(i);
            i--;
         }
      }
      for(int i=0; i<projectiles.size(); i++)								//draw any projectiles on the screen that are not air shots
      {
         Projectile curr = projectiles.get(i);
         if (!curr.inAir())
            g.drawImage(curr.getPictureAndAdvance().getImage(), curr.getX(), curr.getY(), cellSize, cellSize, null);  //scaled image
      }
   
   //draw any aircraft
      x =0; 
      y =0;					//upper left corner location of where image will be drawn
      tempX = 0;
      tempY = 0;			//save locations for graphic position of where the player is to be used to draw the player in motion when transitioning from one cell to another
      for(int r=0;r<board.length;r++)
      {
         x = 0;			//reset the row distance
         for(int c=0;c<board[0].length;c++)
         {
         
            for(int i=0; i<players.length; i++)
            {
               Player curr = players[i];
               if(curr==null || curr.getName().equals("NONE"))		//player 2 might not be activated
                  continue;
               String name = curr.getName();
               if(!name.startsWith("AIR") && !curr.isFlying())		//show aircraft last, so that they appear above everything else
                  continue;
               int sizeBoost = 0;//draw image slightly bigger if we are flying
               if((r>0 && c>0 && r<board.length-1 && c<board[0].length-1) ||
               ((r==0 || r==board.length-1) && (curr.getBodyDirection()==DOWN || curr.getBodyDirection()==UP)) ||
               ((c==0 || c==board[0].length-1) && (curr.getBodyDirection()==LEFT || curr.getBodyDirection()==RIGHT)))
                  sizeBoost = (int)(cellSize * 0.2);   
               if(r==curr.getRow() && c==curr.getCol())
               { 
                  if(curr.getMoveIncrX() == 0 && curr.getMoveIncrY() == 0)	//draw the crosshair on the board after the cell has been drawn
                  {					//don't advance animation bc we are not moving - subtract jump altitude if we are jumping    
                     if(curr.getHealth()<=0)		//show dead monster
                        g.drawImage(curr.getPicture(UP,0,UP).getImage(), x, y, cellSize + sizeBoost, cellSize + sizeBoost, null);  //scaled image
                     else
                        g.drawImage(curr.getPictureAndAdvance().getImage(), x, y, cellSize + sizeBoost, cellSize + sizeBoost, null);  //scaled image
                  }
                  else
                  {	
                     if(curr.isMovingLeft() || curr.isMovingUp())
                     {
                        g.drawImage(curr.getPictureAndAdvance().getImage(), x + curr.getMoveIncrX(), y + curr.getMoveIncrY(), cellSize + sizeBoost, cellSize + sizeBoost, null);  //scaled image
                     }
                     else
                     {
                        curr.setTempX(x);	//store the pixel location of where we need to draw the player if they are moving RIGHT or DOWN
                        curr.setTempY(y);
                     }
                  }
               }
            }
            x+=cellSize;
         }
         y+=cellSize;
      }
      for(int i=0; i<players.length; i++)
      {
         Player curr = players[i];
         if(curr==null || curr.getName().equals("NONE"))		
            continue;
         String name = curr.getName();
         if(!name.startsWith("AIR") && !curr.isFlying())		//show aircraft last, so that they appear above everything else
            continue;
         int sizeBoost = 0;
         int r = curr.getRow();
         int c = curr.getCol();
      //if(curr.isFlying())				//draw image slightly bigger if we are flying
         if((r>0 && c>0 && r<board.length-1 && c<board[0].length-1) ||
         ((r==0 || r==board.length-1) && (curr.getBodyDirection()==DOWN || curr.getBodyDirection()==UP)) ||
         ((c==0 || c==board[0].length-1) && (curr.getBodyDirection()==LEFT || curr.getBodyDirection()==RIGHT)))
            sizeBoost = (int)(cellSize * 0.2); 
         x = curr.getTempX() + curr.getMoveIncrX();
         y = curr.getTempY() + curr.getMoveIncrY();
         if(curr.getMoveIncrX() > 0 ||curr.getMoveIncrY() > 0)	//draw the player that is currently moving on the board after the cell has been drawn
         {
            if(curr.isMovingRight() || curr.isMovingDown())
            {
               g.drawImage(curr.getPictureAndAdvance().getImage(), x, y, cellSize + sizeBoost, cellSize + sizeBoost, null);  //scaled image
            }
         }
      }
      for(int i=0; i<projectiles.size(); i++)								//draw any projectiles on the screen that are air shots
      {
         Projectile curr = projectiles.get(i);
         if (curr.inAir())
            g.drawImage(curr.getPictureAndAdvance().getImage(), curr.getX(), curr.getY(), cellSize, cellSize, null);  //scaled image
      }
   //draw any webs strung between towers  
      for(int i=0; i<webs[panel].size(); i++)
      {
         int[]temp = webs[panel].get(i);
         g.setColor(Color.cyan);
         g.drawLine(temp[0], temp[1], temp[2], temp[3]);
      }
   //draw the mouse pointer
      if(mapMaker)		//click mouse cursor to center of cell
      {
         int mouseRow = mouseY/cellSize;
         int mouseCol = mouseX/cellSize;
         int newMouseX = (mouseCol * cellSize) + (cellSize/4);
         int newMouseY = (mouseRow * cellSize) + (cellSize/4);
         g.drawImage(cursors[4].getImage(), newMouseX, newMouseY, cellSize/2, cellSize/2, null);  //scaled image
      }
      else
         g.drawImage(cursors[cursorIndex].getImage(), mouseX, mouseY, cellSize/2, cellSize/2, null);  //scaled image
   }

//pre:   g!=null, 0<=whichMonster<playerImages.length to show a specific monster
//post:  show large version of selected player monster that rotates and turns its head
//       if whichMonster is out of range, change monster images for each rotation
   protected static void showAndRotateMonster(Graphics g, int whichMonster)
   {
      if(whichMonster < 0 || whichMonster >= playerImages.length)
         whichMonster = monster;
      if(numFrames%50 == 0)   //change monster rotation
      {
         headDir = rotation;
         rotation++;
         if(rotation > 3)
            rotation = 0;
         monster++;           //used to scroll through each monster image if the user wants to select a random monster
         if(monster >= playerImages.length)
            monster = 0;
      }	
      if(numFrames%25 == 0)   //change head rotation
      {
         headDir++;
         if(headDir > 3)
            headDir = 0;
      }
      int animFrame = 0;
      g.drawImage(new ImageIcon(playerImages[whichMonster][rotation][animFrame][headDir]).getImage(), cellSize*8, cellSize*4, cellSize*8, cellSize*8, null);
   }

//pre:  g!=null, 1<=which<=4
//post:  show large version of player selected vehicle that rotates and turns turret
   private static void showAndRotateVehicle(Graphics g, int which)
   {
      if(which < 1 || which > 4)
         return;
      if(numFrames%50 == 0)
      {
         headDir = rotation;
         rotation++;
         if(rotation > 3)
            rotation = 0;
      }	
      if(numFrames%25 == 0)
      {
         headDir++;
         if(headDir > 3)
            headDir = 0;
      }
      int animFrame = 0;
      if(which==1)
         g.drawImage(new ImageIcon(jeepImages[rotation][animFrame][headDir]).getImage(), cellSize*8, cellSize*4, cellSize*8, cellSize*8, null);
      else  if(which==2)
         g.drawImage(new ImageIcon(tankImages[rotation][animFrame][headDir]).getImage(), cellSize*8, cellSize*4, cellSize*8, cellSize*8, null);
      else  if(which==3)
         g.drawImage(new ImageIcon(flameTankImages[rotation][animFrame][headDir]).getImage(), cellSize*8, cellSize*4, cellSize*8, cellSize*8, null);
      else
         g.drawImage(new ImageIcon(missileLauncherImages[rotation][animFrame][headDir]).getImage(), cellSize*8, cellSize*4, cellSize*8, cellSize*8, null);
   }

//pre:   g != null
//post:  allows the player to select a monster before the game starts
   public static void selectGame(Graphics g)
   {
      if(highlightArea != 8)			//don't show monster for map maker selection
      {
         if(gameMode != CITY_SAVER)
         {
            if(highlightArea == 7)   //pick a random monster
               showAndRotateMonster(g, -1);
            else if(highlightArea == 0 || highlightArea == 9)
               showAndRotateMonster(g, Integer.parseInt(customInfo[1]));
            else
               showAndRotateMonster(g, highlightArea -1);
         }
         else
            showAndRotateVehicle(g, highlightArea);
      }
   //int size = 18;
      String strength = "";	//stores information for player selection
      String weakness = "";
      String special = "";
      int size = (int)(cellSize/2.25);
      int x = cellSize + size;
      int y = cellSize + size;
      int midScreen = (board.length * cellSize)/2;
      String message = "";
      g.setFont(new Font("Monospaced", Font.BOLD, (int)(size*1.3)));
      g.setColor(Color.white);
   
      message = "Welcome to MASH, MANGLE & MUNCH:";
      int writeX = midScreen - ((message.length()/2)*size);
      g.drawString(message, writeX, y+=size);
   
      x = cellSize*2 + size;
      y = cellSize*2 + size;
   
      g.setFont(new Font("Monospaced", Font.BOLD, size));
      g.setColor(Color.yellow);
   
      int leftShift = (int)(cellSize + (cellSize/4));  //amount we shift character selection image to the left
   //PLAYER OPTION 0 - use custom monster   
      if(gameMode != CITY_SAVER)
      {
         if(highlightArea == 0 )
         {
            g.setColor(Color.yellow.brighter());
            strength = ("it is your own creation:");
            special =  ("what is not to love?");
         }
         else
            g.setColor(Color.yellow.darker());
         g.drawString("0) "+customInfo[0], x, y+=size);
         if(highlightArea==0)
            g.drawImage(new ImageIcon(playerImages[Integer.parseInt(customInfo[1])][DOWN][0][DOWN]).getImage(), x - leftShift, y-cellSize/2, cellSize, cellSize, null);
         else
            g.drawImage(new ImageIcon(playerImages[Integer.parseInt(customInfo[1])][LEFT][0][LEFT]).getImage(), x - leftShift, y-cellSize/2, cellSize, cellSize, null);
      }
      else
         y+=size;
     
   //PLAYER OPTION 1
      y+=size*2;	
      if(highlightArea==1)  
      {
         g.setColor(Color.yellow.brighter());
         if(gameMode == CITY_SAVER)
         {
            strength = ("very fast, rotating machine gun.");
            weakness = ("low armor, low firepower.");
         }
         else
            if(gameMode == EARTH_INVADERS)
            {
               strength = ("throws cars at high velocity.");
            }
            else
            {
               strength = ("fast, ambidextorous.");
               weakness = ("water, low stomp power.");
               special = ("gains health by eating.");
            }
      }
      else
         g.setColor(Color.yellow.darker());
      if(gameMode == CITY_SAVER)
      {
         g.drawString("1) CAR jeep", x, y+=size);
         if(highlightArea==1)
            g.drawImage(new ImageIcon(jeepImages[RIGHT][0][0]).getImage(), x - leftShift, y-cellSize/2, cellSize, cellSize, null);
         else
            g.drawImage(new ImageIcon(jeepImages[UP][0][0]).getImage(), x - leftShift, y-cellSize/2, cellSize, cellSize, null);
      }
      else
      {   
         g.drawString("1) King-Clunk", x, y+=size);
         if(highlightArea==1)
            g.drawImage(new ImageIcon(playerImages[0][DOWN][0][DOWN]).getImage(), x - leftShift, y-cellSize/2, cellSize, cellSize, null);
         else
            g.drawImage(new ImageIcon(playerImages[0][RIGHT][0][DOWN]).getImage(), x - leftShift, y-cellSize/2, cellSize, cellSize, null);
      }
      g.setFont(new Font("Monospaced", Font.ITALIC, (int)(size*.75)));
      g.setColor(Color.cyan);
      if(highlightArea==1)
         g.setColor(Color.white);
      else
         g.setColor(Color.cyan);
   
   //PLAYER OPTION 2	
      y+=size*2;
      g.setFont(new Font("Monospaced", Font.BOLD, size));
      if(highlightArea==2)
      {
         g.setColor(Color.yellow.brighter());
         if(gameMode == CITY_SAVER)
         {
            strength = ("rotating cannon, high firepower.");
            weakness = ("slow speed.");
         }
         else
            if(gameMode == EARTH_INVADERS)
            {
               strength = ("devestating fire breath.");
            }
            else
            {
               strength = ("good stomp power, fire breath.");
               weakness = ("slow on land.");
               special = ("regenerates in water.");
            } 
      }
      else
         g.setColor(Color.yellow.darker());
      if(gameMode == CITY_SAVER)
      {
         g.drawString("2) TANK tank", x, y+=size);
         if(highlightArea==2)
            g.drawImage(new ImageIcon(tankImages[LEFT][0][0]).getImage(), x - leftShift, y-cellSize/2, cellSize, cellSize, null);
         else
            g.drawImage(new ImageIcon(tankImages[UP][0][0]).getImage(), x - leftShift, y-cellSize/2, cellSize, cellSize, null);
      }
      else
      {   
         g.drawString("2) Gobzilly", x, y+=size);
         if(highlightArea==2)
            g.drawImage(new ImageIcon(playerImages[1][LEFT][1][DOWN]).getImage(), x - leftShift, y-cellSize/2, cellSize, cellSize, null);
         else
            g.drawImage(new ImageIcon(playerImages[1][LEFT][0][LEFT]).getImage(), x - leftShift, y-cellSize/2, cellSize, cellSize, null);
      }
      g.setFont(new Font("Monospaced", Font.ITALIC, (int)(size*.75)));
      if(highlightArea==2)
         g.setColor(Color.white);
      else
         g.setColor(Color.cyan);
        
   //PLAYER OPTION 3	
      y+=size*2;
      g.setFont(new Font("Monospaced", Font.BOLD, size));
      if(highlightArea==3)
      {
         g.setColor(Color.yellow.brighter());
         if(gameMode == CITY_SAVER)
         {
            strength = ("slightly faster, shoots flames.");
            weakness = ("higher chance of collateral damage.");
         }
         else
            if(gameMode == EARTH_INVADERS)
            {
               strength = ("360 degree rotating death ray.");
            }
            else
            {
               strength = ("360 degree rotating death ray.");
               weakness = ("constantly loosing energy.");
               special = ("absorb energy from power stations.");
            }
      }
      else
         g.setColor(Color.yellow.darker());
      if(gameMode == CITY_SAVER)
      {
         g.drawString("3) TANK flame", x, y+=size);
         if(highlightArea==3)
            g.drawImage(new ImageIcon(flameTankImages[RIGHT][0][0]).getImage(), x - leftShift, y-cellSize/2, cellSize, cellSize, null);
         else
            g.drawImage(new ImageIcon(flameTankImages[UP][0][0]).getImage(), x - leftShift, y-cellSize/2, cellSize, cellSize, null);
      }
      else
      {   
         g.drawString("3) BoobooTron", x, y+=size);
         if(highlightArea==3)
            g.drawImage(new ImageIcon(playerImages[2][DOWN][0][DOWN]).getImage(), x - leftShift, y-cellSize/2, cellSize, cellSize, null);
         else
            g.drawImage(new ImageIcon(playerImages[2][RIGHT][0][RIGHT]).getImage(), x - leftShift, y-cellSize/2, cellSize, cellSize, null);
      }
      g.setFont(new Font("Monospaced", Font.ITALIC, (int)(size*.75)));
      if(highlightArea==3)
         g.setColor(Color.white);
      else
         g.setColor(Color.cyan);
        
   //PLAYER OPTION 4	
      y+=size*2;
      g.setFont(new Font("Monospaced", Font.BOLD, size));
      if(highlightArea==4)
      {
         g.setColor(Color.yellow.brighter());
         if(gameMode == CITY_SAVER)
         {
            strength = ("slow, massive firepower.");
            weakness = ("collateral damage is certain.");
         }
         else
            if(gameMode == EARTH_INVADERS)
            {
               strength = ("shriek attack.");
            }
            else
            {
               strength = ("can fly, lands with a mighty thump.");
               weakness = ("easily damaged by projectiles and explosives.");
               special = ("shriek attack can stun enemies.");
            }
      }
      else
         g.setColor(Color.yellow.darker());
      if(gameMode == CITY_SAVER)
      {
         g.drawString("4) TANK missile", x, y+=size);
         if(highlightArea==4)
            g.drawImage(new ImageIcon(missileLauncherImages[RIGHT][0][0]).getImage(), x - leftShift, y-cellSize/2, cellSize, cellSize, null);
         else
            g.drawImage(new ImageIcon(missileLauncherImages[UP][0][0]).getImage(), x - leftShift, y-cellSize/2, cellSize, cellSize, null);
      }
      else
      {     
         g.drawString("4) WoeMantis", x, y+=size);
         if(highlightArea==4)
            g.drawImage(new ImageIcon(playerImages[3][DOWN][0][DOWN]).getImage(), x - leftShift, y-cellSize/2, cellSize, cellSize, null);
         else
            g.drawImage(new ImageIcon(playerImages[3][RIGHT][0][DOWN]).getImage(), x - leftShift, y-cellSize/2, cellSize, cellSize, null);
      }
      g.setFont(new Font("Monospaced", Font.ITALIC, (int)(size*.75)));
      if(highlightArea==4)
         g.setColor(Color.white);
      else
         g.setColor(Color.cyan);
        
   //PLAYER OPTION 5	
      y+=size*2;
      g.setFont(new Font("Monospaced", Font.BOLD, size));
      if(highlightArea==5)
      {
         g.setColor(Color.yellow.brighter());
         if(gameMode == EARTH_INVADERS)
         {
            strength = ("fires glops of corrosive sludge.");
         }
         else if(gameMode != CITY_SAVER)
         {
            strength = ("can eat almost everything, corrodes buildings.");
            weakness = ("fire & explosives, no projectile attack.");
            special = ("impervious to projectiles, can separate/recombine.");
         }
      }
      else
         g.setColor(Color.yellow.darker());
      if(gameMode == CITY_SAVER)
      {
         g.drawString("5) Take your chances", x, y+=size);
         if(highlightArea==5)
            special = "we will pick for you.";
      }
      else
      {
         g.drawString("5) The-Blop", x, y+=size);
         if(highlightArea==5)
            g.drawImage(new ImageIcon(playerImages[4][DOWN][0][DOWN]).getImage(), x - leftShift, y-cellSize/2, cellSize, cellSize, null);
         else
            g.drawImage(new ImageIcon(playerImages[4][RIGHT][0][DOWN]).getImage(), x - leftShift, y-cellSize/2, cellSize, cellSize, null);
      }
      g.setFont(new Font("Monospaced", Font.ITALIC, (int)(size*.75)));
      if(highlightArea==5)
         g.setColor(Color.white);
      else
         g.setColor(Color.cyan);
   
   //PLAYER OPTION 6  
      if(gameMode != CITY_SAVER)
      {        
         y+=size*2;
         g.setFont(new Font("Monospaced", Font.BOLD, size));
         if(highlightArea==6)
         {
            g.setColor(Color.yellow.brighter());
            if(gameMode != CITY_SAVER)
            {
               if(gameMode == EARTH_INVADERS)
               {
                  strength = ("fires a sticky web.");
               }
               else
               {
                  strength = ("fires a sticky web.");
                  weakness = ("water/electricity/fire, can't jump.");
                  special =  ("can dig underground.");
               }
            }
         }
         else
            g.setColor(Color.yellow.darker());
      
         g.drawString("6) Wormoid", x, y+=size);
         if(highlightArea==6)
            g.drawImage(new ImageIcon(playerImages[5][LEFT][0][DOWN]).getImage(), x - leftShift, y-cellSize/2, cellSize, cellSize, null);
         else
            g.drawImage(new ImageIcon(playerImages[5][LEFT][0][LEFT]).getImage(), x - leftShift, y-cellSize/2, cellSize, cellSize, null);
      
      //PLAYER OPTION 7         
         y+=size*2;
         if(gameMode != CITY_SAVER)
         {
            g.setFont(new Font("Monospaced", Font.BOLD, size));
            if(highlightArea==7)
            {
               g.setColor(Color.yellow.brighter());
               special = "we will pick for you."; 
            }
            else
               g.setColor(Color.yellow.darker());
            g.drawString("7) Take your chances", x, y+=size);
         }
      
      //OPTION 8
         y+=size*2;
         if(gameMode != CITY_SAVER)
         {
            g.setFont(new Font("Monospaced", Font.BOLD, size));
            if(highlightArea==8)
            {
               g.setColor(Color.yellow.brighter());
               special = "build a custom map."; 
            }
            else
               g.setColor(Color.yellow.darker());
            g.drawString("8) Map Maker", x, y+=size);
         
            g.setFont(new Font("Monospaced", Font.BOLD, size));
            y+=size*2;
            if(highlightArea==9)
            {
               g.setColor(Color.yellow.brighter());
               special = "grow a custom monster."; 
            }
            else
               g.setColor(Color.yellow.darker());
            g.drawString("9) Monster Maker", x, y+=size);
         }
      }
      g.setFont(new Font("Monospaced", Font.ITALIC, size));
      g.setColor(Color.white);
   //y+=size; 
      if(gameMode == CITY_SAVER)
         y+=size*6; 
      if(strength.length() > 0)
         g.drawString("STRENGTH: " + strength, x-50, y+=size);
      if(weakness.length() > 0)
         g.drawString("WEAKNESS: " + weakness, x-50, y+=size);
      if(special.length() > 0)
         g.drawString("SPECIAL : " + special, x-50, y+=size);
   //draw the mouse pointer
      g.drawImage(cursors[4].getImage(), mouseX, mouseY, cellSize/2, cellSize/2, null);  //scaled image
   }

//pre: g!=null, x>=0, y >=0, size > 0
//post:displays monster narratives depending on which monster is highlighted (as opposed to showing key commands)
   private static void showNarratives(Graphics g, int x, int y, int size)
   {
      y = size*24;
      g.setFont(new Font("Monospaced", Font.BOLD, (int)(size*.75)));
      g.setColor(Color.orange);
   
      if(highlightArea==0)
      {
         g.drawString("Use the Monster Maker to  ", x, y+=((int)(size*.75)));
         g.drawString("create your own monster - ", x, y+=((int)(size*.75)));
         g.drawString("Use Monster Money credits ", x, y+=((int)(size*.75)));
         g.drawString("to assign attributes and  ", x, y+=((int)(size*.75)));
         g.drawString("abilities.  Then unleash  ", x, y+=((int)(size*.75)));
         g.drawString("your precious creation on ", x, y+=((int)(size*.75)));
         g.drawString("an unsuspecting city, or  ", x, y+=((int)(size*.75)));
         g.drawString("attempt to stop your      ", x, y+=((int)(size*.75)));
         g.drawString("rampaging creation in CITY", x, y+=((int)(size*.75)));
         g.drawString("SAVER mode.               ", x, y+=((int)(size*.75)));
      }
      else if(highlightArea==1)
      {
         g.drawString("The result of genetic     ", x, y+=((int)(size*.75)));
         g.drawString("experiments gone wrong,   ", x, y+=((int)(size*.75)));
         g.drawString("King Clunk is a giant ape ", x, y+=((int)(size*.75)));
         g.drawString("that ended up as a circus ", x, y+=((int)(size*.75)));
         g.drawString("exibit.  Tired of his     ", x, y+=((int)(size*.75)));
         g.drawString("tormenters, Clunk escaped ", x, y+=((int)(size*.75)));
         g.drawString("from his captors to wreak ", x, y+=((int)(size*.75)));
         g.drawString("havoc on the metropolis.  ", x, y+=((int)(size*.75)));
         g.drawString("Clunk is the fastest of   ", x, y+=((int)(size*.75)));
         g.drawString("the monsters, and can pick", x, y+=((int)(size*.75)));
         g.drawString("up cars and buses to use  ", x, y+=((int)(size*.75)));
         g.drawString("as projectile weapons     ", x, y+=((int)(size*.75)));
         g.drawString("against enemy units or    ", x, y+=((int)(size*.75)));
         g.drawString("buildings.  He can't swim ", x, y+=((int)(size*.75)));
         g.drawString("very well, but can regain ", x, y+=((int)(size*.75)));
         g.drawString("health by eating crowds,  ", x, y+=((int)(size*.75)));
         g.drawString("cars and pesky newscopters", x, y+=((int)(size*.75)));
      }
      else if(highlightArea==2)
      {
         g.drawString("Awaken from her ancient   ", x, y+=((int)(size*.75)));
         g.drawString("slumber from the bottom of", x, y+=((int)(size*.75)));
         g.drawString("the sea, Gobzilly is a    ", x, y+=((int)(size*.75)));
         g.drawString("menace from a past long   ", x, y+=((int)(size*.75)));
         g.drawString("gone.  Slow on land, fast ", x, y+=((int)(size*.75)));
         g.drawString("in the water and with     ", x, y+=((int)(size*.75)));
         g.drawString("destruction on her mind,  ", x, y+=((int)(size*.75)));
         g.drawString("Gobzilly can breathe fire ", x, y+=((int)(size*.75)));
         g.drawString("to destroy enemy units or ", x, y+=((int)(size*.75)));
         g.drawString("light structures ablaze.  ", x, y+=((int)(size*.75)));
         g.drawString("Food will only curb her   ", x, y+=((int)(size*.75)));
         g.drawString("appetite and not restore  ", x, y+=((int)(size*.75)));
         g.drawString("health.  Only the water   ", x, y+=((int)(size*.75)));
         g.drawString("can regenerate her if she ", x, y+=((int)(size*.75)));
         g.drawString("is not weakened from      ", x, y+=((int)(size*.75)));
         g.drawString("hunger.  Stick close to   ", x, y+=((int)(size*.75)));
         g.drawString("the water Gobzilly!       ", x, y+=((int)(size*.75)));
      }
      else if(highlightArea==3)
      {
         g.drawString("A robotic visitor from    ", x, y+=((int)(size*.75)));
         g.drawString("another world, Boobootron ", x, y+=((int)(size*.75)));
         g.drawString("is lost, confused and     ", x, y+=((int)(size*.75)));
         g.drawString("growing more irritated by ", x, y+=((int)(size*.75)));
         g.drawString("the minute.  He has a     ", x, y+=((int)(size*.75)));
         g.drawString("powerful death-ray that   ", x, y+=((int)(size*.75)));
         g.drawString("can rotate any direction, ", x, y+=((int)(size*.75)));
         g.drawString("and is well adept at      ", x, y+=((int)(size*.75)));
         g.drawString("stomping on buildings.    ", x, y+=((int)(size*.75)));
         g.drawString("But far removed from his  ", x, y+=((int)(size*.75)));
         g.drawString("homeworld and core power  ", x, y+=((int)(size*.75)));
         g.drawString("source, Boobootron is     ", x, y+=((int)(size*.75)));
         g.drawString("constantly losing energy. ", x, y+=((int)(size*.75)));
         g.drawString("He can only regain energy ", x, y+=((int)(size*.75)));
         g.drawString("by absorbing power        ", x, y+=((int)(size*.75)));
         g.drawString("stations, electrical      ", x, y+=((int)(size*.75)));
         g.drawString("towers and trains.        ", x, y+=((int)(size*.75)));
      }
      else if(highlightArea==4)
      {
         g.drawString("Oil drillers on a newly   ", x, y+=((int)(size*.75)));
         g.drawString("discovered island found   ", x, y+=((int)(size*.75)));
         g.drawString("the WoeMantis nest and    ", x, y+=((int)(size*.75)));
         g.drawString("stole her eggs.  WoeMantis", x, y+=((int)(size*.75)));
         g.drawString("is now out on a frantic   ", x, y+=((int)(size*.75)));
         g.drawString("mission to find her kids  ", x, y+=((int)(size*.75)));
         g.drawString("and wreck anything that   ", x, y+=((int)(size*.75)));
         g.drawString("gets in her way.  She is  ", x, y+=((int)(size*.75)));
         g.drawString("fast, can fly and has a   ", x, y+=((int)(size*.75)));
         g.drawString("powerful shriek that can  ", x, y+=((int)(size*.75)));
         g.drawString("stun her enemies.  But she", x, y+=((int)(size*.75)));
         g.drawString("is vulnerable to projectiles  ", x, y+=((int)(size*.75)));
         g.drawString("and explosives and she    ", x, y+=((int)(size*.75)));
         g.drawString("can't swim very well.  Be ", x, y+=((int)(size*.75)));
         g.drawString("careful in flight - all   ", x, y+=((int)(size*.75)));
         g.drawString("armed units will be able  ", x, y+=((int)(size*.75)));
         g.drawString("to see you.               ", x, y+=((int)(size*.75)));
      }
      else if(highlightArea==5)
      {
         g.drawString("The offspring of the Bird-", x, y+=((int)(size*.75)));
         g.drawString("Flu and the Superbug, the ", x, y+=((int)(size*.75)));
         g.drawString("mighty Blop was disposed  ", x, y+=((int)(size*.75)));
         g.drawString("of by the CDC in a vat of ", x, y+=((int)(size*.75)));
         g.drawString("toxic waste.  Growing to  ", x, y+=((int)(size*.75)));
         g.drawString("enormous size with an     ", x, y+=((int)(size*.75)));
         g.drawString("insatiable appetite, the  ", x, y+=((int)(size*.75)));
         g.drawString("amorphous mass leaves a   ", x, y+=((int)(size*.75)));
         g.drawString("trail of toxic sludge     ", x, y+=((int)(size*.75)));
         g.drawString("wherever it goes.  Lay    ", x, y+=((int)(size*.75)));
         g.drawString("waste to too much space   ", x, y+=((int)(size*.75)));
         g.drawString("and delicious humans will ", x, y+=((int)(size*.75)));
         g.drawString("not return, so be careful.", x, y+=((int)(size*.75)));
         g.drawString("When at full health, the  ", x, y+=((int)(size*.75)));
         g.drawString("Blop can split in two at  ", x, y+=((int)(size*.75)));
         g.drawString("the expense of losing some", x, y+=((int)(size*.75)));
         g.drawString("health, or recombine back.", x, y+=((int)(size*.75)));
      }
      else if(highlightArea==6)
      {
         g.drawString("While fracking for natural", x, y+=((int)(size*.75)));
         g.drawString("gas, the Shadow Government", x, y+=((int)(size*.75)));
         g.drawString("Energy Conglomerate (SGEC)", x, y+=((int)(size*.75)));
         g.drawString("disturbed the subteranian ", x, y+=((int)(size*.75)));
         g.drawString("nest of Wormoid, who is   ", x, y+=((int)(size*.75)));
         g.drawString("out to silence her noisy  ", x, y+=((int)(size*.75)));
         g.drawString("neighbors upstairs.  She  ", x, y+=((int)(size*.75)));
         g.drawString("can not jump, but she sure", x, y+=((int)(size*.75)));
         g.drawString("can dig to sneak up on    ", x, y+=((int)(size*.75)));
         g.drawString("unsuspecting civilians or ", x, y+=((int)(size*.75)));
         g.drawString("military units.  Take care", x, y+=((int)(size*.75)));
         g.drawString("to not dig under electric ", x, y+=((int)(size*.75)));
         g.drawString("towers or water.  She has ", x, y+=((int)(size*.75)));
         g.drawString("a web to capture food to  ", x, y+=((int)(size*.75)));
         g.drawString("eat, or to string between ", x, y+=((int)(size*.75)));
         g.drawString("two towers to destroy low ", x, y+=((int)(size*.75)));
         g.drawString("flying aircraft.  Nifty.  ", x, y+=((int)(size*.75)));
      }
      else if(highlightArea==7)
      {
         g.drawString("What is life without a    ", x, y+=((int)(size*.75)));
         g.drawString("little risk?  We will pick", x, y+=((int)(size*.75)));
         g.drawString("a monster for you, and    ", x, y+=((int)(size*.75)));
         g.drawString("promise to make it a good ", x, y+=((int)(size*.75)));
         g.drawString("one!  Let's GO!           ", x, y+=((int)(size*.75)));
      }
      else if(highlightArea==8)
      {
         g.drawString("Were you mugged in Detroit", x, y+=((int)(size*.75)));
         g.drawString("or swindled in LA? Do you ", x, y+=((int)(size*.75)));
         g.drawString("hold a grudge against a   ", x, y+=((int)(size*.75)));
         g.drawString("city that is not in the   ", x, y+=((int)(size*.75)));
         g.drawString("game for you to destroy?  ", x, y+=((int)(size*.75)));
         g.drawString("Use the Map Maker to bring", x, y+=((int)(size*.75)));
         g.drawString("a city of your creation   ", x, y+=((int)(size*.75)));
         g.drawString("into the game for you to  ", x, y+=((int)(size*.75)));
         g.drawString("trample on.               ", x, y+=((int)(size*.75)));
      }
      else  if(highlightArea==9)
      {
         g.drawString("Use the Monster Maker to  ", x, y+=((int)(size*.75)));
         g.drawString("create your own monster - ", x, y+=((int)(size*.75)));
         g.drawString("Use Monster Money credits ", x, y+=((int)(size*.75)));
         g.drawString("to assign attributes and  ", x, y+=((int)(size*.75)));
         g.drawString("abilities.  Then unleash  ", x, y+=((int)(size*.75)));
         g.drawString("your precious creation on ", x, y+=((int)(size*.75)));
         g.drawString("an unsuspecting city, or  ", x, y+=((int)(size*.75)));
         g.drawString("attempt to stop your      ", x, y+=((int)(size*.75)));
         g.drawString("rampaging creation in CITY", x, y+=((int)(size*.75)));
         g.drawString("SAVER mode.               ", x, y+=((int)(size*.75)));
      }
   
   }

//post: shows game stats in a window with upper left coordinates x,y with dimensions width * height
//display that shows health, hunger, paw contents, energy (godsilly & boobootron), damage meter, threat level 
   public static void displayStats(Graphics g, int x, int y, int width, int height)	
   {
      int size = (int)(cellSize/2.25);
      x+=size;
      y+=size;
      g.setFont(new Font("Monospaced", Font.BOLD, size));
      g.setColor(Color.yellow);
      int health = 0;
      if(monsterMaker)
      {  
         MonsterMaker.monsterDisplay(g, x, y, size);
         return;
      }
      else if(mapMaker)
      {
         MapBuilder.showInfo(g, x, y);
         return;
      }
      if(gameStarted)
      {
         Player curr = players[PLAYER1];
         health = curr.getHealth();
         String name = curr.getName();
         if(gameMode==CITY_SAVER)
         {
            int spaceLoc = name.indexOf("-");
            if(spaceLoc != -1)		//truncate the name so it fits
               name = name.substring(spaceLoc+1).toUpperCase();   
         }
         if(name.length() > 10)
            name = name.substring(0,10);
         g.drawString(name, x, y+=size);
         if(p1partner)
         {
            String p2name = new String(players[PLAYER2].getName());
            if(p2name.equals("BoobooTron"))	//make the p1partner name fit
               p2name = "BooTron";
            else
               if(p2name.equals("King-Clunk"))	//make the p1partner name fit
                  p2name = "Clunk";
               else
                  if(p2name.equals("The-Blop"))	//make the p1partner name fit
                     p2name = "Blop";
                  else
                     if(p2name.equals("Gobzilly"))	//make the p1partner name fit
                        p2name = "Zilly";
                     else
                        if(p2name.equals("WoeMantis"))	//make the p1partner name fit
                           p2name = "Woe";
                        else if(p2name.length() > 7)
                           p2name = p2name.substring(0,7);  
            g.setColor(Color.yellow.darker().darker());
            g.drawString(p2name, x+125, y);
            g.setColor(Color.yellow);
         }
         if(health > 0)
         {
            y+=size;
            if(gameMode != CITY_SAVER)
            {
               if(curr.energyAbsorber())
                  g.drawString("ENERGY: "+health, x, y);
               else
                  g.drawString("HEALTH: "+health, x, y);
            }  
            if(p1partner)
            {
               g.setColor(Color.yellow.darker().darker());
               g.drawString(""+players[PLAYER2].getHealth(), x+125, y);
               g.setColor(Color.yellow);
            }   
         }
         else
         {
            y+=size;
            if(gameMode == CITY_SAVER)
               g.drawString("STATUS: ", x, y);
            else
               if(curr.energyAbsorber())
                  g.drawString("ENERGY: ", x, y);
               else
                  g.drawString("HEALTH: ", x, y);
         
            g.setFont(new Font("Monospaced", Font.ITALIC, size));
            g.setColor(Color.red);
            if(monsterWins)  
               g.drawString("VICTORIOUS!", x+30, y+=size);
            else
               if(curr.energyAbsorber())
                  g.drawString("DEPLETED", x+30, y+=size);
               else
                  if(gameMode == CITY_SAVER)
                  {
                     g.drawString("DEFEATED", x+30, y+=size);
                     propertyValue = 0;
                  }
                  else
                     g.drawString("DEAD", x+30, y+=size);
         }
         g.setFont(new Font("Monospaced", Font.BOLD, size));
         g.setColor(Color.yellow);
         y+=size;
         if(health > 0)
         {
            if(gameMode == MONSTER_MASH && curr instanceof Monster)
            {
               if(curr.energyAbsorber())
               {
                  int hunger = ((Monster)(curr)).getHunger();  
                  g.drawString("STATUS:", x, y+=size);
                  if(health < 25 )
                  {
                     g.setFont(new Font("Monospaced", Font.ITALIC, size));
                     g.setColor(Color.red);
                  }
                  int sl=healthToStatusIndex(curr.getHealth());
                  g.drawString(""+statusLevels[sl], x, y+=size);
                  if(p1partner && players[PLAYER2] instanceof Monster)
                  {
                     g.setFont(new Font("Monospaced", Font.BOLD, size));
                     g.setColor(Color.yellow.darker().darker());
                     if(players[PLAYER2].energyAbsorber())
                     {
                        if(players[PLAYER2].getHealth() < 25 )
                           g.setFont(new Font("Monospaced", Font.ITALIC, size));
                        sl=healthToStatusIndex(players[PLAYER2].getHealth());
                        g.drawString(""+statusLevels[sl], x+125, y-size);
                     }  
                     else
                     {
                        int hungerLevel = ((Monster)(players[PLAYER2])).getHunger();
                        if(hungerLevel >= hungerLevels.length -1 )
                           g.setFont(new Font("Monospaced", Font.ITALIC, size));
                        g.drawString(""+hungerLevels[hungerLevel], x+125, y-size);
                     }
                     g.setFont(new Font("Monospaced", Font.BOLD, size));
                     g.setColor(Color.yellow);
                  } 
               }
               else
               {
                  int hunger = ((Monster)(curr)).getHunger();  
                  g.drawString("HUNGER:", x, y+=size);
                  if(hunger >= hungerLevels.length -1 )
                  {
                     g.setFont(new Font("Monospaced", Font.ITALIC, size));
                     g.setColor(Color.red);
                  }
                  g.drawString(""+hungerLevels[hunger], x, y+=size);
                  if(p1partner)
                  {
                     g.setFont(new Font("Monospaced", Font.BOLD, size));
                     g.setColor(Color.yellow.darker().darker());
                     if(players[PLAYER2].energyAbsorber())
                     {
                        if(players[PLAYER2].getHealth() < 25 )
                           g.setFont(new Font("Monospaced", Font.ITALIC, size));
                        int sl=healthToStatusIndex(players[PLAYER2].getHealth());
                        g.drawString(""+statusLevels[sl], x+125, y-size);
                     }  
                     else
                     {
                        int hungerLevel = ((Monster)(players[PLAYER2])).getHunger();
                        if(hungerLevel >= hungerLevels.length -1 )
                           g.setFont(new Font("Monospaced", Font.ITALIC, size));
                        g.drawString(""+hungerLevels[hungerLevel], x+125, y-size);
                     }
                     g.setFont(new Font("Monospaced", Font.BOLD, size));
                     g.setColor(Color.yellow);
                  }            
               }
            }
            else	//we only need to see hunger level with MONSTER_MASH
               y+=(size*2);
         
                
            y+=size;
            g.setFont(new Font("Monospaced", Font.BOLD, size));
            g.setColor(Color.yellow);
         
            if(curr.head360() || curr.getName().endsWith("jeep") || curr.getName().startsWith("TANK"))
            {
               if(gameMode == CITY_SAVER)
                  g.drawString("CANNON DIR:", x, y+=size);
               else
                  g.drawString("BEAM DIR:", x, y+=size);
               g.setFont(new Font("Monospaced", Font.ITALIC, size));
               g.setColor(Color.cyan);
               g.drawString(compass[curr.getHeadDirection()], x, y+=size);
            }
            else if(gameMode==MONSTER_MASH && curr instanceof Monster && !curr.cantGrab())
            //for monsters that can't grab, we shouldn't show what is in its claws
            {
               g.drawString("CLAW:", x, y+=size);
               g.setFont(new Font("Monospaced", Font.ITALIC, size));
               String[] contents = ((Monster)(curr)).getClawContents();
               if(!contents[0].equals("empty"))
               {
                  g.setColor(Color.cyan);
                  if(p1partner)
                  {
                     int spaceLoc = contents[0].indexOf(" ");
                     if(spaceLoc != -1)		//truncate the description of p1 claw contents so it fits
                        contents[0] = contents[0].substring(0,spaceLoc);
                  }
               }
               if(curr.isAmbidextorous())	//King Clunk
                  g.drawString("L:"+contents[0], x, y+=size);
               else
                  g.drawString(contents[0], x, y+=size);
               g.setColor(Color.yellow);
               if(curr.isAmbidextorous())	//King Clunk
               {
                  if(!contents[1].equals("empty"))
                  {
                     g.setColor(Color.cyan);
                     if(p1partner)
                     {
                        int spaceLoc = contents[1].indexOf(" ");
                        if(spaceLoc != -1)		//truncate the description of p1 claw contents so it fits
                           contents[1] = contents[1].substring(0,spaceLoc);
                     }
                  
                  }
                  g.drawString("R:"+contents[1], x, y+size);
               }
               if(p1partner && players[PLAYER2] instanceof Monster)
               {
                  contents = ((Monster)(players[PLAYER2])).getClawContents();
                  int spaceLoc = contents[0].indexOf(" ");
                  if(spaceLoc != -1)		//truncate the description of p2 claw contents so it fits
                     contents[0] = contents[0].substring(0,spaceLoc);
                  g.setColor(Color.yellow.darker().darker());
                  g.drawString(contents[0], x+125, y-size);
                  if(players[PLAYER2].isAmbidextorous())
                  {
                     spaceLoc = contents[1].indexOf(" ");
                     if(spaceLoc != -1)		//truncate the description of p2 claw contents so it fits
                        contents[1] = contents[1].substring(0,spaceLoc);
                     g.setColor(Color.yellow.darker().darker());
                     g.drawString(contents[1], x+125, y);
                  }
               } 
            }
            else	//we only need to show claw contents in MONSTER_MASH
               y+=(size*2);
            g.setFont(new Font("Monospaced", Font.BOLD, size));
            g.setColor(Color.yellow);
            y+=size;
         }
         else	//show kill stats
         {//crowds, cars, boats, aircraft, buildings
            g.setColor(Color.cyan);
            g.drawString("UNITS MASHED:", x, y+=size);
            g.drawString("CROWDS: "+ killStats[0], x, y+=size);
            g.drawString("CARS  : "+ killStats[1], x, y+=size);
            g.drawString("BOATS : "+ killStats[2], x, y+=size);
            g.drawString("AIRCFT: "+ killStats[3], x, y+=size);
            g.drawString("BLDGS : "+ killStats[4], x, y+=size);
            g.drawString("TOTAL : "+ killStats[5], x, y+=size);
         }
         g.setColor(Color.yellow);
         String temp = "";
         if(gameMode == CITY_SAVER)
         {
            temp = formatDamage(propertyValue);
            g.drawString("PROPERTY VALUE:", x, y+=size);
         }
         else
         {
            temp = formatDamage(propertyDamage);
            g.drawString("PROPERTY DAMAGE:", x, y+=size);
         }
         g.drawString(temp, x, y+=size);
         y+=size;	
         if(gameMode==MONSTER_MASH || gameMode==CITY_SAVER)
         {
            g.drawString("RESPONDERS:", x, y+=size);
            if(threatLevel >= threatLevels.length - 1)
            {
               g.setFont(new Font("Monospaced", Font.ITALIC, size));
               g.setColor(Color.red);
            }
            g.drawString(threatLevels[threatLevel], x, y+=size);
         }
         else
            y+=(size*2);
         g.setFont(new Font("Monospaced", Font.BOLD, size));
         g.setColor(Color.yellow);
      
         y+=size;
         g.drawString("TIME: "+time, x, y+=size);
      
         if(health > 0)
         {
            y+=size;
            g.drawString("WIND: "+compass[windDirection], x, y+=size);
         }              	
      
         y+=size*2;
         if(pause)
         {
            g.setFont(new Font("Monospaced", Font.ITALIC, size));
            g.setColor(Color.red);
            g.drawString("GAME PAUSED", x, y);
         }
         else
            if(health > 0 && numFrames <= messageTime + messageDuration)  
            {
               g.setFont(new Font("Monospaced", Font.ITALIC, size));
               g.setColor(Color.red);
               g.drawString(message, x, y);
            }
      }
   
      if(!gameStarted && !titleScreen && (highlightArea>=0 && highlightArea<=23) && gameMode != CITY_SAVER)        
      {	//show narrative of monster that is moused over
         showNarratives(g, x, y, size);
      }
      else if(health > 0 || !gameStarted)
      {
         if(!gameStarted)						//adjust text height to show high scores before game starts
            y = size*21;
      
         g.setFont(new Font("Monospaced", Font.BOLD, (int)(size*.75)));
         g.setColor(Color.cyan.darker().darker());
         y+=(int)(size*.75);
         g.drawString("GAME DELAY: " + t.getDelay(), x, y+=(int)(size*.75));
         if(!toggleMap || gameMode==CITY_SAVER || gameMode==EARTH_INVADERS)
         {
            g.setFont(new Font("Monospaced", Font.BOLD, (int)(size*.75)));
            g.setColor(Color.orange.darker());
            y+=(int)(size*.45);
            g.drawString("Move       : ARROW keys", x, y+=(int)(size*.45));
            y+=(int)(size*.45);
            if(gameMode == CITY_SAVER || gameMode == EARTH_INVADERS || gameMode == BOMBER_DODGER)
               y+=(int)(size*.45);
            else
               g.drawString("Grab:CTRL    Eat:SHIFT", x, y+=(int)(size*.45));
            y+=(int)(size*.45);
            if(gameMode == CITY_SAVER)
               y+=(int)(size*.45);
            else
               if(players[PLAYER1].isFlyer())
                  g.drawString("Fly/land   : SPACE", x, y+=(int)(size*.45));
               else
                  if(players[PLAYER1].isDigger())
                     g.drawString("Dig/surface: SPACE", x, y+=(int)(size*.45));
                  else
                     g.drawString("Stomp/jump : SPACE", x, y+=(int)(size*.45));
         
            y+=(int)(size*.45);
            if(players[PLAYER1].getName().equals("The-Blop") && gameMode != EARTH_INVADERS)
            {
               if(blopSplit && players[BLOPSPLIT].getName().equals("The-Blop"))
                  g.drawString("Recombine  : ENTER", x, y+=(int)(size*.45));
               else
                  g.drawString("Split      : ENTER", x, y+=(int)(size*.45));
            }
            else
               g.drawString("Shoot      : ENTER", x, y+=(int)(size*.45));
            y+=(int)(size*.45);
            if(gameMode == CITY_SAVER)
               y+=(int)(size*.45);
            else
               g.drawString("Air shot   : SPACE+ENTER", x, y+=(int)(size*.45));
            y+=(int)(size*.45);
            if(gameMode == CITY_SAVER)
               g.drawString("Turn Gun   : <, >", x, y+=(int)(size*.45));
            else
               g.drawString("Turn Head  : <, >", x, y+=(int)(size*.45));
            y+=(int)(size*.45);
         }
         else
         {
            showMap(g,x,y);
            y+=((int)(size*.45)*13);
            g.setFont(new Font("Monospaced", Font.BOLD, (int)(size*.75)));
         }
         g.setColor(Color.cyan.darker().darker());
         if(gameMode == EARTH_INVADERS || gameMode == CITY_SAVER)
            g.drawString("", x, y+=(int)(size*.45));
         else
            if(!toggleMap)
               g.drawString("Map        : M", x, y+=((int)(size*.45)));
            else
               g.drawString("Keys       : M", x, y+=((int)(size*.45)));
         y+=((int)(size*.45));
         g.drawString("Delay      : [, ]", x, y+=((int)(size*.45)));
         y+=((int)(size*.45));
         g.drawString("Screen size: -,+", x, y+=((int)(size*.45)));
         y+=((int)(size*.45));
         if(gameMode==CITY_SAVER)
            g.drawString("Toggle P2 Enemy : 1", x, y+=((int)(size*.45)));
         else
            g.drawString("Toggle P2 Friend: 1", x, y+=((int)(size*.45)));
      
         y+=((int)(size*.45));
         if(gameMode==EARTH_INVADERS)
         {
            y+=((int)(size*.45))*4;
         }
         else
         {
            if(gameMode==CITY_SAVER)
            {
               g.drawString("Toggle P2 Friend: 2", x, y+=((int)(size*.45)));
               y+=((int)(size*.45));
               g.drawString("Toggle AI Enemy : 3", x, y+=((int)(size*.45)));
               y+=((int)(size*.45));
            }
            else
            {
               g.drawString("Toggle P2 Enemy : 2", x, y+=((int)(size*.45)));
               y+=((int)(size*.45));
               g.drawString("Toggle AI Friend: 3", x, y+=((int)(size*.45)));
               y+=((int)(size*.45));
            }
         }
         g.drawString("Pause  : P   Quit: ESC", x, y+=(int)(size*.45));
         y+=(int)(size*.45);
      
         if(p2toggle || p1partner)
         {
            g.setColor(Color.orange.darker());
            g.drawString("P2 Move       : W,A,S,D", x, y+=((int)(size*.45)));
            y+=((int)(size*.45));
            if(p1partner)
            {
               if(gameMode == EARTH_INVADERS || gameMode == BOMBER_DODGER)
                  y+=((int)(size*.45));
               else
                  g.drawString("P2 Grab/Eat   : G,E", x, y+=((int)(size*.45)));
               y+=((int)(size*.45));
            }
            if(p1partner)
            {
               g.drawString("P2 Stomp/Shoot: T,C", x, y+=((int)(size*.45)));
               y+=((int)(size*.45));
               g.drawString("P2 Turn Head  : Z,X", x, y+=((int)(size*.45)));
            }
            else
            {
               g.drawString("P2 Shoot      : C", x, y+=((int)(size*.45)));
               y+=((int)(size*.45));
               g.drawString("P2 Rotate Gun : Z,X", x, y+=((int)(size*.45)));
            }
         }
      }
   
   
      if(!gameStarted || health <= 0)		//game has not started or player dead - show high scores
      {
         if(!gameStarted)						//adjust text height to show high scores before game starts
            y = size/2;
      
         if(gameStarted && scoresUpdated == false && !cheatsUsed) //health <= 0 here
         {
            Utilities.updateHighScores(highScores);
         }
         g.setFont(new Font("Monospaced", Font.BOLD, (int)(size*.75)));
         g.setColor(Color.cyan.darker().darker());
         y+=((int)(size*.75));
         g.drawString("HIGH SCORES: ", x, y+=((int)(size*.75)));
         g.setFont(new Font("Monospaced", Font.BOLD, (int)(size*.75)));
         g.setColor(Color.orange.darker());
         y+=((int)(size*.45));
         for(int i=0; i<highScores.length; i++)
         {
            if(gameStarted && highScores[i].getDamage() >= propertyDamage)		//highlight your high score
               g.setColor(Color.orange);
            else
               g.setColor(Color.orange.darker());
            g.drawString(formatDamage(highScores[i].getDamage())+" "+highScores[i].getName(), x, y+=((int)(size*.45)));
            y+=((int)(size*.45));
         }
         if(gameStarted && health <= 0)
         {
            y+= (3 * ((int)(size*.45)));
            g.setColor(Color.cyan.darker().darker());
            g.drawString("Quit: ESC", x, y+=(int)(size*.45));
            y+=(int)(size*.45);
         }
      }
   
      if(!gameStarted)
      {//***SELECT DIFFICULTY or show title screen info
         y+=(int)(size);
         g.setFont(new Font("Monospaced", Font.BOLD, (int)(size*.75)));
         g.setColor(Color.yellow.darker().darker());
      
         if(titleScreen)
         {
            g.drawString("-----------------------", x, y+=((int)(size*.75)));
            g.drawString("Play as a giant monster", x, y+=((int)(size*.75))); 
            g.drawString("as you stomp on tanks, ", x, y+=((int)(size*.75)));
            g.drawString("throw cars at buildings", x, y+=((int)(size*.75)));
            g.drawString("and swat helicopters   ", x, y+=((int)(size*.75)));
            g.drawString("out of the air.        ", x, y+=((int)(size*.75)));
            g.drawString("-----------------------", x, y+=((int)(size*.75)));
            y+=size;  
            g.drawString("-----------------------", x, y+=((int)(size*.75)));
            g.drawString("4 game modes for 1 or 2", x, y+=((int)(size*.75))); 
            g.drawString("players, competetive or", x, y+=((int)(size*.75)));
            g.drawString("cooperative.           ", x, y+=((int)(size*.75)));
            g.drawString("                       ", x, y+=((int)(size*.75)));
            g.setColor(Color.yellow.darker());
            g.drawString("CLICK or KEY to start  ", x, y+=((int)(size*.75)));
            g.setColor(Color.yellow.darker().darker());
            g.drawString("-----------------------", x, y+=((int)(size*.75)));
         }
         else
         {
            g.drawString("D: change difficulty", x, y+=(int)(size*.75));
         
            g.setColor(Color.yellow.darker());
            g.setFont(new Font("Monospaced", Font.BOLD, (int)(size*.75)));
            y+=size;
            g.drawString("DIFFICULTY:", x, y);
         
            String diffLevel = "MEDIUM";
            if(difficulty==0)
               diffLevel = "EASY";
            else
               if(difficulty==2)
                  diffLevel = "HARD";
               else
                  if(difficulty==3)
                     diffLevel = "NIGHTMARE";
                  else
                     if(difficulty==4)
                        diffLevel = "FREE ROAM";
         
            g.setFont(new Font("Monospaced", Font.ITALIC, (int)(size*.75)));
            g.setColor(Color.red);
            g.drawString(diffLevel, x+size*5, y);    
         
            g.setFont(new Font("Monospaced", Font.BOLD, (int)(size*.75)));
            g.setColor(Color.yellow.darker().darker());
            if(difficulty==0)
            {
               if(gameMode==CITY_SAVER)
               {
                  g.drawString("fight one (1) monster", x, y+=((int)(size*.75)));
                  y+=((int)(size*.75));
               }
               else
                  if (gameMode==EARTH_INVADERS)
                  {
                     g.drawString("start real slow", x, y+=((int)(size*.75)));
                     y+=((int)(size*.75));
                  }
                  else
                  {
                     g.drawString("render at least one (1)", x, y+=((int)(size*.75)));
                     g.drawString("city zone uninhabitable", x, y+=((int)(size*.75)));
                  }
            }
            else if(difficulty==1)
            {
               if(gameMode==CITY_SAVER)
               {
                  g.drawString("fight two (2) monsters", x, y+=((int)(size*.75)));
                  y+=((int)(size*.75));
               }
               else
                  if (gameMode==EARTH_INVADERS)
                  {
                     g.drawString("medium paced vehicles", x, y+=((int)(size*.75)));
                     y+=((int)(size*.75));
                  }
                  else
                  {
                     g.drawString("render at least three(3)", x, y+=((int)(size*.75)));
                     g.drawString("city zones uninhabitable", x, y+=((int)(size*.75)));
                  }
            }
            else if(difficulty==2)
            {
               if(gameMode==CITY_SAVER)
               {
                  g.drawString("fight three (3) monsters", x, y+=((int)(size*.75)));
                  y+=((int)(size*.75));
               }
               else
                  if (gameMode==EARTH_INVADERS)
                  {
                     g.drawString("fast vehicles", x, y+=((int)(size*.75)));
                     y+=((int)(size*.75));
                  }
                  else
                  {
                     g.drawString("render at least six  (6)", x, y+=((int)(size*.75)));
                     g.drawString("city zones uninhabitable", x, y+=((int)(size*.75)));
                  }
            }
            else  if(difficulty==3)
            {
               if(gameMode==CITY_SAVER)
               {
                  g.drawString("fight four (4) monsters", x, y+=((int)(size*.75)));
                  y+=((int)(size*.75));
               }
               else
                  if (gameMode==EARTH_INVADERS)
                  {
                     g.drawString("good luck", x, y+=((int)(size*.75)));
                     y+=((int)(size*.75));
                  }
                  else
                  {
                     g.drawString("render ALL the city", x, y+=((int)(size*.75)));
                     g.drawString("zones uninhabitable", x, y+=((int)(size*.75)));
                  }
            }
            else		//FREE ROAM
            {
               if(gameMode==CITY_SAVER)
               {
                  g.drawString("random difficulty", x, y+=((int)(size*.75)));
                  y+=((int)(size*.75));
               }
               else
                  if (gameMode==EARTH_INVADERS)
                  {
                     g.drawString("random difficulty", x, y+=((int)(size*.75)));
                     y+=((int)(size*.75));
                  }
                  else
                  {
                     g.drawString("demolish all you   ", x, y+=((int)(size*.75)));
                     g.drawString("can until you drop ", x, y+=((int)(size*.75)));
                  }
            }
         //***SELECT GAME MODE   
            y+=(int)(size);
         
            g.setFont(new Font("Monospaced", Font.BOLD, (int)(size*.75)));
            g.setColor(Color.yellow.darker().darker());
            g.drawString("G: change game mode", x, y+=(int)(size*.75));
         
            g.setColor(Color.yellow.darker());
            g.setFont(new Font("Monospaced", Font.BOLD, (int)(size*.75)));
            y+=size;
            g.drawString("GAME MODE:", x, y);
         
            String mode = "MONSTER MASH";
            if(gameMode==1)
               mode = "BOMBER DODGER";
            else
               if(gameMode==2)
                  mode = "CITY SAVER";
               else
                  if(gameMode==3)
                     mode = "EARTH INVADERS";
            g.setFont(new Font("Monospaced", Font.ITALIC, (int)(size*.75)));
            g.setColor(Color.red);
            g.drawString(mode, x+size*5, y);
         
            g.setFont(new Font("Monospaced", Font.BOLD, (int)(size*.75)));
            g.setColor(Color.yellow.darker().darker());
            if(gameMode==MONSTER_MASH)
            {
               g.drawString("strive to  inflict as much", x, y+=((int)(size*.75)));
               g.drawString("property damage as you can", x, y+=((int)(size*.75)));
            }
            else
               if(gameMode==BOMBER_DODGER)
               {
                  g.drawString("all  vehicles  are bombers", x, y+=((int)(size*.75)));
                  g.drawString("survive as long as you can", x, y+=((int)(size*.75)));
               }
               else
                  if(gameMode==CITY_SAVER)
                  {
                     g.drawString("defend  city from monsters", x, y+=((int)(size*.75)));
                     g.drawString("and  limit property damage", x, y+=((int)(size*.75)));
                  }
                  else
                     if(gameMode==EARTH_INVADERS)
                     {
                        g.drawString("just like space invaders", x, y+=((int)(size*.75)));
                        g.drawString("but completely different", x, y+=((int)(size*.75)));
                     }
            
         //***SELECT MAP   
            y+=(int)(size);
         
            g.setFont(new Font("Monospaced", Font.BOLD, (int)(size*.75)));
            g.setColor(Color.yellow.darker().darker());
            g.drawString("C: change map", x, y+=(int)(size*.75));
            g.drawString("L: load map", x, y+=(int)(size*.75));
            if(textMode)
            {
               g.setColor(Color.yellow);
               g.fillRect(x, y+(size/2) ,(size*12), size);
            
               if(mapFileName!= null && mapFileName.length() > 0)
               {
                  g.setColor(Color.red.darker());
                  g.drawString(mapFileName, x, y+=(size + size/4));
               }
               return;
            }
         
            g.setColor(Color.yellow.darker());
            g.setFont(new Font("Monospaced", Font.BOLD, (int)(size*.75)));
            y+=size;
            g.drawString("MAP:", x, y);
         
            g.setFont(new Font("Monospaced", Font.ITALIC, (int)(size*.75)));
            g.setColor(Color.red);
            if(cityListIndex==0 && fileQue!=null && fileQue.size() > 0)
               g.drawString(fileQue.get(0), x+size*2, y);
            else
               g.drawString(cityList.get(cityListIndex), x+size*2, y);
         
            g.setFont(new Font("Monospaced", Font.BOLD, (int)(size*.75)));
            g.setColor(Color.yellow.darker().darker());
            if(cityListIndex==0 && (fileQue==null || fileQue.size() == 0))
            {
               g.drawString("we will build a random    ", x, y+=((int)(size*.75)));
               g.drawString("city just for you         ", x, y+=((int)(size*.75)));
            }
            else
            {
               g.drawString("play in a specific or     ", x, y+=((int)(size*.75)));
               g.drawString("custom built city         ", x, y+=((int)(size*.75)));
            }
         }
      }
   }

//post:  beta version of a simple map to show which panel we are in 
   public static void showMapSimple(Graphics g, int x, int y)
   {
      int size = 18;
      int dimensions = (((int)(size*.45))*10)/3;
      g.setColor(Color.red.darker().darker().darker());
      g.fillRect(x+7, y+7, dimensions*3+13, dimensions*3+13);
   
      x += ((int)(size*.75));
      y += ((int)(size*.75));
      g.setColor(Color.blue);		//draw a blue boarder around the board
      int x2 = x;
      int y2 = y;
      for(int r=0; r<3; r++)
      {
         y2 = y;
         for(int c=0; c<3; c++)
         {
            if(!gameStarted)
               g.setColor(colors.get((int)(Math.random()*colors.size())));
            else
            {
               if(panelState[r][c] == 0)
                  g.setColor(Color.green.darker().darker());
               else
                  if(panelState[r][c] == 1)
                     g.setColor(Color.blue.darker().darker().darker());
               if(panel == ((c * panelState.length) + r))	//we are in this panel
               {
                  g.setColor(Color.yellow);
                  if(panelState[r][c] == 1)
                     g.setColor(Color.yellow.darker().darker());
               }
            }
            g.fillRect(x2, y2, dimensions, dimensions);
            y2+=dimensions;
         }
         x2+=dimensions;
      } 
   }

//post:  displays a map to see which panel we are in (red dot), and mark water, railroad tracks and landmarks 
   public static void showMap(Graphics g, int x, int y)
   {
      int size = (int)(cellSize/2.25);
   
      int dimensions = (((int)(size*.45))*10)/3;
      g.setColor(Color.red.darker().darker().darker());
      g.fillRect(x+7, y+7, dimensions*3+((int)(size*.75)), dimensions*3+((int)(size*.75)));
   
      x += ((int)(size*.75));
      y += ((int)(size*.75));
      g.setColor(Color.blue);		//draw a blue boarder around the board
      int x2 = x;
      int y2 = y;
      for(int r=0; r<3; r++)
      {
         y2 = y;
         for(int c=0; c<3; c++)
         {
            if(!gameStarted)
               g.setColor(colors.get((int)(Math.random()*colors.size())));
            else
            {
               Color blockColor = Color.gray.darker().darker();
               if(suburbPanels!= null && suburbPanels.contains(((c * panelState.length) + r)))
                  blockColor = Color.green.darker().darker();
               if(panelState[r][c] == 0)
               {
                  g.setColor(blockColor);
               }
               else
                  if(panelState[r][c] == 1)
                  {
                     g.setColor(blockColor.darker().darker());
                  }
            }
            g.fillRect(x2, y2, dimensions, dimensions);
            if(gameStarted)
            {
            //add water
               if(waterPanels!= null && waterPanels.size()>0)
               {
                  Color waterColor = Color.blue;  
                  if(panelState[r][c] == 1)
                     waterColor = Color.blue.darker().darker();
                  if(waterPanels.contains(((c * panelState.length) + r)))
                  {
                     g.setColor(waterColor);
                     if(isRiver)	//draw thin band of water
                     {
                        if(horizWater)
                        {
                           g.fillRect(x2, y2+(dimensions/6), dimensions, dimensions/3);
                        }
                        else
                        {
                           g.fillRect(x2+(dimensions/6), y2, dimensions/3, dimensions);
                        }
                     }
                     else
                     {				//draw half block with water
                        boolean southBeach = ( waterPanels.contains(6) &&  waterPanels.contains(7) &&  waterPanels.contains(8) );
                        boolean eastBeach = ( waterPanels.contains(2) &&  waterPanels.contains(5) &&  waterPanels.contains(8) );
                        if(southBeach || eastBeach)
                        {
                           if(!horizWater)
                              g.fillRect(x2, y2+(dimensions/2), dimensions, dimensions/2);
                           else
                              g.fillRect(x2+(dimensions/2), y2, dimensions/2, dimensions);
                        }
                        else //northbeach or westbeach
                        {
                           if(!horizWater)
                              g.fillRect(x2, y2, dimensions, dimensions/2);
                           else
                              g.fillRect(x2, y2, dimensions/2, dimensions);
                        }
                     }
                  }
               }
            //add railroad tracks
               if(trackPanels!= null && trackPanels.size()>0)
               {
                  Color trackColor = Color.gray.darker().darker().darker().darker().darker();  
                  if(panelState[r][c] == 1)
                     trackColor = trackColor.darker().darker();
                  if(trackPanels.contains(((c * panelState.length) + r)))
                  {
                     g.setColor(trackColor);
                     boolean horizTracks = (trackPanels.contains(0) &&  trackPanels.contains(1) && trackPanels.contains(2))
                        || (trackPanels.contains(3) &&  trackPanels.contains(4) && trackPanels.contains(5))  
                        || (trackPanels.contains(6) &&  trackPanels.contains(7) && trackPanels.contains(8));
                     if(horizTracks)
                     {
                        g.fillRect(x2, y2+(dimensions/2), dimensions, dimensions/8);
                     }
                     else
                     {
                        g.fillRect(x2+(dimensions/2), y2, dimensions/8, dimensions);
                     }
                  }
               }
            //show where the landmark is (if habitable)
               for(int landmarkPanel: landmarkPanels)
               {
                  int currPanel = ((c * panelState.length) + r);
                  if(landmarkPanel == currPanel)	//we are in this panel
                  {
                     g.setFont(new Font("Monospaced", Font.BOLD, size/2));
                     if(panelState[r][c] == 0)
                        g.setColor(Color.white);
                     else
                        g.setColor(Color.black);
                     g.drawString("L", x2+(size), y2+(size));
                  }
               }
            //show where the player is
               if(panel == ((c * panelState.length) + r))	//we are in this panel
               {
                  g.setColor(Color.red);
                  g.fillOval(x2+(dimensions/4), y2+(dimensions/4), dimensions/2, dimensions/2);
               }
            }
            y2+=dimensions;
         }
         x2+=dimensions;
      } 
   }

//post:  draws the game window - selection screen, stats & game board
   public static void drawMainScreen(Graphics g)
   {
      g.setColor(Color.blue);		//draw a blue boarder around the board
      g.fillRect(0, 0, (board[0].length*cellSize), (board.length*cellSize));
      if(titleScreen)
      {
         g.drawImage(title.getImage(),0, 0, (board[0].length*cellSize), (board.length*cellSize), null);  //scaled image
      //draw the mouse pointer
         g.drawImage(cursors[4].getImage(), mouseX, mouseY, cellSize/2, cellSize/2, null);  //scaled image
      }
      else if(monsterMaker)
      {
         MonsterMaker.monsterLab(g);
      }
      else if(mapMaker)
      {
         showBoard(g);	
      }
      else if(!gameStarted)
         selectGame(g);
      else
      {
         showBoard(g);					//draw the contents of the array board on the screen
         g.setColor(Color.yellow);		//draw a nuke explosion
         if(nuked && nukeSize < ((board[0].length*cellSize))/2)
         {
            int x = (((board[0].length*cellSize)) / 2) - (nukeSize);
            int y = (((board.length*cellSize)) / 2) - (nukeSize);
            int diam = nukeSize;
            g.fillOval(x, y, diam*2, diam*2);
            nukeSize++;
         }
         else
            if(monsterWins  && nukeSize < ((board[0].length*cellSize))/2)
            {	//when the monster wins, show a monster growing out of the center of the screen
               Player curr = players[PLAYER1];
               int x = (((board[0].length*cellSize)) / 2) - (nukeSize);
               int y = (((board.length*cellSize)) / 2) - (nukeSize);
               int diam = nukeSize;
               int bodyDir = RIGHT;
               int headDir = DOWN;
               if(curr instanceof Robot || curr.getName().equals("The-Blop"))
                  bodyDir = DOWN;
               g.drawImage(curr.getPicture(bodyDir,0,headDir).getImage(), x, y, diam*2, diam*2, null);  //scaled image
               nukeSize++;
            }
            else
               nukeSize = 1;
      }
      if(SHOWBORDER && !titleScreen)
      {
         if(monsterWins)
            g.setColor(colors.get((int)(Math.random()*colors.size())));									
         else
         {
            if(numFrames <= warningTime + warningDuration && gameStarted && numFrames>500)  
            {
               if(numFrames%2==0)
                  g.setColor(Color.yellow);									
               else
                  g.setColor(Color.red);
            }
            else
               if(gameMode == CITY_SAVER || gameMode == EARTH_INVADERS)
                  g.setColor(Color.red.darker().darker().darker());
               else
                  if(numFrames <= painTime + painDuration && gameStarted)  
                     g.setColor(Color.green);									
                  else
                     g.setColor(Color.green.darker().darker().darker());	//TOP: draw a blue boarder around the board
            if(panel>=0 && panel<=2 && gameStarted)								//make border red if you are at a boundry
            {
               if(numFrames <= warningTime + warningDuration && gameStarted && numFrames>500)  
               {
                  if(numFrames%2==0)
                     g.setColor(Color.yellow);									
                  else
                     g.setColor(Color.red);
               }
               else
                  if(gameMode == CITY_SAVER || gameMode == EARTH_INVADERS)
                     g.setColor(Color.red.darker().darker().darker());
                  else
                     if(numFrames <= painTime + painDuration)  
                        g.setColor(Color.red);
                     else
                        g.setColor(Color.red.darker().darker().darker());
            }
         }
         g.fillRect(0, 0, (board[0].length*cellSize), cellSize);
      
         if(monsterWins)
            g.setColor(colors.get((int)(Math.random()*colors.size())));									
         else
         {
            if(numFrames <= warningTime + warningDuration && gameStarted && numFrames>500)  
            {
               if(numFrames%2==0)
                  g.setColor(Color.yellow);									
               else
                  g.setColor(Color.red);
            }	
            else
               if(gameMode == CITY_SAVER || gameMode == EARTH_INVADERS)
                  g.setColor(Color.red.darker().darker().darker());
               else
                  if(numFrames <= painTime + painDuration && gameStarted)  
                     g.setColor(Color.green);									
                  else
                     g.setColor(Color.green.darker().darker().darker());		//BOTTOM: draw a blue boarder around the board
            if(panel>=6 && panel<=8 && gameStarted)						//make border red if you are at a boundry
            {
               if(numFrames <= warningTime + warningDuration && gameStarted && numFrames>500)  
               {
                  if(numFrames%2==0)
                     g.setColor(Color.yellow);									
                  else
                     g.setColor(Color.red);
               }
               else
                  if(gameMode == CITY_SAVER || gameMode == EARTH_INVADERS)
                     g.setColor(Color.red.darker().darker().darker());
                  else
                     if(numFrames <= painTime + painDuration)  
                        g.setColor(Color.red);
                     else
                        g.setColor(Color.red.darker().darker().darker());
            }
         }
         g.fillRect(0, ((board.length-1)*cellSize), (board[0].length*cellSize), cellSize);
      
         if(monsterWins)
            g.setColor(colors.get((int)(Math.random()*colors.size())));									
         else
         {
            if(numFrames <= warningTime + warningDuration && gameStarted && numFrames>500)  
            {
               if(numFrames%2==0)
                  g.setColor(Color.yellow);									
               else
                  g.setColor(Color.red);
            }									
            else
               if(gameMode == CITY_SAVER || gameMode == EARTH_INVADERS)
                  g.setColor(Color.red.darker().darker().darker());
               else
                  if(numFrames <= painTime + painDuration && gameStarted)  
                     g.setColor(Color.green);									
                  else  
                     g.setColor(Color.green.darker().darker().darker());		//LEFT: draw a blue boarder around the board
            if((panel==0 || panel==3 || panel==6) && gameStarted)		//make border red if you are at a boundry
            {
               if(numFrames <= warningTime + warningDuration && gameStarted && numFrames>500)  
               {
                  if(numFrames%2==0)
                     g.setColor(Color.yellow);									
                  else
                     g.setColor(Color.red);
               }									
               else
                  if(gameMode == CITY_SAVER || gameMode == EARTH_INVADERS)
                     g.setColor(Color.red.darker().darker().darker());
                  else
                     if(numFrames <= painTime + painDuration)  
                        g.setColor(Color.red);
                     else
                        g.setColor(Color.red.darker().darker().darker());
            }
         }
         g.fillRect(0, 0, cellSize, (board.length*cellSize));
      
         if(monsterWins)
            g.setColor(colors.get((int)(Math.random()*colors.size())));									
         else
         {
            if(numFrames <= warningTime + warningDuration && gameStarted && numFrames>500)  
            {
               if(numFrames%2==0)
                  g.setColor(Color.yellow);									
               else
                  g.setColor(Color.red);
            }
            else
               if(gameMode == CITY_SAVER || gameMode == EARTH_INVADERS)
                  g.setColor(Color.red.darker().darker().darker());
               else
                  if(numFrames <= painTime + painDuration && gameStarted)  
                     g.setColor(Color.green);									
                  else 
                     g.setColor(Color.green.darker().darker().darker());		//RIGHT: draw a blue boarder around the board
            if((panel==2 || panel==5 || panel==8)  && gameStarted)	//make border red if you are at a boundry
            {
               if(numFrames <= warningTime + warningDuration && gameStarted && numFrames>500)  
               {
                  if(numFrames%2==0)
                     g.setColor(Color.yellow);									
                  else
                     g.setColor(Color.red);
               }									
               else
                  if(gameMode == CITY_SAVER || gameMode == EARTH_INVADERS)
                     g.setColor(Color.red.darker().darker().darker());
                  else
                     if(numFrames <= painTime + painDuration)  
                        g.setColor(Color.red);
                     else
                        g.setColor(Color.red.darker().darker().darker());
            }
         }
         g.fillRect((board.length*cellSize)-cellSize, 0, cellSize, (board.length*cellSize));
      }
      g.setColor(Color.black);
      int cornerX = (board.length*cellSize);
      int cornerY = 0;
      int width = cellSize*6;
      int height = (board.length*cellSize);
      g.fillRect(cornerX, cornerY, width, height);      
      if(gameStarted)
      {
         String cityName = cityList.get(cityListIndex);
         if(cityName.equalsIgnoreCase("RANDOMGEN"))
         {
            cityName = "SHEBOYGAN";
         }
         String monsterName = players[PLAYER1].getName().toUpperCase();
         String movieTitle = "Mash, Mangle and Munch!";
         if(monsterWins)  
         {                 //player 1 has won the game
            if(gameMode == CITY_SAVER)
            {
               movieTitle = "THE "+ monsterName + " THAT SAVED " + cityName + "!";
            }
            else
            {
               movieTitle = "THE "+ monsterName + " THAT ATE " + cityName + "!";
            }
         }
         else if(players[PLAYER1].getHealth() <= 0)
         {                 //player 1 has lost the game
            if(gameMode == CITY_SAVER)
            {
               movieTitle = cityName + " FALLS!";
            }
            else
            {
               if(nuked)
               {
                  movieTitle = "ALL IS LOST!";
               }
               else
               {
                  movieTitle = cityName + " WINS!";
               }
            }
         }
         else
         {                 //player 1 is still playing
            if(gameMode == CITY_SAVER)
            {
               String foeName = "THE CREATURE";
               if(players[AIMONSTER1] != null && !players[AIMONSTER1].getName().equalsIgnoreCase("NONE"))
               {
                  foeName = players[AIMONSTER1].getName().toUpperCase();
               }
               else if(players[AIMONSTER2] != null && !players[AIMONSTER2].getName().equalsIgnoreCase("NONE"))
               {
                  foeName = players[AIMONSTER2].getName().toUpperCase();
               }
               else if(players[AIMONSTER3] != null && !players[AIMONSTER3].getName().equalsIgnoreCase("NONE"))
               {
                  foeName = players[AIMONSTER3].getName().toUpperCase();
               }
               else if(players[AIMONSTER4] != null && !players[AIMONSTER4].getName().equalsIgnoreCase("NONE"))
               {
                  foeName = players[AIMONSTER4].getName().toUpperCase();
               }
               movieTitle = monsterName + " VS " + foeName + "!";
            }
            else
            {
               movieTitle = monsterName + " VS " + cityName + "!";
            }
         }
         showMovieTitle(g, movieTitle);
      }
      displayStats(g, cornerX, cornerY, width, height);
   }

   public static void showMovieTitle(Graphics g, String movieTitle)
   {
      g.setFont(new Font("Monospaced", Font.BOLD, (int)((cellSize/2.25)*1.3)));
      int textWidth = g.getFontMetrics().stringWidth(movieTitle);
      int textX = ((board[0].length*cellSize)/2) - (textWidth/2);
      int textY = (cellSize/2) + (cellSize/4);
      g.setColor(Color.black);
      g.fillRect(textX - (cellSize/2), textY - (cellSize/2), textWidth + (cellSize/2), (cellSize/2) + (cellSize/8));
      g.setColor(Color.white);
      g.drawString(movieTitle, textX, textY);
   }

//pre:  dam >=0
//post: returns the property damage amount as a string with units (thousand, million, etc)
   public static String formatDamage(long dam)
   {
      double temp = dam;
      String units="trillion";
      if(temp<1000000)
      {
         units="thousand";
         temp/=1000;
      }
      else
         if(temp<1000000000)
         {
            units="million";
            temp/=1000000;
         }
         else
            if(temp< 1000000000000.0)
            {
               units="billion";
               temp/=1000000000;
            }
            else
            {
               temp/=1000000000000.0;
            }
      String damage = ""+temp;
      int decimal = damage.indexOf(".");
      if(decimal == -1)		//no decimal found, add a .00 on the end
         damage += ".00";
      else
      {
         if(decimal+3 <= damage.length())
            damage = damage.substring(0, decimal+3);      
      }  
      return ("$"+damage+" "+units);
   }

//pre:
//post: returns the appropriate index for the status array description depending on health
   public static int healthToStatusIndex(int health)
   {
      int sl=0;
      if(health < 80)
         sl++;
      if(health < 60)
         sl++;
      if(health < 40)
         sl++;
      if(health < 20)
         sl++;
      if(sl>=statusLevels.length)
         sl =  statusLevels.length -1;
      return sl;
   }

}