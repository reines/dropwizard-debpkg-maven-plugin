package com.jamierf.dropwizard.debpkg.template;

import com.google.common.collect.ImmutableMap;
import com.google.common.io.Resources;
import com.jamierf.dropwizard.debpkg.template.mustache.MustacheTemplater;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MustacheTemplateTest {

    private static String getTemplate(String path) throws IOException {
        return Resources.toString(MustacheTemplateTest.class.getResource(path), StandardCharsets.UTF_8);
    }

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
        final String template = getTemplate("text.mustache");
        final String result = templater.execute(template, "test", Collections.emptySet());
        assertEquals("hello world", result);
    }

    @Test
    public void testSimpleTemplate() throws IOException {
        final String template = getTemplate("simple.mustache");
        final String result = templater.execute(template, "test", ImmutableMap.of(
                "location", "world"
        ));
        assertEquals("hello world", result);
    }

    @Test
    public void testConditionalTemplate() throws IOException {
        final String template = getTemplate("conditional.mustache");
        final String result = templater.execute(template, "test", ImmutableMap.of(
                "location", "world"
        ));
        assertEquals("hello world", result);
    }

    @Test(expected = MissingParameterException.class)
    public void testMissingParameter() throws IOException {
        final String template = getTemplate("simple.mustache");
        templater.execute(template, "test", Collections.emptySet());
    }
}
