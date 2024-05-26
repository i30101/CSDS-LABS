//create your own version of the ArrayList by completing this class definition
//look in ListInterface to see what methods you need to create.

/** Recreating an ArrayList */    
public class MyArrayList<anyType> implements ListInterface<anyType> {
    // Data field to stores the elements that the client adds with buffer space
    private Object[] list;

    // Data field to keep track of the number of logical elements added
    private int numElements;
    
    
    // No-arg constructor starts with a buffer size of 10 
    public MyArrayList() {
        list = new Object[10];
        numElements = 0;
    }


    /**
     * add an element to the end of list
     * @param x the element to be added
     * @return whether the element was added correctly or not
     */
    public boolean add(anyType x) {
        // check if list is too small and change size accordingly
        if(list.length < numElements + 1) {
            doubleCapacity();
        }
        list[numElements] = x;
        numElements++;
        return true;
    }


    /**
     * @param index where the element is to be added, max is numElements
     * @param x the element to be added
     * @return whether the element was added correctly or not
     */
    public boolean add(int index, anyType x) {
        // check if index is valid
        if(index > numElements) {
            throw new IndexOutOfBoundsException(index);
        }

        // check if list is too small and change size accordingly
        if(list.length < numElements + 1) {
            doubleCapacity();
        }

        // pushedElements incluedes element originally at index 
        Object[] pushedElements = new Object[numElements - index];

        // index at which to assign elements to pushedElements
        int count = 0;
        for(int i = index; i < numElements; i++) {
            pushedElements[count] = list[i];
            count++;
        }

        // reassign list value at index
        list[index] = x;

        // reassign pushedElements to list
        count = index + 1;
        for(Object pushedElement : pushedElements) {
            list[count] = pushedElement;
            count++;
        }
        numElements++;
        
        return true;
    }


    /**
     * @return the number of elements in the ArrayList
     */
    public int size() {
        return numElements;
    }


    /**
     * sets the spot at the given index to the given value
     * @param index where the element is to be set
     * @param x the value to be set
     * @return the old value that was replaced
     */
    public anyType set(int index, anyType x) {
        // check if index is valid
        if(index >= numElements) {
            throw new IndexOutOfBoundsException(index);
        }

        Object replacedItem = list[index];
        list[index] = x;
        return (anyType) replacedItem;
    }

    /**
     * @param index the desired index
     * @return the value at the given index
     */
    public anyType get(int index) {
        // check if index is valid
        if(index >= numElements) {
            throw new IndexOutOfBoundsException(index);
        }
        return (anyType) list[index];
    }


    /**
     * @param index the index at which to remove an object
     * @return the removed object
     */
    public anyType remove(int index) {
        if(index >= numElements) {
            throw new IndexOutOfBoundsException(index);
        }

        // shift objects
        Object removedObject = list[index];

        for(int i = index; i < numElements - 1; i++) {
            list[i] = list[i + 1];
        }

        list[numElements - 1] = null;

        numElements--;

        cutCapacity();

        return (anyType) removedObject;
    }

    
    // Helper method to double the capacity of list in case we run out of buffer space 
    // private because this is a helper method that need not be used outside of the class
    private void doubleCapacity()	{
        int doubledSize = list.length * 2;
        Object[] doubledList = new Object[doubledSize];
        for(int i = 0; i < list.length; i++) {
            doubledList[i] = list[i];
        }
        list = doubledList;
    }

    // Helper method to cut the list size in half in case we are wasting memory with our buffer space */       
    // private because this is a helper method that need not be used outside of the class
    private void cutCapacity()	{
        int halvedSize = list.length / 2 + 1;
        if(halvedSize >= numElements) {
            Object[] halvedList = new Object[halvedSize];
            for(int i = 0; i < numElements; i++) {
                halvedList[i] = list[i];  
            }
            list = halvedList;
        }
    }


    /** 
    * Return the logical contents of list as a string
    * @return a String showing the contents of the list in the form [a0, a1, a2, ... ,an] where n is (numElements-1)  
    */
    @Override
    public String toString(){
        String ans = "[";
        for(int i = 0; i < numElements; i++) {
            if(list[i] != null) {
                ans += list[i].toString();
                if(i < numElements - 1) {
                ans += ", ";
                }
            }
        }
        //add all array elements with a comma separating each, i.e. [A, B, C] 
        return ans + "]";
    }    
}
