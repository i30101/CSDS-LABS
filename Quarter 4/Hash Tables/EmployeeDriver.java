/**
 * @author Andrew Kim
 * @since 26 May 2024
 * @version 1.0.0
 */

import java.util.*;
import java.io.*;

public class EmployeeDriver {
    private static final int CONTAINER_SIZE = 10;

    public static Tree[] readFile(String filepath) {
        try {
            // hash table with binary tree container
            Tree[] eTable = new Tree[CONTAINER_SIZE];

            for (int i = 0; i < CONTAINER_SIZE; i++) {
                eTable[i] = new Tree();
            }

            Scanner scanner = new Scanner(new File(filepath));
            scanner.useDelimiter("\n");
            while (scanner.hasNext()) {
                String[] eStrings = scanner.next().split(" ");
                Employee tempEmployee = new Employee(Integer.parseInt(eStrings[0]), eStrings[1]);
                // System.out.println(tempEmployee.getName() + " " + tempEmployee.hashCode());
                int index = tempEmployee.hashCode() % CONTAINER_SIZE;
                eTable[index].add(tempEmployee);
            }
            scanner.close();
            return eTable;
        } catch (NumberFormatException n) {
            System.out.println("Error parsing integer");
        } catch (Exception e) {
            System.out.println("Error reading file: " + e);
        }

        return null;
    }

    public static void main(String[] args) {
        Tree[] employeeTable = readFile(EmployeeGenerator.FILEPATH);
        for (Tree tree : employeeTable) {
            // System.out.println(tree);
        }
        System.out.println("Find an employee in the directory.");
        Scanner scanner = new Scanner(System.in);
        String response = "temp";
        while (!response.equals("")) {
            System.out.print("Enter an employee name to get their number or enter to exit: ");
            response = scanner.nextLine();
            Employee tempEmployee = new Employee(-1, response);
            int index = tempEmployee.hashCode() % CONTAINER_SIZE;
            if (employeeTable[index].contains(tempEmployee)) {
                System.out.println("Employee number: " + employeeTable[index].find(tempEmployee).getNumber());
            } else {
                System.out.println("Employee not found.");
            }
        }
        System.out.println("Goodbye!");
        scanner.close();
    }
}
