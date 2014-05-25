package com.jamierf.dropwizard.filter;

import com.google.common.base.Predicate;

public class StringMatchingFilter implements Predicate<String> {

    private final String expected;

    public StringMatchingFilter(String expected) {
        this.expected = expected;
    }

    @Override
    public boolean apply(String actual) {
        return actual != null && expected.equals(actual);
    }
}
