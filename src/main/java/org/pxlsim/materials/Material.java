package org.pxlsim.materials;

import javafx.scene.paint.Color;
import org.pxlsim.Display;

import java.util.UUID;

public abstract class Material {
    private final UUID id;
    private final int xLimit, yLimit;
    private int x, y;
    private final Color COLOR;

    public Material(Display display, int x, int y, Color color) {
        this.id = UUID.randomUUID();
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

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" + id + "}";
    }
}
