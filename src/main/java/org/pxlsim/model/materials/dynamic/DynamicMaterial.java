package org.pxlsim.model.materials.dynamic;

import javafx.scene.paint.Color;
import lombok.Getter;
import org.pxlsim.Display;
import org.pxlsim.model.materials.Material;
import org.pxlsim.model.MaterialType;

import java.math.BigDecimal;

@Getter
public abstract class DynamicMaterial extends Material {

    private final int fallSpeed;

    public DynamicMaterial(Display display, int positionX, int positionY, MaterialType type, Color color) {
        super(display, positionX, positionY, type, color);
        this.fallSpeed = display.getRefreshRate()
                .multiply(new BigDecimal(Math.round(Math.pow(9.81, 2))))
                .intValue();
    }
}
