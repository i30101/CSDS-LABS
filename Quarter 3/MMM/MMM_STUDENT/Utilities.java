//Mash, Mangle & Munch - Rev. Dr. Douglas R Oberle, June 2012  doug.oberle@fcps.edu
//MISCELANEOUS UTILITIES & FILE IO
import java.util.ArrayList;
import java.io.IOException;
import java.io.PrintStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.File;
import java.util.Scanner;
import java.awt.event.KeyEvent;

  //handles miscelaneous methods and file IO for highscores
public class Utilities extends MMMPanel {

    private static int  hungerTime = 500; //for every numFrames that is a muitiple of this, the monster gets hungrier
    private static int energyTime = 1000;  //for every numFrames that is a muitiple of this, Boobootrom drops energy

    //pre:  all arguments are valid pixel coordinates >= 1 && <= screen size
    //post: returns the distance between the two points to see if there is a collision between players
    public static double distance(int x1, int y1, int x2, int y2)
    {
        return Math.sqrt(Math.pow((x2-x1), 2) + Math.pow((y2-y1), 2));
    }

    //pre:  x1 - x2 != 0
    //post: returns true if the point (x,y) is within (cellSize/2) units from the ray defined by (x1,y1) & (x2, y2)
    public static boolean isPointOnRay(int x, int y, int x1, int y1, int x2, int y2)
    {
        return false;		//temporary value to keep things compiling
    }

    //pre:  word != null, word has been encoded with an encryption
    //post: returns the word decoded
    /**
     * 
     * @param word
     * @return
     */
    public static String decode(String word) {
        String decoded = "";
        String[] characters = word.split(" ");
        for (String character : characters) {
            decoded += Character.toString((char) Integer.parseInt(character));
        }
        return decoded;
    }

    //pre:  word != null, word has not been encoded with an encryption
    //post: returns the word encoded with an encryption
    /**
     * encryption scheme: first convert each character to ASCII
     * then turn into string and return all characters as one string
     * @param word
     * @return
     */
    public static String encode(String word) {
        String encoded = "";
        for (int i = 0; i < word.length(); i++) {
            char charAtIndex = word.charAt(i);
            Integer ascii = (int) charAtIndex;
            encoded += ascii.toString();
            if (i < word.length() - 1) {
                encoded += " ";
            }
        }
        System.out.println(encoded);
        return encoded;
    }


    //pre:  a and b are valid indecies of the array
    //post: nums[a] swaps values with nums[b]
    public static void swap(playerScore[] nums, int a, int b)
    {
        playerScore temp = nums[a];
        nums[a] = nums[b];
        nums[b] = temp;
    }

    //post: array is sorted in decending order
    public static void selSort(playerScore[] nums)
    {
        int max, size = nums.length;
        for (int i=0; i < size; i++)
        {
            max = i;
            for (int j = i + 1; j < size; j++)
            {
                if (nums[j].getDamage() > nums[max].getDamage())
                max = j;
            }
            swap (nums, i, max);
        }
    }

    //pre:  fileName exists as a text file and contains 5 lines of scores in the format of:  score name time (long String int)
    //post: fills up the hishScores array with data from fineName
    public static void readFile(String fileName,  playerScore [] scores)
    {
        try
        {
            Scanner input = new Scanner(new FileReader(fileName));
            int i=0;									//index for placement in the array
            long damage=0;
            String name="";
            int time=0;
            while (input.hasNextLine() && i < scores.length)		//while there is another line in the file
            {
                try
                {
                String sentence = decode(input.nextLine());
                String [] parts = sentence.split(" ");
                    
                damage=Long.parseLong(parts[0]);		//propertyDamage (score)
                name = parts[1];							//monster name
                time = Integer.parseInt(parts[2]);	//time expired
                }
                catch (java.util.InputMismatchException ex1)			//file is corrupted or doesn't exist - clear high scores and remake the file
                {
                for(int j=0; j < scores.length; j++)
                    scores[j] = new playerScore(0, "none", 0);
                writeToFile(scores, fileName);
                return;
                }			
                catch (java.util.NoSuchElementException ex2)			//file is corrupted or doesn't exist - clear high scores and remake the file
                {
                for(int j=0; j < scores.length; j++)
                    scores[j] = new playerScore(0, "none", 0);
                writeToFile(scores, fileName);
                return;
                }			
                scores[i++]= new playerScore(damage, name, time);			//add the line into the array
            }
            input.close();	
        }
        catch (IOException ex3)			//file is corrupted or doesn't exist - clear high scores and remake the file
        {
            for(int i=0; i < scores.length; i++)
                scores[i] = new playerScore(0, "none", 0);
            writeToFile(scores, fileName);
        }				
    }

    //post: writes to file highScores
    public static void writeToFile(playerScore[] array, String filename)
    {
        try
        {
            System.setOut(new PrintStream(new FileOutputStream(filename)));
            for(int i = 0; i < array.length; i++) 
                System.out.println(encode(array[i].toString()));
        }
        catch (IOException e)
        {
            System.out.println("File not written");
            System.out.println(e);
        } 
    }

    //post: inserts new highscore in to array if it is in the top 5
    public static void updateHighScores(playerScore[] nums)
    {
        selSort(nums);
        for(int i=nums.length -1; i>=0; i--)
        {
            long value = propertyDamage;
            if(gameMode == CITY_SAVER)
                value = propertyValue;
            if(value >= nums[i].getDamage())
            {
                nums[i] = new playerScore(value, players[PLAYER1].getName(), time);
                message = "HIGH SCORE ENTRY!";
                messageTime = numFrames; 
                scoresUpdated = true;
                break;
            }
        }
        selSort(nums);
    }

    //pre:  x & y are valid pixel coordinates in the game window
    //post: returns the row and col where those pixels are contained
    public static int[] findCoord(int x, int y)
    {
        int [] coord = new int[2];
        int r = 0; 
        int c = 0;

        r = y / cellSize;
        c = x / cellSize;

        if(r>=0 && r < board.length && c>=0 && c<board[0].length)
        {
            coord[0] = r;
            coord[1] = c;
        }
        return coord;
    }

    //pre:  curr!=null
    //post: returns true if curr can only travel on streets
    public static boolean streetTraveler(Player curr)
    {
        if(curr==null)
            return false;
        if(curr.getName().startsWith("CYCLE") || curr.getName().startsWith("CAR") || curr.getName().endsWith("artillery") || curr.getName().endsWith("missile"))
            return true;
        return false;
    }

    //pre:  name is a valid unit name
    //post: returns if name is a ground based civilian - used to direct their movement away from monsters
    public static boolean nonFlyingCivilian(String name)
    {
        return (!name.equals("AIR civilian") && !name.endsWith("newscopter") && (name.endsWith("civilian") || name.endsWith("bus")));
    }

    //pre:  name is a valid unit name
    //post: returns if name is an armed unit that can shoot
    public static boolean shootingVehicle(String name)
    {
        return (!name.endsWith("newscopter") && !name.endsWith("civilian") && !name.endsWith("bus") && !name.startsWith("TRAIN") && !name.endsWith("firetruck") && !name.endsWith("commerce"));
    }


    //post:  returns true if d1 and d2 are opposite directions (used to see if a monster AI can make a valid head turn
    public static boolean oppositeDirections(int d1, int d2)
    {
        return ((d1==UP && d2==DOWN) || (d1==LEFT && d2==RIGHT) || (d1==DOWN && d2==UP) || (d1==RIGHT && d2==LEFT));
    }

    //pre:  k is a valid key code
    //post:  returns true if k is a player 2 command (controls a 2nd player monster or vehicle)
    public static boolean p2Command(int k)
    {
        if(k==KeyEvent.VK_C || k==KeyEvent.VK_Z || k==KeyEvent.VK_X || k==KeyEvent.VK_W || k==KeyEvent.VK_S || k==KeyEvent.VK_D || k==KeyEvent.VK_A || k==KeyEvent.VK_G || k==KeyEvent.VK_E || k==KeyEvent.VK_T)
            return true; 
        return false;
    }

    //pre:  dir ends with one of the following: "up", "right", "down" or "left"
    //post: returns whether or not a player can turn their head a certain way with respect to which way their body is facing
    public static boolean isValidHeadTurn(Player curr, String dir)
    {
        if(curr.head360())	//some players can spin their heads all the way around (tanks, jeep's machine gun, Boobootron death ray)
            return true;
        int hd = curr.getHeadDirection();
        int bd = curr.getBodyDirection();
        if(dir.endsWith("right"))
            hd = (hd + 1) % 4;
        else
        {
            hd = (hd - 1);
            if(hd == -1)
                hd = 3;
        }
        if(oppositeDirections(bd, hd))
            return false;
        return true;
    }

    //pre:  dir ends with one of the following: "up", "right", "down" or "left"
    //post: changes head direction of player curr
    public static void turnHead(Player curr, String dir)
    {
        if(pause)
            return;
        int hd = curr.getHeadDirection();
        if(isValidHeadTurn(curr, dir))
        {
            if(dir.endsWith("right"))
                hd = (hd + 1) % 4;
            else
            {
                hd = (hd - 1);
                if(hd == -1)
                hd = 3;
            }
            curr.setHeadDirection(hd);
        }
    }

    public static void removeWebFromStructure(Structure str)
    {
        if(str==null || str.getWebValue()==0)
            return;
        int r = str.getRow();
        int c = str.getCol();
        int p = str.getPanel();   
        if(str.getWebValue() == 2)			//there is a web attached to the tower - remove it
        {
            for(int i=0; i<webs[p].size(); i++)
            {
                int[] temp = webs[p].get(i);
                if((r==temp[4] && c==temp[5]) || (r==temp[6] && c==temp[7]))
                {
                webs[p].remove(i);
                Structure str2 = structures[temp[4]][temp[5]][p];
                if(str2 != null)
                    str2.setWebValue(0);
                str2 = structures[temp[6]][temp[7]][p];
                if(str2 != null)
                    str2.setWebValue(0);
                break;
                }
            }
        }
        str.setWebValue(0);   
    }

    //post:  if there is an electrical tower in front of BoobooTron, it absorbs it to regain energy and the tower is removed from the map
    public static void absorbElecTower(Monster curr)
    {
        if(curr==null || curr.getName().equals("NONE"))
            return;
        int bDir = curr.getBodyDirection();
        int r = curr.getRow();
        int c = curr.getCol();
        if(bDir==UP && r>0)
        {
            Structure str = structures[r-1][c][panel];
            if(str!=null && str.getName().startsWith("ELEC"))
            {
                int healthBoost = (int)(Math.random()*21)+5;
                if(str.getName().endsWith("0"))	//large tower
                healthBoost = (int)(Math.random()*41)+5;
            
                message = "Absorbing energy!";
                messageTime = numFrames;
                explosions.add(new Explosion("SMALL", str.findX(cellSize)-(cellSize/2), str.findY(cellSize)-(cellSize/2), elecExplImages, animation_delay));
            
                updateKillStats(str);
                if(curr.getHealth() > 0)
                {
                int value = (int)(str.getPropertyValue());
                propertyDamage += value;
                if(panel == 4)		//we only protect the center panel in CITY_SAVER
                    propertyValue -= value;
                }
                structures[r-1][c][panel] = null;
            
                curr.setHealth(curr.getHealth()+healthBoost);
                board[r-1][c][panel] = "S--";
            }
        }
        else
            if(bDir==RIGHT && c<board[0].length-1)
            {
                Structure str = structures[r][c+1][panel];
                if(str!=null && str.getName().startsWith("ELEC"))
                {
                int healthBoost = (int)(Math.random()*11)+5;
                if(str.getName().endsWith("0"))	//large tower
                    healthBoost = (int)(Math.random()*31)+5;
                
                message = "Absorbing energy!";
                messageTime = numFrames;
                explosions.add(new Explosion("SMALL", str.findX(cellSize)-(cellSize/2), str.findY(cellSize)-(cellSize/2), elecExplImages, animation_delay));
                
                updateKillStats(str);
                if(curr.getHealth() > 0)
                {
                    int value = (int)(str.getPropertyValue());
                    propertyDamage += value;
                    if(panel == 4)		//we only protect the center panel in CITY_SAVER
                        propertyValue -= value;
                }
                structures[r][c+1][panel] = null;
                curr.setHealth(curr.getHealth()+healthBoost);
                board[r][c+1][panel] = "S--";
                }
            }
            else
                if(bDir==DOWN && r<board.length-1)
                {
                Structure str = structures[r+1][c][panel];
                if(str!=null && str.getName().startsWith("ELEC"))
                {
                    int healthBoost = (int)(Math.random()*11)+5;
                    if(str.getName().endsWith("0"))	//large tower
                        healthBoost = (int)(Math.random()*31)+5;
                
                    message = "Absorbing energy!";
                    messageTime = numFrames;
                    explosions.add(new Explosion("SMALL", str.findX(cellSize)-(cellSize/2), str.findY(cellSize)-(cellSize/2), elecExplImages, animation_delay));
                
                    updateKillStats(str);
                    if(curr.getHealth() > 0)
                    {
                        int value = (int)(str.getPropertyValue());
                        propertyDamage += value;
                        if(panel == 4)		//we only protect the center panel in CITY_SAVER
                            propertyValue -= value;
                    }
                    structures[r+1][c][panel] = null;
                    curr.setHealth(curr.getHealth()+healthBoost);
                    board[r+1][c][panel] = "S--";
                }
                }
                else
                if(bDir==LEFT && c>0)
                {
                    Structure str = structures[r][c-1][panel];
                    if(str!=null && str.getName().startsWith("ELEC"))
                    {
                        int healthBoost = (int)(Math.random()*11)+5;
                        if(str.getName().endsWith("0"))	//large tower
                            healthBoost = (int)(Math.random()*31)+5;
                    
                        message = "Absorbing energy!";
                        messageTime = numFrames;
                        explosions.add(new Explosion("SMALL", str.findX(cellSize)-(cellSize/2), str.findY(cellSize)-(cellSize/2), elecExplImages, animation_delay));
                    
                        updateKillStats(str);
                        if(curr.getHealth() > 0)
                        {
                            int value = (int)(str.getPropertyValue());
                            propertyDamage += value;
                            if(panel == 4)		//we only protect the center panel in CITY_SAVER
                            propertyValue -= value;
                        }
                        structures[r][c-1][panel] = null;
                        curr.setHealth(curr.getHealth()+healthBoost);
                        board[r][c-1][panel] = "S--";
                    }
                }
    }

    //post: monster tries to grab a car or person in an adjacent space (r,c)
    public static void grab(Monster curr)
    {
        if(curr==null || curr.getName().equals("NONE") || pause)		//player 2 might not be activated
            return;
        if(gameMode != MONSTER_MASH)
            return;   
        int bDir = curr.getBodyDirection();
        if(curr.energyAbsorber())				//Boobootron can only grab power stations and trains to get energy
            absorbElecTower(curr);
        for(int i=1; i<players.length; i++)
        {
            Player currTemp = players[i];
            if(currTemp==null || currTemp.getName().equals("NONE") || (currTemp instanceof Monster))
                continue;
        
            if(curr.isFlying() && !currTemp.isFlying())	//if we are flying, we can not grab ground units
                continue;
        
            Vehicle food = (Vehicle)(currTemp);
        
            int fR = food.getRow();  
            int fC = food.getCol();
            int r = curr.getRow();
            int c = curr.getCol();
            if(bDir==UP)
            {
                if((fR==r && fC==c) || (fR==r-1 && fC==c))
                {}	//must be on the same spot, or one row up
                else
                continue;
            }
            else
                if(bDir==RIGHT)
                {
                if((fR==r && fC==c) || (fR==r && fC==c+1))
                {}	//must be on the same spot, or one col right
                else
                    continue;
                }
                else
                if(bDir==DOWN)
                {
                    if((fR==r && fC==c) || (fR==r+1 && fC==c))
                    {}	//must be on the same spot, or one row down
                    else
                        continue;
                }
                else
                    if(bDir==LEFT)
                    {
                        if((fR==r && fC==c) || (fR==r && fC==c-1))
                        {}	//must be on the same spot, or one row left
                        else
                            continue;
                    }
            int fX = food.getX();
            int fY = food.getY();
            int cX = curr.getX();
            int cY = curr.getY();
            if(distance(cX, cY, fX, fY) > (cellSize))
                continue;
            String name = food.getName();
            if(curr.energyAbsorber())	//Boobootron can only grab power stations and trains to get energy  
            {
                if(name.startsWith("TRAIN"))
                {
                message = "Absorbing energy!";
                explosions.add(new Explosion("SMALL", fX-(cellSize/2), fY-(cellSize/2), elecExplImages, animation_delay));
                if(curr.getHealth() > 0)
                    propertyDamage += food.getPropertyValue();
                messageTime = numFrames;
                Spawner.resetEnemy(i);					//reset the enemy's position
                updateKillStats(food);
                curr.setHealth(curr.getHealth()+25);
                return;
                }
            }
            else
                if(curr.canGrabUnit(name))					//check to see if we can grab a unit of this type (in Monster.java)
                {
                message = "Gotcha!";
                messageTime = numFrames;
                if(curr.getHealth() > 0)
                    propertyDamage += food.getPropertyValue();
                Spawner.resetEnemy(i);					//reset the enemy's position
                updateKillStats(food);
                curr.grabUnit(name);
                if(curr.cantGrab())                 //monsters that can't grab a unit will automatically eat them
                {
                    curr.eatUnit();
                }        			
                return;
                }
        }
    }

    //pre:   curr != null
    //post:  for a shooting vehicle within one cell of curr, set their mind control status to true so that they attack teammates
    public static void mindControlUnits(Monster curr)
    {
        if(curr==null || curr.getName().equals("NONE") || !curr.isMindControl())
            return;
        boolean needToReload = false;
        if((numFrames > curr.getReloadTime() && numFrames < curr.getLastShotTime() + curr.getReloadTime()))
            needToReload = true;
        if(needToReload)          
        {  
            if(curr==players[PLAYER1])
            {
            
                message = curr.reloadingMessage();
                messageTime = numFrames;
            }
            return;
        }
        for(int i=0; i<players.length; i++)
        {
            Player vehicle = players[i];
            if(vehicle==null || !(vehicle instanceof Vehicle))	
                continue;
            if(!Utilities.shootingVehicle(vehicle.getName()))		//we can only mind control units that shoot
                continue;
            int x1 =curr.findX(cellSize);			   //these return the x and y coordinate in pixel space of the monster player
            int y1 =curr.findY(cellSize);
            int x2 = vehicle.findX(cellSize);		//these return the x and y coordinate in pixel space of the player
            int y2 = vehicle.findY(cellSize);
        
            if(Utilities.distance(x1, y1, x2, y2) <= (cellSize) && !(((Vehicle)(vehicle)).isMindControlled()))
            {
                ((Vehicle)(vehicle)).setMindControlled(true);
                Sound.shriekAttack();
                curr.setLastShotTime(numFrames);
                message = "Yes, my queen?";
                messageTime = numFrames;
                                        //exerting energy to mind control units makes us hungry
                if(curr.getHunger() < hungerLevels.length-1 && Math.random() < .5)
                curr.setHunger(curr.getHunger()+1);
                break;                              //only control one unit at a time
            }  
        }
    }

    //post:  remove mind control status from all units
    public static void breakMindControl()
    {
        for(int i=0; i<players.length; i++)
        {
            Player vehicle = players[i];
            if(vehicle==null || !(vehicle instanceof Vehicle) || !(((Vehicle)(vehicle)).isMindControlled()))	
                continue;
            ((Vehicle)(vehicle)).setMindControlled(false);
            message = "LINK SEVERED!";
            messageTime = numFrames;
        }
    }

    //post:  monster jumps/stomps on structures
    public static void monsterStomp(Monster curr, int playerIndex)
    {
        if(pause || playerIndex<0 || playerIndex>=players.length)
            return;
        if(curr == null || curr.getName().equals("NONE"))
            return;
        int r = curr.getRow();
        int c = curr.getCol();
        if(curr.isFlyer() && gameMode!=EARTH_INVADERS)
        {
            if(!curr.isFlying())
            {
                curr.setIsFlying(true);
                curr.setAnimationDelay(curr.getAnimationDelay()/3);
            }
            else
            {
                curr.setIsFlying(false);
                curr.setAnimationDelay(curr.getAnimationDelay()*3);
            }
            if(!curr.isFlying())									//only stomp if we are landing
            {  
                Ordinance.radiusDamage(playerIndex, curr.findX(cellSize), curr.findY(cellSize), 50, panel, .15);
                if(board[r][c][panel].startsWith("#"))		//landing on electric lines or power station - instant death
                {
                explosions.add(new Explosion("BIG", curr.getX()-(cellSize/2), curr.getY()-(cellSize/2), elecExplImages, animation_delay));
                message = "ZZZZAP!";
                curr.setHealth(0);
                return;
                }
                else
                {
                Ordinance.bigExplosion2(curr.findX(cellSize), curr.findY(cellSize), panel);
                
                //destroy any bridge that is close to where we land  
                if(board[r][c][panel].startsWith("S~") || board[r][c][panel].equals("TU2") || board[r][c][panel].equals("TR2"))		
                {//if we land on a bridge, destroy it (change it to water)
                    board[r][c][panel] = "~~~";
                    if(structures[r][c][panel]!=null)	//there might be Blop-Glop there to remove
                        structures[r][c][panel]=null;
                    if(players[PLAYER1].getHealth() > 0)
                        propertyDamage += 5000000;
                }
                }
            }
            return;
        }
        curr.setIsJumping(true);
        Structure str = structures[r][c][panel];
        if(str!=null && str.isDestroyable()) 
        {
            if(str.getHealth() != 0)
            {
                str.damage(((Monster)(curr)).getStompPower());
                if(str.getHealth() == 0)
                {
                if(str.getName().startsWith("FUEL"))	//if we stomp a fuel depot, make it explode
                {
                    Ordinance.bigExplosion(str.findX(cellSize), str.findY(cellSize), panel);
                    Ordinance.radiusDamage(-1, str.findX(cellSize), str.findY(cellSize), 50, panel, .5);
                }
                if(players[PLAYER1].getHealth() > 0)
                {
                    int value = (int)(str.getPropertyValue());
                    propertyDamage += value;
                    if(panel == 4)		//we only protect the center panel in CITY_SAVER
                        propertyValue -= value;
                    updateKillStats(str);
                    if(str.getName().endsWith("LANDMARK"))
                    {	//if we destroyed a landmark, elevate the threat level
                        if(threatLevel < 4)
                            threatLevel++;
                    }
                }
                if(gameMode != EARTH_INVADERS)
                    Spawner.removeFromSpawn(humanSpawnPoints[panel], r, c);   
                }
            }
        }
        if(board[r][c][panel].startsWith("S~") || board[r][c][panel].equals("TU2") || board[r][c][panel].equals("TR2"))		
        {//if we stomp a bridge, destroy it (change it to water)
            board[r][c][panel] = "~~~";
            if(structures[r][c][panel]!=null)	//there might be Blop-Glop there to remove
                structures[r][c][panel]=null;
            if(players[PLAYER1].getHealth() > 0)
                propertyDamage += 5000000;
        }
    }

    //post:  if there is a monster at row r, col c, panel p, they get burned
    public static void burnPlayer(Player curr, int r, int c, int p)
    {
        if(curr==null || curr.getName().equals("NONE") || curr.isFlying())
            return;
        if(curr.getRow()==r && curr.getCol()==c && panel==p)	//burn players that are standing in the fire
        {
            if(curr==players[PLAYER1])
            {
                painTime = numFrames; 
                message = "BURNING!";
                messageTime = numFrames;
            } 
            String name = curr.getName();
            if(curr instanceof Monster)
                curr.damage(((Monster)(curr)).burnDamage());
            else
                curr.damage(100);
        }
    }


    //post:  removes any destroyed Blop-glop from the array of structures
    public static void clearGlop()
    {
        for(int r=0;r<structures.length;r++)
        {
            for(int c=0;c<structures[0].length;c++)
            {
                for(int p=0; p<structures[0][0].length; p++)
                {
                Structure curr = structures[r][c][p];
                if(curr!= null && curr.getName().startsWith("Blop") && curr.getHealth()==0)
                    structures[r][c][p] = null;
                }
            }
        }
    }

    public static void monsterEat(Monster curr)
    {
        if(pause)
            return;
        if(curr.getHealth() <= 0)
            return;   
        if(curr.energyAbsorber())			//Boobootron doesn't eat, so just make him grab what is there	
        {
            grab(curr);
            return;
        }
        String[] contents = curr.getClawContents();
        int index = -1;		//index of claw contents that are full
        if(!contents[0].equals("empty"))
            index = 0;
        else
            if(!contents[1].equals("empty"))
                index = 1;
        if(curr.isAmbidextorous())
        {	//if they are holding a CROWD, prioritize eating them
            if(contents[0].startsWith("CROWD"))
                index = 0;
            else
                if(contents[1].startsWith("CROWD"))
                index = 1;
        } 
        if(index >= 0)
        {
            message = "MUNCH!";
            messageTime = numFrames;
            curr.eatUnit();
        }
        else
        {
            message = "Nothing to eat!";
            messageTime = numFrames;
        }
    }

    //post:  automatically stomps on any structrure with a small enough health when moved over it
    public static void walkDamage(Monster curr, int r, int c, int p)
    {
        if(pause || curr==null || curr.getName().equals("NONE") || curr.isFlying())
            return;
        int strHealth = curr.walkDamage();

        if(strHealth > 0)
        {   
            Structure str = structures[r][c][p];
            if(str!=null && str.isDestroyable() && str.getHealth() < strHealth && !str.getName().startsWith("Blop")  && !str.getName().equals("hole")) 
            {
                if(str.getHealth() != 0)
                {
                str.damage(curr.getStompPower());
                if(str.getHealth() == 0)
                {
                    if(str.getName().startsWith("FUEL"))	//if we stomp a fuel depot, make it explode
                    {
                        Ordinance.bigExplosion(str.findX(cellSize), str.findY(cellSize), p);
                        Ordinance.radiusDamage(-1, str.findX(cellSize), str.findY(cellSize), 50, p, .5);
                    }
                    if(players[PLAYER1].getHealth() > 0)
                    {
                        int value = (int)(str.getPropertyValue());
                        propertyDamage += value;
                        if(p == 4)		//we only protect the center panel in CITY_SAVER
                            propertyValue -= value;
                        updateKillStats(str);
                    }
                    if(gameMode != EARTH_INVADERS)
                        Spawner.removeFromSpawn(humanSpawnPoints[p], r, c);   
                }
                }
            }
        }
    }

    //pre: curr is a vehicle player or a structure
    //post:add to kill count for each unit type - crowds, cars, boats, aircraft, buildings and possibly update the threat level
    public static void updateKillStats(Entity curr)
    {
        if(curr==null || curr.getName().equals("NONE"))
            return;
        String name = curr.getName();
        if(name.startsWith("CROWD"))
            killStats[0]++;
        else
            if(name.startsWith("CYCLE") || name.startsWith("CAR") || name.startsWith("TRAIN")  || name.startsWith("TANK"))
                killStats[1]++;
            else
                if(name.startsWith("BOAT"))
                killStats[2]++;
                else
                if(name.startsWith("AIR"))
                    killStats[3]++;
                else
                    if(curr instanceof Structure)
                    {
                        Structure str = (Structure)(curr);
                        removeWebFromStructure(str);
                        if(name.startsWith("BLDG") || name.startsWith("FUEL") || name.startsWith("ELEC"))
                        {
                            int p = str.getPanel();
                            killStats[4]++;
                            if(name.startsWith("BLDG"))
                            numStructures[p]--;
                            if(name.indexOf("WATER") >= 0)	//water tower - extinguish close fires
                            {
                            Sound.waterExplosion();
                            int sr = str.getRow();
                            int sc = str.getCol();
                            for(int r=sr-2; r<=sr+2; r++)
                                for(int c=sc-2; c<=sc+2; c++)
                                {
                                    if(r<1 || c<1 || r>board.length-1 || c>board[0].length-1)
                                        continue;
                                    if(structures[r][c][p]!=null && structures[r][c][p].onFire())
                                        structures[r][c][p].setOnFire(false);
                                }
                            }
                            else
                            {
                            Sound.buildingFall();
                            }
                        }     
                    }
        int sum=0;  
        for(int i=0; i<killStats.length-1; i++)
            sum += killStats[i];
        killStats[5] = sum;    
    //update threat level 0-5 (mystery, police, national guard, military, global)
        if(gameMode==BOMBER_DODGER)
            threatLevel = 4; 
        else
            if(gameMode==EARTH_INVADERS)
                threatLevel = 3;
            else
                if(sum < 15 && threatLevel < 0)
                threatLevel = 0;
                else
                if(sum < 30)
                {
                    if(threatLevel==0)
                    {
                        Sound.threatResponse();
                        message = "Police respond!";
                        messageTime = numFrames; 
                    }
                    if(threatLevel < 1)
                        threatLevel = 1;   
                }
                else
                    if(sum < 90)
                    {
                        if(threatLevel==1)
                        {
                            Sound.threatResponse();
                            message = "Natl Guard arrives!";
                            messageTime = numFrames; 
                        }
                        if(threatLevel < 2)
                            threatLevel = 2;  
                    }
                    else
                        if(sum < 150)
                        {
                            if(threatLevel==2)
                            {
                            Sound.threatResponse();
                            message = "Army responds!";
                            messageTime = numFrames; 
                            }
                            if(threatLevel < 3)
                            threatLevel = 3;   
                        }
                        else
                        {
                            if(threatLevel==3)
                            {
                            Sound.titleScreen();
                            message = "Govt approves A-Bomb!";
                            messageTime = numFrames; 
                            }
                            if(threatLevel < 4)
                            threatLevel = 4; 
                        }
    }

    //pre:   index is a valid index of players array
    //post:  update hunger and health status (for drowning and the like) for a monster
    //			any player 2 monsters are removed if they die
    public static void updateMonsterStatus(int index)
    {
        if (index < 0 || index >= players.length)
            return;
        Player curr = players[index];
        if(curr==null || curr.getName().equals("NONE"))
            return;
        if(gameMode == CITY_SAVER)
        {
            boolean allDead = true;  
            for(int i=AIMONSTER1; i<= AIMONSTER4; i++)
                if(players[i]!=null && players[i].getHealth() > 0)
                allDead = false;
            if(allDead)
            {
                monsterWins = true;
                curr.setHealth(0);
                message = "YOU WIN!"; 
                Sound.win();
                return;
            }
            if(index == PLAYER1)
            {
                int pointBoost = difficulty;
                if(difficulty == FREEROAM)		//FREE ROAM mode has same point boost as nightmare
                pointBoost = NIGHTMARE;
                if(numMonstersKilled >= (pointBoost+1))
                {
                monsterWins = true;
                curr.setHealth(0);
                message = "YOU WIN!";
                //channels[0].programChange(instr[ORCH_HIT].getPatch().getProgram());
                //channels[0].noteOn(42, 100);
                }
                return;
            }
        }
        else
            if(gameMode==EARTH_INVADERS)
            {
                if(EI_vehicles <= 0)
                {
                int pointBoost = difficulty;
                if(difficulty == FREEROAM)		//FREE ROAM mode has same point boost as nightmare
                    pointBoost = NIGHTMARE;
                MapBuilder.generateMap();
                if(players[PLAYER1].getHealth() > 0)		//stage clear bonus
                {
                    propertyDamage += 1000000*(pointBoost+1);
                    Sound.win();
                }
                players[PLAYER1].setHealth(players[PLAYER1].getHealth() + 25);
                if(players[PLAYER2]!=null && !players[PLAYER2].getName().equals("NONE"))
                    players[PLAYER2].setHealth(players[PLAYER2].getHealth() + 25);
                if(difficulty != NIGHTMARE)
                    difficulty++;
                Spawner.initialVehicleSpawn(4);
                }
            }
            else
            {
                for(int p=0; p<numStructures.length; p++)
                {	//check all panels to see if we clear one (because we might be in another panel when it reaches the level of uninhabitabe because of fire)
                if(numStructures[p] >= 0 && numStructures[p] <= 2)
                {	//we rendered a panel uninhabitable - give monster a bonus
                    numStructures[p] = -1;		//flag so we don't continually get a bonus for every frame we are in the panel
                    curr.setHealth(curr.getHealth()+25);
                    propertyDamage += 100000000;
                    numPanelsCleared++;
                    int r=0;
                    int c=0;
                    if(p==1 || p==2)
                        c+=p;
                    else
                        if(p>=3 && p<=5)
                        {
                            r=1;
                            c=p-3;
                        }
                        else
                            if(p>=6 && p<=8)
                            {
                            r=2;
                            c=p-6; 
                            }     
                    panelState[c][r] = 1;
                    if((difficulty == EASY && numPanelsCleared >= 1) || (difficulty==MEDIUM && numPanelsCleared >= 3) || (difficulty==HARD && numPanelsCleared >= 6) || numPanelsCleared >=9)
                    {
                        monsterWins = true;
                        curr.setHealth(0);
                        Sound.win();
                        message = "YOU WIN!";
                    }
                    else
                    {
                        Sound.win();
                        message = "Destruction Bonus!";
                    }
                    messageTime = numFrames; 
                }
                }
            }
        boolean currDies = false;
    //if player1 has a twin and dies, kill the twin as well
        if(blopSplit &&  players[PLAYER1].getHealth() <= 0 && index == BLOPSPLIT)
        {
            currDies = true;
        }
        if ((index==PLAYER2 || index == AIPLAYER || index == BLOPSPLIT) && curr.getHealth() <= 0)
            currDies = true;
        else
            if(gameMode == CITY_SAVER && index>=AIMONSTER1 && index<=AIMONSTER4 && curr.getHealth() <= 0)
                currDies = true;
        if(currDies)
        {	//p2 monster partner is dead - remove them
            Ordinance.bigExplosion( players[index].getX(),  players[index].getY(), panel);
            Sound.monsterDie();
            if(index==PLAYER2)
            {
                players[index] = new Player();
                p1partner = false;
                message = "Partner Dies!";
            }
            else
                if(p3toggle && index == AIPLAYER)
                {
                players[index] = new Player();
                p3toggle = false;
                message = "Partner Dies!";
                }
                else
                if(blopSplit && index == BLOPSPLIT)
                {
                    players[index] = new Player();
                    blopSplit = false;
                    message = "Twin Dies!";
                }
                else
                    if(gameMode == CITY_SAVER && index>=AIMONSTER1 && index<=AIMONSTER4)
                    {
                        players[index] = new Player();
                        numMonstersKilled++;
                        message = "Monster Killed!";
                        propertyValue += 100000000;
                        Sound.win();
                    }
            messageTime = numFrames; 
            return;
        }
        if(index==AIPLAYER || index==BLOPSPLIT || (gameMode == CITY_SAVER && index>=AIMONSTER1 && index<=AIMONSTER4) || gameMode==EARTH_INVADERS)
        {}		//AI monster doesn't get hungry
        else
            if(gameMode == MONSTER_MASH && numFrames%energyTime==0)
            {	
                if(curr.energyAbsorber())	//Boobootron doesn't get hungry, but continues to drop health
                {
                curr.damage((int)(Math.random()*3)+1);	
                painTime = numFrames; 
                Sound.robotHurt();
                }
            }
            else
                if(numFrames%hungerTime==0)
                {	
                if(gameMode == MONSTER_MASH)			//BOMBER_DODGER and CITY_SAVER don't have to worry about food
                {
                    int hunger = ((Monster)(curr)).getHunger();
                    if(hunger < hungerLevels.length-1)
                    {
                        if(curr.lessAppetite())			//Gobzilly doesn't get hungry as fast
                        {
                            if( Math.random() < .5)
                            ((Monster)(curr)).setHunger(hunger+1); 
                        }
                        else
                            ((Monster)(curr)).setHunger(hunger+1); 
                    }
                    else
                    {
                        curr.damage(5);													//weakened from hunger
                        painTime = numFrames; 
                        Sound.monsterHungry();
                    }
                }  
                } 
            if(board[curr.getRow()][curr.getCol()][panel].startsWith("~") && !curr.isFlying())	//we are in the water
            {
                if(curr.damageInWater() && numFrames%250==0)	//gorilla, bug and robot lose health in water
                {
                    curr.damage((int)(Math.random()*8)+3);
                    message = "DROWNING!";
                    messageTime = numFrames; 
                    painTime = numFrames; 
                }
                else
                    if(curr.energyAbsorber() && numFrames%125==0)
                    {
                        curr.damage((int)(Math.random()*16)+5);
                        message = "SHORT CIRCUITING!";
                        messageTime = numFrames;  
                        painTime = numFrames; 
                    }
                    else																		//Gobzilly heals in water if not hungry
                        if(curr.healInWater() && numFrames%500==0 && ((Monster)(curr)).getHunger() < hungerLevels.length -1)	
                        {
                            curr.setHealth(curr.getHealth()+(int)(Math.random()*8)+3);
                            message = "HEALING!";
                            messageTime = numFrames; 
                        }
            }
    }

    //pre:  fileName is the name of a valid file in the maps folder comprised of the list of valid map file names
    //post: fills up list with each map file name from fileName
    public static void readFileNames(String fileName, ArrayList<String> list)
    {
        try
        {
            java.util.Scanner input = new java.util.Scanner(new FileReader(fileName));
            while (input.hasNextLine())		//while there is another value in the file
            {
                try
                {
                String filename = input.next();
                list.add(filename);
                }
                catch (java.util.InputMismatchException ex1)			//file is corrupted or doesn't exist
                {
                message = "Error loading "+fileName;
                messageTime = numFrames;
                return;
                }			
                catch (java.util.NoSuchElementException ex2)			//file is corrupted or doesn't exist
                {
                message = "Error loading "+fileName;
                messageTime = numFrames;
                return;
                }	
            }
            input.close();	
        }
        catch (IOException ex3)			//file is corrupted or doesn't exist - clear high scores and remake the file
        {
            System.out.println(fileName+" does not exist");
            return;
        }		
    }

    //pre:  fileName exists as a text file and contains 3 character string descriptions for the terrain tiles in a map
    //the name of the file is the city name followed by the panel number, 
    //i.e. "Washington4.txt" is the center panel, where "Washington0.txt" is the top left panel, and "Washington8.txt" is the bottom right panel
    //the values in the file can be from the following set:
    //{"PK1","PK2","PK3","SR1","SU1","SX1","SX2","SX3","TR1","TU1","TXR","TXU","TR2","TU2","S~1","S~2","~~~","MD2","MD1","#EH","S--"}
    //post: fills up the board array with data from fileName
    public static boolean readFileToBoard(String fileName)//, boolean forceToCenterPanel)
    {
        if(fileName==null || fileName.length() == 0)
            return false;
        int p = panel;																			//panel number		      
        char last = fileName.charAt(fileName.length()-1);      
        if(Character.isDigit(last))// && !forceToCenterPanel)
        {
            int num = Integer.parseInt(""+fileName.charAt(fileName.length()-1));//extract panel number from the filename
            if(num>=0 && num <=8)				//there is no panel number 9
                p = num;
        }
    //clear structures on that panel
        for(int r=0; r < structures.length; r++)  
            for(int c=0; c < structures[0].length; c++)
                structures[r][c][p] = null;

        fileName = "maps/"+fileName+".txt";
        try
        {
            java.util.Scanner input = new java.util.Scanner(new FileReader(fileName));
            String value = "";
            for (int r=0; r<board.length && input.hasNext(); r++)
                for (int c=0; c<board[0].length && input.hasNext(); c++)
                {
                try
                {
                    value = input.next();
                }
                catch (java.util.NoSuchElementException ex2)			//file is corrupted or doesn't exist
                {
                    message = "Error loading map";
                    messageTime = numFrames;
                    return false;
                }	
                catch (java.lang.ArrayIndexOutOfBoundsException ex3)			//file is corrupted or doesn't exist
                {
                    message = "Error loading map";
                    messageTime = numFrames;
                    return false;
                    
                }	
                board[r][c][p] = value;
                }  
            while(input.hasNextLine())
            {
                value = input.nextLine();
                int index = value.indexOf(":");
                if(value.length() > 0 && index >= 0)	//structures written in the file should have a : after the structure name
                {
                String name = value.substring(0, index).trim();
                value = value.substring(index+1);
                if(value.length() > 0 && value.indexOf(" ") >= 0)
                {
                    String [] parts = value.split(" ");
                    if(parts.length == 10)
                    {
                        int row = Integer.parseInt(parts[0]);
                        int col = Integer.parseInt(parts[1]);
                        int pan = Integer.parseInt(parts[2]);
                        int animDelay = Integer.parseInt(parts[3]);
                        boolean isPassable = false;
                        boolean isDestroyable = false;
                        if(parts[4].equals("true"))
                            isPassable = true;
                        if(parts[5].equals("true"))
                            isDestroyable = true;
                        int height = Integer.parseInt(parts[6]);
                        int health = Integer.parseInt(parts[7]);
                        int imageIndex = Integer.parseInt(parts[8]);
                        long propertyValue = Long.parseLong(parts[9]);
                        String[][][] strImages = null;
                        if(name.equals("TREES"))
                            strImages = treeImages;
                        else if (name.endsWith("HOUSES"))
                            strImages = houseImages;
                        else if (name.endsWith("WATER TOWER"))
                            strImages = waterTowerImages;
                        else if (name.startsWith("ELEC"))
                            strImages = elecTowerImages;
                        else if (name.endsWith("GAS STATION"))
                            strImages = gasStationImages;
                        else if (name.equals("FUEL DEPOT"))
                            strImages = fuelDepotImages;
                        else if (name.endsWith("BUISNESS"))
                            strImages = buisnessImages;
                        else if (name.endsWith("HIGHRISE"))
                            strImages = buildingImages;
                        else if (name.endsWith("TOWER"))
                            strImages = skyscraperImages;
                        else if (name.endsWith("LANDMARK"))
                            strImages = landmarkImages;
                        else if (name.endsWith("CASINO"))
                            strImages = casinoImages;
                        else if (name.endsWith("RIDE"))
                            strImages = rideImages;
                    //ARGS:  name, row, col, panel, image collection, animation delay, is passable?, is destroyable?, structure height, health, image size, image index, property value
                        if(strImages != null)
                            structures[row][col][p] = new Structure(name, row, col, p, strImages, animDelay, isPassable, isDestroyable, height, health, cellSize, imageIndex, propertyValue);
                    }
                }
                }
            }  
            input.close();	
        }
        catch (IOException ex3)			//file is corrupted or doesn't exist 
        {
            message = "Error loading map";
            messageTime = numFrames;
            return false;
        }	
        return true;
    }

    //pre:  fileName is the name of the user created map and is unique to the list of files in fileList.txt
    //the name of the file is the city name followed by the panel number, 
    //i.e. "Washington4.txt" is the center panel, where "Washington0.txt" is the top left panel, and "Washington8.txt" is the bottom right panel
    //post: Writes the user created board to the file
    public static void writeToFile(String fileName)
    {
        PrintStream imageWriter = null;
        int pan = panel;
        char last = mapFileName.charAt(mapFileName.length()-1);
        if(Character.isDigit(last))
            pan = Integer.parseInt(""+last);
        if(pan != panel && pan>=0 && pan<=8)				//make sure that the structures go into the panel specified by the last character of the fileName	
        {
            for(int r=0; r < structures.length; r++)  
                for(int c=0; c < structures[0].length; c++)
                if(structures[r][c][panel]!= null)
                    structures[r][c][panel].setPanel(pan);
        }   
        File imageFile = new File("maps/"+fileName+".txt");
        if(imageFile.exists())		//make sure to write over old file
        {
            imageFile.delete();
            imageFile = new File("maps/"+fileName+".txt");
        }

        while(imageWriter == null){
            try{
                imageWriter = new PrintStream(new FileOutputStream(imageFile, true));
            }
            catch(Exception E){
                System.exit(42);
            }
        }
        for(int r = 0; r < board.length; r++)
        {
            String terrain = "";
            for(int c = 0; c < board[0].length; c++)
            {
                terrain += board[r][c][panel];
                if(c + 1 != board[0].length)
                terrain += " ";
            }
            imageWriter.println(terrain);
        }
        imageWriter.println();
        for(int r=0; r < structures.length; r++)  
            for(int c=0; c < structures[0].length; c++)
                if(structures[r][c][panel] != null)
                imageWriter.println(structures[r][c][panel]);
        imageWriter.close();
    }

    //pre:  fileName is the name of the file that contains the list of unique to the list of files (fileList.txt)
    //       toAdd is the name of the file that needs to be added to the fileList
    //post: adds toAdd to the list of files in fileName if it is not already there
    public static void updateFileList(String fileName, String toAdd)
    {
        if(mapList.contains(toAdd))
            return;
        PrintStream imageWriter = null;
        File imageFile = new File("maps/"+fileName+".txt");
        
        while(imageWriter == null){
            try{
                imageWriter = new PrintStream(new FileOutputStream(imageFile, true));
            }
            catch(Exception E){
                System.exit(42);
            }
        }
        imageWriter.println();
        imageWriter.print(toAdd);
        imageWriter.close();
    }


    //post:  looks in collection of map files to see if there are any cities for which there is a file for each panel
    //			if so, the city is added to a list
    public static void findCompleteCities()
    {
        for(String fname: mapList)
        {
            if(fname.length() > 1)
            {
                char last = fname.charAt(fname.length() -1);
                if(Character.isDigit(last))
                { //record all of the panel numbers recorded at the end of each map that starts with this name
                //if we have all 9, then add that name to the cityList
                ArrayList<Integer> panelNums = new ArrayList();
                String name = fname.substring(0, fname.length() - 1);
                if(cityList.contains(name))
                    continue;
                for(String curr:mapList)
                {
                    if(curr.startsWith(name))
                    {
                        char last2 = curr.charAt(curr.length() -1);
                        if(Character.isDigit(last2))
                            panelNums.add(Integer.parseInt(""+last2));
                    }
                }
                boolean hasAll = true;
                for(int i=0; i<=8; i++)
                    if(!panelNums.contains(i))
                    {
                        hasAll = false;
                        break;
                    }
                if(hasAll)
                    cityList.add(name);      
                }
            } 
        }
    }
}