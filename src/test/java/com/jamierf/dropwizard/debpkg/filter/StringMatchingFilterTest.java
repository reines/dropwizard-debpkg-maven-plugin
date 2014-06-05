package com.jamierf.dropwizard.debpkg.filter;

import com.jamierf.dropwizard.debpkg.filter.StringMatchingFilter;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StringMatchingFilterTest {

    private static boolean match(String a, String b) {
        return new StringMatchingFilter(a).apply(b);
    }

    @Test
    public void testSimpleMatch() {
        assertTrue(match("test", "test"));
    }

    @Test
    public void testNotMatch() {
        assertFalse(match("test", "hello"));
    }

    @Test
    public void testCaseSensitive() {
        assertFalse(match("teSt", "test"));
    }
}
