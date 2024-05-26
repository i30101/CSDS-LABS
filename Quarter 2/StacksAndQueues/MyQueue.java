/**
 * @author Andrew Kim
 * @version 1.0.0
 * @since 6 November 2023
 */

// import external libraries
import java.util.*;

public class MyQueue <anyType> implements Queueable<anyType> {
    private List<anyType> list;

    public MyQueue() {
        list = new ArrayList<>();
    }

    public boolean isEmpty() {
        return list.size() == 0;
    }

    public void add(anyType x) {
        list.add(x);
    }

    public anyType remove() {
        if (list.isEmpty()) {
            throw new IndexOutOfBoundsException();
        }

        anyType previous = list.get(0);
        list.remove(0);
        return previous;
    }

    public anyType peek() {
        if (list.isEmpty()) {
            throw new IndexOutOfBoundsException();
        }

        return list.get(0);
    }

    public int size() {
        return list.size();
    }


    @Override
    public String toString() {
        String output = "";
        for (anyType l : list) {
            output += l.toString() + " ";
        }
        return output;
    }
        
}
