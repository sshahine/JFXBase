package com.jfxbase.sample;

import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.utils.JFXUtilities;
import com.jfxbase.base.tasks.ITaskWrapper;
import com.jfxbase.sample.content.Home;
import com.jfxbase.services.FXSwitcher;
import com.jfxbase.services.FXTaskManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

        StackPane root = new StackPane();

        Scene scene = new Scene(root, 640, 480);


        // load jfoenix css files
        scene.getStylesheets().add(getClass().getClassLoader().getResource("com/jfoenix/assets/css/jfoenix-fonts.css").toExternalForm());
        scene.getStylesheets().add(getClass().getClassLoader().getResource("com/jfoenix/assets/css/jfoenix-design.css").toExternalForm());
        stage.setScene(scene);
        stage.show();

        JFXSpinner loader = new JFXSpinner(0);
        loader.setMaxSize(100, 100);
        FXTaskManager.getInstance().addTaskWrapper("Launch", new ITaskWrapper() {
            @Override
            public void started() {
                root.getChildren().setAll(loader);
            }

            @Override
            public void finished() {
                FXSwitcher.getInstance().switchContent(root, new Home());
            }
        });
        FXTaskManager.getInstance().submitTask(() -> {
            try {
                Thread.sleep(1000);
                JFXUtilities.runInFXAndWait(() -> loader.setProgress(.3));
                Thread.sleep(1000);
                JFXUtilities.runInFXAndWait(() -> loader.setProgress(.6));
                Thread.sleep(1000);
                JFXUtilities.runInFXAndWait(() -> loader.setProgress(1));
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "Launch");

    }

    public static void main(String[] args) {
        launch();
    }
}
