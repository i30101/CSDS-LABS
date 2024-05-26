//D Oberle 6/5/23
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.Timer;                          //there is a swing timer and an awt timer, so we have to be specific and not use .*
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Point;
import java.awt.event.*;
import java.util.HashSet;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
  
public class GamePanel extends JPanel implements KeyListener, MouseListener, MouseMotionListener
{
   private static int screenWidth,screenHeight;    //size of screen to be drawn

   private static int cellSize;                    //pixel size of a game-board cell to be drawn (scales for size of the board)
   private static int textSize;                    //font size - consistent so the buttons are the same size when the game board might be different
   private static final int DELAY = 1;	            //#miliseconds delay between each time the screen refreshes for the timer
   private static Timer t;							      //used to control what happens each frame (game code)
   private static int frame;                       //keep track of # of elapsed frames to control the speed of events
   private static long time,startTime;             //the time that we complete the puzzle in for the high-score
   private static int flashTime;                   //used to time the screen flashing with a collission
   private static int textAlpha;                   //use to dissolve in the text on the screen
   protected static List<Circle> circles;          //smoke puffs from revealing a game-board cell
   private static final String fontName = "Monospaced"; 
    
   protected static final int STARTSCREEN=0, GAME=1, AFTERMATH=2, INSTRUCTIONS=3; //different game modes
   private static int gameMode;                    //either STARTSCREEN, GAME, INSTRUCTIONS or AFTERMATH
    
   protected static int playerR, playerC;          //position of the player cursor in game-board row/column terms 
   
                                                   //image under the game-board tiles to reveal - randomly picked 
   protected static final ImageIcon [] fossils = { new ImageIcon("images/fossil1.jpg"), new ImageIcon("images/fossil2.jpg"), 
                                                   new ImageIcon("images/fossil3.jpg"), new ImageIcon("images/fossil4.jpg"), 
                                                   new ImageIcon("images/fossil5.jpg"), new ImageIcon("images/fossil6.jpg"),
                                                   new ImageIcon("images/fossil7.jpg"), new ImageIcon("images/fossil8.jpg"),
                                                   new ImageIcon("images/fossil9.jpg"), new ImageIcon("images/fossil10.jpg")};
   private static int fossilIndex;                 //index of which fossil we pick from the fossils array
   private static final ImageIcon crosshair = new ImageIcon("images/crosshair.gif");
   private static final ImageIcon depthLayer = new ImageIcon("images/depth.jpg");
   private static final ImageIcon grassImage = new ImageIcon("images/grass.jpg");
   private static final ImageIcon grassFront = new ImageIcon("images/grassFront.gif");
   
   private static final ImageIcon introPicture = new ImageIcon("images/introPicture.jpg");

   private static int difficulty;                  //0, 1, 2 or 3 for classic, expert, advanced and beginner - affects the # of strikes given at the start of the game
   protected static final int BEGINNER = 3, ADVANCED = 2, EXPERT = 1, CLASSIC = 0, NUMDIFFICULTIES = 4;
   private static final String [] difficulties = {"Classic", "Expert","Advanced","Beginner"};   //descriptions for option button
   private static int numStrikes;                  //the number of times you can hit an ant hill before losing
   
   private static int size;                        //0, 1 or 2 for small, medium or large - affects the size of the game board
   protected static final int SMALL = 0, MEDIUM = 1, LARGE = 2, NUMSIZES = 3;
   private static final String [] sizes = {"Small","Medium","Large"};                           //descriptions for option button
   
   private Button [][] buttons;                    //buttons for each cell of the game board.  Each button stores a value for the number of adjacent cells with an ant hill
   protected static final int ANTHILL = 9;         //value that represents an ant hill
   
   private Button [] options;                      //buttons for reset game, size, difficulty and quit
   private static int RESET_BTN = 0, SIZE_BTN = 1, DIFFICULTY_BTN = 2, INSTR_BTN = 3, SOUND_BTN = 4, STYLE_BTN = 5, QUIT_BTN = 6, NUM_BUTTONS = 7;
   private static int optionIndex;                 //index of which one of the option buttons the keyboard-using client is on
                                                   //-1 if they are not on the option buttons and on the game board
   
   private static final String [] soundOptions = {"All on", "No music", "All off"};             //descriptions for option button
   protected static final int ALL_ON = 0, NO_MUSIC = 1, ALL_OFF = 2, NUM_SOUND_OPTIONS = 3;
   private static int soundIndex;                  //0, 1 or 2 for All on, No music or All off
   
   private static final String [] styleOptions = {"Ants", "Numbers"};                           //descriptions for option button
   protected static final int ANT_STYLE = 0, NUMBER_STYLE = 1, NUM_STYLE_OPTIONS = 2;
   private static int styleIndex;                  //0 for ants, 1 for numbers
   
   private static boolean optionsChanged;          //used to know if the client has selected some different options to write out to the file to remember for the next game

   private static ArrayList<Score> highScores;     //high-scores for each difficulty and size board
   private static Score currentHighScore;          //the current high score for the current difficulty and puzzle size
   private static boolean highScoreAchieved;       //did we get a high-score this round?

   private static boolean winState, loseState;     //keeps track if the game should end by winning (finding all ant hills) or losing (running out of strikes)
   private static int numMines, flagsPlaced;       //number of ant hills in the level and the number of flags currently placed
   
   protected static int mouseX, mouseY;	         //locations for the mouse pointer
   protected static int startX, startY;            //location of where the game board will start to be drawn
          
    //pre:  w and h are the width and height of the size of the primary monitor, sent from the JFrame in FossilSweeperDriver.java    
    //post: constructor for the JPanel
   public GamePanel(int w, int h)
   {
      optionsChanged = false;
      int [] lastOptionsUsed = Utilities.readFile("data/options.txt");
      highScores = Score.readHighScores("data/scores.txt");          //read in last options used and high-scores from files
      if(highScores == null)
      {  //no scores file or corrupted scores, so make a new blank high-score file
         highScores = Score.generateBlankScores();
         Score.writeToFile("data/scores.txt", highScores);
      }
      highScoreAchieved = false;
      screenWidth = w;
      screenHeight = h;
      frame = 0;
      gameMode = STARTSCREEN;
      difficulty = lastOptionsUsed[Utilities.DIFFICULTY_OPTION];     //(BEGINNER = 3, ADVANCED = 2, EXPERT = 1, CLASSIC = 0)
      numStrikes = Math.max(1, difficulty);
      size = lastOptionsUsed[Utilities.SIZE_OPTION];                 //(SMALL = 0, MEDIUM = 1, LARGE = 2) 
      soundIndex = lastOptionsUsed[Utilities.SOUND_OPTION];          //(ALL_ON = 0, NO_MUSIC = 1, ALL_OFF = 2)
      styleIndex = lastOptionsUsed[Utilities.STYLE_OPTION];          //(ANT_STYLE = 0, NUMBER_STYLE = 1)
       
      addMouseListener( this );
      addMouseMotionListener( this );
      mouseX = screenWidth/2;
      mouseY = screenHeight/2; 
       
      setupBoard();                                                  //create a new game board
             
      playerR = buttons[0].length/2;                                 //start the player cursor in the center of the screen
      playerC = buttons.length/2;
      optionIndex = -1;
             
      Sound.initialize();                                            //prepare for sound
      time = -1;
      startTime = -1;
      t = new Timer(DELAY, new Listener());	                        //the higher the value of DELAY, the slower the refresh rate is
      t.start();
   }
   
   public static int getStyle()                                     //info to be passed off to the button object so we know what kind of graphics to show
   {
      return styleIndex;
   }
   
   public static int getFrameNum()                                   //info to be passed off to the Sound object for the timing of sound events
   {
      return frame;
   }
   
   public static int getGameMode()                                   //info to be passed off to the button object so we know what kind of graphics to show
   {
      return gameMode;
   }
   
   public static int getCellSize()                                   //info to be passed off to the button object so we know what kind of graphics to show
   {
      return cellSize;
   }
   
   public static int getScreenWidth()                                //info to be passed off to the button object so we know what kind of graphics to show
   {
      return screenWidth;
   }
   
   public static int getScreenHeight()                               //info to be passed off to the button object so we know what kind of graphics to show
   {
      return screenHeight;
   }
   
   public static int getSoundOption()                                //info to be passed off to the Sound object so we know when to play sounds
   {
      return soundIndex;
   }
   
   //post: creates the option buttons on the right side of the screen
   public void createOptionButtons()
   {
      options = new Button[NUM_BUTTONS];
      int width = textSize * 4;
      int height = textSize;
      int x = screenWidth - width - textSize;
      int y = textSize;
      Shape [] shapes = new Shape[options.length];
      String [] names = {"Reset", "Size:"+sizes[size], "Difficulty:"+difficulties[difficulty], "Instructions", "Sound:"+soundOptions[soundIndex], "Style:"+styleOptions[styleIndex],"Quit"};
      for(int i = 0; i < shapes.length; i++)
      {
         shapes[i] = new Rectangle(x, y, width, height);
         y += textSize*2;
         options[i] = new Button(shapes[i], names[i], new Color(100,255,255), Color.YELLOW, Color.BLACK);
         if(i == SIZE_BTN)    //add extra info for number of ant hills and flags placed
         {
            options[i].setExtraInfo("Hills:"+numMines+" Flags:"+flagsPlaced);  
         }
         else if(i == DIFFICULTY_BTN)
         {
            options[i].setExtraInfo("Strikes left:"+numStrikes);
         }
      }  
   }
         
   //post: sets up the game board
   public void setupBoard()
   {
      cellSize = screenHeight / 16;
      textSize = screenHeight / 16;
      int numRows = 8, numColumns = 8;
      if(size == SMALL)
      {
         numMines = 10;
         cellSize = screenHeight / 16;
      }
      else if(size == MEDIUM)
      {
         numMines = 40;
         cellSize = screenHeight / 20;
         numRows = 16;
         numColumns = 16;
      }
      else if(size == LARGE)
      {
         numMines = 99;
         cellSize = screenHeight / 25;
         numRows = 16;
         numColumns = 30;
      }  
      
      //find the width and height of the space where we want the game board to be drawn:
      //from the left bound of the screen size to the space where we want to start drawing the option buttons        
      int boardWidth = (numColumns+2)*cellSize;
      int boardHeight = (numRows+2)*cellSize; 
      int widthSpace = (screenWidth - (textSize * 5)); //where the option buttons start      
      int heightSpace = screenHeight;      
      startX = (widthSpace/2) - (boardWidth/2) + cellSize;
      startY = (heightSpace/2) - (boardHeight/2);
   
      flashTime = 0;
      flagsPlaced = 0;
      fossilIndex = (int)(Math.random()*fossils.length);
      circles = new LinkedList<Circle>();
      winState = false;
      loseState = false;
      textAlpha = 0;
      int x = startX, y = startY;
      numStrikes = Math.max(1, difficulty);
      buttons = new Button[numRows][numColumns];
      for(int r=0; r<buttons.length; r++)          
      {  //initialize buttons to have the correct plaement in x,y pixel space
         x = startX;
         for(int c=0; c<buttons[r].length; c++)
         {
            buttons[r][c] = new Button(x, y, cellSize);
            x += cellSize;
         }
         y += cellSize;
      }
      for(int i = 0; i < numMines; i++)
      {  //place the anthills (mines) on the board in random locations that have not already been chosen
         int row = (int)(Math.random()*buttons.length);
         int col = (int)(Math.random()*buttons[0].length);
         while(buttons[row][col].getNumAnts() == ANTHILL)
         {
            row = (int)(Math.random()*buttons.length);
            col = (int)(Math.random()*buttons[0].length);
         }
         buttons[row][col].setNumAnts(ANTHILL);
      }
      //populate the board cells with numbers that represent the number of adjacent spaces that have an anthill
      Utilities.populateBoardValues(buttons);
      if(difficulty == CLASSIC)                          
      {  //pick a random zero tile to reveal
         List <Point> zeroes = new LinkedList<Point>();
         for(int r=0; r<buttons.length; r++)
         {
            for(int c=0; c<buttons[r].length; c++)
            {
               if(buttons[r][c].getNumAnts() == 0)
               {
                  zeroes.add(new Point(r, c));
               }
            }
         }
         if(zeroes.size() > 0)
         {
            int randIndex = (int)(Math.random() * zeroes.size());
            Point randPoint = zeroes.get(randIndex);
            int row = (int)(randPoint.getX());
            int col = (int)(randPoint.getY());
            Utilities.revealEmpties(buttons, row, col);
         }
      }   
      createOptionButtons();
      time = -1;
      startTime = -1;
      currentHighScore = Score.findHighScore(difficulty, size, highScores);
      long hsTime = currentHighScore.getTime();
      if(hsTime == 0)
      {
         options[RESET_BTN].setExtraInfo("Best time: NONE");
      }
      else
      {
         options[RESET_BTN].setExtraInfo("Best time:"+hsTime);
      }
      highScoreAchieved = false;
      repaint();
   }

	//post:  shows graphics for each game state
   public void showGraphics(Graphics g)	
   {   
      Font font = new Font(fontName, Font.BOLD, textSize/3);         
      g.setFont(font);
      if(gameMode == STARTSCREEN)
      {   //show graphics for intro screen while the game loads
         showStart(g);
      }
      else if(gameMode == AFTERMATH)
      {  //show graphics for game aftermath here using winState and loseState
         showAftermath(g);
      }
      else if(gameMode == INSTRUCTIONS)
      {   //show graphics for game instructions
         showInstructions(g);
      }
      else //if(gameMode == GAME)     
      {     
         showGame(g);
      }
   }
   
   //post: graphics for the intro screen
   public void showStart(Graphics g)
   {  //draw the background
      g.setColor(Color.blue.darker());            
      g.fillRect(0, 0, screenWidth, screenHeight);
      Font font = new Font(fontName, Font.BOLD, screenHeight/32);         
      g.setFont(font);
      Color textColor = Color.orange;
      String message = "Fossil Sweeper";
      int mWidth = g.getFontMetrics().stringWidth(message);
      int mHeight = cellSize/2;
      int mXmid = screenWidth/2;
      g.setColor(new Color(textColor.getRed(), textColor.getGreen(), textColor.getBlue(), textAlpha));
      g.drawString(message, (mXmid) - (mWidth/2), mHeight);
      int imageSize = screenHeight - (cellSize*2);
      int imageX = (screenWidth/2) - (imageSize/2);
      int imageY = (screenHeight/2) - (imageSize/2);
      g.drawImage(introPicture.getImage(), imageX, imageY, imageSize, imageSize, null);
   }
   
   //post: graphics to show when the game is over (winning/losing)
   public void showAftermath(Graphics g)
   {  //draw the background
      Color textColor = Color.white;                              
      Color backColor = Color.green.darker().darker().darker();   //background color
      if(winState == false)
      {
         textColor = Color.yellow;
         backColor = Color.red.darker().darker();            
      }
      g.setColor(backColor);
      g.fillRect(0, 0, screenWidth, screenHeight);
      showGameBoard(g);
      String message = "You win! Fossil revealed in "+time+" seconds.";
      if(loseState == true)
      {
         message = "You distrubed an ant hill!";
      }
      else if(highScoreAchieved)
      {
         message = "Fossil revealed in record time of "+time+" seconds!";
         options[RESET_BTN].setExtraInfo("Best time:"+time);
      }
      centerMessage(g, message);
   }
   
   //post: graphics and text to show the instuctions for the game
   public void showInstructions(Graphics g)
   {  //draw the background
      String [] instrLines = {"Instructions:","Uncover as much of the fossil as possible without disturbing the ant hills.",
         "Left mouse-click or Space to uncover a plot of land.","The number of ants seen represent the number of adjacent plots that contain an ant hill.",
         "Uncovering a plot of land with an ant hill will disturb the ants.", "Right mouse-click or Enter to mark a suspected ant hill with a flag.", 
         "Win the game by successfully marking all ant hills without disturbing them.", "The difficulty option grants you a number of strikes:",
         "Strikes represent the number of ant hills you can disturb before losing the game."};
      Font font = new Font(fontName, Font.BOLD, screenHeight/64);         
      g.setFont(font);
      g.setColor(Color.blue.darker().darker().darker());            
      g.fillRect(0, 0, screenWidth, screenHeight);
      Color textColor = Color.orange;
      Color textColor2 = Color.orange.darker();
      int x = textSize/2;
      int y = textSize/2;
      int maxWidth = 0; //find the max line width to properly place and scale an image to the right of it
      for(int i=0; i<instrLines.length; i++)
      {
         String message = instrLines[i];
         int mWidth = g.getFontMetrics().stringWidth(message);
         if(mWidth > maxWidth)
         {  
            maxWidth = mWidth;
         }
         if(i % 2 == 0)
         {
            g.setColor(new Color(textColor.getRed(), textColor.getGreen(), textColor.getBlue(), textAlpha));
         }
         else
         {
            g.setColor(new Color(textColor2.getRed(), textColor2.getGreen(), textColor2.getBlue(), textAlpha));
         }
         g.drawString(message, x, y+=textSize);
      }
      x = textSize + maxWidth;
      int imageSize = screenWidth - x - textSize;
      y = textSize/2;
      g.drawImage(introPicture.getImage(), x, y, imageSize, imageSize, null);
   }
   
   //post: graphics for the actual gameplay
   public void showGame(Graphics g)
   {  //draw the background
      g.setColor(new Color(15, 170, 45));              //background
      g.fillRect(0, 0, screenWidth, screenHeight);
      showGameBoard(g);
   }
      
   //post: displays the game board   
   public void showGameBoard(Graphics g)
   {
      int x = startX, y = startY;       
      g.drawImage(fossils[fossilIndex].getImage(), x, y, cellSize*buttons[0].length, cellSize*buttons.length, null);
      if(winState == true && loseState == false)
      {}
      else
      {  //***BUTTON CODE***draw buttons
         for(int r=0; r<buttons.length; r++)
         {
            x = startX;
            for(int c=0; c<buttons[r].length; c++)
            {
               Button b = buttons[r][c];
               if(gameMode==GAME && r > 0 && b.hasBeenClicked() && !buttons[r-1][c].hasBeenClicked())
               {  //draw a depth layer
                  g.drawImage(depthLayer.getImage(), x, y, cellSize, cellSize/7, null);
               }
               b.drawButton(g);
               if(r==playerR && c==playerC)
               {
                  g.setColor(new Color(0, 0, 255, 50));
                  g.fillRect(x, y, cellSize, cellSize);
                  g.drawImage(crosshair.getImage(), x, y, cellSize, cellSize, null);
               }
               x += cellSize;
            }
            y += cellSize;
         }
         for(Circle curr: circles)                 
         {  //draw smoke puffs
            g.setColor(curr.getColor());		            
            g.fillOval(curr.getX()-(curr.getRadius()/2), curr.getY()-(curr.getRadius()/2), curr.getRadius(), curr.getRadius());
         }
      }
      
      for(int i = 0; i < buttons[0].length+2; i++)
      {  //draw a grass border
         x = (startX - cellSize) + (cellSize * i);
         y = (startY - cellSize) + (buttons.length*cellSize) + cellSize;
         int overlap = (int)(cellSize / 8);
         g.drawImage(grassImage.getImage(), x, (startY - cellSize), cellSize, cellSize, null);
         g.drawImage(grassFront.getImage(), x, y - overlap, cellSize, cellSize + (overlap * 2), null);
      }
      for(int i = 1; i < buttons.length+1; i++)
      {  //draw a grass border
         x = (startX - cellSize) + (buttons[0].length * cellSize + cellSize);
         y = (startY - cellSize) + (cellSize * i);
         int overlap = (int)(cellSize * 1.064);
         g.drawImage(grassImage.getImage(), (startX - cellSize), y, cellSize, cellSize, null);
         g.drawImage(grassImage.getImage(), x, y, cellSize, cellSize, null);
      }
      for(Button b: options)
      {  //draw option buttons
         b.drawButton(g);
      }
      if(startTime != -1 && gameMode == GAME)
      {
         String message = "Time: "+((System.currentTimeMillis() - startTime)/1000);
         centerMessage(g, message);
      }
   }
   
   //shows a message centered relative to the size of the game board at the top of the screen in a box
   public void centerMessage(Graphics g, String message)
   {
      Color textColor = Color.orange;
      int mWidth = g.getFontMetrics().stringWidth(message);
      int mHeight = g.getFontMetrics().getHeight();
      int mXmid = startX + ((buttons[0].length/2)*cellSize);
      int textX = (mXmid) - (mWidth/2);
      int textY = mHeight;
      g.setColor(Color.green.darker().darker().darker());
      g.fillRect(textX, textY-(2*mHeight/3), mWidth, mHeight);
      g.setColor(new Color(textColor.getRed(), textColor.getGreen(), textColor.getBlue(), textAlpha));
      g.drawString(message, textX, textY);
      g.drawRect(textX, textY-(2*mHeight/3), mWidth, mHeight);
   }
   
   //post: called every time we call repaint() - once a frame
   public void paintComponent(Graphics g)
   {
      super.paintComponent(g); 
      showGraphics(g);					      
   }
   
   //post: handles all code that we want to trigger every frame - play sound, fade smoke puffs, dissolve in text
   private class Listener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)	
      {  //this is called for each timer iteration 
         frame++;
         if(frame == Integer.MAX_VALUE)
            frame = 0;   
         Sound.playMusic();
         if(frame%5 == 0)
         {  //fade out smoke puffs
            for(int i=circles.size()-1; i>=0; i--)    
            {
               Circle curr = circles.get(i);
               curr.age();
               if(curr.isDead())
                  circles.remove(i);
            }
         }
         if(textAlpha < 255)                       
         {  //dissolve in any text on the screen
            textAlpha++;
         }
         repaint();
      }
   }

   //post: checks to see if the game has been won
   public void checkForWin()
   {
      for(int r=0; r<buttons.length; r++)
      {
         for(int c=0; c<buttons[r].length; c++)
         {
            if(buttons[r][c].getNumAnts() == ANTHILL && !buttons[r][c].isFlagged() && !buttons[r][c].hasBeenClicked())
            {  //we have not won if there is an anthill that has not been clicked on and not been flagged
               winState = false;
               return;
            } 
            if(buttons[r][c].getNumAnts() != ANTHILL && buttons[r][c].isFlagged() && !buttons[r][c].hasBeenClicked())
            {  //we have not won if there is a non-anthill that has not been clicked on and has been flagged
               winState = false;
               return;
            }
         }
      } 
      if(!loseState)
      {
         Sound.win();
         winState = true;
         gameMode = AFTERMATH;
         textAlpha = 0;
         time = (System.currentTimeMillis() - startTime)/1000;
         if(currentHighScore.getTime() == 0 || (currentHighScore.getTime() != 0 && time < currentHighScore.getTime()))
         {
            boolean success = Score.updateHighScore(time, difficulty, size, highScores);
            if(success)
            {
               highScoreAchieved = true;
               Score.writeToFile("data/scores.txt", highScores);
            }
         }
      }
   }

          
   //pre:  b != null and is one of the options buttons
   //post: handles code when an open button has been pressed
   public void selectOption(Button b)
   {
      b.highlight();
      if(b.getTitle().contains("Quit"))
      {
         System.exit(0);
      }
      else if(b.getTitle().contains("Reset"))
      {
         setupBoard();
         Sound.click();
      }   
      else if(b.getTitle().contains("Size"))
      {
         size = (size + 1) % NUMSIZES;
         options[SIZE_BTN].setTitle("Size:"+sizes[size]);
         if(size == SMALL)
         {
            cellSize = screenHeight / 16;
         }
         else if(size == MEDIUM)
         {
            cellSize = screenHeight / 20;
         }
         else if(size == LARGE)
         {
            cellSize = screenHeight / 25;
         }
         setupBoard();
         Sound.click();
         optionsChanged = true;
      }
      else if(b.getTitle().contains("Difficulty"))
      {
         difficulty--;
         if(difficulty < 0)
         {
            difficulty = NUMDIFFICULTIES - 1;
         }
         numStrikes = Math.max(1, difficulty);
         options[DIFFICULTY_BTN].setTitle("Difficulty:"+difficulties[difficulty]);
         options[DIFFICULTY_BTN].setExtraInfo("Strikes left:"+numStrikes);
         setupBoard();
         Sound.click();
         optionsChanged = true;
      }
      else if(b.getTitle().contains("Instructions"))
      {
         gameMode = INSTRUCTIONS;
         textAlpha = 0;
         Sound.click();
      }
      else if(b.getTitle().contains("Sound"))
      {
         soundIndex = (soundIndex+1) % soundOptions.length;
         options[SOUND_BTN].setTitle("Sound:"+soundOptions[soundIndex]);
         if(soundIndex == ALL_OFF)
         {
            Sound.silence();
         }
         else if(soundIndex == NO_MUSIC)
         {
            Sound.silence(1);
         }
         Sound.click();
         optionsChanged = true;
      }
      else if(b.getTitle().contains("Style"))
      {
         styleIndex = (styleIndex+1) % styleOptions.length;
         options[STYLE_BTN].setTitle("Style:"+styleOptions[styleIndex]);
         Sound.click();
         optionsChanged = true;
      }
      currentHighScore = Score.findHighScore(difficulty, size, highScores);
      repaint();
   }
   
   //pre: b!=null, row>=0, col>=0, row<buttons.length, col<buttons[0].length
   //post:the client has clicked on a board cell, so we want to reveal what is there and possibly open up an empty area
   public void revealSpace(Button b, int row, int col)
   {
      if(startTime == -1)    //this is the first space we are clicking on, so record the time
      {
         startTime = System.currentTimeMillis();
      }
      if(optionsChanged)
      {
         int [] optionValues = new int[4];
         optionValues[Utilities.SIZE_OPTION] = size;
         optionValues[Utilities.DIFFICULTY_OPTION] = difficulty;
         optionValues[Utilities.SOUND_OPTION] = soundIndex;
         optionValues[Utilities.STYLE_OPTION] = styleIndex;
         Utilities.writeToFile(optionValues, "data/options.txt");
         optionsChanged = false;
      }
      if(gameMode == AFTERMATH)
      {
         Sound.click();
         setupBoard();
         gameMode = GAME;
         textAlpha = 0;
         return;
      }
      if(!b.hasBeenClicked())
      { 
         if(b.getNumAnts() != 0)    //we will click the empty space in revealEmpties
         {
            b.setClicked(true);
            if(b.getNumAnts() == ANTHILL)
            {
               numStrikes--;
               options[DIFFICULTY_BTN].setExtraInfo("Strikes left:"+numStrikes);
               if(numStrikes <= 0)
               {
                  gameMode = AFTERMATH;
                  textAlpha = 0;
                  Sound.randomNote();
                  winState = false;
                  loseState = true;
               }
            }
         }
         else //if(b.getNumAnts() == 0)
         {
            Utilities.revealEmpties(buttons, row, col);
            repaint();
         }
      }
      else                      //already clicked on
      {
         Sound.randomNote();      
      }
      checkForWin();
   }
   
   //pre: b!=null
   //post:the client has clicked on a board cell to toggle a flag on that spot
   public void flagSpace(Button b)
   {
      if(startTime == -1)    //this is the first space we are clicking on, so record the time
      {
         startTime = System.currentTimeMillis();
      }
      if(gameMode == AFTERMATH)              
      {  //if the game is over and we are seeing the aftermath, reset to a new game
         Sound.click();
         setupBoard();
         gameMode = GAME;
         textAlpha = 0;
         return;
      }
      if(!b.hasBeenClicked() && !b.isFlagged())
      {
         b.setFlagged(true);
         flagsPlaced++;
         options[SIZE_BTN].setExtraInfo("Hills:"+numMines+" Flags:"+flagsPlaced);  
         Sound.click();
      }
      else                                   //already clicked on
      {
         if(b.isFlagged())
         {
            b.setFlagged(false);
            b.setClicked(false);
            flagsPlaced--;
            options[SIZE_BTN].setExtraInfo("Hills:"+numMines+" Flags:"+flagsPlaced);
            Sound.click();
         }
         else
         {
            Sound.randomNote();      
         }
      }
      checkForWin();
   }
   
   //post: highlight the button that the mouse is on, unlighlight buttons that the mouse is not on   
   public void highlightButtons()   
   {
      int mouseR = ((mouseY-startY)/cellSize);
      int mouseC = ((mouseX-startX)/cellSize);
      
      if(mouseR >=0 && mouseC >= 0 && mouseR < buttons.length && mouseC < buttons[0].length)
      {  //if the mouse is in bounds of the board
         playerR = mouseR;
         playerC = mouseC;
         for(int r=0; r<buttons.length; r++)
         {  //highlight game-board button if mouse is on it
            for(int c=0; c<buttons[r].length; c++)
            {
               Button b = buttons[r][c];
               if(b.getShape().contains(mouseX, mouseY))
                  b.highlight();
               else
                  b.unHighlight();
            }
         }
      }  
      else
      {  //mouse is not on the game board, so check option buttons
         for(Button b: options)
         {  //highlight option button if mouse is on it
            if(b.getShape().contains(mouseX, mouseY))
               b.highlight();
            else
               b.unHighlight();
         }
      } 
   }

   //methods we need for implementing mouseListener and mouseMotionListener
   public void mousePressed( MouseEvent e )
   {}//Invoked when a mouse button has been pressed on a component.

   public void mouseReleased( MouseEvent e )
   {//Invoked when a mouse button has been released on a component.
      int button = e.getButton();
      mouseX = e.getX();
      mouseY = e.getY(); 
      int mouseR = ((mouseY-startY)/cellSize);
      int mouseC = ((mouseX-startX)/cellSize);
      if(gameMode == INSTRUCTIONS)
      {
         Sound.click();
         gameMode = GAME;
         textAlpha = 0;
      }
      else if((gameMode == AFTERMATH || gameMode == STARTSCREEN) && !options[QUIT_BTN].getShape().contains(mouseX, mouseY))
      {
         Sound.click();
         gameMode = GAME;
         textAlpha = 0;
         setupBoard();
      }
      else
      {
         if(mouseR >= 0 && mouseC >= 0 && mouseR < buttons.length && mouseC < buttons[0].length)
         {  //is our mouse somewhere on the game board?
            Button b = buttons[mouseR][mouseC];
            if(button == MouseEvent.BUTTON1)          //reveal
            {
               revealSpace(b, mouseR, mouseC);
            }
            else if(button == MouseEvent.BUTTON3)     //plant a flag
            {
               flagSpace(b);
            }
         }
         else
         {  //our mouse is not on the game board, so check the option buttons
            for(Button b:options)
            {
               if(b.getShape().contains(mouseX, mouseY))
               {
                  selectOption(b);
               }
            }
         } 
      }  
      highlightButtons();
      repaint();
   }

   public void mouseEntered( MouseEvent e )
   {}//Invoked when the mouse enters a component.
   
   public void mouseExited( MouseEvent e )
   {}//Invoked when the mouse exits a component.
   
   public void mouseDragged( MouseEvent e)
   {}//invoked when the mouse is moved while holding down a mouse button

   public void mouseClicked( MouseEvent e )
   {}//Invoked when the mouse button has been clicked (pressed and released) on a component.

   //post: invoked when the mouse movement has been detected.  Update mouse position and button highlight/unhighlighting
   public void mouseMoved( MouseEvent e)
   {
      mouseX = e.getX();
      mouseY = e.getY(); 
      highlightButtons();
      repaint();			//refresh the screen
   }   
       
   //methods we need to define from implementing the KeyListener interface
   public void keyTyped(KeyEvent e)         //don't need this one
   {}
   
   public void keyReleased(KeyEvent e)
   {}
      
   //post: invoked when a keyboard key has been pressed   
   public void keyPressed(KeyEvent e)
   {
      boolean moved = false, clicked = false, flagged = false, optionMove = false;
      int k = e.getKeyCode();
      if(k==KeyEvent.VK_Q || k==KeyEvent.VK_ESCAPE)					
      {  //End the program	
         System.exit(1);
      }
      if(gameMode == INSTRUCTIONS)
      {
         Sound.click();
         gameMode = GAME;
         textAlpha = 0;
      }
      else if(gameMode == AFTERMATH || gameMode == STARTSCREEN)
      {
         Sound.click();
         setupBoard();
         gameMode = GAME;
         textAlpha = 0;
      }
      else
      {
         if(k==KeyEvent.VK_UP)
         {
            if(optionIndex == -1 && playerR>0)
            {  //we are hitting the up-arrow on the game board
               playerR--;
               moved = true;
            }
            else if(optionIndex != -1 && optionIndex >= 1)
            {  //we are hitting the up-arrow on the option buttons
               optionIndex--;
               optionMove = true;   //go highlight the correct option button
            }
         }
         else 
            if(k==KeyEvent.VK_DOWN)
            {
               if(optionIndex == -1 && playerR<buttons.length-1)
               {  //we are hitting the down-arrow on the game board
                  playerR++;
                  moved = true;
               }
               else if(optionIndex != -1 && optionIndex < options.length-1)
               {  //we are hitting the down-arrow on the option buttons
                  optionIndex++;
                  optionMove = true;   //go highlight the correct option button
               }
            }
            else
               if(k==KeyEvent.VK_LEFT)
               {
                  if(optionIndex == -1 && playerC>0)
                  {  //we are hitting the left-arrow on the game board
                     playerC--;
                     moved = true;
                  }
                  else if(optionIndex != -1)
                  {  //we are hitting the left-arrow on the options buttons - return to the game board
                     playerR = optionIndex;
                     playerC = buttons[0].length - 1;
                     optionIndex = -1;
                     optionMove = true;      //unhighlight all option buttons
                  }
               }
               else 
                  if(k==KeyEvent.VK_RIGHT)
                  {  
                     if(optionIndex == -1)
                     {  //we are hitting the right-arrow on the game board
                        if(playerC<buttons[0].length-1)
                        {  //we are hitting the right-arrow on the game board and staying on the game board
                           playerC++;
                           moved = true;
                        }
                        else  
                        {  //we will move off the game board over to the options buttons
                           optionIndex = Math.min(options.length-1, playerR);
                           optionMove = true;   //go highlight the correct option button
                        }
                     }
                  }
                  else 
                     if(k==KeyEvent.VK_SPACE)	
                     {   
                        if(optionIndex == -1)    
                        {  //on game board, not on options buttons
                           moved = true;
                           clicked = true;
                        }
                        else                    
                        {  //on an option button - select it
                           Button b = options[optionIndex];
                           selectOption(b);
                        }
                     }
                     else if(k==KeyEvent.VK_ENTER) //flag
                     {
                        if(optionIndex == -1)    
                        {  //on game board, not on options buttons
                           moved = true;
                           clicked = false;
                           flagged = true;
                        }
                        else                    
                        {  //on an option button - select it
                           Button b = options[optionIndex];
                           selectOption(b);
                        }
                     }
         if(optionMove)     
         {
            if(optionIndex>=0 && optionIndex<options.length) 
            {  //go highlight the correct option button
               for(int i=0; i<options.length; i++)
               {
                  Button b = options[i];
                  if(i == optionIndex)
                  {
                     b.highlight();
                  }
                  else
                  {
                     b.unHighlight();
                  }
               }
               Button b = options[optionIndex];
               b.highlight();
            }
            else
            {  //unhighlight all option buttons
               for(Button b:options)
               {
                  b.unHighlight();
               }
            }
         }            
         else if(moved)
         {
            if(playerR >=0 && playerC >= 0 && playerR < buttons.length && playerC < buttons[0].length)
            {
               Button b = buttons[playerR][playerC];
               for(int r=0; r<buttons.length; r++)
               {
                  for(int c=0; c<buttons[r].length; c++)
                  {
                     Button b2 = buttons[r][c];
                     if(b2 == b)
                        b2.highlight();
                     else
                        b2.unHighlight();
                  }
               }
               if(clicked)
               {   
                  revealSpace(b, playerR, playerC);
               }
               else if(flagged)
               {
                  flagSpace(b);
               }
            }
         }     
      }       
      repaint();			//refresh the screen
   }
}

