package jp.co.topgate.atoze.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * HTTPリクエストに応じた処理を行います.
 *
 * @author atoze
 */
public class Server extends Thread {
    //private final Socket socket;
    private ServerSocket serverSocket = null;
    private Socket socket = new Socket();
    final int PORT;
    private final String HOST_NAME = "localhost";
    protected static final String ROOT_DIRECTORY = "./src/main/resources";

    public Server(int PORT) throws IOException {
        this.PORT = PORT;
        serverSocket = new ServerSocket(PORT);
    }

    /**
     * HTTPリクエストに応じた処理を行います.
     */
    public void run() {
        try {
            while (true) {
                socket = serverSocket.accept();
                InputStream in = this.socket.getInputStream();
                OutputStream out = this.socket.getOutputStream();

                HTTPRequest request = new HTTPRequest();
                request.readRequest(in, this.HOST_NAME + ":" + this.PORT);
                System.out.println("Request incoming...");
                //System.out.println(request.getRequestHeader());

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
                if (socket != null) {
                    socket.close();
                    System.out.println("Disconnected");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void startServer() {
        this.start();
    }

    public boolean stopServer() throws IOException {
        boolean result = false;
        if (socket == null || socket.isClosed()) {
            serverSocket.close();
            result = true;
        }
        return result;
    }

    public void endServer() throws IOException {
        if (socket != null) {
            socket.close();
        }
        serverSocket.close();
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

