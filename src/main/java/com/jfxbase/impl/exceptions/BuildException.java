package com.jfxbase.impl.exceptions;

import com.jfxbase.base.IFXNodeBuilder;

public class BuildException extends Exception {

    public BuildException(Throwable cause, IFXNodeBuilder builder) {
        super(String.format("Error in building : \"%s\"", builder), cause);
    }
}
