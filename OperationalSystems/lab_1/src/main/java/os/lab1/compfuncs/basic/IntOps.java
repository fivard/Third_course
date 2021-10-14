package os.lab1.compfuncs.basic;

import java.util.Optional;

public class IntOps {
    public static Optional<Integer> trialF(Integer x){
        return Optional.of(x * 3);
    }

    public static Optional<Integer> trialG(Integer x){
        return Optional.of(x + 5);
    }
}

