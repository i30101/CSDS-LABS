//Mash, Mangle & Munch - Douglas R Oberle, 2023  doberle@fcps.edu.
//GAME PANEL
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.*;

import java.util.ArrayList;

import java.io.IOException;


//TO DO:
// add descriptions in the character select screen for take your chances and map maker
//	figure out processor speed - adjust the delay accordingly.
// figure out display resolution and size - adjust the game window and resolution accordingly
// attempt depth perception - add row number to size of panels, structures and players.
// BUG: in Earth Invaders, if you kill the top unit furthest to the side when the formation drops to the next row, the enemy unites freeze.
// BUG: sometimes when writing new elements to the fileList, copies are made.
// when blop-glop is freed up by means of fire or explosion, restore spawn points.	
// read in options file for adjustable mouse-wheel speed, etc.
// small jump in move-player-smoothly when traversing from one cell to the next (easiet seen in jet fighters).
public class MMMPanel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener {	
    protected static boolean SHOWBORDER = true;			//show or hide borders for debugging AI movement
    protected static boolean mapMaker;						//are we in map-making mode?
    protected static boolean monsterMaker;					//are we in monster-maker mode?
    protected static String mapFileName;					//name of map to be saved with the map maker
    protected static ArrayList<String> mapList;			//list of map file names to be read in
    protected static ArrayList<String> fileQue;			//list of map file names to be loaded when game starts
    protected static ArrayList<String> cityList;			//list of all cities in map folders where there is a file for every panel - will be added to city selection list
    protected static int cityListIndex;						//for traversing through cityList
    protected static boolean textMode;						//are we entering text to save a map file name?
    protected static boolean titleScreen;					//display title screen at start(true) until the user hits key or button (false)    
    protected static boolean gameStarted;					//true if monster has been selected to start the game
    protected static boolean pause;							//toggle pause game

    protected static int difficulty;							//difficulty level 0, 1, 2, 3 or 4
    protected static final int EASY = 0;					//clear 1 panel
    protected static final int MEDIUM = 1;					//clear 3 panels
    protected static final int HARD = 2;					//clear 6 panels
    protected static final int NIGHTMARE = 3;				//clear 9 panels
    protected static final int FREEROAM = 4;				//play until you drop

    protected static int gameMode;							//what game mode to play
    protected static final int MONSTER_MASH = 0;			//destroy the city
    protected static final int BOMBER_DODGER = 1;		//evade the bombers - don't have to worry about food
    protected static final int CITY_SAVER = 2;			//you are a vehicle - destroy the monsters to limit damage
    protected static final int EARTH_INVADERS = 3;		//like space invaders
    protected static int numPanelsCleared;					//# panels with all habitable structures destoryed
    protected static int[][] panelState;					//0 if not cleared, 1 if cleared
    protected static int numMonstersKilled;				//for CITY SAVER mode, counts the # monsters you have killed for win game scenario
    protected static boolean p1partner;						//is there a 2nd player as a monster?
    protected static boolean p2toggle;						//is there a 2nd player as a vehicle?
    protected static boolean p3toggle;						//is there a 2nd AI monster?
    protected static boolean blopSplit;						//has the blop divided into two beings?
    protected static int cellSize;							//size of cell being drawn in pixels

    private static int delay;						//#miliseconds delay between each command and screen refresh (for the timer)
    protected static final int SPEED=1;			//when players moves from one cell to the next, this is how many pixels they move in each frame (designated by the timer)
    protected static int animation_delay;		//the speed at which the animations occur for the players
    protected static int numFrames;				//count the # of time frames to adjust speed in water vs land as well as message time

    //***BOARD/MAP BUILDING
    protected static String[][][] board;				//we will fill with descriptions [row][col][panel]
    protected static int panel;							//so we know which screen we are on
    protected static boolean[] trainInPanel;			//flags to see if there is a train in the panel (so we can make only one per panel)
    protected static ArrayList<Integer> trackPanels;//contains panels that will have rr tracks
    protected static boolean forceTracks;				//should we force train tracks on the map (for Boobootron and other energy absorbers)
    protected static boolean forceWater;				//should we force water on the map (for Gobzilly and others that heal in water)
    protected static ArrayList<Integer> waterPanels;//contains the panel #s that might have a body of water it
    protected static boolean isRiver;					//is it a river or a beach (used for in game map)
    protected static boolean horizWater;				//true if water goes across horizontal panels (east-west river or beach) (used for in game map)
    protected static ArrayList<Integer> suburbPanels;//panel #s that contain suburbs
    protected static ArrayList<Integer> landmarkPanels;	//records the panel # the landmark is in (used for in game map)
    //players and player array indexes (indicies?)
    protected static Player[] players;					//array of player objects [0] is main player, [1] is optional 2nd player
    protected static final int PLAYER1 	  = 0;		//player 1 index
    protected static final int PLAYER2 	  = 1;		//player 2 index
    protected static final int AIPLAYER   = 2;		//player 3 index AI monster
    protected static final int BLOPSPLIT  = 3;		//AI controlled blop split
    protected static final int AIMONSTER1 = 4;		//CITY SAVER monster
    protected static final int AIMONSTER2 = 5;		//CITY SAVER monster
    protected static final int AIMONSTER3 = 6;		//CITY SAVER monster
    protected static final int AIMONSTER4 = 7;		//CITY SAVER monster
    protected static final int TRAIN1 	  = 8;		//train engine
    protected static final int TRAIN2     = 9;		//train car
    protected static final int TRAIN3     = 10;		//train caboose
    protected static final int FIRST_VEHICLE= 11;	//index of the 1st AI vehicle
    //scoring
    protected static playerScore [] highScores;	//array of high scores
    protected static boolean scoresUpdated;		//toggle to see if we need to update the high scores when a player dies
    protected static boolean cheatsUsed;			//to keep highscore from updating if player calls in a human or AI helper

    protected static boolean day;						//is it day or night?  (determines which GFX are loaded)
    protected static long propertyDamage;			//how much damage our monster has done
    protected static long propertyValue;			//how much total property value there is in CITY_SAVER game mode for the panel
    protected static int threatLevel;				//1-5 (mystery, police, national guard, military, global)
    protected static String[] threatLevels = {"?", "Local Police", "National Guard", "US Military", "Global Threat"};//description of threat levels
    protected static String[] hungerLevels = {"Hungry", "Famished", "Ravenous", "Enraged", "Weakened"};
    protected static String[] statusLevels = {"Lonely", "Confused", "Perturbed", "Enraged", "Frightened"};				//status descriptions for Boobootron
    protected static int[] killStats;				//record the # enemies destroyed by category [crowds, cars, aircraft, boats, structures]
    protected static int time;							//to keep track of time
    protected static boolean nuked;					//has the board been nuked or not?
    protected static boolean monsterWins;			//did the monster destroy all the habitable buildings?
    private static int jumpPeak;						//max jump height (depends on size)
    //mouse stuff
    protected static int mouseX;						//locations for the mouse pointer
    protected static int mouseY;
    protected static int cursorIndex;				//to determine which directional picture to use for the cursor
    private static final int WHEEL_SENSITIVITY=1;//sensitivity of the mouse wheel
    protected static int highlightArea;				//highlight area for monster selection before game starts
    protected static ImageIcon [] cursors;			//mouse cursor images
    //monsters
    protected static String[][][][] playerImages;	//monster images [monster #]x[body direction]x[animation frames]x[head positions]
    //projectiles
    protected static ArrayList<Projectile> projectiles;		//projectiles fired by the user
    public static final int BULLET_LIMIT = 10;		//total # projectiles that can be fired on the screen at one time
    protected static String[][][] rocketImages;		//array of bullet images [direction]x[animation frames]x[last index not used yet]
    protected static String[][][] monsterFireImages;//for Gobzilly fire breath & flame tank
    protected static String[][][] bulletImages;		//for police, army
    protected static String[][][] machBulletImages;	//for machine gun
    protected static String[][][] shellImages;		//for tank/battleship/artilery
    protected static String[][][] beamImages;			//for Boobootron death beam
    protected static String[][][] shriekImages;		//for vehicles thrown by King Clunk
    protected static String[][][] junkImages;			//for vehicles thrown by King Clunk
    protected static String[][][] webImages;			//for Wormoid's web
    protected static ImageIcon[]  web;					//for structures or units that are stuck in a web
    protected static ArrayList<int[]> [] webs;		//collection of row & col values [r1,c1 to r2,c2] for each panel - web locations between buildings

    //enemy units
    protected static String[][][] crowdImages;		//[body direction]x[animation frames]x[head positions] 
    protected static String[][][] troopImages;	
    protected static String[][][] motorcycleImages;
    protected static String[][][] carImages; 		 	
    protected static String[][][] busImages;
    protected static String[][][] firetruckImages; 		 	
    protected static String[][][] policeMotorcycleImages;
    protected static String[][][] policeImages; 
    protected static String[][][] jeepImages; 		 	
    protected static String[][][] tankImages; 	
    protected static String[][][] artilleryImages;  	
    protected static String[][][] flameTankImages; 
    protected static String[][][] missileLauncherImages; 		 		 	
    protected static String[][][] heliImages; 
    protected static String[][][] cessnaImages; 		 		
    protected static String[][][] fighterImages; 	 	
    protected static String[][][] bomberImages; 
    protected static String[][][] bomber2Images; 	 		 	
    protected static String[][][][] trainImages; 	//[train car part]x[body direction]x[animation frames]x[head positions]
    //structures
    protected static Structure[][][] structures;		//array of destroyable/static structures [row location]x[col location]x[panel #]
    protected static int[] numStructures;				//keep track of # of structures in the panel that are still standing (except elect towers) for bonuses when a panel is cleared
    protected static String[][][] skyscraperImages;	//skyscraper structures
    protected static String[][][] buildingImages;	//city building structures
    protected static String[][][] buisnessImages;	//suburb building structures
    protected static String[][][] houseImages;		//suburb house structures
    protected static String[][][] landmarkImages;	//landmark structures
    protected static String[][][] rideImages;			//images for amusement park rides
    protected static String[][][] casinoImages;		//images for casino district
    protected static String[][][] waterTowerImages;	//water tower structures
    protected static String[][][] fuelDepotImages;	//fuel depot structures
    protected static String[][][] gasStationImages;	//gas station structures
    protected static String[][][] treeImages;			//images for trees
    protected static String[][][] elecTowerImages;	//electric tower structures
    protected static String[][][] blopGlopImages;	//images for Blop-glop
    protected static String[][][] holeImages;			//images for hole in the ground (from a tunneling worm)
    //terrain
    protected static String[][] terrainImages;		//array of terrain images [terrain type][animation frames]
    protected static ImageIcon[][] terrain;			//terrain image icons
    //explosions	
    protected static ArrayList<Explosion> explosions;	//active explosions to show on the screen
    protected static String[][][] explosionImages;		//array of explosion images ([direction]x[animation frames]x[not used yet])
    protected static ImageIcon[][] fire;					//for structures that are on fire
    protected static String[][][] elecExplImages;		//array of electric explosion images ([direction]x[animation frames]x[not used yet])
    protected static String[][][] waterExplImages;		//array of electric explosion images ([direction]x[animation frames]x[not used yet])
    protected static String[][][] puffImages;				//array of smoke puff images [direction]x[animation frames]x[not used yet]
    protected static String[][] fireImages;				//array of fire images [direction]x[animation frames]x[not used yet]
    //player / AI spawns
    protected static ArrayList<int[]>[] vehicleSpawnPoints;	//collection of spawn locations (row, col) for ground vehicles for each panel
    protected static ArrayList<int[]>[] humanSpawnPoints;		//spawn locations for people
    protected static ArrayList<int[]>[] boatSpawnPoints;		//spawn locations for boats
    protected static ArrayList<int[]>[] airSpawnPoints;		//spawn locations for aircraft
    protected static ArrayList<int[]>[] tankSpawnPoints;		//spawn locations for ground vehicles
    protected static ArrayList<int[]>[] trainSpawnPoints;		//spawn locations for train
    //the ArrayList of int[] is the collection of coordinates (row, col)
    //we need an array of these ArrayLists to store locations across multiple panels (of which there are 9)
    protected static ArrayList<int[]>[] p2TankSpawnPoints;	//collection of spawn locations (row, col) for player 2 for each panel
    protected static ArrayList<int[]>[] p2CarSpawnPoints;		//spawn locations for player 2

    protected static final int UP		= 0;				//movement directions to use as index for moveDir array
    protected static final int RIGHT = 1;
    protected static final int DOWN  = 2;
    protected static final int LEFT  = 3;

    protected static boolean toggleMap;					//do we want to see key layout or a map?
    protected static int windDirection;					//what direction is the wind blowing (for spreading fires)
    protected static ArrayList<Color> colors;  		//collection of colors to cycle through when the monster wins
    protected static int EI_moving;						//movement direction for EARTH INVADERS vehicles
    protected static int EI_vehicles;					//# enemy vehicles for EARTH INVADERS mode

    protected static String message;						//message to be displayed in stat bar
    protected static int messageTime;					//the frame time in which a message should be displayed
    protected static int messageDuration;				//# frames a message lasts	
    protected static int painTime;						//the frame time in which the main player took damage (used to change border color)
    protected static int painDuration;					//# frames a pain border color should last
    protected static int warningTime;					//the frame time nuke bomber spawns (used to change border color)
    protected static int warningDuration;				//# frames a nuke warning color should last
    protected static int ceaseFireTime;					//the frame time a player changes panels - halt enemy fire for a moment
    protected static int ceaseFireDuration;			//# frames enemy halts fire when a player changes panels

    protected static String[] customInfo;				//stores information for a custom built monster

    protected static Timer t;								//used to set the speed of the enemy that moves around the screen

    public MMMPanel() {
        titleScreen = true;
        mapMaker = false;
        monsterMaker = false;
        mapFileName = null;
        fileQue = new ArrayList<String>();
        mapList = new ArrayList<String>();
        cityList = new ArrayList<String>();
        cityList.add("RANDOMGEN");
        cityListIndex = 0;
        Utilities.readFileNames("maps/filelist.txt", mapList);
        Utilities.findCompleteCities();
        customInfo = MonsterMaker.readCustomInfo("custom.txt");
        textMode = false;
        gameStarted = false;
        //***MOUSE STUFF***
        addMouseListener( this );
        addMouseMotionListener( this );
        addMouseWheelListener(this);
        mouseX = 0;
        mouseY = 0;
        cursorIndex = 4;											//center cursor
        //****END MOUSE STUFF***
        cellSize=40;
        cursors = new ImageIcon[5];							//mouse cursor images - 4 directions and center
        //projectiles
        rocketImages = new String[4][4][1];					//[4 directions] x [4 animation frames] x [not used yet]
        monsterFireImages = new String[4][4][1];	
        shellImages = new String[4][1][1];
        beamImages = new String[4][1][1];
        shriekImages = new String[4][1][1];
        junkImages = new String[4][1][1];
        machBulletImages = new String[4][1][1];
        bulletImages = new String[1][1][1];					//[1 direction image] x [no animation frames] x [not used]
        webImages = new String[4][4][1];  
        web = new ImageIcon[4];
        //explosions
        explosionImages = new String[1][4][1];				//explosions only have one direction, but 4 animation frames
        elecExplImages = new String[1][4][1];				//explosions only have one direction, but 4 animation frames
        waterExplImages = new String[1][4][1];				//explosions only have one direction, but 4 animation frames
        puffImages = new String[1][4][1];					//smoke puffs only have one direction, but 4 animation frames
        fireImages = new String[1][4];						//fire has only have one direction, but 4 animation frames
        fire = new ImageIcon[fireImages.length][fireImages[0].length];  
        //player / AI units
        carImages = new String[4][1][1]; 					//[body direction]x[animation frames]x[head positions] 
        busImages = new String[4][1][1]; 
        motorcycleImages = new String[4][1][1]; 		 
        heliImages = new String[4][2][1]; 					
        cessnaImages = new String[4][2][1]; 				 	 	
        fighterImages = new String[4][1][1]; 				 	
        bomberImages = new String[4][1][1]; 
        bomber2Images = new String[4][1][1]; 				 					 	
        artilleryImages = new String[4][1][1];	
        firetruckImages  = new String[4][2][1];
        policeMotorcycleImages = new String[4][2][1]; 		 			 	
        policeImages = new String[4][2][1]; 				 
        tankImages = new String[4][1][4]; 	
        flameTankImages = new String[4][1][4]; 	
        missileLauncherImages = new String[4][1][4]; 	
        jeepImages = new String[4][1][4]; 					 
        crowdImages = new String[4][4][1];																			
        troopImages = new String[4][4][1];													
        trainImages = new String[3][4][1][1]; 				//[train car part]x[body direction]x[animation frames]x[head positions]	
        playerImages = new String[10][4][4][4]; 			//[monster #]x[body direction]x[animation frames]x[head positions]
        //structures
        treeImages = new String[2][4][1]; 					//[# building types]x[4 animation frames for destruction]x[not used yet];
        elecTowerImages = new String[2][4][1];
        skyscraperImages = new String[6][4][1];
        buildingImages = new String[4][4][1];		
        buisnessImages = new String[4][4][1];	
        houseImages = new String[2][4][1];	
        landmarkImages = new String[10][4][1]; 	
        rideImages = new String[6][4][1];
        casinoImages = new String[6][4][1];
        waterTowerImages = new String[2][4][1];
        fuelDepotImages = new String[2][2][1];
        gasStationImages = new String[3][2][1];
        blopGlopImages = new String[4][4][1];
        holeImages = new String[4][1][1];
        //terrain
        terrainImages = new String[22][4];					//array of terrain images [terrain type][animation frames]
        terrain = new ImageIcon[terrainImages.length][terrainImages[0].length];  

        difficulty = MEDIUM;
        gameMode = MONSTER_MASH;
        highScores = new playerScore[5];		
        for(int i=0; i < highScores.length; i++)
            highScores[i] = new playerScore();

        day = true;
        if(Math.random() < 0.5)	
            day = false;
        ImageDisplay.loadImages();   
        jumpPeak = (cellSize/4);

        int numRows = 17;
        int numColumns = 17;
        int numPanels = 9;
        board = new String[numRows][numColumns][numPanels];
        colors = new ArrayList<Color>();
        colors.add(Color.cyan);
        colors.add(Color.blue);
        colors.add(Color.green);
        colors.add(Color.yellow);
        colors.add(Color.orange);
        colors.add(Color.red);
        colors.add(Color.pink);
        colors.add(Color.magenta);

        // Utilities.readFile("scores/MM_RANDOMGEN.txt", highScores);				//read in high scores

        delay=40;										//#miliseconds delay between each time the enemy moves and screen refresh (or the timer)
        animation_delay = 10;	
        messageDuration = delay * 4;
        ceaseFireDuration = delay * 2;
        painDuration = delay/2;
        warningDuration = delay*2;

        //sound stuff
        Sound.initialize();

        t = new Timer(delay, new Listener());	//the higher the value of delay, the slower the enemy will move
        int numPlayers = 21;
        players = new Player[numPlayers];
        resetGame();
        t.start();
    }

    public static void resetGame() {
        Sound.silence();		//turn sounds off 
        cityListIndex = 0;
        gameStarted = false;
        toggleMap = false;
        mapMaker = false;
        monsterMaker = false;
        mapFileName = null;
        fileQue = new ArrayList<String>();
        textMode = false;
        numPanelsCleared = 0;
        numMonstersKilled = 0;
        numPanelsCleared = 0;
        panelState = new int[3][3];
        for(int r=0; r<panelState.length; r++)
            for(int c=0; c<panelState[0].length; c++)
                panelState[r][c]= 0;
        mouseX = 0;
        mouseY = 0;
        cursorIndex = 4;						//center cursor
        highlightArea = -1;
        if(Math.random() < 0.5)				//change time of day and reload images	
            day = !day;
        ImageDisplay.loadImages();   
        killStats = new int[6];				//[0]crowds, [1]cars, [2]boats, [3]aircraft, [4]buildings, [5]sum
        scoresUpdated = false;
        cheatsUsed = false;
        pause = false;
        p1partner = false;
        p2toggle = false;
        p3toggle = false;
        blopSplit = false;
        nuked = false;
        monsterWins = false;
        windDirection = (int)(Math.random()*4);
        numFrames = 0;
        messageTime = 0;
        painTime = 0;
        warningTime = 0;
        ceaseFireTime = 0;
        message = "";
        propertyDamage = 0;
        propertyValue = 0;
        threatLevel = 0;
        time = 0;
        projectiles = new ArrayList();
        explosions = new ArrayList();
        webs = new ArrayList[board[0][0].length];
        for(int i=0; i<webs.length; i++)
        {	//collection of row & col values [r1,c1 to r2,c2] for each panel - web locations between buildings
            webs[i] = new ArrayList<int[]>();
        }
        panel = 4;		//start in the middle panel
        EI_moving = RIGHT;
        for(int i=0; i<players.length; i++)	 //temp spot for players
            players[i]=new Player();  
        SHOWBORDER = true;
        t.setDelay(delay);					//the higher the value of delay, the slower the enemy will move
        t.restart();
    }

    //pre:   curr!=null,  i>=1 && i<players.length
    //post:  checks for a physical collision between monster curr and vehicle unit at index i
    public static void monsterCollidesVehicle(Monster curr, int i) { 
        if(i < 0 || i >= players.length || players[i]==null || !(players[i] instanceof Vehicle) || players[i].getHealth() <= 0 || players[i].getName().equals("NONE"))
            return;   
        int x1 =curr.findX(cellSize);			//these return the x and y coordinate in pixel space of the monster player
        int y1 =curr.findY(cellSize);
        Vehicle victim = (Vehicle)(players[i]);
        String name = victim.getName();  
        int x2 = victim.findX(cellSize);		//these return the x and y coordinate in pixel space of the player
        int y2 = victim.findY(cellSize);

        if(Utilities.distance(x1, y1, x2, y2) <= (cellSize/2)) {    
            if(victim.isFlying() && !curr.isJumping() && !curr.isFlying())
                return;
            if(curr.isFlying() && !victim.isFlying())
                return;
            Utilities.updateKillStats(victim);
            if(name.startsWith("CROWD"))
            {
                Sound.splat();  
                explosions.add(new Explosion("SMALL", x2-(cellSize/2), y2-(cellSize/2), puffImages, animation_delay));
            }
            else
            {
                if(name.equals("TRAIN engine"))
                {//the engine is destroyed, so destroy the rest of the train
                trainInPanel[panel] = false;
                for(int tp=TRAIN1; tp<=TRAIN3; tp++)
                {
                    if(players[tp] instanceof Vehicle)	//we need this test becuase if the train part is inactive, it is a Player that is not a Vehicle
                    {
                        Vehicle trainPart = (Vehicle)(players[tp]);
                        if(trainPart.getName().equals("TRAIN car") || trainPart.getName().equals("TRAIN caboose"))
                        {
                            if(players[PLAYER1].getHealth() > 0)
                            {
                            propertyDamage += trainPart.getPropertyValue();
                            Utilities.updateKillStats(trainPart);
                            }
                            // if monster is not The-Blop, make explosion
                            // otherwise, create a puff explosion
                            if (!curr.getName().equals("The-Blop")) {
                                
                                Sound.fireballExplosion(); 
                                explosions.add(new Explosion("SMALL", trainPart.getX()-(cellSize/2), trainPart.getY()-(cellSize/2), explosionImages, animation_delay));
                            }  else {
                                Sound.smokepuffExplosion();
                                explosions.add(new Explosion("SMALL", trainPart.getX() - (cellSize / 2), trainPart.getY() - (cellSize / 2), puffImages, animation_delay))    ;
                            }
                            players[tp]=new Player();
                        }//give removed train cars a temprary name so we can remove them at the end of the method without disrupting the index i of the greater loop that traverses through players
                    }
                }	  
                }    
                else
                if(name.equals("TRAIN car"))
                {//the car is destroyed, so destroy the caboose
                    trainInPanel[panel] = false;
                    for(int tp=TRAIN1; tp<=TRAIN3; tp++)
                    {
                        if(players[tp] instanceof Vehicle)
                        {
                            Vehicle trainPart = (Vehicle)(players[tp]);
                            if(trainPart.getName().equals("TRAIN caboose"))
                            {
                            if(players[PLAYER1].getHealth() > 0)
                            {
                                propertyDamage += trainPart.getPropertyValue();
                                Utilities.updateKillStats(trainPart);
                            }  
                            // if monster is not The-Blop, make explosion
                            // otherwise, create a puff explosion
                            if (curr.getName() != "The-Blop") {
                                
                                Sound.fireballExplosion(); 
                                explosions.add(new Explosion("SMALL", trainPart.getX()-(cellSize/2), trainPart.getY()-(cellSize/2), explosionImages, animation_delay));
                            } else {
                                Sound.smokepuffExplosion();
                                explosions.add(new Explosion("SMALL", trainPart.getX() - (cellSize / 2), trainPart.getY() - (cellSize / 2), puffImages, animation_delay))    ;
                            }
                            players[tp]=new Player();
                            }//give removed train cars a temprary name so we can remove them at the end of the method without disrupting the index i of the greater loop that traverses through players
                        }
                    }	 
                }
                if(victim.isFlying())
                {
                Sound.fireballExplosion(); 
                explosions.add(new Explosion("BIG", x2-(cellSize/2), y2-(cellSize/2), explosionImages, animation_delay));
                }
                else
                if(name.startsWith("BOAT"))
                {
                    Sound.waterExplosion();
                    explosions.add(new Explosion("SMALL", x2-(cellSize/2), y2-(cellSize), waterExplImages, animation_delay*2));
                }
                else
                {
                    Sound.smallExplosion();
                    // if monster is not The-Blop, make an explosion
                    // otherwise, create a puff explosion
                    if (curr.getName() != "The-Blop") {
                        explosions.add(new Explosion("SMALL", x2 - (cellSize / 2), y1 - (cellSize / 2), explosionImages, animation_delay));
                    } else {
                        explosions.add(new Explosion("SMALL", x2-(cellSize/2), y2-(cellSize/2), puffImages, animation_delay));
                    }
                }
            }
            if(players[PLAYER1].getHealth() > 0 )
                propertyDamage += victim.getPropertyValue();
            if(i > 0)    
                Spawner.resetEnemy(i);					//reset the enemy's position
            else
                if(gameMode == CITY_SAVER)
                {
                players[i].setHealth(0);
                propertyValue = 0;
                }
        }

    }

    //pre:   the human player is at index 0 of the players array, possible player 2 at index 1 of the players array
    //post:  handles collsions between players and projectiles
    public static void checkCollisions()
    {
        for(int i=0; i<players.length; i++)
        {
            Player curr = players[i];
            if(curr==null || curr.getName().equals("NONE"))	//player 2 might not be activated
                continue;
        
            String name = curr.getName();
        
            int x2 = curr.findX(cellSize);				//these return the x and y coordinate in pixel space of the player
            int y2 = curr.findY(cellSize);
        
            for(int j=0; j<projectiles.size(); j++)			//see if projectiles hit any enemies
            {
                Projectile shot = projectiles.get(j);
            
                int sX = shot.getX();
                int sY = shot.getY();
                if(Utilities.distance(sX, sY, x2, y2) <= (cellSize/2))
                {		//if bullet is an air shot and target is not an airplane nor one of the monsters
                if(shot.inAir() && !curr.isFlying() && (curr instanceof Vehicle))
                {  //air shots can only hit aircraft
                    continue;
                }   
                if(gameMode!=EARTH_INVADERS && !shot.inAir() && curr.isFlying())
                {
                    continue;
                }
                if(shot.getOwner()==i)				//don't shoot ourselves
                    continue;
                
                //mind controlled vehicles can shoot other vehicles - so see if the shooter is mind controlled   
                Player shooter = players[shot.getOwner()];
                boolean shooterIsMindControlled = false;
                if(shooter!=null && (shooter instanceof Vehicle) && ((Vehicle)(shooter)).isMindControlled())
                    shooterIsMindControlled = true;
                
                if(!name.startsWith("CROWD") && (curr instanceof Vehicle) && shot.getType().equals("BULLET") && !shooterIsMindControlled)
                    continue;							//only CROWDS are effected by bullets
                if(curr instanceof Vehicle)		//***VEHICLE GETS HIT
                {
                    if(gameMode!=EARTH_INVADERS && shot.getType().startsWith("SHRIEK") 
                    && !curr.isFlying() && !name.startsWith("TRAIN"))
                    {
                        ((Vehicle)(curr)).setStunTime(numFrames + messageTime);	
                    }
                    else
                    {
                        Utilities.updateKillStats(curr);
                        if(players[PLAYER1].getHealth() > 0)
                        {
                            if(gameMode==EARTH_INVADERS)
                            {
                            int pointBoost = difficulty;
                            if(difficulty == FREEROAM)		//FREE ROAM mode has same point boost as nightmare
                                pointBoost = NIGHTMARE;
                            propertyDamage += (((Vehicle)(curr)).getPropertyValue() * (1+pointBoost));
                            }   
                            else
                            propertyDamage += ((Vehicle)(curr)).getPropertyValue();
                        }
                        if(name.equals("TRAIN engine"))
                        {		//the engine is destroyed, so destroy the rest of the train
                            trainInPanel[panel] = false;
                            for(int tp=TRAIN1; tp<=TRAIN3; tp++)
                            {
                            if(players[tp] instanceof Vehicle)
                            {
                                Vehicle trainPart = (Vehicle)(players[tp]);
                                if(trainPart.getName().equals("TRAIN car") || trainPart.getName().equals("TRAIN caboose"))
                                {
                                    if(players[PLAYER1].getHealth() > 0)
                                    {
                                        propertyDamage += trainPart.getPropertyValue();
                                        Utilities.updateKillStats(trainPart);
                                    }  
                                    Sound.fireballExplosion();
                                    explosions.add(new Explosion("SMALL", trainPart.getX()-(cellSize/2), trainPart.getY()-(cellSize/2), explosionImages, animation_delay));
                                    players[tp]=new Player();
                                }//give removed train cars a temprary name so we can remove them at the end of the method without disrupting the index i of the greater loop that traverses through players
                            }
                            }	  
                        } 
                        else
                            if(name.equals("TRAIN car"))
                            {	//the car is destroyed, so destroy the caboose
                            for(int tp=TRAIN1; tp<=TRAIN3; tp++)
                            {
                                if(players[tp] instanceof Vehicle)
                                {
                                    Vehicle trainPart = (Vehicle)(players[tp]);
                                    if(trainPart.getName().equals("TRAIN caboose"))
                                    {
                                        if(players[PLAYER1].getHealth() > 0)
                                        {
                                        propertyDamage += trainPart.getPropertyValue();
                                        Utilities.updateKillStats(trainPart);
                                        }  
                                        Sound.fireballExplosion();
                                        explosions.add(new Explosion("SMALL", trainPart.getX()-(cellSize/2), trainPart.getY()-(cellSize/2), explosionImages, animation_delay));
                                        players[tp]=new Player();
                                    }//give removed train cars a temprary name so we can remove them at the end of the method without disrupting the index i of the greater loop that traverses through players
                                }	 
                            } 
                            }    
                        if(gameMode == CITY_SAVER && i==0)
                            curr.damage(shot.getPower()/2);
                        else
                            Spawner.resetEnemy(i);				//reset the enemy's position
                    }
                }									//***END VEHICLE GETS HIT
                else								//***MONSTER GETS HIT
                {
                    int dam = shot.getPower();
                    if(shot.getOwner()>=0 && shot.getOwner() < players.length && (players[shot.getOwner()] instanceof Monster))//monster friendly fire, do half damage
                        dam /= 2;
                    int mr = curr.getRow();
                    int mc = curr.getCol();	//if monster is hiding in buildings, lessen the damage and deal some to the buildings as well
                    if(structures[mr][mc][panel]!=null && structures[mr][mc][panel].getHealth() > 0 )
                    {
                        structures[mr][mc][panel].damage(dam);
                        dam /= 4;
                        if(structures[mr][mc][panel].getHealth() == 0)
                        {
                            Utilities.updateKillStats(structures[mr][mc][panel]);
                            if(players[PLAYER1].getHealth() > 0)
                            {
                            int value = (int)(structures[mr][mc][panel].getPropertyValue());
                            propertyDamage += value;
                            if(panel == 4)		//we only protect the center panel in CITY_SAVER
                                propertyValue -= value;
                            }
                            if(gameMode != EARTH_INVADERS)
                            Spawner.removeFromSpawn(humanSpawnPoints[panel], mr, mc);
                        }
                    }
                    if(gameMode!=EARTH_INVADERS && curr.isFragile())	//WoeMantis takes more damage
                        curr.damage((int)(dam*1.75));
                    else
                        curr.damage(dam);
                
                    Sound.monsterHurt();
                    if(i==0)
                        painTime = numFrames; 
                }	//***END MONSTER GETS HIT
                if(shot.getType().startsWith("SHRIEK") && !curr.isFlying())
                    explosions.add(new Explosion("SMALL", x2-(cellSize/2), y2-(cellSize/2), puffImages, animation_delay));
                else
                    if(shot.getType().startsWith("BULLET"))
                    {
                        // monster is not impervious to bullets OR game mode is EARTH_INVADERS
                        if (!curr.imperviousToBullets() || gameMode == EARTH_INVADERS) { 
                            explosions.add(new Explosion("SMALL", x2-(cellSize/2), y2-(cellSize/2), puffImages, animation_delay));
                        }
                    }
                    else
                        if(curr.isFlying())
                        {
                            Sound.fireballExplosion();
                            explosions.add(new Explosion("BIG", x2-(cellSize/2), y2-(cellSize/2), explosionImages, animation_delay));
                        }
                        else
                            if(name.startsWith("BOAT"))
                            {
                            Sound.waterExplosion();
                            explosions.add(new Explosion("SMALL", x2-(cellSize/2), y2-(cellSize), waterExplImages, animation_delay*2));
                            }
                            else
                            {
                            if(board[curr.getRow()][curr.getCol()][panel].startsWith("~"))	//player gets hit in the water
                            {
                                Sound.waterExplosion();
                                explosions.add(new Explosion("SMALL", x2-(cellSize/2), y2-(cellSize/2), waterExplImages, animation_delay*2));
                            }
                            else
                            {
                                Sound.smallExplosion();
                                explosions.add(new Explosion("SMALL", x2-(cellSize/2), y2-(cellSize/2), explosionImages, animation_delay));
                            }
                            }
                if(projectiles.size()>0 && j<projectiles.size())      
                    projectiles.remove(j);					//remove bullet that struck enemy
                j--;	
                if(projectiles.size()==0)
                    break;
                else
                    continue;
                }
            }
        
        //check for monster colliding with vehicle units
            for(int m=0; m<players.length; m++)
            {
                Player currM = players[m];
                if(currM==null || currM.getName().equals("NONE"))
                continue;
                if((currM instanceof Monster) && (curr instanceof Vehicle))
                monsterCollidesVehicle((Monster)(currM), i);
            }
        } 
    }

    //post:  if the blop has split and we are close enough, recombine them.  If the blop hasn't split and we have 100 health, split it
    public static void splitTheBlop(Monster curr) {
        // blop has not been split: no AIPlayer
        if (curr.canSplit()) {
            // check if Blop can split
            if (curr.getHealth() == 100) {
                // iniitalize AI player
                players[AIPLAYER] = (new Blop("The-Blop", 5, 5, playerImages[4], 100, 0, 30));

                // reduce health of original Blop
                curr.setHealth((int) (Math.random() * 51 + 25));
                
                // Blop cannot split once it has already splitted
                curr.setCanSplit(false);
            }
        } else if (
                // Blop has already been split
                // check if AI player is within 1 space 
                Math.abs(players[AIPLAYER].getX() - curr.getX()) < 2 &&
                Math.abs(players[AIPLAYER].getY() - curr.getY()) < 2
            ) {
                curr.setCanSplit(true);
                players[AIPLAYER] = null;
                curr.setHealth(curr.getHealth() + (int) (Math.random() * 51) + 50);
        }
    }


    //pre:   curr != null && owner < players.size
    //post:  monster at index owner shoots
    public static void playerShoot(Player current, int owner)
    {
        if(pause || current==null || current.getName().equals("NONE"))
            return;
        if (gameMode==CITY_SAVER && (current instanceof Vehicle))    
        {
            AImovement.vehicleShoot(current, 0);
            return;
        }
        if(!(current instanceof Monster))
            return;

        Monster curr = (Monster)(current);
        if(gameMode!=EARTH_INVADERS)        //we always want to shoot a projectile in earth invaders mode
        {
            if(curr.isMindControl())			//scramble the brains of any Vehicle in close proximity	
            {
                Utilities.mindControlUnits(curr);
                return;
            }
            else  
                if(curr.canSplit())			   //splitters doesn't shoot, but can separate for the price of health	
                {
                splitTheBlop(curr);
                return;
                }
                else                          //if we are not a shooter, nor a mind controller nor a splitter, end the method
                if(!curr.isShooter() && !curr.isThrower())			
                {                          //becuase we have nothing to shoot
                    return;
                }
        }
        boolean airShot = false;
        if(curr.isJumping() || curr.isFlying())
            airShot = true;
    //see if there is a possible air target - if not, make it a ground shot  
        int hd = curr.getHeadDirection();
        if(airShot && gameMode!=EARTH_INVADERS)
        {
            int ourX = curr.getX();
            int ourY = curr.getY();
            if(hd == UP || hd == DOWN)
            {
                boolean airTarget = false;
                for(int i=FIRST_VEHICLE; i<players.length; i++)
                {
                if(players[i]==null || players[i].getHealth() <= 0)
                    continue;
                if(players[i].getRow()==0 || players[i].getCol()==0 || players[i].getRow()==board.length-1 || players[i].getCol()==board[0].length-1)
                    continue;      
                int targX = players[i].findX(cellSize);   
                if(targX >= ourX - (cellSize*1.5) && targX <= ourX + (cellSize*1.5) && players[i].isFlying())
                {
                    airTarget = true;
                    break;
                }
                }
                if(!airTarget)
                airShot = false;
            }
            else
            {
                boolean airTarget = false;
                for(int i=FIRST_VEHICLE; i<players.length; i++)
                {
                if(players[i]==null || players[i].getHealth() <= 0)
                    continue;
                if(players[i].getRow()==0 || players[i].getCol()==0 || players[i].getRow()==board.length-1 || players[i].getCol()==board[0].length-1)
                    continue;      
                int targY = players[i].findY(cellSize);   
                if(targY >= ourY - (cellSize*1.5) && targY <= ourY + (cellSize*1.5) && players[i].isFlying())
                {
                    airTarget = true;
                    break;
                }
                }
                if(!airTarget)
                airShot = false;
            }
        
        }  
        boolean needToReload = false;
        if(gameMode == EARTH_INVADERS)
        {	//everyone can shoot in EARTH_INVADERS, so reload if time - don't shoot and show message
            if((numFrames > curr.getReloadTime() && numFrames < curr.getLastShotTime() + curr.getReloadTime()))
                needToReload = true;
        }
        else
            if(curr.isThrower())
            {	//Throwers have to have ammo in hand, otherwise, don't shoot and show message
                if(curr.projectileType().equals("empty"))
                needToReload = true;
            }
            else
            {	//non-throwers in non-EARTH_INVADERS mode might have reload time
                if((numFrames > curr.getReloadTime() && numFrames < curr.getLastShotTime() + curr.getReloadTime()))
                needToReload = true;
            }
        if(needToReload)          
        {  
            if(curr==players[PLAYER1])
            {
                if(gameMode!=EARTH_INVADERS)            
                {
                message = curr.reloadingMessage();
                messageTime = numFrames;
                }
            }
            return;
        }
        if(gameMode==EARTH_INVADERS || (curr.isThrower() && !curr.projectileType().equals("empty")) || (curr.getHunger() < hungerLevels.length -1  || curr.energyAbsorber()))
        {//always allowed to shoot in earth invaders mode, otherwise, shooter monster types only shoot if they are not weakened from hunger
        //thrower monster types can only shoot if holding a vehicle - energy absorbers can always shoot (they are not goverend by hunger)
            if(gameMode==EARTH_INVADERS && hd!=UP)
            {
                hd=UP;
                curr.setHeadDirection(hd);
            }
            Projectile temp= null;
            String type = curr.projectileType();
            if(type.equals("WEB"))
            {
                Sound.shootWeb();
                int speed = SPEED*4;
                if(gameMode == EARTH_INVADERS)
                speed = SPEED*8;
                temp = new Projectile(curr.getName()+hd, curr.getX(), curr.getY(), 1, webImages, SPEED, type, speed, airShot, owner, -1, -1);
            }
            else
                if(type.equals("SHRIEK"))
                {
                Sound.shriekAttack();
                int speed = SPEED*10;
                if(gameMode == EARTH_INVADERS)
                    speed = SPEED*8;
                temp = new Projectile(curr.getName()+hd, curr.getX(), curr.getY(), 1, shriekImages, SPEED, type, speed, airShot, owner, -1, -1);
                }
                else
                if(type.equals("BEAM"))
                {
                    Sound.shootBeam();
                    int speed = SPEED*10;
                    if(gameMode == EARTH_INVADERS)
                        speed = SPEED*8;
                    temp = new Projectile(curr.getName()+hd, curr.getX(), curr.getY(), 50, beamImages, SPEED, type, speed, airShot, owner, -1, -1);
                }
                else
                    if(type.equals("FIRE"))
                    {
                        Sound.shootFire();
                        int speed = SPEED*4;
                        if(gameMode == EARTH_INVADERS)
                            speed = SPEED*8;
                        temp = new Projectile("flame"+hd, curr.getX(), curr.getY(), 15, monsterFireImages, SPEED, type, speed, airShot, owner, -1, -1);
                    }
                    else
                        if(type.equals("GLOP"))
                        {
                            Sound.shootWeb();
                            temp = new Projectile("glop"+hd, curr.getX(), curr.getY(), 15, blopGlopImages, SPEED, type, SPEED*8, airShot, owner, -1, -1);
                        }
                        else
                            if(curr.isThrower() && ((!type.equals("empty") && !type.equals("CROWD")) || gameMode == EARTH_INVADERS))
                            {
                            Sound.throwCar();
                            int speed = SPEED*4;
                            if(gameMode == EARTH_INVADERS)
                            {
                                speed = SPEED*8;
                                type = "CAR";
                            }
                            else
                            {
                            //we are throwing a vehicle, so empty out the hand that was holding the vehicle   
                                String[] contents = curr.getClawContents();
                                if(!contents[0].startsWith("CROWD") && !contents[0].startsWith("empty"))
                                    contents[0] = "empty";
                                else
                                    if(!contents[1].startsWith("CROWD") && !contents[1].startsWith("empty"))
                                        contents[1] = "empty";
                                curr.setClawContents(contents);      
                            }
                            temp = new Projectile(curr.getName()+hd, curr.getX(), curr.getY(), 20, junkImages, SPEED, type, speed, airShot, owner, -1, -1);
                            }
                            else
                            temp = new Projectile(""+hd, curr.getX(), curr.getY(), 20, rocketImages, SPEED, type, SPEED*3, airShot, owner, -1, -1);
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

    //post: even though players are moving one array cell at a time, make all players move smoothly by pixel to the next cell
    public static void movePlayerToAdjacentCell() {
        for(int i=0; i < players.length; i++) {
            Player curr = players[i];
            if(curr==null)		//player 2 might not be activated
                continue;
        
            String name = curr.getName();
            if(name.equals("NONE"))
                continue;
            int r = curr.getRow();
            int c = curr.getCol();
            int shotBoost = difficulty;
            if (difficulty == FREEROAM)		//FREE ROAM mode has same point boost as nightmare
                shotBoost = NIGHTMARE;
        
            if (gameMode==EARTH_INVADERS && (curr instanceof Vehicle) && Math.random() < (.001*(shotBoost+1)) 
                && projectiles.size() < BULLET_LIMIT-2 && !name.startsWith("TRAIN") && !curr.isFlying() 
                && players[PLAYER1].getHealth() > 0) {
                AImovement.vehicleShoot( curr, i); 
            }
        
            if (curr instanceof Monster)	{	//monster player
                Utilities.walkDamage((Monster)(curr), r, c, panel);
                if(((Monster)(curr)).isJumping() && ((Monster)(curr)).hitJumpPeak() && ((Monster)(curr)).getJumpAlt() <= 0) {  
                    ((Monster)(curr)).setIsJumping(false);
                    ((Monster)(curr)).setHitJumpPeak(false);
                    ((Monster)(curr)).setJumpAlt(0);
                    continue;
                }
            } 
            if (curr.isMovingUp() && r>0 && board[r-1][c][panel].startsWith("#") && !curr.isFlying() && !curr.isDigging()) {
                curr.clearDirections();
                continue;
            } 
            if(curr.isMovingDown() && r<board.length-1 && board[r+1][c][panel].startsWith("#") && !curr.isFlying() && !curr.isDigging()) {
                curr.clearDirections();
                continue;
            } 
            if(curr.isMovingLeft() && c>0 && board[r][c-1][panel].startsWith("#") && !curr.isFlying() && !curr.isDigging()) {
                curr.clearDirections();
                continue;
            } 
            if(curr.isMovingRight() && c<board[0].length-1 && board[r][c+1][panel].startsWith("#") && !curr.isFlying() && !curr.isDigging()) {
                curr.clearDirections();
                continue;
            } 
            if(curr instanceof Monster) {
                if(((Monster)(curr)).isJumping()) {  
                    if(!((Monster)(curr)).hitJumpPeak()) {
                        if(((Monster)(curr)).getJumpAlt() >= jumpPeak) {
                            ((Monster)(curr)).setHitJumpPeak(true);
                            continue;
                        } else {
                            ((Monster)(curr)).addJumpAlt(SPEED*2);
                        }
                    } else
                        ((Monster)(curr)).subtJumpAlt(SPEED*2);
                }
            }
            if(Math.abs(curr.getMoveIncrX()) >= cellSize || Math.abs(curr.getMoveIncrY()) >= cellSize) {
                if(curr.isMovingUp() && r>0 && (!board[r-1][c][panel].startsWith("#") || curr.isFlying() || curr.isDigging())) {
                    curr.setRow(r-1);
                } else if(curr.isMovingDown() && r<board.length-1 && (!board[r+1][c][panel].startsWith("#") || curr.isFlying() || curr.isDigging())) {
                    curr.setRow(r+1);
                } else {
                    if(curr.isMovingLeft() && curr.getCol()>0 && (!board[r][c-1][panel].startsWith("#") || curr.isFlying() || curr.isDigging()))
                        curr.setCol(c-1);
                    else 
                        if(curr.isMovingRight() && c<board[0].length-1 && (!board[r][c+1][panel].startsWith("#") || curr.isFlying() || curr.isDigging()))
                            curr.setCol(c+1);
                }
                curr.clearDirections(); 
            } else {
                int speed = SPEED;
            
                if(board[curr.getRow()][curr.getCol()][panel].equals("~~~") && !curr.isFlying() && !curr.isDigging()) {
                    if(!curr.isSwimmer()) {	//take away speed if in the water and we are not a swimmer
                        if(SPEED > 1)
                            speed /= 2;
                        else
                            if(numFrames%2 == 0) {					
                                continue;
                            }
                    } else							//we are a swimmer, so move faster in the water
                        speed *= 2;
                }
                if (curr.isFlying() && gameMode!=EARTH_INVADERS) {
                    speed *= 2;
                } if (curr.getSpeedPenalty() > 1 && numFrames%curr.getSpeedPenalty() != 0) {
                    continue;
                }
            
                if (curr.getName().equals("AIR fighter")) {	//jet fighter moves faster
                    speed *= 2;
                }
            
                if (curr.isMovingUp() && curr.getRow()>0) {
                    curr.addMoveIncrY(-1*speed);
                } else if(curr.isMovingDown() && curr.getRow()<board.length-1) {
                    curr.addMoveIncrY(speed);
                } else {
                    if (curr.isMovingLeft() && curr.getCol()>0) {
                        curr.addMoveIncrX(-1*speed);
                    }
                    else if (curr.isMovingRight() && curr.getCol()<board[0].length-1) {
                        curr.addMoveIncrX(speed);
                    }
                }
            }


            if(curr.getName().indexOf("firetruck") >= 0) {	//put out any fire close to the firetruck, and spray away any Blop-glop
                for(int nr=r-1; nr<=r+1; nr++) {
                    for(int nc=c-1; nc<=c+1; nc++) {
                        if(nr<1 || nc<1 || nr>board.length-1 || nc>board[0].length-1) {
                            continue;
                        } 
                        if(structures[nr][nc][panel]!=null) {
                            if (structures[nr][nc][panel].onFire() && Math.random() < .025) {
                                structures[nr][nc][panel].setOnFire(false);
                            }
                            if (structures[nr][nc][panel].getName().startsWith("Blop") && Math.random() < .025) {
                                structures[nr][nc][panel].damage(1);
                            }
                        }
                    }
                }
            }

            if (curr.slimeTrail() &&
                (!board[r][c][panel].equals("~~~")) && // blop is not on water
                (
                    structures[r][c][panel] == null || // structure is empty OR
                    (structures[r][c][panel] != null && !structures[r][c][panel].getName().equals("Blop-glop")) // structure is NOT Blop-glop
                )
                ) {
                    // create Blop-glop
                    structures[r][c][panel] = new Structure("Blop-glop", r, c, panel, blopGlopImages, 15, true, true, 1, -1, 1, (int) (Math.random() * 3), 0);

                    // add damage
                    propertyDamage += (int) (Math.random() * 3000);
            }
        }
    }

    //pre: k is a valid keyboard command sent from the driver's keyboard input, playerIndex>=0 && playerIndex < players.length
    //post:directs monster or vehicle depending on keyboard input k
    public static void playerDirect(int k, int playerIndex)
    {
        if(pause || playerIndex<0 || playerIndex>=players.length)
            return;
        Player curr = players[playerIndex];
        if(curr == null || curr.getName().equals("NONE"))
            return;
        curr.clearDirections();
        int r = curr.getRow();
        int c = curr.getCol();
        if((k==KeyEvent.VK_SPACE || k==KeyEvent.VK_T) && curr instanceof Monster)		//stomp on a structure if there is one there
        {	//stomp command for p1 || p2
            if(curr.isFlyer() && gameMode!=EARTH_INVADERS)	//insect wants to fly, so see if it has enough stamina to takeoff
            {
                if(!curr.isFlying() && (numFrames > curr.getReloadTime() && numFrames < curr.getLastShotTime() + curr.getReloadTime()))
                {//use the same stamina that we use to keep breath attack from being spammed
                message = curr.reloadingMessage();
                messageTime = numFrames;
                }
                else
                {
                Sound.stomp();
                Utilities.monsterStomp((Monster)(curr), playerIndex);
                if(curr.isFlying())		//add to reload time if we just took off
                    curr.setLastShotTime(numFrames);
                }
            }
            else
            {
                Sound.stomp();
                Utilities.monsterStomp((Monster)(curr), playerIndex);
            }
        }
        else
            if(k==KeyEvent.VK_UP || k==KeyEvent.VK_W)	//we can't walk from under an impassable structure to behind it (unless we destroy it first, or we are flying over or digging under it)
            {	//up command for player 1 || player 2
                if(curr.getBodyDirection() == UP && r>0 && (MapBuilder.noStructure(r-1, c, panel) || curr.isFlying() || curr.isDigging()))
                //While underground or flying, a monster should be able to traverse to a location above or below her even if there is a structure there.
                {
                if(r==1)		//top row	
                {
                //can't move up in panels 0, 1 or 2
                    if(gameMode!=CITY_SAVER && gameMode!=EARTH_INVADERS && playerIndex==0 && panel > 2 && (!p1partner || (p1partner && players[PLAYER2].getRow()==r)))
                    {	//if there isn't a monster partner OR there is, and they are in the same row, then change panels
                        ceaseFireTime = numFrames;
                        panel -= 3;
                        curr.setRow(board.length-2);
                        Spawner.initialVehicleSpawn(panel);
                        Sound.silence();
                        if(p1partner)	//move p2 monster to the new panel also
                        {
                            players[PLAYER2].setRow(board.length-2);
                        }
                    }
                    else //if(!((p3toggle && playerIndex==2) || (blopSplit && playerIndex==3) || (gameMode==CITY_SAVER && monsterIndex.contains(playerIndex))))
                    {//don't show message if AI monster tries to move off the panel
                        if(p1partner && players[PLAYER1].getRow() != players[PLAYER2].getRow() && panel > 2)
                        {
                            message = "Wait for friend!";
                            messageTime = numFrames;
                        }
                        else
                            if(panel<=2 && playerIndex==0 )
                            {
                            message = "Blocked!";
                            messageTime = numFrames;
                            }
                    }
                    curr.setBodyDirection(UP);
                    curr.setHeadDirection(UP);
                }
                else 
                {
                    if(gameMode!=EARTH_INVADERS)	//can't move up or down in EARTH INVADERS
                        curr.setDirection(UP);
                    else
                    {
                        curr.setBodyDirection(UP);
                        curr.setHeadDirection(UP);
                    }
                }
                }
                else
                {
                curr.setBodyDirection(UP);
                curr.setHeadDirection(UP);
                }
            }
            else 
                if((k==KeyEvent.VK_DOWN || k==KeyEvent.VK_S) && gameMode!=EARTH_INVADERS)//we can not move down in this mode
                {	//down command for player 1 || player 2
                if(curr.getBodyDirection() == DOWN && r < board.length-1 && (MapBuilder.noStructure(r, c, panel) || curr.isFlying()  || curr.isDigging()))
                //While underground or flying, a monster should be able to traverse to a location above or below her even if there is a structure there.
                {
                    if(r==board.length-2)		//bottom row	
                    {	//can't move down in panels 6, 7 or 8
                        if(gameMode!=CITY_SAVER  && gameMode!=EARTH_INVADERS && playerIndex==0 && panel < 6 && (!p1partner || (p1partner && players[PLAYER2].getRow()==r)))
                        {	//if there isn't a monster partner OR there is, and they are in the same row, then change panels
                            panel += 3;
                            ceaseFireTime = numFrames;
                            curr.setRow(1);
                            Spawner.initialVehicleSpawn(panel);
                            Sound.silence();
                            if(p1partner)	//move p2 monster to the new panel also
                            {
                            players[PLAYER2].setRow(1);
                            }
                        }
                        else //if(!((p3toggle && playerIndex==2) ||  (blopSplit && playerIndex==3) || (gameMode==CITY_SAVER && monsterIndex.contains(playerIndex))))
                        {
                            if(p1partner && players[PLAYER1].getRow() != players[PLAYER2].getRow() && panel < 6)
                            {
                            message = "Wait for friend!";
                            messageTime = numFrames;
                            }
                            else
                            if(panel>=6 && playerIndex==0)
                            {
                                message = "Blocked!";
                                messageTime = numFrames;
                            }
                        }
                        curr.setBodyDirection(DOWN);
                        curr.setHeadDirection(DOWN);
                    }
                    else
                    {
                        if(gameMode!=EARTH_INVADERS)	//can't move up or down in EARTH INVADERS
                            curr.setDirection(DOWN);
                        else
                        {
                            curr.setBodyDirection(DOWN);
                            curr.setHeadDirection(DOWN);
                        }
                    }
                }
                else
                {
                    curr.setBodyDirection(DOWN);	//here, we might be behind a structure, but we don't want to walk through it if it is impassable
                    curr.setHeadDirection(DOWN);
                }
                }
                else
                if(k==KeyEvent.VK_LEFT || k==KeyEvent.VK_A)	//we can walk behind structures from the left and right
                {								//so we dont have to check to see if there is a structure here
                    if(curr.getBodyDirection() == LEFT && c>0)
                    {
                        if(c==1)		//first col	
                        {	//can't move left in panels 0, 3 or 6
                            if(gameMode!=CITY_SAVER  && gameMode!=EARTH_INVADERS && playerIndex==0 && panel !=0 && panel != 3 && panel != 6 && (!p1partner || (p1partner && players[PLAYER2].getCol()==c)))		
                            {
                            panel--;
                            ceaseFireTime = numFrames;
                            curr.setCol(board[0].length-2);
                            Spawner.initialVehicleSpawn(panel);
                            Sound.silence();
                            if(p1partner)	//move p2 monster to the new panel also
                            {
                                players[PLAYER2].setCol(board[0].length-2);
                            }
                            }
                            else// if(!((p3toggle && playerIndex==2) ||  (blopSplit && playerIndex==3) || (gameMode==CITY_SAVER && monsterIndex.contains(playerIndex))))
                            {
                            if(p1partner && players[PLAYER1].getCol() != players[PLAYER2].getCol() && panel !=0 && panel != 3 && panel != 6)
                            {
                                message = "Wait for friend!";
                                messageTime = numFrames;
                            }
                            else
                                if((panel==0 || panel==3 || panel==6) && playerIndex==0)
                                {
                                    message = "Blocked!";
                                    messageTime = numFrames;
                                }
                            }
                            curr.setBodyDirection(LEFT);
                            curr.setHeadDirection(LEFT);
                        }
                        else
                            curr.setDirection(LEFT);
                    }
                    else
                    {
                        curr.setBodyDirection(LEFT);
                        curr.setHeadDirection(LEFT);
                    }
                }
                else 
                    if(k==KeyEvent.VK_RIGHT || k==KeyEvent.VK_D)
                    {  
                        if(curr.getBodyDirection() == RIGHT && c<board[0].length-1)
                        {
                            if(c==board[0].length-2)		//last col	
                            {	//can't move right in panels 2, 5 or 8
                            if(gameMode!=CITY_SAVER  && gameMode!=EARTH_INVADERS && playerIndex==0 && panel !=2 && panel != 5 && panel != 8 && (!p1partner || (p1partner && players[PLAYER2].getCol()==c)))		
                            {
                                panel++;
                                ceaseFireTime = numFrames;
                                curr.setCol(1);
                                Spawner.initialVehicleSpawn(panel);
                                Sound.silence();
                                if(p1partner)	//move p2 monster to the new panel also
                                {
                                    players[PLAYER2].setCol(1);
                                }
                            }
                            else// if(!((p3toggle && playerIndex==2) ||  (blopSplit && playerIndex==3) || (gameMode==CITY_SAVER && monsterIndex.contains(playerIndex))))
                            {
                                if(p1partner && players[PLAYER1].getCol() != players[PLAYER2].getCol() && panel !=2 && panel != 5 && panel != 8)
                                {
                                    message = "Wait for friend!";
                                    messageTime = numFrames;
                                }
                                else
                                    if((panel==2 || panel==5 || panel==8) && playerIndex==0)
                                    {
                                        message = "Blocked!";
                                        messageTime = numFrames;
                                    }
                            }
                            curr.setBodyDirection(RIGHT);
                            curr.setHeadDirection(RIGHT);
                            }
                            else
                            
                            curr.setDirection(RIGHT);
                        }
                        else
                        {
                            curr.setBodyDirection(RIGHT);
                            curr.setHeadDirection(RIGHT);
                        }
                    }
    }   

    //THIS METHOD IS ONLY CALLED THE MOMENT A KEY IS HIT - NOT AT ANY OTHER TIME
    //pre:   dir is a valid command (sent from the driver when a key is hit)
    //			arg is sent to know the screen size if it changes
    //post:  changes the players position depending on the key that was pressed (sent from the driver)
    //			keeps the player in the bounds of the size of the array board
    public void change(int k)
    {
        if(gameStarted && !p2toggle && !p1partner && !textMode && !mapMaker && !monsterMaker)
        {  //only 1 person playing, make it so they can move with arrows or WASD
            if(k==KeyEvent.VK_W)
            {
                k = KeyEvent.VK_UP;
            }
            else if(k==KeyEvent.VK_A)
            {
                k = KeyEvent.VK_LEFT;
            }
            else if(k==KeyEvent.VK_S)
            {
                k = KeyEvent.VK_DOWN;
            }
            else if(k==KeyEvent.VK_D)
            {
                k = KeyEvent.VK_RIGHT;
            }
        }
        boolean fileRead = false;							//trouble reading in a city file?
        if(k==KeyEvent.VK_ADD || k==KeyEvent.VK_PLUS || k==KeyEvent.VK_EQUALS)
        {  //increase graphics size
            if(gameStarted)
                pause = true;
            cellSize += 2;
            return;
        }
        if (!textMode && (k==KeyEvent.VK_SUBTRACT || k==KeyEvent.VK_MINUS || k==KeyEvent.VK_UNDERSCORE))
        {  //decrease graphics size
            if(gameStarted)
                pause = true;
            cellSize -= 2;
            if(cellSize < 40)
                cellSize = 40;
            return;
        }
        if(k==KeyEvent.VK_ESCAPE)						
        {  //back out to main menu or quit from the main menu
            System.out.println("Scores updated: " + scoresUpdated);
            if(scoresUpdated)
            {
                // String fileName="scores/MM_"+cityList.get(cityListIndex);
                // if(gameMode==EARTH_INVADERS)
                // fileName="scores/EIscores.txt";
                // else
                // if(gameMode==BOMBER_DODGER)
                //     fileName="scores/BD_"+cityList.get(cityListIndex);
                // else
                //     if(gameMode==CITY_SAVER)
                //         fileName="scores/CS_"+cityList.get(cityListIndex);

                // default game mode: Earth Invaders
                String fileName = "scores/";
                if (gameMode == EARTH_INVADERS) {
                    fileName += Utilities.encode("EIscores");
                } else {
                    if (gameMode == MONSTER_MASH) {
                        fileName += Utilities.encode("MM_");
                    } else if (gameMode == BOMBER_DODGER) {
                        fileName += Utilities.encode("BD_");
                    } else if (gameMode == CITY_SAVER) {
                        fileName += Utilities.encode("CS_");
                    }
                    fileName += Utilities.encode(cityList.get(cityListIndex));
                }

                Utilities.writeToFile(highScores, fileName);
            }
            if(!gameStarted && !mapMaker && !monsterMaker)
            {
                System.exit(1);
            }
            else
            {
                resetGame();
            }
        }
        if(monsterMaker)
        {
            MonsterMaker.processKey(k);
            repaint();
            return;
        }
        if(!gameStarted && !mapMaker)							//we are entering the name of the file to load the map
        {
            if(textMode)
            {
                if(mapFileName==null)
                mapFileName = "";
                if(k==KeyEvent.VK_ENTER)
                {
                Sound.enterText();
                if(mapFileName!=null && mapFileName.length() > 0)
                { 
                    char last = mapFileName.charAt(mapFileName.length() - 1);
                    if(Character.isDigit(last))
                        fileQue.add(mapFileName);
                    else	//look to add any fileName that starts with mapFileName
                    {
                        for(String fname: mapList)
                            if(fname.startsWith(mapFileName))
                            fileQue.add(fname);
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
                repaint();
                return;
            }
            else  
            {
                if(k==KeyEvent.VK_L)								//load a map file
                textMode = true;
                else if(k==KeyEvent.VK_C)						//change from random maps to specific cities
                {
                if(gameMode == EARTH_INVADERS)
                {
                    cityListIndex = 0;
                    fileQue.clear();
                }
                else
                {
                    cityListIndex = (cityListIndex+1) % cityList.size();
                    fileQue.clear();
                    if(cityListIndex > 0)
                        for(String fname: mapList)
                            if(fname.startsWith(cityList.get(cityListIndex)))
                            fileQue.add(fname);
                }
                // String fileName="scores/MM_"+cityList.get(cityListIndex)+".txt";
                // if(gameMode==EARTH_INVADERS)
                //     fileName="scores/EIscores.txt";
                // else
                //     if(gameMode==BOMBER_DODGER)
                //         fileName="scores/BD_"+cityList.get(cityListIndex)+".txt";
                //     else
                //         if(gameMode==CITY_SAVER)
                //             fileName="scores/CS_"+cityList.get(cityListIndex)+".txt";
                String fileName = "scores/";
                if (gameMode == EARTH_INVADERS) {
                    fileName += Utilities.encode("EIscores");
                } else {
                    if (gameMode == MONSTER_MASH) {
                        fileName += Utilities.encode("MM_");
                    } else if (gameMode == BOMBER_DODGER) {
                        fileName += Utilities.encode("BD_");
                    } else if (gameMode == CITY_SAVER) {
                        fileName += Utilities.encode("CS_");
                    }
                    fileName += Utilities.encode(cityList.get(cityListIndex));
                }
                Utilities.readFile(fileName, highScores);				//read in high scores
                }
            }
        }
        if(titleScreen)
        {
            titleScreen = false;
            Sound.titleScreen();
            repaint();
            return;
        }
        if(mapMaker)
        {
            MapBuilder.keyInput(k);
            repaint();
            return;
        }

        if(k==KeyEvent.VK_M)								                  //toggle map
        {
            toggleMap = !toggleMap;
            return;
        }
        if((k==KeyEvent.VK_BRACERIGHT || k==KeyEvent.VK_CLOSE_BRACKET) && delay > 0)
        {                                                           //speed up game
            delay = Math.max(0, delay - 10);
            t.setDelay(delay);   
            return;
        }		
        if(k==KeyEvent.VK_BRACELEFT || k==KeyEvent.VK_OPEN_BRACKET) //slow down game
        {
            delay += 10;
            t.setDelay(delay); 
            return;
        }

        if(gameStarted)
        {
            if(players.length == 0 || players[PLAYER1].getHealth() <= 0)
                return;
            if(k==KeyEvent.VK_P)												   //pause game
            {
                pause = !pause;
                return;
            }
            if(k==KeyEvent.VK_Y && gameMode==MONSTER_MASH)			//change threat level
            {
                threatLevel++;
                if(threatLevel >= threatLevels.length)
                threatLevel = 0;
            }
            if(k==KeyEvent.VK_3 && gameMode!=EARTH_INVADERS)		//hit 3 key - activate AI monster
            {
                if(gameMode != CITY_SAVER)
                cheatsUsed = true;
                p3toggle = !p3toggle;
                if(!p3toggle)													//remove the AI monster that is there
                {
                Sound.removeMonster();
                if(gameMode == CITY_SAVER)
                    message = "AI monster removed";
                else
                    message = "AI friend removed";
                messageTime = numFrames;
                explosions.add(new Explosion("SMALL", players[AIPLAYER].getX()-(cellSize/2), players[AIPLAYER].getY()-(cellSize/2), puffImages, animation_delay));
                players[AIPLAYER] = new Player();
                }
                else  															//add an AI monster
                {
                Sound.addMonster();
                if(gameMode == CITY_SAVER)
                    message = "AI monster joins";
                else
                    message = "AI friend joins";
                messageTime = numFrames;
                int[] coord = Spawner.getRandomP2Spawn();
                int monsterType = (int)(Math.random()*5);
                //**************************************
                if(monsterType == 0)
                    players[AIPLAYER] = new Custom(coord[0], coord[1], customInfo, playerImages[Integer.parseInt(customInfo[1])]);
                else if(monsterType == 1)	    //ARGS:  row, col, image collection, animation delay
                    players[AIPLAYER] = new Gorilla(coord[0], coord[1], playerImages[0]);
                else if(monsterType == 2)
                    players[AIPLAYER] = new Dinosaur(coord[0], coord[1], playerImages[1]);
                else if(monsterType == 3)
                    players[AIPLAYER] = new Robot(coord[0], coord[1], playerImages[2]);
                else if(monsterType == 4)
                    players[AIPLAYER] = new Insect(coord[0], coord[1], playerImages[3]);
                else 
                    players[AIPLAYER] = new Blop(coord[0], coord[1], playerImages[4]);
                
                if(!players[AIPLAYER].isSwimmer() && board[coord[0]][coord[1]][panel].equals("~~~"))
                {
                    int tries = 0;
                    while(!players[AIPLAYER].isSwimmer() && board[coord[0]][coord[1]][panel].equals("~~~") && tries<1000)
                    {
                        coord = Spawner.getRandomP2Spawn();
                        tries++;
                    }
                    players[AIPLAYER].setRow(coord[0]);
                    players[AIPLAYER].setCol(coord[1]);
                }
                players[AIPLAYER].findX(cellSize);
                players[AIPLAYER].findY(cellSize);
                explosions.add(new Explosion("SMALL", players[AIPLAYER].getX()-(cellSize/2), players[AIPLAYER].getY()-(cellSize/2), puffImages, animation_delay));
                }
                return;
            }
            if(k==KeyEvent.VK_2 && gameMode!=EARTH_INVADERS)	//activates or deactivates a 2nd player as a vehicle
            {
                if(gameMode == CITY_SAVER)
                cheatsUsed = true;
                p2toggle = !p2toggle; 
                if(!p2toggle)														//remove player 2 vehicle
                {
                Sound.removeMonster();
                message = "Player 2 removed";
                messageTime = numFrames;
                explosions.add(new Explosion("SMALL", players[PLAYER2].getX()-(cellSize/2), players[PLAYER2].getY()-(cellSize/2), puffImages, animation_delay));
                players[PLAYER2] = new Player();
                }
                else  																//add player 2 vehicle
                {
                Sound.addMonster();
                p1partner = false;
                if(!players[PLAYER2].getName().equals("NONE"))		//if we are switching from monster to vehicle, leave a puff as monster leaves
                    explosions.add(new Explosion("BIG", players[PLAYER2].getX()-(cellSize/2), players[PLAYER2].getY()-(cellSize/2), puffImages, animation_delay));
                
                message = "Player 2 joins";
                messageTime = numFrames;
                ArrayList<String> units = Spawner.findAvailableP2Units();
                int randUnit = (int)(Math.random()*units.size());
                String unit = units.get(randUnit);
                int rand = 0;
                int[] coord;
                if(unit.equals("TANK missile"))
                {
                    rand = (int)(Math.random() * p2CarSpawnPoints[panel].size());
                    coord = p2CarSpawnPoints[panel].get(rand);
                    players[PLAYER2] = (new Vehicle("TANK missile", coord[0], coord[1],missileLauncherImages, animation_delay, 3, 200, 200000)); 
                }
                else
                    if(unit.equals("TANK artillery"))
                    {
                        rand = (int)(Math.random() * p2CarSpawnPoints[panel].size());
                        coord = p2CarSpawnPoints[panel].get(rand);
                        players[PLAYER2] = (new Vehicle("TANK artillery", coord[0], coord[1],artilleryImages, animation_delay, 3, 100, 80000)); 
                    }
                    else
                        if(unit.equals("TANK tank"))
                        {
                            rand = (int)(Math.random() * p2TankSpawnPoints[panel].size());
                            coord = p2TankSpawnPoints[panel].get(rand);
                            players[PLAYER2] = (new Vehicle("TANK tank", coord[0], coord[1],tankImages, animation_delay, 3, 100, 100000)); 
                        }
                        else
                            if(unit.equals("TANK flame"))
                            {
                            rand = (int)(Math.random() * p2TankSpawnPoints[panel].size());
                            coord = p2TankSpawnPoints[panel].get(rand);
                            players[PLAYER2] = (new Vehicle("TANK flame", coord[0], coord[1],flameTankImages, animation_delay,  2, 100, 150000)); 
                            }
                            else
                            if(unit.equals("CAR jeep"))
                            {
                                rand = (int)(Math.random() * p2CarSpawnPoints[panel].size());
                                coord = p2CarSpawnPoints[panel].get(rand);
                                players[PLAYER2] = (new Vehicle("CAR jeep", coord[0], coord[1],jeepImages, animation_delay, 2, 50, 35000)); 
                            }
                            else
                                if(unit.equals("CROWD troops"))
                                {
                                    rand = (int)(Math.random() * p2CarSpawnPoints[panel].size());
                                    coord = p2CarSpawnPoints[panel].get(rand);
                                    players[PLAYER2] = (new Vehicle("CROWD troops",  coord[0], coord[1], troopImages, animation_delay*2, 4, 25, 0)); 
                                }
                                else
                                    if(unit.equals("CYCLE police"))
                                    {
                                        rand = (int)(Math.random() * p2CarSpawnPoints[panel].size());
                                        coord = p2CarSpawnPoints[panel].get(rand);
                                        players[PLAYER2] = (new Vehicle("CYCLE police", coord[0], coord[1], policeMotorcycleImages, animation_delay*2, 1, 50, 8000)); 
                                    }
                                    else
                                    {
                                        rand = (int)(Math.random() * p2CarSpawnPoints[panel].size());
                                        coord = p2CarSpawnPoints[panel].get(rand);
                                        players[PLAYER2] = (new Vehicle("CAR police", coord[0], coord[1], policeImages, animation_delay*2, 2, 75, 25000)); 
                                    }
                players[PLAYER2].findX(cellSize);
                players[PLAYER2].findY(cellSize);
                explosions.add(new Explosion("SMALL", players[PLAYER2].getX()-(cellSize/2), players[PLAYER2].getY()-(cellSize/2), puffImages, animation_delay));
                }
                return;
            }
            if(k==KeyEvent.VK_1)								//activates or deactivates a 2nd player as a monster
            {
                if(gameMode != CITY_SAVER)
                cheatsUsed = true;
                p1partner = !p1partner; 
                if(!p1partner)									//remove player 2 monster
                {
                Sound.removeMonster();
                message = "Player 2 removed";
                messageTime = numFrames;
                explosions.add(new Explosion("BIG", players[PLAYER2].getX()-(cellSize/2), players[PLAYER2].getY()-(cellSize/2), puffImages, animation_delay));
                players[PLAYER2] = new Player();
                }
                else  											//add player 2 monster
                {
                Sound.addMonster();
                p2toggle = false;
                int[] coord = Spawner.getRandomP2Spawn();
                if(!players[PLAYER2].getName().equals("NONE"))		//if we are switching from vehicle to monster, leave a puff as vehicle leaves
                    explosions.add(new Explosion("SMALL", players[PLAYER2].getX()-(cellSize/2), players[PLAYER2].getY()-(cellSize/2), puffImages, animation_delay));
                message = "Player 2 joins";
                messageTime = numFrames;
                int monsterType = (int)(Math.random()*5);
                //************************************************
                if(gameMode==EARTH_INVADERS)
                {
                    coord[0] = board.length-2;
                    coord[1] = board[0].length/2;
                    if(monsterType == 0)
                        players[PLAYER2] = new Custom(coord[0], coord[1], customInfo, playerImages[Integer.parseInt(customInfo[1])], 100, 0, 30);
                    else if(monsterType == 1)	//name, row, col, anim images, anim speed, stomp power, speed penalty, reload time
                        players[PLAYER2] = (new Gorilla("King-Clunk",  coord[0], coord[1], playerImages[0], 100, 0, 30));
                    else	if(monsterType == 2)
                        players[PLAYER2] = (new Dinosaur("Gobzilly",  coord[0], coord[1], playerImages[1], 100, 0, 30));
                    else if(monsterType == 3)
                        players[PLAYER2] = (new Robot("BoobooTron",  coord[0], coord[1], playerImages[2], 100, 0, 30));
                    else if(monsterType == 4)
                        players[PLAYER2] = (new Insect("WoeMantis",  coord[0], coord[1], playerImages[3], 100, 0, 30));
                    else
                        players[PLAYER2] = (new Blop("The-Blop", coord[0], coord[1], playerImages[4], 100, 0, 30));
                }
                else
                {
                    if(monsterType == 0)
                        players[PLAYER2] = new Custom(coord[0], coord[1], customInfo, playerImages[Integer.parseInt(customInfo[1])]);
                    else if(monsterType == 1)	//row, col, anim images, anim speed
                        players[PLAYER2] = new Gorilla(coord[0], coord[1], playerImages[0]);
                    else if(monsterType == 2)
                        players[PLAYER2] = new Dinosaur(coord[0], coord[1], playerImages[1]);
                    else if(monsterType == 3)
                        players[PLAYER2] = new Robot(coord[0], coord[1], playerImages[2]);
                    else if(monsterType == 4)
                        players[PLAYER2] = new Insect(coord[0], coord[1], playerImages[3]);
                    else
                        players[PLAYER2] = new Blop(coord[0], coord[1], playerImages[4]);
                }
                if(!players[PLAYER2].isSwimmer() && board[coord[0]][coord[1]][panel].equals("~~~"))
                {
                    int tries = 0;
                    while(!players[PLAYER2].isSwimmer() && board[coord[0]][coord[1]][panel].equals("~~~") && tries<1000)
                    {
                        coord = Spawner.getRandomP2Spawn();
                        tries++;
                    }
                    players[PLAYER2].setRow(coord[0]);
                    players[PLAYER2].setCol(coord[1]);
                }
                
                players[PLAYER2].findX(cellSize);
                players[PLAYER2].findY(cellSize);
                explosions.add(new Explosion("SMALL", players[PLAYER2].getX()-(cellSize/2), players[PLAYER2].getY()-(cellSize/2), puffImages, animation_delay));
                }
                return;
            }
        
            Player curr = players[PLAYER1];		//players[0] is person playing the game
        //cancel move order if we are already moving from one space to the next
            if(k==KeyEvent.VK_COMMA || k==KeyEvent.VK_PERIOD)			//turn head
            {
                String cleanDir = "left";
                if(k==KeyEvent.VK_PERIOD)
                cleanDir = "right";
                Utilities.turnHead(curr, cleanDir);
                return;
            }
            if(k==KeyEvent.VK_SHIFT && gameMode!=CITY_SAVER && curr instanceof Monster)	//eat	
            {
                Utilities.monsterEat((Monster)(curr));
                return;
            }
            if(k==KeyEvent.VK_CONTROL && gameMode!=CITY_SAVER && curr instanceof Monster)		//if there is a car or people in the direction you are facing, grab it
            {
                Utilities.grab((Monster)(curr));
                return;
            }
            if(k==KeyEvent.VK_ENTER)		//throw a car in your hand (if you have one), or breathe fire or shoot death beam
            {
                playerShoot(curr, 0);
                return;
            }
        
            if(!(curr.isMoving() || (curr instanceof Monster && ((Monster)(curr)).isJumping())) && (!Utilities.p2Command(k)))
            {  //if we are not in the middle of a move, we are a monster that is not jumping and we did not issure a command for player 2
                playerDirect(k, 0);
                return;
            }
            if(p2toggle || p1partner)
            {
                Player curr2 = players[PLAYER2];		//players[0] is person playing the game
            //cancel move order if we are already moving from one space to the next
                if(k==KeyEvent.VK_Z || k==KeyEvent.VK_X)		//p2 turn head
                {
                String cleanDir = "left";
                if(k==KeyEvent.VK_X)
                    cleanDir = "right";
                Utilities.turnHead(curr2, cleanDir);
                return;
                }
                if(p1partner)
                {
                if(k==KeyEvent.VK_E && curr2 instanceof Monster)			//player 2 eat	
                {
                    Utilities.monsterEat((Monster)(curr2));
                    return;
                }
                
                if(k==KeyEvent.VK_G && curr2 instanceof Monster)		  //if there is a car or people in the direction you are facing, grab it
                {
                    Utilities.grab((Monster)(curr2));
                    return;
                }
                
                if(k==KeyEvent.VK_C)		//throw a car in your hand (if you have one), or breathe fire or shoot death beam
                {
                    playerShoot(curr2, 1);
                    return;
                }
                }
            
                if(p2toggle && k==KeyEvent.VK_C)
                {
                AImovement.vehicleShoot(curr2, 1);
                return;
                }
            
                if(curr2.isMoving())
                return;
                curr2.clearDirections();
                int r2 = curr2.getRow();
                int c2 = curr2.getCol();
                if(p2toggle)	//player 2 vehicle movements
                {					//move up
                if(k==KeyEvent.VK_W)	  //we can't walk from under an impassable structure to behind it (unless we destroy it first)
                {
                    if((curr2.getBodyDirection() == UP && r2>1 && MapBuilder.noStructure(r2-1, c2, panel)))
                    {
                        Structure str = structures[r2-1][c2][panel];
                        if(str != null && str.getName().startsWith("Blop") && str.getHealth() != 0 && !curr2.getName().endsWith("tank")  && !curr2.getName().endsWith("flame"))
                        {//if we are not a tank or flame tank and there is Blop-glop above us, don't move into it (just change orientation)
                            curr2.setBodyDirection(UP);
                            curr2.setHeadDirection(UP);
                        }
                        else if(str != null && str.getName().equals("hole") && str.getHealth() != 0)
                        {//if there is a hole there, don't move into it (just change orientation)
                            curr2.setBodyDirection(UP);
                            curr2.setHeadDirection(UP);
                        }
                        else
                            curr2.setDirection(UP);
                    }
                    else
                    {
                        curr2.setBodyDirection(UP);
                        curr2.setHeadDirection(UP);
                    }
                }
                else 
                    if(k==KeyEvent.VK_S)		//move down
                    {
                        if((curr2.getBodyDirection() == DOWN && r2 < board.length-2 && MapBuilder.noStructure(r2, c2, panel)))
                        {
                            Structure str = structures[r2+1][c2][panel];
                            if(str != null && str.getName().startsWith("Blop") && str.getHealth() != 0 && !curr2.getName().endsWith("tank")  && !curr2.getName().endsWith("flame"))
                            {//if we are not a tank or flame tank and there is Blop-glop below us, don't move into it (just change orientation)
                            curr2.setBodyDirection(DOWN);
                            curr2.setHeadDirection(DOWN);
                            }
                            else if(str != null && str.getName().equals("hole") && str.getHealth() != 0)
                            {//if there is a hole there, don't move into it (just change orientation)
                            curr2.setBodyDirection(DOWN);
                            curr2.setHeadDirection(DOWN);
                            }
                            else
                            curr2.setDirection(DOWN);
                        }
                        else
                        {
                            curr2.setBodyDirection(DOWN);	//here, we might be behind a structure, but we don't want to walk through it if it is impassable
                            curr2.setHeadDirection(DOWN);
                        }
                    }
                    else								//move left
                        if(k==KeyEvent.VK_A)		//we can walk behind structures from the left and right
                        {								//so we dont have to check to see if there is a structure here
                            if((curr2.getBodyDirection() == LEFT && c2>1))// || curr2.head360())
                            {
                            Structure str = structures[r2][c2-1][panel];
                            if(str != null && str.getName().startsWith("Blop") && str.getHealth() != 0 && !curr2.getName().endsWith("tank")  && !curr2.getName().endsWith("flame"))
                            {//if we are not a tank or flame tank and there is Blop-glop left of us, don't move into it (just change orientation)
                                curr2.setBodyDirection(LEFT);
                                curr2.setHeadDirection(LEFT);
                            }
                            else if(str != null && str.getName().equals("hole") && str.getHealth() != 0)
                            {//if there is a hole there, don't move into it (just change orientation)
                                curr2.setBodyDirection(LEFT);
                                curr2.setHeadDirection(LEFT);
                            }
                            else
                                curr2.setDirection(LEFT);
                            }
                            else
                            {
                            curr2.setBodyDirection(LEFT);
                            curr2.setHeadDirection(LEFT);
                            }
                        }
                        else 
                            if(k==KeyEvent.VK_D)			//move right
                            {
                            if((curr2.getBodyDirection() == RIGHT && c2<board[0].length-2))// || curr2.head360())
                            {
                                Structure str = structures[r2][c2+1][panel];
                                if(str != null && str.getName().startsWith("Blop") && str.getHealth() != 0 && !curr2.getName().endsWith("tank")  && !curr2.getName().endsWith("flame"))
                                {//if we are not a tank or flame tank and there is Blop-glop RIGHT of us, don't move into it (just change orientation)
                                    curr2.setBodyDirection(RIGHT);
                                    curr2.setHeadDirection(RIGHT);
                                }
                                else if(str != null && str.getName().equals("hole") && str.getHealth() != 0)
                                {//if there is a hole there, don't move into it (just change orientation)
                                    curr2.setBodyDirection(RIGHT);
                                    curr2.setHeadDirection(RIGHT);
                                }
                                else
                                    curr2.setDirection(RIGHT);
                            }
                            else
                            {
                                curr2.setBodyDirection(RIGHT);
                                curr2.setHeadDirection(RIGHT);
                            }
                            }
                return;
                }
                else  //p1partner player 2 monster movements (& stomp)
                {
                if(!(curr2.isMoving() || (curr2 instanceof Monster && ((Monster)(curr2)).isJumping())) && (Utilities.p2Command(k)))
                {	//if we are not in the middle of a move, we are a monster that is not jumping and issued a player 2 command
                    playerDirect(k, 1);
                    return;
                }
                
                }
            }
        }
        else		//game has not stareted yet - select monster and build map
        {
            int monsterType =  0;   
            boolean needToStart = false;  
            if(k==KeyEvent.VK_0)				//hit 0 key
            {
                monsterType = 0;				//select custom monster
                needToStart = true;
            }    
            else if(k==KeyEvent.VK_1)		//hit 1 key
            {
                monsterType = 1;				//select King-Clunk
                needToStart = true; 
            }
            else if(k==KeyEvent.VK_2)		//hit 2 key
            {
                monsterType = 2;				//select Gobzilly
                needToStart = true; 
            }
            else if(k==KeyEvent.VK_3)		//hit 3 key
            {
                monsterType = 3;			//select BoobooTron
                needToStart = true; 
            }
            else if(k==KeyEvent.VK_4)		//hit 4 key
            {
                monsterType = 4;			//select WoeMantis
                needToStart = true; 
            }
            else if(k==KeyEvent.VK_5)		//hit 5 key - pick random vehicle or The-Blop
            {
                if(gameMode == CITY_SAVER){
                    monsterType = (int)(Math.random()*4);
                    needToStart = true;
                }else {
                    monsterType = 5;
                    needToStart = true;
                }
            //****************************************** 
            }
            else if(k==KeyEvent.VK_6)		//hit 6 key
            {
            
            }
            else if(k==KeyEvent.VK_7)		//hit 7 key - pick random monster
            {
                monsterType = (int)(Math.random()*5);
                needToStart = true;
                //****************************************** 
            }
            else if(k==KeyEvent.VK_8)     //hit 8 key - map maker
            {
                mapFileName = null;
                textMode = false;
                MapBuilder.clearPanels();
                SHOWBORDER = false;
                mapMaker = true;
                mouseX = (board[0].length / 2) * cellSize;
                mouseY = (board.length / 2) * cellSize; 
            }
            else if(k==KeyEvent.VK_9)		//9 key - enter monster maker mode to create custom monster
            {
                textMode = false;
                monsterMaker = true;
            }
            else if(k==KeyEvent.VK_D)		//hit D key - change difficulty
            {
                difficulty = (difficulty+1)%5; 
            }
            else if(k==KeyEvent.VK_G)		//hit G key - change game mode
            {
                gameMode = (gameMode + 1) % 4; 
                String fileName="scores/MM_"+cityList.get(cityListIndex)+".txt";
                if(gameMode==EARTH_INVADERS)		
                {
                fileName="scores/EIscores.txt";	//only random gen maps for earth invaders
                cityListIndex = 0;
                fileQue.clear();
                }
                else
                if(gameMode==BOMBER_DODGER)
                    fileName="scores/BD_"+cityList.get(cityListIndex)+".txt";
                else
                    if(gameMode==CITY_SAVER)
                        fileName="scores/CS_"+cityList.get(cityListIndex)+".txt";
                Utilities.readFile(fileName, highScores);				//read in high scores
            }
            if(needToStart)
            {	
                if(gameMode ==CITY_SAVER)
                {			
                int numPlayers = 21;
                players = new Player[numPlayers];																																																		
                if(monsterType == 1)//name, row, col, anim images, anim speed, speed penalty, reload time, property value
                    players[PLAYER1] = (new Vehicle("CAR-jeep",  board.length/2, board[0].length/2, jeepImages, animation_delay, 0, 35, 35000));
                else
                    if(monsterType == 2)
                        players[PLAYER1] = (new Vehicle("TANK-tank",  board.length/2, board[0].length/2, tankImages, animation_delay, 2, 75, 100000));
                    else
                        if(monsterType == 3)
                            players[PLAYER1] = (new Vehicle("TANK-flame",  board.length/2, board[0].length/2, flameTankImages, animation_delay, 1, 75, 150000));       
                        else 
                            players[PLAYER1] = (new Vehicle("TANK-missile", board.length/2, board[0].length/2, missileLauncherImages, animation_delay, 3, 175, 200000)); 
                }
                else
                {
                int[] coord = new int[2];
                coord[0] =  board.length/2;
                coord[1] =  board[0].length/2;
                if(gameMode==EARTH_INVADERS)
                {
                    int numPlayers = 67;
                    players = new Player[numPlayers];
                    for(int i=0; i<players.length; i++)
                        players[i] = new Player();
                    coord[0] = board.length-2;
                    coord[1] = board[0].length/2;
                    if(monsterType == 0)
                        players[PLAYER1] = new Custom(coord[0], coord[1], customInfo, playerImages[Integer.parseInt(customInfo[1])], 100, 0, 30);
                    else if(monsterType == 1)	//name, row, col, anim images, anim speed, stomp power, speed penalty, reload time
                        players[PLAYER1] = (new Gorilla("King-Clunk",  coord[0], coord[1], playerImages[0], 100, 0, 30));
                    else if(monsterType == 2)
                        players[PLAYER1] = (new Dinosaur("Gobzilly", coord[0], coord[1], playerImages[1], 100, 0, 30));
                    else if(monsterType == 3)
                        players[PLAYER1] = (new Robot("BoobooTron", coord[0], coord[1], playerImages[2], 100, 0, 30));
                    else if(monsterType == 4)
                        players[PLAYER1] = (new Insect("WoeMantis",  coord[0], coord[1], playerImages[3], 100, 0, 30));
                    else 
                        players[PLAYER1] = (new Blop("The-Blop", coord[0], coord[1], playerImages[4], 100, 0, 30));
                }
                else
                {	
                    int numPlayers = 21;
                    if(gameMode == BOMBER_DODGER)
                        numPlayers = 25;
                    players = new Player[numPlayers];
                    for(int i=0; i<players.length; i++)
                        players[i] = new Player();
                    if(monsterType == 0)
                        players[PLAYER1] = new Custom(coord[0], coord[1], customInfo, playerImages[Integer.parseInt(customInfo[1])]);
                    else if(monsterType == 1)	//row, col, anim images, anim speed
                        players[PLAYER1] = new Gorilla(coord[0], coord[1], playerImages[0]);
                    else if(monsterType == 2)
                        players[PLAYER1] = new Dinosaur(coord[0], coord[1], playerImages[1]);
                    else if(monsterType == 3)
                        players[PLAYER1] = new Robot(coord[0], coord[1], playerImages[2]);
                    else if(monsterType == 4)
                        players[PLAYER1] = new Insect(coord[0], coord[1], playerImages[3]); 
                    else 
                        players[PLAYER1] = new Blop(coord[0], coord[1], playerImages[4]);
                }
                if(players[PLAYER1].healInWater())
                {
                    forceWater = true;
                }
                else
                    if(players[PLAYER1].energyAbsorber())
                    {
                        forceTracks = true;
                    }
                }
                if(fileQue==null || fileQue.isEmpty())
                MapBuilder.generateMap(); 
                else 
                {
                MapBuilder.initializePanels();  
                for(String fileName: fileQue)
                {
                    fileRead = Utilities.readFileToBoard(fileName);
                    if(fileRead == false) 
                    {
                        MapBuilder.generateMap(); 
                        message = "Error loading map";
                        messageTime = numFrames;
                        break;
                    } 
                }
                MapBuilder.findSpecialPanels();
                }
            //count the # of inhabitable structures in the panel           
                for(int p=0; p<structures[0][0].length; p++)
                numStructures[p]=MapBuilder.countStructures(p);
            //build collection of spawn points
                MapBuilder.buildSpawnPoints();    
            
                int mr = players[PLAYER1].getRow();
                int mc = players[PLAYER1].getCol();
                if(!players[PLAYER1].isSwimmer() && board[mr][mc][panel].equals("~~~"))
                {
                int tries = 0;
                int[] coord =  Spawner.getRandomP2Spawn();
                while(!players[PLAYER1].isSwimmer() && board[coord[0]][coord[1]][panel].equals("~~~") && tries<1000)
                {
                    coord = Spawner.getRandomP2Spawn();
                    tries++;
                }
                players[PLAYER1].setRow(coord[0]);
                players[PLAYER1].setCol(coord[1]);
                }
            
                if(players[PLAYER1].healInWater())
                {	//...now that the map is generated, drop player into a spot with water
                if(waterPanels.size() > 0)
                {
                    panel = waterPanels.get((int)(Math.random()*waterPanels.size()));
                    if(panel==2 || panel==5 || panel==8)			//water is in right panel, so start facing left			
                    {
                        players[PLAYER1].setBodyDirection(LEFT);
                        players[PLAYER1].setHeadDirection(LEFT);
                    }
                    ArrayList<int[]> coords = new ArrayList();	//store locations of water cells so we can pick one at random
                    for(int r=1; r<board.length-1; r++)				//we don't want to add the borders because they are hidden
                        for(int c=1; c<board[0].length-1; c++)
                            if(board[r][c][panel].equals("~~~") && (r==board.length/2 || c==board[0].length/2))
                            {	//add water cells that are in the center of the board
                            int[] coord = new int[2];
                            coord[0] = r;
                            coord[1] = c;
                            coords.add(coord);
                            }
                    if(coords.size() > 0)
                    {      
                        int[] ourSpot =  coords.get((int)(Math.random()*coords.size()));
                        players[PLAYER1].setRow(ourSpot[0]);
                        players[PLAYER1].setCol(ourSpot[1]);
                    }
                }
                }
            
                Spawner.initialVehicleSpawn(panel);
                players[PLAYER2] = new Player();
            
                t.setDelay(delay);			//the higher the value of delay, the slower the enemy will move
                t.restart();
                gameStarted = true;
            }
        }
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g); 
        ImageDisplay.drawMainScreen(g);
    }


    private class Listener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)	//this is called for each timer iteration - make the enemy move randomly
        {
            if(numFrames == Integer.MAX_VALUE)					//roll over frame count and reset shot times if we get to max int value
            {
                numFrames = 0;
                if(!pause && gameStarted)
                {
                for(int i=0; i<players.length; i++)
                {
                    if(players[i]==null || players[i].getName().equals("NONE"))	//player 2 might not be activated
                        continue;
                    players[i].setLastShotTime(0);
                }
                }
            }
            numFrames++;
        
            if(!pause && gameStarted)
            {
                Player curr = players[PLAYER1];
                Player curr2 = players[PLAYER2];
                movePlayerToAdjacentCell();
                AImovement.makeEnemyMove();
                Ordinance.moveBullets();
                checkCollisions();
                if(numFrames%500==0 && curr.getHealth() > 0)
                {
                time++;   
                Utilities.clearGlop();
                Sound.silence();
                }
                for(int m=0; m<players.length; m++)
                {
                Player monster = players[m];
                if(m!=0 && (monster==null || monster.getName().equals("NONE") || !(monster instanceof Monster)))
                    continue;
                Utilities.updateMonsterStatus(m);
                }
                if(p2toggle)
                {
                if(board[curr2.getRow()][curr2.getCol()][panel].startsWith("~"))	//we are in the water
                {
                    explosions.add(new Explosion("SMALL", curr2.getX()-(cellSize/2), curr2.getY()-(cellSize/2), puffImages, animation_delay));
                    Spawner.resetEnemy(PLAYER2);
                }
                }
                Ordinance.manageFire();
            }
            repaint();
        }
    }

    //***BEGIN MOUSE STUFF***
    public void mouseClicked( MouseEvent e )
    {
        if(titleScreen)
        {
            titleScreen = false;
            Sound.titleScreen();
            repaint();
            return;
        }
        int button = e.getButton();
        mouseX = e.getX();
        mouseY = e.getY();
        int mouseRow = mouseY/cellSize;
        int mouseCol = mouseX/cellSize;
        if(gameStarted || mapMaker)
        {
            if(button == MouseEvent.BUTTON1)		//shoot
            {
                change(KeyEvent.VK_ENTER);
            }
            else
                if(button == MouseEvent.BUTTON3)
                {
                change(KeyEvent.VK_SPACE);		//space
                }
        }
        if (monsterMaker)	
        {
            Sound.selection();
            if(highlightArea >= 0 && highlightArea<26)
                MonsterMaker.changeAttribute(button);
        }
        else if(!gameStarted)		//game has not started - monster select
        {
            Sound.selection();
            if(highlightArea == 0)
                change(KeyEvent.VK_0);
            else if(highlightArea == 1)
                change(KeyEvent.VK_1);
            else if(highlightArea == 2)
                change(KeyEvent.VK_2);
            else if(highlightArea == 3)
                change(KeyEvent.VK_3);
            else if(highlightArea == 4)
                change(KeyEvent.VK_4);
            else if(highlightArea == 5)
                change(KeyEvent.VK_5);
            else if(highlightArea == 6)
                change(KeyEvent.VK_6);
            else if(highlightArea == 7)
                change(KeyEvent.VK_7);
            else if(highlightArea == 8)
                change(KeyEvent.VK_8);
            else if(highlightArea == 9)
                change(KeyEvent.VK_9);
        }
    }

    public void mousePressed( MouseEvent e )
    {}

    public void mouseReleased( MouseEvent e )
    {}

    public void mouseEntered( MouseEvent e )
    {}

    public void mouseMoved( MouseEvent e)
    {
        mouseX = e.getX();
        mouseY = e.getY();
        int mouseRow = mouseY/cellSize;
        int mouseCol = mouseX/cellSize;
    //System.out.println(mouseRow + " " + mouseCol + " " + cellSize);
    //System.out.println(mouseX + " " + mouseY);
        if(gameStarted)
        {
            Player curr = players[PLAYER1];
            int playerRow = players[PLAYER1].getRow();
            int playerCol = players[PLAYER1].getCol();
            int horizDiff = (mouseCol - playerCol);
            int vertDiff = (mouseRow - playerRow);
            int bodyDir = curr.getBodyDirection();
            if(horizDiff==vertDiff && horizDiff==0)
                cursorIndex = 4;		//center cursor
            else
                if(Math.abs(horizDiff) < Math.abs(vertDiff))
                {
                if(vertDiff < 0)
                    cursorIndex = UP;
                else
                    cursorIndex = DOWN;   
                }
                else
                if(horizDiff < 0)
                    cursorIndex = LEFT; 
                else
                    cursorIndex = RIGHT;
        
            if(curr.getMoveIncrY()!=0  || curr.getMoveIncrX()!=0 || pause ) 
                return;
            if(horizDiff==vertDiff && horizDiff==0)
            {
            }
            else
                if(Math.abs(horizDiff) < Math.abs(vertDiff))
                {	//move up or down
                if(vertDiff < 0)
                {
                    if(Math.abs(vertDiff)<=1 && playerRow > 1)		//only change direction if mouse is within one unit away
                    {
                        curr.setBodyDirection(UP);
                        curr.setHeadDirection(UP);
                    }
                    else
                        change(KeyEvent.VK_UP);				//only move monster if mouse is more than one unit away
                }
                else
                {
                    if(Math.abs(vertDiff)<=1 && playerRow < board.length-2)
                    {
                        curr.setBodyDirection(DOWN);
                        curr.setHeadDirection(DOWN);
                    }
                    else
                        change(KeyEvent.VK_DOWN);
                }
                }
                else
                {	//move left or right
                if(horizDiff < 0)
                {
                    if(Math.abs(horizDiff)<=1 && playerCol > 1)
                    {
                        curr.setBodyDirection(LEFT);
                        curr.setHeadDirection(LEFT);
                    }
                    else
                        change(KeyEvent.VK_LEFT);
                }
                else
                {
                    if(Math.abs(horizDiff)<=1 && playerCol < board[0].length-2)
                    {
                        curr.setBodyDirection(RIGHT);
                        curr.setHeadDirection(RIGHT);
                    }
                    else
                        change(KeyEvent.VK_RIGHT);
                }
                }
        }
        else		//game not started - highlight area for monster selection
        {
            if(monsterMaker)
            {
                mouseRow = mouseY/(cellSize/2);
                mouseCol = mouseX/(cellSize/2);
            //System.out.println(mouseRow + " " + mouseCol + " " + cellSize);
                highlightArea = mouseRow - 3;
            }
            else
            {
                if(mouseRow == 2)
                highlightArea = 0;
                else if(mouseRow == 3)
                highlightArea = 1;
                else if(mouseRow == 5)
                highlightArea = 2;
                else if(mouseRow == 6)
                highlightArea = 3;
                else if(mouseRow == 7)
                highlightArea = 4;
                else if(mouseRow == 8)
                highlightArea = 5;
                else if(mouseRow == 10)
                highlightArea = 6;
                else if(mouseRow == 11)
                highlightArea = 7;
                else if(mouseRow == 12)
                highlightArea = 8;
                else if(mouseRow == 13)
                highlightArea = 9;   
                else
                highlightArea = -1;
            }
        }

    }

    public void mouseDragged( MouseEvent e) {}

    public void mouseExited( MouseEvent e ) {}

    public void mouseWheelMoved(MouseWheelEvent e) {
        if(gameStarted && !pause) {
            Player curr = players[PLAYER1];
            int notches = e.getWheelRotation();
            if (notches <= -WHEEL_SENSITIVITY && numFrames > curr.getHeadTurnTime()) {
                Utilities.turnHead(curr, "left");
                curr.setHeadTurnTime(numFrames + 10);
            }else {
                if (notches >= WHEEL_SENSITIVITY && numFrames > curr.getHeadTurnTime()) {
                Utilities.turnHead(curr, "right");
                curr.setHeadTurnTime(numFrames + 10);
                }
            }
        }
    }

    //***END MOUSE STUFF***/
}
