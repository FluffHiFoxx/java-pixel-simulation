package org.pxlsim.stage;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.TilePane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.pxlsim.Display;
import org.pxlsim.util.PropertiesUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * <h1>Screen size cheat sheet</h1>
 * <h2>Using aspect ratio of 16:9</h2>
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
public class MenuStage {

    private final int width = PropertiesUtil.getWidth();
    private final int height = PropertiesUtil.getHeight();
    private final int zoom = PropertiesUtil.getZoom();
    private final double screenWidth = Screen.getPrimary().getBounds().getWidth();
    private final double screenHeight = Screen.getPrimary().getBounds().getHeight();

    private final Stage stage;

    public MenuStage(Stage stage) {
        this.stage = stage;
    }

    public void openMenu() {
        stage.setResizable(false);
        stage.centerOnScreen();

        TilePane root = createWindow();
        Scene scene = new Scene(root, 256, 384);
        stage.setScene(scene);
        stage.setTitle("Pixel Simulator [Menu] - " + PropertiesUtil.getVersion());
        stage.show();
    }

    private TilePane createWindow() {
        TilePane window = new TilePane();
        window.setTileAlignment(Pos.CENTER);
        window.setAlignment(Pos.CENTER);
        window.setVgap(10);

        Button startButton = new Button("Start");
        startButton.setScaleX(2);
        startButton.setScaleY(2);

        Button defaultButton = new Button("Default");

        CheckBox enableFullscreen = new CheckBox("Fullscreen");

        TextField width = createNumberField(this.width, (int) this.screenWidth);

        TextField height = createNumberField(this.height, (int) this.screenHeight);

        TextField zoom = createNumberField(this.zoom, 0);
        zoom.setStyle("-fx-opacity: 1;");

        width.textProperty().addListener(limitZoom(zoom, width, height));
        height.textProperty().addListener(limitZoom(zoom, width, height));
        zoom.textProperty().addListener(limitZoom(zoom, width, height));

        window.getChildren().add(startButton);
        window.getChildren().add(new Label("Window Width"));
        window.getChildren().add(width);
        window.getChildren().add(new Label("Window Height"));
        window.getChildren().add(height);
        window.getChildren().add(new Label("Scale of the Pixels"));
        window.getChildren().add(zoom);
        window.getChildren().add(enableFullscreen);
        window.getChildren().add(defaultButton);

        startButton.setOnAction(handleStart(width, height, zoom));
        defaultButton.setOnAction(handleDefault(width, height, zoom));
        enableFullscreen.setOnAction(handleFullscreen(zoom, width, height));

        return window;

    }

    private EventHandler<ActionEvent> handleStart(TextField width, TextField height, TextField zoom) {
        return actionEvent -> {
            int displayWidth = Integer.parseInt(width.getText());
            int displayHeight = Integer.parseInt(height.getText());
            double displayZoom = Double.parseDouble(zoom.getText());
            Display display = new Display(displayWidth, displayHeight, 60, displayZoom);
            GameStage game = new GameStage(display);
            game.startGame(stage);
        };
    }

    private EventHandler<ActionEvent> handleDefault(TextField width, TextField height, TextField zoom) {
        return actionEvent -> {
            width.setText(String.valueOf(this.width));
            height.setText(String.valueOf(this.height));
            zoom.setText(String.valueOf(this.zoom));
        };
    }

    private EventHandler<ActionEvent> handleFullscreen(TextField zoom, TextField width, TextField height) {
        return actionEvent -> {
            zoom.setEditable(!zoom.isEditable());
            zoom.setMouseTransparent(!zoom.isMouseTransparent());
            zoom.setFocusTraversable(!zoom.isFocusTraversable());

            if (zoom.getStyle().equals("-fx-opacity: 1;")) {
                zoom.setStyle("-fx-opacity: 0.5;");
            } else {
                zoom.setStyle("-fx-opacity: 1;");
            }

            int widthZoom = BigDecimal.valueOf(screenWidth / Integer.parseInt(width.getText()))
                    .intValue();

            int heightZoom = BigDecimal.valueOf(screenHeight / Integer.parseInt(height.getText()))
                    .intValue();

            zoom.setText(String.valueOf(Math.max(widthZoom, heightZoom)));
        };
    }

    private TextField createNumberField(int initial, int maximum) {
        TextField field = new TextField(String.valueOf(initial));
        field.textProperty().addListener(getNumberListener(field, maximum));
        return field;
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
        return Math.min(screenWidth / width, screenHeight / height);
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
}
