package task_a;

public class App {
    public static void main(String[] args){
        Pot pot = new Pot(10);
        Bear bear = new Bear(pot);
        int numOfBees = 5;

        Thread threadBear = new Thread(bear);
        threadBear.start();

        for(int i =0 ; i < numOfBees; i++){
            Thread threadBee = new Thread(new Bee(bear, pot));
            threadBee.start();
        }
    }
}
