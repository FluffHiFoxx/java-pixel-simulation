package org.pxlsim.util;

import java.util.Objects;

public class Coordinates {
    final int X;
    final int Y;

    public Coordinates(int x, int y) {
        this.X = x;
        this.Y = y;
    }

    public int getX() {
        return X;
    }

    public int getY() {
        return Y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return X == that.X && Y == that.Y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(X, Y);
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "X=" + X +
                ", Y=" + Y +
                '}';
    }
}
