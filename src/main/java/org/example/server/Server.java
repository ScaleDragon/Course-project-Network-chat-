package org.example.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.example.server.LoadConfig.getloadConfigPort;


public class Server {
    private static final int PORT;
    protected static Map<String, PrintWriter> clients = new HashMap<>();
    private static ExecutorService pool = Executors.newCachedThreadPool();
    /*
    класс для создания лог-файла и логировния сообщений
     */
    private static ServerLogger logger = new ServerLogger("server_log.txt");

    static {
        //метод Установка порта для подключения клиентов через файл настроек ( settings.txt);
        PORT = getloadConfigPort("src/main/resources/settings.txt");
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Server is running on port " + PORT);
        logger.logMessage("Server is running on port " + PORT);
        /*
        Сервер запускается, создавая объект ServerSocket, который слушает указанный порт.
        В бесконечном цикле сервер ожидает подключения клиентов. Каждый раз, когда клиент подключается,
        сервер принимает соединение, создавая новый объект Socket для клиента. Затем для каждого клиента создаётся
        новый поток, в котором будет выполняться обработка сообщений этого клиента. Это обеспечивается методом pool.execute()
         */
        try (ServerSocket listener = new ServerSocket(PORT);) {
            while (true) {
                Socket clientSocket = listener.accept();
                /*
                Каждый клиент обрабатывается в отдельном потоке.
                Прием и отправка сообщений также обрабатывается в этом потоке.
                 */
                pool.execute(new Runnable() {
                    @Override
                    public void run() {
                        String name = null;
                        try (Scanner in = new Scanner(clientSocket.getInputStream()); PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
                            name = creationName(in, out);

                            out.println("Name accepted " + name);
                            logger.logMessage("Name accepted " + name);
                            /*
                            добавление клиента в коллекцию
                             */
                            clients.put(name, out);
                            String welcomeMessage = "Message: " + name + " has joined";
                            broadcastMessage(welcomeMessage);
                            logger.logMessage("Message: " + name + " has joined");

                            String message;
                            sendingMessages(name, in);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            if (name != null) {
                                clients.remove(name);
                                String leaveMessage = "Message: " + name + " has left";
                                broadcastMessage(leaveMessage);
                                logger.logMessage("Message: " + name + " has left");
                            }
                            try {
                                clientSocket.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /*
    отправка сообщений всем пользователям из списка
     */
    protected static void broadcastMessage(String message) {
        for (PrintWriter writer : clients.values()) {
            writer.println(message);
        }
    }

    /*
     Создание имени при подключении нового клиента,
     если у него нет имени или такое имя уже было добавлено
     */
    protected static String creationName(Scanner scanner, PrintWriter out) {
        String name = null;
        while (name == null || name.isEmpty() || clients.containsKey(name)) {
            out.println("Create a name for the chat");
            logger.logMessage("Create a name for the chat");
            name = scanner.nextLine();
        }
        return name;
    }

    /*
    Отправка сообщений
     */
    protected static void sendingMessages(String name, Scanner in) {
        String message;
        while (in.hasNextLine()) {
            message = in.nextLine();
            if (message.toLowerCase().startsWith("/exit")) {
                break;
            }
            String formattedMessage = "Message: " + name + ": " + message;
            broadcastMessage(formattedMessage);
            logger.logMessage("Message: " + name + ": " + message);
        }

    }
}
