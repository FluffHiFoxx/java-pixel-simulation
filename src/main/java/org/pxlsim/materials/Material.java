package org.pxlsim.materials;

import javafx.scene.paint.Color;
import org.pxlsim.Display;

public abstract class Material {
    int xLimit, yLimit;
    int x, y;
    final Color COLOR;

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
}
