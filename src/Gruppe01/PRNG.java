package Gruppe01;
import java.time.Instant;
import java.util.Random;


/**
 * By implementing this interface, all random methods always have the same seed based on time of initialization.
 * This is useful for debugging and limits random errors.
 */
public interface PRNG {
    int seed = (int) Instant.now().getEpochSecond();
    Random rand = new Random(seed);
    
    static Random rand() {
        return rand;
    }
}
