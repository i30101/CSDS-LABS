//D Oberle 6/5/23
import javax.swing.JFrame;
import java.awt.*;

public class FossilSweeperDriver					   
{
   public static GamePanel screen;					            //Our Custom JPanel

   public static void main(String[]args)
   {
      JFrame frame = new JFrame("Fossil Sweeper");                            //window title
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      frame.setSize((int)screenSize.getWidth(), (int)screenSize.getHeight());	//Size of game window
      frame.setLocation(1, 1);				                                    //location of game window on the screen
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
      screen = new GamePanel((int)screenSize.getWidth(), (int)screenSize.getHeight());
      frame.setContentPane(screen);		                                       //fill the frame with the JPanel
      frame.setVisible(true);
      frame.addKeyListener(screen);		           
   } 
}
