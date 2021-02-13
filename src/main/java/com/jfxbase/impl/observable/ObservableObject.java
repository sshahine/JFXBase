package com.jfxbase.impl.observable;

import com.jfxbase.base.observable.IObservableObjectImpl;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ObservableObject<L> implements IObservableObjectImpl<L> {

    private CopyOnWriteArrayList<WeakObject<L>> listeners = new CopyOnWriteArrayList<>();

    public ObservableObject() {

    }

    @Override
    public boolean addListener(L observer) {
        return listeners.add(new WeakObject<>(observer));
    }

    @Override
    public boolean removeListener(L observer) {
        return listeners.remove(new WeakObject<>(observer));
    }

    @Override
    public void fireEvent(Consumer<L> listenerConsumer) {
        ArrayList<WeakObject<L>> toBeRemoved = new ArrayList<>();
        for (Iterator<WeakObject<L>> itr = listeners.iterator(); itr.hasNext(); ) {
            WeakObject<L> ref = itr.next();
            try {
                // notify
                L listener = ref.get();
                if (listener != null) {
                    listenerConsumer.accept(listener);
                } else {
                    toBeRemoved.add(ref);
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
                toBeRemoved.add(ref);
            }
        }
        // remove null / invalid references
        listeners.removeAll(toBeRemoved);
    }

    @Override
    public Collection<L> listeners() {
        return Collections.unmodifiableCollection(
                listeners.stream().map(Reference::get).collect(Collectors.toList()));
    }

    private static class WeakObject<T> extends WeakReference<T> {
        private WeakObject(T referent) {
            super(referent);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof WeakObject)) {
                return false;
            }
            return ((WeakObject) obj).get() == this.get();
        }
    }
}
