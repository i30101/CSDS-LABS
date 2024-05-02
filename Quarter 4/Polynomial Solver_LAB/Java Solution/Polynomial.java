/**
 * @version 2.0.0, 2 May 2024
 * @author Andrew Kim
 * 
 * Polynomial object for polynomial solver
 */

import java.util.*;

class Polynomial {
    // the coefficients of the polynomial
    private double[] coefficients;

    // the degree of the polynomial
    // 1 less than the number of coefficients
    private int degree;

    /**
     * Creates a new polynomial object
     * @param coeffs coefficients in the order of decreasing powers
     */
    public Polynomial(double[] coeffs) {
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


    /* ************************ HELPER METHODS ************************ */

    /**
     * Returns whether a number is a root or not
     * @param x number to check
     * @return true if number is zero, false if not
     */
    public boolean isRoot(double x) {
        return f(x) == 0;
    }



    /**
     * Finds the indeces where there must be a root between that and the next
     * @param xRange the range of x-values
     * @return 
     */
    private int[] findRootIndeces(double[] xRange) {
        ArrayList<Integer> indexList = new ArrayList<Integer>();
        for (int i = 0; i < xRange.length - 1; i++) {
            if (!PolHelper.sameSigns(f(xRange[i]), f(xRange[i + 1]))) {
                indexList.add(i);
            }
        }
        int[] indeces = new int[indexList.size()];
        for (int i = 0; i < indexList.size(); i++) {
            indeces[i] = indexList.get(i);
        }
        return indeces;
    }


    /**
     * Finds zeroes of the polynomial
     * @param almostZero desired tolerance for calculating the zero
     * @param xRange range of x-values to consider
     * @return list of zeroes
     */
    public double[] findZeroes(double almostZero, double[] xRange) {
        ArrayList<Double> zeroList = new ArrayList<Double>();
        int[] rootIndeces = findRootIndeces(xRange);
        for (int i = 0; i < rootIndeces.length; i++) {
            double lower = xRange[rootIndeces[i]];
            double upper = xRange[rootIndeces[i] + 1];
            double mid = PolHelper.average(lower, upper);
            while (Math.abs(upper - lower) > almostZero) {
                if (PolHelper.sameSigns(f(lower), f(mid))) {
                    lower = mid;
                } else if (PolHelper.sameSigns(f(mid), f(upper))) {
                    upper = mid;
                } else {
                    System.out.println("Bisection error");
                }
                mid = PolHelper.average(lower, upper);
            }
            zeroList.add(mid);
        }
        double[] zeroes = new double[zeroList.size()];
        for (int i = 0; i < zeroList.size(); i++) {
            zeroes[i] = zeroList.get(i);
        }
        return zeroes;
    }
}
