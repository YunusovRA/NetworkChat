package ru.netology.chat.server;

import ru.netology.chat.config.Settings;

public class ServerMain {
    public static void main(String[] args) {
        Settings settings = new Settings("settings.txt");
        ChatServer server = new ChatServer(settings.getServerPort());
        server.start();
    }
}
