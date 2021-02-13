package com.jfxbase.base.observable;

public interface IObservableObject<L> {
    boolean addListener(L listener);

    boolean removeListener(L listener);
}
