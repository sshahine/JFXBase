package com.jfxbase.base.observable;

import java.util.Collection;
import java.util.function.Consumer;

public interface IObservableObjectImpl<L> extends IObservableObject<L>{

    void fireEvent(Consumer<L> listenerConsumer);

    Collection<L> listeners();
}
