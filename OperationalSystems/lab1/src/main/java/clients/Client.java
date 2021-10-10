package clients;

import java.io.*;
import java.net.Socket;
import java.util.Timer;
import java.util.function.Function;

public class Client {
    private static Socket clientSocket;
    private static BufferedReader in;
    private static BufferedWriter out;

    public void process(Function<Integer,Integer> func) throws IOException {
        try {
            try {
                clientSocket = new Socket("localhost", 4004);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                out.write("Client was connected\n");
                out.flush();

                int parameter = Integer.parseInt(in.readLine());
                out.write("Answer:" + func.apply(parameter) + "\n");
                out.flush();

                out.write("stop\n");
                out.flush();
            } finally {
                clientSocket.close();
                in.close();
                out.close();
            }
        } catch (IOException e) {
            out.write(e + "\n");
            out.flush();
        }
    }
}
