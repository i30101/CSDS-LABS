//complete the methods reheapUp and reheapDown.
import java.util.*;

/** a heap priority queue where low-value is high-priority.  d oberle 10/2021 */
public class HeapPriorityQueue implements PriorityQueue {

    /** Data field for default buffer-size */
    private static final int DFLT_CAPACITY = 1024;

    /** Data field to store the logical elements added with buffer-space; index 0 will go unused */
    private Comparable[] items;

    /** Data field for the logical number of elements added */
    private int numItems;

    
    /**
     * Constructor if the client wants to set their own buffer-size 
     * Precondition: initialCapcity >= 0
     * @param initialCapacity the size the client wants the buffer-space to be initialized to 
     */
    public HeapPriorityQueue(int initialCapacity) {
        items = new Comparable[initialCapacity + 1];
        numItems = 0;
    }


    /**  No-arg constructor that uses the default buffer-size */
    public HeapPriorityQueue() {
        this(DFLT_CAPACITY);
    }


    /** Returns whether or not the heap is empty 
     *  @return  true if the heap is empty; false if the heap contains elements
     */
    public boolean isEmpty() {
        return (numItems == 0);
    }


    /** Returns the highest priority element
     *  @return  the highest priority element in the priority-queue
     */
    public Comparable peek() {
        if (numItems == 0) {
            throw new NoSuchElementException();
        }
        return items[1];
    }


    /**  Helper method to swap two elements in a list 
     * @param list  a container of non-null Comparable objects
     * @param a  valid index of list pre: a>=0 and a is less than list.length
     * @param b  valid index of list pre: b>=0 and b is less than list.length 
     */
    private static void swap(Comparable [] list, int a, int b) {
        Comparable temp = list[a];
        list[a] = list[b];
        list[b] = temp;
    }


    /**  Helper method to compare the priority order of two elements
     * @param obj1  non-null Comparable objec
     * @param obj2  non-null Comparable objec
     * @return true if obj1 is lower-priority than obj2 (low-value is high-priority) 
     */
    private static boolean lowerPriority(Comparable obj1, Comparable obj2) {
        return (obj1.compareTo(obj2) > 0);
    }


    /**
     * Helper method called from add()
     * New element to be added has allready been copied at index numItems
     * This will arrange items back into priority order
     */
    private void reheapUp() {
        int i = numItems;
        while(i > 1) {
            if(items[i / 2] != null && items[i / 2].compareTo(items[i]) < 0) {
                break;
            }
            swap(items, i, i / 2);
            i = i / 2;
        }
    }

    /** Add a
     *  new element into the queue, maintain priority order
     *  @param obj  the element to be added to the priority-queue
     *  @return true
     */
    public boolean add(Comparable obj) {
        numItems++;
        if (numItems >= items.length) {
            doubleCapacity();
        }
        items[numItems] = obj;
        reheapUp();
        return true;
    }


    /**  
     * Helper method to count the number of children of a node at a certain index 
     * @param i   index of an item in the heap
     * @return the number children of items[i] 
     */
    private int countKids(int i) {
        int count = 0;
        if( i * 2 <= numItems)
            count++;
        if( i * 2 + 1 <= numItems)
            count++;      
        return count;
    }

    /**
     * Helper method called from remove(); 
     * Last value has already been copied into index 1: 
     * this will rearrange items back into priority order 
     */
    private void reheapDown() {
        int i = 1;
        while(i < numItems + 1) {
            Comparable parent = items[i];
            Comparable leftChild = items[i * 2];
            Comparable rightChild = items[i * 2 + 1];
            if(leftChild == null) {
                break;
            }if(leftChild != null && leftChild.compareTo(parent) > 0) {
                break;
            }if(leftChild.compareTo(parent) > 0 && rightChild.compareTo(parent) > 0) {
                break;
            }if((leftChild != null && rightChild == null) || (rightChild != null && leftChild.compareTo(rightChild) < 0)) {
                swap(items, i, i * 2);
                i *= 2;
            }else {
                swap(items, i, i * 2 + 1);
                i = i * 2 + 1;
            }
        }
    }


    /** 
     * Remove and return the highest-priority element, maintain priority order
     * @return  the highest priority element that was removed
     */
    public Comparable remove() {
        if (numItems == 0) {
            throw new NoSuchElementException();
        }
        Comparable min = items[1];
        items[1] = items[numItems];
        items[numItems] = null;
        numItems--;
        reheapDown();
        return min;
    }

    /** Helper method to double the capacity of list in case we run out of buffer space */
    private void doubleCapacity() {
        Comparable tempItems[] = new Comparable[2 * items.length - 1];
        for (int i = 0; i <= numItems; i++) {
            tempItems[i] = items[i];
        }
        items = tempItems;
    }


    /** 
     * Return the logical contents of list as a string
     * @return  a String showing the contents of the list in the form [a0, a1, a2, ... ,an] where n is (numElements-1)  
     */  
    @Override
    public String toString() {
        String ans = "[";
        for (int i = 1; i <= numItems; i++) {
            ans += items[i];
            if(i <= numItems-1) {
                ans += ", ";   
            }
        }
        return ans+"]";
    }
}
