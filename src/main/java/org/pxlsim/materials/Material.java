package org.pxlsim.materials;

import javafx.scene.paint.Color;

public abstract class Material {
    int xLimit, yLimit;
    int x, y;
    final Color COLOR;

    public Material(int xLimit, int yLimit, int x, int y, Color color) {
        this.xLimit = xLimit - 1;
        this.yLimit = yLimit - 1;
        this.x = Math.min(x, this.xLimit);
        this.y = Math.min(y, this.yLimit);
        this.COLOR = color;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Color getColor() {
        return COLOR;
    }
}
