public class MutationSwapFuzzer {
    public static String fuzz(String input) {
        int inputLen = input.length();
        int index = RandomRange.randomWithRange(0, inputLen - 2);

        String[] inputArr = input.split("");
        String temp = inputArr[index];
        inputArr[index] = inputArr[index + 1];
        inputArr[index + 1] = temp;

        return String.join("", inputArr);
    }

    public static void main(String[] args) {
        String input = "STUDENT";
        System.out.println(MutationSwapFuzzer.fuzz(input));
    }
}
