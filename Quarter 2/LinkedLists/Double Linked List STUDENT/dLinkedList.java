/**a linked list container with double-linked nodes - conserves memory (no buffer space)
   Note - if you want a dLinkedList that is also circular, you will not need a tail pointer.
   (The tail of a double-circular list would be known as head's previous pointer.)
   Otherwise, non-circular dLinkedLists are recommended to have an additional data field
   of a tail pointer to get O(1) efficiency for accessing or modifying the last element.
   Also consider an additional data-field to keep track of the number of elements so that 
   the size() method can be done with O(1) efficiency.
   d oberle 12/2021.
*/
public class dLinkedList<anyType> implements ListInterface<anyType>
{
    /** Data field: a reference to the first node in the list */
    private dListNode<anyType> head;

    // last element of LinkedList
    private dListNode<anyType> tail;

    // size of LinkedList
    private int size;

    /**
     * No arg constructor initializes the dLinkedList to an empty list.
     */
    public dLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    /**
     * Adds a new element to the front of the LinkedList.
     * @param  x a non-null Object.
     */
    public void addFirst(anyType x) {
        //WRITE THIS METHOD***********************************************
        head = new dListNode(x, null, head);
        size++;
        if (size == 1) {
            tail = head;
        }
    }

    /**
     * Adds a new element to the end of the LinkedList.
     * @param  x a non-null Object.
     */
    public void addLast(anyType x) {
        if (head == null) {
            addFirst(x);
        } else {
            tail.setNext(new dListNode(x, tail, null));
            tail = tail.getNext();
            size++;
        }
    }

    /**
     * Retrieve the first node in the LinkedList if the head is not null
     * @return the value of the first node in the List, or null if the head is null
     */
    public anyType getFirst() {
        if (head==null)							//if list is empty
            return null;
        return head.getValue();
    }

    /**
     * Retrieve the last node in the LinkedList if the head is not null
     * @return the value of the last node in the List, or null if the head is null
     */
    public anyType getLast() {
        if (head == null) {
            return null;
        }
        return tail.getValue();						//temporary code to keep the file compiling
    }

    /**
     * Remove the first node in the LinkedList and return its value if the head is not null
     * @return the value of the node removed from the List, or null if the LinkedList is empty
     */
    public anyType removeFirst() {
        if (head==null)	{			   //if list is empty
            return null;
        }
        head = head.getNext();
        head.setPrev(null);
        size--;
        return null;						//temporary code to keep the file compiling
    }

    /**
     * Remove the last element of the list and return its value if the list is not empty
     * @return the value of the element removed, or null if the list is empty
     */
    public anyType removeLast() {
        if (head==null)					//if list is empty
            return null;
        dListNode last = tail;
        tail = tail.getPrev();
        tail.setNext(null);
        size--;
        return tail.getValue();						//temporary code to keep the file compiling
    }

    /**
     * Returns the number of logical elements stored in the LinkedList.
     * @return the size of the LinkedList.
     */
    public int size() {
        //WRITE THIS METHOD***********************************************
        return size;							//temporary code to keep the file compiling
    }

    /**
     * Finds the Object that resides at a given index
     * @param index a value such that index is greater or equal to 0 and index is less than size()
     * @return the value stored in the node at the given index, or null if the list is empty or invalid index
     */
    public anyType get(int index) {
        //WRITE THIS METHOD***********************************************
        if (head == null || index > size - 1) {
            return null;
        }
        dListNode current = head;
        for(int i = 0; i < index; i++) {
            current = current.getNext();
        }
        return (anyType) current.getValue();
    }	

    /**
     * Change the Object that resides at a given index to a new value
     * @param index a value such that index is greater or equal to 0 and index is less than size()
     * @param x a non-null Object
     * @return the old value stored in the node at the given index, or null if the list is empty or invalid index
     */
    public anyType set(int index, anyType x) {
        //WRITE THIS METHOD***********************************************
        if (head == null || index > size - 1) {
            return null;
        }
        dListNode current = head;
        for(int i = 0; i < index; i++) {
            current = current.getNext();
        }
        anyType old = (anyType) current.getValue();
        current.setValue(x);
        return old;
    }	

    /**
     * Add a new element at the end of the list
     * @param x a non-null Object
     * @return true
     */
    public boolean add(anyType x) {
        addLast(x);
        return true;			
    }	

    /**
     * Add a new element at a given index
     * @param index a value such that index is greater or equal to 0 and index is less than size()
     * @param x a non-null Object
     * @return if the element was added successfully, false if the index is invalid
     */
    public boolean add(int index, anyType x){
        if (head == null || index > size - 1) {
            return false;
        }
        dListNode current = head;
        for (int i = 0; i < index - 1; i++) {
            current = current.getNext();
        }
        current.setNext(new dListNode(x, current.getPrev(), current.getNext()));
        if (index == size - 1) {
            tail = current;
        }
        size++;
        return true;
    }	

    /**
     * Remove a node that resides at a given index and return its value
     * @param index a value such that index is greater or equal to 0 and index is less than size()
     * @return the value of the element removed, or null if the list is empty or invalid index
     */
    public anyType remove(int index) {
        if (head == null || index > size - 1) {
            return null;
        }
        dListNode current = head;
        for (int i = 0; i < index - 1; i++) {
            current = current.getNext();
        }
        if (index == size - 1) {
            tail = current.getNext().getNext();
        }
        anyType remove = (anyType) current.getNext().getNext().getValue();
        current.setNext(current.getNext().getNext());
        current.getNext().getNext().setPrev(current.getNext());
        size--;
        return remove;
    }	
        

    /**
     * Returns a String of all the elements in the List in the form [a0, a1, a2, . . . , aan-1]
     * @return String of all the list elements separated by a comma
     */
    //******************NOTE************************************************************************
    //If you are making a dLinkedList that is also circular, this method will need some modification
    //**********************************************************************************************/
    public String toString() {
        String ans = "[";									//start with left bookend						
        dListNode<anyType> current =  head;
        while(current != null)
        {
            ans += current.getValue().toString();
            current = current.getNext();
            if (current != null)							//don't add comma after the last element
                ans += ",";
        }
        ans += "]";											//end with right bookend
        return ans;
    }

    /**
     * Finds if the dLinkedList is empty (true) or contains items (false).
     * @return whether or not the dLinkedList is empty.
     */
    public boolean isEmpty() {
        if (head == null)
            return true;
        return false;
    }

}