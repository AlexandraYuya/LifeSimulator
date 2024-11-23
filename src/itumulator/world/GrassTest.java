package itumulator.world;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import itumulator.world.*;
import java.util.*;

public class GrassTest {
    private Grass grass;
    private World world;

    @Before
    public void setUp() {
        world = new World(10);
        grass = new Grass();
        grass.placeInWorld(world);
    }

    @Test
    public void testGrassInitialization() {
        Grass newGrass = new Grass();
        assertNotNull("Grass should be created", newGrass);
    }

    @Test
    public void testGrassPlacement() {
        Location grassLocation = world.getLocation(grass);
        assertNotNull("Grass should be placed in the world", grassLocation);
        assertEquals("World should contain grass at the location",
                grass, world.getNonBlocking(grassLocation));
    }

    @Test
    public void testGrassSpreadingTimer() {
        // Store initial location
        Location initialLocation = world.getLocation(grass);
        assertNotNull("Initial grass location should not be null", initialLocation);

        // Count initial grass in surrounding area
        int initialGrassCount = countGrassInSurroundingTiles(initialLocation);

        // Act for 10 steps to trigger potential spreading
        for (int i = 0; i < 10; i++) {
            grass.act(world);
        }

        // Count grass after acting
        int finalGrassCount = countGrassInSurroundingTiles(initialLocation);
        assertTrue("Grass count should either stay same or increase",
                finalGrassCount >= initialGrassCount);
    }

    @Test
    public void testGrassSpreadingToEmptyTile() {
        // Get initial location
        Location initialLocation = world.getLocation(grass);
        assertNotNull("Initial grass location should not be null", initialLocation);

        // Clear surrounding tiles
        Set<Location> surroundingTiles = world.getSurroundingTiles(initialLocation);
        for (Location loc : surroundingTiles) {
            if (world.getNonBlocking(loc) != null) {
                world.remove(world.getNonBlocking(loc));
            }
        }

        // Act multiple times to increase chance of spreading
        for (int i = 0; i < 20; i++) {
            grass.act(world);
        }

        // Check if grass has spread to any surrounding tile
        int grassCount = countGrassInSurroundingTiles(initialLocation);
        assertTrue("Grass should have spread to at least one surrounding tile",
                grassCount > 0);
    }

    // Helper method to count grass in surrounding tiles
    private int countGrassInSurroundingTiles(Location center) {
        int count = 0;
        Set<Location> surroundingTiles = world.getSurroundingTiles(center);

        for (Location loc : surroundingTiles) {
            Object entity = world.getNonBlocking(loc);
            if (entity instanceof Grass) {
                count++;
            }
        }
        return count;
    }
}