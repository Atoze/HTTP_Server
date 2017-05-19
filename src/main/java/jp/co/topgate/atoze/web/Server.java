package jp.co.topgate.atoze.web;

import jp.co.topgate.atoze.web.app.forum.ForumAppHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * HTTPリクエストに応じた処理を行います.
 *
 * @author atoze
 */
public class Server extends Thread {
    private final Socket socket;
    private final int PORT;
    private static final String HOST_NAME = "localhost";
    static final String ROOT_DIRECTORY = "./src/main/resources";

    public Server(Socket socket, int PORT) {
        this.PORT = PORT;
        this.socket = socket;
    }

    /**
     * HTTPリクエストに応じた処理を行います.
     */

    @Override
    public void run() {
        System.out.println("\nRequest incoming..." + Thread.currentThread().getName());

        try {
            InputStream input = this.socket.getInputStream();
            HTTPRequest httpRequest = new HTTPRequest();
            httpRequest.readRequest(input, "localhost:" + PORT);
            System.out.println(httpRequest.getRequestHeader());
            System.out.println(httpRequest.getRequestText());
            System.out.println(httpRequest.getRequestBodyFile());

            OutputStream output = this.socket.getOutputStream();

            String filePath = httpRequest.getFilePath();
            if (filePath.startsWith("/program/board/")) {
                ForumAppHandler forum = new ForumAppHandler();
                forum.setRequest(httpRequest);
                forum.handle(httpRequest.getMethod());
                forum.response(output);

            } else {
                StaticHandler request2 = new StaticHandler();
                //StaticHandler request2 = new StaticHandler(httpRequest.getFilePath(), HOST_NAME + ":" + PORT);
                request2.setRequest(httpRequest);
                request2.response(output);
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
