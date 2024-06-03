/**
 * @author Andrew Kim
 * @since 2 June 2024
 * @version 2.0.0
 */

public class TreeNode {
    private Employee employee;

    private TreeNode left;

    private TreeNode right;

    /**
     * Creates new TreeNode object
     * @param e Employee object
     * @param l what we want left pointer to point to
     * @param r what we want right pointer to point to
     */
    public TreeNode(Employee e, TreeNode l, TreeNode r) {
        employee = e;
        left = l;
        right = r;
    }

    /**
     * Sets left and right pointers to null
     * @param e Employee object 
     */
    public TreeNode(Employee e) {
        employee = e;
        left = null;
        right = null;
    }

    public Employee getEmployee() {
        return employee;
    }

    public TreeNode getRight() {
        return right;
    }

    public TreeNode getLeft() {
        return left;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    
    public void setLeft(TreeNode left) {
        this.left = left;
    }
    
    public void setRight(TreeNode right) {
        this.right = right;
    }

    @Override
    public String toString() {
        return employee.getNumber() + " " + employee.getName();
    }

    // @Override
    public boolean equals(Employee e) {
        return (employee.getNumber() == e.getNumber()) && (employee.getName().equals(e.getName()));
    }
}
