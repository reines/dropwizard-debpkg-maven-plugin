package com.jamierf.dropwizard.template;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public interface Templater {
    void execute(Reader input, Writer output, Object parameters) throws IOException;
}
