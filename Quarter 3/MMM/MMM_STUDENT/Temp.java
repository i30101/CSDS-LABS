public class Temp {
    public static void main(String[] args) {
        String test = "tesing";
        String encoded = Utilities.encode(test);
        System.out.println(encoded);
        String decoded = Utilities.decode(encoded);
        System.out.println(decoded);
    }
}
