package com.jfxbase.impl.builders;

import com.jfxbase.base.IFXNodeBuilder;
import javafx.animation.Animation;
import javafx.scene.Node;
import javafx.scene.control.Label;

import java.util.List;

public class EmptyBuilder implements IFXNodeBuilder {

    private static volatile EmptyBuilder instance;

    private EmptyBuilder() {
        // Protect against instantiation via reflection
        if (instance == null) {
            instance = this;
        } else {
            throw new IllegalStateException("Already initialized.");
        }
    }

    public static synchronized EmptyBuilder getInstance() {
        if (instance == null) {
            synchronized (EmptyBuilder.class) {
                if (instance == null) {
                    instance = new EmptyBuilder();
                }
            }
        }
        return instance;
    }

    @Override
    public List<String> getCSSFiles() {
        return null;
    }

    @Override
    public Animation animateEntrance(Node root) {
        return null;
    }

    @Override
    public Animation animateExit(Node root) {
        return null;
    }

    @Override
    public Node build() {
        return new Label("Coming soon....");
    }

}
