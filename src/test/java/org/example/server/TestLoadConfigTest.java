package org.example.server;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestLoadConfigTest {
    @Test
    public void testGetLoadConfigPort() {
        int result= LoadConfig.getloadConfigPort("src/main/resources/settings.txt");
        assertEquals(10888,result);
    }
}
