package clients;

import java.util.function.Function;

public class FunctionG {
    public static void main(String[] args){
        Function<Integer, Integer> calculate = x -> x+5;
        Client client = new Client();
        client.process(calculate);
    }
}
