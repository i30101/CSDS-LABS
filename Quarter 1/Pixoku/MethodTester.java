//tester program to see if your invert and mirrorFlip methods work.
//use PixokiDriver program to test checkForWin
public class MethodTester
{
   public static void show(int[][]board)
   {
      for(int r=0; r<board.length; r++)
      {
         for(int c=0; c<board[0].length; c++)
            System.out.print(board[r][c]);
         System.out.println();
      }
      System.out.println();
   }


   public static void main(String[]arg)		//should display:	100
   {														//						011
      int[][]board = {{1,0,0},{0,1,1}};		//
      System.out.println("default board");
      show(board);									//						011					

      System.out.println("default inverted");
      show(Pixoku.invert(board));				//						100					

      System.out.println("default");
      show(board);

      System.out.println("default slipped");
      show(Pixoku.mirrorFlip(board));			//						
   														//						001
   														//						110
                                             //
      show(board);                           //                100
                                             //                011       
                                             
      System.out.println("temp inverted");
      int[][] temp = {
         {1, 2, 3},
         {4, 5, 6}
      };
      show(Pixoku.mirrorFlip(temp));
      

      int[][]guess1 = {{1,0,0},{0,1,1}};
      System.out.println(Pixoku.testCheckForWin(board, guess1));                                       
                                             //                true
      int[][]guess2 = {{-1,0,2},{3,-1,1}};
      System.out.println(Pixoku.testCheckForWin(board, guess2));                                       
                                             //                true
      int[][]guess3 = {{1,0,1},{0,1,1}};
      System.out.println(Pixoku.testCheckForWin(board, guess3));                                       
                                             //                false
      int[][]guess4 = {{1,0,0},{0,1,0}};
      System.out.println(Pixoku.testCheckForWin(board, guess4));                                       
                                             //                false                                                                                                  
   }
}