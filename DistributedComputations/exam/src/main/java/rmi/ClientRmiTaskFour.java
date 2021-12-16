package rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class ClientRmiTaskFour {
    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
        int choice = 1000;
        int x, y;
        Scanner in = new Scanner(System.in);
        try {
            RMIServer st = (RMIServer) Naming.lookup("//localhost:123/exam");
            System.out.println("Choose option:\n"
                    + "1 - display Abiturients with bad marks\n"
                    + "2 - display Abiturients with sum marks higher then given\n"
                    + "3 - display N Abiturients with the highest marks sum\n");
            choice = in.nextInt();
            switch (choice) {
                case 1:
                    System.out.println(st.displayBadMarks());
                    break;
                case 2:
                    System.out.print("Enter threshold sum value");
                    x = in.nextInt();
                    System.out.println(st.displayHighSumBiggerThanGiven(x));
                    break;
                case 3:
                    System.out.print("Enter N");
                    x = in.nextInt();
                    System.out.println(st.displayNHighestSum(x));
            }
        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            e.printStackTrace();
        }
    }
}

