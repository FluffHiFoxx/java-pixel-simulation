package org.pxlsim.materials;

import javafx.scene.paint.Color;
import org.pxlsim.Display;

import java.util.Random;

public class WaterMaterial extends DynamicMaterial {
    /**
     * @param display This parameter ensures that the correct values are extracted from the current working display.
     * @param x       This material's X coordinate.
     * @param y       This material's Y coordinate.
     */
    public WaterMaterial(Display display, int x, int y) {
        super(display, x, y, Color.DEEPSKYBLUE, display.getRefreshRate());
    }

    @Override
    public void move(Material[][] board) {
        int nextY = this.getY() + Math.min(this.getYLimit() - this.getY(), this.FALL_SPEED);
        int xModifier = new Random().nextBoolean() ? 1 : -1;
        int nextX = isInBoundsX(this.getX() + xModifier) ? this.getX() + xModifier
                : isInBoundsX(this.getX() - xModifier) ? this.getX() - xModifier
                : this.getX();
        board[this.getY()][this.getX()] = null;
        if (board[Math.max(this.getY() - 1, 0)][this.getX()] instanceof WaterMaterial waterMaterial) {
            Color colorAbove = waterMaterial.getColor();
            double r = colorAbove.getRed() - 0.1 >= 0 ? colorAbove.getRed() - 0.1 : 0;
            double g = colorAbove.getGreen() - 0.1 >= 0 ? colorAbove.getGreen() - 0.1 : 0;
            double b = colorAbove.getGreen() == 0 && colorAbove.getRed() == 0 && colorAbove.getBlue() > 0.2 ? colorAbove.getBlue() - 0.1 : colorAbove.getBlue();
            this.setColor(Color.color(r, g, b));
        } else if (!this.getColor().equals(Color.DEEPSKYBLUE)) {
            this.setColor(Color.DEEPSKYBLUE);
        }
        for (int i = this.getY(); i <= nextY; i++) {
            if (board[i][this.getX()] != null) {
                if (board[i][nextX] == null
                        && board[this.getY()][nextX] == null) {
                    setY(i);
                    setX(nextX);
                } else if (board[this.getY()][nextX] == null) {
                    setX(nextX);
                } else {
                    setY(i - 1);
                }
                board[this.getY()][this.getX()] = this;
                return;
            }
        }
        setY(nextY);
        if (board[this.getY()][nextX] == null && this.getY() == this.getYLimit()) {
            setX(nextX);
        }
        board[this.getY()][this.getX()] = this;
    }
}
