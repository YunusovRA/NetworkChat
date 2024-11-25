package ru.netology.chat.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {
    private static final Logger logger = LoggerFactory.getLogger(ChatServer.class);
    private final int port;
    private final ConcurrentHashMap<String, ClientHandler> clients = new ConcurrentHashMap<>();
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private ServerSocket serverSocket;

    public ChatServer(int port) {
        this.port = port;
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            logger.info("Server started on port: {}", port);

            while (!serverSocket.isClosed()) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                executorService.execute(clientHandler);
            }
        } catch (IOException e) {
            logger.error("Error starting server: ", e);
        }
    }

    public void broadcast(String message, String sender) {
        logger.info("Broadcasting message from {}: {}", sender, message);
        clients.forEach((username, client) -> {
            if (!username.equals(sender)) {
                client.sendMessage(sender + ": " + message);
            }
        });
    }

    public void addClient(String username, ClientHandler handler) {
        clients.put(username, handler);
        broadcast(username + " joined the chat", "SERVER");
        logger.info("New client connected: {}", username);
    }

    public void removeClient(String username) {
        clients.remove(username);
        broadcast(username + " left the chat", "SERVER");
        logger.info("Client disconnected: {}", username);
    }

    public boolean isUsernameTaken(String username) {
        return clients.containsKey(username);
    }

    public void stop() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            executorService.shutdown();
        } catch (IOException e) {
            logger.error("Error stopping server: ", e);
        }
    }
}
