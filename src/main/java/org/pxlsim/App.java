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
    private final Display display = new Display(WIDTH, HEIGHT, 0.02);
    private final Set<Material> MATERIALS = new HashSet<>();
    private final Set<DynamicMaterial> DYNAMIC_MATERIALS = new HashSet<>();
    private final Material[][] BOARD = new Material[HEIGHT][WIDTH];
    final EventHandler<MouseEvent> CREATE = new EventHandler<>() {
        double mouseX;
        double mouseY;
        MouseButton button;
        final Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(display.getRefreshRate().doubleValue()), e -> {
            int x = (int) Math.round(mouseX);
            int y = (int) Math.round(mouseY);
            if (BOARD[y][x] == null) {
                if (button.equals(MouseButton.PRIMARY)) {
                    SandMaterial material = new SandMaterial(display, x, y);
                    DYNAMIC_MATERIALS.add(material);
                    MATERIALS.add(material);
                } else if (button.equals(MouseButton.SECONDARY)) {
                    Platform platform = new Platform(display, x, y, Color.GRAY);
                    MATERIALS.add(platform);
                    BOARD[platform.getY()][platform.getX()] = platform;
                }
            }
        }));

        @Override
        public void handle(MouseEvent mouseEvent) {
            mouseX = mouseEvent.getSceneX();
            mouseY = mouseEvent.getSceneY();
            button = mouseEvent.getButton();
            timeline.setCycleCount(Animation.INDEFINITE);
            if (mouseEvent.getEventType().equals(MouseEvent.MOUSE_DRAGGED)) {
                mouseX = mouseEvent.getSceneX();
                mouseY = mouseEvent.getSceneY();
            }
            if (mouseEvent.getEventType().equals(MouseEvent.MOUSE_PRESSED)) {
                timeline.playFromStart();
            } else if (mouseEvent.getEventType().equals(MouseEvent.MOUSE_RELEASED)) {
                timeline.pause();
            }
        }
    };

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        display.getScene().addEventFilter(MouseEvent.MOUSE_PRESSED, CREATE);
        display.getScene().addEventFilter(MouseEvent.MOUSE_RELEASED, CREATE);
        display.getScene().addEventFilter(MouseEvent.MOUSE_DRAGGED, CREATE);
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
