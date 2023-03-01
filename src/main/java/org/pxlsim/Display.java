package org.pxlsim;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;

import java.math.BigDecimal;

public class Display {
    private final int WIDTH;
    private final int HEIGHT;
    private final BigDecimal REFRESH_RATE;
    private final StackPane STACK_PANE;
    private final Canvas CANVAS;
    private final GraphicsContext GRAPHICS;
    private final Scene SCENE;

    /**
     * @param refreshRate This needs to be a value between 1 and 0.02
     *                    these are the tested refresh rates that still work as intended.
     *                    The given value is validated so there is no chance to give a bad parameter.
     * @param width This value supposed to match the exact width of the board you are working on.
     * @param height This value supposed to match the exact height of the board you are working on.
     */
    public Display(int width, int height, double refreshRate) {
        this.WIDTH = width;
        this.HEIGHT = height;
        this.REFRESH_RATE = new BigDecimal(String.valueOf(refreshRate))
                .min(new BigDecimal("1"))
                .max(new BigDecimal("0.02"));
        this.STACK_PANE = new StackPane();
        this.CANVAS = new Canvas(width, height);
        this.GRAPHICS = this.CANVAS.getGraphicsContext2D();
        this.SCENE = new Scene(this.STACK_PANE, this.WIDTH, this.HEIGHT);
        this.STACK_PANE.getChildren().add(this.CANVAS);
    }

    public StackPane getStackPane() {
        return STACK_PANE;
    }

    public Canvas getCanvas() {
        return CANVAS;
    }

    public GraphicsContext getGraphics() {
        return GRAPHICS;
    }

    public Scene getScene() {
        return SCENE;
    }

    public BigDecimal getRefreshRate() {
        return REFRESH_RATE;
    }

    public int getWidth() {
        return WIDTH;
    }

    public int getHeight() {
        return HEIGHT;
    }
}
