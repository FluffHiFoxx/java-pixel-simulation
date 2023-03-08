package org.pxlsim.materials;

import javafx.scene.paint.Color;
import org.pxlsim.Display;

public abstract class Material {
    private final int xLimit, yLimit;
    private int x, y;
    private final Color COLOR;

    public Material(Display display, int x, int y, Color color) {
        this.xLimit = display.getWidth() - 1;
        this.yLimit = display.getHeight() - 1;
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

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getXLimit() {
        return xLimit;
    }

    public int getYLimit() {
        return yLimit;
    }
}
