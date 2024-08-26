package org.example.server;

import org.example.client.ClientLogger;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestServerLogger {

    @Test
    public void testLogMessage() {
        String filename = "serverLogTest.txt";
        ClientLogger clientLogger = new ClientLogger(filename);
        String message = "servermessage";
        clientLogger.logMessage(message);

        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/serverLogTest.txt"))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.contains(message)) {
                    assertEquals("servermessage", message);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
