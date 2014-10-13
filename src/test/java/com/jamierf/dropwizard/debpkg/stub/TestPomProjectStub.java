package com.jamierf.dropwizard.debpkg.stub;

import java.io.IOException;

public class TestPomProjectStub extends TestProjectStub {
    public TestPomProjectStub() throws IOException {
        super("com.jamierf", "test", "1.0", "pom");
    }
}
