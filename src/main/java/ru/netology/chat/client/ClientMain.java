package ru.netology.chat.client;

import ru.netology.chat.config.Settings;

public class ClientMain {
    public static void main(String[] args) {
        Settings settings = new Settings("settings.txt");
        ChatClient client = new ChatClient(settings.getServerHost(), settings.getServerPort());
        client.start();
    }
}
