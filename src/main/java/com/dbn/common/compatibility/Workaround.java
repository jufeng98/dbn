package com.dbn.common.compatibility;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker annotation for ambiguous blocks of code motivated by api gaps or lack of support
 * To be occasionally reviewed to assess potential api compliant alternatives
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface Workaround {
}
