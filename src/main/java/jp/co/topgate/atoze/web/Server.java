package jp.co.topgate.atoze.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Server
 *
 */
public class Server {
    final int PORT = 8080;
    private final String hostName = "localhost";

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

    private void serverProcess(ServerSocket serverSocket)  {
        Socket socket = null;
        try {
            socket = serverSocket.accept();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Request incoming...");
        try
                (
                        InputStream in = socket.getInputStream();
                        OutputStream out = socket.getOutputStream()
                ) {

            HTTPRequest request = new HTTPRequest();
            request.readRequestText(in, this.hostName + ":" + this.PORT);
            System.out.println(request.getHeaderText());

            ResponseHandler responseHandler = new ResponseHandler(this.hostName, this.PORT);
            responseHandler.responseOutput(request, out);

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

