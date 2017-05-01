package jp.co.topgate.atoze.web;

import java.io.*;
import java.net.Socket;

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
            HTTPRequestLine request = new HTTPRequestLine(in, HOST_NAME + ":" + PORT);
            //System.out.print(request.getFilePath());
            switch ("GET") {
                case "GET":
                    StaticHandler request2 = new StaticHandler(request.getFilePath());
                    request2.request(in, HOST_NAME + ":" + PORT);
                    request2.response(out);
                    break;

                default:
                    break;
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
