package org.pxlsim;


import javafx.application.Application;
import javafx.stage.Stage;
import org.pxlsim.stage.MenuStage;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        MenuStage menuStage = new MenuStage(stage);
        menuStage.openMenu();
    }
}
