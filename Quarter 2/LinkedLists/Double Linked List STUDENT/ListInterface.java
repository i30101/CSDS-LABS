/**a subset of the java.util.List interface
   d oberle 10/2021
*/
public interface ListInterface<anyType>
{
/**
 * adds element x to the end of the list.
 *
 * @param  x a non-null anyType object.
 * @return true if added correctly (possibly false for containers with restrictions).
 */
   public boolean add(anyType x);
 
 /**
 * adds element x to the list at a particular index.
 *
 * @param  index index >= 0 and index is less than or equal to size().
 * @param  x a non-null anyType object.
 * @return true if added correctly (possibly false for containers with restrictions).
 */
   public boolean add(int index, anyType x);	
  
 /**
 * returns the number of logical elements stored in the list.
 *
 * @return the number of elements added into the list.
 */ 									
   public int size();								
   
 /**
 * changes the element at a specific index to x, returning the element that was originally there.
 *
 * @param  index index >= 0 and index is less than size().
 * @param  x a non-null anyType object.
 * @return the old value that was replaced with x.
 */   
   public anyType set(int index, anyType x);
   
 /**
 * returns the element at a specific index (first element is index 0).
 *
 * @param  index index >= 0 and index is less than size().
 * @return the element stored at position index.
 */   
   public anyType get(int index);
   
 /**
 * removes and returns the element at a specific index to x
 *
 * @param  index index >= 0 and index is less than size().
 * @return the value that was removed.
 */
   public anyType remove(int index);
}