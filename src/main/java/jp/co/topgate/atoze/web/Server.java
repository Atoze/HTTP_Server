package jp.co.topgate.atoze.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by atoze on 2017/04/12.
 */

public class Server {
    //private ExecutorService service = Executors.newCachedThreadPool();

    public void startServer() {
        System.out.println("Starting up HTTP server...");
        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            while (true) {
                this.serverProcess(serverSocket);
            }
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    //Request受信
    private void serverProcess(ServerSocket serverSocket) throws IOException {
        Socket socket = serverSocket.accept();
        //socket.setSoTimeout(1000);
        //this.service.execute(() -> {
        System.out.println("Request incoming...");
        try
                (
                        InputStream in = socket.getInputStream();
                        OutputStream out = socket.getOutputStream();
                ) {
            ServerHandler server = new ServerHandler(in, out);
            //out.close();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }
        //});
    }
}

