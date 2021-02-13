package com.jfxbase.base.forms.bindings;

public interface IPropertyConverter<A, B> {
    B to(A a);

    A from(B b);
}
