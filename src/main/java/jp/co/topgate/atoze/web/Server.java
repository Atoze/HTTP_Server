package jp.co.topgate.atoze.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
            System.out.println(httpRequest.getRequestHeader());

            String filePath = httpRequest.request.getFilePath();
            if (filePath.startsWith("/program/board/")) {
                System.out.println("DynamicMode");
                ForumAppHandler request3 = new ForumAppHandler();
                request3.request(httpRequest);
                if (httpRequest.request.getMethod().equals("POST")) {
                    if (filePath.endsWith("search")) {
                        request3.findThread(URLDecoder.decode(httpRequest.getParameter("search"), "UTF-8"));
                    } else if (filePath.endsWith("delete")) {
                        int id = Integer.parseInt(httpRequest.getParameter("threadID"));
                        request3.deleteThread(id);
                    } else {
                        request3.newThread();
                        System.out.println(request3.request.getMessageBody());
                        System.out.println(Arrays.toString(request3.request.getMessageFile()));
                    }
                } else {
                    request3.GETThread();
                }
                request3.response(out);

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
