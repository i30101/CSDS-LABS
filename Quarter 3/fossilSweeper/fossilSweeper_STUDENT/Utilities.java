//Oberle, 2023
import java.io.*;
import java.util.*;

//Student assignment: complete the methods populateBoardValues and revealEmpties.  Make sure to read the pre and post conditions.
//This class has a main function to test your methods without having to run the game.
//Check to see if the output is the same as the comments in the main function.

public class Utilities {
    public static final int SIZE_OPTION = 0, DIFFICULTY_OPTION = 1, SOUND_OPTION = 2, STYLE_OPTION = 3;
    
    //pre: gameBoard!=null.  Each button has a data-field called numAnts that is either the value GamePanel.ANTHILL (9) or ZERO (0).
    //     Button objects have a method getNumAnts() that returns an int value, and setNumAnts(int x) that will change its value to a new one.
    //post:For each cell in gameBoard that does not have its numAnts value equal to GamePanel.ANTHILL (9), 
    //     this will set the numAnts data field to an integer that represents the number of adjacent cells for which the numAnts value is GamePanel.ANTHILL (9).
    //     Given a buttons array that is passed in with the following values for numants:  0 0 9 0     will change the array to this:   1 2 9 1
    //                                                                                     0 9 0 0                                      1 9 4 3
    //                                                                                     0 0 9 9                                      1 2 9 9
    //                                                                                     0 0 0 0                                      0 1 2 2                                          
    public static void populateBoardValues(Button [][] gameBoard) {
        //*****COMPLETE THIS METHOD*****/
        // int r = 0, c = 0, count = 0;
        // gameBoard[r][c].setNumAnts(count);
        int rows = gameBoard.length;
        int cols = gameBoard[0].length;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (gameBoard[r][c].getNumAnts() == GamePanel.ANTHILL) { // button is ant hill
                    continue;
                } else { // button is not ant hill
                    // number of adjacent ant hils
                    int count = 0;

                    // set row range
                    int minRow = (r == 0) ? 0 : r - 1;
                    int maxRow = (r == rows - 1) ? rows - 1 : r + 1; 

                    // set column range
                    int minCol = (c == 0) ? 0 : c - 1;
                    int maxCol = (c == cols - 1) ? cols - 1 : c + 1;

                    // traverse through neighbors within ranges above
                    for (int nRow = minRow; nRow <= maxRow; nRow++) {
                        for (int nCol = minCol; nCol <= maxCol; nCol++) {
                            if (gameBoard[nRow][nCol].getNumAnts() == GamePanel.ANTHILL) {
                                count++;
                            }
                        }
                    }
                    gameBoard[r][c].setNumAnts(count);
                }
            }
        }
    }
    
    //pre:  gameBoard!=null, row>=0, col>=0, row<gameBoard.length, col<gameBoard[0].length.
    //post: For an unclicked cell at row/col with ZERO ants (where gameBoard[row][col].getNumAnts()==0), this sets clicked status to true for all adjacent ZERO ant cells as well as any adjacent cell bordering a ZERO ant cell.
    //      This is used to open up an area when the client clicks on a space where there are no close ant hills.
    public static void revealEmpties(Button [][] gameBoard, int row, int col) {
        //*****COMPLETE THIS METHOD*****/
        //terminating cases go here
        if (row < 0 || row >= gameBoard.length) {
            return;
        } else if (col < 0 || col >= gameBoard[0].length) {
            return;
        } else if (gameBoard[row][col].hasBeenClicked()) {
            return;
        }

        gameBoard[row][col].setClicked(true);
        revealAdjacentSpaces(gameBoard, row, col);   //now reveal any adjacent space next to a 0-ant space
        
        // //recursive calls go here

        for (int r = row - 1; r <= row + 1; r++) {
            for (int c = col - 1; c <= col + 1; c++) {
                revealEmpties(gameBoard, r, c);
            }
        }
        
    }

    //pre:  gameBoard!=null, row>=0, col>=0, row<gameBoard.length, col<gameBoard[0].length
    //post: For any unclicked space adjacent to (row, col) that does not contain the value zero, reveal it.
    //      This is a helper method for revealEmpties.
    public static void revealAdjacentSpaces(Button[][]gameBoard, int row, int col) {
        for(int i = row - 1; i <= row + 1; i++)
        {  //traverse through each space adjacent to the cell at (row,col)
            for(int j = col - 1; j <= col + 1; j++)
            {
                if(i < 0 || j < 0 || i >= gameBoard.length || j >= gameBoard[0].length || (row == i && col == j)) {  //if out of bounds or the same space as row,col, skip it
                    continue;
                }
                if(gameBoard[i][j].getNumAnts() != 0 && !gameBoard[i][j].hasBeenClicked()) {
                    gameBoard[i][j].setClicked(true);
                }
            }
        }
    }
    
    //for testing helper methods
    public static void main(String [] arg) {
        Button [][] testBoard = createTestBoard1();
        System.out.println("Initial board:");
        showNumAnts(testBoard);    //0 0 9 0
                                    //0 9 0 0
                                    //0 0 9 9
                                    //0 0 0 0
        populateBoardValues(testBoard);
        System.out.println("\n\nAfter populating:");
        showNumAnts(testBoard);    //1 2 9 1
                                    //1 9 4 3
                                    //1 2 9 9
                                    //0 1 2 2   
        testBoard = createTestBoard2();
        System.out.println("\n\nInitial board:");
        showNumAnts(testBoard);          //0 1 1 1 0 0     shown as:   X X X X X X No cells have been clicked yet
                                        //0 2 9 2 0 0                 X X X X X X
                                        //0 2 9 2 0 0                 X X X X X X
                                        //0 1 1 2 1 1                 X X X X X X
                                        //0 0 0 1 9 1                 X X X X X X
                                        //0 0 0 1 1 1                 X X X X X X
        revealEmpties(testBoard, 4, 0);
        System.out.println("\n\nAfter clicking on (4,0):");
        showNumAnts(testBoard);          //0 1 X X X X     Clicked on 4,0 with empty area and adjacent spaces opened up to see
                                        //0 2 X X X X
                                        //0 2 X X X X
                                        //0 1 1 2 X X
                                        //0 0 0 1 X X
                                        //0 0 0 1 X X
        testBoard = createTestBoard2();                    //reset board back to everything unclicked
        revealEmpties(testBoard, 0, 4);
        System.out.println("\n\nAfter clicking on (0,4):");
        showNumAnts(testBoard);          //X X X 1 0 0     Clicked on 0,4 with empty area and adjacent spaces opened up to see
                                        //X X X 2 0 0
                                        //X X X 2 0 0
                                        //X X X 2 1 1
                                        //X X X X X X
                                        //X X X X X X
                                                                    
    }
    
    //post:create a buttons array for testing that looks like this:  0 0 9 0
    //                                                               0 9 0 0
    //                                                               0 0 9 9
    //                                                               0 0 0 0
    public static Button[][] createTestBoard1() {
        Button [][] board = new Button[4][4];
        for(int r=0; r<board.length; r++) {  //populate the board cells with buttons
            for(int c=0; c<board[r].length; c++) {
                board[r][c] = new Button(0,0,0);  //dummy values just for testing
                board[r][c].setClicked(true);     //so we see the numAnts value when we go to display the board
            }
        }
        board[0][2].setNumAnts(GamePanel.ANTHILL);
        board[1][1].setNumAnts(GamePanel.ANTHILL);
        board[2][2].setNumAnts(GamePanel.ANTHILL);
        board[2][3].setNumAnts(GamePanel.ANTHILL);
        return board;
    }
    
    //post:create a buttons array for testing that looks like this:   0 1 1 1 0 0
    //                                                                0 2 9 2 0 0
    //                                                                0 2 9 2 0 0
    //                                                                0 1 1 2 1 1
    //                                                                0 0 0 1 9 1
    //                                                                0 0 0 1 1 1
    //     No buttons will be clicked yet, so it will be displayed as all X's if we call showNumAnts on the returned board
    public static Button[][] createTestBoard2() {
        Button [][] board = new Button[6][6];
        for(int r=0; r<board.length; r++) {  //populate the board cells with buttons
            for(int c=0; c<board[r].length; c++)
            {
                board[r][c] = new Button(0,0,0);  //dummy values just for testing
            }
        }
        board[1][2].setNumAnts(GamePanel.ANTHILL);
        board[2][2].setNumAnts(GamePanel.ANTHILL);
        board[4][4].setNumAnts(GamePanel.ANTHILL);
        populateBoardValues(board);
        return board;
    }
        
        //post: displays elements of the board by showing which cells have been clicked (numAnts value) or not ("X") in row/column format
    public static void showNumAnts(Button[][] board)
    {
        for(int r=0; r<board.length; r++) {  
            for(int c=0; c<board[r].length; c++) {
                if(!board[r][c].hasBeenClicked()) {
                System.out.print("X ");
                }
                else {
                System.out.print(board[r][c].getNumAnts()+ " ");
                }
            }
            System.out.println();
        }
    }
    
    //pre:  fileName exists as a text file and contains 4 integer values separated by a space (representing options information to remember)
    //      options are:    SIZE (SMALL = 0, MEDIUM = 1, LARGE = 2), 
    //                      DIFFICULTY (BEGINNER = 3, ADVANCED = 2, EXPERT = 1, CLASSIC = 0), 
    //                      SOUND(ALL_ON = 0, NO_MUSIC = 1, ALL_OFF = 2), 
    //                      STYLE(ANT_STYLE = 0, NUMBER_STYLE = 1)
    //post: reads in options information for the GamePanel to use when the game starts - returns an int [] of the 4 values
    //      if the file is not found or there is an error when reading it in, it will create a new file with standard options
    public static int[] readFile(String fileName) {
        int [] standardOptions = {0,3,0,0};    //default options to use if something goes wrong: small size, beginner difficulty, all sound on, ant style
        try {
            Scanner input = new Scanner(new FileReader(fileName));
            int [] optionValues = new int[4];
            int index = 0;
            while (input.hasNextInt())	{	//while there is another int in the file to read in
                try {
                optionValues[index++] = input.nextInt();
                }
                catch (java.util.InputMismatchException ex1)	{		//file is corrupted or doesn't exist - return standard values
                writeToFile(standardOptions, fileName);
                return standardOptions;
                }			
                catch (java.util.NoSuchElementException ex2)	{		//file is corrupted or doesn't exist - return standard values and remake the file
                writeToFile(standardOptions, fileName);
                return standardOptions;
                }			
            }
            input.close();	
            return optionValues;
        }
        catch (IOException ex3)	{		//file is corrupted or doesn't exist - return standard values and remake the file
            writeToFile(standardOptions, fileName);
            return standardOptions;
        }				
    }

    //pre:  array is not null and contains 4 integers representing option values
    //      options are:    SIZE (SMALL = 0, MEDIUM = 1, LARGE = 2), 
    //                      DIFFICULTY (BEGINNER = 3, ADVANCED = 2, EXPERT = 1, CLASSIC = 0), 
    //                      SOUND(ALL_ON = 0, NO_MUSIC = 1, ALL_OFF = 2), 
    //                      STYLE(ANT_STYLE = 0, NUMBER_STYLE = 1)
    //post: writes 4 integers to the file separated by a space
    //      if a corrupt array is sent, writes out standard default options
    public static void writeToFile(int[] array, String filename) {
        int [] standardOptions = {0,3,0,0};    //default options to use if something goes wrong: small size, beginner difficulty, all sound on, ant style
        boolean valueProblem = false;          //will be true if the array or the values within it are corrputed
        if(array == null || array.length != 4) {
            valueProblem = true;
        }
        else {
            if(array[SIZE_OPTION] < 0 || array[SIZE_OPTION] >= GamePanel.NUMSIZES || 
                    array[DIFFICULTY_OPTION] < 0 || array[DIFFICULTY_OPTION] >= GamePanel.NUMDIFFICULTIES ||
                    array[SOUND_OPTION] < 0 || array[SOUND_OPTION] >= GamePanel.NUM_SOUND_OPTIONS ||
                    array[STYLE_OPTION] < 0 || array[STYLE_OPTION] >= GamePanel.NUM_STYLE_OPTIONS) {
                valueProblem = true;
            }
                
        }
        if(valueProblem) {
            array = standardOptions;
        } 
        try {
            System.setOut(new PrintStream(new FileOutputStream(filename)));
            for(int i = 0; i < array.length; i++) {
                System.out.println(array[i] + " ");
            }
            System.out.flush();    
            System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
        }
        catch (IOException e) {
        } 
    }
}