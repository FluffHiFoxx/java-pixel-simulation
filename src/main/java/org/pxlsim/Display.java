package org.pxlsim;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class Display {

    private final int width;
    private final int height;
    private final BigDecimal refreshRate;
    private final StackPane stackPane;
    private final Canvas canvas;
    private final GraphicsContext graphics;
    private final Scene scene;
    private final BigDecimal zoom;
    private final double windowWidth;
    private final double windowHeight;

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
        this.width = width;
        this.height = height;
        this.zoom = BigDecimal.valueOf(Math.abs(zoom));
        this.windowWidth = getZoom().multiply(new BigDecimal(getWidth())).doubleValue();
        this.windowHeight = getZoom().multiply(new BigDecimal(getHeight())).doubleValue();
        this.refreshRate = new BigDecimal(String.valueOf((double) 1 / refreshRate))
                .min(new BigDecimal("1"))
                .max(new BigDecimal("0.016"));
        this.stackPane = new StackPane();
        this.canvas = new Canvas(this.windowWidth, this.windowHeight);
        this.graphics = this.canvas.getGraphicsContext2D();
        this.scene = new Scene(this.stackPane, this.windowWidth, this.windowHeight);
        graphics.setImageSmoothing(false);
        stackPane.getChildren().add(this.canvas);
    }
}
