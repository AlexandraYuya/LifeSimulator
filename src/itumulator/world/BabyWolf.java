package itumulator.world;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;

import java.awt.*;

public class BabyWolf implements DynamicDisplayInformationProvider {
    @Override
    public DisplayInformation getInformation() {
            return new DisplayInformation(Color.GRAY, "wolf-small");
    }
}
