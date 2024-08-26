package org.example.client;

import java.io.*;
import java.net.*;
import java.util.*;

import static org.example.client.LoadConfig.getloadConfigPort;

public class Client {
    private static final String SERVER_ADDRESS;
    private static final int SERVER_PORT;
    private static final ClientLogger clientLogger = new ClientLogger("client_log.txt");

    static {
        //метод установка порта и адреса для подключения к серверу через файл настроек ( settings.txt);
        SERVER_PORT = getloadConfigPort("src/main/resources/settings.txt");
        SERVER_ADDRESS = "127.0.01";
    }

    public static void main(String[] args) throws Exception {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            System.out.println("Connected to the chat server");
            clientLogger.logMessage("Connected to the chat server");

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            Scanner scanner = new Scanner(System.in);
            String name = null;
/*
 подключения к серверу клиент запрашивает у пользователя ввод имени и отправляет его на сервер.
 Сервер проверяет имя на уникальность. Если имя принимается, клиент получает подтверждение и начинает
 обрабатывать ввод сообщений от пользователя для отправки на сервер.
 */
            while (true) {
                String serverMessage = reader.readLine();
                if (serverMessage.equals("Create a name for the chat")) {
                    System.out.print("Enter your name: ");
                    name = scanner.nextLine();
                    writer.println(name);
                } else if (serverMessage.startsWith("Name accepted ")) {
                    System.out.println("Welcome " + name + "! You can start typing messages!");
                    clientLogger.logMessage("Welcome" + name + "! You can start typing messages!");
                    break;
                }
            }
/*
отдельный поток для чтения и отправки сообщений.
Это позволяет клиенту одновременно слушать сервер и читать ввод пользователя.
 */
            Thread sendMessageThread = new Thread(() -> {
                while (true) {
                    String message = scanner.nextLine();
                    if (message.toLowerCase().equals("/exit")) {
                        writer.println(message);
                        System.exit(0);
                    }
                    writer.println(message);
                }
            });
            sendMessageThread.start();
/*
Основной поток клиента слушает входящие сообщения от сервера и выводит их в консоль.
Это позволяет пользователю видеть сообщения от других пользователей в реальном времени.
 */
            while (true) {
                String response = reader.readLine();
                if (response != null && response.startsWith("Message: ")) {
                    System.out.println(response.substring(8));  // remove the "MESSAGE " prefix
                    clientLogger.logMessage(response.substring(8));
                } else {
                    break;
                }
            }
        }
    }
}

