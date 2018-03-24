import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;

public class FinalClientSocket {
    private static PrintWriter out;
    private static BigInteger n;
    private static BigInteger init;
    private static BigInteger stepSize;
    private static volatile boolean isFound = false;
    private static final Object outLock = new Object();

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 4321);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            System.out.println("Connected to server.");
            String line;

            // Wait for server message
            while ((line = in.readLine()) != null) {
                String[] lineArr = line.trim().split(" ");
                if (lineArr[0].equalsIgnoreCase("start")) {
                    System.out.println("\nServer computation request received...");
                    n = BigInteger.valueOf(Long.valueOf(lineArr[1].trim()));
                    init = BigInteger.valueOf(Long.valueOf(lineArr[2].trim()));
                    stepSize = BigInteger.valueOf(Long.valueOf(lineArr[3].trim()));

                    System.out.println("Number: " + n);
                    System.out.println("Init: " + init);
                    System.out.println("Step size: " + stepSize);
                    System.out.println();
                    System.out.println("Computing...");

                    out.println("start work");
                    out.flush();
                    FinalClientSocketWorker fw = new FinalClientSocketWorker();
                    fw.start();

                    String newLine;
                    while (!isFound) {
                        // Asked to stop by the server
                        newLine = in.readLine();
                        if (newLine == null || newLine.equalsIgnoreCase("stop")) {
                            System.out.println("Interrupted by server.");
                            fw.interrupt();
                            fw.join();
                            break;
                        }

                        // "Ping" back to the server
                        synchronized (outLock) {
                            out.println("im alive");
                            out.flush();
                        }
                    }

                    out.close();
                    in.close();
                    socket.close();
                    return;
                }
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static class FinalClientSocketWorker extends Thread {
        @Override
        public void run() {
            BigInteger zero = new BigInteger("0");

            while (init.compareTo(n) < 0) {
                if (this.isInterrupted()) {
                    return;
                }

                if (n.remainder(init).compareTo(zero) == 0) {
                    synchronized (outLock) {
                        out.println("result " + init);
                        out.flush();

                        System.out.println("Got it: " + init);
                        isFound = true;
                        return;
                    }
                }

                init = init.add(stepSize);
            }
        }
    }
}
