package itumulator.world;

import com.sun.tools.javac.Main;
import itumulator.executable.Program;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MainTest {
    private World world;
    private Program program;

    @Before
    public void setUp() {
        program = new Program(10, 800, 800); // Create a small test world
        world = program.getWorld();
    }

    @Test
    public void testParseCount() {
        // Use reflection to access private method
        java.lang.reflect.Method method;
        try {
            method = Main.class.getDeclaredMethod("parseCount", String.class);
            method.setAccessible(true);

            // Test single number
            int result1 = (int) method.invoke(null, "5");
            assertEquals("Single number should parse correctly", 5, result1);

            // Test range
            int result2 = (int) method.invoke(null, "1-3");
            assertTrue("Range number should be within bounds", result2 >= 1 && result2 <= 3);

        } catch (Exception e) {
            fail("Test failed due to " + e.getMessage());
        }
    }

    @Test
    public void testFileProcessing() throws IOException {
        // Create a temporary test file
        File testFile = createTestFile();

        // Count initial entities
        int initialGrassCount = world.getEntities(Grass.class).size();
        int initialRabbitCount = world.getEntities(Rabbit.class).size();
        int initialBurrowCount = world.getEntities(Burrow.class).size();

        // Run main with test file
        try {
            Main.main(new String[]{testFile.getPath()});
        } catch (Exception e) {
            fail("Main execution failed: " + e.getMessage());
        }

        // Verify entities were added
        assertTrue("Grass should be added to world",
                world.getEntities(Grass.class).size() > initialGrassCount);
        assertTrue("Rabbits should be added to world",
                world.getEntities(Rabbit.class).size() > initialRabbitCount);
        assertTrue("Burrows should be added to world",
                world.getEntities(Burrow.class).size() > initialBurrowCount);

        // Clean up
        testFile.delete();
    }

    private File createTestFile() throws IOException {
        File tempFile = File.createTempFile("test", ".txt");
        FileWriter writer = new FileWriter(tempFile);
        writer.write("10\n");  // World size
        writer.write("grass 3\n");
        writer.write("rabbit 2\n");
        writer.write("burrow 1-2\n");
        writer.close();
        return tempFile;
    }

    @Test
    public void testWorldInitialization() {
        assertNotNull("Program should be initialized", program);
        assertNotNull("World should be created", world);
        assertEquals("World size should match", 10, world.getSize());
    }

    @Test
    public void testEntityPlacement() {
        // Test placing a grass
        Grass grass = new Grass();
        grass.placeInWorld(world);
        assertTrue("Grass should be placed in world",
                world.getEntities(Grass.class).contains(grass));

        // Test placing a rabbit
        Rabbit rabbit = new Rabbit();
        rabbit.placeInWorld(world);
        assertTrue("Rabbit should be placed in world",
                world.getEntities(Rabbit.class).contains(rabbit));

        // Test placing a burrow
        Burrow burrow = new Burrow();
        burrow.placeInWorld(world);
        assertTrue("Burrow should be placed in world",
                world.getEntities(Burrow.class).contains(burrow));
    }

    @Test
    public void testDisplayInformation() {
        DisplayInformation grassInfo = program.getDisplayInformation(Grass.class);
        DisplayInformation rabbitInfo = program.getDisplayInformation(Rabbit.class);
        DisplayInformation burrowInfo = program.getDisplayInformation(Burrow.class);

        assertNotNull("Grass should have display information", grassInfo);
        assertNotNull("Rabbit should have display information", rabbitInfo);
        assertNotNull("Burrow should have display information", burrowInfo);

        assertEquals("Grass should be green", Color.green, grassInfo.getColor());
        assertEquals("Grass should have correct sprite", "grass", grassInfo.getSpriteName());
    }
}