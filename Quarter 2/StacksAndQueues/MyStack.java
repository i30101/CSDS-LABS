/**
 * @author Andrew Kim
 * @version 1.0.0
 * @since 5 November 2023
 */

// import external libraries
import java.util.*;

public class MyStack<anyType> implements Stackable<anyType> {
    
    private List<anyType> list;

    public MyStack() {
        list = new ArrayList<>();
    }

    public void push(anyType x) {
        list.add(0, x);
    }

    public anyType pop() {
        if (list.size() == 0) {
            throw new IndexOutOfBoundsException();
        }
        anyType previous = list.get(0);
        list.remove(0);
        return previous;
    }

    public anyType peek() {
        if (list.size() == 0) {
            throw new IndexOutOfBoundsException();
        }
        return list.get(0);
    }

    public boolean isEmpty() {
        return list.size() == 0;
    }

    public int size() {
        return list.size();
    }

    @Override
    public String toString() {
        String output = "";
        for(anyType l : list) {
            output += l.toString() + " ";
        }
        return output;
    }
}
