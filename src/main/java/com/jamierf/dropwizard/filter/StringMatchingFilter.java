package com.jamierf.dropwizard.filter;

import com.google.common.base.Predicate;

public class StringMatchingFilter implements Predicate<String> {

    private final String expected;

    public StringMatchingFilter(final String expected) {
        this.expected = expected;
    }

    @Override
    public boolean apply(final String actual) {
        return actual != null && expected.equals(actual);
    }
}
