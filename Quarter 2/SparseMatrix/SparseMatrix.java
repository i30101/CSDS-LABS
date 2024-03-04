/**
 * @author Andrew Kim
 * @version 1.0.0
 * @since 4 November 2023
 */

import java.util.*;

public class SparseMatrix<anyType> implements Matrixable<anyType> {
    
    // private variables
    private LinkedList<Cell<anyType>> list;
    private int numRow, numCol;


    /**
     * SparseMatrix constructor
     * @param r the number of rows
     * @param c the number of columns
     */
    public SparseMatrix(int r, int c) {
        list = new LinkedList();
        numRow = r;
        numCol = c;
    }

    /**
     * Returns the key for a Cell object
     * @param cell
     * @return the key
     */
    private int getKey(Cell<anyType> cell) {
        return (cell.getRow() * numCol) + cell.getCol();
    }


    /**
     * Retuns the key for a set of coordinates
     * @param r the row
     * @param c the column
     * @return the key
     */
    private int getKey(int r, int c) {
        return (r * numCol) + c;
    }



    public anyType get(int r, int c) {
        // traverse through list until desired Cell is found
        int i = 0;
        while(getKey(list.get(i)) != getKey(r, c)) {
            i++;
        }

        // return cell
        return list.get(i).getValue();
    }


    public anyType set(int r, int c, anyType x) {
        // traverse through list until desired Cell is found
        int i = 0;
        while(getKey(list.get(i)) != getKey(r, c)) {
            i++;
        }

        // save previous value, set new value, return previous value
        anyType previous = list.get(i).getValue();
        list.get(i).setValue(x);
        return previous;
    }

    public boolean add(int r, int c, anyType x) {
        // list has more than 1 elements
        if(list.size() > 0) {
            // traverse through list until desired index reached
            for(int i = 0; i < list.size(); i++) {
                if(getKey(r, c) < getKey(list.get(i))) {
                    // add new value
                    list.add(i, new Cell(x, r, c));
                    return true;
                }
            }
        }

        // list has no elements or element should be added at end
        list.add(new Cell(x, r, c));
        return true;
    }


    public anyType remove(int r, int c) {
        // traverse through array until desired index reached
        for(int i = 0; i < list.size(); i++) {
            if(getKey(r, c) == getKey(list.get(i))) {
                // save previous value, remove value, return previous value
                anyType previous = list.get(i).getValue();
                list.remove(i);
                return previous;
            }
        }
        return null;
    }

    public int size() {
        return list.size();
    }

    public int numRows() {
        return numRow;
    }

    public int numColumns() {
        return numCol;
    }


    @Override
    public String toString() {
        String output = "";
        for(int r = 0; r < numRow; r++) {
            for(int c = 0; c < numCol; c++) {
                boolean added = false;
                for(Cell<anyType> cell : list) {
                    if(getKey(cell) == getKey(r, c)) {
                        output += cell.getValue() + " ";
                        added = true;
                        break;
                    }
                }
                if (!added) {
                    output += "- ";
                }
            }
            output += "\n";
        }
        return output;
    }
}