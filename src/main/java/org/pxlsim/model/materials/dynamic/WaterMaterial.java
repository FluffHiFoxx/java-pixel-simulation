package org.pxlsim.model.materials.dynamic;

import javafx.scene.paint.Color;
import lombok.Getter;
import org.pxlsim.Display;
import org.pxlsim.model.MaterialType;

import java.util.Random;

@Getter
public class WaterMaterial extends DynamicMaterial {

    private int flowDirection;

    /**
     * @param display This parameter ensures that the correct values are extracted from the current working display.
     * @param positionX       This material's X coordinate.
     * @param positionY       This material's Y coordinate.
     */
    public WaterMaterial(Display display, int positionX, int positionY) {
        super(display, positionX, positionY, MaterialType.LIQUID, Color.DEEPSKYBLUE);
        this.flowDirection = new Random().nextBoolean() ? 1 : -1;
    }

    public void reverseFlow() {
        this.flowDirection = -flowDirection;
    }

}
