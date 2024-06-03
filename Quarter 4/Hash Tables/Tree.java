/**
 * @author Andrew Kim
 * @since 2 June 2024
 * @version 1.0.0
 */


public class Tree {
    private TreeNode myRoot;


    /**
     * 0-arg constructor creates empty tree
     */
    public Tree() {
        myRoot = null;
    }

    /**
     * Adds new element to Tree
     * @param e Employee to be added
     */
    public void add(Employee e) {
        myRoot = addHelper(myRoot, e);
    }

    private TreeNode addHelper(TreeNode root, Employee e) {
        if (myRoot == null) {
            root = new TreeNode(e);
        } else if (e.hashCode() > root.getEmployee().hashCode()) {
            if (root.getRight() == null) {
                root.setRight(new TreeNode(e));
            } else {
                addHelper(root.getRight(), e);
            }
        } else {
            if (root.getLeft() == null) {
                root.setLeft(new TreeNode(e));
            } else {
                addHelper(root.getLeft(), e);
            }
        }
        return root;
    }

    /**
     * Returns whether an Employee is present in tree or not
     * @param e the Employee
     * @return whether the Employee is in the tree or not
     */
    public boolean contains(Employee e) {
        return searchHelper(myRoot, e) != null;
    }

    public TreeNode searchHelper(TreeNode root, Employee e) {
        if (root == null) {
            return null;
        } else if (root.getEmployee().getName().equals(e.getName())) {
            return root;
        } else if (root.getEmployee().hashCode() > e.hashCode() || root.getEmployee().hashCode() == e.hashCode()) {
            return searchHelper(root.getLeft(), e);
        } else {
            return searchHelper(root.getRight(), e);
        }
    }

    public Employee find(Employee e) {
        return searchHelper(myRoot, e).getEmployee();
    }

    private String temp;

    private void inOrderHelper(TreeNode root) {
        if (root != null) {
            inOrderHelper(root.getLeft());
            temp += (root.getEmployee().getName() + " " + root.getEmployee().hashCode() + ", ");
            inOrderHelper(root.getRight());
        }
    }

    @Override
    public String toString() {
        System.out.println("Printing root: " + myRoot);
        temp = "";
        inOrderHelper(myRoot);
        if (temp.length() > 1) {
            temp = temp.substring(0, temp.length() - 2);
        }
        return "[" + temp + "]";
    }
}
