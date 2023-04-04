package org.pxlsim.materials;

import javafx.scene.paint.Color;
import org.pxlsim.Display;

import java.math.BigDecimal;
import java.util.*;

public class GasMaterial extends DynamicMaterial {
    public GasMaterial(Display display, int x, int y) {
        super(display, x, y,
                Color.hsb(Color.DARKKHAKI.getHue(),
                        Color.DARKKHAKI.getSaturation(),
                        BigDecimal.valueOf(Color.DARKKHAKI.getBrightness())
                                .subtract(BigDecimal.valueOf(0.5))
                                .doubleValue(),
                        Color.DARKKHAKI.getOpacity()),
                display.getRefreshRate());
    }

    @Override
    public void handle(Material[][] board) {
        move(board);
        changeColor(board);
    }

    private void move(Material[][] board) {
        int xModifier = new Random().nextInt(-1, 2);
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

    private void changeColor(Material[][] board) {
        List<BigDecimal> gasCount = new ArrayList<>();
        getBoardMaterial(board, this.getY(), this.getX() + 1).ifPresent(gasMaterial ->
                gasCount.add(BigDecimal.valueOf(gasMaterial.getColor().getBrightness())));
        getBoardMaterial(board, this.getY(), this.getX() - 1).ifPresent(gasMaterial ->
                gasCount.add(BigDecimal.valueOf(gasMaterial.getColor().getBrightness())));
        getBoardMaterial(board, this.getY() + 1, this.getX()).ifPresent(gasMaterial ->
                gasCount.add(BigDecimal.valueOf(gasMaterial.getColor().getBrightness())));
        getBoardMaterial(board, this.getY() - 1, this.getX()).ifPresent(gasMaterial ->
                gasCount.add(BigDecimal.valueOf(gasMaterial.getColor().getBrightness())));
        if (gasCount.size() >= 3) {
            BigDecimal brightness = gasCount.stream()
                    .min(BigDecimal::compareTo)
                    .get();
            setColor(Color.hsb(
                    Color.DARKKHAKI.getHue(),
                    Color.DARKKHAKI.getSaturation(),
                    Math.min(Color.DARKKHAKI.getBrightness(),brightness.add(BigDecimal.valueOf(0.05)).doubleValue()),
                    Color.DARKKHAKI.getOpacity()));
        } else {
            setColor(Color.hsb(Color.DARKKHAKI.getHue(),
                    Color.DARKKHAKI.getSaturation(),
                    BigDecimal.valueOf(Color.DARKKHAKI.getBrightness())
                            .subtract(BigDecimal.valueOf(0.3))
                            .doubleValue(),
                    Color.DARKKHAKI.getOpacity()));
        }
    }

    private Optional<GasMaterial> getBoardMaterial(Material[][] board, int y, int x) {
        if (isInBoundsX(x) && y >= 0 && y <= getYLimit()) {
            if (board[y][x] instanceof GasMaterial) {
                return Optional.of((GasMaterial) board[y][x]);
            }
        }
        return Optional.empty();
    }
}
