   //Name:
	//Date: 
   import java.util.StringTokenizer;
 	/***********************************
	Represents a binary expression tree.
	The BXT can build itself from a postorder expression.  It can
	evaluate and print itself. It also prints an inorder string and a preorder string.  
	************************************/
    public class BXT
   {
      private int count;
      private TreeNode root;
   
       public BXT()
      {
         count = 0;
         root = null;
      }
       public BXT(Object obj)
      {
         count = 1;
         root = new TreeNode(obj, null, null);
      }
      /***********************
   	Builds a BXT from a postfix expression.  Uses a helper stack of TreeNodes.
   	****************************/
       public void buildTree(String str)
      {
         //enter code here
      }
       public double evaluateTree()
      {
         return evaluateNode(root);
      }
       private double evaluateNode(TreeNode root)  //recursive
      {
          //enter code here
      }
       private double computeTerm(String s, double a, double b)
      {
          //enter code here
      }
       private boolean isOperator(String s)
      {
          //enter code here
      }
    
   	 
     // inorder traverse
   	 
     // preorder traverse
   }
	
