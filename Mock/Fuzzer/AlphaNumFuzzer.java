import java.util.Random;

public class AlphaNumFuzzer {
    private static String alphaNum =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
            "abcdefghijklmnopqrstuvwxyz" +
            "0123456789";

    public static String fuzz() {
        StringBuilder strBld = new StringBuilder();
        int limit = new Random().nextInt(1025);
        for (int i = 0, len = alphaNum.length(); i < limit; i++) {
            int index = new Random().nextInt(len);
            strBld.append(alphaNum.charAt(index));
        }

        return strBld.toString();
    }

    public static void main(String[] args) {
        System.out.println(AlphaNumFuzzer.fuzz());
    }
}
