/**
 * @version 2.0.0, 2 May 2024
 * @author Andrew Kim
 * 
 * Helper class for polynomial solver
 */

public class PolHelper {
    /**
     * Sees if two numbers have the same sign
     * @param a the first number
     * @param b the second number
     * @return whether both have the same sign or not
     */
    public static boolean sameSigns(double a, double b) {
        return (a > 0) == (b > 0);
    }

    public static double average(double a, double b) {
        return (a + b) / 2;
    }
}
