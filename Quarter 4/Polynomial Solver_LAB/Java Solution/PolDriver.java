/**
 * @version 2.0.0
 * @author Andrew Kim
 * 
 * Driver class for polynomial solver
 */

public class PolDriver {
    // tolerance for finding value of roots
    public static final double almostZero = 0.0000000001;
    
    // minimum of considered values
    public static final double min = -1000000;
    
    // maximum of considered values
    public static final double max = 1000000;
    
    // number of divions in x range
    public static final int resolution = 1000000;
    
    public static void main(String[] args) {
        double[] xRange = new double[resolution];
        double xTemp = 0;
        for (int i = 0; i < xRange.length; i++) {
            xRange[i] = min + xTemp;
            xTemp += (max - min) / resolution;
        }
        System.out.println(xRange);
    }
}
