package org.pxlsim.materials;

import javafx.scene.paint.Color;

import java.math.BigDecimal;

public class SandMaterial extends DynamicMaterial {
    public SandMaterial(int xLimit, int yLimit, int x, int y, Color color, BigDecimal refreshRate) {
        super(xLimit, yLimit, x, y, color, refreshRate);
    }

    @Override
    public void move() {
        if (canMove()) {
            this.y += Math.min(this.yLimit - this.y, this.FALL_SPEED);
        }
    }

    @Override
    public boolean canMove() {
        return this.y < this.yLimit && this.y >= 0;
    }
}
