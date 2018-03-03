public class MutationSwapFuzzer {
    public static String swapFuzz(String input) {
        int inputLen = input.length();
        int index = randomWithRange(0, inputLen - 2);
        String preStr = input.substring(0, index);
        String postStr = input.substring(index + 2, inputLen);
        return preStr + input.charAt(index + 1) + input.charAt(index) + postStr;
    }

    private static int randomWithRange(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("Min cannot be greater than max.");
        }

        int range = max - min + 1;
        return (int) (Math.random() * range) + min;
    }

    public static void main(String[] args) {
        String input = "STUDENT";
        System.out.println(MutationSwapFuzzer.swapFuzz(input));
    }
}
