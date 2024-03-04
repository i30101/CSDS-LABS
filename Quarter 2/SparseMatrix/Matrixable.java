/** An interface for a sparse matrix - works like a 2D array but does not use memory for unoccupied cells
 * d oberle 10/2021.
 */

public interface Matrixable<anyType> {
    
    /** returns the element at row r, col c. 
     * @param r the row location; r >=0 and r is less than the number of rows.
     * @param c the column location; c >=0 and c is less than the number of columns.
     * @return the element at row r, column c, return null if r or c are invalid.
     */  
    public anyType get(int r, int c);				


    /** changes the element at row r, col c to a new value, returning the old value that was there. 
     * @param r the row location; r >=0 and r is less than the number of rows.
     * @param c the column location; c >=0 and c is less than the number of columns.
     * @param x a non-null anyType object
     * @return the element that was at row r, column c before it was changed to x, return null if r or c are invalid.
     */  
    public anyType set(int r, int c, anyType x);
    

    /** adds a new element at row r, col c. 
     * @param r the row location; r >=0 and r is less than the number of rows.
     * @param c the column location; c >=0 and c is less than the number of columns.
     * @param x a non-null anyType object
     * @return true if r and c are valid and the item was added, false if r or c are out-of-bounds with no item added
     */  
    public boolean add(int r, int c, anyType x);
    

    /** removes and returns the element at row r, col c. 
     * @param r the row location; r >=0 and r is less than the number of rows.
     * @param c the column location; c >=0 and c is less than the number of columns.
     * @return the element that was at row r, column c before it was removed, return null if r or c are invalid.
     */  
    public anyType remove(int r, int c);


    /** returns the number of logical elements added to the matrix. 
     * @return the number of logical elements added to the matrix.
     */  
    public int size();			//returns # actual elements stored
        
    
    /** returns the number of rows in the matrix. 
     * @return the number of rows in the matrix.
     */  
    public int numRows();		//returns # rows set in constructor
        

    /** returns the number of columns in the matrix. 
     * @return the number of columns in the matrix.
     */  
    public int numColumns();	//returns # cols set in constructor

    
    /*   Extension methods:
        public boolean contains(anyType x);	//true if x exists in list
        public int[] getLocation(anyType x);	//returns location [r,c] of where x exists in list, null otherwise
        public Object[][] toArray();			//returns equivalent structure in 2-D array form
        public boolean isEmpty();				//returns true if there are no actual elements stored
        public void clear();						//clears all elements out of the list
        
        public void setBlank(char blank);	   //allows the client to set the character that a blank spot in the array is
                                                            //represented by in String toString()
    */
    }
