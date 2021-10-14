package compfuncs.basic;

import java.util.Optional;

public class Conjuction {
    public static Optional<Integer> trialF(Integer x){
        return Optional.of(x != 0 ? 1 : 0);
    }

    public static Optional<Integer> trialG(Integer x){
        return Optional.of(x + 5 != 0 ? 1 : 0);
    }
}
