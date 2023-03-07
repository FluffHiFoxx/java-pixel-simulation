package org.pxlsim;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.pxlsim.materials.DynamicMaterial;
import org.pxlsim.materials.Material;
import org.pxlsim.materials.Platform;
import org.pxlsim.materials.SandMaterial;

import java.util.Set;

public class Mouse {
    // TODO: find a way to have the required field somewhere else and still be useful
    private final Display DISPLAY;
    private final Set<DynamicMaterial> DYNAMIC_MATERIALS;
    private final Set<Material> MATERIALS;
    private final Material[][] BOARD;
    private double mouseX;
    private double mouseY;
    private MouseButton button;
    private final Timeline TIMELINE;

    public Mouse(Display display, Material[][] board, Set<Material> materials, Set<DynamicMaterial> dynamicMaterials) {
        this.DISPLAY = display;
        this.BOARD = board;
        this.MATERIALS = materials;
        this.DYNAMIC_MATERIALS = dynamicMaterials;
        this.TIMELINE = createTimeline();
        EventHandler<MouseEvent> CREATE = createEventHandler();
        this.DISPLAY.getScene().addEventFilter(MouseEvent.MOUSE_PRESSED, CREATE);
        this.DISPLAY.getScene().addEventFilter(MouseEvent.MOUSE_RELEASED, CREATE);
        this.DISPLAY.getScene().addEventFilter(MouseEvent.MOUSE_DRAGGED, CREATE);
    }

    private Timeline createTimeline() {
        return new Timeline(new KeyFrame(Duration.seconds(DISPLAY.getRefreshRate().doubleValue()), e -> {
            int x = (int) Math.round(mouseX);
            int y = (int) Math.round(mouseY);
            if (BOARD[y][x] == null) {
                if (button.equals(MouseButton.PRIMARY)) {
                    SandMaterial material = new SandMaterial(DISPLAY, x, y);
                    DYNAMIC_MATERIALS.add(material);
                    MATERIALS.add(material);
                } else if (button.equals(MouseButton.SECONDARY)) {
                    Platform platform = new Platform(DISPLAY, x, y, Color.GRAY);
                    MATERIALS.add(platform);
                    BOARD[platform.getY()][platform.getX()] = platform;
                }
            }
        }));
    }

    private EventHandler<MouseEvent> createEventHandler() {
        return mouseEvent -> {
            mouseX = mouseEvent.getSceneX();
            mouseY = mouseEvent.getSceneY();
            button = mouseEvent.getButton();
            TIMELINE.setCycleCount(Animation.INDEFINITE);
            if (mouseEvent.getEventType().equals(MouseEvent.MOUSE_DRAGGED)) {
                mouseX = mouseEvent.getSceneX();
                mouseY = mouseEvent.getSceneY();
            }
            if (mouseEvent.getEventType().equals(MouseEvent.MOUSE_PRESSED)) {
                TIMELINE.playFromStart();
            } else if (mouseEvent.getEventType().equals(MouseEvent.MOUSE_RELEASED)) {
                TIMELINE.pause();
            }
        };
    }
}
