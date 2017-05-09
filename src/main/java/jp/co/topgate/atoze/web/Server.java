package jp.co.topgate.atoze.web;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.Arrays;

/**
 * HTTPリクエストに応じた処理を行います.
 *
 * @author atoze
 */
public class Server extends Thread {
    private final Socket socket;
    final int PORT;
    private final String HOST_NAME = "localhost";
    protected static final String ROOT_DIRECTORY = "./src/main/resources";

    public Server(Socket socket, int PORT) {
        this.PORT = PORT;
        this.socket = socket;
    }

    /**
     * HTTPリクエストに応じた処理を行います.
     */
    public void run() {
        try {
            InputStream in = this.socket.getInputStream();
            OutputStream out = this.socket.getOutputStream();

            HTTPRequest httpRequest = new HTTPRequest();
            httpRequest.readRequest(in, "localhost:" + PORT);
            System.out.println(httpRequest.request.getFilePath());

            if (httpRequest.request.getFilePath().startsWith("/program/board/")) {
                System.out.println("DynamicMode");
                ForumAppHandler request3 = new ForumAppHandler();
                request3.request(httpRequest);
                if (httpRequest.request.getMethod().equals("POST")) {
                    if ("削除".equals(URLDecoder.decode(httpRequest.getParameter("button"), "UTF-8"))) {
                        request3.deleteThread(1);
                        request3.response(out);
                    } else {
                        request3.newThread();
                        System.out.println(request3.request.getMessageBody());
                        System.out.println(Arrays.toString(request3.request.getMessageFile()));
                        request3.response(out);
                    }
                } else {
                    request3.response(out);
                }

            } else {
                StaticHandler request2 = new StaticHandler(httpRequest.request.getFilePath(), HOST_NAME + ":" + PORT);
                request2.request(httpRequest);
                request2.response(out);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Disconnected" + Thread.currentThread().getName());
        }
    }
}
