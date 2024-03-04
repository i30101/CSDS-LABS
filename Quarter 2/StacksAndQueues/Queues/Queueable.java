/**a subset of the java.util.Queue interface.
 d oberle 10/2021.
*/  
public interface Queueable <anyType> {
    /** lets the client know if the queue has any elements or is empty.
     *   @return true if the queue is empty; returns false if the queue has elements stored in it.
     */ 
    public boolean isEmpty(); 
        
    
    /** add x to the end of the queue.
     *  precondition: queue is [a1, a2, ..., an]. 
     *  postcondition: queue is [a1, a2, ..., an, x]. 
     *  @param  x a non-null E object.
     */   
    public void add(anyType x); 
        

    /** removes and returns the element at the front of a non-empty queue.
     *  precondition: queue is [a1, a2, ..., an].  
     *  postcondition: queue is [a2, ..., an]; returns a1.
     * @return the value that was removed; returns null if the queue is empty.
     */
    public anyType remove(); 


    /** returns the element at the front of a non-empty queue.
     *  precondition: queue is [a1, a2, ..., an].
     *  postcondition: returns a1.  
     * @return the element at the front of the queue; returns null if the queue is empty.
     */    
    public anyType peek(); 


    /** returns the number of logical elements stored in the queue.
     *   @return the number of elements added into the queue.
     */    
    public int size();   
}