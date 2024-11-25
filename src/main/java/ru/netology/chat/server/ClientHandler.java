package ru.netology.chat.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(ClientHandler.class);
    private final Socket clientSocket;
    private final ChatServer server;
    private PrintWriter out;
    private BufferedReader in;
    private String username;

    public ClientHandler(Socket socket, ChatServer server) {
        this.clientSocket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            while (true) {
                out.println("Enter your username:");
                username = in.readLine();
                if (username == null || username.trim().isEmpty()) {
                    continue;
                }
                if (server.isUsernameTaken(username)) {
                    out.println("Username already taken. Please choose another one.");
                    continue;
                }
                break;
            }

            server.addClient(username, this);

            String message;
            while ((message = in.readLine()) != null) {
                if (message.equals("/exit")) {
                    break;
                }
                server.broadcast(message, username);
            }
        } catch (IOException e) {
            logger.error("Error handling client {}: ", username, e);
        } finally {
            closeConnection();
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    private void closeConnection() {
        if (username != null) {
            server.removeClient(username);
        }
        try {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
            }
        } catch (IOException e) {
            logger.error("Error closing client connection: ", e);
        }
    }
}
