package clients;

import java.io.*;
import java.net.Socket;

public class FunctionF {
    private static Socket clientSocket;
    private static BufferedReader in;
    private static BufferedWriter out;

    public static void main(String[] args) {
        try {
            try {
                clientSocket = new Socket("localhost", 4004);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                out.write("Client was connected\n");
                out.flush();

                int parameter = Integer.parseInt(in.readLine());

                out.write(Integer.toString(parameter*5)+ "\n");
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
