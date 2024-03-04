/** 
 * a linked list container - conserves memory (no buffer space)
 * d oberle 10/2021.
 */

public class LinkedList<anyType> implements ListInterface<anyType> {
    
    /** Data field: a reference to the first node in the list */
    private ListNode<anyType> head;

    /**
     * No arg constructor initializes the LinkedList to an empty list.
     */
    public LinkedList() {
        head = null;
    }

    /**
     * Finds if the LinkedList is empty (true) or contains items (false).
     * @return whether or not the LinkedList is empty.
     */
    public boolean isEmpty() {
        if (head == null) {
            return true;
        }
        return false;							//temporary code to keep the file compiling
    }   

    /**
     * Adds a new element to the front of the LinkedList.
     * @param  x a non-null Object.
     */
    public void addFirst(anyType x) {
        head = new ListNode(x, head);
    }

    /**
     * Adds a new element to the end of the LinkedList.
     * @param  x a non-null Object.
     */
    public void addLast(anyType x) {
        if (head==null)	{
            head = new ListNode(x,null);					//head refers to the only node
        } else {
            ListNode current = head;
            while(current.getNext()!= null)				//make current go to the last element
                current = current.getNext();
            current.setNext(new ListNode(x, null));	//make the last element's next become a new ending node
        }
    }

    /**
     * Retrieve the first node in the LinkedList if the head is not null
     * @return the value of the first node in the List, or null if the head is null
     */
    public anyType getFirst() {
        if (head==null){
            return null;							//this is our flag for an unsuccessful add
        }
        return head.getValue();
    }

    /**
     * Retrieve the last node in the LinkedList if the head is not null
     * @return the value of the last node in the List, or null if the head is null
     */
    public anyType getLast(){
        if (head == null) {
            return null;
        }
        ListNode current = head;
        while (current.getNext() != null) {
            current = current.getNext();
        }
        return (anyType) current.getValue();						//temporary code to keep the file compiling
    }   

    /**
     * Remove the first node in the LinkedList and return its value if the head is not null
     * @return the value of the node removed from the List, or null if the LinkedList is empty
     */
    public anyType removeFirst() {
        anyType firstValue = head.getValue();
        head = head.getNext();
        return firstValue;						//temporary code to keep the file compiling
    }

    /**
     * Remove the last element of the list and return its value if the list is not empty
     * @return the value of the element removed, or null if the list is empty
     */
    public anyType removeLast() {
        if (head==null) {
            return null;
        }
        anyType temp = getLast();
        if (head.getNext() == null) {							//only one element in the list
            head = null;
        }else {
            ListNode current = head;							//current will traverse the list
            while(current.getNext().getNext() != null)	//move current to the second to last node
                current=current.getNext();
            current.setNext(null);								//then cap off the end of the new, smaller list with null
        }	
        return temp;
    }

    /**
     * Returns the number of logical elements stored in the LinkedList.
     * @return the size of the LinkedList.
     */
    public int size() {
        if (head == null) {
            return 0;
        }
        int i = 1;
        ListNode current = head;
        while (current.getNext() != null) {
            current = current.getNext();
            i++;
        }
        return i;
    }

    /**
     * Finds the Object that resides at a given index
     * @param index a value such that index is greater or equal to 0 and index is less than size()
     * @return the value stored in the node at the given index, or null if the list is empty or invalid index
     */
    public anyType get(int index) {
        if (head == null || index > size() - 1) {
            return null;
        }
        ListNode current = head;
        for(int i = 0; i < index; i++) {
            current = current.getNext();
        }
        return (anyType) current.getValue();						//temporary code to keep the file compiling
    }	

    /**
     * Change the Object that resides at a given index to a new value
     * @param index a value such that index is greater or equal to 0 and index is less than size()
     * @param x a non-null Object
     * @return the old value stored in the node at the given index, or null if the list is empty or invalid index
     */
    public anyType set(int index, anyType x) {
        if (head == null || index > size() - 1) {
            return null;
        }
        ListNode current = head;
        for(int i = 0; i < index; i++) {
            current = current.getNext();
        }
        anyType removed = (anyType) current.getValue();
        current.setValue(x);
        return removed;						//temporary code to keep the file compiling
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
    public boolean add(int index, anyType x) {
        if (head == null || index > size() - 1) {
            return false;
        }
        ListNode current = head;
        for (int i = 0; i < index - 1; i++) {
            current = current.getNext();
        }
        current.setNext(new ListNode(x, current.getNext()));
        return true;			
    }	

    /**
     * Remove a node that resides at a given index and return its value
     * @param index a value such that index is greater or equal to 0 and index is less than size()
     * @return the value of the element removed, or null if the list is empty or invalid index
     */
    public anyType remove(int index) {
        if (head == null || index > size() - 1) {
            return null;
        }
        ListNode current = head;
        for (int i = 0; i < index - 1; i++) {
            current = current.getNext();
        }
        anyType remove = (anyType) current.getNext().getNext().getValue();
        current.setNext(current.getNext().getNext());
        return remove;
    }	

    /**
     * Returns a String of all the elements in the List in the form [a0, a1, a2, . . . , an-1]
     * @return String of all the list elements separated by a comma
     */
    public String toString() {
        String ans = "[";									//start with left bookend						
        ListNode current =  head;
        while(current != null){
            ans += current.getValue().toString();
            current = current.getNext();
            if (current != null) {							//don't add comma after the last element
                ans += ",";
            }
        }
        ans += "]";											//end with right bookend
        return ans;
    }
}