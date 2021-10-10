package manager;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Manager {
    public static ServerSocket server;

    private static final ArrayList<ServerThread> serverList = new ArrayList<>();
    private static final ArrayList<Integer> gainedValue = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        System.out.println("Enter a parameter");
        String parameter = inputParameter();

        Process processF = initialiseProcess("F");
        Process processG = initialiseProcess("G");

        try{
            server = new ServerSocket(4004, 2);
            System.out.println("Server started\n");

            while (true){
                Socket socket = server.accept();
                try {
                    serverList.add(new ServerThread(socket, parameter, gainedValue));
                } catch (IOException e) {
                    socket.close();
                }
            }

        } catch (Exception e){
            System.out.println("Server closed!");
            processF.destroy();
            processG.destroy();
        }
    }

    private static String inputParameter(){
        Scanner s = new Scanner(System.in);
        return s.next();
    }

    private static Process initialiseProcess(String functionName) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(
                "java", "clients.Function" + functionName);
        processBuilder.directory(new File("/Users/bernada/Documents/GitHub/Third_course/OperationalSystems/lab1/target/classes"));
        return processBuilder.start();
    }
}
