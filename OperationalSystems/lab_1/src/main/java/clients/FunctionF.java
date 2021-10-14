package clients;

import os.lab1.compfuncs.basic.*;

import java.io.*;
import java.net.Socket;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class FunctionF {
    private static Socket clientSocket;
    private static BufferedReader in;
    private static BufferedWriter out;
    private static final int maxAttempts = 3;

    public static void main(String[] args) throws IOException {
        try {
            clientSocket = new Socket("localhost", 4003);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

            out.write("Client was connected\n");
            out.flush();

            int parameter = Integer.parseInt(in.readLine());
            int countAttempts = 0;
            int randomValue = (int)(Math.random() * 10);
            TimeUnit.SECONDS.sleep(randomValue);
            while (countAttempts < maxAttempts) {
                if (randomValue < 4) {
                    Optional<Integer> result = IntOps.trialF(parameter);
                    if (result.isPresent()) {
                        out.write("Answer F: " + result.get() + "\n");
                        out.flush();

                        out.write("stop\n");
                        out.flush();
                    }
                } else {
                    out.write("Function F failed. Try again\n");
                    out.flush();
                    randomValue = (int)(Math.random() * 10);
                    countAttempts++;
                }
            }
            throw new ArithmeticException("Function F failed " + maxAttempts + " times");
        } catch (IOException | InterruptedException | ArithmeticException e) {
            out.write("Exception occurred: " + e + "\n");
            out.flush();

            clientSocket.close();
            in.close();
            out.close();
        }
    }
}
