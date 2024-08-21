package org.example.Server;

import org.example.Client.ClientLogger;
import org.junit.jupiter.api.Test;

public class TestServerLogger {
    @Test
    public void testLogMessage(){
        ClientLogger serverLogger = new ClientLogger("serverLogTest.txt");
        String message = "servermessage";
        serverLogger.logMessage(message);
    }
}
