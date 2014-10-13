package com.jamierf.dropwizard.debpkg.stub;

import java.io.IOException;

public class TestJarProjectStub extends TestProjectStub {
    public TestJarProjectStub() throws IOException {
        super ("com.jamierf", "test", "1.0", "jar");
    }
}
