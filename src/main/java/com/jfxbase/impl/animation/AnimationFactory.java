package com.jfxbase.impl.animation;

import com.jfoenix.svg.SVGGlyph;
import com.jfoenix.transitions.JFXAnimationTimer;
import com.jfoenix.transitions.JFXKeyFrame;
import com.jfoenix.transitions.JFXKeyValue;
import javafx.animation.*;
import javafx.scene.Node;
import javafx.scene.control.TitledPane;
import javafx.util.Duration;


public class AnimationFactory {

    public static final Animation scale(Node node) {
        return new Timeline(
                new KeyFrame(Duration.millis(0),
                        event -> {
                            node.setScaleX(0);
                            node.setScaleY(0);
                        },
                        new KeyValue(node.scaleXProperty(), 0, Interpolator.EASE_BOTH),
                        new KeyValue(node.scaleYProperty(), 0, Interpolator.EASE_BOTH)),
                new KeyFrame(Duration.millis(160),
                        event -> {
                            node.setScaleX(1);
                            node.setScaleY(1);
                        },
                        new KeyValue(node.scaleXProperty(), 1, Interpolator.EASE_BOTH),
                        new KeyValue(node.scaleYProperty(), 1, Interpolator.EASE_BOTH)));
    }

    public static final Animation opacity(Node node, double start, double end) {
        return new Timeline(
                new KeyFrame(Duration.millis(0),
                        event -> node.setOpacity(start),
                        new KeyValue(node.opacityProperty(), start, Interpolator.EASE_BOTH)),
                new KeyFrame(Duration.millis(160),
                        event -> node.setOpacity(end),
                        new KeyValue(node.opacityProperty(), end, Interpolator.EASE_BOTH)));
    }

    public static final Animation exitNode(Node node) {
        return node == null ? null : opacity(node, 1, 0);
    }

    public static final Animation enterNode(Node node) {
        return node == null ? null : opacity(node, 0, 1);
    }


    public static void titledPaneDisclosure(TitledPane titledPane, SVGGlyph arrow) {
        JFXAnimationTimer timer = new JFXAnimationTimer(
                new JFXKeyFrame(Duration.millis(1160),
                        JFXKeyValue.builder()
                                .setTargetSupplier(() -> arrow.rotateProperty())
                                .setEndValueSupplier(() -> titledPane.isExpanded() ? 180 : 0)
                                .setInterpolator(Interpolator.EASE_BOTH).build()
                )
        );
        titledPane.expandedProperty().addListener(observable -> {
            if (timer.isRunning()) {
                timer.reverseAndContinue();
            } else {
                timer.start();
            }
        });
    }

}
