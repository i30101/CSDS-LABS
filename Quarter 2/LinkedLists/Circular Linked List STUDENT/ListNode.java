/** A single node to be used with single-linked lists  d oberle 10/2021 */
public class ListNode<anyType>
{
   /** Data filed that stores the value in the node */   
   private anyType value;
   /** Data filed that is a reference to the next node in the list */
   private ListNode next;

   /** A two-arg constructor to set a node's value and next pointer 
    *  @param initValue  what we want to store in the node
    *  @param initNext   what we want this node's next pointer to point to
    */
   public ListNode(anyType initValue, ListNode initNext)
   {
      value = initValue;
      next = initNext;
   }
 
   /** A 1-arg constructor to set a node's value where the next pointer is null
    *  @param initValue  what we want to store in the node
    */   
   public ListNode(anyType initValue)
   {
      this(initValue, null);					//this calls the objects other constructor
   }

  /** Return the value stored in the node; pre-condition: this is not null
   * @return the value that is stored in the node
   */
   public anyType getValue()				
   {
      return value;
   }

  /** Return a pointer to the next node in the list; pre-condition: this is not null
   * @return a reference to the next node in the list
   */  
   public ListNode getNext()
   {
      return next;
   }

  /** Allow the value of the node to be changed to a new value; pre-condition: this is not null
   *  @param newValue  what we want to change the value to
   */
   public void setValue(anyType newValue)
   {
      value = newValue;
   }

  /** Allow the next-pointer of the node to be changed to a new node; pre-condition: this is not null
   *  @param newNext  what we want to change the next pointer to point to
   */
   public void setNext(ListNode newNext)
   {
      next = newNext;
   }
   
  /** Return the value contents of node as a string
   *  @return  a String showing the contents of the value in the node  
   */
   @Override
   public String toString()
   {
      return value.toString();
   } 
}
