package org.example.Server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LoadConfig {
    private static int namePort;

    public LoadConfig() {
    }

    public static int getloadConfigPort(String settings) {
        try (BufferedReader br = new BufferedReader(new FileReader(settings))) {
            String s;
            while ((s = br.readLine()) != null) {
                if (s.contains("=")) {
                    String[] splitString = s.split("=");       // получение порта из файла
                    String sr = splitString[0];
                    namePort = Integer.parseInt(splitString[1]);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return namePort;
    }
}
