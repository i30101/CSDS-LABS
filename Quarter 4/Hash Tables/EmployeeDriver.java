/**
 * @author Andrew Kim
 * @since 26 May 2024
 * @version 1.0.0
 */
import java.util.*;
import java.io.*;

public class EmployeeDriver {

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(new File(EmployeeGenerator.FILEPATH));
            scanner.useDelimiter("\n");
            while (scanner.hasNext()) {
                System.out.println(scanner.next());
            }
            scanner.close();
        } catch (Exception e) {
            System.out.println("Could not read file");
        }
    }
}
