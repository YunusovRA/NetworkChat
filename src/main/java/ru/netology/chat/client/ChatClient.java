package ru.netology.chat.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {
    private static final Logger logger = LoggerFactory.getLogger(ChatClient.class);
    private final String serverHost;
    private final int serverPort;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private volatile boolean running = true;

    public ChatClient(String serverHost, int serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    public void start() {
        try {
            socket = new Socket(serverHost, serverPort);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Thread receiveThread = new Thread(this::receiveMessages);
            receiveThread.start();

            Scanner scanner = new Scanner(System.in);
            String userInput;

            while (running && (userInput = scanner.nextLine()) != null) {
                if (userInput.equals("/exit")) {
                    out.println(userInput);
                    running = false;
                    break;
                }
                out.println(userInput);
                logger.info("Sent message: {}", userInput);
            }

        } catch (IOException e) {
            logger.error("Error in client: ", e);
        } finally {
            closeConnection();
        }
    }

    private void receiveMessages() {
        try {
            String message;
            while (running && (message = in.readLine()) != null) {
                System.out.println(message);
                logger.info("Received message: {}", message);
            }
        } catch (IOException e) {
            if (running) {
                logger.error("Error receiving message: ", e);
            }
        }
    }

    private void closeConnection() {
        running = false;
        try {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            logger.error("Error closing client connection: ", e);
        }
    }
}
