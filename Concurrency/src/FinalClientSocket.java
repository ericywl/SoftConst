import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;

public class FinalClientSocket {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 4321);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in =
                    new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));

            BigInteger zero = new BigInteger("0");
            String line;
            while ((line = in.readLine()) != null) {
                String[] lineArr = line.split(" ");
                if (lineArr[0].equalsIgnoreCase("start")) {
                    BigInteger n = BigInteger.valueOf(Long.valueOf(lineArr[1].trim()));
                    BigInteger init = BigInteger.valueOf(Long.valueOf(lineArr[2].trim()));
                    BigInteger stepSize = BigInteger.valueOf(Long.valueOf(lineArr[3].trim()));

                    System.out.println("Number: " + n);
                    System.out.println("Init: " + init);
                    System.out.println("Step size: " + stepSize);
                    System.out.println();
                    System.out.println("Computing...");

                    while (init.compareTo(n) < 0) {
                        if (n.remainder(init).compareTo(zero) == 0) {
                            System.out.println("Got it: " + init + "\n");
                            out.println("Got it: " + init);;
                            return;
                        }

                        init = init.add(stepSize);
                    }

                } else {
                    System.out.println(line);
                }
            }

            socket.close();
            in.close();
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
