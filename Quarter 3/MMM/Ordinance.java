//Mash, Mangle & Munch - Rev. Dr. Douglas R Oberle, June 2012   doug.oberle@fcps.edu
//PROJECTILE, FIRE & EXPLOSION UTILITIES  
//handles motion of projectiles and behavior of fire and explosions
   public class Ordinance extends MMMPanel
   {
   //post:  advance all active projectiles in this panel
      public static void moveBullets()
      {
         for(int i=0; i<projectiles.size(); i++)
         {
            Projectile curr = projectiles.get(i);
            double range = cellSize;     
            double dist = Utilities.distance(curr.getX(),curr.getY(),curr.getDetX(),curr.getDetY());
            boolean detonate =  ((curr.getDetX()!=-1) && dist < range);
            int[] coord = Utilities.findCoord(curr.getX(), curr.getY());
            if(gameMode == EARTH_INVADERS && curr.getOwner()>1 && coord[0] == board.length-2 && (curr.getType().equals("SHELL") || curr.getType().equals("FIRE") || curr.getName().startsWith("missile")))
               detonate = true; 
            if(outOfBounds(curr) || detonate)  
            {	//remove any projectiles that travel off the screen or is set to detonate at a particualr spot
               if(detonate)
               {
                  if(curr.getType().equals("FIRE"))
                     explosions.add(new Explosion("SMALL", curr.getX()-(cellSize/2), curr.getY()-(cellSize/2), puffImages, animation_delay));
                  else
                     if(curr.getName().startsWith("missile"))
                     {
                        bigExplosion(curr.getX(), curr.getY(), panel);
                     }
                     else
                     {
                        coord = Utilities.findCoord(curr.getX(), curr.getY());
                        if(board[coord[0]][coord[1]][panel].equals("~~~"))
                        {
                           Sound.waterDetonation();
                           explosions.add(new Explosion("SMALL", curr.getX()-(cellSize/2), curr.getY()-(cellSize/2), waterExplImages, animation_delay*2));
                        }
                        else
                        {
                           Sound.smallExplosion();
                           explosions.add(new Explosion("SMALL", curr.getX()-(cellSize/2), curr.getY()-(cellSize/2), explosionImages, animation_delay));
                        }
                     }
                  radiusDamage(-1, curr.getX(), curr.getY(), curr.getPower(), panel, .25);
               }
               if(projectiles.size()>0 && i<projectiles.size())
               {
                  projectiles.remove(i);
                  i--;
               }
               continue;	
            }
            int speed = curr.getSpeed();
            if(curr.isMovingUp())
               curr.setY(curr.getY()-speed);
            else 
               if(curr.isMovingDown())
                  curr.setY(curr.getY()+speed);
               else
                  if(curr.isMovingLeft())
                     curr.setX(curr.getX()-speed);
                  else 
                     if(curr.isMovingRight())
                        curr.setX(curr.getX()+speed);
         }
      }
   
   //post:  returns true if the structure is large enough to attach a web trap on
      private static boolean webable(Structure str)
      {
         return (str.getHealth() > 0 && (str.getHeight()==2 || str.getName().endsWith("LIGHTHOUSE") || str.getName().endsWith("TOWER") || str.getName().equals("ELEC TOWER 0")));
      }
   
   //post:  returns wether or not a bullet should be removed because it is out of bounds (or hit an obstacle)
      public static boolean outOfBounds(Projectile curr)
      {
         if(curr.getX()<0 || curr.getY()<0 || curr.getX()>=(board[0].length*cellSize) || curr.getY()>=(board.length*cellSize))
            return true;
         if(curr.inAir() && gameMode!=EARTH_INVADERS)			//airshots don't collide with buildings
            return false;   
         int cX = curr.getX();
         int cY = curr.getY();
         for(int r=0;r<board.length;r++)		//see if there is a wall that stops the bullet
         {
            for(int c=0;c<board[0].length;c++)
            {
               if(board[r][c][panel].startsWith("#") && !curr.getType().equals("WEB"))
               {
                  int bX = (cellSize*c);
                  int bY = (cellSize*r);
                  if(Utilities.distance(cX, cY, bX, bY) <= (cellSize/2))
                  {	//only show elec explosion for hitting a spot that is on the visible board
                     if(r>0 && c>0 && r<board.length-1 && c<board[0].length-1)
                     {
                        Sound.elecExplosion();
                        explosions.add(new Explosion("SMALL", curr.getX()-(cellSize/2), curr.getY()-(cellSize/2), elecExplImages, animation_delay));        
                     //only Boobootron has enough firepower to destroy elect towers/plants
                        if(curr.getType().equals("BEAM") && structures[r][c][panel]!=null)
                        {
                           message = "ERROR!";
                           messageTime = numFrames;
                           if(players[PLAYER1].getHealth() > 0)  
                           {
                              int value =  (int)(structures[r][c][panel].getPropertyValue());
                              propertyDamage += value;
                              if(panel == 4)		//we only protect the center panel in CITY_SAVER
                                 propertyValue -= value;
                              Utilities.updateKillStats(structures[r][c][panel]);
                           }
                           structures[r][c][panel] = null;
                           board[r][c][panel] = "S--";
                        }
                     }
                     return true;
                  }
               
               }
            }
         }
         double impactRadius = cellSize/2.0;
         int damage = curr.getPower();
      
         for(int r=0;r<structures.length;r++)		//see if there is a structure that stops the bullet
         {
            for(int c=0;c<structures[0].length;c++)
            {
               Structure str = structures[r][c][panel];
               if(str != null)		//shoot over small buildings and trees (which have a health of -1)
               {
                  int bX = (cellSize*c);
                  int bY = (cellSize*r);
                  double distance = Utilities.distance(cX, cY, bX, bY);
                  if((gameMode!=EARTH_INVADERS && !str.canShootOver(curr)) || (gameMode==EARTH_INVADERS && str.getHealth()>0))
                  {
                     if(distance <= impactRadius)
                     {
                        if(str.getName().startsWith("Blop"))
                        {
                           if(curr.getType().startsWith("FIRE"))	//flames will destroy Blop-glop as it passes over it
                              str.damage(damage);
                           return false;									//projectiles are fired over Blop-glop
                        }
                        if(curr.getType().startsWith("BULLET") || curr.getType().startsWith("SHRIEK") || curr.getType().startsWith("WEB"))
                        {
                           Sound.bulletHit();   
                           explosions.add(new Explosion("SMALL", curr.getX()-(cellSize/2), curr.getY()-(cellSize/2), puffImages, animation_delay));
                        }
                        else
                        {
                           Sound.smokepuffExplosion(); 
                           explosions.add(new Explosion("SMALL", curr.getX()-(cellSize/2), curr.getY()-(cellSize/2), explosionImages, animation_delay));
                        }
                        str.damage(damage);
                        if(str.getHealth() == 0)
                        {
                           if(str.getName().startsWith("FUEL"))	//if we shoot a fuel depot or gas statio, make it explode
                           {
                              bigExplosion(str.findX(cellSize), str.findY(cellSize), panel);
                              radiusDamage(-1, str.findX(cellSize), str.findY(cellSize), 50, panel, .5);
                           }
                           if(gameMode != EARTH_INVADERS)
                              Spawner.removeFromSpawn(humanSpawnPoints[panel], r, c); 
                           if(players[PLAYER1].getHealth() > 0)  
                           {
                              int value = (int)(str.getPropertyValue());
                              propertyDamage += value;
                              if(panel == 4)		//we only protect the center panel in CITY_SAVER
                                 propertyValue -= value;
                              Utilities.updateKillStats(str);
                           }
                        }
                        else
                        {	//75% that hitting a building with a car starts a fire
                           if(((curr.getType().startsWith("CAR") && Math.random() < .75) || curr.getType().startsWith("FIRE") || str.getName().startsWith("FUEL")) && !str.onFire())
                           {
                              str.setOnFire(true);
                           }
                         
                           if(curr.getType().startsWith("WEB") && str.getWebValue()==0 && webable(str))
                           {
                              str.setWebValue(str.getWebValue()+1);
                           //now, see if there is another tall building around it with a web value of 1
                           //if so, string a web between them and change their web values to 2
                              boolean done = false;
                              for(int i=-3; i<=3; i++)
                              {
                                 for(int j=-3; j<=3; j++)
                                 {
                                    if(i==0 && j==0)	//don't check our own location
                                       continue;
                                    if(r+i < 0 || r+i >= structures.length)
                                       continue;		//don't check out of bounds
                                    if(c+j < 0 || c+j >= structures[0].length)
                                       continue;		//don't check out of bounds    
                                    Structure str2 = structures[r+i][c+j][panel];
                                    if(str2==null || !webable(str) || str2.getWebValue()!=1)
                                       continue;
                                    int[]temp = new int[8];	//web points x1,y1,x2,y2, then r & c loc of buildings to make it easy to find it to remove it when the buliding is destroyed
                                    temp[0] = c*cellSize + (cellSize/2);		
                                    temp[1] = r*cellSize - ((str.getHeight()-1)*cellSize) - str.getHeightDiff()  + (cellSize/2);			
                                    temp[2] = (c+j)*cellSize  + (cellSize/2); 
                                    temp[3] = (r+i)*cellSize - ((str2.getHeight()-1)*cellSize) - str2.getHeightDiff()  + (cellSize/2);	
                                    temp[4] = r;	//record the building's row & col location to make the web easy to find for removal
                                    temp[5] = c;
                                    temp[6] = r + i;
                                    temp[7] = c + j;
                                    webs[panel].add(temp); 
                                    str.setWebValue(str.getWebValue()+1);
                                    str2.setWebValue(str.getWebValue()+1);
                                    done = true;
                                    break;
                                 }
                                 if(done)
                                    break;
                              }
                           }
                        }
                        return true;
                     }
                  }
               }
            }
         }
      
         return false;
      }
   
   //post: puts a big explosion effect at coordinate x, y in panel p
      public static void bigExplosion(int x, int y, int p)
      {
         if(panel == p)
         {
            int [] coord = Utilities.findCoord(x, y);
            if(coord[0] >= 1 && coord[0] < board.length-1 && coord[1]>=1 && coord[1]<board[0].length-1)
            {
               if(board[coord[0]][coord[1]][p].equals("~~~"))
               {
                  Sound.waterDetonation();
                  explosions.add(new Explosion("BIG", x - (cellSize/2), y - (cellSize/2), waterExplImages, animation_delay*2));
               }
               else
               {
                  Sound.bigExplosion();
                  explosions.add(new Explosion("BIG", x - (cellSize/2), y - (cellSize/2), explosionImages, animation_delay));
               }
            }
            if(coord[1]-1 >= 0)					//to the left
               explosions.add(new Explosion("SMALL", (int)(x - (cellSize*1.5)), y - (cellSize/2), puffImages, animation_delay));
            if(coord[1]+1<board[0].length)	//right
               explosions.add(new Explosion("SMALL", x + (cellSize/2), y - (cellSize/2), puffImages, animation_delay));
            if(coord[0]-1 >= 0)					//above
               explosions.add(new Explosion("SMALL", x - (cellSize/2), (int)(y - (cellSize*1.5)), puffImages, animation_delay));
            if(coord[0]+1<board.length)		//below
               explosions.add(new Explosion("SMALL", x - (cellSize/2), y + (cellSize/2), puffImages, animation_delay));
         }
      }
   
   //post: puts a big smoke explosion effect at coordinate x, y in panel p
      public static void bigExplosion2(int x, int y, int p)
      {
         if(panel == p)
         {
            int [] coord = Utilities.findCoord(x, y);
            if(coord[0] >= 1 && coord[0] < board.length-1 && coord[1]>=1 && coord[1]<board[0].length-1)
            {
               if(board[coord[0]][coord[1]][p].equals("~~~"))
               {
                  Sound.waterDetonation();
                  explosions.add(new Explosion("BIG", x - (cellSize/2), y - (cellSize/2), waterExplImages, animation_delay*2));
               }
               else
               {
                 Sound.massiveExplosion();
                  explosions.add(new Explosion("BIG", x - (cellSize/2), y - (cellSize/2), puffImages, animation_delay));
               }
            }
            if(coord[1]-1 >= 0)					//to the left
               explosions.add(new Explosion("SMALL", (int)(x - (cellSize*1.5)), y - (cellSize/2), puffImages, animation_delay));
            if(coord[1]+1<board[0].length)	//right
               explosions.add(new Explosion("SMALL", x + (cellSize/2), y - (cellSize/2), puffImages, animation_delay));
            if(coord[0]-1 >= 0)					//above
               explosions.add(new Explosion("SMALL", x - (cellSize/2), (int)(y - (cellSize*1.5)), puffImages, animation_delay));
            if(coord[0]+1<board.length)		//below
               explosions.add(new Explosion("SMALL", x - (cellSize/2), y + (cellSize/2), puffImages, animation_delay));
         }
      }
   
   //post:  does radius damage to any player or structure close to coordinate x, y in panel p, with a probability of starting a fire
   //			but skips the player at index skip
      public static void radiusDamage(int skip, int x, int y, int power, int p, double fireProb)
      {
         double impactRadius = cellSize*1.5;
         if(gameMode == EARTH_INVADERS)
            impactRadius = cellSize*.75;
         int damage = 0;
         for(int pl=0; pl<players.length; pl++)
         {
            Player current = players[pl];
            if(current == null || current.getName().equals("NONE") || pl==skip)
               continue;
            int cRow = current.getRow();
            int cCol = current.getCol();
            if(panel != p || cRow==0 || cCol==0 || cRow==board.length-1 || cCol==board[0].length-1)
               continue;
            if(gameMode == EARTH_INVADERS && (current instanceof Vehicle))
               continue;
            if(current.getName().startsWith("AIR"))
               continue;   
            double distance =  Utilities.distance(x, y, current.getX(), current.getY());
            if(distance <= impactRadius*2)
            {
               if((distance/1.5) > power)
                  damage = (int)(Math.random()*5);
               else
                  damage = power - (int)(distance/1.5);
               if(gameMode!=BOMBER_DODGER && current.isDigging())
               {	//less damage if we are underground
                  damage /= 2;
               }
               if(gameMode == BOMBER_DODGER)
                  current.damage((int)(damage*1.5));
               else
                  current.damage(damage);
               if(pl==0)
                  painTime = numFrames; 
            
               if(current instanceof Vehicle)
               {
                  current.damage(damage*2);
                  if(current.getHealth() <= 0)
                  {
                     explosions.add(new Explosion("SMALL", current.getX()-(cellSize/2), current.getY()-(cellSize/2), puffImages, animation_delay));
                     Utilities.updateKillStats(current);
                     Spawner.resetEnemy(pl);
                  }
               }
            }
         }
         for(int r=0;r<structures.length;r++)		//see if there is a structure
         {
            for(int c=0;c<structures[0].length;c++)
            {
               Structure str = structures[r][c][p];
               if(str != null && str.getHealth()!=0)		
               {
                  int bX = (cellSize*c);
                  int bY = (cellSize*r);
                  double distance = Utilities.distance(x, y, bX, bY);
                  if(distance <= impactRadius)
                  {
                     if((distance/3) > power)
                        damage = (int)(Math.random()*5);
                     else
                        damage = power - (int)(distance/3);
                     str.damage(damage);
                     if(str.getHealth() == 0)
                     {
                        if(gameMode != EARTH_INVADERS)
                           Spawner.removeFromSpawn(humanSpawnPoints[p], r, c); 
                        if(players[PLAYER1].getHealth() > 0)  
                        {
                           int value =  (int)(str.getPropertyValue());
                           propertyDamage += value;
                           if(p == 4)		//we only protect the center panel in CITY_SAVER
                              propertyValue -= value;
                           Utilities.updateKillStats(str);
                        }
                     }
                     else
                     {	//starts a fire
                        if(Math.random() < fireProb && !str.onFire())
                        {
                           str.setOnFire(true);
                        }
                     }
                  }
               }
            }
         }
      
      }
   
   
   //post: a-bomb has been dropped - damage all players and structures in this panel
      public static void nuke()
      {
         if(nuked)
            return;
         Sound.nuke();
         for(int r=0; r<structures.length; r++)
            for(int c=0; c<structures[0].length; c++)
               if(structures[r][c][panel]!=null && structures[r][c][panel].getHealth()>0)
               {
                  structures[r][c][panel].damage(1000);
                  int value = (int)(structures[r][c][panel].getPropertyValue());
                  propertyDamage += value;
                  if(panel == 4)		//we only protect the center panel in CITY_SAVER
                     propertyValue -= value;
                  Utilities.updateKillStats(structures[r][c][panel]);
               }
         for(int i=0; i<players.length; i++)
         {
            if(players[i]==null || players[i].getName().equals("NONE"))
               continue;
            if(i==0)
            {
               players[i].damage(1000);
               warningTime = numFrames; 
            }
            explosions.add(new Explosion("BIG", players[i].getX()-(cellSize/2), players[i].getY()-(cellSize/2), puffImages, animation_delay));
            if(i>0)
            {
               p2toggle = false;
               players[i] = new Player();//"NONE",  0, 0, bulletImages, animation_delay, 0, false, false, 0, 0, 0));
            }
         
         }
         numFrames = 0;		//use to make nuke effect
         nuked = true;
      }
   
   //post:  structures burn, fire spreads depending on wind and players are burnt if they are in a fire
      public static void manageFire()
      {
         for(int p=0; p<structures[0][0].length; p++)
         {
            for(int r=0;r<structures.length;r++)
            {
               for(int c=0;c<structures[0].length;c++)
               {
                  Structure str = structures[r][c][p];
                  if(str!=null && str.onFire() && numFrames%100==0)
                  {   
                     for(int i=0; i<players.length; i++)
                     {	//burn players that are standing in the fire
                        if(players[i]!=null && !players[i].getName().equals("NONE"))
                           Utilities.burnPlayer(players[i], r, c, p);
                     }
                  //25% chance wind spreads fire in the direction of the wind - 5% chance without the winds help
                     if(((windDirection == UP  && Math.random() < .25) || (Math.random() < .05)) && r-1 > 0 && structures[r-1][c][p]!=null && !structures[r-1][c][p].onFire())
                        structures[r-1][c][p].setOnFire(true);
                     else
                        if(((windDirection == DOWN  && Math.random() < .25) || (Math.random() < .05)) && r+1 < board.length-1 && structures[r+1][c][p]!=null && !structures[r+1][c][p].onFire())
                           structures[r+1][c][p].setOnFire(true);
                        else
                           if(((windDirection == LEFT  && Math.random() < .25) || (Math.random() < .05)) && c-1 > 0 && structures[r][c-1][p]!=null && !structures[r][c-1][p].onFire())
                              structures[r][c-1][p].setOnFire(true);
                           else
                              if(((windDirection == RIGHT  && Math.random() < .25) || (Math.random() < .05)) && c+1 < board[0].length-1 && structures[r][c+1][p]!=null && !structures[r][c+1][p].onFire())
                                 structures[r][c+1][p].setOnFire(true);
                                    
                     str.damage(2);
                     if(players[PLAYER1].getHealth() > 0)
                     {
                        propertyDamage += (int)(Math.random()*1000);
                     }
                     if(str.getHealth() == 0)
                     {
                        if(str.getName().startsWith("FUEL"))	//if we stomp a fuel depot, make it explode
                        {
                           bigExplosion(str.findX(cellSize), str.findY(cellSize), p);
                           radiusDamage(-1, str.findX(cellSize), str.findY(cellSize), 50, p, .5);
                        }
                        str.setOnFire(false);
                        if(gameMode != EARTH_INVADERS)
                           Spawner.removeFromSpawn(humanSpawnPoints[p], r, c);  
                        if(players[PLAYER1].getHealth() > 0) 
                        {
                           int value = (int)(str.getPropertyValue());   
                           propertyDamage += value;
                           if(p == 4)		//we only protect the center panel in CITY_SAVER
                              propertyValue -= value;
                           Utilities.updateKillStats(str);
                        }
                     }
                  }     
               }
            }
         }
      }
   
   }