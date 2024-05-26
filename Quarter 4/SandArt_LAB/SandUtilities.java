//d oberle - doberle@fcps.edu.
import java.awt.Color;

public class SandUtilities {
    //pre : c!= null.
    //post: returns the inverted color from the one sent as c.
    public static Color invert(Color c) {
        return new Color(255 - c.getRed(), 255 - c.getGreen(), 255 - c.getBlue());
        // return c;      //temporary return statement to keep things compiling
    }

    //pre:   m!= null.
    //post:  for each non-null element of m, changes it to its inverted color.
    //       skips any color with the value skip1 and skip2, leaving them unchanged.
    public static void invertColors(Color[][]m, Color skip1, Color skip2) {
        for (int r = 0; r < m.length; r++) {
            for (int c = 0; c < m[0].length; c++) {
                Color tempColor = m[r][c];
                if (tempColor == null || tempColor.equals(skip1) || tempColor.equals(skip2)) {
                    continue;
                }
                m[r][c] = invert(tempColor);
            }
        }
    }


    //pre:   m is a square 2-D array (m.length==m[0].length).
    //post:  flips the array upside down.
    public static void flipUpsideDown(Object[][]m) {
        int numRows = m.length;
        for (int r = 0; r < numRows / 2; r++) {
            Object[] tempRow = m[numRows - 1 - r];
            m[numRows - 1 - r] = m[r];
            m[r] = tempRow;
        }
    }

    //pre:   m is a square 2-D array (m.length==m[0].length)
    //post:  rotates the array 90 degrees to the left
    public static void rotateLeft(Object[][] m) {
        int numRows = m.length;
        int numCols = m[0].length;
        Object[][] newM = new Object[numRows][numCols];
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                newM[c][numRows - 1 - r] = m[r][c];
            }
        }

        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                m[r][c] = newM[r][c];
            }
        }
    }

    //pre:   m is a square 2-D array (m.length==m[0].length)
    //post:  rotates the array 90 degrees to the right
    public static void rotateRight(Object[][] m) {
        int numRows = m.length;
        int numCols = m[0].length;
        Object[][] newM = new Object[numRows][numCols];
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                newM[numCols - 1 - c][r] = m[r][c];
            }
        }

        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                m[r][c] = newM[r][c];
            }
        }
    } 
        
    //main function for testing and debugging
    public static void main (String [] arg) {
        String [][] test = {{"A","B","C"},{"D","E","F"},{"G","H","I"}};
        show(test);
        flipUpsideDown(test);
        show(test);
        rotateLeft(test);
        show(test);
        rotateRight(test);
        show(test);
    }  

    public static void show(String [][] grid) {
        for(int r = 0; r < grid.length; r++) {
            for(int c = 0; c < grid[r].length; c++) {
                System.out.print(grid[r][c]+ " ");          
            }
            System.out.println();
        }
        System.out.println();
    }
}