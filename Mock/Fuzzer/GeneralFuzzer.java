import java.io.*;
import java.util.Random;
import java.util.regex.Pattern;

public class GeneralFuzzer {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("One pathname argument required.");
            return;
        }

        String filePathName = args[0];
        try {
            GeneralFuzzer.fuzz(filePathName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void fuzz(String filePath) throws IOException {
        File readFile = new File(filePath);
        BufferedReader br = new BufferedReader(new FileReader(readFile));
        File writeFile = new File(getWriteFileName(readFile));
        BufferedWriter bw = new BufferedWriter(new FileWriter(writeFile));

        String line;
        while ((line = br.readLine()) != null) {
            if (line.isEmpty()) {
                bw.write(line);
                continue;
            }

            // choose fuzzing method from the currently available
            int fuzzMethod = new Random().nextInt(3);
            String resultLine;
            switch (fuzzMethod) {
                case 0:
                    resultLine = MutationSwapFuzzer.fuzz(line);
                    break;
                case 1:
                    resultLine = BitFlipFuzzer.fuzz(line);
                    break;
                case 2:
                    resultLine = TrimFuzzer.fuzz(line);
                    break;
                default:
                    resultLine = line;
                    break;
            }

            bw.write(resultLine + "\n");
        }

        br.close();
        bw.close();
    }

    private static String getWriteFileName(File readFile) {
        String[] readFileNameArr = readFile.getName().split(Pattern.quote("."));
        readFileNameArr[0] = readFileNameArr[0] + "-fuzz";

        String writeFileName = String.join(".", readFileNameArr);
        String[] absFilePathArr = readFile.getAbsolutePath()
                .split(Pattern.quote(System.getProperty("file.separator")));
        int aLen = absFilePathArr.length;
        absFilePathArr[aLen - 1] = writeFileName;

        return String.join(File.separator, absFilePathArr);
    }
}

class BitFlipFuzzer {
    static String fuzz(String input) {
        int inputLen = input.length();
        int binIndex = new Random().nextInt(inputLen);
        String binary = Integer.toBinaryString(input.codePointAt(binIndex));
        char flippedChar = bitFlip(binary);

        return input.substring(0, binIndex) + flippedChar + input.substring(binIndex + 1, inputLen);
    }

    private static char bitFlip(String binaryInput) {
        int flipIndex = new Random().nextInt(binaryInput.length() - 1);
        String[] binaryArr = binaryInput.split("");
        binaryArr[flipIndex] = (binaryArr[flipIndex].equals("0")) ? "1" : "0";

        return (char) Integer.parseInt(String.join("", binaryArr), 2);
    }
}

class TrimFuzzer {
    static String fuzz(String input) {
        int trimIndex = new Random().nextInt(input.length() - 1);

        return input.substring(0, trimIndex);
    }
}
