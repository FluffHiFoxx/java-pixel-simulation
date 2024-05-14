package org.pxlsim.model.materials.dynamic;

import javafx.scene.paint.Color;
import org.pxlsim.Display;
import org.pxlsim.model.MaterialType;

import java.util.Random;

public class SandMaterial extends DynamicMaterial {

    /**
     * @param display This parameter ensures that the correct values are extracted from the current working display.
     * @param positionX       This material's X coordinate.
     * @param positionY       This material's Y coordinate.
     */
    public SandMaterial(Display display, int positionX, int positionY) {
        super(
                display,
                positionX,
                positionY,
                MaterialType.SAND,
                Color.color(
                        Color.BEIGE.getRed(),
                        Color.BEIGE.getGreen() - new Random().nextDouble(0.2),
                        Color.BEIGE.getBlue() - new Random().nextDouble(0.5)));
    }

}
