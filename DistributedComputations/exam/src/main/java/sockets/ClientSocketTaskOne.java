package sockets;

import model.Abiturient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ClientSocketTaskOne {

    private static int port = 9876;

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException{
        InetAddress host = InetAddress.getLocalHost();
        Socket socket = null;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        Scanner scan = new Scanner(System.in);
        while (true)
        {
            System.out.println("Choose option:\n"
                    + "1 - display Abiturients with bad marks\n"
                    + "2 - display Abiturients with sum marks higher then given\n"
                    + "3 - display N Abiturients with the highest marks sum\n");
            socket = new Socket(host.getHostName(), port);
            oos = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("Sending request to Socket Server");
            int commandIndex = scan.nextInt();
            if (commandIndex == 3)
            {
                socket = new Socket(host.getHostName(), port);
                oos = new ObjectOutputStream(socket.getOutputStream());
                System.out.println("Sending close Request");
                oos.writeInt(commandIndex); oos.flush();
                break;
            }
            switch (commandIndex) {
                case 1:  {
                    String message = "" + commandIndex;
                    oos.writeBytes(message);
                    oos.flush();
                    break;
                }
                case 2: {
                    System.out.println("Enter count");
                    Integer count = scan.nextInt();
                    String message = "" + commandIndex + "#" + count;
                    oos.writeBytes(message);
                    oos.flush();
                    break;
                }
                case 3: {
                    System.out.println("Enter N");
                    Integer N = scan.nextInt();
                    String message = "" + commandIndex + "#" + N;
                    oos.writeBytes(message);
                    oos.flush();
                    break;
                }

            }
            System.out.println("Results: ");
            ois = new ObjectInputStream(socket.getInputStream());
            ArrayList<Abiturient> results = (ArrayList<Abiturient>) ois.readObject();
            for (Abiturient abiturient: results)
            {
                System.out.println(abiturient);
            }
            ois.close();
            oos.close();
            Thread.sleep(100);
        }
        oos.writeInt(4);
        System.out.println("Shutting down client!!");
    }
}

