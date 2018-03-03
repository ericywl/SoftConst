public class AlphaNumFuzzer {
    private static String alphaNum =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
            "abcdefghijklmnopqrstuvwxyz" +
            "0123456789";

    public static String fuzz() {
        StringBuilder strBld = new StringBuilder();
        int limit = RandomRange.randomWithRange(0, 1024);
        for (int i = 0, len = alphaNum.length(); i < limit; i++) {
            int index = RandomRange.randomWithRange(0, len - 1);
            strBld.append(alphaNum.charAt(index));
        }

        return strBld.toString();
    }

    public static void main(String[] args) {
        System.out.println(AlphaNumFuzzer.fuzz());
    }
}
