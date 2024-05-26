/** A single node to be used with a binary-search-tree  d oberle 10/2021 */
public class TreeNode {
    /** Data filed that stores the Comparable value in the node */
    private Comparable value;
    /** Data filed that points to the left-subtree of this node */
    private TreeNode left; 
    /** Data filed that points to the right-subtree of this node */
    private TreeNode right;


    /** A 3-arg constructor to set a node's value and left and right pointers 
     * @param v  what we want to store in the node
     * @param l  what we want this node's left-pointer to point to
     * @param r  what we want this node's right-pointer to point to
     */
    public TreeNode(Comparable v, TreeNode l, TreeNode r) { 
        value = v; 
        left = l; 
        right = r; 
    }


    /** A 1-arg constructor to set a node's value where the left and right pointers are null
     * @param v  what we want to store in the node
     */ 
    public TreeNode(Comparable v) { 
        value = v; 
        left = null; 
        right = null; 
    }


    /** Accessor method to return the value stored in the node; pre-condition: this is not null
     * @return the value that is stored in the node
     */
    public Comparable getValue() { 
        return value; 
    }


    /** Accessor method to return what the left-pointer points to; pre-condition: this is not null
     * @return the reference that is pointed to by the left-pointer
     */
    public TreeNode getLeft() { 
        return left; 
    }


    /** Accessor method to return what the right-pointer points to; pre-condition: this is not null
     * @return the reference that is pointed to by the right-pointer
     */
    public TreeNode getRight() { 
        return right; 
    }


    /** Mutator method to allow the value of the node to be changed to a new value; pre-condition: this is not null
     * @param v  what we want to change the value to
     */
    public void setValue(Comparable v) { 
        value = v;
    }


    /** Mutator method to allow the left-pointer to be changed to point to something new; pre-condition: this is not null
     * @param l  what we want the left-pointer to point to
     */
    public void setLeft(TreeNode l) { 
        left = l; 
    }


    /** Mutator method to allow the right-pointer to be changed to point to something new; pre-condition: this is not null
     * @param r  what we want the right-pointer to point to
     */
    public void setRight(TreeNode r) { 
        right = r; 
    }


    /** Return the value contents of node as a string
     * @return  a String showing the contents of the value in the node  
     */
    @Override
    public String toString() {
        return value.toString();
    }
}


