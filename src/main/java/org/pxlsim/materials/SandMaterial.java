package org.pxlsim.materials;

import javafx.scene.paint.Color;

import java.math.BigDecimal;
import java.util.Random;

public class SandMaterial extends DynamicMaterial {
    public SandMaterial(int xLimit, int yLimit, int x, int y, BigDecimal refreshRate) {
        super(xLimit, yLimit, x, y, Color.BEIGE, refreshRate);
    }

    @Override
    public void move(Material[][] board) {
        int nextY = this.y + Math.min(this.yLimit - this.y, this.FALL_SPEED);
        int xModifier = new Random().nextBoolean() ? 1 : -1;
        board[this.y][this.x] = null;
        for (int i = this.y; i <= nextY; i++) {
            if (board[i][this.x] != null) {
                if (board[i][this.x + xModifier] == null) {
                    this.y = i;
                    this.x = this.x + xModifier;
                } else if (board[i][this.x - xModifier] == null) {
                    this.y = i;
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
