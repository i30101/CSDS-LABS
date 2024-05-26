//Comparison of how much work/time is used to compute the nth term of the Fibonacci sequence
//Iterative (loop) versus recursion.
public class FibRecur
{
   //Recursive method to find the nth term in the fibonacci sequence O(2^n).
   //pre: n >=1
   //post:returns the nth term in the fibonacci sequence
   public static long fib(int n)
   {
      if(n <= 2)
         return 1;
      return fib(n-1) + fib(n-2);   
   }
   
   //Iterative method to find the nth term in the fibonacci sequence O(n).
   //pre: n >=1
   //post:returns the nth term in the fibonacci sequence
   public static long fib2(int n)
   {
      if(n <= 2)
         return 1;
      long fibo1=1, fibo2=1, sum=1;
      for(int i=3; i<=n; i++)
      {
         sum = fibo1 + fibo2; 
         fibo1 = fibo2;
         fibo2 = sum;
      }
      return sum; 
   }
         
   public static void main(String [] arg)
   {
      double startTime=0, endTime=0, lastTime = 0, seconds = 0;
      int nthTerm = 1;
      
      //try to compute the first 130 Fibonacci numbers 
      //and time them until a computation takes a second or more 
      System.out.println("Recursive fib:");
      for(nthTerm=1; nthTerm<=130; nthTerm++)
      {
         lastTime = startTime;
         startTime = System.currentTimeMillis();
         System.out.print("fib("+nthTerm+"):"+ fib(nthTerm));
         endTime = System.currentTimeMillis();
         seconds = (endTime - startTime)/1000.0;
         System.out.println("\t time:"+seconds+" sec");
         startTime = seconds;
         if(seconds >= 1)
         {  //it took a second or longer, so break out and estimate how long it will take to get to fib(125)
            break;
         }
      }
      //find how much longer it will take to find fib(n+1) after finding fib(n)
      double ratio = startTime/lastTime; //should be close to the golden ratio, around 1.618
      //ratio will likely be a little more than the golden ratio because of anything the 
      //processor is doing in the background will make it take more time
      System.out.println("***********************\n");
      System.out.println("time ratio from fib(n) to fib(n+1):"+ratio);
      System.out.println("golden ratio                      :"+((1+Math.sqrt(5))/2.0)+"\n");
      System.out.println("***********************\n");
      //extrapolate to see how long it will take to get to fib(130)
      for(int i=nthTerm; i<=130; i++)
      {
         System.out.println("Recursive fib("+i + ") will take " + convertTime(startTime));
         startTime *= ratio;
      }
      System.out.println("***********************");
      startTime = System.currentTimeMillis();
      System.out.print("Iterative fib(130):"+ fib2(130));
      endTime = System.currentTimeMillis();
      seconds = (endTime - startTime)/1000.0;
      System.out.println("\t time:"+seconds+" sec");
      System.out.println("***********************\n");
   
   }
      
   //post: give a simple conversion of seconds to the largest unit of time
   //      from billions of years down to minutes, rounded to two decimal places
   public static String convertTime(double seconds)
   {
      double years = seconds/31536000;
      seconds %= 31536000;
      double months = seconds/2628288;
      seconds %= 2628288;
      double days = seconds/86400;
      seconds %= 86400;
      double hours = seconds/3600;
      seconds %= 3600;
      double minutes = seconds/60;
      seconds %= 60;
      if(years > 1)
      {
         if(years > 1000000000.0d)
            return round(years/1000000000.0d) +" billion years";
         if(years > 1000000.0d)
            return round(years/1000000.0d) +" million years";
         if(years > 1000.0d)
            return round(years/1000.0d) +" thousand years";
         return round(years)+" years";
      }
      else if(months > 1)
         return round(months)+" months";
      else if(days > 1)
         return round(days)+" days";
      else if(hours > 1)
         return round(hours)+" hours";
      else if(minutes > 1)
         return round(minutes)+" minutes";
      return round(seconds)+" seconds";
   }
   
   //post: returns the number rounded to two decimal places
   public static double round(double n)
   {
      return ((int)(n*100))/100.0;
   }
}