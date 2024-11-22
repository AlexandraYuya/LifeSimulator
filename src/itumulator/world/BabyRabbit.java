package itumulator.world;
import itumulator.executable.Program;
import itumulator.simulator.Actor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;


public class BabyRabbit implements Actor{
    //should maybe BabyRabbit extend from Rabbit??
    // maybe babyrabbit and adultrabbit can both extends from rabbit?

    private int life;
    private int energy;
    private int stepCount;

    public BabyRabbit() {
        this.life = 15; // babies start with 15 lives??
        this.energy = 100;
        this.stepCount = 0;
    }

    @Override
    public void act(World world) {
        stepCount++;
        // only execute for every 20 steps 1 day
        if (stepCount == 20) {
            stepCount = 0;
            life--;
        }
        energy--;
        Location curLocation = world.getLocation(this);
        Set<Location> neighbours = world.getEmptySurroundingTiles(curLocation);
        if (!neighbours.isEmpty()) {
            Random rand = new Random();
            List<Location> list = new ArrayList<>(neighbours);
            Location newLocation = list.get(rand.nextInt(list.size()));
            world.move(this, newLocation);
            world.setCurrentLocation(newLocation);
        }
        eat(world);
        System.out.println("energy" + energy);
        System.out.println("Life" + life);

        if (life == 0 || energy == 0) {
            world.delete(this);
            return;
        }

        if (this.life == 10){
            grow(world);
        }
    }

    private void eat(World world) {

        Location curLocation = world.getLocation(this);
        Object hasGrass = world.getNonBlocking(curLocation);
        if(hasGrass instanceof Grass) {
            if(energy <= 95) {
                energy += 5;
            }
            world.delete(hasGrass);
        }
    }

    public void grow(World world) {
        Location currentLocation = world.getLocation(this);
        // Create new adult rabbit
        Rabbit adultRabbit = new Rabbit();
        // Delete baby rabbit (this will remove it from both world and tile)
        world.delete(this);
        // Add adult rabbit to world
        world.add(adultRabbit);
        // Place adult rabbit at the location
        world.setTile(currentLocation, adultRabbit);
    }
}
