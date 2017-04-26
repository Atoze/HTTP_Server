package jp.co.topgate.atoze.web;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * HTTPサーバーを起動します.
 */
class Main {
    final static int PORT = 8080;

    public static void main(String[] args){
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                System.out.println("Starting up HTTP server...at PORT:" + PORT);
                Socket socket = serverSocket.accept();
                new Server(socket, PORT).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}