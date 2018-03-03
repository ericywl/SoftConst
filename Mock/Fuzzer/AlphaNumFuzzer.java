public class AlphaNumFuzzer {
    private static String alphaNum =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
            "abcdefghijklmnopqrstuvwxyz" +
            "0123456789";

    public static String fuzz() {
        StringBuilder strBld = new StringBuilder();
        int limit = randomWithRange(0, 1024);
        for (int i = 0, len = alphaNum.length(); i < limit; i++) {
            int index = randomWithRange(0, len - 1);
            strBld.append(alphaNum.charAt(index));
        }

        return strBld.toString();
    }

    private static int randomWithRange(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("Min cannot be greater than max.");
        }

        int range = max - min + 1;
        return (int) (Math.random() * range) + min;
    }

    public static void main(String[] args) {
        System.out.println(AlphaNumFuzzer.fuzz());
    }
}
