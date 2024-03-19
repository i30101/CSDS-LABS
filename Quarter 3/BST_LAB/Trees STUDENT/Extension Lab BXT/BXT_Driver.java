
   import java.util.*;
   import java.io.*;

	/*******************
	Driver for a binary expression tree class.
	Input: a postfix string, each token separated by a space.
	**********************/
    public class BXT_Driver
   {
       public static void main(String[] args)
      {
         BXT tree = new BXT();
         Scanner sc = new Scanner(System.in);
         System.out.print("Input postfix string: ");
         String s =  sc.nextLine();    //"3 4 5 + *", "3 3 + 5 / 4 5 - *"
         tree.buildTree(s);
         System.out.print("Infix order:  ");
         tree.inorderTraverse();
         System.out.print("\nPrefix order:  ");
         tree.preorderTraverse();
         System.out.print("\nEvaluates to " + tree.evaluateTree());
      }
   }
   
	
	/***************************************
   Input postfix string: 3 3 + 5 / 4 5 - *
    		5
    	-
    		4
    *
    		5
    	/
    			3
    		+
    			3
    Infix order:  3 + 3 / 5 * 4 - 5 
    Prefix order: * / + 3 3 5 - 4 5 
    Evaluates to -1.2
	************************************/