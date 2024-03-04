   import java.util.*;
    public class Infix
   {
       public static void main(String[] args)
      {
         String s = "3*5+7";
         System.out.println(s + " = " + Postfix.eval(trans(s)) + "\n");
         System.out.println((s = "(3+5*4)*7") + " = " + 
                           Postfix.eval(trans(s)) + "\n");
         System.out.println((s = "8-2") + " = " + 
                           Postfix.eval(trans(s)) + "\n");
         System.out.println((s = "8/2") + " = " + 
                           Postfix.eval(trans(s)) + "\n");
      }
       public static String trans(String x)
      {
         
         return "";
      }
   	//returns true if c1 has strictly lower precedence than c2
       public static boolean isLower(char c1, char c2)
      {
         if(c1 == '*' || c1 == '/')
            return false;
         return c2 == '*' || c2 == '/';
      }
   }