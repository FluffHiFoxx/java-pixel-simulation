package org.pxlsim;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.pxlsim.materials.SandMaterial;

import java.util.HashSet;
import java.util.Set;

public class App extends Application {
    final int WIDTH = 640;
    final int HEIGHT = 360;
    final Set<SandMaterial> materials = new HashSet<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        materials.add(new SandMaterial(WIDTH, HEIGHT, 320, 180, Color.BEIGE));
        StackPane root = new StackPane();
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext graphics = canvas.getGraphicsContext2D();
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        root.getChildren().add(canvas);
        render(graphics);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.setScene(scene);
        stage.setTitle("Test");
        stage.show();
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.05), e -> {
            handleContent();
            render(graphics);
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.playFromStart();
    }

    private void handleContent() {
        for (SandMaterial sand : materials) {
            sand.move();
        }
    }

    private void render(GraphicsContext graphics) {
        graphics.clearRect(0,0,WIDTH,HEIGHT);
        graphics.setFill(Color.BLACK);
        graphics.fillRect(0, 0, WIDTH, HEIGHT);
        for (SandMaterial sand : materials) {
            graphics.getPixelWriter().setColor(sand.getX(), sand.getY(), sand.getColor());
        }
    }
}
