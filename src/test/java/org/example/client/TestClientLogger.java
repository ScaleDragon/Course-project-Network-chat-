package org.example.client;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestClientLogger {

    @Test
    public void testLogMessage() {
        String filename = "clientLogTest.txt";
        ClientLogger clientLogger = new ClientLogger(filename);
        String message = "user message";
        clientLogger.logMessage(message);

        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/clientLogTest.txt"))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.contains(message)) {
                    assertEquals("user message", message);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
