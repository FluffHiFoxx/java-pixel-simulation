package org.pxlsim;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.pxlsim.materials.DynamicMaterial;
import org.pxlsim.materials.Material;

import java.util.HashSet;
import java.util.Set;

public class App extends Application {
    private final int WIDTH = 640;
    private final int HEIGHT = 360;
    private final Display DISPLAY = new Display(WIDTH, HEIGHT, 60, 3);
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
        stage.setTitle("Test");
        stage.show();
        if (Screen.getPrimary().getOutputScaleX() == DISPLAY.getWindowWidth()
                && Screen.getPrimary().getOutputScaleY() == DISPLAY.getWindowHeight()) {
            stage.setFullScreen(true);
        }
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
        graphics.clearRect(0, 0, DISPLAY.getWidth(), DISPLAY.getHeight());
        graphics.setFill(Color.BLACK);
        graphics.fillRect(0, 0, DISPLAY.getWidth(), DISPLAY.getHeight());
        for (Material mat : MATERIALS) {
            graphics.getPixelWriter().setColor(mat.getX(), mat.getY(), mat.getColor());
        }
    }
}
