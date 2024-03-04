/**a subset of the java.util.Stack interface
 d oberle 10/2021.
*/
public interface Stackable<anyType> {
    
    /** add x to the top of the stack.
     * @param  x a non-null anyType object.
     */
    public void push(anyType x);


    /** removes and returns the element at the top of a non-empty stack.
     * @return the value that was removed; returns null if the stack is empty.
     */
    public anyType pop();
                             

    /** returns the element at the top of a non-empty stack.
     * @return the element at the top of the stack; returns null if the stack is empty.
     */                                     
    public anyType peek();
                        

    /** lets the client know if the stack has any elements or is empty.
     * @return true if the stack is empty; returns false if the stack has elements stored in it.
     */                                     
    public boolean isEmpty();


    /** returns the number of logical elements stored in the stack.
     *   @return the number of elements added into the stack.
     */    
    public int size();
}