package jp.co.topgate.atoze.web;

import jp.co.topgate.atoze.web.app.forum.ForumAppHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

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

    private final static Set<String> SUPPORTED_PROTOCOL_VERSION = new HashSet<>();

    static {
        SUPPORTED_PROTOCOL_VERSION.add("1.0");
        SUPPORTED_PROTOCOL_VERSION.add("1.1");
    }

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
            InputStream input = socket.getInputStream();
            HTTPRequest httpRequest = HTTPRequestParser.parse(input, HOST_NAME + ":" + PORT);
            System.out.println(httpRequest.getHeader());
            System.out.println(httpRequest.getBodyText());
            System.out.println(httpRequest.getBodyFile());
            String filePath = httpRequest.getFilePath();

            OutputStream output = socket.getOutputStream();
            if (checkIfSupportsProtocolVer(httpRequest.getProtocolVer(), httpRequest.getHeaderParam("Host"), output)) {
                HTTPHandler handler;
                if (filePath.startsWith(URLPattern.PROGRAM_BOARD.getURL())) {
                    handler = new ForumAppHandler(httpRequest);
                } else {
                    handler = new StaticHandler(httpRequest);
                }
                handler.writeResponse(output);
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

    private boolean checkIfSupportsProtocolVer(String protocolVer, String requestHost, OutputStream output) {
        if (!SUPPORTED_PROTOCOL_VERSION.contains(protocolVer)) {
            HTTPResponse response = new HTTPResponse(505);
            response.writeTo(output);
            return false;
        }
        if (protocolVer.equals("1.1") && !requestHost.equals(HOST_NAME + ":" + PORT)) {
            HTTPResponse response = new HTTPResponse(400);
            response.writeTo(output);
            return false;
        }
        return true;
    }

}