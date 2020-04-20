package com.samples.reactor;

import org.testng.annotations.Test;

public class SampleAppTest {
    private final SampleApp app = new SampleApp();

    @Test
    public void testApp() {
        app.run("how", "long", "can", "we", "go", "on", "like", "this", "?");
    }

    @Test
    public void testDelayedValues() {
        app.fluxOne(4, 31);
    }

}
