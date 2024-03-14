//Mash, Mangle & Munch - Rev. Dr. Douglas R Oberle, June 2012  doug.oberle@fcps.edu
// AI UNIT UTILITIES
   import java.util.ArrayList;
   import java.awt.event.KeyEvent;

   public class AImovement extends MMMPanel
   {
      public static final boolean DISABLE_BOMBERS = false;	//for testing BOMBER_DODGER mode
   
   //pre:  curr != null
   //post: returns the direction in which the monster is if it is in unobstructed sight from curr, -1 if not in sight
   //      as well as the index of where the monster is in the players array (used to target)
      public static int[] isMonsterInSight(Player curr)
      {
         int[] ans = {-1,-1};
         ArrayList<Integer> dirs = new ArrayList();
         int bodyDir = curr.getBodyDirection();
         String name = curr.getName();
         if(bodyDir==UP)
         {
            dirs.add(UP);	
            if(!name.endsWith("fighter"))		//figher planes can only see what is in front of them
            {
               if(!name.equals("AIR bomber"))//bombers only see what is in front and behind them for their bombing run
               {
                  dirs.add(RIGHT); 
                  dirs.add(LEFT);
               }
               if(name.startsWith("TANK") || name.endsWith("jeep") || name.endsWith("destroyer") || name.endsWith("coastguard") || name.equals("AIR bomber"))
                  dirs.add(DOWN);
            }
         } 
         else if(bodyDir==RIGHT)
         {
            dirs.add(RIGHT); 
            if(!name.endsWith("fighter"))		//figher planes can only see what is in front of them
            {
               if(!name.equals("AIR bomber"))//bombers only see what is in front and behind them for their bombing run
               {
                  dirs.add(UP);	
                  dirs.add(DOWN);
               }
               if(name.startsWith("TANK") || name.endsWith("jeep") || name.endsWith("destroyer") || name.endsWith("coastguard")|| name.equals("AIR bomber"))
                  dirs.add(LEFT);
            }
         } 
         else if(bodyDir==DOWN)
         {
            dirs.add(DOWN);	
            if(!name.endsWith("fighter"))		//figher planes can only see what is in front of them
            {
               if(!name.equals("AIR bomber"))//bombers only see what is in front and behind them for their bombing run
               {
                  dirs.add(RIGHT); 
                  dirs.add(LEFT);
               }
               if(name.startsWith("TANK") || name.endsWith("jeep") || name.endsWith("destroyer") || name.endsWith("coastguard")|| name.equals("AIR bomber"))
                  dirs.add(UP);
            }
         }
         else //if(curr.getBodyDirection()==LEFT)
         {
            dirs.add(LEFT);
            if(!name.endsWith("fighter"))		//figher planes can only see what is in front of them
            {
               if(!name.equals("AIR bomber"))//bombers only see what is in front and behind them for their bombing run
               {
                  dirs.add(UP);	
                  dirs.add(DOWN); 
               }
               if(name.startsWith("TANK") || name.endsWith("jeep") || name.endsWith("destroyer") || name.endsWith("coastguard")|| name.equals("AIR bomber"))
                  dirs.add(RIGHT);
            }
         }
      
         if(dirs == null || dirs.size()==0)  
            return ans;	//{-1,-1}
         for(int m=0; m<players.length; m++)
         {  //skip player if Vehicle is not mind controlled and the player is not a monster
         //skip player if Vehicle is mind controlled and the player is a monster
            if(players[m]==null || players[m].getName().equals("NONE") || curr==players[m] ||
            ((curr instanceof Vehicle && !((Vehicle)(curr)).isMindControlled()) && !(players[m] instanceof Monster)) ||
            ((curr instanceof Vehicle && ((Vehicle)(curr)).isMindControlled()) && (players[m] instanceof Monster)) 
            || players[m].isDigging())       //you can not see monsters that are underground
               continue;   
            int mR = players[m].getRow();    
            int mC = players[m].getCol();
         
            for(int i=0; i<dirs.size(); i++)
            {
               int dir = dirs.get(i);
               boolean skip = false;
               if(dir==UP)
               {
                  for(int r=curr.getRow(); r>=0; r--)
                  {	//coastguard, destroyer, fighter planes and artillery have the monsters position known - the monster is not hidden by structures
                     if(!MapBuilder.noStructure(r,curr.getCol(), panel) && !name.endsWith("coastguard") && !name.endsWith("destroyer") && !name.endsWith("fighter") && !name.endsWith("artillery") && !name.endsWith("missile") && !name.equals("AIR bomber") && !players[m].isFlying())
                     {
                        skip = true;
                        break;
                     }
                     if(r==mR && curr.getCol()==mC)
                     {
                        ans[0] = dir;
                        ans[1] = m;
                        return ans;
                     }
                  }
               }
               else
                  if(dir==DOWN)
                  {
                     for(int r=curr.getRow(); r<board.length-1; r++)
                     {
                        if(!MapBuilder.noStructure(r,curr.getCol(), panel) && !name.endsWith("coastguard") && !name.endsWith("destroyer")  && !name.endsWith("fighter") && !name.endsWith("artillery") && !name.endsWith("missile") && !name.equals("AIR bomber") && !players[m].isFlying())
                        {
                           skip = true;
                           break;
                        }
                        if(r==mR && curr.getCol()==mC)
                        {
                           ans[0] = dir;
                           ans[1] = m;
                           return ans;
                        }
                     
                     }
                  }
                  else
                     if(dir==RIGHT)
                     {
                        for(int c=curr.getCol(); c<board[0].length; c++)
                        {
                           if(!MapBuilder.noStructure(curr.getRow(),c, panel) && !name.endsWith("coastguard") && !name.endsWith("destroyer") && !name.endsWith("fighter") && !name.endsWith("artillery") && !name.endsWith("missile") && !name.equals("AIR bomber") && !players[m].isFlying())
                           {
                              skip = true;
                              break;
                           }
                           if(curr.getRow()==mR && c==mC)
                           {
                              ans[0] = dir;
                              ans[1] = m;
                              return ans;
                           }
                        }
                     }
                     else
                        if(dir==LEFT)
                        {
                           for(int c=curr.getCol(); c>=0; c--)
                           {
                              if(!MapBuilder.noStructure(curr.getRow(),c, panel) && !name.endsWith("coastguard") && !name.endsWith("destroyer") && !name.endsWith("fighter") && !name.endsWith("artillery") && !name.endsWith("missile") && !name.equals("AIR bomber") && !players[m].isFlying())
                              {
                                 skip = true;
                                 break;
                              }
                              if(curr.getRow()==mR && c==mC)
                              {
                                 ans[0] = dir;
                                 ans[1] = m;
                                 return ans;
                              }
                           }
                        }
               if(skip)
                  continue;
            }
         }
         return ans;
      }
   
   //pre:  curr != null
   //post: returns the direction in which the vehicle is if it is in unobstructed sight from curr, -1 if not in sight
   //      returns the direction+10 if it should be an airshot (shoot at plane)
      public static int isVehicleInSight(Player curr)
      {
         ArrayList<Integer> dirs = new ArrayList();
         int bodyDir = curr.getBodyDirection();
         if(bodyDir==UP)
         {
            dirs.add(UP);	
            dirs.add(RIGHT); 
            dirs.add(LEFT); 
            if(curr.head360())
               dirs.add(DOWN);
         } 
         else if(bodyDir==RIGHT)
         {
            dirs.add(RIGHT); 
            dirs.add(UP); 
            dirs.add(DOWN); 
            if(curr.head360())
               dirs.add(LEFT);
         } 
         else if(bodyDir==DOWN)
         {
            dirs.add(DOWN);
            dirs.add(RIGHT); 
            dirs.add(LEFT); 	
            if(curr.head360())
               dirs.add(UP);
         }
         else //if(curr.getBodyDirection()==LEFT)
         {
            dirs.add(LEFT);
            dirs.add(UP); 
            dirs.add(DOWN); 
            if(curr.head360())
               dirs.add(RIGHT);
         }
      
         if(dirs == null || dirs.size()==0)  
            return -1;
         for(int vi=0; vi<players.length; vi++)
         {
            if(players[vi]==null || players[vi].getName().equals("NONE") || (players[vi] instanceof Monster))		//we don't want the AI monster to consider itself or other monsters a target
               continue;
            Player veh = players[vi];
            boolean isAir = veh.isFlying();
            int vR = veh.getRow();
            int vC = veh.getCol();
            if(veh.getName().endsWith("civilian"))
               continue;
            for(int i=0; i<dirs.size(); i++)
            {
               int dir = dirs.get(i);
               boolean skip = false;
               if(dir==UP)
               {
                  for(int r=curr.getRow(); r>=1; r--)
                  {	
                     if(isAir && r==vR && curr.getCol()==vC)
                        return dir+10;
                     if(r==vR && curr.getCol()==vC)
                        return dir;
                  }
               }
               else
                  if(dir==DOWN)
                  {
                     for(int r=curr.getRow(); r<board.length-1; r++)
                     {
                        if(isAir && r==vR && curr.getCol()==vC)
                           return dir+10;
                        if(r==vR && curr.getCol()==vC)
                           return dir;
                     }
                  }
                  else
                     if(dir==RIGHT)
                     {
                        for(int c=curr.getCol(); c<board[0].length-1; c++)
                        {
                           if(isAir && c==vC && curr.getRow()==vR)
                              return dir+10;
                           if(curr.getRow()==vR && c==vC)
                              return dir;
                        }
                     }
                     else
                        if(dir==LEFT)
                        {
                           for(int c=curr.getCol(); c>=1; c--)
                           {
                              if(isAir && c==vC && curr.getRow()==vR)
                                 return dir+10;
                              if(curr.getRow()==vR && c==vC)
                                 return dir;
                           }
                        }
               if(skip)
                  continue;
            }
         }
         return -1;
      }
   
   //pre: curr!=null, r, c are valid coordinates of the board  
   //post:  returs true if r & c are valid coordinates for the enemy player to move into
      public static boolean isValidMove(Player curr, int r, int c)
      {
         if(r < 0 || c < 0 || r >= board.length || c >= board[0].length)
            return false;							//nobody can escape the dimensions of the board
         if(curr==null)
            return false;   
         if(curr.isFlying())			//aircraft can fly over anything
            return true;
         if(curr.getName().startsWith("TRAIN")) 		//trains can only run on tracks
         {
            if(board[r][c][panel].startsWith("T"))		//train tracks
               return true;
            return false;
         }
      
         if(board[r][c][panel].startsWith("#"))		//nobody can cross electric lines
            return false;
         if(Utilities.streetTraveler(curr))	//curr can only travel on streets
         {  	  //street or crossroad	or empty lot or bridge or rr x-ing AND	no structure
            if(MapBuilder.isStreet(r,c,panel))
            {	//don't try to move into the monster
               if(r==players[PLAYER1].getRow() && c==players[PLAYER1].getCol())
                  return false;
               return true;
            }
            return false;
         }
         else
            if(curr.getName().startsWith("CROWD"))
            {  	  //street, crossroad, empty lot, bridge, mud, sand or park or rr x-ing AND	no structure
               if(MapBuilder.passableByCrowd(r,c,panel))
               {	//don't try to move into the monster
                  if(r==players[PLAYER1].getRow() && c==players[PLAYER1].getCol())
                     return false;
                  return true;
               }
               return false;
            }
            else
               if(curr.getName().endsWith("tank") || curr.getName().endsWith("flame"))
               {  	 //park, street, rubble  or mud or rr x-ing
                  if(MapBuilder.passableByTank(r,c,panel))
                  {	//don't try to move into the monster
                     if(r==players[PLAYER1].getRow() && c==players[PLAYER1].getCol())
                        return false;
                     return true;
                  }
                  return false;
               }
               else
                  if(curr.getName().startsWith("BOAT"))
                  {
                     if(board[r][c][panel].startsWith("~"))
                        return true;
                     return false;
                  }
         return true;
      }
   
   //post: enemies move to one of 4 adjacent places around it or perhaps shoots at the monster
      public static void makeEnemyMove()
      {	//all players after index 1 are enemies
         if(gameMode == EARTH_INVADERS)
         {//find leftmost and rightmost vehicle columns to see if we need to move down
            int leftMost = board[0].length;
            int rightMost= 0;
            int lowest = 0;
            for(int i=FIRST_VEHICLE; i<players.length; i++)
            {//find left-most, right-most and lowest most vehicles (non aircraft/train) to determine how formation should move 
               Player curr = players[i];
               if(curr==null)
                  continue;
               String name = curr.getName();
               if(name.equals("NONE") || curr.isFlying() || name.startsWith("TRAIN") || (curr instanceof Monster))
                  continue;
               if(curr.getCol() < leftMost)
                  leftMost = curr.getCol();
               if(curr.getCol() > rightMost)
                  rightMost = curr.getCol();
               if(curr.getRow() > lowest)
                  lowest = curr.getRow();   
               curr.setHeadDirection(DOWN);
            }
            for(int i=FIRST_VEHICLE; i<players.length; i++)
            {//***move aircraft and trains
               Player curr = players[i];  
               if(curr==null)
                  continue;
               String name = curr.getName();
               if(name.equals("NONE") || curr.isFlying() || name.startsWith("TRAIN") || (curr instanceof Monster))
               {
                  if(curr.isMoving())
                     continue;
                  ArrayList<Integer> dirs = new ArrayList();		//array of preferred directions - everything but the reverse of their body position 
                  int bodyDir = curr.getBodyDirection();
                  curr.clearDirections();
                  int r=curr.getRow();
                  int c=curr.getCol();
                  if(bodyDir==UP)
                  {
                     if(isValidMove(curr, r-1, c))
                        dirs.add(UP);	
                     if(isValidMove(curr, r, c+1))
                        dirs.add(RIGHT); 
                     if(!name.startsWith("TRAIN"))		//trains need to follow the track circuit - only straight and right turns
                     {	
                        if(isValidMove(curr, r, c-1))
                           dirs.add(LEFT);
                     }
                  } 
                  else if(bodyDir==RIGHT)
                  {
                     if(!name.startsWith("TRAIN"))		//trains need to follow the track circuit - only straight and right turns
                     {	
                        if(isValidMove(curr, r-1, c))
                           dirs.add(UP);	
                     }
                     if(isValidMove(curr, r, c+1))
                        dirs.add(RIGHT); 
                     if(isValidMove(curr, r+1, c))
                        dirs.add(DOWN);
                  } 
                  else if(bodyDir==DOWN)
                  {
                     if(isValidMove(curr, r+1, c))
                        dirs.add(DOWN);
                     if(!name.startsWith("TRAIN"))		//trains need to follow the track circuit - only straight and right turns
                     {		
                        if(isValidMove(curr, r, c+1))
                           dirs.add(RIGHT);
                     } 
                     if(isValidMove(curr, r, c-1))
                        dirs.add(LEFT);
                  }
                  else //if(curr.getBodyDirection()==LEFT)
                  {
                     if(isValidMove(curr, r-1, c))
                        dirs.add(UP);
                     if(!name.startsWith("TRAIN"))		//trains need to follow the track circuit - only straight and right turns
                     {	
                        if(isValidMove(curr, r+1, c))
                           dirs.add(DOWN); 
                     }
                     if(isValidMove(curr, r, c-1))
                        dirs.add(LEFT);
                  }
                  int rand = 0;
                  if(dirs.size()>0)
                     rand=dirs.get((int)(Math.random()*dirs.size()));
                  if(curr.isFlying())
                  {	//if aircraft is flying in the visible board, don't change direction			
                     if(r==1 && (c==0 || c==board[0].length-1))	//we are in the first row but behind a border
                     {	//we only want aircraft to appear in row 1 in EARTH INVADERS (like the UFO in space invaders)
                        if(bodyDir==LEFT || bodyDir==RIGHT)
                        {
                           rand = UP;
                           if(Math.random() < .5)
                              rand = DOWN;
                        }
                        else
                           if(c==0)
                              rand=RIGHT;
                           else
                              if(c==board[0].length-1)
                                 rand=LEFT;
                              else
                              //if(dirs.contains(bodyDir))
                                 rand = bodyDir;
                     }
                     else
                        if(r==0 || c==0 || r==board.length-1 || c==board[0].length-1)
                        {
                           rand = bodyDir;
                           if(r==0 && c==0)
                           {
                              rand = RIGHT;
                              if(Math.random()<.5)
                                 rand = DOWN;
                           }
                           else
                              if(r==0 && c==board[0].length-1)
                              {
                                 rand = LEFT;
                                 if(Math.random()<.5)
                                    rand = DOWN;
                              }
                              else
                                 if(r==board.length-1 && c==0)
                                 {
                                    rand = RIGHT;
                                    if(Math.random()<.5)
                                       rand = UP;
                                 }
                                 else
                                    if(r==board.length-1 && c==board[0].length-1)
                                    {
                                       rand = LEFT;
                                       if(Math.random()<.5)
                                          rand = UP;
                                    }
                        }
                        else
                           if(/*dirs.contains(bodyDir) && */(r>0 && c>0 && r<board.length-1 && c<board[0].length-1))  
                              rand = bodyDir;		//make it so aircraft prefer to keep going the same direciton
                  
                  }
                  else
                     if(name.startsWith("TRAIN"))  
                     {//make it so trains prefer to follow the right wall
                        for(int j=0; j<dirs.size(); j++)
                           if(dirs.get(j)!=bodyDir)
                           {
                              rand = dirs.get(j);
                              break;
                           }
                     }
                  curr.setDirection(rand);
                  curr.setBodyDirection(rand);
               }
            }//***end aircraft/train movement
            if(leftMost == 1)	//move vehicles down and start them moving right
            {
               if(EI_moving == LEFT)
               {
                  EI_moving = DOWN;
                  for(int i=FIRST_VEHICLE; i<players.length; i++)
                  {
                     Player curr = players[i];
                     if(curr==null || curr.getName().equals("NONE"))
                        continue;
                     if(lowest < board.length-3 && !curr.isFlying() && !curr.getName().startsWith("TRAIN"))
                        curr.setRow(curr.getRow()+1);
                  }
               }
               else
                  if(EI_moving == DOWN)
                  {
                     EI_moving = RIGHT;
                     for(int i=FIRST_VEHICLE; i<players.length; i++)
                     {
                        Player curr = players[i];
                        if(curr==null || curr.getName().equals("NONE"))
                           continue;
                        if(!curr.isFlying() && !curr.getName().startsWith("TRAIN"))
                        {
                           curr.setDirection(RIGHT);
                           curr.setBodyDirection(RIGHT);
                        }
                     }
                  }
                  else
                     if(EI_moving == RIGHT)
                        for(int i=FIRST_VEHICLE; i<players.length; i++)
                        {
                           Player curr = players[i];
                           if(curr==null || curr.getName().equals("NONE"))
                              continue;
                           if(!curr.isFlying() && !curr.getName().startsWith("TRAIN"))
                              curr.setDirection(RIGHT);
                        }
            }//***end leftmost is first col
            else
               if(rightMost == board[0].length-2) //move vehicles down and start them moving left
               {
                  if(EI_moving == RIGHT)
                  {
                     EI_moving = DOWN;
                     for(int i=FIRST_VEHICLE; i<players.length; i++)
                     {
                        Player curr = players[i];
                        if(curr==null || curr.getName().equals("NONE"))
                           continue;
                        if(lowest < board.length-3 && !curr.isFlying() && !curr.getName().startsWith("TRAIN"))
                           curr.setRow(curr.getRow()+1);
                     }
                  }
                  else
                     if(EI_moving == DOWN)
                     {
                        EI_moving = LEFT;
                        for(int i=FIRST_VEHICLE; i<players.length; i++)
                        {
                           Player curr = players[i];
                           if(curr==null || curr.getName().equals("NONE"))
                              continue;
                           if(!curr.isFlying() && !curr.getName().startsWith("TRAIN"))
                           {
                              curr.setDirection(LEFT);
                              curr.setBodyDirection(LEFT);
                           }
                        }
                     }
                     else
                        if(EI_moving == LEFT)
                           for(int i=FIRST_VEHICLE; i<players.length; i++)
                           {
                              Player curr = players[i];
                              if(curr==null || curr.getName().equals("NONE"))
                                 continue;
                           
                              if(!curr.isFlying() && !curr.getName().startsWith("TRAIN"))
                                 curr.setDirection(LEFT);
                           }
               }//***end rightmost is last col
               else
                  if(EI_moving == LEFT)
                     for(int i=FIRST_VEHICLE; i<players.length; i++)
                     {
                        Player curr = players[i];
                        if(curr==null || curr.getName().equals("NONE"))
                           continue;
                        if(!curr.isFlying() && !curr.getName().startsWith("TRAIN"))
                           curr.setDirection(LEFT);
                     }
                  else
                     if(EI_moving == RIGHT)
                        for(int i=FIRST_VEHICLE; i<players.length; i++)
                        {
                           Player curr = players[i];
                           if(curr==null || curr.getName().equals("NONE"))
                              continue;
                           if(!curr.isFlying() && !curr.getName().startsWith("TRAIN"))
                              curr.setDirection(RIGHT);
                        }		
            return;    
         }//***end EARTH INVADERS enemy movements
         for(int i=2; i<players.length; i++)
         {
            Player curr = players[i];
            if(curr==null || curr.getName().equals("NONE"))
               continue;
            String name = curr.getName();
            int r=curr.getRow();
            int c=curr.getCol();
            if(r>0 && c>0 && r<board.length-1 && c<board[0].length-1 && (curr instanceof Vehicle))
               ((Vehicle)(curr)).setOnField(true);
         
            if(curr.isFlying() && webs[panel].size() > 0)         
            {
               int x = curr.findX(cellSize);
               int y = curr.findY(cellSize);
               for(int p=0; p<webs[panel].size(); p++)
               {
                  int[]ray = webs[panel].get(p);
                  if(Utilities.isPointOnRay(x, y, ray[0], ray[1], ray[2], ray[3]))
                  {
                     explosions.add(new Explosion("BIG", x, y, explosionImages, animation_delay));
                     Ordinance.radiusDamage(-1, x, y, 25, panel, .5);
                     Spawner.resetEnemy(i);
                     if(webs[panel].size() > 0 && p < webs[panel].size())
                        webs[panel].remove(p);
                     p--;
                     Structure str1 = structures[ray[4]][ray[5]][panel];
                     Structure str2 = structures[ray[6]][ray[7]][panel];
                     if(str1 != null)
                        str1.setWebValue(0);
                     if(str2 != null)
                        str2.setWebValue(0);
                     Utilities.updateKillStats(curr);
                     break;
                  }
               }
            }
         //reset any ground vehicle that ended up in the water
         //reset any train not on tracks - we need this because a player might change panels as a vehicle spawns
            if(name.startsWith("TRAIN"))
            {							
               Structure str = structures[r][c][panel];
               if(str != null && str.getName().equals("hole") && str.getHealth() != 0)
               {
                  explosions.add(new Explosion("BIG", curr.getX()-(cellSize/2), curr.getY()-(cellSize/2), puffImages, animation_delay));
                  Utilities.updateKillStats(curr);
                  Spawner.resetEnemy(i);
                  continue;
               }
               else
                  if(board[r][c][panel].startsWith("~"))
                  {
                     explosions.add(new Explosion("BIG", curr.getX()-(cellSize/2), curr.getY()-(cellSize/2), puffImages, animation_delay));
                     Spawner.resetEnemy(i);
                     continue;
                  }
                  else
                     if(!board[r][c][panel].startsWith("T"))
                     {
                        explosions.add(new Explosion("SMALL", curr.getX()-(cellSize/2), curr.getY()-(cellSize/2), puffImages, animation_delay));
                        Spawner.resetEnemy(i);
                        continue;
                     }
            }	 
            else //ground vehicles
               if(!curr.isFlying() && !curr.getName().startsWith("BOAT") && (curr instanceof Vehicle))
               {
                           
                  if(((Vehicle)(curr)).getStunTime() > numFrames)	//ground unit might be stunned by WoeMantis shriek
                     continue;
                  if(board[r][c][panel].startsWith("~"))
                  {
                     explosions.add(new Explosion("SMALL", curr.getX()-(cellSize/2), curr.getY()-(cellSize/2), puffImages, animation_delay));
                     Spawner.resetEnemy(i);
                     continue;
                  }
               }
               else					//reset any water vehicle that ended up on land
                  if(name.startsWith("BOAT"))
                  {							
                     if(!board[r][c][panel].startsWith("~"))
                     {
                        explosions.add(new Explosion("SMALL", curr.getX()-(cellSize/2), curr.getY()-(cellSize/2), puffImages, animation_delay));
                        Spawner.resetEnemy(i);
                        continue;
                     }
                  }
                  else
                     if(curr.getHealth() <= 0)
                     {
                        Spawner.resetEnemy(i);
                        continue;
                     }
         
         //if a ground unit has been on the playable field and leaves it, respawn them
            if(!curr.isFlying() && !name.startsWith("TRAIN") && (curr instanceof Vehicle))
            {
               if((r==0 || c==0 || r==board.length-1 || c==board[0].length-1) && ((Vehicle)(curr)).getOnField())
               {
                  ((Vehicle)(curr)).setOnField(false);
                  Spawner.resetEnemy(i);
                  continue;
               }
            }
            if(name.endsWith("nukebomber"))
            {//for the nukebomber, set the detonation coordinates so nuke fires when the plane exits the field
               int dr = curr.getDetRow();
               int dc = curr.getDetCol();
               int bd = curr.getBodyDirection();
               int dd = curr.getDetDir();
               if(bd==dd && (((bd==LEFT || bd==RIGHT) && c==dc) || (bd==UP || bd==DOWN) && r==dr))
               {
                  Ordinance.nuke();
                  curr.setDetRow(-1);
                  curr.setDetCol(-1);
               }
            }    
         
            if(curr.isMoving())
               continue;
            int bodyDir = curr.getBodyDirection();
            curr.clearDirections();
            int[] target =  isMonsterInSight(curr);
            int mDir = target[0];			//direction of the monster, -1 if none
            int monsterIndex = target[1];	//index of the monster in the players array, -1 if none
            ArrayList<Integer> dirs = new ArrayList();		//array of preferred directions - everything but the reverse of their body position 
            if(bodyDir==UP)
            {
               if(isValidMove(curr, r-1, c))
               {
                  if( Utilities.nonFlyingCivilian(name) && mDir==UP)
                  {}//civilians don't want to move towards the monster if it is in direct sight
                  else
                     dirs.add(UP);	
               }
               if(isValidMove(curr, r, c+1))
               {
                  if( Utilities.nonFlyingCivilian(name) && mDir==RIGHT)
                  {}//civilians don't want to move towards the monster if it is in direct sight
                  else
                     dirs.add(RIGHT); 
               }
               if(!name.startsWith("TRAIN"))		//trains need to follow the track circuit - only straight and right turns
               {	
                  if(isValidMove(curr, r, c-1))
                  {
                     if( Utilities.nonFlyingCivilian(name) && mDir==LEFT)
                     {}//civilians don't want to move towards the monster if it is in direct sight
                     else
                        dirs.add(LEFT);
                  }
               }
            } 
            else if(bodyDir==RIGHT)
            {
               if(!name.startsWith("TRAIN"))		//trains need to follow the track circuit - only straight and right turns
               {	
                  if(isValidMove(curr, r-1, c))
                  {
                     if( Utilities.nonFlyingCivilian(name) && mDir==UP)
                     {}//civilians don't want to move towards the monster if it is in direct sight
                     else
                        dirs.add(UP);	
                  }
               }
               if(isValidMove(curr, r, c+1))
               {
                  if( Utilities.nonFlyingCivilian(name) && mDir==RIGHT)
                  {}//civilians don't want to move towards the monster if it is in direct sight
                  else
                     dirs.add(RIGHT); 
               }
               if(isValidMove(curr, r+1, c))
               {
                  if( Utilities.nonFlyingCivilian(name) && mDir==DOWN)
                  {}//civilians don't want to move towards the monster if it is in direct sight
                  else
                     dirs.add(DOWN);
               }
            } 
            else if(bodyDir==DOWN)
            {
               if(isValidMove(curr, r+1, c))
               {
                  if( Utilities.nonFlyingCivilian(name) && mDir==DOWN)
                  {}//civilians don't want to move towards the monster if it is in direct sight
                  else
                     dirs.add(DOWN);
               }
               if(!name.startsWith("TRAIN"))		//trains need to follow the track circuit - only straight and right turns
               {		
                  if(isValidMove(curr, r, c+1))
                  {
                     if( Utilities.nonFlyingCivilian(name) && mDir==RIGHT)
                     {}//civilians don't want to move towards the monster if it is in direct sight
                     else
                        dirs.add(RIGHT);
                  }
               } 
               if(isValidMove(curr, r, c-1))
               {
                  if( Utilities.nonFlyingCivilian(name) && mDir==LEFT)
                  {}//civilians don't want to move towards the monster if it is in direct sight
                  else
                     dirs.add(LEFT);
               }
            }
            else //if(curr.getBodyDirection()==LEFT)
            {
            
               if(isValidMove(curr, r-1, c))
               {
                  if( Utilities.nonFlyingCivilian(name) && mDir==UP)
                  {}//civilians don't want to move towards the monster if it is in direct sight
                  else
                     dirs.add(UP);
               }
               if(!name.startsWith("TRAIN"))		//trains need to follow the track circuit - only straight and right turns
               {	
                  if(isValidMove(curr, r+1, c))
                  {
                     if( Utilities.nonFlyingCivilian(name) && mDir==DOWN)
                     {}//civilians don't want to move towards the monster if it is in direct sight
                     else
                        dirs.add(DOWN); 
                  }
               }
               if(isValidMove(curr, r, c-1))
               {
                  if( Utilities.nonFlyingCivilian(name) && mDir==LEFT)
                  {}//civilians don't want to move towards the monster if it is in direct sight
                  else
                     dirs.add(LEFT);
               }
            }
         
            if(dirs.size() > 0)
            {  
               if(curr instanceof Monster)
               {//***MONSTER AI*******************
                  double headTurnProb = 0.25;
                  double stompProb = 0.5;
                  int vDir = isVehicleInSight(curr);
                  if(vDir>=0)
                  { 
                     boolean airShot = false;
                     int vD = vDir;
                     if(vD >= 10)
                     {
                        airShot = true;
                        vD-=10;
                     }
                     if(curr.head360() || !Utilities.oppositeDirections(vD, curr.getHeadDirection()))   
                     {
                        curr.setHeadDirection(vD);
                        if((curr.getName().startsWith("Gob") || curr.getName().startsWith("Boo")) && numFrames >= curr.getLastShotTime() + curr.getReloadTime())
                        {
                           Projectile temp = null;
                           if(curr.getName().startsWith("Boo"))
                           {
                              temp = new Projectile(curr.getName()+vD, curr.getX(), curr.getY(), 50, beamImages, SPEED, "BEAM", SPEED*10, airShot, i, -1, -1);
                           }
                           else
                              if(curr.getName().startsWith("Gob"))
                              {
                                 temp = new Projectile("flame"+vD, curr.getX(), curr.getY(), 15, monsterFireImages, SPEED, "FIRE", SPEED*4, airShot, i, -1, -1);
                              }
                           if(temp!=null)
                           {
                              temp.setDirection(vD);
                              projectiles.add(temp);
                              curr.setLastShotTime(numFrames);
                              continue;   
                           }
                        }
                     }
                  }
                  else
                     if(Math.random() < headTurnProb)
                     {
                        String hd = "right";
                        if(Math.random() < .5)
                           hd = "left";
                        Utilities.turnHead(curr, hd);  
                     }
                  Structure str = structures[r][c][panel];
                  if(str!=null && str.getHealth() > 0 && str.isDestroyable() && !str.getName().startsWith("FUEL") && Math.random() < stompProb)
                     playerDirect(KeyEvent.VK_SPACE, i);
                  else
                  {
                     int dir = dirs.get((int)(Math.random()*dirs.size()));
                     if(dir==UP)
                     {
                        if(curr.getBodyDirection()!=UP)
                        {
                           curr.setBodyDirection(UP);
                           curr.setHeadDirection(UP);
                        }
                        else
                        {
                           if(!curr.isSwimmer() && board[r-1][c][panel].equals("~~~") && !board[r][c][panel].equals("~~~"))
                           {
                              continue;
                           }
                           if(r-1 >= 1)
                           {
                              str = structures[r-1][c][panel];	//don't walk into a fire
                              if(str!=null && str.onFire())
                                 continue;
                           }
                           playerDirect(KeyEvent.VK_UP, i);
                        }
                     }
                     else
                        if(dir==DOWN)
                        {
                           if(curr.getBodyDirection()!=DOWN)
                           {
                              curr.setBodyDirection(DOWN);
                              curr.setHeadDirection(DOWN);
                           }
                           else
                           {
                              if(!curr.isSwimmer() && board[r+1][c][panel].equals("~~~") && !board[r][c][panel].equals("~~~"))
                              {
                                 continue;
                              }
                              if(r+1 <= structures.length -1)
                              {
                                 str = structures[r+1][c][panel];	//don't walk into a fire
                                 if(str!=null && str.onFire())
                                    continue;
                              }
                              playerDirect(KeyEvent.VK_DOWN, i);
                           }
                        }
                        else
                           if(dir==LEFT)
                           {
                              if(curr.getBodyDirection()!=LEFT)
                              {
                                 curr.setBodyDirection(LEFT);
                                 curr.setHeadDirection(LEFT);
                              }
                              else
                              {
                                 if(!curr.isSwimmer() && board[r][c-1][panel].equals("~~~") && !board[r][c][panel].equals("~~~"))
                                 {
                                    continue;
                                 }
                                 if(c-1 >= 1)
                                 {
                                    str = structures[r][c-1][panel];	//don't walk into a fire
                                    if(str!=null && str.onFire())
                                       continue;
                                 }
                                 playerDirect(KeyEvent.VK_LEFT, i);
                              }
                           }
                           else
                              if(dir==RIGHT)
                              {
                                 if(curr.getBodyDirection()!=RIGHT)
                                 {
                                    curr.setBodyDirection(RIGHT);
                                    curr.setHeadDirection(RIGHT);
                                 }
                                 else
                                 {
                                    if(!curr.isSwimmer() && board[r][c+1][panel].equals("~~~") && !board[r][c][panel].equals("~~~"))
                                    {
                                       continue;
                                    }
                                    if(c+1 <= board[0].length -1)
                                    {
                                       str = structures[r][c+1][panel];	//don't walk into a fire
                                       if(str!=null && str.onFire())
                                          continue;
                                    }
                                    playerDirect(KeyEvent.VK_RIGHT, i);
                                 }
                              }
                  }
                  continue;
               }//end monster AI movement
               else  //shoot at a target
                  if(name.endsWith("troops") || name.endsWith("jeep")|| name.endsWith("police") || name.startsWith("TANK")  || name.endsWith("coastguard")  || name.endsWith("destroyer") || name.endsWith("fighter") || name.equals("AIR bomber"))
                  { 
                     boolean airShot = false;
                     if(gameMode == EARTH_INVADERS)		//we don't want vehicles to shoot each other
                     {
                        airShot = true;
                        curr.setHeadDirection(DOWN);   
                     }
                     if(monsterIndex>=0 && monsterIndex<players.length && players[monsterIndex].isFlying())
                        airShot = true;
                     if(curr.getName().endsWith("fighter") )
                        curr.setDirection(bodyDir);	//keep moving while shooting
                     if(mDir != -1 && curr.getRow() > 0 && curr.getCol() > 0 && curr.getRow() < board.length-1 && curr.getCol() < board[0].length-1)
                     {						//don't shoot from off the visible board
                        if((mDir==bodyDir || ((curr.getName().startsWith("TANK") || curr.getName().endsWith("jeep") || name.equals("AIR bomber")) && (mDir==curr.getHeadDirection() || name.equals("AIR bomber")))) && numFrames >= curr.getLastShotTime() + curr.getReloadTime())
                        {					//AIR bomber needs to be able to drop bombs if they see the monster in front of them or behind them, so we need to check the name in two conditions
                           Projectile temp;
                           if(name.endsWith("jeep") || name.endsWith("coastguard"))
                           {
                              Sound.machinegun();
                              temp = new Projectile("jeep"+mDir, curr.getX(), curr.getY(), 5, machBulletImages, SPEED, "BULLET", SPEED*6, airShot, i, -1, -1);
                           }
                           else
                              if(name.endsWith("troops") || name.endsWith("police") )
                              {
                                 Sound.gunshot();
                                 temp = new Projectile("troops"+mDir, curr.getX(), curr.getY(), 3, bulletImages, SPEED, "BULLET", SPEED*6, airShot, i, -1, -1);
                              }
                              else
                                 if(name.endsWith("destroyer") || name.endsWith("artillery"))
                                 {
                                    Sound.cannonshot();
                                    temp = new Projectile("destroyer"+mDir, curr.getX(), curr.getY(), 15, shellImages, SPEED, "SHELL", SPEED*3, true, i, players[monsterIndex].findX(cellSize), players[monsterIndex].findY(cellSize));
                                 }
                                 else
                                    if(name.endsWith("fighter"))
                                    {
                                       Sound.shootMissile();
                                       temp = new Projectile("fighter"+mDir, curr.getX(), curr.getY(), 30, rocketImages, SPEED, "SHELL", SPEED*8, true, i, players[monsterIndex].findX(cellSize), players[monsterIndex].findY(cellSize));
                                    }
                                    else
                                       if(name.endsWith("missile"))
                                       {
                                          Sound.shootMissile();
                                          temp = new Projectile("missile"+mDir, curr.getX(), curr.getY(), 50, rocketImages, SPEED, "SHELL", SPEED*3, true, i, players[monsterIndex].findX(cellSize), players[monsterIndex].findY(cellSize));
                                       }
                                       else
                                          if(name.endsWith("flame"))
                                          {
                                             Sound.shootMissile();
                                             temp = new Projectile("flame"+mDir, curr.getX(), curr.getY(), 15, monsterFireImages, SPEED, "FIRE", SPEED*4, airShot, i, -1, -1);
                                          }
                                          else   
                                             if(name.equals("AIR bomber"))
                                             {
                                                temp = null;
                                                if(!DISABLE_BOMBERS)
                                                {
                                                   int mR = players[PLAYER1].getRow();	//main player row & col
                                                   int mC = players[PLAYER1].getCol();
                                                   int mR2 = -99;								//possible 2nd monster row & col
                                                   int mC2 = -99;
                                                   if(p1partner)
                                                   {
                                                      mR2 = players[PLAYER2].getRow();
                                                      mC2 = players[PLAYER2].getCol();
                                                   }
                                                   if(players[PLAYER1].getHealth() > 0 && (Math.abs(r - mR) <= 2 || Math.abs(c - mC) <= 2 || Math.abs(r - mR2) <= 2 || Math.abs(c - mC2) <= 2))
                                                   {//our bomber is in the same row & col as a monster + or - 1
                                                      if(bodyDir==UP && r < board.length -1)
                                                      {
                                                         Ordinance.bigExplosion(curr.getX(), curr.getY()+(cellSize*2), panel);
                                                         Ordinance.radiusDamage(-1,curr.getX(), curr.getY()+(cellSize*2), 50, panel, .25);
                                                      }
                                                      else
                                                         if(bodyDir==DOWN && r > 1)
                                                         {
                                                            Ordinance.bigExplosion(curr.getX(), curr.getY()-(cellSize*2), panel);
                                                            Ordinance.radiusDamage(-1,curr.getX(), curr.getY()-(cellSize*2), 50, panel, .25);
                                                         }
                                                         else
                                                            if(bodyDir==RIGHT && c < board[0].length -1)
                                                            {
                                                               Ordinance.bigExplosion(curr.getX()-(cellSize*2), curr.getY(), panel);
                                                               Ordinance.radiusDamage(-1,curr.getX()-(cellSize*2), curr.getY(), 50, panel, .25);
                                                            }
                                                            else
                                                               if(bodyDir==LEFT && c > 1)
                                                               {
                                                                  Ordinance.bigExplosion(curr.getX()+(cellSize*2), curr.getY(), panel);
                                                                  Ordinance.radiusDamage(-1,curr.getX()+(cellSize*2), curr.getY(), 50, panel, .25);
                                                               }
                                                   }
                                                }  
                                             }
                                             else
                                             {
                                                Sound.cannonshot();
                                                temp = new Projectile(""+mDir, curr.getX(), curr.getY(), 15, shellImages, SPEED, "SHELL", SPEED*5, airShot, i, -1, -1);
                                             }
                           if(temp != null)       
                              temp.setDirection(mDir);
                        
                           if(name.endsWith("troops") || name.endsWith("police") && Math.random() < .5)		//make the police move towards the monster half the time
                           {
                              if(mDir==UP && isValidMove(curr, curr.getRow()-1,  curr.getCol()))
                                 curr.setDirection(UP);
                              else if(mDir==DOWN  && isValidMove(curr, curr.getRow()+1,  curr.getCol()))
                                 curr.setDirection(DOWN);
                              else if(mDir==LEFT  && isValidMove(curr, curr.getRow(),  curr.getCol()-1))
                                 curr.setDirection(LEFT); 
                              else if(mDir==RIGHT  && isValidMove(curr, curr.getRow(),  curr.getCol()+1))
                                 curr.setDirection(RIGHT);
                              else
                                 curr.setDirection(-1);
                           }
                           if(temp != null && players[PLAYER1].getHealth() > 0)     
                           {   
                              if(gameMode==EARTH_INVADERS && !curr.isFlying())
                              {}
                              else
                              {
                                 if(numFrames >= ceaseFireTime + ceaseFireDuration || gameMode==EARTH_INVADERS)
                                 {
                                    projectiles.add(temp);
                                    curr.setLastShotTime(numFrames);
                                    continue;
                                 }
                              }   
                           }
                        }
                        else		//change to face the monster to line up a shot
                        {
                           if(name.endsWith("troops") || name.endsWith("police")  || name.endsWith("destroyer")  || name.endsWith("coastguard") || name.endsWith("artillery"))
                           {
                              curr.setDirection(-1);		//stop to shoot
                              curr.setBodyDirection(mDir);
                           }
                           else
                              if(curr.getName().startsWith("TANK") || curr.getName().endsWith("jeep"))
                                 curr.setHeadDirection(mDir);
                           continue;
                        }
                     }
                  }
               int rand = dirs.get((int)(Math.random()*dirs.size()));
               if(curr.isFlying() && (r>0 && c>0 && r<board.length-1 && c<board[0].length-1))  
               {
                  rand = bodyDir;		//make it so aircraft prefer to keep going the same direciton
               }
               else
                  if(name.startsWith("TRAIN"))  
                  {//make it so trains prefer to follow the right wall
                     for(int j=0; j<dirs.size(); j++)
                        if(dirs.get(j)!=bodyDir)
                        {
                           rand = dirs.get(j);
                           break;
                        }
                  }
               curr.setDirection(rand);
               curr.setBodyDirection(rand);
            
               continue;
            }
         //if no preferred direction, include the option to turn around
         //civilians should prefer to turn around rather than approach the monster
            if(name.endsWith("civilian") || name.endsWith("bus"))
            {
               if(bodyDir==mDir)	//if we are facing the same direction as the monster, turn around
               {
                  if(bodyDir==UP && isValidMove(curr, curr.getRow()+1,  curr.getCol()))
                  {
                     curr.setDirection(DOWN);
                     curr.setBodyDirection(DOWN);
                     continue;
                  }
                  else
                     if(bodyDir==DOWN && isValidMove(curr, curr.getRow()-1,  curr.getCol()))
                     {
                        curr.setDirection(UP);
                        curr.setBodyDirection(UP);
                        continue;
                     }
                     else
                        if(bodyDir==LEFT && isValidMove(curr, curr.getRow(),  curr.getCol()+1))
                        {
                           curr.setDirection(RIGHT);
                           curr.setBodyDirection(RIGHT);
                           continue;
                        }
                        else
                           if(bodyDir==RIGHT && isValidMove(curr, curr.getRow(),  curr.getCol()-1))
                           {
                              curr.setDirection(LEFT);
                              curr.setBodyDirection(LEFT);
                              continue;
                           }
               }
            }
            int rand = (int)(Math.random()*4);
            if(rand==UP && isValidMove(curr, curr.getRow()-1,  curr.getCol()))
            {
               curr.setDirection(UP);
               curr.setBodyDirection(UP);
               continue;
            }
            if(rand==DOWN  && isValidMove(curr, curr.getRow()+1,  curr.getCol()))
            {
               curr.setDirection(DOWN);
               curr.setBodyDirection(DOWN);
               continue;
            }
            if(rand==LEFT  && isValidMove(curr, curr.getRow(),  curr.getCol()-1))
            {
               curr.setDirection(LEFT); 
               curr.setBodyDirection(LEFT);
               continue;
            }
            if(rand==RIGHT  && isValidMove(curr, curr.getRow(),  curr.getCol()+1))
            {
               curr.setDirection(RIGHT);
               curr.setBodyDirection(RIGHT);
               continue;
            }
         
         }  
      }
   
   //pre:  cur!= null, owner>=0 && owner < players.size
   //post: vehicle at index owner shoots
      public static void vehicleShoot(Player curr, int owner)
      {
         boolean airShot = false;
         if(gameMode == EARTH_INVADERS)	//so vehicles don't shoot each other
            airShot = true;
         if(numFrames >= curr.getLastShotTime() + curr.getReloadTime())
         {
            int hd = curr.getHeadDirection();
            Projectile temp= null;
            int bulletSpeed = SPEED*6;
         //find monster in sights   
            int[] target = isMonsterInSight(curr);
            int mIndex = target[1];
            int detX = -1;
            int detY = -1;
            if(mIndex > 0)
            {
               detX = players[mIndex].findX(cellSize);
               detY = players[mIndex].findY(cellSize);
               if(players[mIndex].isFlying())
                  airShot = true;
            }
            if(curr.getName().endsWith("missile") )
            {
               Sound.shootMissile();
            
               if(gameMode == CITY_SAVER)
                  bulletSpeed = SPEED*4;
               else
                  bulletSpeed = SPEED*3;
               temp = new Projectile("missile"+hd, curr.getX(), curr.getY(), 50, rocketImages, SPEED, "SHELL", bulletSpeed, true, owner, detX, detY);
            }
            else
               if(curr.getName().endsWith("jeep") )
               {
                  Sound.machinegun();
                  if(gameMode == CITY_SAVER)
                     bulletSpeed = SPEED*7;
                  else
                     bulletSpeed = SPEED*6;
                  temp = new Projectile("jeep"+hd, curr.getX(), curr.getY(), 5, machBulletImages, SPEED, "BULLET", bulletSpeed, airShot, owner, -1, -1);
               }
               else
                  if(curr.getName().endsWith("troops") || curr.getName().endsWith("police") )
                  {
                     Sound.gunshot();
                     temp = new Projectile("troops"+hd, curr.getX(), curr.getY(), 3, bulletImages, SPEED, "BULLET", SPEED*6, airShot, owner, -1, -1);
                  }
                  else
                     if(curr.getName().endsWith("flame") )
                     {
                        Sound.shootFlame();
                     
                        if(gameMode == CITY_SAVER)
                           bulletSpeed = SPEED*5;
                        else
                           bulletSpeed = SPEED*4;
                        temp = new Projectile("flame"+hd, curr.getX(), curr.getY(), 15, monsterFireImages, SPEED, "FIRE", bulletSpeed, airShot, owner, -1, -1);
                     }
                     else  
                     {
                        Sound.cannonshot();
                     
                        if(gameMode == CITY_SAVER)
                           bulletSpeed = SPEED*6;
                        else
                           bulletSpeed = SPEED*5; 
                        temp = new Projectile(""+hd, curr.getX(), curr.getY(), 15, shellImages, SPEED, "SHELL", bulletSpeed, airShot, owner, -1, -1);
                     }
            temp.setDirection(hd);
            if(temp != null) 
            {       
               projectiles.add(temp);
               curr.setLastShotTime(numFrames);   
            }
            if(projectiles.size()>BULLET_LIMIT)	//remove earliest fired bullet if we have more than the bullet limit
            {
               explosions.add(new Explosion("SMALL", projectiles.get(0).getX()-(cellSize/2), projectiles.get(0).getY()-(cellSize/2), explosionImages, animation_delay));
               projectiles.remove(0);
            }
         }
      }
   
   }