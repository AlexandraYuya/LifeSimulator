package itumulator.world;
import java.time.Instant;
import java.util.Random;


/**
 * By implementing this interface, other actors / objects within the world may exist on top of it.
 * However, two objects implementing {@link PRNG} cannot exist on top of each other.
 */
public interface PRNG {
    int seed = (int) Instant.now().getEpochSecond();
    Random rand = new Random(seed);
    
    public static Random rand() {
        return rand;
    }
}
