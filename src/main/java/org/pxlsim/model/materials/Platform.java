package org.pxlsim.model.materials;

import javafx.scene.paint.Color;
import org.pxlsim.Display;
import org.pxlsim.model.MaterialType;

public class Platform extends Material {
    public Platform(Display display, int positionX, int positionY) {
        super(display, positionX, positionY, MaterialType.PLATFORM, Color.GRAY);
    }
}
