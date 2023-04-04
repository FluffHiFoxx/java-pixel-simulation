package org.pxlsim.materials;

import javafx.scene.paint.Color;
import org.pxlsim.Display;

import java.util.Random;

public class GasMaterial extends DynamicMaterial {
    public GasMaterial(Display display, int x, int y) {
        super(display, x, y, Color.THISTLE, display.getRefreshRate());
    }

    @Override
    public void handle(Material[][] board) {
        move(board);
    }

    private void move(Material[][] board) {
        int xModifier = new Random().nextInt(-1,2);
        int nextX = this.getX();
        if (isInBoundsX(this.getX() + xModifier)) {
            nextX += xModifier;
        } else if (isInBoundsX(this.getX() - xModifier)) {
            nextX -= xModifier;
        }
        int nextY = this.getY() - Math.min(this.getY(), new Random().nextBoolean() ? this.FALL_SPEED : 0);
        board[this.getY()][this.getX()] = null;
        for (int i = this.getY(); i >= nextY; i--) {
            if (board[i][this.getX()] != null) {
                if (board[i][nextX] == null
                        && board[this.getY()][nextX] == null) {
                    setY(i);
                    setX(nextX);
                } else if (board[this.getY()][nextX] == null) {
                    setX(nextX);
                } else {
                    setY(i + 1);
                }
                board[this.getY()][this.getX()] = this;
                return;
            }
        }
        setY(nextY);
        if (board[this.getY()][nextX] == null) {
            setX(nextX);
        }
        board[this.getY()][this.getX()] = this;
    }
}
