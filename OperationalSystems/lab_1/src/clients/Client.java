package clients;

import java.io.*;
import java.net.Socket;
import java.util.function.Function;

public class Client {
    private static Socket clientSocket;
    private static BufferedReader in;
    private static BufferedWriter out;

    public void process(Function<Integer,Integer> func) {

        try {
            try {

                clientSocket = new Socket("localhost", 4004);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                out.write("Client was connected\n");
                out.flush();

                int parameter = Integer.parseInt(in.readLine());

                out.write(Integer.toString(func.apply(parameter)) + "\n");
                out.flush();

                out.write("stop\n");
                out.flush();
            } finally {
                clientSocket.close();
                in.close();
                out.close();
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
