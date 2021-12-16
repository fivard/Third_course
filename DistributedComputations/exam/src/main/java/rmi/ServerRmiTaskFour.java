package rmi;

import model.Abiturient;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

interface RMIServer extends Remote {
    List<Abiturient> displayBadMarks();
    List<Abiturient> displayHighSumBiggerThanGiven(int a);
    List<Abiturient> displayNHighestSum(int count);
}

public class ServerRmiTaskFour {
    public static void main(String[] args) throws RemoteException {
        Registry registry = LocateRegistry.createRegistry(123);
        RMIServer service = new Service();
        registry.rebind("exam", service);
        System.out.println("Server started!");
    }

    static class Service extends UnicastRemoteObject implements RMIServer {
        List<Abiturient> abiturients;

        Service() throws RemoteException {
            super();
            abiturients = new ArrayList<Abiturient>() {
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
        }

        @Override
        public List<Abiturient> displayBadMarks() {
            List<Abiturient> results = new ArrayList<>();
            for(Abiturient abiturient: abiturients) {
                for (int mark : abiturient.getMarks())
                    if (mark < 10){
                        results.add(abiturient);
                        break;
                    }
            }
            return results;
        }

        @Override
        public List<Abiturient> displayHighSumBiggerThanGiven(int a) {
            List<Abiturient> results = new ArrayList<>();
            for(Abiturient abiturient: abiturients) {
                int sum = 0;
                for (int mark : abiturient.getMarks())
                    sum += mark;
                if(sum > a) {
                    results.add(abiturient);
                }
            }
            return results;
        }

        @Override
        public List<Abiturient> displayNHighestSum(int count) {
            List<Abiturient> temp = abiturients;
            List<Abiturient> results = new ArrayList<>();
            Collections.sort(temp);
            for (int i = 0; i < count; i++)
                results.add(temp.get(temp.size() - 1 - i));
            return results;
        }
    }
}
