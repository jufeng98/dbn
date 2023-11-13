package com.dbn.common.util;

import com.dbn.common.util.Strings;
import junit.framework.TestCase;

public class StringsTest extends TestCase {

    public void testTrim1() {
        StringBuilder builder = new StringBuilder();

        Strings.trim(builder);
        assertEquals("", builder.toString());
    }

    public void testTrim2() {
        StringBuilder builder = new StringBuilder("\n\t\n  \n   \n\t  ");

        Strings.trim(builder);
        assertEquals("", builder.toString());
    }

    public void testTrim3() {
        StringBuilder builder = new StringBuilder("test \n   \n\t");

        Strings.trim(builder);
        assertEquals("test", builder.toString());
    }

    public void testTrim4() {
        StringBuilder builder = new StringBuilder("\n\t\n test");

        Strings.trim(builder);
        assertEquals("test", builder.toString());
    }

    public void testTrim5() {
        StringBuilder builder = new StringBuilder("\n\t\n test \n   \n\t");

        Strings.trim(builder);
        assertEquals("test", builder.toString());
    }

}