package org.example.Server;

import org.junit.jupiter.api.Test;

import java.io.PrintWriter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TestServer {
    @Test
    public void TestBroadcastMessage() {
        PrintWriter client1 = mock(PrintWriter.class);
        PrintWriter client2 = mock(PrintWriter.class);
        Server.clients.put("client1", client1);
        Server.clients.put("client2", client2);

        Server.broadcastMessage("Test message");

        verify(client1).println("Test message");
        verify(client2).println("Test message");
    }
}

