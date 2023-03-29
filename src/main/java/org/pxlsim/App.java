package org.pxlsim;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.pxlsim.materials.DynamicMaterial;
import org.pxlsim.materials.Material;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * <h1>Screen size cheat sheet</h1>
 * <h2>16:9</h2>
 * <table>
 *     <thead>
 *          <tr><th>WIDTH</th><th>HEIGHT</th><th>Zoom to 1920x1080</th><th>Recommended Zoom</th><th>Problems</th></tr>
 *     </thead>
 *     <tbody>
 *          <tr><td>128</td><td>72</td><td>15</td><td>12</td><td>-</td></tr>
 *          <tr><td>256</td><td>144</td><td>7.5</td><td>6</td><td>-</td></tr>
 *          <tr><td>384</td><td>216</td><td>5</td><td>4</td><td>-</td></tr>
 *          <tr><td>512</td><td>288</td><td>3.75</td><td>3</td><td>-</td></tr>
 *          <tr><td>640</td><td>360</td><td>3</td><td>2.5</td><td>Wierd grid-like rendering issue</td></tr>
 *          <tr><td>768</td><td>432</td><td>2.5</td><td>2</td><td>-</td></tr>
 *     </tbody>
 * </table>
 */
public class App extends Application {
    private final int WIDTH = 256;
    private final int HEIGHT = 144;
    private final Display DISPLAY = new Display(WIDTH, HEIGHT, 60, 6);
    private final Set<Material> MATERIALS = new HashSet<>();
    private final Set<DynamicMaterial> DYNAMIC_MATERIALS = new HashSet<>();
    private final Material[][] BOARD = new Material[HEIGHT][WIDTH];

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        new MaterialCreator(DISPLAY, BOARD, MATERIALS, DYNAMIC_MATERIALS);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.setScene(DISPLAY.getScene());
        stage.setTitle("Pixel Simulator - V.0.16");
        stage.show();
/*
TODO: fix not getting dedicated-fullscreen with this JavaFX code

        if (Screen.getPrimary().getOutputScaleX() == DISPLAY.getWindowWidth()
                && Screen.getPrimary().getOutputScaleY() == DISPLAY.getWindowHeight()) {
            stage.setFullScreen(true);
        }
 */
        render();
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(DISPLAY.getRefreshRate().doubleValue()), e -> {
            if (!DYNAMIC_MATERIALS.isEmpty()) {
                handleContent();
            }
            render();
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.playFromStart();
    }

    private void handleContent() {
        for (DynamicMaterial mat : DYNAMIC_MATERIALS) {
            mat.move(BOARD);
        }
    }

    private void render() {
        GraphicsContext graphics = DISPLAY.getGraphics();
//        graphics.clearRect(0, 0, DISPLAY.getWidth(), DISPLAY.getHeight());                     [Old-rendering method]
        graphics.clearRect(0, 0, DISPLAY.getWindowWidth(), DISPLAY.getWindowHeight());
        graphics.setFill(Color.BLACK);
//        graphics.fillRect(0, 0, DISPLAY.getWidth(), DISPLAY.getHeight());                      [Old-rendering method]
        graphics.fillRect(0, 0, DISPLAY.getWindowWidth(), DISPLAY.getWindowHeight());
        for (Material mat : MATERIALS) {
            double zoom = DISPLAY.getZoom().doubleValue();
            double x = DISPLAY.getZoom().multiply(new BigDecimal(mat.getX())).doubleValue();
            double y = DISPLAY.getZoom().multiply(new BigDecimal(mat.getY())).doubleValue();
            graphics.setFill(mat.getColor());
            graphics.fillRect(x, y, zoom, zoom);
//            graphics.getPixelWriter().setColor(mat.getX(), mat.getY(), mat.getColor());        [Old-rendering method]
        }
    }
}
