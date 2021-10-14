package manager;

import sun.misc.Signal;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ServerThread extends Thread {
    private final BufferedReader in;
    private final BufferedWriter out;
    private final ArrayList<Double> gainedValue;
    private final String funcName;

    ServerThread(String funcName, Socket socket, String parameter, ArrayList<Double> gainedValue) throws IOException {
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.gainedValue = gainedValue;
        this.funcName = funcName;
        send(parameter);
        setDaemon(true);
        start();
    }

    @Override
    public void run() {
        String message;
        boolean endProcessCheck = false;
        Signal.handle(new Signal("INT"), signal -> Manager.cancel.set(true));

        try {

            while (!endProcessCheck) {
                message = in.readLine();
                endProcessCheck = processMessage(message);
            }

            System.out.println("A socket closed\n");

            if (gainedValue.size() == 2){ // Evaluate final result in non-main thread
                Double firstResult = gainedValue.get(0);
                Double secondResult = gainedValue.get(1);
                System.out.println("Final result: " + firstResult + "*" +  secondResult + "=" + firstResult*secondResult);
                Manager.gotFinalResult.set(true);
            }

        } catch (IOException e ) {
            System.out.println(e);
            Manager.handleSoftFail(funcName);
        }
    }

    private void send(String msg) {
        try {
            out.write(msg + "\n");
            out.flush();
        } catch (IOException ignored) {
        }
    }

    private boolean processMessage(String msg) {
        if (msg != null) {
            if (msg.contains("Exception occurred:")){
                System.out.println(msg);
                Manager.handleSoftFail(funcName);
            }
            if (msg.equals("stop"))
                return true;
            if (msg.contains("Answer F: ") || msg.contains("Answer G: "))
                synchronized (gainedValue) {
                    gainedValue.add(Double.parseDouble(msg.substring("Answer ?: ".length())));
                }
            System.out.println(msg);
        }
        return false;
    }
}
