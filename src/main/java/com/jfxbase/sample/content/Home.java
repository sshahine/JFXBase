package com.jfxbase.sample.content;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.effects.JFXDepthManager;
import com.jfxbase.base.IFXNodeBuilder;
import com.jfxbase.base.forms.bindings.CustomBidirectionalBinding;
import com.jfxbase.base.forms.bindings.IPropertyConverter;
import com.jfxbase.services.FXSwitcher;
import javafx.animation.Animation;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Home implements IFXNodeBuilder {
    List<Slide> slides = Arrays.asList(new Slide("Slide 1"), new Slide("Slide 2"));
    IntegerProperty activeIndex = new SimpleIntegerProperty();

    // can be replaced by adding a weak listener to activeIndex prop
    StackPane content;

    public Home() {
        // switch content on index changes
        activeIndex.addListener(observable ->
                FXSwitcher.getInstance().switchContent(content, slides.get(activeIndex.get())));
    }

    @Override
    public List<String> getCSSFiles() {
        return Collections.singletonList(getClass().getResource("css/main.css").toExternalForm());
    }

    @Override
    public Animation animateEntrance(Node root) {
        // can be execute at the end of the build or here
        FXSwitcher.getInstance().switchContent(content, slides.get(activeIndex.get()));
        return null;
    }

    @Override
    public Node build() {
        // content
        content = new StackPane();
        HBox.setHgrow(content, Priority.ALWAYS);

        // sideList
        ListView<Slide> sideList = new ListView<>();
        sideList.setCellFactory(param -> new ListCell<>() {
            Label label = new Label();
            StackPane graphic = new StackPane(label);

            @Override
            protected void updateItem(Slide item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    setText(null);
                    label.setText(item.getText());
                    graphic.getStyleClass().add("cell-graphic");
                    setGraphic(graphic);
                }
            }
        });
        sideList.getItems().addAll(slides);
        CustomBidirectionalBinding<Number, Number> binding = new CustomBidirectionalBinding<>(
                activeIndex,
                number -> activeIndex.setValue(number),
                sideList.getSelectionModel().selectedIndexProperty(),
                selectedIndex -> sideList.getSelectionModel().select(selectedIndex.intValue()),
                new IPropertyConverter<>() {
                    @Override
                    public Number to(Number number) {
                        return number;
                    }

                    @Override
                    public Number from(Number number) {
                        return number;
                    }
                }
        );
        binding.bindBi();

        HBox app = new HBox(sideList, content);
        VBox.setVgrow(app, Priority.ALWAYS);

        // header
        JFXButton next = new JFXButton("NEXT");
        next.setOnAction(action ->
                activeIndex.set((activeIndex.get() + 1) % slides.size()));
        JFXButton pre = new JFXButton("PREVIOUS");
        pre.setOnAction(action ->
                activeIndex.set((activeIndex.get() == 0 ?
                        slides.size() - 1
                        : activeIndex.get() - 1) % slides.size()));
        HBox header = new HBox(pre, next);

        header.getStyleClass().addAll("header", "indigo-500");
        JFXDepthManager.setDepth(header, 2);
        return new VBox(header, app);
    }
}
