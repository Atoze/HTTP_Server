package jp.co.topgate.atoze.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Server
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

    private void serverProcess(ServerSocket serverSocket) {
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

            if (request.getMethod() != null) {
                switch (request.getMethod()) {
                    case "GET":
                        int statusCode = checkStatusCode(request, file);
                        runGETResponse(statusCode, file, out);
                        break;

                    default:
                        setError(405, out);
                }
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

    private int checkStatusCode(HTTPRequest request, File file) throws IOException {
        String host = request.getHeaderParam("HOST");
        if (host == null || request.getMethod() == null || !host.startsWith(this.hostName + ":" + PORT)) {
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

