package manager;

import sun.misc.Signal;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class Manager {
    private static final ArrayList<ServerThread> serverThreadsList = new ArrayList<>();
    private static final ArrayList<Double> gainedValue = new ArrayList<>();
    private static final ArrayList<Process> processesList = new ArrayList<>();
    private static ServerSocket server;

    public static AtomicBoolean gotFinalResult = new AtomicBoolean(false);
    public static AtomicBoolean cancel = new AtomicBoolean(false);

    public static void run(String parameter) throws IOException {
        Signal.handle(new Signal("INT"), signal -> cancel.set(true));

        server = new ServerSocket(4003, 2);
        System.out.println("Server started\n");

        initialiseProcess("F", parameter);
        initialiseProcess("G", parameter);

        while(!gotFinalResult.get()) {
            if (cancel.get()) {
                handleHardFail();
            }
        }
        endSession();
    }


    private static void endSession(){
        System.out.println("Server closed!");
        for (Process p : processesList)
            p.destroy();
        System.exit(0);
    }

    public static void handleHardFail(){
        System.out.println("Hard fail");
        endSession();
    }

    public static void handleSoftFail(String funcName){
        System.out.println("Soft Fail in " + funcName + ", can't finish computation");
        endSession();
    }

    public static void initialiseProcess(String functionName, String parameter) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(
                "java", "clients.Function" + functionName);
        processBuilder.directory(new File("/Users/bernada/Documents/GitHub/Third_course/OperationalSystems/lab_1/target/classes"));
        processesList.add(processBuilder.start());

        Socket socket = server.accept();
        serverThreadsList.add(new ServerThread(functionName, socket, parameter, gainedValue));
    }
}
