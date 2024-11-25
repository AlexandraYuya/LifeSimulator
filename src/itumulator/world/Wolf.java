package itumulator.world;

import itumulator.simulator.Actor;

public class Wolf implements Actor {
    private int life;
    private int energy;
    private int stepCount;

    public Wolf(){
        this.life = 10;
        this.energy = 100;
        this.stepCount = 0;
    }

    @Override
    public void act(World world) {
        stepCount++;
        // check if a full night has passed with 0 energy
        if(stepCount % 20 == 0 && energy <= 0){
           life--;
           System.out.println("Wolf has lost life due to zero energy. Remaining lives: " + life);
        }

        hasDied(world);


    }
/**
 * This is a method that check if the wolf has 0 life and then is remove it
 * @param world The current world.*/
    public void hasDied(World world){
        if (life <= 0) {
            // Remove wolf from the world
            world.delete(this);
            System.out.println("A Wolf has died.");
        }
    }

    /**
     * This is the method place the Wolf in the world
     * @param world The current world.*/
    public void placeInWorld(World world) {
        int size = world.getSize();
        Location location = null;

        while (location == null || !world.isTileEmpty(location)) {
            int x = (int) (Math.random() * size);
            int y = (int) (Math.random() * size);
            location = new Location(x, y);
        }
        if (!world.containsNonBlocking(location)) {
            world.setTile(location, this);
        }
    }
}
