package jp.co.topgate.atoze.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by atoze on 2017/04/12.
 */

public class Server {
    final int PORT = 8080;

    public void start() {
        System.out.println("Starting up HTTP server...");
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            while (true) {
                this.serverProcess(serverSocket);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //Request受信
    private void serverProcess(ServerSocket serverSocket) throws IOException {
        Socket socket = serverSocket.accept();
        System.out.println("Request incoming...");
        try
                (
                        InputStream in = socket.getInputStream();
                        OutputStream out = socket.getOutputStream()
                ) {

            new ServerHandler (in, out, PORT);

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

