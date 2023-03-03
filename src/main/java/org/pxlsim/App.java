package org.pxlsim;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.pxlsim.materials.DynamicMaterial;
import org.pxlsim.materials.Material;
import org.pxlsim.materials.Platform;
import org.pxlsim.materials.SandMaterial;

import java.util.HashSet;
import java.util.Set;

public class App extends Application {
    private final int WIDTH = 640;
    private final int HEIGHT = 360;
    private Display display;
    private final Set<Material> MATERIALS = new HashSet<>();
    private final Set<DynamicMaterial> DYNAMIC_MATERIALS = new HashSet<>();
    private final Material[][] BOARD = new Material[HEIGHT][WIDTH];
    final EventHandler<MouseEvent> CREATE = new EventHandler<>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            int x = (int) Math.round(mouseEvent.getSceneX());
            int y = (int) Math.round(mouseEvent.getSceneY());
            if (BOARD[y][x] == null) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    SandMaterial e = new SandMaterial(display, x, y);
                    DYNAMIC_MATERIALS.add(e);
                    MATERIALS.add(e);
                } else if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
                    Platform e = new Platform(display, x, y, Color.AQUAMARINE);
                    MATERIALS.add(e);
                    BOARD[e.getY()][e.getX()] = e;
                }
            }
        }
    };

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.display = new Display(WIDTH, HEIGHT, 0.02);
        display.getScene().addEventFilter(MouseEvent.MOUSE_PRESSED, CREATE);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.setScene(display.getScene());
        stage.setTitle("Test");
        stage.show();
//        fillFields();
        render();
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(display.getRefreshRate().doubleValue()), e -> {
            handleContent();
            render();
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.playFromStart();
    }

    /**
     * Pre-fills the board and its fields with some materials for testing
     */
    private void fillFields() {
        DYNAMIC_MATERIALS.add(new SandMaterial(display, 320, 0));
        DYNAMIC_MATERIALS.add(new SandMaterial(display, 320, 20));
        DYNAMIC_MATERIALS.add(new SandMaterial(display, 320, 40));
        MATERIALS.addAll(DYNAMIC_MATERIALS);
        for (Material mat : MATERIALS) {
            BOARD[mat.getY()][mat.getX()] = mat;
        }
    }

    private void handleContent() {
        for (DynamicMaterial mat : DYNAMIC_MATERIALS) {
            mat.move(BOARD);
        }
    }

    private void render() {
        GraphicsContext graphics = display.getGraphics();
        graphics.clearRect(0, 0, display.getWidth(), display.getHeight());
        graphics.setFill(Color.BLACK);
        graphics.fillRect(0, 0, display.getWidth(), display.getHeight());
        for (Material mat : MATERIALS) {
            graphics.getPixelWriter().setColor(mat.getX(), mat.getY(), mat.getColor());
        }
    }
}
