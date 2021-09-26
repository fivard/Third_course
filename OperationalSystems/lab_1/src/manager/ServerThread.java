package manager;

import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread {
    private final BufferedReader in;
    private final BufferedWriter out;

    ServerThread(Socket socket, String parameter) throws IOException {
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        send(parameter);
        start();
    }

    @Override
    public void run() {
        String word = "";
        try {
            while (true) {
                word = in.readLine();
                if (word != null)
                    System.out.println(word);
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private void send(String msg) {
        try {
            out.write(msg + "\n");
            out.flush();
        } catch (IOException ignored) {}
    }
}
