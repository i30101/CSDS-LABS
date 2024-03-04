/**
 * @author Andrew Kim
 * @version 1.0.0
 * @since 4 November 2023
 */

public class Cell<anyType> {
    // value inside cell
    private anyType value;

    // coordinate values of Cell
    private int row;
    private int col;


    /**
     * Cell constructor
     * @param v the value inside the cell
     * @param r the row of the cell
     * @param c the column of the cell
     */
    public Cell(anyType v, int r ,int c) {
        value = v;
        row = r;
        col = c;
    }

    public void setValue(anyType v) {
        value = v;
    }

    public anyType getValue() {
        return value;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }
}
