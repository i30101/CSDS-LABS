/**
 * @author Andrew Kim
 * @since 2 June 2024
 * @version 1.0.0
 */

public class Employee {
    private int number;
    private String name;


    /**
     * Creates an Employee based on specific inputs
     * @param n their employee number
     * @param s ther name
     */
    public Employee(int n, String s) {
        number = n;
        name = s;
    }

    
    /**
     * Defines hash code of employee based on their name
     * Used for storign in Hash Table
     * @return the hash code of the employee
     */
    public int hashCode() {
        int sum = 0;
        for (int i = 0; i < name.length(); i++) {
            char letter = name.charAt(i);
            sum += (int) (letter) * i;
        }
        return sum;
    }


    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public boolean equals(Employee e) {
        return (name.equals(e.getName())) && (number == e.getNumber());
    }

    @Override
    public String toString() {
        return number + " " + name;
    }
}
