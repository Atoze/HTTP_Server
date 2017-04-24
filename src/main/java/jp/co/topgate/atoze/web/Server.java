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

    private void serverProcess(ServerSocket serverSocket) throws IOException {
        Socket socket = null;
        try {
            socket = serverSocket.accept();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try
                (
                        InputStream in = socket.getInputStream();
                        OutputStream out = socket.getOutputStream()
                ) {

            HTTPRequest request = new HTTPRequest();
            request.readRequest(in, this.hostName + ":" + this.PORT);
            System.out.println("Request incoming...");
            System.out.println(request.getRequestHeader());

            String filePath = "." + request.getFilePath();
            File file = new File(filePath);

            if ("GET".equals(request.getMethod())) {
                int statusCode = checkStatusCode(request, file);
                runGETResponse(statusCode, file, out);

                setError(405, out);
            } else {
                setError(400, out);
            }

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

    /**
     * HTTPリクエストに応じてステータスコードを設定します.
     *
     * @param request　HTTPリクエスト
     * @param file 要求されたファイルパス
     * @return ステータスコード
     */
    private int checkStatusCode(HTTPRequest request, File file) throws IOException {
        String host = request.getHeaderParam("HOST");
        if (host == null || !host.startsWith(this.hostName + ":" + PORT)) {
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

    private void runGETResponse(int statusCode, File file, OutputStream out) throws IOException {
        HTTPResponse response = new HTTPResponse();
        Status status = new Status();
        status.setStatus(statusCode);
        if (statusCode == 200) {
            response.addResponseHeader("Content-Type", ContentTypeUtil.getContentType(file.toString()));
            response.setResponseBody(file);
            response.writeTo(out, status);
            return;
        }
        this.setError(statusCode, out);
    }

    /**
     * エラー発生時のステータスコードに合わせたページを設定、またはテンプレートを作成します.
     * 設置したホームディレクトリの "ステータスコード".html を参照します.
     * 存在しない場合は、テンプレートを送信します.
     *
     * @param statusCode　ステータスコード
     * @param out 書き出し先
     */
    private void setError(int statusCode, OutputStream out) throws IOException {
        HTTPResponse response = new HTTPResponse();
        Status status = new Status();
        status.setStatus(statusCode);
        File errorFile = new File(statusCode + ".html");
        if (errorFile.exists() && errorFile.isFile() && errorFile.canRead()) {
            response.setResponseBody(errorFile);
        } else {
            response.addResponseHeader("Content-Type", ContentTypeUtil.getContentType(".html"));
            response.setResponseBody("<html><head><title>" + status.getStatus() + "</title></head><body><h1>" +
                    status.getStatus() + "</h1></body></html>");
        }
        response.writeTo(out, status);
    }
}

