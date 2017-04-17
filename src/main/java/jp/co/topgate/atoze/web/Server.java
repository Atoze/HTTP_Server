package jp.co.topgate.atoze.web;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by atoze on 2017/04/12.
 */

public class Server {
    Status status;

    public void startServer() {
        System.out.println("Starting up HTTP server...");
        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            while (true) {
                this.serverProcess(serverSocket);
            }
        } catch (IOException e) {

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

            new ServerHandler (in, out);

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {

                //status.setStatusCode();
            }
        }
    }
}

