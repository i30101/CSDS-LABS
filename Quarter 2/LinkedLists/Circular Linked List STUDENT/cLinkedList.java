/**a circular-linked list container - conserves memory (no buffer space)
   d oberle 10/2021.
*/
public class cLinkedList<anyType> implements ListInterface<anyType>
{
/** Data field: a reference to the first node in the list */
   private ListNode<anyType> head;

/** No arg constructor initializes the LinkedList to an empty list. */
   public cLinkedList()		
   {
      head = null;
   }

/**
 * Adds a new element to the front of the LinkedList.
 *
 * @param  x a non-null Object.
 */
   public void addFirst(anyType x)				
   {
      //WRITE THIS METHOD***********************************************
   }

/**
 * Adds a new element to the end of the LinkedList.
 *
 * @param  x a non-null Object.
 */
   public void addLast(anyType x)
   {
       //WRITE THIS METHOD***********************************************
       //if list is empty
       //   head will also be the last node who's next points to itself
       //else
       //   make current go to the last element
       //   make the current's next become a new ending node who's next points back to the first
   }

/**Retrieve the first node in the LinkedList if the head is not null
 * @return the value of the first node in the List, or null if the head is null
 */
   public anyType getFirst()
   {
      if (head==null)							//if list is empty
         return null;							//this is our flag for an unsuccessful add
      return head.getValue();
   }

/**Retrieve the last node in the LinkedList if the head is not null
 * @return the value of the last node in the List, or null if the head is null
 */
   public anyType getLast()
   {
      if (head==null)							//if list is empty
         return null;
   
      ListNode<anyType> current = head;
      while(current.getNext()!= head)		//make current go to the last element
         current = current.getNext();
      return current.getValue();
   }

/**Remove the first node in the LinkedList and return its value if the head is not null
 * @return the value of the node removed from the List, or null if the LinkedList is empty
 */
   public anyType removeFirst() 
   {
      //WRITE THIS METHOD***********************************************
      return null;						//temporary code to keep the file compiling
   }

/**Remove the last element of the list and return its value if the list is not empty
 * @return the value of the element removed, or null if the list is empty
 */
   public anyType removeLast()
   {
      //WRITE THIS METHOD***********************************************
      return null;						//temporary code to keep the file compiling
   }
   
/**Returns the number of logical elements stored in the LinkedList.
 * @return the size of the LinkedList.
 */
   public int size()
   {
      //WRITE THIS METHOD***********************************************
      return 0;							//temporary code to keep the file compiling
   }

/**Finds the Object that resides at a given index
 * @param index a value such that index is greater or equal to 0 and index is less than size()
 * @return the value stored in the node at the given index, or null if the list is empty or invalid index
 */
   public anyType get(int index)		
   {
      //WRITE THIS METHOD***********************************************
      return null;						//temporary code to keep the file compiling
   }

/**Change the Object that resides at a given index to a new value
 * @param index a value such that index is greater or equal to 0 and index is less than size()
 * @param x a non-null Object
 * @return the old value stored in the node at the given index, or null if the list is empty or invalid index
 */
   public anyType set(int index, anyType x)
   {
      //WRITE THIS METHOD***********************************************
      return null;						//temporary code to keep the file compiling
   }

/**Add a new element at the end of the list
 * @param x a non-null Object
 * @return true
 */
   public boolean add(anyType x)
   {
      addLast(x);
      return true;			
   }	

/**Add a new element at a given index
 * @param index a value such that index is greater or equal to 0 and index is less than size()
 * @param x a non-null Object
 * @return if the element was added successfully, false if the index is invalid
 */
   public boolean add(int index, anyType x)
   {
      //WRITE THIS METHOD***********************************************
      return true;			
   }

/**Remove a node that resides at a given index and return its value
 * @param index a value such that index is greater or equal to 0 and index is less than size()
 * @return the value of the element removed, or null if the list is empty or invalid index
 */
   public anyType remove(int index)		
   {
      //WRITE THIS METHOD***********************************************
      return null;						//temporary code to keep the file compiling
   }

/**Returns a String of all the elements in the List in the form [a0, a1, a2, . . . , an-1]
 * @return String of all the list elements separated by a comma
 */
   public String toString()
   {
      //WRITE THIS METHOD***********************************************
      return null;
   }

/**Finds if the LinkedList is empty (true) or contains items (false).
 * @return whether or not the LinkedList is empty.
 */
   public boolean isEmpty()
   {
      if (head == null)
         return true;
      return false;
   }
}