package sockets;

import model.Abiturient;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Callback
{
    public boolean shouldEnd = false;
}

class AbiturientIterator implements Runnable
{
    private Socket socket;
    private Callback callback;
    private List<Abiturient> abiturients;

    public AbiturientIterator(Socket socket, Callback callback, List<Abiturient> abiturients)
    {
        this.callback = callback;
        this.socket = socket;
        this.abiturients = abiturients;
    }

    @Override
    public void run() {
        try {
            InputStreamReader ois = new InputStreamReader(socket.getInputStream());
            System.out.println("Receiving input");
            BufferedReader reader = new BufferedReader(ois);
            String message = reader.readLine();
            String splitMessage[] = message.split("#");
            String commandIndex = splitMessage[0];
            System.out.println("Command " + splitMessage[0]);

            if (commandIndex.equals("4"))
            {
                System.out.println("Close command");
                callback.shouldEnd = true;
                return;
            }
            List<Abiturient> result = new ArrayList<>();
            switch (commandIndex) {
                case "1": {
                    for(Abiturient abiturient: abiturients) {
                        for (int mark : abiturient.getMarks())
                            if (mark < 10){
                                result.add(abiturient);
                                break;
                            }
                    }
                    break;
                }
                case "2": {
                    Integer a = Integer.parseInt(splitMessage[1]);
                    for(Abiturient abiturient: abiturients) {
                        int sum = 0;
                        for (int mark : abiturient.getMarks())
                            sum += mark;
                        if(sum > a) {
                            result.add(abiturient);
                        }
                    }
                    break;
                }
                case "3": {
                    Integer count = Integer.parseInt(splitMessage[1]);
                    List<Abiturient> temp = abiturients;
                    Collections.sort(temp);
                    for (int i = 0; i < count; i++)
                        result.add(temp.get(temp.size() - 1 - i));
                    break;
                }

            }
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(result);
            ois.close();
            oos.close();
            socket.close();
        }
        catch (IOException e) { }
    }
}

public class ServerSocketTaskOne {
    private static ServerSocket server;

    private static int port = 9876;

    public static Callback c = new Callback();

    public static void main(String args[]) throws IOException, ClassNotFoundException{
        server = new ServerSocket(port);
        List<Abiturient> abiturients = new ArrayList<>() {
            {
                add(new Abiturient("001", "Dmytryi", "Bernada",
                        "Kyiv", "123451234", new int[]{10, 10}));
                add(new Abiturient("002", "Mykyta", "Oleksienko",
                        "Kyiv", "123451234", new int[]{10, 10}));
                add(new Abiturient("003", "Jenya", "Vorobyov",
                        "Kyiv", "123451234", new int[]{10, 10}));
                add (new Abiturient("004", "Maryna", "Zolotaryova",
                        "Kyiv", "123451234", new int[]{10, 10}));
                add(new Abiturient("005", "Arkadii", "Cyganov",
                        "Kyiv", "123451234", new int[]{10, 10}));
            }
        };

        while(!c.shouldEnd){
            System.out.println("Waiting for the client request");
            Socket socket = server.accept();
            AbiturientIterator iterator = new AbiturientIterator(socket, c, abiturients);
            iterator.run();
        }
        System.out.println("Shutting down Socket server!!");
        server.close();
    }
}

