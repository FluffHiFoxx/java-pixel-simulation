package org.pxlsim;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.pxlsim.materials.*;

import java.util.Set;

public class MaterialCreator {
    // TODO: find a way to have the required field somewhere else and still be useful
    private final Display DISPLAY;
    private final Set<DynamicMaterial> DYNAMIC_MATERIALS;
    private final Set<Material> MATERIALS;
    private final Material[][] BOARD;
    private double mouseX;
    private double mouseY;
    private MouseButton button;
    private int materialIndex;
    private final Timeline TIMELINE;

    public MaterialCreator(Display display, Material[][] board, Set<Material> materials, Set<DynamicMaterial> dynamicMaterials) {
        this.DISPLAY = display;
        this.BOARD = board;
        this.MATERIALS = materials;
        this.DYNAMIC_MATERIALS = dynamicMaterials;
        this.TIMELINE = createTimeline();
        this.materialIndex = 1;
        EventHandler<MouseEvent> CREATE = createMouseEventHandler();
        EventHandler<KeyEvent> CHANGE = createKeyEventHandler();
        this.DISPLAY.getScene().addEventFilter(MouseEvent.MOUSE_PRESSED, CREATE);
        this.DISPLAY.getScene().addEventFilter(MouseEvent.MOUSE_RELEASED, CREATE);
        this.DISPLAY.getScene().addEventFilter(MouseEvent.MOUSE_DRAGGED, CREATE);
        this.DISPLAY.getScene().addEventFilter(KeyEvent.KEY_TYPED, CHANGE);
        this.DISPLAY.getScene().addEventFilter(KeyEvent.KEY_TYPED, CHANGE);
    }

    private Timeline createTimeline() {
        return new Timeline(new KeyFrame(Duration.seconds(DISPLAY.getRefreshRate().doubleValue()), e -> {
            int y = (int) Math.round((mouseY / DISPLAY.getZoom().doubleValue()));
            int x = (int) Math.round((mouseX / DISPLAY.getZoom().doubleValue()));
            Material cell;
            try {
                cell = BOARD[y][x];
            } catch (ArrayIndexOutOfBoundsException exception) {
                if (y >= BOARD.length) {
                    y = BOARD.length - 1;
                } else if (y < 0) {
                    y = 0;
                }
                if (x >= BOARD[y].length) {
                    x = BOARD[y].length - 1;
                } else if (x < 0) {
                    x = 0;
                }
                cell = BOARD[y][x];
            }
            if (cell == null) {
                if (button.equals(MouseButton.PRIMARY)) {
                    Material material = switch (materialIndex) {
                        case 0 -> {
                            Platform platform = new Platform(DISPLAY, x, y, Color.GRAY);
                            BOARD[platform.getY()][platform.getX()] = platform;
                            yield platform;
                        }
                        case 1 -> new SandMaterial(DISPLAY, x, y);
                        case 2 -> new WaterMaterial(DISPLAY, x, y);
                        default -> throw new IllegalStateException("Unexpected value: " + materialIndex);
                    };
                    if (material instanceof DynamicMaterial) {
                        DYNAMIC_MATERIALS.add((DynamicMaterial) material);
                    }
                    MATERIALS.add(material);
                }
            } else if (button.equals(MouseButton.SECONDARY)) {
                MATERIALS.remove(cell);
                if (cell instanceof DynamicMaterial) {
                    DYNAMIC_MATERIALS.remove(cell);
                }
                BOARD[y][x] = null;
            }
        }));
    }

    private EventHandler<MouseEvent> createMouseEventHandler() {
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

    private EventHandler<KeyEvent> createKeyEventHandler() {
        return keyEvent -> {
            switch (keyEvent.getCharacter()) {
                case "1" -> materialIndex = 0;
                case "2" -> materialIndex = 1;
                case "3" -> materialIndex = 2;
            }
        };
    }
}
