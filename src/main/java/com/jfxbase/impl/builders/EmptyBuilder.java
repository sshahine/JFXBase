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
