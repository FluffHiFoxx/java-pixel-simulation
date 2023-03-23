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
    private final BigDecimal ZOOM;
    private final int WINDOW_WIDTH;
    private final int WINDOW_HEIGHT;

    /**
     * @param zoom        If the pixel size is small this will scale everything accordingly.
     *                    Recommended to scale using whole numbers or halves.
     * @param refreshRate This needs to be a value between 1 and 60 frames per second
     *                    these are the tested refresh rates that still work as intended.
     *                    The given value is validated so there is no chance to give a bad parameter.
     * @param width       This value supposed to match the exact width of the board you are working on.
     * @param height      This value supposed to match the exact height of the board you are working on.
     */
    public Display(int width, int height, int refreshRate, double zoom) {
        this.WIDTH = width;
        this.HEIGHT = height;
        this.ZOOM = BigDecimal.valueOf(Math.abs(zoom));
        this.WINDOW_WIDTH = getZoom().multiply(new BigDecimal(getWidth())).intValue();
        this.WINDOW_HEIGHT = getZoom().multiply(new BigDecimal(getHeight())).intValue();
        this.REFRESH_RATE = new BigDecimal(String.valueOf((double) 1 / refreshRate))
                .min(new BigDecimal("1"))
                .max(new BigDecimal("0.016"));
        this.STACK_PANE = new StackPane();
        this.CANVAS = new Canvas(this.WIDTH, this.HEIGHT);
        this.GRAPHICS = this.CANVAS.getGraphicsContext2D();
        this.SCENE = new Scene(this.STACK_PANE, this.WINDOW_WIDTH, this.WINDOW_HEIGHT);
        GRAPHICS.setImageSmoothing(false);
        CANVAS.setScaleY(ZOOM.doubleValue());
        CANVAS.setScaleX(ZOOM.doubleValue());
        STACK_PANE.getChildren().add(this.CANVAS);
    }

    /**
     * @param refreshRate This needs to be a value between 1 and 60 frames per second
     *                    these are the tested refresh rates that still work as intended.
     *                    The given value is validated so there is no chance to give a bad parameter.
     * @param width       This value supposed to match the exact width of the board you are working on.
     * @param height      This value supposed to match the exact height of the board you are working on.
     */
    public Display(int width, int height, int refreshRate) {
        this.WIDTH = width;
        this.HEIGHT = height;
        this.ZOOM = new BigDecimal(1);
        this.WINDOW_WIDTH = getZoom().multiply(new BigDecimal(getWidth())).intValue();
        this.WINDOW_HEIGHT = getZoom().multiply(new BigDecimal(getHeight())).intValue();
        this.REFRESH_RATE = new BigDecimal(String.valueOf(1 / refreshRate))
                .min(new BigDecimal("1"))
                .max(new BigDecimal("0.016"));
        this.STACK_PANE = new StackPane();
        this.CANVAS = new Canvas(this.WIDTH, this.HEIGHT);
        this.GRAPHICS = this.CANVAS.getGraphicsContext2D();
        this.SCENE = new Scene(this.STACK_PANE,
                this.WIDTH * ZOOM.doubleValue(),
                this.HEIGHT * ZOOM.doubleValue());
        GRAPHICS.setImageSmoothing(false);
        CANVAS.setScaleY(ZOOM.doubleValue());
        CANVAS.setScaleX(ZOOM.doubleValue());
        STACK_PANE.getChildren().add(this.CANVAS);
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

    public BigDecimal getZoom() {
        return ZOOM;
    }

    public int getWindowWidth() {
        return WINDOW_WIDTH;
    }

    public int getWindowHeight() {
        return WINDOW_HEIGHT;
    }
}
