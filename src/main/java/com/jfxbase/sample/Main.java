/**
 * Copyright (c) 2021 Shadi Shaheen
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
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
