package java_9;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class Auction {
    public static final int countParticipants = 10;
    private static final int countLots = 5;

    public static ArrayList<Participant> participants= new ArrayList<Participant>();
    public static final AtomicBoolean startLot = new AtomicBoolean(false);

    public static void main(String[] args) throws InterruptedException {

        for (int i = 0; i < countParticipants; i++){
            participants.add(new Participant(i));
        }

        TimeUnit.SECONDS.sleep(1);
        for (int i = 0; i < countLots; i++){
            System.out.println("\nSTARTED LOT " + i);
            Lot lot = new Lot(i);
            lot.run();
        }
    }
}
