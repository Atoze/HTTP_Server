package jp.co.topgate.atoze.web;

import java.io.File;
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
    final int PORT;
    private final String HOST_NAME = "localhost";
    protected static final String ROOT_DIRECTORY = "./src/main/resources";

    public Server(Socket socket, int PORT) {
        this.PORT = PORT;
        this.socket = socket;
        System.out.println("Connected");
    }

    /**
     * HTTPリクエストに応じた処理を行います.
     */
    public void run() {
        try {
            InputStream in = this.socket.getInputStream();
            OutputStream out = this.socket.getOutputStream();

            HTTPRequest request = new HTTPRequest();
            request.readRequest(in, this.HOST_NAME + ":" + this.PORT);
            System.out.println("Request incoming...");
            System.out.println(request.getRequestHeader());

            File file = new File(ROOT_DIRECTORY, request.getFilePath());
            HTTPHandler httpHandler = new HTTPHandler();

            int statusCode = checkStatusCode(request, file);
            switch (request.getMethod()) {
                case "GET":
                    httpHandler.handlerGET(statusCode, file, out);
                    break;

                default:
                    httpHandler.handlerError(statusCode, out);
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
            System.out.println("Disconnected");
        }
    }

    /**
     * HTTPリクエストに応じてステータスコードを設定します.
     *
     * @param request 　HTTPリクエスト
     * @param file    要求されたファイルパス
     * @return ステータスコード
     */
    private int checkStatusCode(HTTPRequest request, File file) throws IOException {
        String host = request.getHeaderParam("HOST");
        if (request.getMethod() == null || host == null || !host.startsWith(this.HOST_NAME + ":" + PORT)) {
            return 400;
        }
        if (!file.exists() || !file.isFile()) {
            return 404;
        }
        if (!file.canRead()) {
            return 403;
        }
        return 200;
    }

}

