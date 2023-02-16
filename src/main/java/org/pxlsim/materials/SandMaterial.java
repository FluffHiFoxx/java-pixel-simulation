package org.pxlsim.materials;

import javafx.scene.paint.Color;

import java.util.Objects;

public class SandMaterial {
    int xLimit, yLimit;
    int x, y;
    Color color;

    public SandMaterial(int xLimit, int yLimit, int x, int y, Color color) {
        this.xLimit = xLimit - 1;
        this.yLimit = yLimit - 1;
        this.x = Math.min(x, this.xLimit);
        this.y = Math.min(y, this.yLimit);
        this.color = color;
    }

    public void move() {
        if(y < yLimit && y >= 0) y++;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SandMaterial that = (SandMaterial) o;
        return x == that.x && y == that.y && Objects.equals(color, that.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, color);
    }
}
