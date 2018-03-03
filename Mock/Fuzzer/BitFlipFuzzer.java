public class BitFlipFuzzer {
    public static String fuzz(String input) {
        int inputLen = input.length();
        int index = RandomRange.randomWithRange(0, inputLen - 1);
        String binary = Integer.toBinaryString(input.codePointAt(index));

        return input;
    }

    private static String bitFlip(String binaryInput) {

    }

    public static void main(String[] args) {
        BitFlipFuzzer.fuzz("abcdee");
    }
}
