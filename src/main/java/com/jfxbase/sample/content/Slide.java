package com.jfxbase.sample.content;

import com.jfxbase.base.IFXNodeBuilder;
import com.jfxbase.impl.animation.AnimationFactory;
import javafx.animation.Animation;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.util.Collections;
import java.util.List;

public class Slide implements IFXNodeBuilder {

    private String text;

    public Slide(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public List<String> getCSSFiles() {
        return Collections.singletonList(getClass().getResource("css/slide.css").toExternalForm());
    }

    @Override
    public Animation animateEntrance(Node root) {
        return AnimationFactory.scale(root);
    }

    @Override
    public Node build() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Label slideContent = new Label(text);

        StackPane root = new StackPane(slideContent);
        root.setScaleX(0);
        root.setScaleY(0);
        return root;
    }
}