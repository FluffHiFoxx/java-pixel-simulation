package org.pxlsim;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.pxlsim.materials.DynamicMaterial;
import org.pxlsim.materials.Material;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    private final String VERSION = "V.0.421"; // <- V.[Major Release].[Major Addition][Change in the current Addition][Fixes]
    private final int WIDTH = 128;
    private final int HEIGHT = 72;
    private final int ZOOM = 12;
    private Display display;
    private final Set<Material> MATERIALS = new HashSet<>();
    private final Set<DynamicMaterial> DYNAMIC_MATERIALS = new HashSet<>();
    private Material[][] board;
    private final double SCREEN_WIDTH = Screen.getPrimary().getBounds().getWidth();
    private final double SCREEN_HEIGHT = Screen.getPrimary().getBounds().getHeight();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        menu(stage);
    }

    //TODO: refactor this whole menu and Display thing because its horrible
    private void menu(Stage stage) {
        stage.setResizable(false);
        stage.centerOnScreen();
        TilePane root = new TilePane();
        root.setTileAlignment(Pos.CENTER);
        root.setAlignment(Pos.CENTER);
        root.setVgap(10);
        Button startButton = new Button("Start");
        startButton.setScaleX(2);
        startButton.setScaleY(2);
        Button defaultButton = new Button("Default");
        CheckBox enableFullscreen = new CheckBox("Fullscreen");
        TextField width = new TextField(String.valueOf(this.WIDTH));
        width.textProperty().addListener(getNumberListener(width, (int) SCREEN_WIDTH));
        TextField height = new TextField(String.valueOf(this.HEIGHT));
        height.textProperty().addListener(getNumberListener(height, (int) SCREEN_HEIGHT));
        TextField zoom = new TextField(String.valueOf(this.ZOOM));
        zoom.textProperty().addListener(getNumberListener(zoom, 0));
        zoom.textProperty().addListener(limitZoom(zoom, width, height));
        width.textProperty().addListener(limitZoom(zoom, width, height));
        height.textProperty().addListener(limitZoom(zoom, width, height));
        zoom.setStyle("-fx-opacity: 1;");
        root.getChildren().add(startButton);
        root.getChildren().add(new Label("Window Width"));
        root.getChildren().add(width);
        root.getChildren().add(new Label("Window Height"));
        root.getChildren().add(height);
        root.getChildren().add(new Label("Scale of the Pixels"));
        root.getChildren().add(zoom);
        root.getChildren().add(enableFullscreen);
        root.getChildren().add(defaultButton);
        startButton.setOnAction(actionEvent -> {
            stage.hide();
            int displayWidth = Integer.parseInt(width.getText());
            int displayHeight = Integer.parseInt(height.getText());
            double displayZoom = Double.parseDouble(zoom.getText());
            this.board = new Material[displayHeight][displayWidth];
            startGame(stage, new Display(displayWidth, displayHeight, 60, displayZoom));
        });
        defaultButton.setOnAction(actionEvent -> {
            width.setText(String.valueOf(this.WIDTH));
            height.setText(String.valueOf(this.HEIGHT));
            zoom.setText(String.valueOf(this.ZOOM));
        });
        enableFullscreen.setOnAction(actionEvent -> {
            zoom.setEditable(!zoom.isEditable());
            zoom.setMouseTransparent(!zoom.isMouseTransparent());
            zoom.setFocusTraversable(!zoom.isFocusTraversable());
            if (zoom.getStyle().equals("-fx-opacity: 1;")) {
                zoom.setStyle("-fx-opacity: 0.5;");
            } else {
                zoom.setStyle("-fx-opacity: 1;");
            }
            int widthZoom = BigDecimal.valueOf(SCREEN_WIDTH / Integer.parseInt(width.getText()))
                    .intValue();
            // use double with: .setScale(1, RoundingMode.HALF_EVEN).doubleValue();
            int heightZoom = BigDecimal.valueOf(SCREEN_HEIGHT / Integer.parseInt(height.getText()))
                    .intValue();
            // use double with: .setScale(1, RoundingMode.HALF_EVEN).doubleValue();
            zoom.setText(String.valueOf(Math.max(widthZoom, heightZoom)));
        });
        Scene scene = new Scene(root, 256, 384);
        stage.setScene(scene);
        stage.setTitle("Menu - " + VERSION);
        stage.show();
    }

    private ChangeListener<String> limitZoom(TextField zoom, TextField width, TextField height) {
        return (observable, oldValue, newValue) -> {
            double max = BigDecimal
                    .valueOf(getMaxZoom(Double.parseDouble(width.getText()), Double.parseDouble(height.getText())))
                    .setScale(1, RoundingMode.HALF_EVEN)
                    .doubleValue();
            if (Double.parseDouble(zoom.getText()) > max) {
                zoom.setText(String.valueOf(max));
            }
            if (!zoom.isEditable()) {
                zoom.setText(String.valueOf(max));
            }
        };
    }

    private double getMaxZoom(double width, double height) {
        return Math.min(SCREEN_WIDTH / width, SCREEN_HEIGHT / height);
    }

    private ChangeListener<String> getNumberListener(TextField textField, double limit) {
        return (observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                newValue = String.valueOf(1);
            }
            if (!newValue.matches("^[0-9]*\\.?[0-9]+$")) {
                if (newValue.contains(".")) {
                    int dotIndex = newValue.indexOf('.');
                    String newValue1 = newValue.substring(0, dotIndex).replaceAll("\\D", "");
                    String newValue2 = newValue.substring(dotIndex + 1).replaceAll("\\D", "");
                    newValue = newValue1 + "." + newValue2;
                } else {
                    newValue = newValue.replaceAll("\\D", "");
                }
            }
            if (Double.parseDouble(newValue) >= limit && limit > 0) {
                newValue = String.valueOf(limit);
            }
            textField.setText(newValue);
        };
    }

    private void startGame(Stage stage, Display gameDisplay) {
        this.display = gameDisplay;
        new MaterialCreator(display, board, MATERIALS, DYNAMIC_MATERIALS);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.setScene(display.getScene());
        stage.setTitle("Pixel Simulator - " + VERSION);
        stage.show();
        if (SCREEN_WIDTH == display.getWindowWidth() || SCREEN_HEIGHT == display.getWindowHeight()) {
            stage.setFullScreen(true);
            // TODO: stop app when exiting fullscreen
        }
        render();
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(display.getRefreshRate().doubleValue()), e -> {
            if (!DYNAMIC_MATERIALS.isEmpty()) {
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
        for (DynamicMaterial mat : DYNAMIC_MATERIALS) {
            mat.handle(board);
        }
    }

    private void render() {
        GraphicsContext graphics = display.getGraphics();
//        graphics.clearRect(0, 0, DISPLAY.getWidth(), DISPLAY.getHeight());                     [Old-rendering method]
        graphics.clearRect(0, 0, display.getWindowWidth(), display.getWindowHeight());
        graphics.setFill(Color.BLACK);
//        graphics.fillRect(0, 0, DISPLAY.getWidth(), DISPLAY.getHeight());                      [Old-rendering method]
        graphics.fillRect(0, 0, display.getWindowWidth(), display.getWindowHeight());
        for (Material mat : MATERIALS) {
            double zoom = display.getZoom().doubleValue();
            double x = display.getZoom().multiply(new BigDecimal(mat.getX())).doubleValue();
            double y = display.getZoom().multiply(new BigDecimal(mat.getY())).doubleValue();
            graphics.setFill(mat.getColor());
            graphics.fillRect(x, y, zoom, zoom);
//            graphics.getPixelWriter().setColor(mat.getX(), mat.getY(), mat.getColor());        [Old-rendering method]
        }
    }
}
