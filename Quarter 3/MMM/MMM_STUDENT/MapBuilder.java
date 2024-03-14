//Mash, Mangle & Munch - Douglas R Oberle, June 2012  doug.oberle@fcps.edu
//WORLD GENERATOR UTILITIES
import java.util.ArrayList;
import java.awt.event.KeyEvent;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.Color;
//handles creation of the random world
public class MapBuilder extends MMMPanel
{
   private static double SUBURB_PROB = 0.5;		//probability that the map will have suburb district
   private static double FOREST_PROB = 0.5;		//probability that the map will have a forest section
   private static double AMUSEMENT_PROB = 0.33;	//probability that the map will have a carnival
   private static double CASINO_PROB = 0.33;		//probability that the map will have casino district
   private static double WATER_PROB = 0.66;		//probability that the map will have a body of water
   private static double RIVER_PROB = 0.33;		//probability that the body of water will be a river
   private static boolean thereIsWater;			//is there water in the map
   private static String[] terrainTypes = {"S--","PK1","PK2","PK3","SR1","SU1","SX1","SX2","SX3","TR1","TU1","TXR",
                                           "TXU","TR2","TU2","S~1","S~2","~~~","MD2","MD1","#EH"};
   private static int terrainIndex = 0;
   private static int structureIndex = 0;
   private static int totalStr = treeImages.length + houseImages.length + buisnessImages.length + buildingImages.length +
                                 rideImages.length + casinoImages.length + waterTowerImages.length + fuelDepotImages.length + 
   										landmarkImages.length + gasStationImages.length + elecTowerImages.length + skyscraperImages.length;
																//total number of structures					
   private static boolean loadFile = false;		//are we loading a map to edit (true) versus entering a filename to save (false)								   												    
   private static String currTerrain;				//for map maker, records the current selected terrain tile to paint with
   private static Structure currStr;				//for map maker, records the current selected structure to place

  
   //pre: k is a valid key input from user
	//post:executes command depending on k
   public static void keyInput(int k)
   {
      if(textMode)				//we are entering the name of the file to save the map
      {
         if(mapFileName==null)
            mapFileName = "";
         if(k==KeyEvent.VK_ENTER)
         {
            Sound.enterText();
            if(mapFileName.length() > 0)
            {
               char last = mapFileName.charAt(mapFileName.length()-1);  
               if(loadFile == false)
               {        
                  if(!Character.isDigit(last) || Integer.parseInt(""+last) != panel)
                     mapFileName += (""+panel);     
                  Utilities.writeToFile(mapFileName);
                  Utilities.updateFileList("fileList", mapFileName);
               }  
               else if(loadFile == true)
               {
                  if(Character.isDigit(last))
                     Utilities.readFileToBoard(mapFileName);
                  else		//doesn't end with a panel number, so try to load any map that starts with mapFileName
                  {
                     for(String fname: mapList)
                        if(fname.startsWith(mapFileName))
                           Utilities.readFileToBoard(fname);
                  }
               }
            }
            textMode = false;
         }
         else if(((k>=KeyEvent.VK_A && k<=KeyEvent.VK_Z) || (k>=KeyEvent.VK_0 && k<=KeyEvent.VK_9)) && mapFileName.length()<24)
            mapFileName += (char)(k);
         else if(k==KeyEvent.VK_BACK_SPACE && mapFileName.length() > 0) 
            mapFileName = mapFileName.substring(0, mapFileName.length()-1);
         if(MonsterMaker.isSuspect(mapFileName))
         {
            mapFileName = "PROFANIDELPHIA";
         } 
         return;
      }
      if(k==KeyEvent.VK_F)		//save to file
      {
         textMode = true;
         loadFile = false;
         return;
      }  
      if(k==KeyEvent.VK_L)		//load a file
      {
         textMode = true;
         loadFile = true;
         return;
      } 
      if(k==KeyEvent.VK_O)		//scroll panel left
      {
         if(panel == 0)
            panel = 8;
         else
            panel--;
         return;
      }  
      if(k==KeyEvent.VK_P)		//scroll panel right         
      {
         if(panel == 8)
            panel = 0;
         else
            panel++;
         return;
      } 
   
      int r = mouseY/cellSize;
      int c = mouseX/cellSize;
      if(k==KeyEvent.VK_UP)
      {
         if(r>=1)
         {
            mouseY -= cellSize;
            r--;
         }
         else if(panel>=3 && panel<=8)
         {
            panel-=3;
            mouseY += (cellSize * (board.length-1));
         }
      }
      else if(k==KeyEvent.VK_DOWN)
      {
         if(r<board.length-1)
         {
            mouseY += cellSize;
            r++;
         }
         else if(panel>=0 && panel<=5)
         {
            panel+=3; 
            mouseY -= (cellSize * (board.length-1));
         }
      }
      else if(k==KeyEvent.VK_LEFT)
      {
         if(c>=1)
         {
            mouseX -= cellSize;
            c--;
         }
         else if(panel !=0 && panel != 3 && panel != 6)
         {
            panel--;
            mouseX += (cellSize * (board[0].length-1));
         }
      }
      else if(k==KeyEvent.VK_RIGHT)
      {
         if(c<board[0].length-1)
         {
            mouseX += cellSize;
            c++;
         }
         else if(panel !=2 && panel != 5 && panel != 8)
         {
            panel++;
            mouseX -= (cellSize * (board[0].length-1));
         }
      }  
      if (k==KeyEvent.VK_C && r>=0 && c>=0 && r<board.length && c<board[0].length)							
      {	//C key - delete both
         if(r==0 || c==0 || r==board.length-1 || c==board[0].length-1)
         {
            board[r][c][panel] = "#EH";				//make tiles behind the borders elec so that cars don't spawn there - only on the roads
         }
         else
            board[r][c][panel] = "S--";
         structures[r][c][panel] = null;
      }   
      else if(k==KeyEvent.VK_Z)				//Z key - scroll through terrain types left
      {
         terrainIndex--;
         if(terrainIndex < 0)
            terrainIndex = terrainTypes.length - 1;
         currTerrain = terrainTypes[terrainIndex];
      }
      else if(k==KeyEvent.VK_X)				//X key - scroll through terrain types right
      {
         terrainIndex++;
         if(terrainIndex >= terrainTypes.length)
            terrainIndex = 0;
         currTerrain = terrainTypes[terrainIndex];
      }
      else if(k==KeyEvent.VK_SPACE && r>=0 && c>=0 && r<board.length && c<board[0].length)						
      {	//space key - paint with selected terrain
         if(currTerrain != null)
         {
            Sound.placeSelected();
         
            if(r==0 || c==0 || r==board.length-1 || c==board.length-1)
            {//borders can only be #, water, bridge, street, intersection or railroad tracks
               if(!currTerrain.startsWith("M") && !currTerrain.equals("S--"))
                  board[r][c][panel] = currTerrain;
            }
            else
               board[r][c][panel] = currTerrain;
         }   
         else
         {
            if(r==0 || c==0 || r==board.length-1 || c==board.length-1)
            {
               board[r][c][panel] = "#EH";				//make tiles behind the borders elec so that cars don't spawn there - only on the roads
            }
            else
            {
               if(structures[r][c][panel]!=null && structures[r][c][panel].getName().startsWith("ELEC"))
                  board[r][c][panel] = "#EH";			//make sure elec towers have impassable terrain	
               else
                  board[r][c][panel] = "S--";
            }   
         }
      }
      else if((k==KeyEvent.VK_A || k==KeyEvent.VK_S) && r>0 && c>0 && r < board.length-1 && c<board[0].length-1)						
      {																//A or S key - toggle structure left or right
         if(k==KeyEvent.VK_A)
         {
            structureIndex--;
            if(structureIndex < 0)
               structureIndex = totalStr -1;
         }
         else
         {
            structureIndex++;
            if(structureIndex >= totalStr)
               structureIndex = 0;
         }
         if(structureIndex == 0)
            currStr = null;
         else if(structureIndex >= 1 && structureIndex <= 2)
            currStr = new Structure("TREES", r, c, panel, treeImages, animation_delay*3, true, true, 1, -1, cellSize, structureIndex-1, (long)(Math.random()*1000)+2000);	
         else if(structureIndex >= 3 && structureIndex <= 4)
            currStr = new Structure("BLDG - HOUSES", r, c, panel, houseImages, animation_delay*3, true, true, 1, -1, cellSize, structureIndex-3, houseValue());	
         else if(structureIndex == 5)
            currStr = new Structure("BLDG - WATER TOWER", r, c, panel, waterTowerImages, animation_delay*3, false, true, 1, 50, cellSize, 0, 200000);
         else if(structureIndex == 6)
            currStr = new Structure("BLDG - WATER TOWER", r, c, panel, waterTowerImages, animation_delay*3, false, true, 1, 50, cellSize, 1, 100000);
         else if(structureIndex == 7)
            currStr = new Structure("ELEC TOWER "+0, r, c, panel, elecTowerImages, animation_delay, false, false, 1, 90, cellSize, 0, 150000);
         else if(structureIndex == 8)
            currStr = new Structure("ELEC TOWER "+1, r, c, panel, elecTowerImages, animation_delay, false, false, 1, 90, cellSize, 1, 150000);
         else if(structureIndex >= 9 && structureIndex<=10)
            currStr = new Structure("FUEL - GAS STATION", r, c, panel,gasStationImages, animation_delay*3, false, true, 1, 25, cellSize, structureIndex-9, buisnessValue());	
         else if(structureIndex == 11)
            currStr = new Structure("FUEL DEPOT", r, c, panel, fuelDepotImages, animation_delay*3, false, true, 1, 15, cellSize, 0, 1000000);
         else if(structureIndex == 12)
            currStr = new Structure("FUEL DEPOT", r, c, panel, fuelDepotImages, animation_delay*3, false, true, 1, 15, cellSize, 1, 1000000);
         else if(structureIndex >= 13 && structureIndex < 13 + buisnessImages.length)
            currStr = new Structure("BLDG - BUISNESS", r, c, panel, buisnessImages, animation_delay*3, false, true, 1, 35, cellSize, structureIndex - 13, buisnessValue());	
         else if(structureIndex >= 17 && structureIndex < 17 + buildingImages.length)
            currStr = new Structure("BLDG - HIGHRISE", r, c, panel, buildingImages, animation_delay*3, false, true, 1, 50, cellSize, structureIndex - 17, highRiseValue());
         else if(structureIndex >= 21 && structureIndex < 21 + skyscraperImages.length)
            currStr = new Structure("BLDG - TOWER", r, c, panel, skyscraperImages, animation_delay*3, false, true, 2, 90, cellSize, structureIndex - 21, towerValue());
         else if(structureIndex >= 27 && structureIndex < 27 + (landmarkImages.length))	
         {								//subtract 1 because the last index is the lighthouse
            int index = structureIndex - 27;  
            int lmSize = 2;		//landmark height
            int lmHealth = 200;	//landmark health
            if(index<=1)			//ferris wheel/arch is only 1 high and easier to take down
            {
               lmSize = 1;
               lmHealth = 100;
            }
            currStr = new Structure("BLDG - LANDMARK", r, c, panel, landmarkImages, animation_delay*3, false, true, lmSize, lmHealth, cellSize, index, towerValue()*10);
         }
         else if(structureIndex >= 37 && structureIndex < 37 + casinoImages.length)
            currStr = new Structure("BLDG - CASINO", r, c, panel, casinoImages, animation_delay*3, false, true, 1, 75, cellSize, structureIndex - 37, towerValue()*3);
         else if(structureIndex >= 43 && structureIndex < 43 + rideImages.length)
         {
            int index = structureIndex - 43; 
            int rideHealth = -1;
            long value = 50000;
            if(index <= 2)	//ferris wheel and roller coaster have higher health
            {
               rideHealth = 35;   
               value = 85000;
            }
            boolean passable = false;
            if(index > 2)	//rides and tent are passable
               passable = true;
            currStr = new Structure("BLDG - RIDE", r, c, panel, rideImages, animation_delay*3, passable, true, 1, rideHealth, cellSize, index, value);
         }
      }
      else if(k==KeyEvent.VK_ENTER && r>0 && c>0 && r < board.length-1 && c<board[0].length-1)						
      {															//ENTER key - place selected structure
         if(structures[r][c][panel] == null && currStr!=null)
         {
            //System.out.println(r + " " + c);
            Sound.enterText();
            Structure newStr = currStr.clone();
            newStr.setRow(r);							//we might have moved the mouse between choosing the structure and placing it
            newStr.setCol(c);
            String terr = board[r][c][panel];  		//this is the terrain we want to place our structure on
            if(terr.equals("SR1"))						//if we are placing the structure on a horiz road, set the health to -1 so that it is passable
               newStr.setHealth(-1);
            structures[r][c][panel] = newStr;
            if(newStr!=null && newStr.getName().startsWith("ELEC"))
               board[r][c][panel] = "#EH";			//needed to make elec towers impassable
            else if(newStr!=null && board[r][c][panel].startsWith("#"))
               board[r][c][panel] = "S--";
         }
         else
         {
            if(currStr!=null && currStr.getName().startsWith("ELEC"))
               board[r][c][panel] = "S--";
            structures[r][c][panel] = null;
         }
      }
      else if(k==KeyEvent.VK_N)				//N key - change time of day
      {
         day = !day;
         ImageDisplay.loadImages();  
      }
   }


   public static void showInfo(Graphics g, int x, int y)
   {
      int size = (int)(cellSize/2.25);
      g.setFont(new Font("Monospaced", Font.BOLD, (int)(size*.75)));
      
      y+=((int)(size*.75));
        
      g.drawString("----------------------", x, y+=(size));
      g.drawString("Scroll Terrain <:    Z", x, y+=(size));
      g.drawString("Scroll Terrain >:    X", x, y+=(size));
      g.drawString("Paint  Terrain  :SPACE", x, y+=(size));
      g.drawString("or         RIGHT CLICK", x, y+=(size));
      g.setColor(Color.yellow.darker().darker());
      if(currTerrain==null)
         g.drawString("Current Selection:NONE", x, y+=(size));
      else
         g.drawString("Current Selection:"+currTerrain, x, y+=(size));
      y+= (int)(size*.75);
      g.setColor(Color.yellow);
      g.drawString("----------------------", x, y+=(size));
      g.drawString("Scroll Structure <:  A", x, y+=(size));
      g.drawString("Scroll Structure >:  S", x, y+=(size));
      g.drawString("Paint  Structure:ENTER", x, y+=(size));
      g.drawString("or          LEFT CLICK", x, y+=(size));
      if(currStr == null)
      {
         g.setColor(Color.yellow.darker().darker());
         g.drawString("Current Selection:NONE", x, y+=(size));
         if(currTerrain==null)
         {
            ImageDisplay.drawTerrain(g, "S--",  x,  y);
            ImageDisplay.drawTerrain(g, "S--",  x,  y+cellSize);    
         }
         else
         {
            ImageDisplay.drawTerrain(g, currTerrain,  x,  y);
            ImageDisplay.drawTerrain(g, currTerrain,  x,  y+cellSize);    
         }
         y+=(cellSize);
      }
      else
      {
         g.drawString("Current Selection:    ", x, y+=(size));
         if(currTerrain==null)
         {
            ImageDisplay.drawTerrain(g, "S--",  x,  y);
            ImageDisplay.drawTerrain(g, "S--",  x,  y+cellSize);    
         }
         else
         {
            ImageDisplay.drawTerrain(g, currTerrain,  x,  y);
            ImageDisplay.drawTerrain(g, currTerrain,  x,  y+cellSize);    
         }
         y+=(cellSize);
         if(currStr.getHeight()==1) //each structure has a heightDiff that is little variations in height   
            g.drawImage(currStr.getPictureAndAdvance().getImage(), x, y, cellSize, cellSize, null);  //scaled image
         else							//add the height of the building	
            g.drawImage(currStr.getPictureAndAdvance().getImage(), x, y - ((currStr.getHeight()-1)*cellSize), cellSize ,(currStr.getHeight()*cellSize), null);          
      }
      y+=(cellSize);
      g.setColor(Color.yellow);
      g.drawString("Clear Space    :     C", x, y+=(size));
      g.drawString("Scroll Panel <     : O", x, y+=(size));
      g.drawString("Scroll Panel >     : P", x, y+=(size));
      g.drawString("PANEL:", x, y+=(size));
      g.setFont(new Font("Monospaced", Font.BOLD, size));
      g.setColor(Color.red);
      g.drawString(""+panel, x + (size*3), y);
      g.setFont(new Font("Monospaced", Font.BOLD, (int)(size*.75)));
      g.setColor(Color.yellow);
      y+=(size);
         
      g.drawString("Write Panel to File: F", x, y+=(size*2));
      g.setColor(Color.yellow.darker().darker());
      g.drawString("Type the city name and", x, y+=(size));
      g.drawString("we will add the panel ", x, y+=(size));
      g.drawString("number to the end.    ", x, y+=(size));
      g.drawString("Sheboygan0 would be   ", x, y+=(size));
      g.drawString("the upper left panel, ", x, y+=(size));
      g.drawString("Sheboygan8 would be   ", x, y+=(size));
      g.drawString("the lower right panel.", x, y+=(size));
      g.setColor(Color.yellow);
      g.drawString("Load a map to edit : L", x, y+=(size*2));
      g.drawString("Return to Main Menu: R", x, y+=(size));
      g.drawString("----------------------", x, y+=(size));
      
      if(textMode)
      {
         g.setColor(Color.yellow);
         g.fillRect(x, y+(size/2) ,(size*12), size);
         
         if(mapFileName!= null && mapFileName.length() > 0)
         {
            g.setColor(Color.red.darker());
            g.drawString(mapFileName, x, y+=(size + size/4));
         }
      }
      return;
   }


  	//pre:	r, c & p are valid indexes of board
	//post: returns if a location is spawnable
   public static boolean spawnableLoc(int r, int c, int p)
   {
      if(noStructure(r,c, p) && (board[r][c][p].startsWith("S") ||board[r][c][p].startsWith("TX")) )
         return true;
      return false;
   }
	
	 //pre: curr!=null
//post:returns true if curr is a habitable building - used to keep track of zone-clearing bonus
   public static boolean habitable(Structure curr)
   {
      if(curr==null)
         return false;
      if(curr.getName().startsWith("TREE") || curr.getName().startsWith("ELEC") || curr.getName().startsWith("Blop") || curr.getName().startsWith("FUEL") || curr.getName().startsWith("hole"))
         return false;
      if(curr.getHealth()==0)
         return false;
      return true;
   }
   
		//pre:  r & c are valid board coordinates
	//post: returns true if there is no structure at r, c and the space is passable
   public static boolean noStructure(int r, int c, int p)
   {
      if(r < 0 || c < 0 || r >= board.length || c >= board.length)
         return false;
      if((structures[r][c][p]!=null && !structures[r][c][p].isPassable()) || (board[r][c][p].startsWith("#") && r > 0))
         return false;
      return true;
   }
	
	 //pre: r,c,p are valid indexes of board and structures
//post:returns true if board[r][c][p] is some kind of travelable street
   public static boolean isStreet(int r, int c, int p)
   {
      if(r<0 || c<0 || r>=board.length || c>=board[0].length)
         return false;
      String name = board[r][c][p];
      Structure str = structures[r][c][p];
      if(isRoad(r,c,p) || name.startsWith("S~"))
      {
         if(str!=null)
         {//since the structure is on a street, it is either destroyed blop- or an "overlap" structure
            if(str.getName().startsWith("hole"))
               return false;
            if(str.getHealth() == 0 || ((str.getName().startsWith("BLDG") || str.getName().startsWith("FUEL") || str.getName().startsWith("TREE")) && str.getHealth()<0))
               return true;
            return false;
         } 
         return true;
      }
      if(name.equals("S--") &&  structures[r][c][p] == null)
         return true; 
      return false;
   }
   
	 //pre: r,c,p are valid indexes of board and structures
//post:returns true if board[r][c][p] is passable by a crowd
   public static boolean passableByCrowd(int r, int c, int p)
   {
      if(r<0 || c<0 || r>=board.length || c>=board[0].length)
         return false;
      if(MapBuilder.isStreet(r,c,p))
         return true; 
      String name = board[r][c][p];
      if(name.startsWith("~"))
         return false;
      Structure str = structures[r][c][p]; 
      if(str!=null)
      {
         if(str.getName().startsWith("hole"))
            return false;
         if(str.getName().startsWith("Blop") && str.getHealth() == 0)
            return true;
         return false;
      } 
      if(name.startsWith("M") || name.startsWith("PK"))
         return true;
      
      return false;
   }
   
 //pre: r,c,p are valid indexes of board and structures
//post:returns true if board[r][c][p] is passable by a tank
   public static boolean passableByTank(int r, int c, int p)
   {
      if(r<0 || c<0 || r>=board.length || c>=board[0].length)
         return false;
      if(passableByCrowd(r,c,p))
         return true; 
      String name = board[r][c][p];
      Structure str = structures[r][c][p];
      if(name.startsWith("~"))
         return false;
      if(r>0 && c>0 && r<board.length-1 && c<board[0].length-1)
      {
         if(str==null)
            return true;
         if(str!=null)
         {
            if(str.getName().startsWith("hole"))
               return false;
            if(str.getHealth()==0)
               return true;
            return false;
         }
         return true;
      }
      return false;
   }
		
   public static long houseValue()
   {
      return (long)((Math.random()*800000) + 200000);
   }
	
   public static long buisnessValue()
   {
      return (long)((Math.random()*800000) + 200000);
   }
	
   public static long highRiseValue()
   {
      return (long)((Math.random()*3000000) + 2000000);
   }
	
   public static long towerValue()
   {
      return (long)((Math.random()*30000000) + 10000000);
   }
			
	//post:  builds the collection of spawn points for players
   public static void buildSpawnPoints()
   {
      int numPanels = board[0][0].length;
      vehicleSpawnPoints = new ArrayList[numPanels];			//spawn locations for ground vehicles
      for(int i=0; i<vehicleSpawnPoints.length; i++)
         vehicleSpawnPoints[i] = new ArrayList();
         
      humanSpawnPoints = new ArrayList[numPanels];				//spawn locations for people
      for(int i=0; i<humanSpawnPoints.length; i++)
         humanSpawnPoints[i] = new ArrayList();
   
      boatSpawnPoints = new ArrayList[numPanels];				//spawn locations for boats
      for(int i=0; i<boatSpawnPoints.length; i++)
         boatSpawnPoints[i] = new ArrayList();
   
      airSpawnPoints = new ArrayList[numPanels];				//spawn locations for aircraft
      for(int i=0; i<airSpawnPoints.length; i++)
         airSpawnPoints[i] = new ArrayList();
   
      tankSpawnPoints = new ArrayList[numPanels];				//spawn locations for ground vehicles
      for(int i=0; i<tankSpawnPoints.length; i++)
         tankSpawnPoints[i] = new ArrayList();
   
      trainSpawnPoints = new ArrayList[numPanels];				//spawn locations for trains
      for(int i=0; i<trainSpawnPoints.length; i++)
         trainSpawnPoints[i] = new ArrayList();
   
      p2TankSpawnPoints = new ArrayList[numPanels];			//spawn locations for player 2
      for(int i=0; i<p2TankSpawnPoints.length; i++)
         p2TankSpawnPoints[i] = new ArrayList();
   
      p2CarSpawnPoints = new ArrayList[numPanels];				//spawn locations for player 2
      for(int i=0; i<p2CarSpawnPoints.length; i++)
         p2CarSpawnPoints[i] = new ArrayList();
   
      for(int r=0;r<board.length;r++)
      {
         for(int c=0;c<board[0].length;c++)
         {
            for(int p=0; p<board[0][0].length; p++)
            {
               int[]coord = new int[2];
               if((r==1 && c>0 && c<board[0].length-1) || (c==1 && r>0 && r<board[0].length-1) || (r==board.length-2 && c>0 && c<board[0].length-1) || (c==board[0].length-2 && r>0 && r<board[0].length-1))
               {
                  if((board[r][c][p].startsWith("S") || board[r][c][p].startsWith("M") || board[r][c][p].startsWith("PK") || board[r][c][p].startsWith("TX")) && structures[r][c][p]==null)
                  {
                     coord[0]=r;				//p2 can spawn at any steet, mud, train x-road or park inside the visible playing field
                     coord[1]=c;
                     if(board[r][c][p].startsWith("S") || board[r][c][p].startsWith("TX"))
                        p2CarSpawnPoints[p].add(coord);
                     p2TankSpawnPoints[p].add(coord);
                  }
               }
            
               if(r==0 || c==0 || r==board.length-1 || c==board[0].length-1)
               {								//on a hidden border
                  coord[0]=r;				//aircraft can spawn at any border
                  coord[1]=c;
                  airSpawnPoints[p].add(coord);
                  if(board[r][c][p].startsWith("T"))	//train tracks
                  {	//we want trains to start away from the borders because they will have multiple cars
                     if((board[r][c][p].startsWith("TU") && r>3 && r<board.length-3) || (board[r][c][p].startsWith("TR") && c>3 && c<board[0].length-3))
                        trainSpawnPoints[p].add(coord);
                  }
                  if((board[r][c][p].startsWith("S")  || board[r][c][p].startsWith("TX")) && structures[r][c][p]==null)
                  {	//add borders with a street or bridge with no structure on it
                     coord[0]=r;
                     coord[1]=c;
                     vehicleSpawnPoints[p].add(coord);
                  }
                  else
                     if(board[r][c][p].startsWith("~"))
                     {	//add border with water
                        coord[0]=r;
                        coord[1]=c;
                        boatSpawnPoints[p].add(coord);
                     }
                     else
                        if((board[r][c][p].startsWith("S") || board[r][c][p].startsWith("M") || board[r][c][p].startsWith("TX")) && structures[r][c][p]==null)
                        {	//add borders with a street or bridge or mud/sand with no structure on it
                           coord[0]=r;
                           coord[1]=c;
                           tankSpawnPoints[p].add(coord);
                        }
               }
               else		//not on a hidden border
                  if(board[r][c][p].startsWith("S-") || board[r][c][p].startsWith("PK"))	//empty lot or park, maybe with a structure in it
                  {	
                     if(board[r][c][p].startsWith("S-") && structures[r][c][p]==null)
                     {//if an empty lot with not structures, add it
                        if(spawnableLoc(r-1,c,p) || spawnableLoc(r+1,c,p) || spawnableLoc(r,c-1,p) || spawnableLoc(r,c+1,p))
                        {  //only if the empty lot is next to another empty lot
                           coord[0]=r;
                           coord[1]=c;
                           vehicleSpawnPoints[p].add(coord);
                           humanSpawnPoints[p].add(coord);
                        }
                     }
                     else
                     {//if it has a structure on it and is bordering a road, add a person spawn point
                        if((board[r][c][p].startsWith("S-") && structures[r][c][p]!=null) || board[r-1][c][p].startsWith("PK"))
                        {
                           if(spawnableLoc(r-1,c,p) || spawnableLoc(r+1,c,p) || spawnableLoc(r,c-1,p) || spawnableLoc(r,c+1,p))
                           {  //only if the lot is next to another empty lot
                              coord[0]=r;
                              coord[1]=c;
                              humanSpawnPoints[p].add(coord);
                           }
                        }
                     }
                  }
            }
         }
      }
   }

	//post:  record panels with tracks, water, etc for spawn locations and in-game map
   public static void findSpecialPanels()
   {
      for(int p=0; p<board[0][0].length; p++)
      {
         int numGrass = 0;		//counts the # grassy panels to see if this is a suburb panel
         for(int r=0;r<board.length;r++)
         {
            for(int c=0;c<board[0].length;c++)
            {
                            
               String terrain = board[r][c][p];
               if(terrain.startsWith("T") && !trackPanels.contains(p))	//train track panel
                  trackPanels.add(p);
               if(terrain.equals("~~~") && !waterPanels.contains(p))		//open water panel
                  waterPanels.add(p);
               if(terrain.startsWith("P"))
                  numGrass++;
            }
         }
         if(numGrass > (((board.length - 1) * (board[0].length-1)) / 4) && !suburbPanels.contains(p) && !waterPanels.contains(p))
            suburbPanels.add(p);
      }
   }

 	//pre:	r, c & p are valid indexes of board, forestPanels and forestDimensions are the same size
	//post:  looks to see if a coordinate and panel is in territory where a forest is defined to be
   public static boolean inForest(int r, int c, int p, ArrayList<Integer>forestPanels, ArrayList<int[]> forestDimensions)
   {
      if(forestPanels.contains(p))
      {
         int i=0;													//find the index of this panel so we know which forest dimensions to use
         for(i=0; i<forestPanels.size(); i++)
            if(forestPanels.get(i)==p)
               break;
         if(i < forestDimensions.size())
         {															//coord is the boundries of the forest from r1,c1 to r2,c2
            int[] coord = forestDimensions.get(i);		//[index 0] is r1, [index 1] is c1, [index 2] is r2, [index 3] is c2
            if(r>=coord[0] && r<=coord[2] && c>=coord[1] && c<=coord[3])
               return true;	
         }
      }
      return false;
   }
	
	//pre:	r, c & p are valid indexes of board
	//post:  returns true if location is in an amusement parks bounds
   public static boolean inPark(int r, int c, int p, int parkPanel, int[]coord)
   {
      if(p==parkPanel && r>=coord[0] && r<=coord[2] && c>=coord[1] && c<=coord[3])
         return true;	
      return false;
   }

//pre:	r, c & p are valid indexes of board
//post:  returns true if location is next to a street (for placement of gas stations
   public static boolean nextToAStreet(int r, int c, int p)
   {
      if(r<=0 || c<=0 || r>=board.length-1 || c>=board.length-1 || p<0 || p>=board[0][0].length)
         return false;
      if(board[r-1][c][p].startsWith("SR") || board[r-1][c][p].startsWith("SU") || board[r-1][c][p].startsWith("SX"))
         return true;
      if(board[r+1][c][p].startsWith("SR") || board[r+1][c][p].startsWith("SU") || board[r+1][c][p].startsWith("SX"))
         return true;
      if(board[r][c-1][p].startsWith("SR") || board[r][c-1][p].startsWith("SU") || board[r][c-1][p].startsWith("SX"))
         return true;
      if(board[r][c+1][p].startsWith("SR") || board[r][c+1][p].startsWith("SU") || board[r][c+1][p].startsWith("SX"))
         return true;
      return false;
   }
	
//pre:	r, c & p are valid indexes of board
//post:  returns true if location is next to a beach (for placement of light houses
   public static boolean nextToABeach(int r, int c, int p)
   {
      if(r<=0 || c<=0 || r>=board.length-1 || c>=board.length-1 || p<0 || p>=board[0][0].length)
         return false;
      if(board[r-1][c][p].equals("MD2") || board[r+1][c][p].equals("MD2") || board[r][c-1][p].equals("MD2") || board[r][c+1][p].equals("MD2"))
         return true;
      return false;
   }

//pre:   allHorizStreetRows & allVertStreetCols are not null and contain the coordinates of where streets are for each panel
//post:  places railroad tracks on the map
   public static void addRailroadTracks(ArrayList<Integer>[] allHorizStreetRows, ArrayList<Integer>[] allVertStreetCols)
   {
          //*******ADD RAILROAD TRACKS************** 
      if(Math.random() < 0.5 || forceTracks || (gameMode==EARTH_INVADERS && allHorizStreetRows[4].contains(1)))					//50% chance there will be railroad tracks 
      {
         boolean vertTracks = false;		//will tracks go East-West or North-South?
         if(Math.random() < 0.5 && gameMode!=EARTH_INVADERS)	
            vertTracks = true;
         if(vertTracks)
         {
            int tries=0;
            int randPanel = (int)(Math.random()*3);	//0,1 or 2
            int trackCol = 0;									//the column that will contain the track
            for(int i=0; i<allVertStreetCols[randPanel].size(); i++)
            {	//we don't want a track right next to the border - 
               int c = allVertStreetCols[randPanel].get(i);
               if(c==1 || c==board[0].length-2)
                  allVertStreetCols[randPanel].remove(i);
            }    
            if(allVertStreetCols[randPanel].size()>0) 
            {           
               trackCol = allVertStreetCols[randPanel].get((int)(Math.random()*allVertStreetCols[randPanel].size()));
            
               if(gameMode == EARTH_INVADERS)
                  randPanel = 1;
               if(randPanel==0)
               {
                  trackPanels.add(0);
                  trackPanels.add(3);
                  trackPanels.add(6);
               }
               else
                  if(randPanel==1)
                  {
                     trackPanels.add(1);
                     trackPanels.add(4);
                     trackPanels.add(7);
                  }
                  else
                  {
                     trackPanels.add(2);
                     trackPanels.add(5);
                     trackPanels.add(8);
                  }
                       
               for(int i=0; i<trackPanels.size(); i++)
               {
                  int p = trackPanels.get(i);
                  for(int r=0; r<board.length; r++)
                  {
                     if(board[r][trackCol][p].equals("SX1"))   //if its a street intersection
                        board[r][trackCol][p] = "TXU";			//make it a track x-ing up
                     else
                        if(board[r][trackCol][p].indexOf("~") >= 0)		//bridge/water
                           board[r][trackCol][p] = "TU2";
                        else
                           board[r][trackCol][p] = "TU1";
                  } 
                  for(int r=0; r<board.length; r++)							//put tracks around the hidden border so trains can complete a circuit
                  {
                     if(board[r][0][p].equals("SR1"))
                        board[r][0][p] = "TXU";
                     else
                        if(board[r][0][p].indexOf("~") >= 0)		//bridge/water
                           board[r][0][p] = "TU2";
                        else
                           board[r][0][p] = "TU1";
                  } 
                  for(int r=0; r<board.length; r++)
                  {
                     if(board[r][board[0].length-1][p].equals("SR1"))
                        board[r][board[0].length-1][p] = "TXU";
                     else
                        if(board[r][board[0].length-1][p].indexOf("~") >= 0)		//bridge/water
                           board[r][board[0].length-1][p] = "TU2";
                        else
                           board[r][board[0].length-1][p] = "TU1";
                  } 
                  for(int c=0; c<board[0].length; c++)							//put tracks around the hidden border so trains can complete a circuit
                  {
                     if(board[0][c][p].equals("SU1"))
                        board[0][c][p] = "TXR";
                     else
                        if(board[0][c][p].indexOf("~") >= 0)		//bridge/water
                           board[0][c][p] = "TR2";
                        else
                           board[0][c][p] = "TR1";
                  } 
                  for(int c=0; c<board[0].length; c++)							//put tracks around the hidden border so trains can complete a circuit
                  {
                     if(board[board.length-1][c][p].equals("SU1"))
                        board[board.length-1][c][p] = "TXR";
                     else
                        if(board[board.length-1][c][p].indexOf("~") >= 0)		//bridge/water
                           board[board.length-1][c][p] = "TR2";
                        else
                           board[board.length-1][c][p] = "TR1";
                  } 
               }
            }
         }
         else			//horiz tracks
         {
            int tries=0;
            int randPanel = (int)(Math.random()*3);	//0,1 or 2
            int trackRow = 0;									//the row that will contain the track
            //if(gameMode!=EARTH_INVADERS)
            for(int i=0; i<allHorizStreetRows[randPanel].size(); i++)
            {	//we don't want a track right next to the border - 
               int r = allHorizStreetRows[randPanel].get(i);
               if(r==1 || r==board.length-2)
                  allHorizStreetRows[randPanel].remove(i);
            }    
            if(allHorizStreetRows[randPanel].size()>0) 
            {                           
               trackRow = allHorizStreetRows[randPanel].get((int)(Math.random()*allHorizStreetRows[randPanel].size()));
               if(gameMode == EARTH_INVADERS)
               {
                  randPanel = 3;
                  //trackRow = 1;
               }
               if(randPanel==0)
               {
                  trackPanels.add(0);
                  trackPanels.add(1);
                  trackPanels.add(2);
               }
               else
                  if(randPanel==1)
                  {
                     trackPanels.add(3);
                     trackPanels.add(4);
                     trackPanels.add(5);
                  }
                  else
                  {
                     trackPanels.add(6);
                     trackPanels.add(7);
                     trackPanels.add(8);
                  }
                       
               for(int i=0; i<trackPanels.size(); i++)
               {
                  int p = trackPanels.get(i);
                  if(gameMode!=EARTH_INVADERS)				//don't put in horiz tracks for EARTH INVADERS because it will be at row 1 instead
                     for(int c=0; c<board[0].length; c++)
                     {
                        if(board[trackRow][c][p].equals("SX1"))
                           board[trackRow][c][p] = "TXR";
                        else
                           if(board[trackRow][c][p].indexOf("~") >= 0)		//bridge/water
                              board[trackRow][c][p] = "TR2";
                           else
                              board[trackRow][c][p] = "TR1";
                     } 
                  for(int c=0; c<board[0].length; c++)	//put tracks around the hidden border so trains can complete a circuit
                  {
                     int row = 0;
                     if(gameMode == EARTH_INVADERS)
                        row = 1;
                     if(board[row][c][p].equals("SU1"))
                        board[row][c][p] = "TXR";
                     else
                        if(board[row][c][p].indexOf("~") >= 0)		//bridge/water
                           board[row][c][p] = "TR2";
                        else
                           board[row][c][p] = "TR1";
                  } 
                  for(int c=0; c<board[0].length; c++)
                  {
                     if(board[board.length-1][c][p].equals("SU1"))
                        board[board.length-1][c][p] = "TXR";
                     else
                        if(board[board.length-1][c][p].indexOf("~") >= 0)		//bridge/water
                           board[board.length-1][c][p] = "TR2";
                        else
                           board[board.length-1][c][p] = "TR1";
                  } 
                  for(int r=0; r<board.length; r++)							//put tracks around the hidden border so trains can complete a circuit
                  {
                     if(gameMode == EARTH_INVADERS && r==0)			//for EARTH INVADERS, there is a track in row 1, so we don't want vert tracks to meet them in row 0
                        continue;
                     if(board[r][0][p].equals("SR1"))
                        board[r][0][p] = "TXU";
                     else
                        if(board[r][0][p].indexOf("~") >= 0)		//bridge/water
                           board[r][0][p] = "TU2";
                        else
                           board[r][0][p] = "TU1";
                  } 
                  for(int r=0; r<board.length; r++)							//put tracks around the hidden border so trains can complete a circuit
                  {
                     if(board[r][board[0].length-1][p].equals("SR1"))
                        board[r][board[0].length-1][p] = "TXU";
                     else
                        if(board[r][board[0].length-1][p].indexOf("~") >= 0)		//bridge/water
                           board[r][board[0].length-1][p] = "TU2";
                        else
                           board[r][board[0].length-1][p] = "TU1";
                  } 
               }
            }
         }
      }//*********END RAILROAD TRACKS*********
   }
	
//post: fills all panels with blank lots and borders with barriers
   public static void clearPanels()
   {
      structures = new Structure[board.length][board[0].length][board[0][0].length];
      for(int r=0;r<board.length;r++)			//fill with blank lots
      {
         for(int c=0;c<board[0].length;c++)
         {
            for(int p=0; p<board[0][0].length; p++)
            {
               if(r==0 || c==0 || r==board.length-1 || c==board.length-1)
               {
                  board[r][c][p] = "#EH";				//make tiles behind the borders elec so that cars don't spawn there - only on the roads
               }
               else
                  board[r][c][p] = "S--";
            }
         }
      }
   }
      
   public static void initializePanels()
   {
      clearPanels();
      numStructures = new int[board[0][0].length];		//keep track of # structures in each panel for bonuses if you clear out an entire panel
      for(int i=0; i<numStructures.length; i++)
         numStructures[i] = 0;
      landmarkPanels = new ArrayList<Integer>();
      trackPanels = new ArrayList();			//contains panels that will have rr tracks
      waterPanels = new ArrayList();			//contains the panel #s that might have a body of water it
      suburbPanels = new ArrayList();    
      trainInPanel = new boolean[board[0][0].length];
      for(int i=0; i<trainInPanel.length; i++)
         trainInPanel[i] = false;   
   }
	
	//post:creates a randomly generated 9 panel map (might have suburbs, river/oceanside, landmark bldg and/or railroad tracks)
   public static void generateMap()
   {
      thereIsWater = false;
      forceWater = false;
      isRiver = false;
      horizWater = false;
      forceTracks = false;
      if(players[PLAYER1].getName().startsWith("Gob"))
         forceWater = true;
      else
         if(players[PLAYER1].getName().startsWith("Boo"))
            forceTracks = true;
   
      boolean[] waterTowerInPanel;				//only allow 1 water panel in each panel
      waterTowerInPanel = new boolean[board[0][0].length];
      for(int i=0; i<waterTowerInPanel.length; i++)
         waterTowerInPanel[i] = false;
   
      boolean[] fuelDepotInPanel;		//only allow 1 fuel depot in each panel
      fuelDepotInPanel = new boolean[board[0][0].length];
      for(int i=0; i<fuelDepotInPanel.length; i++)
         fuelDepotInPanel[i] = false;
         
      boolean[] lighthouseInPanel;		//only allow 1 lighthouse in each panel
      lighthouseInPanel = new boolean[board[0][0].length];
      for(int i=0; i<lighthouseInPanel.length; i++)
         lighthouseInPanel[i] = false;
   
      boolean landmark = false;					//have we placed a landmark yet?
      
      initializePanels();
   	
      //***SUBURBS
      boolean suburbs = false;					//will there be suburbs?
      if(Math.random() < SUBURB_PROB)
         suburbs = true;
      int suburbSize = 0;
      if(gameMode == EARTH_INVADERS)
         suburbPanels.add(4);
      else
      {
         if(suburbs)	
         {									//there will be between 1 and 8 suburb panels
            suburbSize = (int)(Math.random()*8)+1;
         }
         for(int i = 0; i < suburbSize; i++)		//choose the suburb panels
         {
            int randPanel = (int)(Math.random()*3);
            if(!suburbPanels.contains(randPanel))
               suburbPanels.add(randPanel);
         }
      }
      //**END SUBURBS
   	
      //***FOREST
      boolean forests = false;					//will there be forests?
      if(Math.random() < FOREST_PROB)
         forests = true;
      int forestSize = 0;
      if(forests)										//between 1 and 3 forest panels
         forestSize = (int)(Math.random()*3)+1;
      ArrayList<Integer>forestPanels = new ArrayList();
      for(int i = 0; i < forestSize; i++)
      {
         int randPanel = (int)(Math.random()*3);
         if(!forestPanels.contains(randPanel))
            forestPanels.add(randPanel);
      }
      ArrayList<int[]> forestDimensions = new ArrayList();
      for(int i=0; i<forestPanels.size(); i++)
      {
         int[] dim = new int[4];												//{start row, start col, end row, end col}
         int width = (int)(Math.random()*(board.length/3)) + 2;	//between 2x2 to 8x8
         int height = (int)(Math.random()*(board[0].length/3))+ 2;
         dim[0] = (int)(Math.random()*(board.length - width));
         dim[1] = (int)(Math.random()*(board[0].length - height));
         dim[2] = dim[0] + width;
         dim[3] = dim[1] + height;
         if(dim[0] < 0) 	
            dim[0] = 0;
         if(dim[1] < 0)  	
            dim[1] = 0;
         if(dim[2] >= board.length)  
            dim[2] = board.length-1;
         if(dim[3] >= board[0].length)	
            dim[3] = board[0].length-1;
         forestDimensions.add(dim);
      }
    //***END FOREST  
   	
   	//***AMUSEMENT PARK
      boolean park = false;										//will there be an amusement park?
      if(Math.random() < AMUSEMENT_PROB)
         park = true;
      int parkPanel =  (int)(Math.random()*board[0][0].length);
      int[] parkDimensions = new int[4];										//{start row, start col, end row, end col}
      int w = (int)(Math.random()*2)+ 2;	
      int h = (int)(Math.random()*2)+ 2;
      parkDimensions[0] = (int)(Math.random()*(board.length - w));
      parkDimensions[1] = (int)(Math.random()*(board[0].length - h));
      parkDimensions[2] = parkDimensions[0] + w;
      parkDimensions[3] = parkDimensions[1] + h;
      if(parkDimensions[0] < 0)
         parkDimensions[0] = 0;
      if(parkDimensions[1] < 0) 
         parkDimensions[1] = 0;
      if(parkDimensions[2] >= board.length) 
         parkDimensions[2] = board.length-1;
      if(parkDimensions[3] >= board[0].length)	
         parkDimensions[3] = board[0].length-1;
      ArrayList<Integer> parkRides = new ArrayList();	//collection of available park rides to pick at least 1 of each
      for(int i=0; i<rideImages.length; i++)
         if(i!=1)													//don't add index 1 because it is the right side of the roller coaster (left side is index 0)
            parkRides.add(i);	
      
      String parkSurface = "PK3";//field
      if(Math.random() < .5)		//perhaps make the park surface concrete
         parkSurface = "S--";
     	//**END AMUSEMENT PARK
   	
   	//***CASINO DISTRICT
      boolean casino = false;										//will there be a casino part of town?
      if(Math.random() < CASINO_PROB)
         casino = true;
      int casinoPanel =  (int)(Math.random()*board[0][0].length);
      int[] casinoDimensions = new int[4];										//{start row, start col, end row, end col}
      w = (int)(Math.random()*2)+ 2;	
      h = (int)(Math.random()*2)+ 2;
      casinoDimensions[0] = (int)(Math.random()*(board.length - w));
      casinoDimensions[1] = (int)(Math.random()*(board[0].length - h));
      casinoDimensions[2] = casinoDimensions[0] + w;
      casinoDimensions[3] = casinoDimensions[1] + h;
      if(casinoDimensions[0] < 0)
         casinoDimensions[0] = 0;
      if(casinoDimensions[1] < 0) 
         casinoDimensions[1] = 0;
      if(casinoDimensions[2] >= board.length) 
         casinoDimensions[2] = board.length-1;
      if(casinoDimensions[3] >= board[0].length)	
         casinoDimensions[3] = board[0].length-1;
      ArrayList<Integer> casinos = new ArrayList();	//collection of available casinos to pick at least 1 of each
      for(int i=0; i<casinoImages.length; i++)
         casinos.add(i);	
      
      String casinoSurface = "SX3";	//parking lot
      if(Math.random() < .5)			//perhaps make the parking lot turn the other way
         casinoSurface = "SX2";
     	//**CASINO DISTRICT
   
      ArrayList<Integer>[] horizStreetRows = new ArrayList[9];	//store rows where horizontal streets are to pick bridges for each panel
      for(int i=0; i<horizStreetRows.length; i++)
         horizStreetRows[i] = new ArrayList();
      ArrayList<Integer>[] vertStreetCols = new ArrayList[9];	//store rows where vertical streets are to pick bridges
      for(int i=0; i<vertStreetCols.length; i++)
         vertStreetCols[i] = new ArrayList();
   
      int blockSize1 = (int)(Math.random() * 4) + 3;		//random block width
      for(int p=0; p<board[0][0].length; p++)
      {
         for(int r=blockSize1;r<board.length-1;r+=blockSize1)
         {													//add horizontal streets
            horizStreetRows[p].add(r);
            for(int c=0;c<board[0].length;c++)
            {
               board[r][c][p] = "SR1";
            }
         }
      }
      if(gameMode == EARTH_INVADERS && Math.random() < .25)
      {//for EARTH INVADERS, sometimes add a street in the top row so there might be railroad tracks there
         horizStreetRows[4].add(1);
         for(int c=0;c<board[0].length;c++)
         {
            board[1][c][4] = "SR1";
         }
      }
      int blockSize2 = (int)(Math.random() * 4) + 3;		//random block height
      for(int p=0; p<board[0][0].length; p++)
      {
         for(int c=blockSize2;c<board.length-1;c+=blockSize2)
         {													//add vertical streets, cross streets where they intersect
            vertStreetCols[p].add(c);
            for(int r=0;r<board.length;r++)
            {
               if(board[r][c][p].equals("SR1"))
                  board[r][c][p] = "SX1";
               else
                  board[r][c][p] = "SU1";
            }
         }
      }
   
      ArrayList<Integer>[] allHorizStreetRows = horizStreetRows.clone();	//remember where all the streets are so we can put down railroad tracks
      ArrayList<Integer>[] allVertStreetCols = vertStreetCols.clone();		//(we remove elements of the originals to place bridges below)
   
      if(gameMode!= EARTH_INVADERS && (Math.random() < WATER_PROB || forceWater))
      {						//there will be a body of water
         thereIsWater = true;
         if(Math.random() <  RIVER_PROB)	//it will be a river
         {	//******************ADD RIVER*********************
            isRiver = true;
            int dir = (int)(Math.random()*2);				//0 for UP, 1 for RIGHT  
            int width = (int)(Math.random()*2)+1;				//1 width or 2  
                         //figure out the # bridges for each of 3 panels for EW rivers and NS rivers
            int numRivers = (int)(Math.random()*2)+1;			//1 or 2 rivers
            for(int nr=0; nr < numRivers; nr++)
            {
               if(dir==UP)
               {
                  int randCol = (int)(Math.random()*3);	//0, 1 or 2
                  if(randCol==0 && !waterPanels.contains(0))
                  {
                     for(int i=0; i<=6; i+=3)		//adds panels 0,3 and 6
                        waterPanels.add(i);
                  }
                  else
                     if(randCol==1 && !waterPanels.contains(1))
                     {
                        for(int i=1; i<=7; i+=3)		//adds panels 1,4 and 7
                           waterPanels.add(i);
                     }		
                     else
                        if(randCol==2 && !waterPanels.contains(2))
                        {
                           for(int i=2; i<=8; i+=3)		//adds panels 2,5 and 8
                              waterPanels.add(i);
                        }
               }	
               else	//dir==RIGHT
               {
                  horizWater = true;
                  int randRow = (int)(Math.random()*3);	//0, 1 or 2
                  if(randRow==0 && !waterPanels.contains(0))
                  {
                     for(int i=0; i<3; i++)		//adds panels 0,1 and 2
                        waterPanels.add(i);
                  }
                  else
                     if(randRow==1 && !waterPanels.contains(3))
                     {
                        for(int i=3; i<6; i++)		//adds panels 3,4 and 5
                           waterPanels.add(i);
                     }		
                     else
                        if(randRow==2 && !waterPanels.contains(6))
                        {
                           for(int i=6; i<9; i++)		//adds panels 6,7 and 8
                              waterPanels.add(i);
                        }
               }
            }
            int[] numBridges = new int[waterPanels.size()];						//sores the number of bridges for each panel
                            	
            if(dir==UP)			//vertical river - pick where the random bridges are for each panel
            { 
               for(int rp=0; rp < waterPanels.size(); rp++)
               {
                  int p = waterPanels.get(rp);
                  numBridges[rp] = (int)(Math.random() * (horizStreetRows[p].size()-1) + 1);
                  if(numBridges[rp] < horizStreetRows[p].size())					//remove random elements from possible vertical bridges until it is the same size as numBridges
                  {
                     while(horizStreetRows[p].size() > numBridges[rp])
                     {
                        int rand = (int)(Math.random() * (horizStreetRows[p].size()-1) + 1);
                        horizStreetRows[p].remove(rand);
                     }
                  }
                  
               }
               int[] riverCols = new int[numRivers];				//stores the col #s for where each river runs down
               for(int i=0; i < riverCols.length; i++)
               {
                  riverCols[i] = (int)(Math.random() * (board[0].length - 2) )+ 1; //don't include 1st or last col because they are behind a border
                  for(int rp=0; rp<waterPanels.size(); rp++)
                  {
                     int p = waterPanels.get(rp);
                     for(int tries=0; tries<1000; tries++)		//try 1000 times to find a river that is not on a road (including beaches)
                     {
                        if(!vertStreetCols[p].contains(riverCols[i]) && !vertStreetCols[p].contains(riverCols[i]-1) && !vertStreetCols[p].contains(riverCols[i]+1))
                           break;
                        riverCols[i] = (int)(Math.random() * (board[0].length - 2) )+ 1; //don't include 1st or last col because they are behind a border
                     }
                  }
               }
               if(riverCols.length==2 && riverCols[0]==riverCols[1])
               {
                  int temp = riverCols[0];
                  riverCols = new int[1];
                  riverCols[0] = temp;
               }
            
               for(int p=0; p<waterPanels.size(); p++)
               {
                  int i=0;
                  if(riverCols.length==2)
                  {
                     if(waterPanels.size()==6 && p>=3)
                     {
                        i=1;	
                     }
                  }
                  for(int r=0; r<board.length; r++)
                  {
                     if(horizStreetRows[p].contains(r))
                        board[r][riverCols[i]][waterPanels.get(p)]="S~1";			//horiz bridge
                     else
                        board[r][riverCols[i]][waterPanels.get(p)] = "~~~";			//water
                     if(width==2 && riverCols[i]+1 < board[0].length)
                     {
                        if(horizStreetRows[p].contains(r))
                           board[r][riverCols[i]+1][waterPanels.get(p)]="S~1";		//horiz bridge
                        else
                           board[r][riverCols[i]+1][waterPanels.get(p)] = "~~~";	//water
                     }
                  }  
                  if(riverCols[i]-1 >= 0)
                  { 
                     for(int r=0; r<board.length; r++)
                     {
                        if(horizStreetRows[p].contains(r))
                           board[r][riverCols[i]-1][waterPanels.get(p)]="SX1";	//intersection
                        else
                           board[r][riverCols[i]-1][waterPanels.get(p)] = "MD1";	//mud
                     }    
                  }
                  if(width == 1 && riverCols[i]+1 < board[0].length) 
                  {
                     for(int r=0; r<board.length; r++)
                     {
                        if(horizStreetRows[p].contains(r))
                           board[r][riverCols[i]+1][waterPanels.get(p)]="SX1";	//intersection
                        else
                           board[r][riverCols[i]+1][waterPanels.get(p)] = "MD1";	//mud
                        
                     }
                  }
                  else
                     if(width == 2 && riverCols[i]+2 < board[0].length) 
                     {
                        for(int r=0; r<board.length; r++)
                        {
                           if(horizStreetRows[p].contains(r))
                              board[r][riverCols[i]+2][waterPanels.get(p)]="SX1";	//intersection
                           else
                              board[r][riverCols[i]+2][waterPanels.get(p)] = "MD1";	//mud
                           
                        }
                     }
               }
            }
            else														//horizontal river
            {
               for(int rp=0; rp < waterPanels.size(); rp++)
               {
                  int p = waterPanels.get(rp);
                  numBridges[rp] = (int)(Math.random() * (vertStreetCols[p].size()-1) + 1);
                  if(numBridges[rp] < vertStreetCols[p].size())					//remove random elements from possible horiz bridges until it is the same size as numBridges
                  {
                     while(vertStreetCols[p].size() > numBridges[rp])
                     {
                        int rand = (int)(Math.random() * (vertStreetCols[p].size()-1) + 1);
                        vertStreetCols[p].remove(rand);
                     }
                  }
                  
               }
               int[] riverRows = new int[numRivers];				//stores the row #s for where each river runs thru
               for(int i=0; i < riverRows.length; i++)
               {
                  riverRows[i] = (int)(Math.random() * (board.length - 2) ) + 1;
                  for(int rp=0; rp<waterPanels.size(); rp++)
                  {
                     int p = waterPanels.get(rp);
                     for(int tries=0; tries<1000; tries++)		//try 1000 times to find a river that is not on a road (including beaches)
                     {
                        if(!horizStreetRows[p].contains(riverRows[i]) && !horizStreetRows[p].contains(riverRows[i]-1) && !horizStreetRows[p].contains(riverRows[i]+1))
                           break;
                        riverRows[i] = (int)(Math.random() * (board.length - 2) ) + 1;
                     }
                  }
               }
             
               if(riverRows.length==2 && riverRows[0]==riverRows[1])
               {
                  int temp = riverRows[0];
                  riverRows = new int[1];
                  riverRows[0] = temp;
               }
            
               for(int p=0; p<waterPanels.size(); p++)
               {
                  int i=0;
                  if(riverRows.length==2)
                  {
                     if(waterPanels.size()==6 && p>=3)
                     {
                        i=1;	
                     }
                  }
                  for(int c=0; c<board[0].length; c++)
                  {
                     if(vertStreetCols[p].contains(c))
                        board[riverRows[i]][c][waterPanels.get(p)]="S~2";			//vert bridge
                     else
                        board[riverRows[i]][c][waterPanels.get(p)] = "~~~";			//water
                     if(width==2 && riverRows[i]+1 < board.length)
                     {
                        if(vertStreetCols[p].contains(c))
                           board[riverRows[i]+1][c][waterPanels.get(p)]="S~2";		//vert bridge
                        else
                           board[riverRows[i]+1][c][waterPanels.get(p)] = "~~~";	//water
                     }
                  }  
                  if(riverRows[i]-1 >= 0)
                  { 
                     for(int c=0; c<board[0].length; c++)
                     {
                        if(vertStreetCols[p].contains(c))
                           board[riverRows[i]-1][c][waterPanels.get(p)]="SX1";	//intersection
                        else
                           board[riverRows[i]-1][c][waterPanels.get(p)] = "MD1";	//mud
                     }    
                  }
                  if(width == 1 && riverRows[i]+1 < board.length) 
                  {
                     for(int c=0; c<board[0].length; c++)
                     {
                        if(vertStreetCols[p].contains(c))
                           board[riverRows[i]+1][c][waterPanels.get(p)]="SX1";	//intersection
                        else
                           board[riverRows[i]+1][c][waterPanels.get(p)] = "MD1";	//mud
                        
                     }
                  }
                  else
                     if(width == 2 && riverRows[i]+2 < board.length) 
                     {
                        for(int c=0; c<board[0].length; c++)
                        {
                           if(vertStreetCols[p].contains(c))
                              board[riverRows[i]+2][c][waterPanels.get(p)]="SX1";	//intersection
                           else
                              board[riverRows[i]+2][c][waterPanels.get(p)] = "MD1";	//mud
                           
                        }
                     }
               }
            }
         }
         else			//not a river, but a beach and ocean
         {				//**********ADD OCEANSIDE*********************************
            int randSide = (int)(Math.random()*4);					//UP, RIGHT, DOWN or LEFT  
            int quarterRows = (int)(board.length * 0.25);		//25 % of the board dimensions to place the water
            int quarterCols = (int)(board[0].length * 0.25);
            int randBeach = 0;
            if(randSide == UP)
            {
               for(int i=0; i<3; i++)	//panels 0, 1 & 2 will have a beach
                  waterPanels.add(i);
            }
            else
               if(randSide == DOWN)
               {
                  for(int i=6; i<9; i++)	//panels 6, 7 & 8 will have a beach
                     waterPanels.add(i);
               }
               else
                  if(randSide == RIGHT)
                  {
                     horizWater = true;
                     for(int i=0; i<=6; i+=3)	//panels 0, 3 & 6 will have a beach
                        waterPanels.add(i);
                  }
                  else
                     if(randSide == LEFT)
                     {
                        horizWater = true;
                        for(int i=2; i<=8; i+=3)	//panels 2, 5 & 8 will have a beach
                           waterPanels.add(i);
                     }
            int[] numBridges = new int[3];
            if(randSide == UP || randSide == DOWN)
            {
               randBeach = (int)(Math.random() * quarterRows) + quarterRows;
               for(int i=0; i<numBridges.length; i++)
               {
                  int p = waterPanels.get(i);
                  numBridges[i] = (int)(Math.random() * vertStreetCols[p].size());
                  if(numBridges[i] < vertStreetCols[p].size())					//remove random elements from possible vertical bridges until it is the same size as numBridges
                  {
                     while(vertStreetCols[p].size() > numBridges[i])
                     {
                        int rand = (int)(Math.random() * vertStreetCols[p].size());
                        vertStreetCols[p].remove(rand);
                     }
                  }
               }
            }
            else
               if(randSide == RIGHT || randSide == LEFT)
               {
                  randBeach = (int)(Math.random() * quarterCols) + quarterCols;
                  for(int i=0; i<numBridges.length; i++)
                  {
                     int p = waterPanels.get(i);
                     numBridges[i] = (int)(Math.random() * horizStreetRows[p].size());
                     if(numBridges[i] < horizStreetRows[p].size())					//remove random elements from possible vertical bridges until it is the same size as numBridges
                     {
                        while(horizStreetRows[p].size() > numBridges[i])
                        {
                           int rand = (int)(Math.random() * horizStreetRows[p].size());
                           horizStreetRows[p].remove(rand);
                        }
                     }
                  }
               }
         
            if(randSide == UP)
            {
               for(int r=0; r<board.length && r<randBeach; r++)
                  for(int c=0; c<board[0].length; c++)
                     for(int p=0; p<waterPanels.size(); p++)
                     {
                        if(vertStreetCols[waterPanels.get(p)].contains(c))
                        {
                           if(r==randBeach-1)
                              board[r][c][waterPanels.get(p)]="SX1";							//intersection
                           else
                              board[r][c][waterPanels.get(p)]="S~2";							//vert bridge
                        }
                        else
                        {
                           if(r==randBeach-1)
                              board[r][c][waterPanels.get(p)]="MD2";							//sand
                           else
                              board[r][c][waterPanels.get(p)]="~~~";							//water
                        }
                     }
            }  
            else
               if(randSide == DOWN)
               {
                  randBeach = board.length - randBeach;
                  for(int r=randBeach; r<board.length; r++)
                     for(int c=0; c<board[0].length; c++)
                        for(int p=0; p<waterPanels.size(); p++)
                        {
                           if(vertStreetCols[waterPanels.get(p)].contains(c))
                           {
                              if(r==randBeach)
                                 board[r][c][waterPanels.get(p)]="SX1";					//intersection
                              else
                                 board[r][c][waterPanels.get(p)]="S~2";					//vert bridge
                           }
                           else
                           {
                           
                              if(r==randBeach)
                                 board[r][c][waterPanels.get(p)]="MD2";						//sand
                              else
                                 board[r][c][waterPanels.get(p)]="~~~";						//water
                           }
                        }
               }   
               else
                  if(randSide == RIGHT)
                  {
                     for(int c=0; c<board[0].length && c<randBeach; c++)
                        for(int r=0; r<board.length; r++)
                           for(int p=0; p<waterPanels.size(); p++)
                           {
                              if(horizStreetRows[waterPanels.get(p)].contains(r))
                              {
                                 if(c==randBeach-1)
                                    board[r][c][waterPanels.get(p)]="SX1";					//intersection
                                 else
                                    board[r][c][waterPanels.get(p)]="S~1";					//horiz bridge
                              }
                              else
                              {
                                 if(c==randBeach-1)
                                    board[r][c][waterPanels.get(p)]="MD2";					//sand
                                 else
                                    board[r][c][waterPanels.get(p)]="~~~";					//water
                              }
                           }
                  }  
                  else
                     if(randSide == LEFT)
                     {                  
                        randBeach = board.length - randBeach;
                        for(int c=randBeach; c<board[0].length; c++)
                           for(int r=0; r<board.length; r++)
                              for(int p=0; p<waterPanels.size(); p++)
                              {
                                 if(horizStreetRows[waterPanels.get(p)].contains(r))
                                 {
                                    if(c==randBeach)
                                       board[r][c][waterPanels.get(p)]="SX1";					//intersection
                                    else
                                       board[r][c][waterPanels.get(p)]="S~1";					//horiz bridge
                                 }
                                 else
                                 {
                                    if(c==randBeach)
                                       board[r][c][waterPanels.get(p)]="MD2";				//sand
                                    else
                                       board[r][c][waterPanels.get(p)]="~~~";				//water
                                 }
                              }
                     }   
         }
      }
      if(gameMode == EARTH_INVADERS)	//4 blocks of two structures together near the bottom
      {
         for(int r=1;r<board.length-1;r++)	//**********ADD STRUCTURES************************
         {
            for(int c=1;c<board[0].length-1;c++)
            {
               for(int p=0; p<board[0][0].length; p++)
               {	
               //***ADD FOREST
                  if(forests && inForest(r,c,p,forestPanels, forestDimensions) && board[r][c][p].indexOf("~")==-1 && !board[r][c][p].startsWith("SR") && !board[r][c][p].startsWith("SU") && !board[r][c][p].startsWith("SX") && !board[r][c][p].equals("MD2") && structures[r][c][p]==null)				
                  {																					//not water or bridge                    or a vert road                    or a horiz road                        or an intersection                  or sand
                     board[r][c][p] = "PK2";				//forest
                     int index = (int)(Math.random()*treeImages.length);
                     long value = (int)(Math.random()*1000)+2000;
                     // if(p == 4)		//we only protect the center panel in CITY_SAVER
                        // propertyValue += value;
                     structures[r][c][p] = new Structure("TREES", r, c, p, treeImages, animation_delay*3, true, true, 1, -1, cellSize, index, value);	
                  }
                  else
                     //***ADD SUBURBS
                     if(board[r][c][p].equals("S--") && structures[r][c][p]==null)					
                     {
                        if(suburbPanels.contains(p))					//suburb panel
                        {											
                           double parkProb = 0.05;
                           double forestProb = 0.30;
                           double suburbProb = 0.35;
                           double rand = Math.random();
                           if(rand < parkProb)
                              board[r][c][p] = "PK1";						//park 1, passable
                           if(rand < forestProb)
                           {
                              board[r][c][p] = "PK2";				//forest
                              int treeType = (int)(Math.random()*3);
                              if(treeType<=1)
                              {
                                 int index = (int)(Math.random()*treeImages.length);
                                 long value = (int)(Math.random()*1000)+2000;
                                 // if(p == 4)		//we only protect the center panel in CITY_SAVER
                                    // propertyValue += value;
                                 structures[r][c][p] = new Structure("TREES", r, c, p, treeImages, animation_delay*3, true, true, 1, -1, cellSize, index, value);
                              }                                       
                              else
                                 structures[r][c][p] = null;
                           }
                           else
                           {
                              board[r][c][p] = "PK3";						//field, passable
                              int index = (int)(Math.random()*houseImages.length);				//index of structure image
                              long value = (int)( houseValue());
                              // if(p == 4)		//we only protect the center panel in CITY_SAVER
                                 // propertyValue += value;
                              structures[r][c][p] = new Structure("BLDG - HOUSES", r, c, p, houseImages, animation_delay*3, true, true, 1, -1, cellSize, index, value);	
                           }
                        }
                     }
               }
            }
         }
         for(int r=board.length-4; r<=board.length-3; r++)
         {
            for(int i=0; i<8; i++)
            {
               int c=0;
               if(i==0) c = 3;
               else if(i==1) c = 4;
               else if(i==2) c = 6;
               else if(i==3) c = 7;
               else if(i==4) c = 9;
               else if(i==5) c = 10;
               else if(i==6) c = 12;
               else if(i==7) c = 13;
               if(!board[r][c][4].startsWith("SR") && !board[r][c][4].startsWith("SU"))
               {
                  String randLot = "SX2";		//pick random parking lot
                  if(Math.random() < .5)
                     randLot = "SX3";
                  if(Math.random() < .5)
                     board[r][c][4] = randLot;
                  if(Math.random() < .5)
                  {   
                     int index = (int)(Math.random()*buisnessImages.length);				//index of structure image
                     long value = (int)( buisnessValue());
                     // propertyValue += value;
                     structures[r][c][4] = new Structure("BLDG - BUISNESS", r, c, 4, buisnessImages, animation_delay*3, false, true, 1, 100, cellSize, index, value);	
                  }
                  else
                  {
                     int index = (int)(Math.random()*buildingImages.length);				//index of structure image
                     long value = (int)( highRiseValue());
                     // propertyValue += value;
                     structures[r][c][4] = new Structure("BLDG - HIGHRISE", r, c, 4, buildingImages, animation_delay*3, false, true, 1, 200, cellSize, index, value);
                  }
               }
            }          
         }
         addRailroadTracks(allHorizStreetRows, allVertStreetCols);
         return;
      }  //***END if gameMode == EARTH_INVADERS
      for(int r=1;r<board.length-1;r++)	//**********ADD STRUCTURES************************
      {
         for(int c=1;c<board[0].length-1;c++)
         {
            for(int p=0; p<board[0][0].length; p++)
            {	//***ADD AMUSEMENT PARK (structure index 2-ferris wheel, [0, 1 roller coaster], 3 rides, 4 tent)
               if(park && inPark(r,c,p,parkPanel,parkDimensions) && structures[r][c][p]==null && board[r][c][p].indexOf("~")==-1 && !board[r][c][p].startsWith("SR") && !board[r][c][p].startsWith("SU") && !board[r][c][p].startsWith("SX") && !board[r][c][p].equals("MD2"))
               {
                  board[r][c][p] = parkSurface;					//field or concrete
                  
                  int randRide = (int)(Math.random()*(rideImages.length-3))+3;	//index 3-rides or 4-tent (we want only one roller coaster and ferris wheel, so only pick from what is left)
                  while (randRide==1)		//we don't want index 1, because that is the right side of the roller coaster
                     randRide = (int)(Math.random()*(rideImages.length));
                  if(parkRides.size() > 0)
                  {
                     int rideIndex = (int)(Math.random()*parkRides.size());
                     randRide = parkRides.remove(rideIndex);
                  }
                  	
                  int rideHealth = -1;
                  if(randRide <= 2)	//ferris wheel and roller coaster have higher health
                     rideHealth = 35;   
                  boolean passable = false;
                  if(randRide > 2)	//rides and tent are passable
                     passable = true;
                  if(randRide == 0 && inPark(r,c+1,p,parkPanel,parkDimensions) && board[r][c+1][p].indexOf("~")==-1 && !isRoad(r,c+1,p) && !board[r][c+1][p].equals("MD2"))	
                  {//left half of roller coaster - put right half on the other side if you can
                     long value = 85000;
                     // if(p == 4)		//we only protect the center panel in CITY_SAVER
                        // propertyValue += (value*2);
                     structures[r][c][p] = new Structure("BLDG - RIDE", r, c, p, rideImages, animation_delay*3, false, true, 1, rideHealth, cellSize, randRide, value);
                     structures[r][c+1][p] = new Structure("BLDG - RIDE", r, c+1, p, rideImages, animation_delay*3, false, true, 1, rideHealth, cellSize, 1, value);
                     board[r][c+1][p] = parkSurface;				//field or concrete
                  }   
                  else
                  {
                     if(randRide == 0)	//we tried to place a roller coaster, but didn't have room - so place rides or a tent
                        randRide = (int)(Math.random()*(rideImages.length-3))+3;	
                     long value = 50000;
                     // if(p == 4)		//we only protect the center panel in CITY_SAVER
                        // propertyValue += value;
                     structures[r][c][p] = new Structure("BLDG - RIDE", r, c, p, rideImages, animation_delay*3, passable, true, 1, rideHealth, cellSize, randRide, value);
                  }
               }
               else
               //***ADD CASINO DISTRICT
                  if(casino && inPark(r,c,p,casinoPanel,casinoDimensions) && structures[r][c][p]==null && board[r][c][p].indexOf("~")==-1 && !board[r][c][p].startsWith("SR") && !board[r][c][p].startsWith("SU") && !board[r][c][p].startsWith("SX") && !board[r][c][p].equals("MD2"))
                  {
                     board[r][c][p] = casinoSurface;					//parking lot
                     int randCasino = (int)(Math.random()*casinoImages.length);
                     if(casinos.size() > 0)
                     {
                        int casinoIndex = (int)(Math.random()*casinos.size());
                        randCasino = casinos.remove(casinoIndex);
                     }	
                     long value = (int)( towerValue()*3);
                     // if(p == 4)		//we only protect the center panel in CITY_SAVER
                        // propertyValue += value;
                     structures[r][c][p] =  new Structure("BLDG - CASINO", r, c, p, casinoImages, animation_delay*3, false, true, 1, 75, cellSize, randCasino, value);
                  }
                  else
                  //***ADD FOREST
                     if(forests && inForest(r,c,p,forestPanels, forestDimensions) && board[r][c][p].indexOf("~")==-1 && !board[r][c][p].startsWith("SR") && !board[r][c][p].startsWith("SU") && !board[r][c][p].startsWith("SX") && !board[r][c][p].equals("MD2") && structures[r][c][p]==null)				
                     {																					//not water or bridge                    or a vert road                    or a horiz road                        or an intersection                  or sand
                        board[r][c][p] = "PK2";				//forest
                        int index = (int)(Math.random()*treeImages.length);
                        long value = (int)(Math.random()*1000)+2000;
                        // if(p == 4)		//we only protect the center panel in CITY_SAVER
                           // propertyValue += value;
                        structures[r][c][p] = new Structure("TREES", r, c, p, treeImages, animation_delay*3, true, true, 1, -1, cellSize, index, value);
                     }
                     else
                     //***ADD SUBURBS
                        if(board[r][c][p].equals("S--") && structures[r][c][p]==null)					
                        {
                           if(suburbPanels.contains(p))					//suburb panel
                           {											
                              ArrayList<Integer> cityBorders = new ArrayList();	//do we have a city in a neighboring cell - if so, transition to bigger buildings
                              if(!suburbPanels.contains(p+1))
                                 cityBorders.add(RIGHT);
                              if(!suburbPanels.contains(p-1))
                                 cityBorders.add(LEFT);
                              if(!suburbPanels.contains(p+3))
                                 cityBorders.add(DOWN);
                              if(!suburbPanels.contains(p-3))
                                 cityBorders.add(UP);
                           
                              double parkProb = 0.05;
                              double highriseProb = 0.10;
                              double buisnessProb = 0.20;
                              double forestProb = 0.30;
                              double suburbProb = 0.35;
                              double rand = Math.random();
                              if((cityBorders.contains(UP) && r < 3) || (cityBorders.contains(DOWN) && r > board.length-4) || (cityBorders.contains(LEFT) && c < 3) || (cityBorders.contains(RIGHT) && c > board[0].length-4) )
                              {	//make borders with the city contain more city like structures
                                 forestProb = 0.05;
                                 parkProb = 0.10;
                                 suburbProb = 0.20;
                                 buisnessProb = 0.30;
                                 highriseProb = 0.35;
                                 if(rand < forestProb)
                                 {
                                    board[r][c][p] = "PK2";				//forest
                                    int treeType = (int)(Math.random()*3);
                                    if(treeType<=1)
                                    {
                                       int index = (int)(Math.random()*treeImages.length);
                                       long value = (int)(Math.random()*1000)+2000;
                                       // if(p == 4)		//we only protect the center panel in CITY_SAVER
                                          // propertyValue += value;
                                       structures[r][c][p] = new Structure("TREES", r, c, p, treeImages, animation_delay*3, true, true, 1, -1, cellSize, index, value);
                                    }                                       
                                    else
                                       structures[r][c][p] = null;
                                 }
                                 else
                                    if(rand < parkProb)
                                       board[r][c][p] = "PK1";						//park 1, passable
                                    else
                                       if(!waterTowerInPanel[p] && Math.random() < 0.05)
                                       {
                                          waterTowerInPanel[p] = true;
                                          if(Math.random() < .5)
                                             board[r][c][p] = "PK3";	
                                          int index = (int)(Math.random()*waterTowerImages.length);		//index of structure image
                                          long value = 200000;
                                          if(index > 0)
                                             value = 100000;
                                          // if(p == 4)		//we only protect the center panel in CITY_SAVER
                                             // propertyValue += value;
                                          structures[r][c][p] = new Structure("BLDG - WATER TOWER", r, c, p, waterTowerImages, animation_delay*3, false, true, 1, 50, cellSize, index, value);
                                       }
                                       else
                                          if(!fuelDepotInPanel[p] && Math.random() < 0.05)
                                          {
                                             int index = (int)(Math.random()*fuelDepotImages.length);
                                             fuelDepotInPanel[p] = true;
                                             board[r][c][p] = "S--";	
                                             if(Math.random() < .25)
                                                board[r][c][p] = "PK3";	
                                             long value = 1000000;
                                             // if(p == 4)		//we only protect the center panel in CITY_SAVER
                                                // propertyValue += value;
                                             structures[r][c][p] = new Structure("FUEL DEPOT", r, c, p, fuelDepotImages, animation_delay*3, false, true, 1, 15, cellSize, index, value);
                                          }
                                          else
                                             if(!lighthouseInPanel[p] && nextToABeach(r, c, p) && Math.random() < 0.25)
                                             {
                                                int index = 9;
                                                lighthouseInPanel[p] = true;
                                                board[r][c][p] = "PK3";	
                                                if(Math.random() < .25)
                                                   board[r][c][p] = "PK2";	
                                                long value = (int)( towerValue()*10);
                                                // if(p == 4)		//we only protect the center panel in CITY_SAVER
                                                   // propertyValue += value;
                                                structures[r][c][p] = new Structure("BLDG - LIGHTHOUSE", r, c, p, landmarkImages, animation_delay*3, false, true, 2, 100, cellSize, index, value);
                                             }
                                             else
                                                if(rand < suburbProb)
                                                {
                                                   board[r][c][p] = "PK3";						//field, passable
                                                   int index = (int)(Math.random()*houseImages.length);				//index of structure image
                                                   long value = (int)( houseValue());
                                                   // if(p == 4)		//we only protect the center panel in CITY_SAVER
                                                      // propertyValue += value;
                                                   structures[r][c][p] = new Structure("BLDG - HOUSES", r, c, p, houseImages, animation_delay*3, true, true, 1, -1, cellSize, index, value);	
                                                }
                                                else
                                                   if(rand < buisnessProb)
                                                   {
                                                      int index = (int)(Math.random()*buisnessImages.length);				//index of structure image
                                                      String randLot = "SX2";		//pick random parking lot
                                                      if(Math.random() < .5)
                                                         randLot = "SX3";
                                                      if(index==2 && Math.random() < .25)
                                                      {
                                                         if(c+1 < board[0].length-1 && board[r][c+1][p].indexOf("~")==-1 && !board[r][c+1][p].startsWith("SR") && !board[r][c+1][p].startsWith("SU") && !board[r][c+1][p].startsWith("SX") && !board[r][c+1][p].equals("MD2"))	
                                                         {//left half of mall - put right half on the other side if you can
                                                            long value = (int)( buisnessValue());
                                                            // if(p == 4)		//we only protect the center panel in CITY_SAVER
                                                               // propertyValue += (value*2);
                                                            structures[r][c][p] = new Structure("BLDG - BUISNESS", r, c, p, buisnessImages, animation_delay*3, false, true, 1, 35, cellSize, index, value);
                                                            structures[r][c+1][p] = new Structure("BLDG - BUISNESS", r, c+1, p, buisnessImages, animation_delay*3, false, true, 1, 35, cellSize, 3, value);
                                                            board[r][c][p] = randLot;				
                                                            board[r][c+1][p] = randLot;				
                                                         }   
                                                         else//we tried to place the right half of the mall, but didn't have room so place another random buisness
                                                         {
                                                            index = (int)(Math.random()*(buisnessImages.length-2));
                                                            long value = (int)( buisnessValue());
                                                            // if(p == 4)		//we only protect the center panel in CITY_SAVER
                                                               // propertyValue += value;
                                                            structures[r][c][p] = new Structure("BLDG - BUISNESS", r, c, p, buisnessImages, animation_delay*3, false, true, 1, 25, cellSize, index, value);	
                                                         }
                                                      }
                                                      else
                                                      {
                                                         long value = (int)( buisnessValue());
                                                         // if(p == 4)		//we only protect the center panel in CITY_SAVER
                                                            // propertyValue += value;
                                                         if(nextToAStreet(r,c,p) && Math.random() < .25)
                                                         {
                                                            index = (int)(Math.random()*gasStationImages.length);
                                                            structures[r][c][p] = new Structure("FUEL - GAS STATION", r, c, p,gasStationImages, animation_delay*3, false, true, 1, 25, cellSize, index, value);	
                                                         }
                                                         else
                                                         {
                                                            structures[r][c][p] = new Structure("BLDG - BUISNESS", r, c, p, buisnessImages, animation_delay*3, false, true, 1, 25, cellSize, index, value);	
                                                         }
                                                      }
                                                   }
                                                   else
                                                   {
                                                      int index = (int)(Math.random()*buildingImages.length);				//index of structure image
                                                      long value = (int)( highRiseValue());
                                                      // if(p == 4)		//we only protect the center panel in CITY_SAVER
                                                         // propertyValue += value;
                                                      structures[r][c][p] = new Structure("BLDG - HIGHRISE", r, c, p, buildingImages, animation_delay*3, false, true, 1, 50, cellSize, index, value);
                                                   }
                              }  
                              else
                              {
                                 if(!waterTowerInPanel[p] && Math.random() < 0.05)
                                 {
                                    waterTowerInPanel[p] = true;
                                    if(Math.random() < .5)
                                       board[r][c][p] = "PK3";	
                                    int index = (int)(Math.random()*waterTowerImages.length);		//index of structure image
                                    long value = 200000;
                                    if(index > 0)
                                       value = 100000;
                                    // if(p == 4)		//we only protect the center panel in CITY_SAVER
                                       // propertyValue += value;   
                                    structures[r][c][p] = new Structure("BLDG - WATER TOWER", r, c, p, waterTowerImages, animation_delay*3, false, true, 1, 50, cellSize, index, value);
                                 }
                                 else
                                    if(!fuelDepotInPanel[p] && Math.random() < 0.05)
                                    {
                                       int index = (int)(Math.random()*fuelDepotImages.length);
                                       fuelDepotInPanel[p] = true;
                                       board[r][c][p] = "S--";	
                                       if(Math.random() < .25)
                                          board[r][c][p] = "PK3";	
                                       long value = 1000000;
                                       // if(p == 4)		//we only protect the center panel in CITY_SAVER
                                          // propertyValue += value;
                                       structures[r][c][p] = new Structure("FUEL DEPOT", r, c, p, fuelDepotImages, animation_delay*3, false, true, 1, 15, cellSize, index, value);
                                    }
                                    else
                                       if(!lighthouseInPanel[p] && nextToABeach(r, c, p) && Math.random() < 0.25)
                                       {
                                          int index = 9;
                                          lighthouseInPanel[p] = true;
                                          board[r][c][p] = "PK3";	
                                          if(Math.random() < .25)
                                             board[r][c][p] = "PK2";	
                                          long value = (int)( towerValue()*10);
                                          // if(p == 4)		//we only protect the center panel in CITY_SAVER
                                             // propertyValue += value;
                                          structures[r][c][p] = new Structure("BLDG - LIGHTHOUSE", r, c, p, landmarkImages, animation_delay*3, false, true, 2, 100, cellSize, index, value);
                                       }
                                       else
                                          if(rand < parkProb)
                                             board[r][c][p] = "PK1";						//park 1, passable
                                          else
                                             if(rand < highriseProb)
                                             {
                                                int index = (int)(Math.random()*buildingImages.length);				//index of structure image
                                                long value = (int)( highRiseValue());
                                                // if(p == 4)		//we only protect the center panel in CITY_SAVER
                                                   // propertyValue += value;
                                                structures[r][c][p] = new Structure("BLDG - HIGHRISE", r, c, p, buildingImages, animation_delay*3, false, true, 1, 50, cellSize, index, value);                                       }
                                             else
                                                if(rand < buisnessProb)
                                                {
                                                   int index = (int)(Math.random()*buisnessImages.length);				//index of structure image
                                                   String randLot = "SX2";		//pick random parking lot
                                                   if(Math.random() < .5)
                                                      randLot = "SX3";
                                                   if(index==2 && Math.random() < .25)
                                                   {
                                                      if(c+1 < board[0].length-1 && board[r][c+1][p].indexOf("~")==-1 && !board[r][c+1][p].startsWith("SR") && !board[r][c+1][p].startsWith("SU") && !board[r][c+1][p].startsWith("SX") && !board[r][c+1][p].equals("MD2"))	
                                                      {//left half of mall - put right half on the other side if you can
                                                         long value = (int)( buisnessValue());
                                                         // if(p == 4)		//we only protect the center panel in CITY_SAVER
                                                            // propertyValue += (value*2);
                                                         structures[r][c][p] = new Structure("BLDG - BUISNESS", r, c, p, buisnessImages, animation_delay*3, false, true, 1, 35, cellSize, index, value);
                                                         structures[r][c+1][p] = new Structure("BLDG - BUISNESS", r, c+1, p, buisnessImages, animation_delay*3, false, true, 1, 35, cellSize, 3, value);
                                                         board[r][c][p] = randLot;				
                                                         board[r][c+1][p] = randLot;				
                                                      }   
                                                      else//we tried to place the right half of the mall, but didn't have room so place another random buisness
                                                      {
                                                         index = (int)(Math.random()*(buisnessImages.length-2));
                                                         long value = (int)( buisnessValue());
                                                         // if(p == 4)		//we only protect the center panel in CITY_SAVER
                                                            // propertyValue += value;
                                                         structures[r][c][p] = new Structure("BLDG - BUISNESS", r, c, p, buisnessImages, animation_delay*3, false, true, 1, 25, cellSize, index, value);	
                                                      }
                                                   }
                                                   else
                                                   {
                                                      long value = (int)( buisnessValue());
                                                      // if(p == 4)		//we only protect the center panel in CITY_SAVER
                                                         // propertyValue += value;
                                                      if(nextToAStreet(r,c,p) && Math.random() < .25)
                                                      {
                                                         index = (int)(Math.random()*gasStationImages.length);
                                                         structures[r][c][p] = new Structure("FUEL - GAS STATION", r, c, p,gasStationImages, animation_delay*3, false, true, 1, 25, cellSize, index, value);	
                                                      }
                                                      else
                                                         structures[r][c][p] = new Structure("BLDG - BUISNESS", r, c, p, buisnessImages, animation_delay*3, false, true, 1, 25, cellSize, index, value);	
                                                   }
                                                }
                                                else
                                                   if(rand < forestProb)
                                                   {
                                                      board[r][c][p] = "PK2";				//forest
                                                      int treeType = (int)(Math.random()*3);
                                                      if(treeType<=1)
                                                      {
                                                         int index = (int)(Math.random()*treeImages.length);
                                                         long value = (int)(Math.random()*1000)+2000;
                                                         // if(p == 4)		//we only protect the center panel in CITY_SAVER
                                                            // propertyValue += value;
                                                         structures[r][c][p] = new Structure("TREES", r, c, p, treeImages, animation_delay*3, true, true, 1, -1, cellSize, index, value);
                                                      }                                       
                                                      else
                                                         structures[r][c][p] = null;
                                                   }
                                                   else
                                                   {
                                                      board[r][c][p] = "PK3";						//field, passable
                                                      int index = (int)(Math.random()*houseImages.length);				//index of structure image
                                                      long value = (int)( houseValue());
                                                      // if(p == 4)		//we only protect the center panel in CITY_SAVER
                                                         // propertyValue += value;
                                                      structures[r][c][p] = new Structure("BLDG - HOUSES", r, c, p, houseImages, animation_delay*3, true, true, 1, -1, cellSize, index, value);	
                                                   }
                              }
                           }				//city panel
                           else			//*****************ADD CITY PANEL*******************************************
                              if(Math.random() < 0.95  && structures[r][c][p]==null)	//leave a couple of empty lots in the city
                              {
                                 if(Math.random() < 0.05)
                                 {
                                    int parkType = (int)(Math.random()*2);
                                    if(parkType==0)
                                       board[r][c][p] = "PK1";						//park 1, passable
                                    else
                                    {
                                       board[r][c][p] = "PK3";						//field, passable
                                       int treeType = (int)(Math.random()*3);
                                       if(treeType<=1)
                                       {
                                          int index = (int)(Math.random()*treeImages.length);
                                          long value = (int)(Math.random()*1000)+2000;
                                          // if(p == 4)		//we only protect the center panel in CITY_SAVER
                                             // propertyValue += value;
                                          structures[r][c][p] = new Structure("TREES", r, c, p, treeImages, animation_delay*3, true, true, 1, -1, cellSize, index, value);
                                       }                                       
                                       else
                                          structures[r][c][p] = null;
                                    }
                                 }
                                 else
                                    if(Math.random() < 0.05)
                                    {
                                       board[r][c][p] = "#EH";					//elec tower 1, not passable
                                       int index = (int)(Math.random()*elecTowerImages.length);
                                       long value = 150000;
                                       // if(p == 4)		//we only protect the center panel in CITY_SAVER
                                          // propertyValue += value;
                                       structures[r][c][p] = new Structure("ELEC TOWER "+index, r, c, p, elecTowerImages, animation_delay, false, false, 1, 90, cellSize, index, value);
                                    }
                                    else
                                    {
                                       int buildingType = (int)(Math.random()*100);
                                       if(buildingType < 85)				//add buildings
                                       {
                                          int index = (int)(Math.random()*buildingImages.length);				//index of structure image
                                          long value = (int)( highRiseValue());
                                          // if(p == 4)		//we only protect the center panel in CITY_SAVER
                                             // propertyValue += value;
                                          structures[r][c][p] = new Structure("BLDG - HIGHRISE", r, c, p, buildingImages, animation_delay*3, false, true, 1, 50, cellSize, index, value);
                                       }
                                       else
                                          if(buildingType < 90)				//add buildings
                                          {
                                             int index = (int)(Math.random()*buisnessImages.length);				//index of structure image
                                             String randLot = "SX2";		//pick random parking lot
                                             if(Math.random() < .5)
                                                randLot = "SX3";
                                             if(index==2 && Math.random() < .25)
                                             {
                                                if(c+1 < board[0].length-1 && board[r][c+1][p].indexOf("~")==-1 && !board[r][c+1][p].startsWith("SR") && !board[r][c+1][p].startsWith("SU") && !board[r][c+1][p].startsWith("SX") && !board[r][c+1][p].equals("MD2"))	
                                                {//left half of mall - put right half on the other side if you can
                                                   long value = (int)( buisnessValue());
                                                   // if(p == 4)		//we only protect the center panel in CITY_SAVER
                                                      // propertyValue += (value*2);
                                                   structures[r][c][p] = new Structure("BLDG - BUISNESS", r, c, p, buisnessImages, animation_delay*3, false, true, 1, 35, cellSize, index, value);
                                                   structures[r][c+1][p] = new Structure("BLDG - BUISNESS", r, c+1, p, buisnessImages, animation_delay*3, false, true, 1, 35, cellSize, 3, value);
                                                   board[r][c][p] = randLot;				
                                                   board[r][c+1][p] = randLot;				
                                                }   
                                                else//we tried to place the right half of the mall, but didn't have room so place another random buisness
                                                {
                                                   long value = (int)( buisnessValue());
                                                   // if(p == 4)		//we only protect the center panel in CITY_SAVER
                                                      // propertyValue += value;
                                                   index = (int)(Math.random()*(buisnessImages.length-2));
                                                   structures[r][c][p] = new Structure("BLDG - BUISNESS", r, c, p, buisnessImages, animation_delay*3, false, true, 1, 25, cellSize, index, value);	
                                                }
                                             }
                                             else
                                             {
                                                long value = (int)( buisnessValue());
                                                // if(p == 4)		//we only protect the center panel in CITY_SAVER
                                                   // propertyValue += value;
                                                if(nextToAStreet(r,c,p) && Math.random() < .25)
                                                {
                                                   index = (int)(Math.random()*gasStationImages.length);
                                                   structures[r][c][p] = new Structure("FUEL - GAS STATION", r, c, p,gasStationImages, animation_delay*3, false, true, 1, 25, cellSize, index, value);	
                                                }
                                                else
                                                   structures[r][c][p] = new Structure("BLDG - BUISNESS", r, c, p, buisnessImages, animation_delay*3, false, true, 1, 25, cellSize, index, value);
                                             }
                                          }
                                          else
                                          {	
                                             int index = 0;				//index of structure image (tower 0)
                                             if(r>2 && !landmark && Math.random() < 0.5)
                                             {//place a random landmark 18, 19, 20, 21, 22, 23, 24, 25, 26
                                                landmark=true;
                                                index = (int)(Math.random()*landmarkImages.length - 1);//subtract 1 because the last index is the lighthouse
                                                int lmSize = 2;		//landmark height
                                                int lmHealth = 200;	//landmark health
                                                if(index<=1)			//ferris wheel/arch is only 1 high and easier to take down
                                                {
                                                   lmSize = 1;
                                                   lmHealth = 100;
                                                }
                                                long value = (int)( towerValue()*10);
                                                // if(p == 4)		//we only protect the center panel in CITY_SAVER
                                                   // propertyValue += (value);
                                                structures[r][c][p] =  new Structure("BLDG - LANDMARK", r, c, p, landmarkImages, animation_delay*3, false, true, lmSize, lmHealth, cellSize, index, value);
                                                if (landmarkImages[index][0][0].indexOf("monument")>=0 || Math.random() < .25)			//if its a monument, put grass behind it
                                                   board[r][c][p] = "PK3";	
                                             }
                                             else   
                                             {
                                                index = (int)(Math.random()*skyscraperImages.length);
                                                long value = (int)( towerValue());
                                                // if(p == 4)		//we only protect the center panel in CITY_SAVER
                                                   // propertyValue += value;
                                                structures[r][c][p] =  new Structure("BLDG - TOWER", r, c, p, skyscraperImages, animation_delay*3, false, true, 2, 90, cellSize, index, value);
                                             }
                                          }
                                    }
                              }
                              else		//empty lot - maybe make it a parking lot
                              {
                                 if(!waterTowerInPanel[p] && Math.random() < .25 && structures[r][c][p]==null)
                                 {
                                    waterTowerInPanel[p] = true;
                                    if(Math.random() < .5)
                                       board[r][c][p] = "PK3";	
                                    int index = (int)(Math.random()*waterTowerImages.length);		//index of structure image
                                    long value = 200000;
                                    if(index > 0)
                                       value = 100000;
                                    // if(p == 4)		//we only protect the center panel in CITY_SAVER
                                       // propertyValue += value;
                                    structures[r][c][p] = new Structure("BLDG - WATER TOWER", r, c, p, waterTowerImages, animation_delay*3, false, true, 1, 50, cellSize, index, value);
                                 }
                                 else
                                    if(!fuelDepotInPanel[p] && Math.random() < 0.05 && structures[r][c][p]==null)
                                    {
                                       int index = (int)(Math.random()*fuelDepotImages.length);
                                       fuelDepotInPanel[p] = true;
                                       board[r][c][p] = "S--";	
                                       if(Math.random() < .25)
                                          board[r][c][p] = "PK3";	
                                       long value = 1000000;
                                       // if(p == 4)		//we only protect the center panel in CITY_SAVER
                                          // propertyValue += value;
                                       structures[r][c][p] = new Structure("FUEL DEPOT", r, c, p, fuelDepotImages, animation_delay*3, false, true, 1, 15, cellSize, index, value);
                                    }
                                    else
                                       if(!lighthouseInPanel[p] && nextToABeach(r, c, p) && Math.random() < 0.25)
                                       {
                                          int index = 9;
                                          lighthouseInPanel[p] = true;
                                          board[r][c][p] = "PK3";	
                                          if(Math.random() < .25)
                                             board[r][c][p] = "PK2";	
                                          long value = (int)( towerValue()*10);
                                          // if(p == 4)		//we only protect the center panel in CITY_SAVER
                                             // propertyValue += value;
                                          structures[r][c][p] = new Structure("BLDG - LIGHTHOUSE", r, c, p, landmarkImages, animation_delay*3, false, true, 2, 100, cellSize, index, value);
                                       }
                                       else
                                       {
                                          int rand = (int)(Math.random()*5);
                                          if (rand<=1)		//0,1
                                             board[r][c][p] = "SX2";
                                          else
                                             if (rand<=3)	//2,3
                                                board[r][c][p] = "SX3";
                                       }
                              }
                        }
            }
         }
      }   
      
      addRailroadTracks(allHorizStreetRows, allVertStreetCols);
       	
   //clear the spot where monster spawns
      int r = board.length/2;
      int c = board[0].length/2;
      int p = panel;
      structures[r][ c][p] = null;
      if(board[r][c][p].indexOf("~") == -1)	//not in water or on bridge
      {
         if(board[r][c][p].startsWith("#"))
         {
            int parkType = (int)(Math.random()*2);
            if(parkType==0)
               board[r][c][p] = "PK2";						//forest, passable
            else
            {
               board[r][c][p] = "PK3";						//field, passable
               int treeType = (int)(Math.random()*3);
               if(treeType<=1)
               {
                  int index = (int)(Math.random()*treeImages.length);
                  long value = (int)(Math.random()*1000)+2000;
                  // if(p == 4)		//we only protect the center panel in CITY_SAVER
                     // propertyValue += value;
                  structures[r][c][p] = new Structure("TREES", r, c, p, treeImages, animation_delay*3, true, true, 1, -1, cellSize, index, value);
               }                                       
               else
                  structures[r][c][p] = null;
            }
         
         }
      }
          
      for( r=0; r<structures.length; r++)
         for( c=0; c<structures[0].length; c++)
            for( p=0; p<structures[0][0].length; p++)
            {
               if(r-1>=1 && structures[r-1][c][p]==null)
               {	//add structures to overlap on parks
                  if( (board[r][c][p].equals("PK2") || board[r][c][p].equals("PK3")))
                  {
                     int index = (int)(Math.random()*treeImages.length);
                     long value = (int)(Math.random()*1000)+2000;
                     // if(p == 4)		//we only protect the center panel in CITY_SAVER
                        // propertyValue += value;
                     structures[r-1][c][p] = new Structure("TREES", r, c, p, treeImages, animation_delay*3, true, true, 1, -1, cellSize, index, value);
                  }
                  else	//if one space above is a road (non-bridge) && the space below is not a road nor railroad tracks
                     if(r<board.length-2 && isRoad(r-1,c,p) && !isRoad(r,c,p) && !isTracks(r,c,p) && !isBridge(r,c,p) && !isBeach(r,c,p) && Math.random()<.33 && structures[r-1][c][p]==null) 
                     {	//add structures to overlap on streets
                        double houseProb = 0.25;
                        if(suburbPanels.contains(p))
                           houseProb = 0.75;
                        if(Math.random() < houseProb)
                        {   
                           int index = (int)(Math.random()*houseImages.length);				//index of structure image
                           long value = (int)( houseValue());
                           // if(p == 4)		//we only protect the center panel in CITY_SAVER
                              // propertyValue += value;
                           structures[r-1][c][p] = new Structure("BLDG - HOUSES", r, c, p, houseImages, animation_delay*3, true, true, 1, -1, cellSize, index, value);	
                        }
                        else
                           if(structures[r-1][c][p]==null)
                           {
                              long value = (int)( buisnessValue());
                              // if(p == 4)		//we only protect the center panel in CITY_SAVER
                                 // propertyValue += value;
                              if(Math.random() < .25)
                              {
                                 int index = (int)(Math.random()*gasStationImages.length);
                                 structures[r-1][c][p] = new Structure("FUEL - GAS STATION", r, c, p,gasStationImages, animation_delay*3, true, true, 1, -1, cellSize, index, value);	
                              }
                              else
                              {
                                 int index = (int)(Math.random()*buisnessImages.length);
                                 structures[r-1][c][p] = new Structure("BLDG - BUISNESS", r, c, p, buisnessImages, animation_delay*3, true, true, 1, -1, cellSize, index, value);
                              }
                           }
                     }
               }
            }
   }
 
 //pre:  p is a valid panel in structures array  
 //post: count the # of inhabitable structures in the panel p
   public static int countStructures(int p)
   {    
      int count = 0; 
      if(p==4)
         propertyValue = 0;        
      for(int r=1; r<structures.length-1; r++)
         for(int c=1; c<structures[0].length-1; c++)
            if(structures[r][c][p]!=null && habitable(structures[r][c][p]))
            {
               count++;
               if( (structures[r][c][p].getName().endsWith("LANDMARK") || structures[r][c][p].getPropertyValue() >= 100000000) && !landmarkPanels.contains(p)) 	//record where landmarks are for in-game map
                  landmarkPanels.add(p);
               if(p==4)							//add up total of property value in center panel for CITY_SAVER
                  propertyValue += structures[r][c][p].getPropertyValue();
            }
      return count;      
   }

//pre: r, c, p are valid indexes of board
//post:returns true if board[r][c][p] is a road
   public static boolean isRoad(int r, int c, int p)
   {
      if(board[r][c][p].startsWith("SR") || board[r][c][p].startsWith("SU") || board[r][c][p].startsWith("SX") || board[r][c][p].startsWith("TX"))
         return true;
      return false;
   }

 //pre: r, c, p are valid indexes of board
//post:returns true if board[r][c][p] is railraod tracks
   public static boolean isTracks(int r, int c, int p)
   {
      if(board[r][c][p].startsWith("TR") || board[r][c][p].startsWith("TU") || board[r][c][p].startsWith("TX"))
         return true;
      return false;
   }

 //pre: r, c, p are valid indexes of board
//post:returns true if board[r][c][p] is a bridge
   public static boolean isBridge(int r, int c, int p)
   {
      if(board[r][c][p].startsWith("S~"))
         return true;
      return false;
   }

  //pre: r, c, p are valid indexes of board
//post:returns true if board[r][c][p] is a beach
   public static boolean isBeach(int r, int c, int p)
   {
      if(board[r][c][p].startsWith("MD"))
         return true;
      return false;
   }

}