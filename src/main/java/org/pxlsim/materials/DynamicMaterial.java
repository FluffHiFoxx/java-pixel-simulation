package org.pxlsim.materials;

import javafx.scene.paint.Color;

import java.math.BigDecimal;

public abstract class DynamicMaterial extends Material {
    final BigDecimal REFRESH_RATE;
    final int FALL_SPEED;

    public DynamicMaterial(int xLimit, int yLimit, int x, int y, Color color, BigDecimal refreshRate) {
        super(xLimit, yLimit, x, y, color);
        this.REFRESH_RATE = refreshRate;
        this.FALL_SPEED = this.REFRESH_RATE.multiply(new BigDecimal(Math.round(Math.pow(9.81, 2)))).intValue();
    }

    public abstract void move();

    public abstract boolean canMove();
}
