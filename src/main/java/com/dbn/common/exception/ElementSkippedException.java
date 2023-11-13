package com.dbn.common.exception;

public class ElementSkippedException extends RuntimeException{
    public static final ElementSkippedException INSTANCE = new ElementSkippedException();
}
