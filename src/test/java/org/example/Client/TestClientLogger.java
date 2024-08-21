package org.example.Client;

import org.junit.jupiter.api.Test;

public class TestClientLogger {
    @Test
    public void testLogMessage(){
        ClientLogger clientLogger = new ClientLogger("ClientLogTest.txt");
        String message = "user message";
        clientLogger.logMessage(message);
    }
}
