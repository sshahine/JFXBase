package com.jfxbase.impl.builders;

import com.jfxbase.base.IFXNodeBuilder;
import javafx.animation.Animation;
import javafx.scene.Node;

import java.util.List;

public class CachedFXNodeBuilder implements IFXNodeBuilder {

    private IFXNodeBuilder builder;
    private Node node;

    public CachedFXNodeBuilder(IFXNodeBuilder builder) {
        this.builder = builder;
    }

    public CachedFXNodeBuilder(IFXNodeBuilder builder, Node node) {
        this.builder = builder;
        this.node = node;
    }

    @Override
    public List<String> getCSSFiles() {
        return builder.getCSSFiles();
    }

    @Override
    public Animation animateEntrance(Node root) {
        return builder.animateEntrance(root);
    }

    @Override
    public Animation animateExit(Node root) {
        return builder.animateExit(root);
    }

    @Override
    public Node build() {
        if (node == null) node = builder.build();
        return node;
    }
}