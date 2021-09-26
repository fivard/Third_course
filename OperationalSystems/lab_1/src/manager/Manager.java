package manager;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Manager {
    private static final int PORT = 4004;
    private static ServerSocket server;
    private static ArrayList<ServerThread> serverList = new ArrayList<>();
    public static boolean closenServer = false;

    public static void main(String[] args) throws IOException {
        System.out.println("Enter a parameter");
        String parameter = inputParameter();
        try{
            server = new ServerSocket(PORT, 2);
            System.out.println("Server started");

            ProcessBuilder processBuilderF = new ProcessBuilder("java", "-cp", "src", "clients.FunctionF");
            ProcessBuilder processBuilderG = new ProcessBuilder("java", "-cp", "src", "clients.FunctionG");
            processBuilderF.directory(new File("/Users/bernada/Documents/GitHub/Third_course/OperationalSystems/lab_1"))
                    .start();
            processBuilderG.directory(new File("/Users/bernada/Documents/GitHub/Third_course/OperationalSystems/lab_1"))
                    .start();

            while (!closenServer){
                Socket socket = server.accept();
                try {
                    serverList.add(new ServerThread(socket, parameter));
                } catch (IOException e) {
                    socket.close();
                }
            }

        } finally {
            System.out.println("Server closed!");
            server.close();
        }
    }

    private static String inputParameter(){
        Scanner s = new Scanner(System.in);
        return s.next();
    }
}
