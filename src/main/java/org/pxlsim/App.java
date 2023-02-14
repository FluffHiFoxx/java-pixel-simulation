package org.pxlsim;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class App extends Application {

    final int WIDTH = 640;
    final int HEIGHT = 360;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        StackPane root = new StackPane();
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext graphics = canvas.getGraphicsContext2D();
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        root.getChildren().add(canvas);
        graphics.setFill(Color.BLACK);
        graphics.fillRect(0, 0, WIDTH, HEIGHT);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.setScene(scene);
        stage.setTitle("Test");
        stage.show();
    }
}
