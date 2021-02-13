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