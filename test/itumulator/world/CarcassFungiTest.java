package itumulator.world;

import Gruppe01.Fungi;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import itumulator.world.World;
import itumulator.world.Location;

import Gruppe01.CarcassFungi;
import org.junit.Test;

public class CarcassFungiTest {

    @Test
    public void testConstructorWithParameters() {
        CarcassFungi carcassFungi = new CarcassFungi(true, 10);
        assertTrue("CarcassFungi should be small", carcassFungi.isSmall);
        assertEquals("Amount should be 10", 10, carcassFungi.amount);
    }

    @Test
    public void testDefaultConstructor() {
        CarcassFungi carcassFungi = new CarcassFungi();
        assertEquals("Default amount should be 5", 5, carcassFungi.amount);
    }

    @Test
    public void testActMethodDecay() {
        World world = new World(10);
        CarcassFungi carcassFungi = new CarcassFungi(true, 2);
        Location location = new Location(5, 5);

        world.setTile(location, carcassFungi);

        // Simulate 20 steps to trigger one decay
        for (int i = 0; i < 20; i++) {
            carcassFungi.act(world);
        }

        assertEquals("Amount should decrease by 1 after 20 steps", 1, carcassFungi.amount);
    }

    @Test
    public void testTransformationToFungi() {
        World world = new World(10);
        CarcassFungi carcassFungi = new CarcassFungi(true, 1);
        Location location = new Location(5, 5);

        world.setTile(location, carcassFungi);

        // Simulate 20 steps to trigger transformation
        for (int i = 0; i < 20; i++) {
            carcassFungi.act(world);
        }

        // Check if the tile now contains a Fungi object
        assertTrue("Tile should contain a Fungi object", world.getTile(location) instanceof Fungi);
    }

    @Test
    public void testPlaceInWorld() {
        World world = new World(10);
        CarcassFungi carcassFungi = new CarcassFungi(true, 5);
        Location location = new Location(5, 5);

        world.setTile(location, carcassFungi);
        carcassFungi.placeInWorld(world);

        assertEquals("CarcassFungi should be placed at the specified location",
                carcassFungi, world.getTile(location));
    }

    @Test
    public void testMultipleDecayCycles() {
        World world = new World(10);
        CarcassFungi carcassFungi = new CarcassFungi(true, 3);
        Location location = new Location(5, 5);

        world.setTile(location, carcassFungi);

        // Simulate 40 steps (2 decay cycles)
        for (int i = 0; i < 40; i++) {
            carcassFungi.act(world);
        }

        assertEquals("Amount should decrease by 2 after 40 steps", 1, carcassFungi.amount);
    }
}
