import java.util.*;

class Polynomial {
    private int[] coefficients;
    private int degree;

    /**
     * Creates a new polynomial object
     * @param coeffs coefficients in the order of decreasing powers
     */
    public Polynomial(int[] coeffs) {
        coefficients = coeffs;
        degree = coefficients.length - 1;
    }

    /**
     * Returns y-value of function given x-value input
     * @param x input value
     * @return output value
     */
    public double f(double x) {
        double value = 0;
        for (int i = 0; i < coefficients.length; i++) {
            value += (double) (coefficients[i]) * Math.pow(x, degree - i);
        }
        return value;
    }

    /**
     * Returns whether a number is a root or not
     * @param x number to check
     * @return true if number is zero, false if not
     */
    public boolean isRoot(double x) {
        return f(x) == 0;
    }

    /**
     * Sees if two numbers have the same sign
     * @param a the first number
     * @param b the second number
     * @return whether both have the same sign or not
     */
    private boolean sameSigns(double a, double b) {
        return (a > 0) == (b > 0);
    }

    private int[] findRootIndeces(double[] xRange) {
        ArrayList<Integer> indeces = new ArrayList<Integer>();
        for (int i = 0; i < xRange.length - 1; i++) {
            
        }
        return null;
    }
}