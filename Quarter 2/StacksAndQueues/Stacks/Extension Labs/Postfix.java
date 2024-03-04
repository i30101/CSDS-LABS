   import java.util.*;
    public class Postfix
   {
       public static void main(String[] args)
      {
         String s = "35*7+";
         System.out.println(s + " = " + eval(s) + "\n");
         System.out.println((s = "354*+7*") + " = " + eval(s) + "\n");
         System.out.println((s = "82-") + " = " + eval(s) + "\n");
         System.out.println((s = "82/") + " = " + eval(s) + "\n");
      }
       public static int eval(String x)
      {
      
         return -1;
      }
       public static int eval(int a, int b, char ch)
      {
         switch(ch) 
         {
            case '*':  
               return a * b;
            case '/': 
               return b / a;
            case '+': 
               return a + b;
            case '-': 
               return b - a;
         }
         return 0;
      }
       public static boolean isOperator(char ch)
      {
         return ch == '*' || ch == '/' || ch == '+' || ch == '-';
      }
   }