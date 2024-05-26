/** an interface for a priority queue.  d oberle 10/2021 */
public interface PriorityQueue {

   /**
    * returns true if the number of elements in the priority queue is 0
    * returns false otherwise
    * @return queue is emtpy
    */
   boolean isEmpty(); 


   /**
    * adds x to the priority queue; the number of elements increased by 1.
    * @param x a non-null object that implements the Comparable interface.
    * @return true if x is added correctly (false for queues with limitations to adding).
    */
   boolean add(Comparable x); 

   /** 
    * removes and returns the highest-priority item from the priority queue; 
    * the number of elements decreased by 1. 
    * precondition: the queue is not empty.
    * @return the value removed.
    */
   Comparable remove(); 
   
   /** 
    * returns the highest-priority item from the priority queue.
    * precondition: the queue is not empty.
    * @return the highest-priority item in the front of the queue (top of the heap).
    */ 
   Comparable peek();	 

} 

