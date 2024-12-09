package Gruppe01;
import java.awt.*;
import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;

public class Cave implements DynamicDisplayInformationProvider {

    /**
     * This method display a cave png. that we added to the images
     */
    public DisplayInformation getInformation() {
        return new DisplayInformation(Color.blue, "cave");
    }

}
