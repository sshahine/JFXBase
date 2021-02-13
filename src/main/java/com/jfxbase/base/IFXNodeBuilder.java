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
package com.jfxbase.base;

import com.jfxbase.impl.animation.AnimationFactory;
import com.jfxbase.impl.exceptions.BuildException;
import javafx.animation.Animation;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.util.Builder;

import java.util.List;

/**
 * @author sshahine
 * a builder to encapsulate a scene node
 */

public interface IFXNodeBuilder extends Builder<Node> {
    /**
     * @return a list of css files used to build the node
     */
    default List<String> getCSSFiles() {
        return null;
    }

    /**
     * this method is used to animate the node entrance after being added to the scene
     */
    default Animation animateEntrance(Node root) {
        return AnimationFactory.enterNode(root);
    }

    /**
     * this method is used to animate the node exit when switching content
     */
    default Animation animateExit(Node root) {
        return AnimationFactory.exitNode(root);
    }

    /**
     * This is the Default method used to build any {@link IFXNodeBuilder}
     * it contains default behavior to be applied on any IFXNodeBuilder instance
     *
     * @param builder
     * @return
     */
    static Node build(IFXNodeBuilder builder) throws BuildException {
        try {
            Node root = builder.build();
            if (root != null) {
                if (root instanceof Region) {
                    List<String> cssFiles = builder.getCSSFiles();
                    if (cssFiles != null) {
                        ((Region) root).getStylesheets().addAll(cssFiles);
                    }
                }
            }
            return root;
        } catch (Exception e) {
            throw new BuildException(e, builder);
        }

    }
}
