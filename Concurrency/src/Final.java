import java.io.*;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * FinalClientSocket is needed to demo this properly
 *
 * Run this server class first before running FinalClientSocket
 * or else the client cannot connect
 */
public class Final {
    /* Number of connections required before the server can input number for computation
    aka. the number of FinalClientSocket instances to be run */
    private static final int numOfSockets = 2;
    private static volatile boolean isFound = false;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(4321);
            ClientWorker[] cts = new ClientWorker[numOfSockets];
            int index = 0;

            // Waiting for all the required connections
            System.out.println("Waiting for " + numOfSockets + " connections.");
            while (index < numOfSockets) {
                Socket socket = serverSocket.accept();
                cts[index] = new ClientWorker(socket, index, numOfSockets);
                index++;
            }
            System.out.println("Finished establishing all " + numOfSockets + " connections.");

            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            String inputLine = null;

            // Gets input from user and sends that input to the clients
            while (inputLine == null) {
                System.out.print("\nEnter a semi-prime number: ");
                inputLine = stdIn.readLine();
                if (inputLine != null && checkLong(inputLine)) {
                    for (ClientWorker ct : cts) {
                        ct.setNum(Long.valueOf(inputLine));
                    }

                    // Wait for any client to respond
                    System.out.println("Awaiting client response...");
                    while (!isFound) {
                        // IDLE WAIT
                    }

                    // Client responded, close all sockets and join the client workers
                    for (ClientWorker ct : cts) {
                        ct.interrupt();
                        ct.join();
                    }

                } else {
                    System.out.println("Invalid input.");
                    inputLine = null;
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

    private static class ClientWorker extends Thread {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private BigInteger n;
        private BigInteger init;
        private BigInteger stepSize;

        public ClientWorker(Socket socket, int index, int numOfThreads) {
            this.socket = socket;
            this.init = BigInteger.valueOf(index + 2);
            this.stepSize = BigInteger.valueOf(numOfThreads);
        }

        public void setNum(long num) {
            this.n = BigInteger.valueOf(num);
            this.start();
        }

        @Override
        public void run() {
            try {
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out.println("start " + n + " " + init + " " + stepSize);

                String line;
                String[] lineArr;
                while ((line = in.readLine()) != null) {
                    // Tell the client to stop computation
                    if (this.isInterrupted()) {
                        out.println("stop");
                        out.flush();
                        break;
                    }

                    // A result has been found by the client
                    lineArr = line.trim().split(" ");
                    if (lineArr[0].equalsIgnoreCase("result")) {
                        isFound = true;
                        System.out.println("Got it: " + lineArr[1]);
                        break;
                    }

                    // "Ping" the client
                    out.println("pulse check");
                    out.flush();
                }

                in.close();
                out.close();
                socket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


