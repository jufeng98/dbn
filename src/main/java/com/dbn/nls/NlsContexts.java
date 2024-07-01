package com.dbn.nls;

import com.intellij.openapi.util.NlsContext;
import org.jetbrains.annotations.Nls;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

public class NlsContexts {

    @Nls
    @NlsContext(prefix = "dbn.config")
    @Target({ElementType.TYPE_USE, ElementType.PARAMETER, ElementType.METHOD})
    public @interface DbnConfig { }

    @Nls
    @NlsContext(prefix = "dbn.shared")
    @Target({ElementType.TYPE_USE, ElementType.PARAMETER, ElementType.METHOD})
    public @interface DbnShared { }
}
