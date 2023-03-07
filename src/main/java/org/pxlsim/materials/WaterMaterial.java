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
        int nextY = this.y + Math.min(this.yLimit - this.y, this.FALL_SPEED);
        int xModifier = new Random().nextBoolean() ? 1 : -1;
        board[this.y][this.x] = null;
        for (int i = this.y; i <= nextY; i++) {
            if (board[i][this.x] != null) {
                if (board[i][this.x + xModifier] == null && board[this.y][this.x + xModifier] == null) {
                    this.y = i;
                    this.x = this.x + xModifier;
                } else if (board[i][this.x - xModifier] == null && board[this.y][this.x - xModifier] == null) {
                    this.y = i;
                    this.x = this.x - xModifier;
                } else if (board[this.y][this.x + xModifier] == null) {
                    this.x = this.x - xModifier;
                } else if (board[this.y][this.x - xModifier] == null) {
                    this.x = this.x - xModifier;
                } else {
                    this.y = i - 1;
                }
                board[this.y][this.x] = this;
                return;
            }
        }
        this.y = nextY;
        board[this.y][this.x] = this;
    }
}
