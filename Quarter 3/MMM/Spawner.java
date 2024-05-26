//Mash, Mangle & Munch - Rev. Dr. Douglas R Oberle, June 2012   doug.oberle@fcps.edu
//UNIT SPAWNING UTILITIES
   import java.util.ArrayList;
//handles spawning and respawning of player units
   public class Spawner extends MMMPanel
   {
   //pre: r & c are valid coordinates for the board
   //post:if there is an element of spawnPoints that has the coordinates r & c, it is removed
   //used to remove destroyed buildings from the possible human spawn points
      public static void removeFromSpawn(ArrayList<int[]> spawnPoints, int r, int c)
      {
         if(gameMode == EARTH_INVADERS || spawnPoints == null)
            return;
         for(int i=0; i<spawnPoints.size(); i++)
         {
            int[] coord = spawnPoints.get(i);
            if(coord != null && coord[0]==r && coord[1]==c)
            {
               spawnPoints.remove(i);
               return;
            }
         }
      }
         
   //post: returns the names of all available units to spawn depending on the threat level
   //Threat level population 0-4	(news, police, national guard, military, end game)
      public static ArrayList<String> findAvailableUnits()
      {
         ArrayList<String> units = new ArrayList();
         if (gameMode == BOMBER_DODGER)
         {
            for(int i=0; i<9; i++)
               units.add("AIR bomber");
            units.add("AIR nukebomber");
            return units;
         }  
            //***MYSTERY*****
         units.add("CAR civilian");		//car or bus
         units.add("CAR civilian");		//car or bus
         units.add("CROWD civilian");
         units.add("CROWD civilian");
         units.add("SPECIAL civilian");//newscopter, train or boat
         if(threatLevel>=1)
         {//***POLICE*****
            units.add("CAR police");
            units.add("CAR police");
         }
         if(threatLevel>=2)
         {//***NATL GUARD*****
            units.add("CYCLE police");
            units.add("CAR jeep");
            units.add("CAR jeep");
            units.add("CROWD troops");
            units.add("CROWD troops");
            units.add("BOAT coastguard");
         }
         if(threatLevel>=3)
         {//***MILITARY****
            units.add("CAR jeep");
            units.add("CROWD troops");
            units.add("BOAT destroyer");
            units.add("BOAT destroyer");
            units.add("TANK tank");
            units.add("TANK tank");
            units.add("AIR fighter");
         }
         if(threatLevel>=4)
         {//***GLOBAL*****
            units.add("TANK flame");
            units.add("TANK flame");
            units.add("TANK artillery");
            units.add("TANK artillery");
            units.add("TANK missile");
            units.add("TANK missile");
            units.add("AIR fighter");
            units.add("AIR fighter");
            units.add("AIR bomber");
            units.add("AIR nukebomber");
         }
         return units;
      }
   
   //post:  returns the available vehicle types for player 2 depending on threat level
      public static ArrayList<String> findAvailableP2Units()
      {
         ArrayList<String> units = new ArrayList();
         units.add("CYCLE police");
         units.add("CAR police");
         if(threatLevel>=2)
         {
            units.add("CROWD troops");
            units.add("CAR jeep");
         }
         if(threatLevel>=3)
         {
            units.add("CAR jeep");
            units.add("TANK tank");
         }
         if(threatLevel>=4)
         {
            units.add("TANK flame");
            units.add("TANK flame");
            units.add("TANK tank");
            units.add("TANK tank");
            units.add("TANK missile");
            units.add("TANK missile");
            units.add("TANK artillery");
            units.add("TANK artillery");
         }
         return units;
      }
   
   //post:  returns a random coordinate location for AI spawn point in the event we can't find a valid one
      public static int[] getRandomSpawn()
      {
         int [] coord = new int[2];
         coord[0] = (int)(Math.random()*board.length);
         coord[1] = (int)(Math.random()*board[0].length);
         return coord;  
      }
   
    //post:  returns a random coordinate location for 2nd player spawn point in the event we can't find a valid one
      public static int[] getRandomP2Spawn()
      {
         int [] coord = new int[2];
         coord[0] = (int)(Math.random()*(board.length-2)) + 1;
         coord[1] = (int)(Math.random()*(board[0].length-2)) + 1;
         return coord;  
      }
   
   	//return random property values for elements with variation
      public static double carValue()
      {
         return (Math.random()*80000) + 10000;
      }
   
   //pre: p is a valid panel index between 0 and 8
   //post:spawns enemy units in the panel p at the start of the game and when we change panels so that some units are moving throughout the map
   //     (unlike respawns which spawns vehicles "off" the visible map so that they move onto it in a natural way)
      public static void initialVehicleSpawn(int p)
      {
         int[] coord  = new int[2];						//the row & col of where a unit will spawn
         int rand = 0;
         if(projectiles.size() > 0)
            projectiles = new ArrayList();					//if we are changing panels, clear any active projectiles so they don't carry over to the new panel
         if(explosions.size() > 0)
            explosions = new ArrayList();				//if we are changing panels, clear any active explosions so they don't carry over to the new panel
         Player p3temp = null;							//if there is an AI monster, remember it so we can add it later
         Player p4temp = null;							//if the blop split, remember it so we can add it later
         if(p3toggle)
            p3temp = players[AIPLAYER];
         if(blopSplit)
            p4temp = players[BLOPSPLIT];
         for(int i=FIRST_VEHICLE; i<players.length; i++)		//clear out last panel's vehicles (index 0-7 are used for players and monsters)
         {
            players[i]=new Vehicle();  
         }
         if(p2toggle && !players[PLAYER2].getName().equals("NONE"))		//there is a player 2 - relocate them
         {
            String name = players[PLAYER2].getName();
         
            if(name.equals("CROWD troops"))
            {
               if(p2CarSpawnPoints[panel].size()>0)
               {
                  rand = (int)(Math.random() * p2CarSpawnPoints[panel].size());
                  coord = p2CarSpawnPoints[panel].get(rand);
               }
               else		//backup - no spawns, so pick a random loc
               {
                  coord = getRandomP2Spawn();
               }
               players[PLAYER2] =(new Vehicle("CROWD troops",  coord[0], coord[1], troopImages, animation_delay*2, 4, 25, 0)); 
            }
            else
               if(name.equals("TANK tank"))
               {
                  if(p2TankSpawnPoints[panel].size()>0)
                  {
                     rand = (int)(Math.random() * p2TankSpawnPoints[panel].size());
                     coord = p2TankSpawnPoints[panel].get(rand);
                  }
                  else		//backup - no spawns, so pick a random loc
                  {
                     coord = getRandomP2Spawn();
                  }
                  players[PLAYER2] =(new Vehicle("TANK tank", coord[0], coord[1],tankImages, animation_delay, 3, 100, 100000)); 
               }
               else
                  if(name.equals("TANK flame"))
                  {
                     if(p2TankSpawnPoints[panel].size()>0)
                     {
                        rand = (int)(Math.random() * p2TankSpawnPoints[panel].size());
                        coord = p2TankSpawnPoints[panel].get(rand);
                     }
                     else		//backup - no spawns, so pick a random loc
                     {
                        coord = getRandomP2Spawn();
                     }
                     players[PLAYER2] =(new Vehicle("TANK flame", coord[0], coord[1],flameTankImages, animation_delay, 2, 100, 150000)); 
                  }
                  else
                     if(name.equals("TANK missile"))
                     {
                        if(p2CarSpawnPoints[panel].size()>0)
                        {
                           rand = (int)(Math.random() * p2CarSpawnPoints[panel].size());
                           coord = p2CarSpawnPoints[panel].get(rand);
                        }
                        else		//backup - no spawns, so pick a random loc
                        {
                           coord = getRandomP2Spawn();
                        }
                        players[PLAYER2] =(new Vehicle("TANK missile", coord[0], coord[1],missileLauncherImages, animation_delay, 3, 200, 200000)); 
                     }
                     
                     else
                        if(name.equals("TANK artillery"))
                        {
                           if(p2CarSpawnPoints[panel].size()>0)
                           {
                              rand = (int)(Math.random() * p2CarSpawnPoints[panel].size());
                              coord = p2CarSpawnPoints[panel].get(rand);
                           }
                           else		//backup - no spawns, so pick a random loc
                           {
                              coord = getRandomP2Spawn();
                           }
                           players[PLAYER2] =(new Vehicle("TANK artillery", coord[0], coord[1],artilleryImages, animation_delay, 3, 100, 80000)); 
                        }  
                        else
                           if(name.equals("CAR jeep"))
                           {
                              if(p2CarSpawnPoints[panel].size()>0)
                              {
                                 rand = (int)(Math.random() * p2CarSpawnPoints[panel].size());
                                 coord = p2CarSpawnPoints[panel].get(rand);
                              }
                              else		//backup - no spawns, so pick a random loc
                              {
                                 coord = getRandomP2Spawn();
                              }
                              players[PLAYER2] =(new Vehicle("CAR jeep", coord[0], coord[1],jeepImages, animation_delay, 2, 50, 35000)); 
                           } 
                           else
                              if(name.equals("CYCLE police"))
                              {
                                 if(p2CarSpawnPoints[panel].size()>0)
                                 {
                                    rand = (int)(Math.random() * p2CarSpawnPoints[panel].size());
                                    coord = p2CarSpawnPoints[panel].get(rand);
                                 }
                                 else		//backup - no spawns, so pick a random loc
                                 {
                                    coord = getRandomP2Spawn();
                                 }
                                 players[PLAYER2] =(new Vehicle("CYCLE police", coord[0], coord[1], policeMotorcycleImages, animation_delay*2, 1, 50, 8000)); 
                              } 
                              else
                              {
                                 if(p2CarSpawnPoints[panel].size()>0)
                                 {
                                    rand = (int)(Math.random() * p2CarSpawnPoints[panel].size());
                                    coord = p2CarSpawnPoints[panel].get(rand);
                                 }
                                 else		//backup - no spawns, so pick a random loc
                                 {
                                    coord = getRandomP2Spawn();
                                 }
                                 players[PLAYER2] =(new Vehicle("CAR police", coord[0], coord[1],policeImages, animation_delay*2, 2, 75, 25000)); 
                              }
         
            players[PLAYER2].findX(cellSize);
            players[PLAYER2].findY(cellSize);
            explosions.add(new Explosion("SMALL", players[PLAYER2].getX()-(cellSize/2), players[PLAYER2].getY()-(cellSize/2), puffImages, animation_delay));
         }	//end player 2 relocation
      
         ArrayList<String> units = findAvailableUnits();
      //remove bomber, so it doesn't appear right when you change to a new panel
         for(int i=0; i<units.size(); i++)
         {
            if((units.get(i)).endsWith("nukebomber"))
            {
               units.remove(i);
               i--;
            }
         }
      
         ArrayList<int[]> carSpawns = new ArrayList();		//will only contain streets w/o structures
         ArrayList<int[]> boatSpawns = new ArrayList();		//will only contain water
         ArrayList<int[]> trainSpawns = new ArrayList();		//will only contain tracks
      
         boolean isWaterInPanel = false;
         boolean areTracksInPanel = false;  
         for(int r=0; r<board.length; r++)  
         {
            for(int c=0; c<board[0].length; c++)
            {
               int[] spot = new int[2];
               spot[0] = r;
               spot[1] = c;
               if(board[r][c][p].startsWith("T"))	
               {//tracks  - we want to start trains away from the edges because they will have multiple cars
                  if((board[r][c][p].startsWith("TU") && r>3 && r<board.length-3) || (board[r][c][p].startsWith("TR") && c>3 && c<board[0].length-3))
                  {
                     trainSpawns.add(spot);
                     areTracksInPanel = true;  
                  }
               }
               else	
                  if(board[r][c][p].startsWith("~"))	//water
                  {
                     boatSpawns.add(spot);
                     isWaterInPanel = true;  
                  }
                  else											//not water
                     if(structures[r][c][p]==null && board[r][c][p].startsWith("S"))
                     {
                        carSpawns.add(spot);			//not water, no structure, on a street
                     }
            }
         }
         String dayVersion = "";
         if(!day)
            dayVersion = "NIGHT";
         if(gameMode == CITY_SAVER)
         {
         //add monsters
            int numMonsters = 1;			//1 monster for EASY
            if(difficulty == MEDIUM)	//2 monsters for MEDIUM
               numMonsters = 2;
            else
               if(difficulty == HARD)	//3 monsters for HARD
                  numMonsters = 3;   
               else
                  if(difficulty == NIGHTMARE)	//4 monsters for NIGHTMARE
                     numMonsters = 4;   
            for(int m=AIMONSTER1, nm=0; m<=AIMONSTER4 && m<players.length && nm<numMonsters; m++, nm++)
            {  //AI monsters inhabit indexes 4,5,6 & 7
               int monsterType = (int)(Math.random()*5);
            //*****************************************************
               coord = getRandomP2Spawn();
               if(monsterType == 0)
                  players[m] = new Custom(coord[0], coord[1], customInfo, playerImages[Integer.parseInt(customInfo[1])]);
               else if(monsterType == 1)	//name, row, col, anim images, anim speed, stomp power, speed penalty, reload time
                  players[m] = new Gorilla(coord[0], coord[1], playerImages[0]);
               else if(monsterType == 2)
                  players[m] = new Dinosaur(coord[0], coord[1], playerImages[1]);
               else if(monsterType == 3)
                  players[m] = new Robot(coord[0], coord[1], playerImages[2]);
               else //if(monsterType == 4)
                  players[m] = new Insect(coord[0], coord[1], playerImages[3]);
            
               players[m].setSpeedPenalty(2);		//make monsters slower to give player vehicle a chance
               if(!players[m].isSwimmer() && board[coord[0]][coord[1]].equals("~~~"))
               {
                  int tries = 0;
                  while(!players[m].isSwimmer() && board[coord[0]][coord[1]].equals("~~~") && tries<1000)
                  {
                     coord = getRandomP2Spawn();
                     tries++;
                  }
                  players[m].setRow(coord[0]);
                  players[m].setCol(coord[1]);
               }
               players[m].findX(cellSize);
               players[m].findY(cellSize);
               explosions.add(new Explosion("SMALL", players[m].getX()-(cellSize/2), players[m].getY()-(cellSize/2), puffImages, animation_delay));         
            }              
         }
         else
            if(gameMode == EARTH_INVADERS)
            {
               int index = FIRST_VEHICLE;
               EI_moving = RIGHT;
               EI_vehicles = 0;
               int row = 2;
               int col = 1;
               for(int i=0; i<11 && index<players.length; i++)
               {
                  players[index++] = (new Vehicle("TANK missile", row, col++, missileLauncherImages, animation_delay, 4-(difficulty+1), 200, 200000));
                  EI_vehicles++;
               }
               col = 1;
               row++;
               for(int i=0; i<11 && index<players.length; i++)
               {
                  players[index++] = (new Vehicle("TANK tank", row, col++, tankImages, animation_delay, 4-(difficulty+1), 100, 100000));
                  EI_vehicles++;
               }
               col = 1;
               row++;
               for(int i=0; i<11 && index<players.length; i++)
               {
                  players[index++] = (new Vehicle("TANK flame", row, col++, flameTankImages, animation_delay, 4-(difficulty+1), 100, 150000));
                  EI_vehicles++;
               }
               col = 1;
               row++;
               for(int i=0; i<11 && index<players.length; i++)
               {
                  players[index++] = (new Vehicle("TANK tank", row, col++, tankImages, animation_delay, 4-(difficulty+1), 100, 100000));
                  EI_vehicles++;
               }
               col = 1;
               row++;
               for(int i=0; i<11 && index<players.length; i++)
               {
                  players[index++] = (new Vehicle("CAR jeep", row, col++, jeepImages, animation_delay, 4-(difficulty+1), 50, 35000));
                  EI_vehicles++;
               }
               col = 1;
               row++;
            
               int airType = (int)(Math.random()*4);
               int startPoint = (int)(Math.random()*2);
               if(startPoint==0)
               {
                  coord[0]=board.length-1;
                  coord[1]=0;
               }
               else
               {
                  coord[0]=board.length-1;
                  coord[1]=board[0].length-1;
               }
            
               if(airType == 0 && index<players.length)
                  players[index++] = (new Vehicle("AIR newscopter",  coord[0], coord[1], heliImages, animation_delay/2, 0, 0, 120000));
               else
                  if(airType == 1 && index<players.length)
                     players[index++] = (new Vehicle("AIR civilian", coord[0], coord[1], cessnaImages, animation_delay/2, 0, 0, 80000));
                  else
                     if(airType == 2 && index<players.length)
                        players[index++] = (new Vehicle("AIR fighter", coord[0], coord[1], fighterImages, animation_delay, 0, 35, 1500000));
                     else
                        if(index<players.length)
                        {
                           players[index++] = (new Vehicle("AIR bomber", coord[0], coord[1], bomber2Images, animation_delay, 1, 35, 2500000));
                        }
               if(areTracksInPanel && !trainInPanel[p] && trainSpawns.size() > 0)	//are there train tracks?  If so, add a train
               {
                  trainInPanel[p] = true;
                  rand = (int)(Math.random() * trainSpawns.size());
                  coord = trainSpawns.get(rand);
                                                      
                  players[TRAIN1] = (new Vehicle("TRAIN engine", coord[0], coord[1], trainImages[0], animation_delay, 2, 0, 120000));
                  int r = coord[0];
                  int c = coord[1];
                  if(board[r][c][panel].indexOf("U") >= 0)	//we are on a vertical track
                  {														//we want trains to follow the right wall to complete their track circuit
                     if(c==0)
                     {
                        players[TRAIN1].setBodyDirection(UP);
                        players[TRAIN1].clearDirections();
                        if(r+2<board.length)
                        {
                           players[TRAIN2] = (new Vehicle("TRAIN car", r+1, c, trainImages[1], animation_delay, 2, 0, 120000));
                           players[TRAIN2].setBodyDirection(UP);
                           players[TRAIN2].clearDirections();
                           players[TRAIN3] = (new Vehicle("TRAIN caboose", r+2, c, trainImages[2], animation_delay, 2, 0, 120000));
                           players[TRAIN3].setBodyDirection(UP);
                           players[TRAIN3].clearDirections();
                        }
                     }
                     else
                        if(c == board[0].length-1)
                        {
                           players[TRAIN1].setBodyDirection(DOWN);
                           players[TRAIN1].clearDirections();
                           if(r-2>=0)
                           {
                              players[TRAIN2] = (new Vehicle("TRAIN car", r-1, c, trainImages[1], animation_delay, 2, 0, 120000));
                              players[TRAIN2].setBodyDirection(DOWN);
                              players[TRAIN2].clearDirections();
                              players[TRAIN3] = (new Vehicle("TRAIN caboose", r-2, c, trainImages[2], animation_delay, 2, 0, 120000));
                              players[TRAIN3].setBodyDirection(DOWN);
                              players[TRAIN3].clearDirections();
                           }
                                 
                        }
                        else
                        {
                           if(Math.random() < .5)
                              players[TRAIN1].setBodyDirection(DOWN);
                           else
                              players[TRAIN1].setBodyDirection(UP);
                        }
                  }
                  else
                  {
                     if(r==0)
                     {
                        players[TRAIN1].setBodyDirection(RIGHT);
                        players[TRAIN1].clearDirections();
                        if(c-2>=0)
                        {
                           players[TRAIN2] = (new Vehicle("TRAIN car", r, c-1, trainImages[1], animation_delay, 2, 0, 120000));
                           players[TRAIN2].setBodyDirection(RIGHT);
                           players[TRAIN2].clearDirections();
                           players[TRAIN3] = (new Vehicle("TRAIN caboose", r, c-2, trainImages[2], animation_delay, 2, 0, 120000));
                           players[TRAIN3].setBodyDirection(RIGHT);
                           players[TRAIN3].clearDirections();
                        }
                     }
                     else
                        if(r == board.length-1)
                        {
                           players[TRAIN1].setBodyDirection(LEFT);
                           players[TRAIN1].clearDirections();
                           if(c+2 <board[0].length)
                           {
                              players[TRAIN2] = (new Vehicle("TRAIN car", r, c+1, trainImages[1], animation_delay, 2, 0, 120000));
                              players[TRAIN2].setBodyDirection(LEFT);
                              players[TRAIN2].clearDirections();
                              players[TRAIN3] = (new Vehicle("TRAIN caboose", r, c+2, trainImages[2], animation_delay, 2, 0, 120000));
                              players[TRAIN3].setBodyDirection(LEFT);
                              players[TRAIN3].clearDirections();
                           }
                        }
                        else
                        {
                                 
                           if(Math.random() < .5)
                              players[TRAIN1].setBodyDirection(LEFT);
                           else
                              players[TRAIN1].setBodyDirection(RIGHT);
                        }
                  }        
               }
               return; 
            }//***end EARTH INVADERS
      
         int randUnit = 0;
         for(int i=FIRST_VEHICLE; i<players.length; i++)	
         {
            randUnit = (int)(Math.random()*units.size());
                 
            if(randUnit >= units.size())
               randUnit = 0;      
            String unit = units.get(randUnit);	
            if(carSpawns.size() > 0)
            {
               rand = (int)(Math.random() * carSpawns.size());
               coord = carSpawns.get(rand);
            }
            else		//backup - no spawns, so pick a random loc
            {
               coord = getRandomSpawn();
            }
            int fireInPanel = countStructureFires();
            boolean firetruck = firetruckPresent();
            double rnd = Math.random();
            if(fireInPanel > 0 && !firetruck && rnd < .75)
               players[i] = (new Vehicle("CAR firetruck", coord[0], coord[1], firetruckImages, animation_delay*2, 2, 0, 120000));
            else if(unit.equals("CAR civilian"))
            {
               if(rnd < .66)
                  players[i] = (new Vehicle("CAR civilian",coord[0], coord[1], carImages, animation_delay, 3, 0, (int)carValue()));
               else if(rnd < .95)
                  players[i] = (new Vehicle("CAR bus", coord[0], coord[1], busImages, animation_delay, 3, 0, 80000));
               else
                  players[i] = (new Vehicle("CAR firetruck", coord[0], coord[1], firetruckImages, animation_delay*2, 2, 0, 120000));
            }	
            else if(unit.equals("CROWD civilian"))
               players[i] = (new Vehicle("CROWD civilian", coord[0], coord[1], crowdImages, animation_delay*2, 5, 0, 0)); 
            else if(unit.equals("CROWD troops"))
               players[i] = (new Vehicle("CROWD troops", coord[0], coord[1], troopImages, animation_delay*2, 4, 25, 0)); 
            else if(unit.equals("SPECIAL civilian"))
            {
               if(areTracksInPanel && !trainInPanel[p] && trainSpawns != null && trainSpawns.size() > 0)	//are there train tracks?  If so, add a train
               {
                  trainInPanel[p] = true;
                  rand = (int)(Math.random() * trainSpawns.size());
                  coord = trainSpawns.get(rand);
                                                      
                  players[TRAIN1] = (new Vehicle("TRAIN engine", coord[0], coord[1], trainImages[0], animation_delay, 2, 0, 120000));
                  int r = coord[0];
                  int c = coord[1];
                  if(board[r][c][panel].indexOf("U") >= 0)	//we are on a vertical track
                  {														//we want trains to follow the right wall to complete their track circuit
                     if(c==0)
                     {
                        players[TRAIN1].setBodyDirection(UP);
                        players[TRAIN1].clearDirections();
                        if(r+2<board.length)
                        {
                           players[TRAIN2] = (new Vehicle("TRAIN car", r+1, c, trainImages[1], animation_delay, 2, 0, 120000));
                           players[TRAIN2].setBodyDirection(UP);
                           players[TRAIN2].clearDirections();
                           players[TRAIN3] = (new Vehicle("TRAIN caboose", r+2, c, trainImages[2], animation_delay, 2, 0, 120000));
                           players[TRAIN3].setBodyDirection(UP);
                           players[TRAIN3].clearDirections();
                        }
                     }
                     else if(c == board[0].length-1)
                     {
                        players[TRAIN1].setBodyDirection(DOWN);
                        players[TRAIN1].clearDirections();
                        if(r-2>=0)
                        {
                           players[TRAIN2] = (new Vehicle("TRAIN car", r-1, c, trainImages[1], animation_delay, 2, 0, 120000));
                           players[TRAIN2].setBodyDirection(DOWN);
                           players[TRAIN2].clearDirections();
                           players[TRAIN3] = (new Vehicle("TRAIN caboose", r-2, c, trainImages[2], animation_delay, 2, 0, 120000));
                           players[TRAIN3].setBodyDirection(DOWN);
                           players[TRAIN3].clearDirections();
                        }
                                 
                     }
                     else
                     {
                        if(Math.random() < .5)
                           players[TRAIN1].setBodyDirection(DOWN);
                        else
                           players[TRAIN1].setBodyDirection(UP);
                     }
                  }
                  else
                  {
                     if(r==0)
                     {
                        players[TRAIN1].setBodyDirection(RIGHT);
                        players[TRAIN1].clearDirections();
                        if(c-2>=0)
                        {
                           players[TRAIN2] = (new Vehicle("TRAIN car", r, c-1, trainImages[1], animation_delay, 2, 0, 120000));
                           players[TRAIN2].setBodyDirection(RIGHT);
                           players[TRAIN2].clearDirections();
                           players[TRAIN3] = (new Vehicle("TRAIN caboose", r, c-2, trainImages[2], animation_delay, 2, 0, 120000));
                           players[TRAIN3].setBodyDirection(RIGHT);
                           players[TRAIN3].clearDirections();
                        }
                     }
                     else if(r == board.length-1)
                     {
                        players[TRAIN1].setBodyDirection(LEFT);
                        players[TRAIN1].clearDirections();
                        if(c+2 <board[0].length)
                        {
                           players[TRAIN2] = (new Vehicle("TRAIN car", r, c+1, trainImages[1], animation_delay, 2, 0, 120000));
                           players[TRAIN2].setBodyDirection(LEFT);
                           players[TRAIN2].clearDirections();
                           players[TRAIN3] = (new Vehicle("TRAIN caboose", r, c+2, trainImages[2], animation_delay, 2, 0, 120000));
                           players[TRAIN3].setBodyDirection(LEFT);
                           players[TRAIN3].clearDirections();
                        }
                     }
                     else
                     {
                                 
                        if(Math.random() < .5)
                           players[TRAIN1].setBodyDirection(LEFT);
                        else
                           players[TRAIN1].setBodyDirection(RIGHT);
                     }
                  }
               }
               else if(isWaterInPanel && boatSpawns.size() > 0)
               {
                  if(Math.random() < .5)
                  {
                     rand = (int)(Math.random() * boatSpawns.size());
                     coord = boatSpawns.get(rand);
                     String[][][]temp=new String[1][1][1];
                     if(Math.random() < .5)
                     {
                        temp[0][0][0] = "images/vehicles/boat/boat" + dayVersion + ".GIF";
                        players[i] = (new Vehicle("BOAT commerce", coord[0], coord[1], temp, animation_delay, 5, 0, 90000));
                     }
                     else
                     {
                        temp[0][0][0] = "images/vehicles/boat/sailboat" + dayVersion + ".GIF";
                        players[i] = (new Vehicle("BOAT civilian", coord[0], coord[1], temp, animation_delay, 5, 0, 5000));
                     }
                  }
                  else
                  {
                     coord = getRandomSpawn();
                     int randVehicle = (int)(Math.random()*3);
                     if(randVehicle==0)
                        players[i] = (new Vehicle("AIR newscopter", coord[0], coord[1], heliImages, animation_delay/2, 0, 0, 120000));
                     else
                        if(randVehicle==1)
                           players[i] = (new Vehicle("AIR civilian", coord[0], coord[1], cessnaImages, animation_delay/2, 0, 0, 80000));
                        else
                           players[i] = (new Vehicle("CYCLE civilian", coord[0], coord[1], motorcycleImages, animation_delay,  1, 0, 5000));
                  }
               }
               else
               {
                  coord = getRandomSpawn();
                  int randVehicle = (int)(Math.random()*3);
                  if(randVehicle==0)
                     players[i] = (new Vehicle("AIR newscopter", coord[0], coord[1], heliImages, animation_delay/2, 0, 0, 120000));
                  else
                     if(randVehicle==1)
                        players[i] = (new Vehicle("AIR civilian", coord[0], coord[1], cessnaImages, animation_delay/2, 0, 0, 80000));
                     else
                        players[i] = (new Vehicle("CYCLE civilian", coord[0], coord[1], motorcycleImages, animation_delay,  1, 0, 5000));
               }
            }
            else if(unit.equals("CYCLE police"))
               players[i] = (new Vehicle("CYCLE police", coord[0], coord[1], policeMotorcycleImages, animation_delay*2, 1, 50, 8000));
            else if(unit.equals("CAR police"))
               players[i] = (new Vehicle("CAR police", coord[0], coord[1], policeImages, animation_delay*2, 2, 75, 25000));
            else if(unit.equals("CAR jeep"))
               players[i] = (new Vehicle("CAR jeep", coord[0], coord[1],jeepImages, animation_delay, 2, 50, 35000));
            else if(unit.equals("TANK tank"))
               players[i] = (new Vehicle("TANK tank", coord[0], coord[1],tankImages, animation_delay,  3, 100, 100000));
            else if(unit.equals("TANK flame"))
               players[i] = (new Vehicle("TANK flame", coord[0], coord[1],flameTankImages, animation_delay, 2, 100, 150000));
            else if(unit.equals("TANK missile"))
               players[i] = (new Vehicle("TANK missile", coord[0], coord[1],missileLauncherImages, animation_delay, 3, 200, 200000));
            else if(unit.equals("TANK artillery"))
               players[i] = (new Vehicle("TANK artillery", coord[0], coord[1],artilleryImages, animation_delay, 3, 100, 80000));
            else if(unit.equals("BOAT coastguard") && isWaterInPanel && boatSpawns.size() > 0)
            {
               rand = (int)(Math.random() * boatSpawns.size());
               coord = boatSpawns.get(rand);
               String[][][]temp=new String[1][1][1];
               temp[0][0][0] = "images/vehicles/boat/coastguard" + dayVersion + ".GIF";
               players[i] = (new Vehicle("BOAT coastguard", coord[0], coord[1], temp, animation_delay, 5, 50, 180000));
            }
            else if(unit.equals("BOAT destroyer") && isWaterInPanel && boatSpawns.size() > 0)
            {
               rand = (int)(Math.random() * boatSpawns.size());
               coord = boatSpawns.get(rand);
               String[][][]temp=new String[1][1][1];
               temp[0][0][0] = "images/vehicles/boat/destroyer" + dayVersion + ".GIF";
               players[i] = (new Vehicle("BOAT destroyer", coord[0], coord[1], temp, animation_delay, 5, 100, 2500000));
            }
            else if(unit.equals("AIR fighter"))
            {
               coord = getRandomSpawn();
               players[i] = (new Vehicle("AIR fighter", coord[0], coord[1], fighterImages, animation_delay, 0, 35, 1500000));
            }
            else if(unit.equals("AIR bomber"))
            {
               coord = getRandomSpawn();
               if(coord[0]==board.length/2 || coord[1]==board[0].length/2)
                  for(int numTries=0; numTries<1000; numTries++)
                  {	//we don't want bombers to spawn in the same row/col as the monster when we start
                     if(coord[0]!=board.length/2 && coord[1]!=board[0].length/2)
                        break;
                     coord = getRandomSpawn();
                  }
               players[i] = (new Vehicle("AIR bomber", coord[0], coord[1], bomber2Images, animation_delay, 1, 35, 2500000));
            }
            if(!players[i].getName().startsWith("TRAIN"))
            {    
               if(coord[0]==0)
               {
                  players[i].setBodyDirection(DOWN);
               }
               else if(coord[0]==board.length-1)
               {
                  players[i].setBodyDirection(UP);
               }
               else if(coord[1]==0)
               {
                  players[i].setBodyDirection(RIGHT);
               }
               else if(coord[1]==board.length-1)
               {
                  players[i].setBodyDirection(LEFT);
               }
               else
               {
                  players[i].setBodyDirection((int)(Math.random()*4));
               }
            }
            players[i].clearDirections();
         //if we spawn in the visible panel, record it (used to repawn players that leave the panel)
            if(coord[0]>0 && coord[1]>0 && coord[0]<board.length-1 && coord[1]<board[0].length-1)
               if(players[i] instanceof Vehicle)
                  ((Vehicle)(players[i])).setOnField(true);
         }
         if(p3toggle && p3temp!=null)
         {
            coord = getRandomP2Spawn();
            int tries = 0;
            while(!p3temp.isSwimmer() && board[coord[0]][coord[1]][panel].equals("~~~") && tries<1000)
            {
               coord = getRandomP2Spawn();
               tries++;
            }
            p3temp.setRow(coord[0]);
            p3temp.setCol(coord[1]);
         }
         if(blopSplit && p4temp!=null)
         {
            coord = getRandomP2Spawn();
            p4temp.setRow(coord[0]);
            p4temp.setCol(coord[1]);
         }
      }
   
   //pre: i is a valid index of the players array
   //post:finds a respawn location on the map that is a border row or an empty lot for player at index i
      public static void resetEnemy(int i)
      {
         if(i<1 || i>=players.length || (i==PLAYER2 && !p2toggle) || (i==AIPLAYER) || (i==BLOPSPLIT))		//index 0 is the human player, perhaps index 1 is as well
            return;
         if(gameMode == EARTH_INVADERS)
         {
            String name = players[i].getName();
            if(name.startsWith("TANK") || name.endsWith("jeep"))
               EI_vehicles--;
            players[i]=new Vehicle();  
            if(name.startsWith("AIR"))	//if we shot down an airplane, lets spawn a new one
            {									//(ground vehicles don't respawn in EARTH INVADERS until all are cleared)
               int airType = (int)(Math.random()*4);
               int startPoint = (int)(Math.random()*2);
               int []coord = new int[2];
               if(startPoint==0)
               {
                  coord[0]=board.length-1;
                  coord[1]=0;
               }
               else
               {
                  coord[0]=board.length-1;
                  coord[1]=board[0].length-1;
               }
            //the last index is where the aircraft spawn
               int airIndex = players.length-1;
               if(airType == 0)
                  players[airIndex] = (new Vehicle("AIR newscopter",  coord[0], coord[1], heliImages, animation_delay/2, 0, 0, 120000));
               else
                  if(airType == 1)
                     players[airIndex] = (new Vehicle("AIR civilian", coord[0], coord[1], cessnaImages, animation_delay/2, 0, 0, 80000));
                  else
                     if(airType == 2)
                        players[airIndex] = (new Vehicle("AIR fighter", coord[0], coord[1], fighterImages, animation_delay, 0, 35, 1500000));
                     else
                        players[airIndex] = (new Vehicle("AIR bomber", coord[0], coord[1], bomber2Images, animation_delay, 1, 35, 2500000));
            }  
            return;
         }//***end EARTH INVADERS
         ArrayList<String> units = Spawner.findAvailableUnits();
         int randUnit = 0;
         randUnit = (int)(Math.random()*units.size());
       
         if(i==1 && p2toggle)		//there is a player 2
         {
            units = Spawner.findAvailableP2Units();
            randUnit = (int)(Math.random()*units.size());
            String unit = units.get(randUnit);
            int rand = 0;
            int[] coord;
            if(unit.equals("CROWD troops"))
            {
               if(p2CarSpawnPoints[panel].size()>0)
               {
                  rand = (int)(Math.random() * p2CarSpawnPoints[panel].size());
                  coord = p2CarSpawnPoints[panel].get(rand);
               }
               else		//backup - no spawns, so pick a random loc
               {
                  coord = Spawner.getRandomP2Spawn();
                  int tries = 0;
                  while(board[coord[0]][coord[1]][panel].equals("~~~") && tries<1000)
                  {
                     coord = Spawner.getRandomP2Spawn();
                     tries++;
                  }
               }
               players[PLAYER2] = (new Vehicle("CROWD troops",  coord[0], coord[1], troopImages, animation_delay*2, 4, 25,0)); 
            }
            else
               if(unit.equals("TANK tank"))
               {
                  if(p2TankSpawnPoints[panel].size()>0)
                  {
                     rand = (int)(Math.random() * p2TankSpawnPoints[panel].size());
                     coord = p2TankSpawnPoints[panel].get(rand);
                  }
                  else		//backup - no spawns, so pick a random loc
                  {
                     coord = Spawner.getRandomP2Spawn();
                     int tries = 0;
                     while(board[coord[0]][coord[1]][panel].equals("~~~") && tries<1000)
                     {
                        coord = Spawner.getRandomP2Spawn();
                        tries++;
                     }
                  }
                  players[PLAYER2] = (new Vehicle("TANK tank", coord[0], coord[1],tankImages, animation_delay, 3, 100, 100000)); 
               } 
               else
                  if(unit.equals("TANK flame"))
                  {
                     if(p2TankSpawnPoints[panel].size()>0)
                     {
                        rand = (int)(Math.random() * p2TankSpawnPoints[panel].size());
                        coord = p2TankSpawnPoints[panel].get(rand);
                     }
                     else		//backup - no spawns, so pick a random loc
                     {
                        coord = Spawner.getRandomP2Spawn();
                        int tries = 0;
                        while(board[coord[0]][coord[1]][panel].equals("~~~") && tries<1000)
                        {
                           coord = Spawner.getRandomP2Spawn();
                           tries++;
                        }
                     }
                     players[PLAYER2] = (new Vehicle("TANK flame", coord[0], coord[1],flameTankImages, animation_delay, 2, 100, 150000)); 
                  } 
                  else
                     if(unit.equals("TANK missile"))
                     {
                        if(p2CarSpawnPoints[panel].size()>0)
                        {
                           rand = (int)(Math.random() * p2CarSpawnPoints[panel].size());
                           coord = p2CarSpawnPoints[panel].get(rand);
                        }
                        else		//backup - no spawns, so pick a random loc
                        {
                           coord = Spawner.getRandomP2Spawn();
                           int tries = 0;
                           while(board[coord[0]][coord[1]][panel].equals("~~~") && tries<1000)
                           {
                              coord = Spawner.getRandomP2Spawn();
                              tries++;
                           }
                        }
                        players[PLAYER2] = (new Vehicle("TANK missile", coord[0], coord[1],missileLauncherImages, animation_delay, 3, 200, 200000)); 
                     } 
                     else
                        if(unit.equals("TANK artillery"))
                        {
                           if(p2CarSpawnPoints[panel].size()>0)
                           {
                              rand = (int)(Math.random() * p2CarSpawnPoints[panel].size());
                              coord = p2CarSpawnPoints[panel].get(rand);
                           }
                           else		//backup - no spawns, so pick a random loc
                           {
                              coord = Spawner.getRandomP2Spawn();
                              int tries = 0;
                              while(board[coord[0]][coord[1]][panel].equals("~~~") && tries<1000)
                              {
                                 coord = Spawner.getRandomP2Spawn();
                                 tries++;
                              }
                           }
                           players[PLAYER2] = (new Vehicle("TANK artillery", coord[0], coord[1],artilleryImages, animation_delay, 3, 100, 80000)); 
                        } 
                        else
                           if(unit.equals("CAR jeep"))
                           {
                              if(p2CarSpawnPoints[panel].size()>0)
                              {
                                 rand = (int)(Math.random() * p2CarSpawnPoints[panel].size());
                                 coord = p2CarSpawnPoints[panel].get(rand);
                              }
                              else		//backup - no spawns, so pick a random loc
                              {
                                 coord = Spawner.getRandomP2Spawn();
                                 int tries = 0;
                                 while(board[coord[0]][coord[1]][panel].equals("~~~") && tries<1000)
                                 {
                                    coord = Spawner.getRandomP2Spawn();
                                    tries++;
                                 }
                              }
                              players[PLAYER2] = (new Vehicle("CAR jeep", coord[0], coord[1],jeepImages, animation_delay, 2, 50, 35000)); 
                           } 
                           else
                              if(unit.equals("CYCLE police"))
                              {
                                 if(p2CarSpawnPoints[panel].size()>0)
                                 {
                                    rand = (int)(Math.random() * p2CarSpawnPoints[panel].size());
                                    coord = p2CarSpawnPoints[panel].get(rand);
                                 }
                                 else		//backup - no spawns, so pick a random loc
                                 {
                                    coord = Spawner.getRandomP2Spawn();
                                    int tries = 0;
                                    while(board[coord[0]][coord[1]][panel].equals("~~~") && tries<1000)
                                    {
                                       coord = Spawner.getRandomP2Spawn();
                                       tries++;
                                    }
                                 }
                                 players[PLAYER2] = (new Vehicle("CYCLE police", coord[0], coord[1],policeMotorcycleImages, animation_delay*2, 1, 50, 8000)); 
                              }
                              else
                              {
                                 if(p2CarSpawnPoints[panel].size()>0)
                                 {
                                    rand = (int)(Math.random() * p2CarSpawnPoints[panel].size());
                                    coord = p2CarSpawnPoints[panel].get(rand);
                                 }
                                 else		//backup - no spawns, so pick a random loc
                                 {
                                    coord = Spawner.getRandomP2Spawn();
                                    int tries = 0;
                                    while(board[coord[0]][coord[1]][panel].equals("~~~") && tries<1000)
                                    {
                                       coord = Spawner.getRandomP2Spawn();
                                       tries++;
                                    }
                                 }
                                 players[PLAYER2] = (new Vehicle("CAR police", coord[0], coord[1],policeImages, animation_delay*2, 2, 75, 25000)); 
                              }
         
            players[PLAYER2].findX(cellSize);
            players[PLAYER2].findY(cellSize);
            explosions.add(new Explosion("SMALL", players[PLAYER2].getX()-(cellSize/2), players[PLAYER2].getY()-(cellSize/2), puffImages, animation_delay));
            return;
         }
      
         boolean isWaterInPanel = (boatSpawnPoints[panel].size()>0);  
         boolean areTracksInPanel = (trainSpawnPoints[panel].size()>0);  
         String dayVersion = "";
         if(!day)
            dayVersion = "NIGHT";
      
         if(randUnit >= units.size())
            randUnit = 0;      
         String unit = units.get(randUnit);	
         double rnd = Math.random();
         int fireInPanel = countStructureFires();
         boolean firetruck = firetruckPresent();
         if(fireInPanel > 0 && !firetruck && rnd < .25)
            players[i] = (new Vehicle("CAR firetruck", 0 , 0, firetruckImages, animation_delay*2, 2, 0, 120000));
         else if(unit.equals("CAR civilian"))
         {
            rnd = Math.random();
            if(fireInPanel > 0)
            {
               if(rnd < .25 + (fireInPanel/100.0))
                  players[i] = (new Vehicle("CAR firetruck", 0 , 0, firetruckImages, animation_delay*2, 2, 0, 120000));
               else
               {
                  rnd = Math.random();
                  if(rnd < .5)
                     players[i] = (new Vehicle("CAR civilian",0 , 0, carImages, animation_delay, 3, 0, (int)carValue()));
                  else
                     players[i] = (new Vehicle("CAR bus", 0 , 0, busImages, animation_delay, 3, 0, 80000));
               }
            }
            else
            { 
               if(rnd < .66)
                  players[i] = (new Vehicle("CAR civilian",0, 0, carImages, animation_delay, 3, 0, (int)carValue()));
               else if(rnd < .95)
                  players[i] = (new Vehicle("CAR bus", 0, 0, busImages, animation_delay, 3, 0, 80000));
               else
                  players[i] = (new Vehicle("CAR firetruck", 0, 0, firetruckImages, animation_delay*2, 2, 0, 120000));
            }
         }	
         else if(unit.equals("CROWD civilian"))
            players[i] = (new Vehicle("CROWD civilian", 0, 0, crowdImages, animation_delay*2, 5, 0, 0)); 
         else if(unit.equals("CROWD troops"))
            players[i] = (new Vehicle("CROWD troops", 0, 0, troopImages, animation_delay*2, 4, 25, 0)); 
         else if(unit.equals("SPECIAL civilian"))
         {
            if(areTracksInPanel && !trainInPanel[panel] && trainSpawnPoints[panel].size() > 0)	//place a train if there are tracks here
            {
               trainInPanel[panel] = true;
               int rand = (int)(Math.random() *trainSpawnPoints[panel].size());
               int []coord =trainSpawnPoints[panel].get(rand);
               int r = coord[0];
               int c = coord[1];
                  
               players[TRAIN1] = (new Vehicle("TRAIN engine", r, c, trainImages[0], animation_delay, 2, 0, 120000));
                     
               int engineIndex = i;
               if(board[r][c][panel].indexOf("U") >= 0)	//we are on a vertical track
               {														//we want trains to follow the right wall to complete their track circuit
                  if(c==0)
                  {
                     players[TRAIN1].setBodyDirection(UP);
                     players[TRAIN1].clearDirections();
                     if(r+2<board.length)
                     {
                        players[TRAIN2] = (new Vehicle("TRAIN car", r+1, c, trainImages[1], animation_delay, 2, 0, 120000));
                        players[TRAIN2].setBodyDirection(UP);
                        players[TRAIN2].clearDirections();
                        players[TRAIN3] = (new Vehicle("TRAIN caboose", r+2, c, trainImages[2], animation_delay, 2, 0, 120000));
                        players[TRAIN3].setBodyDirection(UP);
                        players[TRAIN3].clearDirections();
                     }
                  }
                  else
                     if(c == board[0].length-1)
                     {
                        players[TRAIN1].setBodyDirection(DOWN);
                        players[TRAIN1].clearDirections();
                        if(r-2>=0)
                        {
                           players[TRAIN2] = (new Vehicle("TRAIN car", r-1, c, trainImages[1], animation_delay, 2, 0, 120000));
                           players[TRAIN2].setBodyDirection(DOWN);
                           players[TRAIN2].clearDirections();
                           players[TRAIN3] = (new Vehicle("TRAIN caboose", r-2, c, trainImages[2], animation_delay, 2, 0, 120000));
                           players[TRAIN3].setBodyDirection(DOWN);
                           players[TRAIN3].clearDirections();
                        }
                                 
                     }
                     else
                     {
                        if(Math.random() < .5)
                           players[TRAIN1].setBodyDirection(DOWN);
                        else
                           players[TRAIN1].setBodyDirection(UP);
                     }
               }
               else
               {
                  if(r==0)
                  {
                     players[TRAIN1].setBodyDirection(RIGHT);
                     players[TRAIN1].clearDirections();
                     if(c-2>=0)
                     {
                        players[TRAIN2] = (new Vehicle("TRAIN car", r, c-1, trainImages[1], animation_delay, 2, 0, 120000));
                        players[TRAIN2].setBodyDirection(RIGHT);
                        players[TRAIN2].clearDirections();
                        players[TRAIN3] = (new Vehicle("TRAIN caboose", r, c-2, trainImages[2], animation_delay, 2, 0, 120000));
                        players[TRAIN3].setBodyDirection(RIGHT);
                        players[TRAIN3].clearDirections();
                     }
                  }
                  else
                     if(r == board.length-1)
                     {
                        players[TRAIN1].setBodyDirection(LEFT);
                        players[TRAIN1].clearDirections();
                        if(c+2 <board[0].length)
                        {
                           players[TRAIN2] = (new Vehicle("TRAIN car", r, c+1, trainImages[1], animation_delay, 2, 0, 120000));
                           players[TRAIN2].setBodyDirection(LEFT);
                           players[TRAIN2].clearDirections();
                           players[TRAIN3] = (new Vehicle("TRAIN caboose", r, c+2, trainImages[2], animation_delay, 2, 0, 120000));
                           players[TRAIN3].setBodyDirection(LEFT);
                           players[TRAIN3].clearDirections();
                        }
                     }
                     else
                     {
                                 
                        if(Math.random() < .5)
                           players[TRAIN1].setBodyDirection(LEFT);
                        else
                           players[TRAIN1].setBodyDirection(RIGHT);
                     }
               }
               return;
            } 
            else if(isWaterInPanel)
            {
               if(Math.random() < .5)
               {
                  String[][][]temp=new String[1][1][1];
                  if(Math.random() < .5)  
                  {
                     temp[0][0][0] = "images/vehicles/boat/boat" + dayVersion + ".GIF";
                     players[i] = (new Vehicle("BOAT commerce", 0, 0, temp, animation_delay, 5, 0, 90000));
                  }
                  else
                  {
                     temp[0][0][0] = "images/vehicles/boat/sailboat" + dayVersion + ".GIF";
                     players[i] = (new Vehicle("BOAT civilian", 0, 0, temp, animation_delay, 5, 0, 5000));
                  }
               }
               else
               {
                  int randVehicle = (int)(Math.random()*3);
                  if(randVehicle==0)
                     players[i] = (new Vehicle("AIR newscopter", 0, 0, heliImages, animation_delay/2, 0, 0, 120000));
                  else
                     if(randVehicle==1)
                        players[i] = (new Vehicle("AIR civilian", 0, 0, cessnaImages, animation_delay/2, 0, 0, 80000));
                     else
                        players[i] = (new Vehicle("CYCLE civilian", 0, 0, motorcycleImages, animation_delay, 1, 0, 5000));
               }
            }
            else
            {
               int randVehicle = (int)(Math.random()*3);
               if(randVehicle==0)
                  players[i] = (new Vehicle("AIR newscopter", 0, 0, heliImages, animation_delay/2, 0, 0, 120000));
               else if(randVehicle==1)
                  players[i] = (new Vehicle("AIR civilian", 0, 0, cessnaImages, animation_delay/2, 0, 0, 80000));
               else
                  players[i] = (new Vehicle("CYCLE civilian", 0, 0, motorcycleImages, animation_delay, 1, 0, 5000));
            }
         }
         else if(unit.equals("CAR police"))
            players[i] = (new Vehicle("CAR police", 0, 0, policeImages, animation_delay*2, 2, 75, 25000));
         else if(unit.equals("CAR jeep"))
            players[i] = (new Vehicle("CAR jeep", 0, 0,jeepImages, animation_delay, 2, 50, 35000));
         else if(unit.equals("TANK tank"))
            players[i] = (new Vehicle("TANK tank", 0, 0,tankImages, animation_delay, 3, 100, 100000));
         else if(unit.equals("TANK flame"))
            players[i] = (new Vehicle("TANK flame", 0, 0,flameTankImages, animation_delay, 2, 100, 150000));
         else if(unit.equals("TANK missile"))
            players[i] = (new Vehicle("TANK missile", 0, 0,missileLauncherImages, animation_delay, 3, 200, 200000));
         else if(unit.equals("TANK artillery"))
            players[i] = (new Vehicle("TANK artillery", 0, 0,artilleryImages, animation_delay, 3, 100, 80000));
         else if(unit.equals("BOAT coastguard") && isWaterInPanel)
         {
            String[][][]temp=new String[1][1][1];
            temp[0][0][0] = "images/vehicles/boat/coastguard" + dayVersion + ".GIF";
            players[i] = (new Vehicle("BOAT coastguard", 0, 0, temp, animation_delay, 5, 50, 180000));
         }
         else if(unit.equals("BOAT destroyer") && isWaterInPanel)
         {
            String[][][]temp=new String[1][1][1];
            temp[0][0][0] = "images/vehicles/boat/destroyer" + dayVersion + ".GIF";
            players[i] = (new Vehicle("BOAT destroyer", 0, 0, temp, animation_delay, 5, 100, 2500000));
         }
         else if(unit.equals("AIR fighter"))
            players[i] = (new Vehicle("AIR fighter", 0, 0, fighterImages, animation_delay, 0, 35, 1500000));
         else if(unit.equals("AIR bomber"))
            players[i] = (new Vehicle("AIR bomber", 0, 0, bomber2Images, animation_delay, 1, 35, 2500000));
         else if(unit.equals("AIR nukebomber"))
         {
            players[i] = (new Vehicle("AIR nukebomber", 0, 0, bomberImages, animation_delay, 1, 0, 10000000));
            message = "A-BOMB WARNING!";
            messageTime = numFrames;
            warningTime = numFrames;
         }
      
         int rand=0;	
         int[] coord;
      
         String name = players[i].getName();
         if(name.startsWith("CROWD") && humanSpawnPoints[panel].size() > 0)	//since spawns can be removed (by buildings being destroyed), we need to make sure that there are some
         {
            rand = (int)(Math.random() * humanSpawnPoints[panel].size());
            coord = humanSpawnPoints[panel].get(rand);
         }
         else 
            if(name.startsWith("BOAT"))	//a boat can be destroyed and try to respawn as we change panels, so if so, make it a car or bus instead
            {
               if( boatSpawnPoints[panel].size() > 0)
               {
                  rand = (int)(Math.random() * boatSpawnPoints[panel].size());
                  coord = boatSpawnPoints[panel].get(rand);
               }
               else
               {
                  double r = Math.random();
                  if(fireInPanel > 0)
                  {
                     if(r < .25 + (fireInPanel/100.0))
                        players[i] = (new Vehicle("CAR firetruck", 0 , 0, firetruckImages, animation_delay*2, 2, 0, 120000));
                     else
                     {
                        r = Math.random();
                        if(r < .5)
                           players[i] = (new Vehicle("CAR civilian",0 , 0, carImages, animation_delay, 3, 0, (int)carValue()));
                        else
                           players[i] = (new Vehicle("CAR bus", 0 , 0, busImages, animation_delay, 3, 0, 80000));
                     }
                  }
                  else
                  { 
                     if(r < .66)
                        players[i] = (new Vehicle("CAR civilian",0, 0, carImages, animation_delay, 3, 0, (int)carValue()));
                     else if(r < .95)
                        players[i] = (new Vehicle("CAR bus", 0, 0, busImages, animation_delay, 3, 0, 80000));
                     else
                        players[i] = (new Vehicle("CAR firetruck", 0, 0, firetruckImages, animation_delay*2, 2, 0, 120000));
                  }
                  if(vehicleSpawnPoints[panel].size() > 0)
                  {   
                     rand = (int)(Math.random() * vehicleSpawnPoints[panel].size());
                     coord =  vehicleSpawnPoints[panel].get(rand);
                  }
                  else		//backup - no spawns, so pick a random loc
                  {
                     coord = Spawner.getRandomSpawn();
                  }
               }
            }
            else 
               if(name.startsWith("TRAIN"))	//a train can be destroyed and try to respawn as we change panels, so if so, make it a car or bus instead
               {
                  if(trainSpawnPoints[panel].size() > 0)
                  {
                     rand = (int)(Math.random() *trainSpawnPoints[panel].size());
                     coord =trainSpawnPoints[panel].get(rand);
                  }
                  else
                  {
                     double r = Math.random();
                     if(fireInPanel > 0)
                     {
                        if(r < .25 + (fireInPanel/100.0))
                           players[i] = (new Vehicle("CAR firetruck", 0 , 0, firetruckImages, animation_delay*2, 2, 0, 120000));
                        else
                        {
                           r = Math.random();
                           if(r < .5)
                              players[i] = (new Vehicle("CAR civilian",0 , 0, carImages, animation_delay, 3, 0, (int)carValue()));
                           else
                              players[i] = (new Vehicle("CAR bus", 0 , 0, busImages, animation_delay, 3, 0, 80000));
                        }
                     }
                     else
                     { 
                        if(r < .66)
                           players[i] = (new Vehicle("CAR civilian",0, 0, carImages, animation_delay, 3, 0, (int)carValue()));
                        else if(r < .95)
                           players[i] = (new Vehicle("CAR bus", 0, 0, busImages, animation_delay, 3, 0, 80000));
                        else
                           players[i] = (new Vehicle("CAR firetruck", 0, 0, firetruckImages, animation_delay*2, 2, 0, 120000));
                     }
                     if(vehicleSpawnPoints[panel].size() > 0)
                     {   
                        rand = (int)(Math.random() * vehicleSpawnPoints[panel].size());
                        coord =  vehicleSpawnPoints[panel].get(rand);
                     }
                     else		//backup - no spawns, so pick a random loc
                     {
                        coord = Spawner.getRandomSpawn();
                     }
                  }
               }
               else
                  if(name.startsWith("AIR") && airSpawnPoints[panel].size() > 0)
                  {
                     rand = (int)(Math.random() * airSpawnPoints[panel].size());
                     coord = airSpawnPoints[panel].get(rand);
                  }
                  else 
                     if((name.equals("TANK tank") || name.equals("TANK flame")) && tankSpawnPoints[panel].size()>0)
                     {
                        rand = (int)(Math.random() * tankSpawnPoints[panel].size());
                        coord = tankSpawnPoints[panel].get(rand);
                     }
                     else 
                        if(vehicleSpawnPoints[panel].size() > 0)
                        {
                           rand = (int)(Math.random() * vehicleSpawnPoints[panel].size());
                           coord =  vehicleSpawnPoints[panel].get(rand);
                        }
                        else		//backup - no spawns, so pick a random loc
                        {
                           coord = Spawner.getRandomSpawn();
                        }
         int r = coord[0];						//row & col of where player will spawn
         int c = coord[1];
         players[i].setRow(r);			//reset the enemy's position
         players[i].setCol(c);
         if(players[i].getName().startsWith("TRAIN"))
         {											//we want trains to follow the right wall to complete their track circuit
            String cell = board[r][c][panel];
            if(cell.indexOf("U") >= 0 )	//we are on a vertical track
            {											
               if(c==0)
                  players[i].setBodyDirection(UP);
               else
                  players[i].setBodyDirection(DOWN);
            }
            else									//we are on a horiz track
            {
               if(r==0)
                  players[i].setBodyDirection(RIGHT);
               else
                  players[i].setBodyDirection(LEFT);
            }
         }
         else
            if(r==0)		//top row
            {
               players[i].setBodyDirection(DOWN);
               if(name.endsWith("nukebomber"))	//if it is a bomber, set the coordinates for the nuke to be triggered
               {
                  players[i].setDetRow(board.length-1);
                  players[i].setDetCol(c);
                  players[i].setDetDir(DOWN);
               }
               else
                  if(name.endsWith("bomber"))
                  {
                     int monsterIndex = PLAYER1;
                     if(p1partner && players[i]!=null && !players[i].getName().equals("NONE") && Math.random() < .5)  
                        monsterIndex = PLAYER2;
                     int bCol = players[monsterIndex].getCol();
                     if(Math.random() < .5)		//set col to one of the two monsters (maybe one off to the left or right)
                     {
                        if(bCol > 1 && Math.random() < .5)
                           bCol--;
                        else
                           if(bCol < board[0].length -1 && Math.random() < .5)
                              bCol++;
                     }   
                     players[i].setCol(bCol);	
                  }
            }
            else
               if(r==board.length-1)
               {
                  players[i].setBodyDirection(UP);
                  if(name.endsWith("nukebomber"))
                  {
                     players[i].setDetRow(0);
                     players[i].setDetCol(c);
                     players[i].setDetDir(UP);
                  }
                  else
                     if(name.endsWith("bomber"))
                     {
                        int monsterIndex = PLAYER1;
                        if(p1partner && players[i]!=null && !players[i].getName().equals("NONE") && Math.random() < .5)  
                           monsterIndex = PLAYER2;
                        int bCol = players[monsterIndex].getCol();
                        if(Math.random() < .5)		//set col to one of the two monsters (maybe one off to the left or right)
                        {
                           if(bCol > 1 && Math.random() < .5)
                              bCol--;
                           else
                              if(bCol < board[0].length -1 && Math.random() < .5)
                                 bCol++;
                        }   
                        players[i].setCol(bCol);	
                     }
               }
               else
                  if(c==0)
                  {
                     players[i].setBodyDirection(RIGHT);
                     if(name.endsWith("nukebomber"))
                     {
                        players[i].setDetRow(r);
                        players[i].setDetCol(board[0].length-1);
                        players[i].setDetDir(RIGHT);
                     }
                     else
                        if(name.endsWith("bomber"))
                        {
                           int monsterIndex = PLAYER1;
                           if(p1partner && players[i]!=null && !players[i].getName().equals("NONE") && Math.random() < .5)  
                              monsterIndex = PLAYER2;
                           int bRow = players[monsterIndex].getRow();
                           if(Math.random() < .5)		//set col to one of the two monsters (maybe one off to the left or right)
                           {
                              if(bRow > 1 && Math.random() < .5)
                                 bRow--;
                              else
                                 if(bRow < board.length -1 && Math.random() < .5)
                                    bRow++;
                           }   
                           players[i].setRow(bRow);	
                        }
                  }
                  else
                     if(c==board.length-1)
                     {
                        players[i].setBodyDirection(LEFT);
                        if(name.endsWith("nukebomber"))
                        {
                           players[i].setDetRow(r);
                           players[i].setDetCol(0);
                           players[i].setDetDir(LEFT);
                        }
                        else
                           if(name.endsWith("bomber"))
                           {
                              int monsterIndex = PLAYER1;
                              if(p1partner && players[i]!=null && !players[i].getName().equals("NONE") && Math.random() < .5)  
                                 monsterIndex = PLAYER2;
                              int bRow = players[monsterIndex].getRow();
                              if(Math.random() < .5)		//set col to one of the two monsters (maybe one off to the left or right)
                              {
                                 if(bRow > 1 && Math.random() < .5)
                                    bRow--;
                                 else
                                    if(bRow < board.length -1 && Math.random() < .5)
                                       bRow++;
                              }   
                              players[i].setRow(bRow);	
                           }
                     }
                     else
                     {
                        players[i].setBodyDirection((int)(Math.random()*4));
                     }
         players[i].clearDirections();
        //if we spawn in the visible panel, record it (used to repawn players that leave the panel)
         if(players[i] instanceof Vehicle)
         {
            if(r>0 && c>0 && r<board.length-1 && c<board[0].length-1)
               ((Vehicle)(players[i])).setOnField(true);
            else
               ((Vehicle)(players[i])).setOnField(false);
         }
      }
   
   //post: counts the # of structures on fire for spawning firetrucks
      public static int countStructureFires()
      {
         int countFire = 0;
         for(int r=0;r<structures.length;r++)
         {
            for(int c=0;c<structures[0].length;c++)
            {
               Structure str = structures[r][c][panel];
               if(str!=null && str.onFire())
                  countFire++;
            }
         }
         return countFire;
      }
   
   //post:  returns true if there is an active firetruck on the map
      public static boolean firetruckPresent()
      {
         for(int i=0; i<players.length; i++)
         {	//burn players that are standing in the fire
            if(players[i]!=null && players[i].getName().indexOf("firetruck")>=0)
               return true;
         }
         return false;
      }
   }