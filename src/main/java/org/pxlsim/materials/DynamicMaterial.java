package org.pxlsim.materials;

import javafx.scene.paint.Color;
import org.pxlsim.Display;

import java.math.BigDecimal;

public abstract class DynamicMaterial extends Material {
    final BigDecimal REFRESH_RATE;
    final int FALL_SPEED;

    public DynamicMaterial(Display display, int x, int y, Color color, BigDecimal refreshRate) {
        super(display, x, y, color);
        this.REFRESH_RATE = refreshRate;
        this.FALL_SPEED = this.REFRESH_RATE.multiply(new BigDecimal(Math.round(Math.pow(9.81, 2)))).intValue();
    }

    public abstract void handle(Material[][] board);
    public boolean isInBoundsX(int value) {
        return value >= 0 && value <= this.getXLimit();
    }
}
