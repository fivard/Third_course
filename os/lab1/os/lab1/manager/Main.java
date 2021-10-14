package manager;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {

        System.out.println("Enter a parameter");
        String parameter = inputParameter();

        Manager.run(parameter);
    }

    private static String inputParameter(){
        Scanner s = new Scanner(System.in);
        return s.next();
    }
}
