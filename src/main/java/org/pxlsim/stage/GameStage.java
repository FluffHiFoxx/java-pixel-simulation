package org.pxlsim.stage;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.pxlsim.Display;
import org.pxlsim.MaterialCreator;
import org.pxlsim.util.PropertiesUtil;
import org.pxlsim.handler.MaterialHandlerManager;
import org.pxlsim.model.materials.Material;
import org.pxlsim.model.materials.dynamic.DynamicMaterial;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class GameStage {

    private final Set<Material> materials = new HashSet<>();
    private final Set<DynamicMaterial> dynamicMaterials = new HashSet<>();
    private final MaterialHandlerManager materialHandlerManager = new MaterialHandlerManager();
    private final Display display;
    private final Material[][] board;

    public GameStage(Display display) {
        this.display = display;
        this.board = new Material[display.getHeight()][display.getWidth()];
    }

    public void startGame(Stage stage) {
        new MaterialCreator(display, board, materials, dynamicMaterials);
        stage.hide();
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.setScene(display.getScene());
        stage.sizeToScene();
        stage.setTitle("Pixel Simulator [Game] - " + PropertiesUtil.getVersion());
        stage.show();

        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        if (screenWidth == display.getWindowWidth() || screenHeight == display.getWindowHeight()) {
            stage.setFullScreen(true);
            // TODO: stop app when exiting fullscreen
        }
        render();
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(display.getRefreshRate().doubleValue()), e -> {
            if (!dynamicMaterials.isEmpty()) {
                handleContent();
            }
            if (stage.isFocused()) {
                render();
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.playFromStart();
    }

    private void handleContent() {
        for (DynamicMaterial material : dynamicMaterials) {
            materialHandlerManager.handle(board, material);
        }
    }

    private void render() {
        GraphicsContext graphics = display.getGraphics();
        graphics.clearRect(0, 0, display.getWindowWidth(), display.getWindowHeight());
        graphics.setFill(Color.BLACK);
        graphics.fillRect(0, 0, display.getWindowWidth(), display.getWindowHeight());
        for (Material material : materials) {
            double zoom = display.getZoom().doubleValue();
            double x = display.getZoom().multiply(new BigDecimal(material.getPosition().getX())).doubleValue();
            double y = display.getZoom().multiply(new BigDecimal(material.getPosition().getY())).doubleValue();
            graphics.setFill(material.getColor());
            graphics.fillRect(x, y, zoom, zoom);
        }
    }
}
