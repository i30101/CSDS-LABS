/** A single node to be used with double-linked lists  d oberle 10/2021 */
public class dListNode<anyType> {
    /** Data filed that stores the value in the node */   
    private anyType value;

    /** Data filed that is a reference to the previous node in the list */
    private dListNode prev;

    /** Data filed that is a reference to the next node in the list */
    private dListNode next;

    /** 
     * A 3-arg constructor to set a node's value, previous and next pointers 
     * @param initValue  what we want to store in the node
     * @param initPrev   what we want this node's previous pointer to point to 
     * @param initNext   what we want this node's next pointer to point to
     */
    public dListNode(anyType initValue, dListNode initPrev, dListNode initNext) {
        value = initValue;
        prev = initPrev;
        next = initNext;
    }

    /** 
     * A 1-arg constructor to set a node's value where the previous and next pointers are null
     * @param initValue  what we want to store in the node
     */
    public dListNode(anyType initValue)	{
        this(initValue, null, null);			//calls the objects other constructor
    }

    /** 
     * Return the value stored in the node; pre-condition: this is not null
     * @return the value that is stored in the node
     */
    public anyType getValue() {
        return value;
    }

    /** 
     * Return a pointer to the next node in the list; pre-condition: this is not null
     * @return a reference to the next node in the list
     */
    public dListNode getNext() {
        return next;
    }

    /** 
     * Return a pointer to the previous node in the list; pre-condition: this is not null
     * @return a reference to the previous node in the list
     */
    public dListNode getPrev() {
        return prev;
    }

    /** 
     * Allow the value of the node to be changed to a new value; pre-condition: this is not null
     *  @param newValue  what we want to change the value to
     */
    public void setValue(anyType newValue) {
        value = newValue;
    }

    /** 
     * Allow the previous-pointer of the node to be changed to a new node; pre-condition: this is not null
     * @param newPrev  what we want to change the previous pointer to point to
     */
    public void setPrev(dListNode newPrev) {
        prev = newPrev;
    }

    /** 
     * Allow the next-pointer of the node to be changed to a new node; pre-condition: this is not null
     * @param newNext  what we want to change the next pointer to point to
     */
    public void setNext(dListNode newNext) {
        next = newNext;
    }

    /** 
     * Return the value contents of node as a string
     * @return  a String showing the contents of the value in the node  
     */
    @Override
    public String toString() {
        return value.toString();
    } 
}
