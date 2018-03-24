import java.io.*;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;

public class Final {
    private static final int numOfSockets = 2;
    private static volatile boolean isFound = false;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(4321);
            ClientThread[] cts = new ClientThread[numOfSockets];
            int index = 0;

            System.out.println("Waiting for " + numOfSockets + " connections.");
            while (index < numOfSockets) {
                Socket socket = serverSocket.accept();
                cts[index] = new ClientThread(socket, index, numOfSockets);
                index++;
            }
            System.out.println("Finished establishing all " + numOfSockets + " connections.");
            System.out.println();

            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            String inputLine = null;

            while (inputLine == null) {
                System.out.print("Enter a semi-prime number: ");
                inputLine = stdIn.readLine();
                if (inputLine != null && !inputLine.equalsIgnoreCase("") && checkLong(inputLine)) {
                    for (ClientThread ct : cts) {
                        ct.setNum(Long.valueOf(inputLine));
                    }

                    System.out.println("Awaiting client response...");
                    while (!isFound) {
                        // idle wait
                    }

                    for (ClientThread ct : cts) {
                        ct.getSocket().close();
                        ct.join();
                    }

                } else {
                    System.out.println("Invalid input.");
                }
            }

            System.out.println("Done");

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static boolean checkLong(String input) {
        try {
            long i = Long.valueOf(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static class ClientThread extends Thread {
        private Socket socket;
        private BigInteger n;
        private BigInteger init;
        private BigInteger stepSize;

        public ClientThread(Socket socket, int index, int numOfThreads) {
            this.socket = socket;
            this.init = BigInteger.valueOf(index + 2);
            this.stepSize = BigInteger.valueOf(numOfThreads);
        }

        public void setNum(long num) {
            this.n = BigInteger.valueOf(num);
            this.start();
        }

        public Socket getSocket() {
            return socket;
        }

        @Override
        public void run() {
            try {
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                out.println("Server message incoming...");
                out.println("start " + n + " " + init + " " + stepSize);

                String line = in.readLine();
                if (line != null) {
                    System.out.println(line);
                    isFound = true;
                }

            } catch (IOException ignored) {

            }
        }
    }
}


