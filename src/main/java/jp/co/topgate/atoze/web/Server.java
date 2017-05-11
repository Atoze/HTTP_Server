package jp.co.topgate.atoze.web;

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
    protected final int PORT;
    protected static final String HOST_NAME = "localhost";
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
            System.out.println(httpRequest.getRequestHeader());
            System.out.println(httpRequest.getMessageFile());
            System.out.println(httpRequest.getMessageBody());
            String filePath = httpRequest.httpRequestLine.getFilePath();
            if (filePath.startsWith("/program/board/")) {
                ForumAppHandler request3 = new ForumAppHandler();
                request3.request(httpRequest);
                request3.handle();
                request3.response(out);

            } else {
                StaticHandler request2 = new StaticHandler(httpRequest.httpRequestLine.getFilePath(), HOST_NAME + ":" + PORT);
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

    public int getPORT() {
        return PORT;
    }

    public static String getHOST_NAME() {
        return HOST_NAME;
    }

    public static String getRootDirectory() {
        return ROOT_DIRECTORY;
    }
}
