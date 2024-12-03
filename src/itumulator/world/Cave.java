package itumulator.world;
import java.awt.*;
import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;

import java.awt.*;

public class Cave implements DynamicDisplayInformationProvider {

    public DisplayInformation getInformation() {
        return new DisplayInformation(Color.blue, "cave");
    }

}
