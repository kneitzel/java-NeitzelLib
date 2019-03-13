package de.kneitzel;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class TestBaseTests extends TestBase {

    private static Logger log = LoggerFactory.getLogger(TestBaseTests.class);

    @Test
    public void propertiesLoadedTest() {
        Assert.assertEquals("Test", prop.getProperty("tests.testbase.testvalue"));
    }
}
