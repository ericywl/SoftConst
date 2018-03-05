import java.util.Random;

public class MutationSwapFuzzer {
    static String fuzz(String input) {
        int inputLen = input.length();
        if (inputLen == 1)
            return input;

        int swapIndex = new Random().nextInt(inputLen - 1);
        String[] inputArr = input.split("");
        String temp = inputArr[swapIndex];
        inputArr[swapIndex] = inputArr[swapIndex + 1];
        inputArr[swapIndex + 1] = temp;

        return String.join("", inputArr);
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("One string argument needed.");
            return;
        }

        String input = args[0];
        System.out.println("Mutating input...");
        System.out.println("Result: " + MutationSwapFuzzer.fuzz(input));
    }
}
