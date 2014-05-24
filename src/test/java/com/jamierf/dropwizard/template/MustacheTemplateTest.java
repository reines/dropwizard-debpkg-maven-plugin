package com.jamierf.dropwizard.template;

import com.google.common.collect.ImmutableMap;
import com.jamierf.dropwizard.template.mustache.MustacheTemplater;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MustacheTemplateTest {

    private Templater templater;

    @Before
    public void setUp() {
        templater = new MustacheTemplater();
    }

    @Test
    public void testNullTemplate() throws IOException {
        final String result = templater.execute(null, "test", Collections.emptyMap());
        assertNull(result);
    }

    @Test
    public void testEmptyTemplate() throws IOException {
        final String result = templater.execute("", "test", Collections.emptyMap());
        assertEquals("", result);
    }

    @Test
    public void testBasicText() throws IOException {
        final String result = templater.execute("hello world", "test", Collections.emptySet());
        assertEquals("hello world", result);
    }

    @Test
    public void testSimpleTemplate() throws IOException {
        final String result = templater.execute("hello {{location}}", "test", ImmutableMap.of(
                "location", "world"
        ));
        assertEquals("hello world", result);
    }

    @Test
    public void testConditionalTemplate() throws IOException {
        final String result = templater.execute("hello{{#location}} {{location}}{{/location}}", "test", ImmutableMap.of(
                "location", "world"
        ));
        assertEquals("hello world", result);
    }

    @Test(expected = MissingParameterException.class)
    public void testMissingParameter() throws IOException {
        templater.execute("hello {{location}}", "test", Collections.emptySet());
    }
}
