package manager;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ServerThread extends Thread {
    private final BufferedReader in;
    private final BufferedWriter out;
    private final ArrayList<Integer> gainedValue;

    ServerThread(Socket socket, String parameter, ArrayList<Integer> gainedValue) throws IOException {
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.gainedValue = gainedValue;
        send(parameter);
        start();
    }

    @Override
    public void run() {
        String message;
        boolean endProcessCheck = false;

        try {

            while (!endProcessCheck) {
                message = in.readLine();
                endProcessCheck = processMessage(message);
            }

            System.out.println("A socket stopped\n");

            if (gainedValue.size() == 2){ // Evaluate final result in non-main thread
                int firstResult = gainedValue.get(0);
                int secondResult = gainedValue.get(1);
                System.out.println("Final result: " + firstResult + "*" +  secondResult + "=" + firstResult*secondResult);
                Manager.server.close();
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

    private boolean processMessage(String msg) {
        if (msg != null) {
            if (msg.equals("stop"))
                return true;
            if (msg.contains("Answer:"))
                synchronized (gainedValue) {
                    gainedValue.add(Integer.parseInt(msg.substring("Answer:".length())));
                }
            System.out.println(msg);
        }
        return false;
    }
}
